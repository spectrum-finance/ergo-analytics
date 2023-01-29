package fi.spectrum.core.domain

object constants {

  val ErgoAssetId: TokenId = TokenId(HexString.fromBytes(Array.fill(32)(0: Byte)))

  val ErgoGenesisBox: BoxId = BoxId(ErgoAssetId.unwrapped)

  val ErgoEmissionAmount: Long = 93409065000000000L

  val ErgoAssetTicker: String = "ERG"

  val ErgoAssetDescription: String = "Ergo Blockchain native token"

  val ErgoAssetDecimals = 9

  def ergoBaseOutput: ErgoTreeTemplate =
    ErgoTreeTemplate.unsafeFromString("d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304")

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

  object cfmm {
    val TotalEmissionLP = 0x7fffffffffffffffL
    val FeeDenominator  = 1000
  }
}
