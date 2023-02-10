package fi.spectrum.api.mocks

import doobie.Update
import fi.spectrum.api.db.models.TxInfo
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.Order.Deposit.LmDeposit.LmDepositV1
import fi.spectrum.core.domain.order.{OrderId, PoolId}
import fi.spectrum.core.domain.{AssetAmount, ProtocolVersion}

object InsertLmDeposit {

  val fields = List(
    "order_id",
    "pool_id",
    "max_miner_fee",
    "expected_num_epochs",
    "input_id",
    "input_amount",
    "protocol_version",
    "contract_version",
    "redeemer_ergo_tree",
    "registered_transaction_id",
    "registered_transaction_timestamp"
  )

  private def fieldsString: String =
    fields.mkString(", ")

  private def holdersString: String =
    fields.map(_ => "?").mkString(", ")

  val tableName: String = "lm_deposits"

  final private case class Insert(
    orderId: OrderId,
    poolId: PoolId,
    fee: Long,
    epochs: Int,
    in: AssetAmount,
    v: Int,
    v1: String,
    key: String,
    txInfo: TxInfo
  )

  final def insert(processed: Processed[LmDepositV1]): doobie.ConnectionIO[Int] = {
    val a = Insert(
      processed.order.id,
      processed.order.poolId,
      processed.order.maxMinerFee,
      processed.order.params.expectedNumEpochs,
      processed.order.params.tokens,
      ProtocolVersion.init.value,
      processed.order.version.entryName,
      processed.order.redeemer.value.value.value.value,
      TxInfo(processed.state.txId, processed.state.timestamp)
    )
    Update[Insert](s"""
         |insert into $tableName ($fieldsString) values ($holdersString)
       """.stripMargin).toUpdate0(a).run

  }

}
