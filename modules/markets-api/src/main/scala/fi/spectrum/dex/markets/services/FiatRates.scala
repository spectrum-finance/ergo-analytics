package fi.spectrum.dex.markets.services

import cats.effect.{Clock, Ref, Sync}
import cats.{FlatMap, Monad}
import derevo.derive
import fi.spectrum.core.common.{AssetClass, FiatUnits}
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.constants.ErgoAssetClass
import fi.spectrum.dex.markets.currencies.UsdUnits
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.{Logging, Logs}
import tofu.syntax.foption._
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait FiatRates[F[_]] {

  def rateOf(asset: AssetClass, units: FiatUnits): F[Option[BigDecimal]]
}

object FiatRates {

  val ErgUsdPoolNft: TokenId =
    TokenId.unsafeFromString("011d3364de07e5a26f0c4eef0852cddb387039a921b7154ef3cab22c6eda887f")

  def make[I[_]: FlatMap, F[_]: Monad: Clock](implicit
    logs: Logs[I, F],
    cache: Ref[F, Option[BigDecimal]]
  ): I[FiatRates[F]] =
    logs.forService[FiatRates[F]].map(implicit __ => new Tracing[F] attach new ErgoOraclesRateSource[F](cache))

  final class ErgoOraclesRateSource[F[_]: Monad: Logging](
    cache: Ref[F, Option[BigDecimal]]
  ) extends FiatRates[F] {

    def rateOf(asset: AssetClass, units: FiatUnits): F[Option[BigDecimal]] =
      if (asset == ErgoAssetClass && units == UsdUnits) cache.get
      else noneF[F, BigDecimal]
  }

  final class Tracing[F[_]: Logging: Monad] extends FiatRates[Mid[F, *]] {

    def rateOf(asset: AssetClass, units: FiatUnits): Mid[F, Option[BigDecimal]] =
      for {
        _ <- trace"Memo read $asset $units"
        r <- _
        _ <- trace"Memo read completed $r"
      } yield r
  }
}
