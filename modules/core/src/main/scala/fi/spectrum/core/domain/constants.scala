package fi.spectrum.core.domain

object constants {

  val ErgoAssetId: TokenId = TokenId.fromBytes(Array.fill(32)(0: Byte))

  object t2t {
    val IndexNFT = 0
    val IndexLP  = 1
    val IndexX   = 2
    val IndexY   = 3
  }

  object n2t {
    val IndexNFT = 0
    val IndexLP  = 1
    val IndexY   = 2
  }
}
