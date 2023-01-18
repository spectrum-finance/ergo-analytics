package fi.spectrum.indexer.db.models

import fi.spectrum.core.domain.BoxId
import fi.spectrum.core.domain.analytics.{OrderEvaluation, Processed}
import fi.spectrum.core.domain.order.{Order, OrderId}
import glass.classic.Prism

final case class UpdateEvaluatedTx[A](
  info: TxInfo,
  poolStateId: Option[BoxId],
  eval: Option[A],
  orderId: OrderId
) {
  def mapEval[B](f: Option[A] => Option[B]): UpdateEvaluatedTx[B] = this.copy(eval = f(eval))
}

object UpdateEvaluatedTx {

  def fromProcessed[O <: Order, E](
    processed: Processed[O]
  )(implicit prism: Prism[OrderEvaluation, E]): UpdateEvaluatedTx[E] =
    UpdateEvaluatedTx(
      TxInfo(processed.state.txId, processed.state.timestamp),
      processed.poolBoxId,
      processed.evaluation.flatMap(prism.getOption),
      processed.order.id
    )
}
