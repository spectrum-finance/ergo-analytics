package fi.spectrum.parser.amm.order.legacy.v3

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.LegacyV3
import fi.spectrum.core.domain.order.Fee._
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit._
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, ErgoTreeTemplate, PubKey}
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.N2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.N2T._
import sigmastate.Values
import sigmastate.Values.ErgoTree

class N2TAmmOrderParser extends AmmOrderParser[LegacyV3, N2T] {

  def swap(box: Output, tree: Values.ErgoTree): Option[Swap] = none

  def deposit(box: Output, tree: ErgoTree): Option[AmmDeposit] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == depositLegacyV3,
        for {
          poolId <- tree.constants.parseBytea(12).map(PoolId.fromBytes)
          maxMinerFee <- tree.constants.parseLong(22)
          inX <- tree.constants.parseLong(16).map(AssetAmount.native)
          inY <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          dexFee <- tree.constants.parseLong(15)
          redeemer <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = AmmDepositParams(inX, inY)
        } yield AmmDepositLegacyV3(
          box,
          ERG(dexFee),
          poolId,
          PublicKeyRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.LegacyV3
        ),
        none
      )
      .merge

  def redeem(box: Output, tree: ErgoTree): Option[Redeem] = none
}

object N2TAmmOrderParser {
  implicit def n2tLegacyV3: AmmOrderParser[LegacyV3, N2T] = new N2TAmmOrderParser
}