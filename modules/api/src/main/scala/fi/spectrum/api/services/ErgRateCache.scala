package fi.spectrum.api.services

import cats.effect.{Clock, Ref}
import cats.{FlatMap, Monad}
import derevo.derive
import fi.spectrum.api.currencies.UsdUnits
import fi.spectrum.api.models.constants.ErgoAssetClass
import fi.spectrum.api.models.{AssetClass, FiatUnits}
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.{Logging, Logs}
import tofu.syntax.foption._
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait ErgRateCache[F[_]] {

  def rateOf(asset: AssetClass, units: FiatUnits): F[Option[BigDecimal]]
}

object ErgRateCache {

  def make[I[_]: FlatMap, F[_]: Monad: Clock](implicit
    logs: Logs[I, F],
    cache: Ref[F, Option[BigDecimal]]
  ): I[ErgRateCache[F]] =
    logs.forService[ErgRateCache[F]].map(implicit __ => new Tracing[F] attach new ErgoOraclesRateSource[F](cache))

  final class ErgoOraclesRateSource[F[_]: Monad: Logging](
    cache: Ref[F, Option[BigDecimal]]
  ) extends ErgRateCache[F] {

    def rateOf(asset: AssetClass, units: FiatUnits): F[Option[BigDecimal]] =
      if (asset == ErgoAssetClass && units == UsdUnits) cache.get
      else noneF[F, BigDecimal]
  }

  final class Tracing[F[_]: Logging: Monad] extends ErgRateCache[Mid[F, *]] {

    def rateOf(asset: AssetClass, units: FiatUnits): Mid[F, Option[BigDecimal]] =
      for {
        _ <- trace"ErgRateCache($asset, $units)"
        r <- _
        _ <- trace"ErgRateCache($asset, $units) -> $r"
      } yield r
  }
}
