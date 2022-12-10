package fi.spectrum.parser.lock.v1

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.order.{Operation, Order, OrderType}
import fi.spectrum.core.domain.transaction.SConstant.{IntConstant, SigmaPropConstant}
import fi.spectrum.core.domain.transaction.{Output, RegisterId}
import fi.spectrum.core.domain.{AssetAmount, ErgoTreeTemplate}
import fi.spectrum.parser.lock.LockOrderParser
import fi.spectrum.parser.templates.Lock._
import sigmastate.Values

class LockParser extends LockOrderParser[V1] {

  def lock(box: Output, tree: Values.ErgoTree): Option[Order.Lock[V1]] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == lockV1,
        for {
          deadline <- box.additionalRegisters.get(RegisterId.R4).collect { case IntConstant(d) => d }
          redeemer <- box.additionalRegisters.get(RegisterId.R5).collect { case SigmaPropConstant(pk) => pk }
          amount   <- box.assets.headOption.map(AssetAmount.fromBoxAsset)
        } yield LockV1(
          box,
          deadline,
          amount,
          PublicKeyRedeemer(redeemer),
          Version.make.v1,
          OrderType.make.lock,
          Operation.make.lock
        ),
        none
      )
      .merge
}

object LockParser {
  implicit def ev: LockOrderParser[V1] = new LockParser
}
