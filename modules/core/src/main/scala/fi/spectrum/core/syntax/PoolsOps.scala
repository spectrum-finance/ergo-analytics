package fi.spectrum.core.syntax

import fi.spectrum.core.domain.pool.Pool

object PoolsOps {

  implicit final class PoolsOps(val value: Pool) extends AnyVal {

    def metric: String =
      value match {
        case _: Pool.AmmPool          => "amm"
        case _: Pool.LmPoolSelfHosted => "lm.self.hosted"
        case _: Pool.LmPoolDefault           => "lm"
      }
  }

}
