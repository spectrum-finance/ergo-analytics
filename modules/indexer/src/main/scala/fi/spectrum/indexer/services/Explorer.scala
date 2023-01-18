package fi.spectrum.indexer.services

import cats.syntax.semigroup._
import cats.{Monad, MonadError}
import derevo.derive
import fi.spectrum.core.domain.TokenId
import fi.spectrum.graphite.Metrics
import fi.spectrum.indexer.config.NetworkConfig
import fi.spectrum.indexer.models.TokenInfo
import retry.RetryPolicies.{constantDelay, limitRetries}
import retry._
import sttp.client3._
import sttp.client3.circe._
import sttp.model.Uri.Segment
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.time.now.millis
import tofu.time.Clock

@derive(representableK)
trait Explorer[F[_]] {
  def getTokenInfo(tokenId: TokenId): F[Option[TokenInfo]]
}

object Explorer {

  def make[F[_]: NetworkConfig.Has: MonadError[*[_], Throwable]: Sleep: Clock](implicit
    backend: SttpBackend[F, Any],
    metrics: Metrics[F],
    logs: Logging.Make[F]
  ): F[Explorer[F]] =
    for {
      config <- NetworkConfig.access
      implicit0(logging: Logging[F]) = logs.forService[Explorer[F]]
    } yield new Retry[F](config) attach (new MetricsMid[F] attach (new Tracing[F] attach new Live[F](config)))

  final private class Live[F[_]: Monad](config: NetworkConfig)(implicit backend: SttpBackend[F, Any])
    extends Explorer[F] {

    private def tokenInfoPathSeg(tokenId: TokenId): Segment =
      Segment(s"api/v1/tokens/$tokenId", identity)

    def getTokenInfo(tokenId: TokenId): F[Option[TokenInfo]] =
      basicRequest
        .get(config.explorerUri.withPathSegment(tokenInfoPathSeg(tokenId)))
        .response(asJson[TokenInfo])
        .send(backend)
        .map(_.body.toOption)
  }

  final private class Retry[F[_]: MonadError[*[_], Throwable]: Sleep: Logging](config: NetworkConfig)
    extends Explorer[Mid[F, *]] {
    private val policy = limitRetries[F](config.limitRetries) |+| constantDelay[F](config.retryDelay)

    def getTokenInfo(tokenId: TokenId): Mid[F, Option[TokenInfo]] =
      retryingOnAllErrors(
        policy,
        (err: Throwable, _: RetryDetails) => info"Failed to find token $tokenId. ${err.getMessage}. Retrying..."
      )(_)
  }

  final private class Tracing[F[_]: Monad: Logging] extends Explorer[Mid[F, *]] {

    def getTokenInfo(tokenId: TokenId): Mid[F, Option[TokenInfo]] =
      for {
        _ <- info"getTokenInfo($tokenId)"
        r <- _
        _ <- info"getTokenInfo($tokenId) -> $r"
      } yield r
  }

  final private class MetricsMid[F[_]: Monad: Clock](implicit metrics: Metrics[F]) extends Explorer[Mid[F, *]] {

    def getTokenInfo(tokenId: TokenId): Mid[F, Option[TokenInfo]] =
      for {
        start  <- millis
        r      <- _
        finish <- millis
        _      <- metrics.sendTs("explorer.token.info", (finish - start).toDouble)
      } yield r

  }
}
