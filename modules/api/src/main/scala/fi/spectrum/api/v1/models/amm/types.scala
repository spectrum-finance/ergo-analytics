package fi.spectrum.api.v1.models.amm

import cats.Show
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.api.db.models.amm.PoolVolumeSnapshot
import fi.spectrum.core.domain.TokenId
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype
import sttp.tapir.{Schema, Validator}
import tofu.logging.Loggable
import tofu.logging.derivation.loggable

import scala.math.BigDecimal.RoundingMode

object types {

  @newtype
  final case class MarketId(value: String)

  object MarketId {

    def apply(baseId: TokenId, quoteId: TokenId): MarketId =
      MarketId(s"${baseId}_$quoteId")

    def apply(volume: PoolVolumeSnapshot): MarketId =
      MarketId(volume.volumeByX.id, volume.volumeByY.id)

    def apply(pool: PoolStats): MarketId =
      MarketId(pool.lockedX.id, pool.lockedY.id)

    implicit val encoder: Encoder[MarketId] = deriving
    implicit val decoder: Decoder[MarketId] = deriving

    implicit val show: Show[MarketId]         = _.value
    implicit val loggable: Loggable[MarketId] = Loggable.show

    implicit val schema: Schema[MarketId]       = deriving
    implicit val validator: Validator[MarketId] = schema.validator
  }

  @derive(encoder, decoder, loggable)
  @newtype case class RealPrice(value: BigDecimal) {
    def setScale(scale: Int): RealPrice = RealPrice(value.setScale(scale, RoundingMode.HALF_UP))
  }

  object RealPrice {

    val defaultScale = 6

    def calculate(
      baseAssetAmount: Long,
      baseAssetDecimals: Option[Int],
      quoteAssetAmount: Long,
      quoteAssetDecimals: Option[Int]
    ): RealPrice =
      RealPrice(
        BigDecimal(quoteAssetAmount) / baseAssetAmount * BigDecimal(10)
          .pow(
            baseAssetDecimals.getOrElse(0) - quoteAssetDecimals.getOrElse(0)
          )
      )

    implicit val realPriceSchema: Schema[RealPrice] = deriving
  }
}
