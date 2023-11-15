package fi.spectrum.api.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.core.domain.order.PoolId
import tofu.WithContext
import tofu.logging.derivation.loggable

@derive(pureconfigReader, loggable)
case class PriceTrackingConfig(
  spfPoolId: PoolId,
  spfTokenId: String,
  spfMintingAddress: String,
  initAirdropTx: String,
  totalSpfSupply: Long
)

object PriceTrackingConfig extends WithContext.Companion[PriceTrackingConfig]
