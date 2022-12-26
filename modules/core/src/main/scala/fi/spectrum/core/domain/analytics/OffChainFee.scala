package fi.spectrum.core.domain.analytics

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.order.{Fee, OrderId, PoolId}
import fi.spectrum.core.domain.{BoxId, PubKey}
import glass.classic.Lens
import glass.macros.GenContains
import tofu.logging.derivation.loggable

@derive(encoder, decoder, loggable)
final case class OffChainFee(
  poolId: PoolId,
  orderId: OrderId,
  outputId: BoxId,
  pubKey: PubKey,
  fee: Fee
)

object OffChainFee {
  implicit val lensOrderId: Lens[OffChainFee, OrderId] = GenContains[OffChainFee](_.orderId)
}