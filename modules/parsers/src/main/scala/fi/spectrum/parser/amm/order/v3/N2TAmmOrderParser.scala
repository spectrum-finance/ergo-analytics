package fi.spectrum.parser.amm.order.v3

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.SPF
import fi.spectrum.core.domain.order.Order.Deposit.DepositV3
import fi.spectrum.core.domain.order.Order.Redeem.RedeemV3
import fi.spectrum.core.domain.order.Order.Swap.SwapV3
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.OrderType.AMM
import fi.spectrum.core.domain.order.Redeemer.ErgoTreeRedeemer
import fi.spectrum.core.domain.analytics.Version.V3
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.N2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.N2T._
import sigmastate.Values

class N2TAmmOrderParser(spf: TokenId) extends AmmOrderParser[V3, N2T] {

  def swap(box: Output, tree: Values.ErgoTree): Option[Swap[V3, AMM]] = {
    val template = ErgoTreeTemplate.fromBytes(tree.template)
    if (template == swapSellV3) swapSell(box, tree)
    else if (template == swapBuyV3) swapBuy(box, tree)
    else none
  }

  private def swapSell(box: Output, tree: Values.ErgoTree): Option[Swap[V3, AMM]] =
    for {
      poolId       <- tree.constants.parseBytea(11).map(PoolId.fromBytes)
      maxMinerFee  <- tree.constants.parseLong(32)
      baseAmount   <- tree.constants.parseLong(24).map(AssetAmount.native)
      outId        <- tree.constants.parseBytea(13).map(TokenId.fromBytes)
      minOutAmount <- tree.constants.parseLong(14)
      outAmount = AssetAmount(outId, minOutAmount)
      dexFeePerTokenNum   <- tree.constants.parseLong(17)
      dexFeePerTokenDenom <- tree.constants.parseLong(18)
      redeemer            <- tree.constants.parseBytea(12).map(SErgoTree.fromBytes)
      params = SwapParams(baseAmount, outAmount, dexFeePerTokenNum, dexFeePerTokenDenom)
      reserveExFee <- tree.constants.parseLong(1)
    } yield SwapV3(
      box,
      SPF(0, spf),
      poolId, ErgoTreeRedeemer(redeemer),
      params,
      maxMinerFee,
      reserveExFee,
      Version.make.v3,
      OrderType.make.amm,
      Operation.make.swap
    )

  private def swapBuy(box: Output, tree: Values.ErgoTree): Option[Swap[V3, AMM]] =
    for {
      poolId         <- tree.constants.parseBytea(13).map(PoolId.fromBytes)
      maxMinerFee    <- tree.constants.parseLong(25)
      spectrumId     <- tree.constants.parseBytea(1).map(TokenId.fromBytes)
      inAmount       <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
      minQuoteAmount <- tree.constants.parseLong(15)
      outAmount = AssetAmount.native(minQuoteAmount)
      dexFeePerTokenDenom   <- tree.constants.parseLong(10)
      dexFeePerTokenNumDiff <- tree.constants.parseLong(9)
      dexFeePerTokenNum = dexFeePerTokenDenom - dexFeePerTokenNumDiff
      reserveExFee <- tree.constants.parseLong(8)
      redeemer     <- tree.constants.parseBytea(14).map(SErgoTree.fromBytes)
      baseAmount = if (spectrumId == inAmount.tokenId) inAmount - reserveExFee else inAmount
      params     = SwapParams(baseAmount, outAmount, dexFeePerTokenNum, dexFeePerTokenDenom)
    } yield SwapV3(
      box,
      SPF(0, spf),
      poolId, ErgoTreeRedeemer(redeemer),
      params,
      maxMinerFee,
      reserveExFee,
      Version.make.v3,
      OrderType.make.amm,
      Operation.make.swap
    )

  def deposit(box: Output, tree: Values.ErgoTree): Option[Deposit[V3, AMM]] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == depositV3,
        for {
          poolId      <- tree.constants.parseBytea(14).map(PoolId.fromBytes)
          maxMinerFee <- tree.constants.parseLong(23)
          dexFeeFromY <- tree.constants.parseBoolean(10)
          inX         <- tree.constants.parseLong(1).map(AssetAmount.native)
          inY         <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          dexFee      <- tree.constants.parseLong(11)
          redeemer    <- tree.constants.parseBytea(15).map(SErgoTree.fromBytes)
          params = DepositParams(inX, if (dexFeeFromY) inY - dexFee else inY)
        } yield DepositV3(
          box,
          SPF(dexFee, spf),
          poolId, ErgoTreeRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.make.v3,
          OrderType.make.amm,
          Operation.make.deposit
        ),
        none
      )
      .merge

  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem[V3, AMM]] =
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
          SPF(dexFee.amount, spf),
          poolId,
          ErgoTreeRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.make.v3,
          OrderType.make.amm,
          Operation.make.redeem
        ),
        none
      )
      .merge
}

object N2TAmmOrderParser {
  def n2tV3(spf: TokenId): AmmOrderParser[V3, N2T] = new N2TAmmOrderParser(spf)
}