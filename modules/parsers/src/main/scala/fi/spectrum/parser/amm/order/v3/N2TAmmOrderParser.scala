package fi.spectrum.parser.amm.order.v3

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.SPF
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit._
import fi.spectrum.core.domain.order.Order.Redeem.AmmRedeem.RedeemV3
import fi.spectrum.core.domain.order.Order.Swap.SwapV3
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.Redeemer.ErgoTreeRedeemer
import fi.spectrum.core.domain.analytics.Version.V3
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.N2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.N2T._
import sigmastate.Values

final class N2TAmmOrderParser(implicit spf: TokenId) extends AmmOrderParser[V3, N2T] {

  def swap(box: Output, tree: Values.ErgoTree): Option[Swap] = {
    val template = ErgoTreeTemplate.fromBytes(tree.template)
    if (template == swapSellV3) swapSell(box, tree)
    else if (template == swapBuyV3) swapBuy(box, tree)
    else none
  }

  private def swapSell(box: Output, tree: Values.ErgoTree): Option[Swap] =
    for {
      baseAmount   <- tree.constants.parseLong(3).map(AssetAmount.native)
      poolId       <- tree.constants.parseBytea(13).map(PoolId.fromBytes)
      maxMinerFee  <- tree.constants.parseLong(31)
      outId        <- tree.constants.parseBytea(15).map(TokenId.fromBytes)
      minOutAmount <- tree.constants.parseLong(16)
      outAmount = AssetAmount(outId, minOutAmount)
      dexFeePerTokenDenom   <- tree.constants.parseLong(1)
      dexFeePerTokenNumDiff <- tree.constants.parseLong(2)
      dexFeePerTokenNum = dexFeePerTokenDenom - dexFeePerTokenNumDiff
      redeemer     <- tree.constants.parseBytea(14).map(SErgoTree.fromBytes)
      reserveExFee <- tree.constants.parseLong(11)
      params = SwapParams(baseAmount, outAmount, dexFeePerTokenNum, dexFeePerTokenDenom)
    } yield SwapV3(
      box,
      poolId,
      ErgoTreeRedeemer(redeemer),
      params,
      maxMinerFee,
      reserveExFee,
      Version.V3
    )

  private def swapBuy(box: Output, tree: Values.ErgoTree): Option[Swap] =
    for {
      poolId         <- tree.constants.parseBytea(11).map(PoolId.fromBytes)
      maxMinerFee    <- tree.constants.parseLong(24)
      inAmount       <- tree.constants.parseLong(1)
      inAssetAmount  <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount)).map(_.withAmount(inAmount))
      minQuoteAmount <- tree.constants.parseLong(13)
      outAmount = AssetAmount.native(minQuoteAmount)
      dexFeePerTokenDenom <- tree.constants.parseLong(8)
      dexFeePerTokenNum   <- tree.constants.parseLong(9)
      reserveExFee        <- tree.constants.parseLong(7)
      redeemer            <- tree.constants.parseBytea(12).map(SErgoTree.fromBytes)
      baseAmount = inAssetAmount
      params     = SwapParams(baseAmount, outAmount, dexFeePerTokenNum, dexFeePerTokenDenom)
    } yield SwapV3(
      box,
      poolId,
      ErgoTreeRedeemer(redeemer),
      params,
      maxMinerFee,
      reserveExFee,
      Version.V3
    )

  def deposit(box: Output, tree: Values.ErgoTree): Option[AmmDeposit] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == depositV3,
        for {
          poolId         <- tree.constants.parseBytea(12).map(PoolId.fromBytes)
          maxMinerFee    <- tree.constants.parseLong(21)
          selfX          <- tree.constants.parseLong(1).map(AssetAmount.native)
          selfYAmount    <- tree.constants.parseLong(9)
          selfYBoxAmount <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          selfY = selfYBoxAmount.withAmount(selfYAmount)
          dexFee <- if (selfY.tokenId == spf) (selfYBoxAmount.amount - selfY.amount).some
                    else box.assets.find(_.tokenId == spf).map(_.amount)
          redeemer <- tree.constants.parseBytea(13).map(SErgoTree.fromBytes)
          params = AmmDepositParams(selfX, selfY)
        } yield AmmDepositV3(
          box,
          SPF(dexFee),
          poolId,
          ErgoTreeRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.V3
        ),
        none
      )
      .merge

  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == redeemV3,
        for {
          poolId      <- tree.constants.parseBytea(11).map(PoolId.fromBytes)
          maxMinerFee <- tree.constants.parseLong(16)
          inLP        <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          dexFee      <- box.assets.lift(1).map(a => AssetAmount(a.tokenId, a.amount))
          redeemer    <- tree.constants.parseBytea(12).map(SErgoTree.fromBytes)
          params = RedeemParams(inLP)
        } yield RedeemV3(
          box,
          SPF(dexFee.amount),
          poolId,
          ErgoTreeRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.V3
        ),
        none
      )
      .merge
}

object N2TAmmOrderParser {
  implicit def n2tV3(implicit spf: TokenId): AmmOrderParser[V3, N2T] = new N2TAmmOrderParser
}
