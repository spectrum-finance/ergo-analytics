package fi.spectrum.parser.amm.order.legacy.v3

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.LegacyV3
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit._
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, ErgoTreeTemplate, PubKey}
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.T2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.T2T._
import sigmastate.Values

class T2TAmmOrderParser extends AmmOrderParser[LegacyV3, T2T] {

  def swap(box: Output, tree: Values.ErgoTree): Option[Swap] = none

  def deposit(box: Output, tree: Values.ErgoTree): Option[AmmDeposit] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == depositLegacyV3,
        for {
          poolId <- tree.constants.parseBytea(13).map(PoolId.fromBytes)
          maxMinerFee <- tree.constants.parseLong(25)
          inX <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          inY <- box.assets.lift(1).map(a => AssetAmount(a.tokenId, a.amount))
          dexFee <- tree.constants.parseLong(15)
          redeemer <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = AmmDepositParams(inX, inY)
        } yield AmmDepositV1(
          box,
          ERG(dexFee),
          poolId,
          PublicKeyRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.V1
        ),
        none
      )
      .merge

  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem] = none
}

object T2TAmmOrderParser {
  implicit def t2tLegacyV3: AmmOrderParser[LegacyV3, T2T] = new T2TAmmOrderParser
}
