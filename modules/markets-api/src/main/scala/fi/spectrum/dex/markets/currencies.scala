package fi.spectrum.dex.markets

import fi.spectrum.core.common.{Currency, FiatUnits}
import fi.spectrum.core.common.types.CurrencyId

object currencies {

  val UsdTicker                 = "USD"
  val UsdDecimals               = 2
  val UsdCurrencyId: CurrencyId = CurrencyId(UsdTicker)
  val UsdCurrency: Currency     = Currency(UsdCurrencyId, UsdDecimals)
  val UsdUnits: FiatUnits       = FiatUnits(UsdCurrency)
}
