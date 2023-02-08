package fi.spectrum.parser.amm.order.legacy.v1

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee._
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit._
import fi.spectrum.core.domain.order.Order.Redeem.RedeemLegacyV1
import fi.spectrum.core.domain.order.Order.Swap.SwapLegacyV1
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.analytics.Version.LegacyV1
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, ErgoTreeTemplate, PubKey, TokenId}
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.N2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.N2T._
import sigmastate.Values
import sigmastate.Values.ErgoTree

class N2TAmmOrderParser extends AmmOrderParser[LegacyV1, N2T] {

  def swap(box: Output, tree: Values.ErgoTree): Option[Swap] = {
    val template = ErgoTreeTemplate.fromBytes(tree.template)
    if (template == swapSellLegacyV1) swapSellV0(box, tree)
    else if (template == swapBuyLegacyV1) swapBuyV0(box, tree)
    else None
  }

  private def swapSellV0(box: Output, tree: ErgoTree): Option[Swap] =
    for {
      poolId       <- tree.constants.parseBytea(8).map(PoolId.fromBytes)
      baseAmount   <- tree.constants.parseLong(2).map(AssetAmount.native)
      outId        <- tree.constants.parseBytea(9).map(TokenId.fromBytes)
      minOutAmount <- tree.constants.parseLong(10)
      outAmount = AssetAmount(outId, minOutAmount)
      dexFeePerTokenNum   <- tree.constants.parseLong(11)
      dexFeePerTokenDenom <- tree.constants.parseLong(12)
      redeemer            <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
      params = SwapParams(
                 baseAmount,
                 outAmount,
                 dexFeePerTokenNum,
                 dexFeePerTokenDenom
               )
    } yield SwapLegacyV1(
      box,
      poolId,
      PublicKeyRedeemer(redeemer),
      params,
      Version.LegacyV1,

    )

  private def swapBuyV0(box: Output, tree: ErgoTree): Option[Swap] =
    for {
      poolId       <- tree.constants.parseBytea(9).map(PoolId.fromBytes)
      inAmount     <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
      minOutAmount <- tree.constants.parseLong(10)
      outAmount = AssetAmount.native(minOutAmount)
      dexFeePerTokenDenom   <- tree.constants.parseLong(5)
      dexFeePerTokenNumDiff <- tree.constants.parseLong(6)
      dexFeePerTokenNum = dexFeePerTokenDenom - dexFeePerTokenNumDiff
      redeemer <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
      params = SwapParams(
                 inAmount,
                 outAmount,
                 dexFeePerTokenNum,
                 dexFeePerTokenDenom
               )
    } yield SwapLegacyV1(
      box,
      poolId,
      PublicKeyRedeemer(redeemer),
      params,
      Version.LegacyV1,

    )

  def deposit(box: Output, tree: Values.ErgoTree): Option[AmmDeposit] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == depositLegacyV1,
        for {
          poolId   <- tree.constants.parseBytea(9).map(PoolId.fromBytes)
          inX      <- tree.constants.parseLong(11).map(AssetAmount.native)
          inY      <- box.assets.lift(1).map(a => AssetAmount(a.tokenId, a.amount))
          dexFee   <- tree.constants.parseLong(11)
          redeemer <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = AmmDepositParams(inX, inY)
        } yield AmmDepositLegacyV1(
          box,
          ERG(dexFee),
          poolId,
          PublicKeyRedeemer(redeemer),
          params,
          Version.LegacyV1,

        ),
        none
      )
      .merge

  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == redeemLegacyV1,
        for {
          poolId   <- tree.constants.parseBytea(11).map(PoolId.fromBytes)
          inLP     <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          dexFee   <- tree.constants.parseLong(12)
          redeemer <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = RedeemParams(inLP)
        } yield RedeemLegacyV1(
          box,
          ERG(dexFee),
          poolId,
          PublicKeyRedeemer(redeemer),
          params,
          Version.LegacyV1,

        ),
        none
      )
      .merge
}

object N2TAmmOrderParser {
  implicit def n2tLegacyV1: AmmOrderParser[LegacyV1, N2T] = new N2TAmmOrderParser
}
