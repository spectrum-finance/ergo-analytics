package fi.spectrum.core.syntax

import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.Order
import cats.syntax.show._

object ProcessedOps {

  implicit final class ProcessedOps(val value: Processed.Any) extends AnyVal {

    def metric: String = {
      val state = value.state.status.show.toLowerCase
      value.order match {
        case _: Order.Deposit.AmmDeposit => s"amm.deposit.$state"
        case _: Order.Deposit.LmDeposit  => s"lm.deposit.$state"
        case _: Order.Redeem.AmmRedeem   => s"amm.redeem.$state"
        case _: Order.Redeem.LmRedeem    => s"lm.redeem.$state"
        case _: Order.Swap               => s"amm.swap.$state"
        case _: Order.Lock               => s"lq.lock.$state"
        case _: Order.Compound           => s"lm.compound.$state"
      }
    }
  }

}
