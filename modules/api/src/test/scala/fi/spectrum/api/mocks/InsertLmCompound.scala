package fi.spectrum.api.mocks

import doobie.Update
import fi.spectrum.api.db.models.TxInfo
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.Order.Compound.CompoundV1
import fi.spectrum.core.domain.order.Order.Deposit.LmDeposit.LmDepositV1
import fi.spectrum.core.domain.order.{OrderId, PoolId}
import fi.spectrum.core.domain.{AssetAmount, ProtocolVersion}

object InsertLmCompound {

  val tableName: String = "lm_compound"

  val fields: List[String] = List(
    "order_id",
    "pool_id",
    "v_lq_id",
    "v_lq_amount",
    "tmp_id",
    "tmp_amount",
    "bundle_key_id",
    "redeemer",
    "version",
    "protocol_version",
    "registered_transaction_id",
    "registered_transaction_timestamp"
  )

  final private case class Insert(
    orderId: OrderId,
    poolId: PoolId,
    lq: AssetAmount,
    tmp: AssetAmount,
    key: String,
    addr: String,
    v: String,
    p: Int,
    txInfo: TxInfo
  )

  private def fieldsString: String =
    fields.mkString(", ")

  private def holdersString: String =
    fields.map(_ => "?").mkString(", ")

  final def insert(processed: Processed[CompoundV1]): doobie.ConnectionIO[Int] = {
    val a = Insert(
      processed.order.id,
      processed.order.poolId,
      processed.order.vLq,
      processed.order.tmp,
      processed.order.bundleKeyId.unwrapped,
      processed.order.redeemer.value.value.value.value,
      processed.order.version.entryName,
      ProtocolVersion.init.value,
      TxInfo(processed.state.txId, processed.state.timestamp)
    )
    Update[Insert](s"""
         |insert into $tableName ($fieldsString) values ($holdersString)
       """.stripMargin).toUpdate0(a).run

  }
}
