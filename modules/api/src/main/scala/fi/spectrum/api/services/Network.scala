package fi.spectrum.api.services

import cats.Monad
import derevo.circe.decoder
import derevo.derive
import fi.spectrum.api.configs.NetworkConfig
import fi.spectrum.api.models.CmcResponse
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.syntax.HttpOps.ResponseOps
import sttp.client3.{basicRequest, SttpBackend}
import sttp.client3.circe.asJson
import sttp.model.Uri.Segment
import tofu.Throws
import tofu.syntax.monadic._
import cats.syntax.option._

trait Network[F[_]] {
  def getErgPriceCMC: F[Option[BigDecimal]]

  def getVerifiedTokenList: F[List[TokenId]]
}

object Network {

  @derive(decoder)
  final case class Token(network: String, address: String, decimals: Int, name: String, ticker: String)

  @derive(decoder)
  final case class TokenResponse(tokens: List[Token])

  def make[F[_]: Monad: Throws: NetworkConfig.Has](implicit backend: SttpBackend[F, _]): F[Network[F]] =
    NetworkConfig.access.map(new Live[F](_))

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
        .get(config.verifiedTokeListUrl)
        .response(asJson[TokenResponse])
        .send(backend)
        .absorbError
        .map { resp =>
          (ergoToken :: resp.tokens).filter(_.network == network).map(tkn => TokenId.unsafeFromString(tkn.address))
        }
  }
}
