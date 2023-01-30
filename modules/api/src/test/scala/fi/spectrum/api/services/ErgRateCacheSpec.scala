package fi.spectrum.api.services

import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Ref}
import cats.syntax.option._
import fi.spectrum.api.currencies.UsdUnits
import org.scalatest.matchers.should
import org.scalatest.propspec.AnyPropSpec
import tofu.logging.{Logging, Logs}

class ErgRateCacheSpec extends AnyPropSpec with should.Matchers {

  implicit val logs: Logs[IO, IO] = Logs.sync[IO, IO]

  implicit val network = NetworkMock.make[IO]

  property("Get ERG/USD rate") {
    val test = for {
      ref                             <- Ref.of[IO, Option[BigDecimal]](none)
      implicit0(logging: Logging[IO]) <- logs.forService[Unit]
      rates = new ErgRate.Live[IO](ref)
      _          <- rates.update
      ergUsdRate <- rates.rateOf(UsdUnits)
    } yield ergUsdRate shouldEqual Some(BigDecimal(1.12))
    test.unsafeRunSync()
  }
}
