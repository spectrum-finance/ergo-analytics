package fi.spectrum.api.v1.models.amm

import derevo.derive
import tofu.logging.derivation.loggable

@derive(loggable)
case class CMCYFInfo(
  provider: String,
  providerLogo: String,
  providerUrl: String,
  links: List[MediaLink],
  pools: List[PoolCMCInfo]
)

@derive(loggable)
case class MediaLink(title: String, link: String)

@derive(loggable)
case class PoolCMCInfo(
  name: String,
  pair: String,
  pairLink: String,
  logo: String,
  poolRewards: List[String],
  apr: BigDecimal,
  totalStaked: Long
)
