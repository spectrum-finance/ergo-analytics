package fi.spectrum.core.domain

import eu.timepit.refined.api.Refined

object constants {

  val ErgoAssetId: TokenId =
    TokenId(Refined.unsafeApply(scorex.util.encode.Base16.encode(Array.fill(32)(0: Byte))))
}
