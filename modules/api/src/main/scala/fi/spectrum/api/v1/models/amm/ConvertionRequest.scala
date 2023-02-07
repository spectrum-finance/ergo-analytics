package fi.spectrum.api.v1.models.amm

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{HexString, TokenId}
import sttp.tapir.Schema

@derive(encoder, decoder)
case class ConvertionRequest(tokenId: TokenId, amount: Long)

object ConvertionRequest {

  implicit val schemaConvertionReq: Schema[ConvertionRequest] =
    Schema
      .derived[ConvertionRequest]
      .modify(_.tokenId)(_.description("Id of converted token"))
      .modify(_.amount)(_.description("Amount of converted token"))
      .encodedExample(
        ConvertionRequest(
          TokenId(HexString.unsafeFromString("9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec")),
          3700
        )
      )
}
