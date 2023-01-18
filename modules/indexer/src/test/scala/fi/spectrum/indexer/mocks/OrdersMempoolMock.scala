package fi.spectrum.indexer.mocks

import cats.Applicative
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.indexer.services.Mempool
import tofu.syntax.monadic._

object OrdersMempoolMock {

  def mock[F[_]: Applicative]: Mempool[F] = new Mempool[F] {
    def put(processed: Processed.Any): F[Unit] = unit

    def del(processed: Processed.Any): F[Long] = 0L.pure

    def del(processed: List[Processed.Any]): F[Long] = 0L.pure
  }
}
