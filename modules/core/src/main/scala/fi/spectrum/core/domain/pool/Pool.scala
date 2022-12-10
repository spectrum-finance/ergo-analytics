package fi.spectrum.core.domain.pool

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.AssetAmount
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.transaction.Output
import io.circe.{Decoder, Encoder}
import io.circe.syntax._
import cats.syntax.functor._

sealed trait Pool[+V <: Version, +T <: PoolType] {
  val poolId: PoolId
  val box: Output
}

object Pool {

  type Any = Pool[Version, PoolType]

  implicit def poolEncoder: Encoder[Pool[Version, PoolType]] = { case amm: AmmPool =>
    amm.asJson
  }

  implicit def orderDecoder: Decoder[Pool[Version, PoolType]] =
    List[Decoder[Pool[Version, PoolType]]](
      Decoder[AmmPool].widen
    ).reduceLeft(_ or _)

  @derive(encoder, decoder)
  final case class AmmPool(
    poolId: PoolId,
    lp: AssetAmount,
    x: AssetAmount,
    y: AssetAmount,
    feeNum: Int,
    box: Output
  ) extends Pool[V1, PoolType.AMM]
}
