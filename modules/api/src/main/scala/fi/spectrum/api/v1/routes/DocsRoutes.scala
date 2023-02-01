package fi.spectrum.api.v1.routes

import cats.effect.Concurrent
import cats.effect.kernel.Async
import cats.syntax.applicative._
import cats.syntax.either._
import cats.syntax.option._
import cats.syntax.semigroupk._
import fi.spectrum.api.configs.RequestConfig
import fi.spectrum.api.v1.endpoints.{AmmStatsEndpoints, DocsEndpoints}
import fi.spectrum.common.http.{HttpError, VersionPrefix}
import org.http4s.HttpRoutes
import sttp.apispec.Tag
import sttp.tapir.apispec.{Tag => TapirTag}
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.circe.yaml.TapirOpenAPICirceYaml
import sttp.tapir.openapi.{Info, OpenAPI}
import sttp.tapir.redoc.http4s.RedocHttp4s
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}

final class DocsRoutes[F[_]: Concurrent: Async](requestConf: RequestConfig)(implicit
  opts: Http4sServerOptions[F]
) extends TapirOpenAPICirceYaml {

  private val endpoints = DocsEndpoints
  import endpoints._

  private val interpreter = Http4sServerInterpreter(opts)

  val routes: HttpRoutes[F] = openApiSpecR <+> redocApiSpecR

  val statsEndpoints = new AmmStatsEndpoints(requestConf)

  private def allEndpoints =
    statsEndpoints.endpoints

  private def tags =
    Tag(statsEndpoints.PathPrefix, "AMM Statistics".some) :: Nil

  private val openapi =
    OpenAPIDocsInterpreter()
      .toOpenAPI(allEndpoints, "ErgoDEX API v1", "1.0")
      .tags(tags)

  private val docsAsYaml =
    OpenAPI(
      openapi.openapi,
      Info(openapi.info.title, openapi.info.version),
      openapi.tags.map(t => TapirTag(t.name, t.description))
    ).toYaml

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

  def make[F[_]: Concurrent: Async](requestConf: RequestConfig)(implicit
    opts: Http4sServerOptions[F]
  ): HttpRoutes[F] =
    new DocsRoutes[F](requestConf).routes
}
