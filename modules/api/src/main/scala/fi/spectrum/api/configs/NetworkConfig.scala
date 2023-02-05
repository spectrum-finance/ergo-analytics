package fi.spectrum.api.configs

import derevo.derive
import derevo.pureconfig.pureconfigReader
import sttp.model.Uri
import tofu.WithContext
import tofu.logging.derivation.loggable
import fi.spectrum.core.common.instances._

import scala.concurrent.duration.FiniteDuration

@derive(pureconfigReader, loggable)
final case class NetworkConfig(
  cmcUrl: Uri,
  cmcApiKey: String,
  cmcRequestTime: FiniteDuration,
  cmcLimitRetries: Int,
  cmcRetryDelay: FiniteDuration,
  verifiedTokenListUrl: Uri,
  verifiedTokenListRequestTime: FiniteDuration,
  verifiedTokenLimitRetries: Int,
  verifiedTokenRetryDelay: FiniteDuration,
  explorerUri: Uri,
  explorerLimitRetries: Int,
  explorerRetryDelay: FiniteDuration,
  mempoolUri: Uri,
  mempoolLimitRetries: Int,
  mempoolRetryDelay: FiniteDuration
)

object NetworkConfig extends WithContext.Companion[NetworkConfig]
