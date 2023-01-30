package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

import scala.math.BigDecimal.RoundingMode

@derive(encoder, decoder, loggable)
case class PoolSlippage(
  slippagePercent: BigDecimal
) {
  def scale(value: Int): PoolSlippage = PoolSlippage(slippagePercent.setScale(value, RoundingMode.HALF_UP))
}

object PoolSlippage {
  implicit val schema: Schema[PoolSlippage] = Schema.derived

  val defaultScale = 3

  def zero: PoolSlippage = PoolSlippage(0)
}
