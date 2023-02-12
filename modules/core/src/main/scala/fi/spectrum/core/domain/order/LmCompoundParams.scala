package fi.spectrum.core.domain.order

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{AssetAmount, TokenId}
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class LmCompoundParams(
  vLq: AssetAmount,
  // TMP tokens are empty if the last epoch is compounded. Otherwise, there are some tokens
  tmp: Option[AssetAmount], //todo tests
  bundleKeyId: TokenId
)
