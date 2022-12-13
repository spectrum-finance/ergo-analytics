package fi.spectrum.core.domain.order

import derevo.cats.eqv
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.AssetAmount
import tofu.logging.derivation.{loggable, show}

@derive(encoder, decoder, loggable, show, eqv)
final case class SwapParams(
  base: AssetAmount,
  minQuote: AssetAmount,
  dexFeePerTokenNum: Long,
  dexFeePerTokenDenom: Long
)
