package fi.spectrum.core.domain

import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.HexStringSpec

object TypeConstraints {
  type HexString = String Refined HexStringSpec
}
