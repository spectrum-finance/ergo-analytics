package fi.spectrum.parser.amm.order.v2

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.V2
import fi.spectrum.core.domain.order.Order.Swap.SwapV2
import fi.spectrum.core.domain.order.Order._

import fi.spectrum.core.domain.order.Redeemer.ErgoTreeRedeemer
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.N2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.N2T._
import sigmastate.Values

class N2TAmmOrderParser extends AmmOrderParser[V2, N2T] {

  def swap(box: Output, tree: Values.ErgoTree): Option[Swap] = {
    val template = ErgoTreeTemplate.fromBytes(tree.template)
    if (template == swapSellV2) swapSell(box, tree)
    else if (template == swapBuyV2) swapBuy(box, tree)
    else none
  }

  private def swapSell(box: Output, tree: Values.ErgoTree): Option[Swap] =
    for {
      poolId       <- tree.constants.parseBytea(8).map(PoolId.fromBytes)
      maxMinerFee  <- tree.constants.parseLong(23)
      baseAmount   <- tree.constants.parseLong(18).map(AssetAmount.native)
      outId        <- tree.constants.parseBytea(10).map(TokenId.fromBytes)
      minOutAmount <- tree.constants.parseLong(11)
      outAmount = AssetAmount(outId, minOutAmount)
      dexFeePerTokenNum   <- tree.constants.parseLong(12)
      dexFeePerTokenDenom <- tree.constants.parseLong(13)
      redeemer            <- tree.constants.parseBytea(9).map(SErgoTree.fromBytes)
      params = SwapParams(
                 baseAmount,
                 outAmount,
                 dexFeePerTokenNum,
                 dexFeePerTokenDenom
               )
    } yield SwapV2(
      box,
      poolId,
      ErgoTreeRedeemer(redeemer),
      params,
      maxMinerFee,
      Version.V2
    )

  private def swapBuy(box: Output, tree: Values.ErgoTree): Option[Swap] =
    for {
      poolId       <- tree.constants.parseBytea(9).map(PoolId.fromBytes)
      maxMinerFee  <- tree.constants.parseLong(20)
      inAmount     <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
      minOutAmount <- tree.constants.parseLong(11)
      outAmount = AssetAmount.native(minOutAmount)
      dexFeePerTokenDenom   <- tree.constants.parseLong(5)
      dexFeePerTokenNumDiff <- tree.constants.parseLong(6)
      dexFeePerTokenNum = dexFeePerTokenDenom - dexFeePerTokenNumDiff
      redeemer <- tree.constants.parseBytea(10).map(SErgoTree.fromBytes)
      params = SwapParams(
                 inAmount,
                 outAmount,
                 dexFeePerTokenNum,
                 dexFeePerTokenDenom
               )
    } yield SwapV2(
      box,
      poolId,
      ErgoTreeRedeemer(redeemer),
      params,
      maxMinerFee,
      Version.V2
    )

  def deposit(box: Output, tree: Values.ErgoTree): Option[Deposit] = none

  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem] = none
}

object N2TAmmOrderParser {
  implicit def n2tV2: AmmOrderParser[V2, N2T] = new N2TAmmOrderParser
}
