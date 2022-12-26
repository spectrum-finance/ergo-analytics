package fi.spectrum.parser.evaluation

import cats.syntax.option._
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.core.domain.order.{OrderState, OrderStatus}
import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.parser.{OrderParser, PoolParser}
import org.ergoplatform.ErgoAddressEncoder
import tofu.syntax.monadic._

/** Parse order evaluation result
  * @param orderParser - initial order parser
  * @param feeParser - off-chain fee parser
  * @param poolParser - pool parser
  * @param evalParser - order evaluation result
  */
class ProcessedOrderParser(implicit
  orderParser: OrderParser,
  feeParser: OffChainFeeParser,
  poolParser: PoolParser,
  evalParser: OrderEvaluationParser
) {

  def parse(tx: Transaction, timestamp: Long): Option[ProcessedOrder.Any] = {
    def registered: Option[ProcessedOrder.Any] = tx.outputs
      .map(out => orderParser.parse(out))
      .collectFirst { case Some(order) => order }
      .map(ProcessedOrder(_, OrderState(tx.id, timestamp, OrderStatus.Registered), none, none, none))

    def executed: Option[ProcessedOrder.Any] = tx.inputs
      .map(in => orderParser.parse(in.output))
      .collectFirst { case Some(order) => order }
      .map { order =>
        val pool = tx.inputs.map(_.output).toList.map(poolParser.parse(_, timestamp)).collectFirst { case Some(v) => v }
        val fee  = pool >>= { p => feeParser.parse(tx.outputs.toList, order, p.poolId) }
        val eval = pool >>= { p => evalParser.parse(order, tx.outputs.toList, p) }
        ProcessedOrder(
          order,
          OrderState(tx.id, timestamp, if (pool.isEmpty) OrderStatus.Refunded else OrderStatus.Executed),
          eval,
          fee,
          pool.map(_.box.boxId)
        )
      }

    registered orElse executed
  }

}

object ProcessedOrderParser {

  def make(spf: TokenId)(implicit e: ErgoAddressEncoder): ProcessedOrderParser = {
    implicit val fee   = OffChainFeeParser.make(spf)
    implicit val pools = PoolParser.make
    new ProcessedOrderParser
  }
}
