package fi.spectrum.indexer.services

import cats.Monad
import derevo.derive
import fi.spectrum.core.domain.TokenId
import fi.spectrum.indexer.config.NetworkConfig
import fi.spectrum.indexer.models.TokenInfo
import sttp.client3._
import sttp.client3.circe._
import sttp.model.Uri
import sttp.model.Uri.Segment
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.monadic._
import tofu.syntax.logging._

@derive(representableK)
trait Explorer[F[_]] {
  def getTokenInfo(tokenId: TokenId): F[Option[TokenInfo]]
}

object Explorer {

  def make[F[_]: NetworkConfig.Has: Monad](implicit
    backend: SttpBackend[F, Any],
    logs: Logging.Make[F]
  ): F[Explorer[F]] =
    for {
      config <- NetworkConfig.access
      implicit0(logging: Logging[F]) = logs.forService[Explorer[F]]
    } yield new Tracing[F] attach new Live[F](config.explorerUri)

  final private class Live[F[_]: Monad](explorerUri: Uri)(implicit backend: SttpBackend[F, Any]) extends Explorer[F] {

    private def tokenInfoPathSeg(tokenId: TokenId): Segment =
      Segment(s"api/v1/tokens/$tokenId", identity)

    def getTokenInfo(tokenId: TokenId): F[Option[TokenInfo]] =
      basicRequest
        .get(explorerUri.withPathSegment(tokenInfoPathSeg(tokenId)))
        .response(asJson[TokenInfo])
        .send(backend)
        .map(_.body.toOption)
  }

  final private class Tracing[F[_]: Monad: Logging] extends Explorer[Mid[F, *]] {

    def getTokenInfo(tokenId: TokenId): Mid[F, Option[TokenInfo]] =
      for {
        _ <- info"Token request with token $tokenId"
        r <- _
        _ <- info"Token request result $r"
      } yield r
  }
}
