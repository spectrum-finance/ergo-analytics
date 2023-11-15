package fi.spectrum.api.v1.endpoints.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

import scala.math.BigDecimal.RoundingMode

@derive(
  encoder(io.circe.derivation.renaming.snakeCase, None),
  decoder,
  loggable
)
final case class TokenSupply(
  mcUsd: BigDecimal,
  fullyDilutedMcUsd: BigDecimal,
  circulatingSupply: BigDecimal,
  totalSupply: Long,
  circulatingSupplyPc: BigDecimal
)

object TokenSupply {

  def apply(
    mcUsd: BigDecimal,
    fullyDilutedMcUsd: BigDecimal,
    circulatingSupply: BigDecimal,
    totalSupply: Long,
    circulatingSupplyPc: BigDecimal
  ): TokenSupply = new TokenSupply(
    mcUsd.setScale(2, RoundingMode.HALF_UP),
    fullyDilutedMcUsd.setScale(2, RoundingMode.HALF_UP),
    circulatingSupply.setScale(2, RoundingMode.HALF_UP),
    totalSupply,
    circulatingSupplyPc.setScale(2, RoundingMode.HALF_UP)
  )

  implicit def schema: Schema[TokenSupply] = Schema.derived
}
