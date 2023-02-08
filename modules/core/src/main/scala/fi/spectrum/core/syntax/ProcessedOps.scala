package fi.spectrum.core.syntax

import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.Order
import cats.syntax.show._

object ProcessedOps {

  implicit final class ProcessedOps(val value: Processed.Any) extends AnyVal {

    def metric: String = {
      val state = value.state.status.show.toLowerCase
      value.order match {
        case _: Order.Deposit        => s"deposit.$state"
        case _: Order.Redeem         => s"redeem.$state"
        case _: Order.Swap           => s"swap.$state"
        case _: Order.Lock           => s"lock.$state"
        case _: Order.Compound => s"compound.$state"
      }
    }
  }

}
