package fi.spectrum.parser.lm.order

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.Order.Compound
import fi.spectrum.core.domain.order.Order.Deposit.LmDeposit
import fi.spectrum.core.domain.transaction.Output
import sigmastate.Values

trait LmOrderParser[+V <: Version] { self =>

  def deposit(box: Output, tree: Values.ErgoTree): Option[LmDeposit]

  def compound(box: Output, tree: Values.ErgoTree): Option[Compound]

  def order(box: Output, tree: Values.ErgoTree): Option[Order] =
    deposit(box, tree) orElse compound(box, tree)

  def or(that: => LmOrderParser[Version]): LmOrderParser[Version] =
    new LmOrderParser[Version] {

      def deposit(box: Output, tree: Values.ErgoTree): Option[LmDeposit] =
        self.deposit(box, tree) orElse that.deposit(box, tree)

      def compound(box: Output, tree: Values.ErgoTree): Option[Compound] =
        self.compound(box, tree) orElse that.compound(box, tree)

      override def order(box: Output, tree: Values.ErgoTree): Option[Order] =
        self.order(box, tree) orElse that.order(box, tree)
    }
}

object LmOrderParser {

  implicit def make(implicit
    v1: LmOrderParser[V1]
  ): LmOrderParser[Version] =
    List[LmOrderParser[Version]](
      v1
    ).reduceLeft(_ or _)
}
