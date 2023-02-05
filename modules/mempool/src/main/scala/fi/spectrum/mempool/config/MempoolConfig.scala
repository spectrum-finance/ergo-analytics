package fi.spectrum.mempool.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import tofu.WithContext

import scala.concurrent.duration.FiniteDuration

@derive(pureconfigReader)
final case class MempoolConfig(ttl: FiniteDuration)

object MempoolConfig extends WithContext.Companion[MempoolConfig]