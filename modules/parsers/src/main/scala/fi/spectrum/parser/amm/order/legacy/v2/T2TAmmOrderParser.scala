package fi.spectrum.parser.amm.order.legacy.v2

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Order.Deposit.DepositLegacyV2
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.OrderType.AMM
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.analytics.Version.LegacyV2
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, ErgoTreeTemplate, PubKey}
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.T2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.T2T._
import sigmastate.Values

class T2TAmmOrderParser extends AmmOrderParser[LegacyV2, T2T] {
  def swap(box: Output, tree: Values.ErgoTree): Option[Swap[LegacyV2, AMM]] = none

  def deposit(box: Output, tree: Values.ErgoTree): Option[Deposit[LegacyV2, AMM]] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == depositLegacyV2,
        for {
          poolId   <- tree.constants.parseBytea(9).map(PoolId.fromBytes)
          inX      <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          inY      <- box.assets.lift(1).map(a => AssetAmount(a.tokenId, a.amount))
          dexFee   <- tree.constants.parseLong(11)
          redeemer <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = DepositParams(inX, inY)
        } yield DepositLegacyV2(
          box,
          ERG(dexFee),
          poolId,
          PublicKeyRedeemer(redeemer),
          params,
          Version.make.legacyV2,
          OrderType.make.amm,
          Operation.make.deposit
        ),
        none
      )
      .merge

  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem[LegacyV2, AMM]] = none
}

object T2TAmmOrderParser {
  implicit def t2tLegacyV2: AmmOrderParser[LegacyV2, T2T] = new T2TAmmOrderParser
}
