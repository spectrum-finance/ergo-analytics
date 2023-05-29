package fi.spectrum.api.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.WithContext
import tofu.logging.derivation.loggable

@derive(pureconfigReader, loggable)
case class PriceTrackingConfig(
  spfTokenId: String,
  spfMintingAddress: String,
  initAirdropOutput: String,
  totalSpfSupply: Long
)

object PriceTrackingConfig extends WithContext.Companion[PriceTrackingConfig]
