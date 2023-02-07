package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.currencies.{UsdCurrency, UsdUnits}
import fi.spectrum.api.models.FiatUnits
import sttp.tapir.Schema
import tofu.logging.derivation.loggable
import scala.math.BigDecimal.RoundingMode

@derive(encoder, decoder, loggable)
case class TransactionsInfo(numTxs: Int, avgTxValue: BigDecimal, maxTxValue: BigDecimal, units: FiatUnits) {

  def roundAvgValue: TransactionsInfo =
    TransactionsInfo(numTxs, avgTxValue.setScale(0, RoundingMode.DOWN), maxTxValue, units)
}

object TransactionsInfo {

  def empty: TransactionsInfo = TransactionsInfo(0, 0, 0, UsdUnits)

  implicit val schemaTrxInfo: Schema[TransactionsInfo] = Schema
    .derived[TransactionsInfo]
    .modify(_.numTxs)(_.description("Transactions quantity"))
    .modify(_.avgTxValue)(_.description("Average transaction value"))
    .modify(_.maxTxValue)(_.description("Maximum transaction value"))
    .modify(_.units)(_.description("Measuring fiat units"))
    .encodedExample(TransactionsInfo(7067, BigDecimal(368.9), BigDecimal(34000), FiatUnits(UsdCurrency)))
}
