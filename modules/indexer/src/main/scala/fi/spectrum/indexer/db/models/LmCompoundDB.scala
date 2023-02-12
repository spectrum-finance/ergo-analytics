package fi.spectrum.indexer.db.models

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.{Processed, Version}
import fi.spectrum.core.domain.order.Order.Compound
import fi.spectrum.core.domain.order.Order.Compound._
import fi.spectrum.core.domain.order.OrderStatus._
import fi.spectrum.core.domain.order.{OrderId, PoolId}
import fi.spectrum.core.domain.{AssetAmount, BoxId, ProtocolVersion, PubKey, TokenId}
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.classes.syntax.ToDBOps

final case class LmCompoundDB(
  orderId: OrderId,
  poolId: PoolId,
  poolStateId: Option[BoxId],
  vLq: AssetAmount,
  tmp: Option[AssetAmount],
  bundleKeyId: TokenId,
  redeemer: PubKey,
  version: Version,
  protocolVersion: ProtocolVersion,
  registeredTx: Option[TxInfo],
  executedTx: Option[TxInfo]
)

object LmCompoundDB {

  implicit val toDB: ToDB[Processed[Compound], LmCompoundDB] = processed => {
    processed.order match {
      case compound: CompoundV1 => processed.widen(compound).toDB
    }
  }

  implicit val ___V1: ToDB[Processed[CompoundV1], LmCompoundDB] =
    processed => {
      val compound: CompoundV1 = processed.order
      val txInfo               = TxInfo(processed.state.txId, processed.state.timestamp)
      LmCompoundDB(
        compound.id,
        compound.poolId,
        processed.poolBoxId,
        compound.params.vLq,
        compound.params.tmp,
        compound.params.bundleKeyId,
        compound.redeemer.value,
        compound.version,
        ProtocolVersion.init,
        if (processed.state.status.in(Registered)) txInfo.some else none,
        if (processed.state.status.in(Evaluated)) txInfo.some else none
      )
    }
}
