package fi.spectrum.core.domain

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.{HexStringSpec, MatchesRegex}

object TypeConstraints {
  type HexStringType   = String Refined HexStringSpec
  type Base58Spec  = MatchesRegex[W.`"[1-9A-HJ-NP-Za-km-z]+"`.T]
  type AddressType = String Refined Base58Spec
}
