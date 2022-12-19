package fi.spectrum.indexer.db.repositories

import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.indexer.db.classes.{DeleteRepository, NonUpdatableRepository}
import fi.spectrum.indexer.models.OffChainFeeDB

final class OffChainFeeRepository
  extends Repository[OffChainFeeDB, OrderId]
  with DeleteRepository[OffChainFeeDB, OrderId]
  with NonUpdatableRepository[OffChainFeeDB] {

  val fields: List[String] = List(
    "pool_id",
    "order_id",
    "output_id",
    "pub_key",
    "dex_fee",
    "fee_type"
  )

  val tableName: String = "off_chain_fee"

  val field: String = "order_id"

}