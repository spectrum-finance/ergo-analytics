package fi.spectrum.api.services

import cats.Monad
import cats.effect.Ref
import cats.effect.kernel.Sync
import fi.spectrum.api.currencies.UsdUnits
import fi.spectrum.api.models.FiatUnits
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.foption._
import tofu.syntax.logging._
import tofu.syntax.monadic._

trait ErgRate[F[_]] {

  def rateOf(units: FiatUnits): F[Option[BigDecimal]]

  def update: F[Option[BigDecimal]]
}

object ErgRate {

  implicit def representableK: RepresentableK[ErgRate] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Sync, F[_]: Sync](implicit
    network: Network[F],
    logs: Logs[I, F]
  ): I[ErgRate[F]] =
    for {
      implicit0(logging: Logging[F]) <- logs.forService[ErgRate[F]]
      cache                          <- Ref.in[I, F, Option[BigDecimal]](None)
    } yield new Tracing[F] attach new Live[F](cache)

  final private[services] class Live[F[_]: Monad: Logging](
    cache: Ref[F, Option[BigDecimal]]
  )(implicit network: Network[F])
    extends ErgRate[F] {

    def rateOf(units: FiatUnits): F[Option[BigDecimal]] =
      if (units == UsdUnits) cache.get else noneF[F, BigDecimal]

    def update: F[Option[BigDecimal]] = for {
      rate <- network.getErgPriceCMC
      _    <- cache.set(rate)
    } yield rate
  }

  final class Tracing[F[_]: Logging: Monad] extends ErgRate[Mid[F, *]] {

    def rateOf(units: FiatUnits): Mid[F, Option[BigDecimal]] =
      for {
        _ <- trace"rateOf(${units.currency})"
        r <- _
        _ <- trace"rateOf(${units.currency}) -> $r"
      } yield r

    def update: Mid[F, Option[BigDecimal]] = info"It's time to update erg rate!" >> _

  }
}
