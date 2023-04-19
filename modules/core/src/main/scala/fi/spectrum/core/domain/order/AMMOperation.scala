package fi.spectrum.core.domain.order

import fi.spectrum.core.domain.{AssetAmount, PubKey, TxId}

case class AMMOperation(
  operationId: OrderId,
  operationType: String,
  poolId: PoolId,
  timestamp: Long,
  inX: Option[AssetAmount],
  inY: Option[AssetAmount],
  outX: Option[AssetAmount],
  outY: Option[AssetAmount],
  redeemer: Option[PubKey],
  receiver: Option[PubKey],
  executedTxId: Option[TxId]
)
