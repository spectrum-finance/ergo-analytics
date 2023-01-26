package fi.spectrum.api.modules

import cats.data.OptionT
import cats.{FlatMap, Functor, Monad}
import fi.spectrum.api.db.models.amm.PoolSnapshot
import fi.spectrum.api.models.constants.{ErgoAssetClass, ErgoUnits}
import fi.spectrum.api.models._
import fi.spectrum.api.services.ErgRateCache
import fi.spectrum.core.domain.constants.ErgoAssetDecimals
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.foption._
import tofu.syntax.logging._
import tofu.syntax.monadic._

import scala.math.BigDecimal.RoundingMode

sealed trait PriceSolverType { type AssetRepr }
trait CryptoSolverType extends PriceSolverType { type AssetRepr = AssetClass }
trait FiatSolverType extends PriceSolverType { type AssetRepr = Currency }

trait PriceSolver[F[_], T <: PriceSolverType] {

  type AssetRepr = T#AssetRepr

  def convert(
    asset: FullAsset,
    target: ValueUnits[AssetRepr],
    knownPools: List[PoolSnapshot]
  ): F[Option[AssetEquiv[AssetRepr]]]
}

object PriceSolver {

  val fiatSolverType   = "Fiat solver"
  val cryptoSolverType = "Crypto solver"

  type CryptoPriceSolver[F[_]] = PriceSolver[F, CryptoSolverType]

  object CryptoPriceSolver {

    implicit def representableK: RepresentableK[CryptoPriceSolver] =
      tofu.higherKind.derived.genRepresentableK

    def make[I[_]: Functor, F[_]: Monad](implicit logs: Logs[I, F]): I[CryptoPriceSolver[F]] =
      logs
        .forService[CryptoPriceSolver[F]]
        .map(implicit l =>
          Mid.attach[CryptoPriceSolver, F](new PriceSolverTracing[F, CryptoSolverType](cryptoSolverType))(
            new CryptoSolver
          )
        )
  }

  type FiatPriceSolver[F[_]] = PriceSolver[F, FiatSolverType]

  object FiatPriceSolver {

    implicit def representableK: RepresentableK[FiatPriceSolver] =
      tofu.higherKind.derived.genRepresentableK

    def make[I[_]: Functor, F[_]: Monad](implicit
      rates: ErgRateCache[F],
      cryptoSolver: CryptoPriceSolver[F],
      logs: Logs[I, F]
    ): I[FiatPriceSolver[F]] =
      logs
        .forService[FiatPriceSolver[F]]
        .map(implicit l =>
          Mid.attach[FiatPriceSolver, F](new PriceSolverTracing[F, FiatSolverType](fiatSolverType))(
            new ViaErgFiatSolver(rates, cryptoSolver)
          )
        )
  }

  final class ViaErgFiatSolver[F[_]: Monad](rates: ErgRateCache[F], cryptoSolver: CryptoPriceSolver[F])
    extends PriceSolver[F, FiatSolverType] {

    def convert(
      asset: FullAsset,
      target: ValueUnits[AssetRepr],
      knownPools: List[PoolSnapshot]
    ): F[Option[AssetEquiv[AssetRepr]]] =
      target match {
        case fiat @ FiatUnits(_) =>
          (for {
            ergEquiv <- OptionT(cryptoSolver.convert(asset, ErgoUnits, knownPools))
            ergRate  <- OptionT(rates.rateOf(ErgoAssetClass, fiat))
            fiatEquiv    = ergEquiv.value / math.pow(10, ErgoAssetDecimals - fiat.currency.decimals) * ergRate
            fiatEquivFmt = fiatEquiv.setScale(0, RoundingMode.FLOOR)
          } yield AssetEquiv(asset, fiat, fiatEquivFmt)).value
      }
  }

  final class CryptoSolver[F[_]: Monad: Logging] extends PriceSolver[F, CryptoSolverType] {

    private def parsePools(pools: List[PoolSnapshot]): List[Market] =
      pools.map(p => Market.fromReserves(p.lockedX, p.lockedY))

    def convert(
      asset: FullAsset,
      target: ValueUnits[AssetRepr],
      knownPools: List[PoolSnapshot]
    ): F[Option[AssetEquiv[AssetRepr]]] =
      target match {
        case CryptoUnits(units) =>
          if (asset.id != units.tokenId) {
            parsePools(knownPools.filter(p => p.lockedX.id == asset.id || p.lockedY.id == asset.id)).pure
              .flatTap(_ => info"Convert $asset using known pools.")
              .map(_.find(_.contains(units.tokenId)).map { market =>
                val amountEquiv = BigDecimal(asset.amount) * market.priceBy(asset.id)
                AssetEquiv(asset, target, amountEquiv)
              })
          } else AssetEquiv(asset, target, BigDecimal(asset.amount)).someF
      }
  }

  final class PriceSolverTracing[F[_]: FlatMap: Logging, T <: PriceSolverType](solverType: String)
    extends PriceSolver[Mid[F, *], T] {

    def convert(
      asset: FullAsset,
      target: ValueUnits[AssetRepr],
      pools: List[PoolSnapshot]
    ): Mid[F, Option[AssetEquiv[AssetRepr]]] =
      for {
        _ <- trace"[$solverType]: convert(asset=$asset, target=$target)"
        r <- _
        _ <- trace"[$solverType]: convert(asset=$asset, target=$target) -> $r"
      } yield r
  }
}
