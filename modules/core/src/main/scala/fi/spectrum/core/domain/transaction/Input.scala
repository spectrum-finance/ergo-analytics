package fi.spectrum.core.domain.transaction

import fi.spectrum.core.domain.{BoxId, HexString}

final case class Input(
  boxId: BoxId,
  spendingProof: Option[HexString],
  output: Output
)

object Input