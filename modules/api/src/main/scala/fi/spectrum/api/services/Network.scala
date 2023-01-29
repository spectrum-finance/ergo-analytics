package fi.spectrum.api.services

import cats.syntax.option._
import cats.syntax.semigroup._
import cats.{Monad, MonadError}
import derevo.circe.decoder
import derevo.derive
import fi.spectrum.api.configs.NetworkConfig
import fi.spectrum.api.models.{BlockInfo, CmcResponse, Items}
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.syntax.HttpOps.ResponseOps
import fi.spectrum.graphite.Metrics
import retry.RetryPolicies.{constantDelay, limitRetries}
import retry.{retryingOnAllErrors, RetryDetails, Sleep}
import sttp.client3.circe.asJson
import sttp.client3.{basicRequest, SttpBackend}
import sttp.model.Uri.Segment
import tofu.Throws
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.lift.Lift
import tofu.logging.{Logging, Logs}
import tofu.syntax.lift._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.syntax.time.now.millis
import tofu.time.Clock

@derive(representableK)
trait Network[F[_]] {
  def getErgPriceCMC: F[Option[BigDecimal]]

  def getVerifiedTokenList: F[List[TokenId]]

  def getCurrentNetworkHeight: F[Int]
}

object Network {

  @derive(decoder)
  final case class Token(network: String, address: String, decimals: Int, name: String, ticker: String)

  @derive(decoder)
  final case class TokenResponse(tokens: List[Token])

  def make[I[_]: Monad, F[_]: MonadError[*[_], Throwable]: Sleep: Throws: Clock: NetworkConfig.Has](implicit
    backend: SttpBackend[F, _],
    metrics: Metrics[F],
    lift: Lift[F, I],
    logs: Logs[I, F]
  ): I[Network[F]] =
    logs.forService[Network[F]].flatMap { implicit __ =>
      NetworkConfig.access.lift.map { config =>
        new Retry[F](config) attach (new Tracing[F] attach (new MetricsMid[F] attach new Live[F](config)))
      }
    }

  final private class Live[F[_]: Monad: Throws](config: NetworkConfig)(implicit backend: SttpBackend[F, _])
    extends Network[F] {

    private val CmcApiKey = "X-CMC_PRO_API_KEY"

    def getErgPriceCMC: F[Option[BigDecimal]] =
      basicRequest
        .header(CmcApiKey, config.cmcApiKey)
        .get(
          config.cmcUrl
            .withPathSegment(Segment("v2/cryptocurrency/quotes/latest", identity))
            .addParams("id" -> s"$ErgCMCId", "convert_id" -> s"$UsdCMCId")
        )
        .response(asJson[CmcResponse])
        .send(backend)
        .absorbError
        .map(_.price.some)

    private val network = "ergo"

    private val ergoToken: Token = Token(
      network,
      "0000000000000000000000000000000000000000000000000000000000000000",
      9,
      "Ergo",
      "ERG"
    )

    def getVerifiedTokenList: F[List[TokenId]] =
      basicRequest
        .get(config.verifiedTokenListUrl)
        .response(asJson[TokenResponse])
        .send(backend)
        .absorbError
        .map { resp =>
          (ergoToken :: resp.tokens).filter(_.network == network).map(tkn => TokenId.unsafeFromString(tkn.address))
        }

    def getCurrentNetworkHeight: F[Int] =
      basicRequest
        .get(
          config.explorerUri
            .withPathSegment(Segment("api/v1/blocks", identity))
            .addParams("limit" -> "1", "order" -> "desc")
        )
        .response(asJson[Items[BlockInfo]])
        .send(backend)
        .absorbError
        .map(_.items.headOption.map(_.height).getOrElse(0))
  }

  final private class Retry[F[_]: MonadError[*[_], Throwable]: Sleep: Logging](config: NetworkConfig)(implicit
    metrics: Metrics[F]
  ) extends Network[Mid[F, *]] {
    private val cmcPolicy = limitRetries[F](config.cmcLimitRetries) |+| constantDelay[F](config.cmcRetryDelay)

    private val verifiedTokensPolicy =
      limitRetries[F](config.verifiedTokenLimitRetries) |+| constantDelay[F](config.verifiedTokenRetryDelay)

    private val explorerPolicy =
      limitRetries[F](config.explorerLimitRetries) |+| constantDelay[F](config.explorerRetryDelay)

    def getErgPriceCMC: Mid[F, Option[BigDecimal]] =
      retryingOnAllErrors(
        cmcPolicy,
        (err: Throwable, _: RetryDetails) =>
          info"Failed to get erg price. ${err.getMessage}. Retrying..." >> metrics.sendCount("cmc.request.failed", 1.0)
      )(_)

    def getVerifiedTokenList: Mid[F, List[TokenId]] =
      retryingOnAllErrors(
        verifiedTokensPolicy,
        (err: Throwable, _: RetryDetails) =>
          info"Failed to get verified tokens. ${err.getMessage}. Retrying..." >>
          metrics.sendCount("verified.tokens.request.failed", 1.0)
      )(_)

    def getCurrentNetworkHeight: Mid[F, Int] =
      retryingOnAllErrors(
        explorerPolicy,
        (err: Throwable, _: RetryDetails) =>
          info"Failed to get best height. ${err.getMessage}. Retrying..." >>
          metrics.sendCount("explorer.best.height.request.failed", 1.0)
      )(_)
  }

  final private class Tracing[F[_]: Monad: Logging] extends Network[Mid[F, *]] {

    def getErgPriceCMC: Mid[F, Option[BigDecimal]] =
      for {
        _ <- info"getErgPriceCMC()"
        r <- _
        _ <- info"getErgPriceCMC() -> $r"
      } yield r

    def getVerifiedTokenList: Mid[F, List[TokenId]] =
      for {
        _ <- info"getVerifiedTokenList()"
        r <- _
        _ <- info"getVerifiedTokenList() -> $r"
      } yield r

    def getCurrentNetworkHeight: Mid[F, Int] =
      for {
        _ <- info"getCurrentNetworkHeight()"
        r <- _
        _ <- info"getCurrentNetworkHeight() -> $r"
      } yield r
  }

  final private class MetricsMid[F[_]: Monad: Clock](implicit metrics: Metrics[F]) extends Network[Mid[F, *]] {

    def getErgPriceCMC: Mid[F, Option[BigDecimal]] =
      for {
        start  <- millis
        r      <- _
        finish <- millis
        _      <- metrics.sendTs("cmc.request", (finish - start).toDouble)
      } yield r

    def getVerifiedTokenList: Mid[F, List[TokenId]] =
      for {
        start  <- millis
        r      <- _
        finish <- millis
        _      <- metrics.sendTs("verified.tokens.request", (finish - start).toDouble)
      } yield r

    def getCurrentNetworkHeight: Mid[F, Int] =
      for {
        start  <- millis
        r      <- _
        finish <- millis
        _      <- metrics.sendTs("explorer.best.height.request", (finish - start).toDouble)
      } yield r
  }
}
