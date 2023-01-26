package fi.spectrum.core.domain.ergo

import derevo.circe.decoder
import derevo.derive
import fi.spectrum.core.domain.BlockId
import tofu.logging.derivation.loggable

@derive(decoder, loggable)
final case class NetworkInfo(
  lastBlockId: BlockId,
  height: Int,
  maxBoxGix: Long,
  maxTxGix: Long,
  params: EpochParams
)
