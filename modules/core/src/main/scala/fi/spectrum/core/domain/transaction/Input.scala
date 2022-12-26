package fi.spectrum.core.domain.transaction

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{BoxId, HexString}
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class Input(
  boxId: BoxId,
  spendingProof: Option[HexString],
  output: Output
)

object Input