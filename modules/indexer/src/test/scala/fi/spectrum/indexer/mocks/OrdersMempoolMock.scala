package fi.spectrum.indexer.mocks

import cats.Applicative
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.indexer.services.OrdersMempool
import tofu.syntax.monadic._

object OrdersMempoolMock {

  def mock[F[_]: Applicative]: OrdersMempool[F] = new OrdersMempool[F] {
    def put(processed: ProcessedOrder.Any): F[Unit] = unit

    def del(processed: ProcessedOrder.Any): F[Long] = 0L.pure

    def del(processed: List[ProcessedOrder.Any]): F[Long] = 0L.pure
  }
}
