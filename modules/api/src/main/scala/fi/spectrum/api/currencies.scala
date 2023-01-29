package fi.spectrum.api

import fi.spectrum.api.models.{Currency, CurrencyId, FiatUnits}

object currencies {

  val UsdTicker                 = "USD"
  val UsdDecimals               = 2
  val UsdCurrencyId: CurrencyId = CurrencyId(UsdTicker)
  val UsdCurrency: Currency     = Currency(UsdCurrencyId, UsdDecimals)
  val UsdUnits: FiatUnits       = FiatUnits(UsdCurrency)
}
