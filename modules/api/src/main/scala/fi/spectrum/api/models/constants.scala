package fi.spectrum.api.models

import fi.spectrum.core.domain.constants.{ErgoAssetDecimals, ErgoAssetId, ErgoAssetTicker => Erg}

object constants {

  val PreGenesisHeight = 0

  val ErgoAssetTicker: Ticker = Ticker(Erg)

  val ErgoAssetClass: AssetClass = AssetClass(ErgoAssetId, Some(ErgoAssetTicker), Some(ErgoAssetDecimals))

  val ErgoUnits: CryptoUnits = CryptoUnits(ErgoAssetClass)
}
