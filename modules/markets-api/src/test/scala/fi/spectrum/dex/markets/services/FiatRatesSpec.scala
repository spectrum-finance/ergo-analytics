package fi.spectrum.dex.markets.services

import cats.effect.{IO, Ref}
import cats.effect.unsafe.implicits.global
import cats.syntax.option._
import fi.spectrum.core.domain.configs.NetworkConfig
import fi.spectrum.core.domain.constants.ErgoAssetClass
import fi.spectrum.dex.markets.currencies.UsdUnits
import fi.spectrum.dex.markets.services.FiatRates.ErgoOraclesRateSource
import org.scalatest.matchers.should
import org.scalatest.propspec.AnyPropSpec
import sttp.client3.UriContext
import tofu.WithContext
import tofu.logging.{Logging, Logs}

class FiatRatesSpec extends AnyPropSpec with should.Matchers {

  implicit val conf: WithContext[IO, NetworkConfig] =
    WithContext.const(NetworkConfig(uri"https://api.ergoplatform.com", uri"http://127.0.0.1:9053"))

  implicit val logs: Logs[IO, IO] = Logs.sync[IO, IO]

  property("Get ERG/USD rate") {
    type S[A] = fs2.Stream[IO, A]
    val test = for {
      ref                                   <- Ref.of[IO, Option[BigDecimal]](none)
      implicit0(logging: Logging[IO])       <- logs.forService[Unit]
      rates = new ErgoOraclesRateSource[IO](ref)
      ergUsdRate <- rates.rateOf(ErgoAssetClass, UsdUnits)
      _          <- IO.delay(println(ergUsdRate))
    } yield ()
    test.unsafeRunSync()
  }
}
