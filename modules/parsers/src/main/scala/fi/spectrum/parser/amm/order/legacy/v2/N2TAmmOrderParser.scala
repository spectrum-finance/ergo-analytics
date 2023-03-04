package fi.spectrum.parser.amm.order.legacy.v2

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.LegacyV2
import fi.spectrum.core.domain.order.Fee._
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit._
import fi.spectrum.core.domain.order.Order.Redeem.AmmRedeem.RedeemLegacyV2
import fi.spectrum.core.domain.order.Order.Swap.SwapLegacyV2
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, ErgoTreeTemplate, PubKey, TokenId}
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.N2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.N2T._
import sigmastate.Values
import sigmastate.Values.ErgoTree

class N2TAmmOrderParser extends AmmOrderParser[LegacyV2, N2T] {

  def swap(box: Output, tree: Values.ErgoTree): Option[Swap] = {
    val template = ErgoTreeTemplate.fromBytes(tree.template)
    if (template == swapSellLegacyV2) swapSell(box, tree)
    else if (template == swapBuyLegacyV2) swapBuy(box, tree)
    else none
  }

  private def swapSell(box: Output, tree: Values.ErgoTree): Option[Swap] =
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
      params = SwapParams(baseAmount, outAmount, dexFeePerTokenNum, dexFeePerTokenDenom)
    } yield SwapLegacyV2(
      box,
      poolId,
      PublicKeyRedeemer(redeemer),
      params,
      maxMinerFee,
      Version.LegacyV2
    )

  private def swapBuy(box: Output, tree: Values.ErgoTree): Option[Swap] =
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
      params = SwapParams(inAmount, outAmount, dexFeePerTokenNum, dexFeePerTokenDenom)
    } yield SwapLegacyV2(
      box,
      poolId,
      PublicKeyRedeemer(redeemer),
      params,
      maxMinerFee,
      Version.LegacyV2
    )

  def deposit(box: Output, tree: ErgoTree): Option[AmmDeposit] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == depositLegacyV2,
        for {
          poolId   <- tree.constants.parseBytea(12).map(PoolId.fromBytes)
          inX      <- tree.constants.parseLong(16).map(AssetAmount.native)
          inY      <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          dexFee   <- tree.constants.parseLong(15)
          redeemer <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = AmmDepositParams(inX, inY)
        } yield AmmDepositLegacyV2(
          box,
          ERG(dexFee),
          poolId,
          PublicKeyRedeemer(redeemer),
          params,
          Version.LegacyV2
        ),
        none
      )
      .merge

  def redeem(box: Output, tree: ErgoTree): Option[Redeem] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == redeemLegacyV2,
        for {
          poolId      <- tree.constants.parseBytea(11).map(PoolId.fromBytes)
          maxMinerFee <- tree.constants.parseLong(16)
          inLP        <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          dexFee      <- tree.constants.parseLong(12)
          redeemer    <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = RedeemParams(inLP)
        } yield RedeemLegacyV2(
          box,
          ERG(dexFee),
          poolId,
          PublicKeyRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.LegacyV2
        ),
        none
      )
      .merge
}

object N2TAmmOrderParser {
  implicit def n2tLegacyV2: AmmOrderParser[LegacyV2, N2T] = new N2TAmmOrderParser
}
