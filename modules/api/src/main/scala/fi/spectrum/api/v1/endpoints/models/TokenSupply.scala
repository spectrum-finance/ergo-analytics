package fi.spectrum.api.v1.endpoints.models

import derevo.circe.{decoder, encoder}
import derevo.derive
import sttp.tapir.Schema
import tofu.logging.derivation.loggable

@derive(
  encoder(io.circe.derivation.renaming.snakeCase, None),
  decoder,
  loggable
)
final case class TokenSupply(
  mcUsd: BigDecimal,
  fullyDilutedMcUsd: BigDecimal,
  circulatingSupply: Long,
  totalSupply: Long,
  circulatingSupplyPc: Long
)

object TokenSupply {

  implicit def schema: Schema[TokenSupply] = Schema.derived
}
