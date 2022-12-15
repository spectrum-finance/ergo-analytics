package fi.spectrum.core.domain.pool

import cats.Show
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
import cats.syntax.show._

import tofu.logging.Loggable
import tofu.logging.derivation.{loggable, show}

/** This abstraction represents any pool in our domain, e.g. Amm pool, LM pool
  * @tparam V - pool version
  * @tparam T = pool type
  */
sealed trait Pool[+V <: Version, +T <: PoolType] {
  val poolId: PoolId
  val box: Output
}

object Pool {

  type Any = Pool[Version, PoolType]

  implicit def poolEncoder: Encoder[Pool[Version, PoolType]] = { case amm: AmmPool =>
    amm.asJson
  }

  implicit def poolLoggable: Loggable[Pool.Any] = Loggable.show

  implicit def poolShow: Show[Pool.Any] = { case amm: AmmPool =>
    amm.show
  }

  implicit def poolDecoder: Decoder[Pool[Version, PoolType]] =
    List[Decoder[Pool[Version, PoolType]]](
      Decoder[AmmPool].widen
    ).reduceLeft(_ or _)

  @derive(encoder, decoder, show, loggable)
  final case class AmmPool(
    poolId: PoolId,
    lp: AssetAmount,
    x: AssetAmount,
    y: AssetAmount,
    feeNum: Int,
    timestamp: Long,
    box: Output
  ) extends Pool[V1, PoolType.AMM]
}
