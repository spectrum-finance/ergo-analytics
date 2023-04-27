package fi.spectrum.api.v1.routes

import cats.effect.Concurrent
import cats.effect.kernel.Async
import cats.syntax.applicative._
import cats.syntax.either._
import cats.syntax.option._
import cats.syntax.semigroupk._
import fi.spectrum.api.v1.endpoints.{
  AmmStatsEndpoints,
  DocsEndpoints,
  HistoryEndpoints,
  LmStatsEndpoints,
  PriceTrackingEndpoints
}
import fi.spectrum.common.http.{HttpError, VersionPrefix}
import org.http4s.HttpRoutes
import sttp.apispec.Tag
import sttp.apispec.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.redoc.http4s.RedocHttp4s
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}

final class DocsRoutes[F[_]: Concurrent: Async](implicit
  opts: Http4sServerOptions[F]
) {

  private val endpoints = DocsEndpoints
  import endpoints._

  private val interpreter = Http4sServerInterpreter(opts)

  val routes: HttpRoutes[F] = openApiSpecR <+> redocApiSpecR

  val ammStatsEndpoints      = new AmmStatsEndpoints
  val priceTrackingEndpoints = new PriceTrackingEndpoints
  val lmStatsEndpoints       = new LmStatsEndpoints
  val historyEndpoints       = new HistoryEndpoints

  private def allEndpoints =
    ammStatsEndpoints.endpoints ++ priceTrackingEndpoints.endpoints ++
      lmStatsEndpoints.endpoints ++ historyEndpoints.endpoints

  private def tags =
    Tag(ammStatsEndpoints.PathPrefix, "AMM Api".some) ::
    Tag(priceTrackingEndpoints.PathPrefixPriceTracking, "Price tracking Api".some) ::
    Tag(lmStatsEndpoints.PathPrefix, "LM Api".some) ::
    Tag(historyEndpoints.PathPrefix, "History Api".some) :: Nil

  private val docsAsYaml =
    OpenAPIDocsInterpreter()
      .toOpenAPI(allEndpoints, "Spectrum finance API v1", "1.0")
      .tags(tags)
      .toYaml

  private def openApiSpecR: HttpRoutes[F] =
    interpreter.toRoutes(
      apiSpecDef.serverLogic(_ =>
        docsAsYaml
          .asRight[HttpError]
          .pure[F]
      )
    )

  private def redocApiSpecR: HttpRoutes[F] =
    new RedocHttp4s(
      "Redoc",
      docsAsYaml,
      "openapi",
      contextPath = VersionPrefix :: "docs" :: Nil
    ).routes
}

object DocsRoutes {

  def make[F[_]: Concurrent: Async](implicit
    opts: Http4sServerOptions[F]
  ): HttpRoutes[F] =
    new DocsRoutes[F].routes
}
