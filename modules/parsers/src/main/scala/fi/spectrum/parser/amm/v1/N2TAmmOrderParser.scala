package fi.spectrum.parser.amm.v1

import cats.syntax.option._
import fi.spectrum.core.domain.order.Fee._
import fi.spectrum.core.domain.order.Order.Deposit.DepositV1
import fi.spectrum.core.domain.order.Order.Redeem.RedeemV1
import fi.spectrum.core.domain.order.Order.Swap.SwapV1
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.OrderType.AMM
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.order.Version.V1
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, ErgoTreeTemplate, PubKey, TokenId}
import fi.spectrum.parser.amm.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.N2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.N2T._
import sigmastate.Values

class N2TAmmOrderParser extends AmmOrderParser[V1, N2T] {



  def swap(box: Output, tree: Values.ErgoTree): Option[Swap[V1, AMM]] = {
    val template = ErgoTreeTemplate.fromBytes(tree.template)
    if (template == swapSellV1) swapSell(box, tree)
    else if (template == swapBuyV1) swapBuy(box, tree)
    else none
  }

  private def swapSell(box: Output, tree: Values.ErgoTree): Option[Swap[V1, AMM]] =
    for {
      poolId       <- tree.constants.parseBytea(8).map(PoolId.fromBytes)
      maxMinerFee  <- tree.constants.parseLong(22)
      baseAmount   <- tree.constants.parseLong(2).map(AssetAmount.native)
      outId        <- tree.constants.parseBytea(9).map(TokenId.fromBytes)
      minOutAmount <- tree.constants.parseLong(10)
      outAmount = AssetAmount(outId, minOutAmount)
      dexFeePerTokenNum   <- tree.constants.parseLong(11)
      dexFeePerTokenDenom <- tree.constants.parseLong(12)
      redeemer            <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
      params = SwapParams(baseAmount, outAmount, dexFeePerTokenNum, dexFeePerTokenDenom, PublicKeyRedeemer(redeemer))
    } yield SwapV1(
      box,
      ERG(0),
      poolId,
      params,
      maxMinerFee,
      Version.make.v1,
      OrderType.make.amm,
      Operation.make.swap
    )

  private def swapBuy(box: Output, tree: Values.ErgoTree): Option[Swap[V1, AMM]] =
    for {
      poolId       <- tree.constants.parseBytea(9).map(PoolId.fromBytes)
      maxMinerFee  <- tree.constants.parseLong(19)
      inAmount     <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
      minOutAmount <- tree.constants.parseLong(10)
      outAmount = AssetAmount.native(minOutAmount)
      dexFeePerTokenDenom   <- tree.constants.parseLong(5)
      dexFeePerTokenNumDiff <- tree.constants.parseLong(6)
      dexFeePerTokenNum = dexFeePerTokenDenom - dexFeePerTokenNumDiff
      redeemer <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
      params = SwapParams(inAmount, outAmount, dexFeePerTokenNum, dexFeePerTokenDenom, PublicKeyRedeemer(redeemer))
    } yield SwapV1(
      box,
      ERG(0),
      poolId,
      params,
      maxMinerFee,
      Version.make.v1,
      OrderType.make.amm,
      Operation.make.swap
    )

  def deposit(box: Output, tree: Values.ErgoTree): Option[Deposit[V1, AMM]] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == depositV1,
        for {
          poolId      <- tree.constants.parseBytea(12).map(PoolId.fromBytes)
          maxMinerFee <- tree.constants.parseLong(22)
          inX         <- tree.constants.parseLong(16).map(AssetAmount.native)
          inY         <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          dexFee      <- tree.constants.parseLong(15)
          redeemer    <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = DepositParams(inX, inY, PublicKeyRedeemer(redeemer))
        } yield DepositV1(
          box,
          ERG(dexFee),
          poolId,
          params,
          maxMinerFee,
          Version.make.v1,
          OrderType.make.amm,
          Operation.make.deposit
        ),
        none
      )
      .merge

  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem[V1, AMM]] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == redeemV1,
        for {
          poolId      <- tree.constants.parseBytea(11).map(PoolId.fromBytes)
          maxMinerFee <- tree.constants.parseLong(16)
          inLP        <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          dexFee      <- tree.constants.parseLong(12)
          redeemer    <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = RedeemParams(inLP, PublicKeyRedeemer(redeemer))
        } yield RedeemV1(
          box,
          ERG(dexFee),
          poolId,
          params,
          maxMinerFee,
          Version.make.v1,
          OrderType.make.amm,
          Operation.make.redeem
        ),
        none
      )
      .merge
}

object N2TAmmOrderParser {
  implicit def ev: AmmOrderParser[V1, N2T] = new N2TAmmOrderParser
}
