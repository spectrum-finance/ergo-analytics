package fi.spectrum.api.mocks

import doobie.Update
import fi.spectrum.api.db.models.TxInfo
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.Order.Compound.CompoundV1
import fi.spectrum.core.domain.order.Order.Deposit.LmDeposit.LmDepositV1
import fi.spectrum.core.domain.order.Order.Redeem.LmRedeem.LmRedeemV1
import fi.spectrum.core.domain.order.{OrderId, PoolId}
import fi.spectrum.core.domain.{AssetAmount, ProtocolVersion, TokenId}

object InsertLmRedeem {

  val tableName: String = "lm_redeems"

  val fields: List[String] = List(
    "order_id",
    "bundle_key_id",
    "expected_lq_id",
    "expected_lq_amount",
    "max_miner_fee",
    "redeemer_ergo_tree",
    "contract_version",
    "protocol_version",
    "registered_transaction_id",
    "registered_transaction_timestamp"
  )

  final private case class Insert(
    orderId: OrderId,
    bundleKeyId: TokenId,
    expectedLq: AssetAmount,
    minerFee: Long,
    addr: String,
    v: String,
    p: Int,
    txInfo: TxInfo
  )

  private def fieldsString: String =
    fields.mkString(", ")

  private def holdersString: String =
    fields.map(_ => "?").mkString(", ")

  final def insert(processed: Processed[LmRedeemV1]): doobie.ConnectionIO[Int] = {
    val a =
      Insert(
        processed.order.id,
        processed.order.bundleKeyId,
        processed.order.expectedLq,
        processed.order.maxMinerFee,
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
