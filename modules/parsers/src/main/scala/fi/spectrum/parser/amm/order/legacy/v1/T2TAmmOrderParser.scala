package fi.spectrum.parser.amm.order.legacy.v1

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Order.Deposit.DepositLegacyV1
import fi.spectrum.core.domain.order.Order.Redeem.RedeemLegacyV1
import fi.spectrum.core.domain.order.Order.Swap.SwapLegacyV1
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.OrderType.AMM
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.analytics.Version.LegacyV1
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, ErgoTreeTemplate, PubKey, TokenId}
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.T2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.T2T._
import sigmastate.Values

class T2TAmmOrderParser extends AmmOrderParser[LegacyV1, T2T] {

  def swap(box: Output, tree: Values.ErgoTree): Option[Swap[LegacyV1, AMM]] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == swapLegacyV1,
        for {
          poolId       <- tree.constants.parseBytea(14).map(PoolId.fromBytes)
          inAmount     <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          outId        <- tree.constants.parseBytea(2).map(TokenId.fromBytes)
          minOutAmount <- tree.constants.parseLong(15)
          outAmount = AssetAmount(outId, minOutAmount)
          dexFeePerTokenNum   <- tree.constants.parseLong(16)
          dexFeePerTokenDenom <- tree.constants.parseLong(17)
          redeemer            <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = SwapParams(inAmount, outAmount, dexFeePerTokenNum, dexFeePerTokenDenom)
        } yield SwapLegacyV1(
          box,
          poolId,
          PublicKeyRedeemer(redeemer),
          params,
          Version.LegacyV1,
          OrderType.AMM,
          Operation.Swap
        ),
        none
      )
      .merge

  def deposit(box: Output, tree: Values.ErgoTree): Option[Deposit[LegacyV1, AMM]] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == depositLegacyV1,
        for {
          poolId   <- tree.constants.parseBytea(9).map(PoolId.fromBytes)
          inX      <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          inY      <- box.assets.lift(1).map(a => AssetAmount(a.tokenId, a.amount))
          dexFee   <- tree.constants.parseLong(11)
          redeemer <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = DepositParams(inX, inY)
        } yield DepositLegacyV1(
          box,
          ERG(dexFee),
          poolId,
          PublicKeyRedeemer(redeemer),
          params,
          Version.LegacyV1,
          OrderType.AMM,
          Operation.Deposit
        ),
        none
      )
      .merge

  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem[LegacyV1, AMM]] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == redeemLegacyV1,
        for {
          poolId   <- tree.constants.parseBytea(13).map(PoolId.fromBytes)
          inLP     <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          dexFee   <- tree.constants.parseLong(15)
          redeemer <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = RedeemParams(inLP)
        } yield RedeemLegacyV1(
          box,
          ERG(dexFee),
          poolId,
          PublicKeyRedeemer(redeemer),
          params,
          Version.LegacyV1,
          OrderType.AMM,
          Operation.Redeem
        ),
        none
      )
      .merge
}

object T2TAmmOrderParser {
  implicit def t2tLegacyV1: AmmOrderParser[LegacyV1, T2T] = new T2TAmmOrderParser
}
