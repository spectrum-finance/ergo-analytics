package fi.spectrum.parser.evaluation

import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.core.domain.order.{OrderState, OrderStatus}
import cats.syntax.option._
import fi.spectrum.core.domain.transaction.Transaction
import fi.spectrum.parser.{OrderParser, PoolParser}
import tofu.syntax.monadic._

class ProcessedOrderParser(implicit
  orderParser: OrderParser,
  feeParser: OffChainFeeParser,
  poolParser: PoolParser,
  evalParser: OrderEvaluationParser
) {

  def parse(tx: Transaction, timestamp: Long): Option[ProcessedOrder] = {
    def registered: Option[ProcessedOrder] = tx.outputs
      .map(out => orderParser.parse(out))
      .collectFirst { case Some(order) => order }
      .map(ProcessedOrder(_, OrderState(tx.id, timestamp, OrderStatus.Registered), none, none, none))

    def executed: Option[ProcessedOrder] = tx.inputs
      .map(in => orderParser.parse(in.output))
      .collectFirst { case Some(order) => order }
      .map { order =>
        val pool = tx.outputs.toList.map(poolParser.parse).collectFirst { case Some(v) => v }
        val fee  = pool >>= { p => feeParser.parse(tx.outputs.toList, order, p.poolId) }
        val eval = pool >>= { p => evalParser.parse(order, tx.outputs.toList, p) }
        ProcessedOrder(
          order,
          OrderState(tx.id, timestamp, if (pool.isEmpty) OrderStatus.Refunded else OrderStatus.Executed),
          eval,
          fee,
          pool
        )
      }

    registered orElse executed
  }

}

object ProcessedOrderParser {

  implicit def make(implicit
    orderParser: OrderParser,
    feeParser: OffChainFeeParser,
    poolParser: PoolParser,
    evalParser: OrderEvaluationParser
  ): ProcessedOrderParser = new ProcessedOrderParser
}
