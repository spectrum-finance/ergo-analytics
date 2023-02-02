package fi.spectrum.indexer.mocks

import cats.Applicative
import fi.spectrum.core.domain
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.indexer.services.Mempool
import tofu.syntax.monadic._

object MempoolMock {
  def make[F[_]: Applicative]: Mempool[F] = new Mempool[F] {
    def getOrder(id: List[domain.BoxId]): F[Option[Processed.Any]] = Option.empty[Processed.Any].pure

    def getPool(id: List[domain.BoxId]): F[Option[Pool]] =  Option.empty[Pool].pure

    def put(pool: Option[Pool], order: Option[Processed.Any]): F[Unit] = unit

    def del(pool: Option[Pool], order: Option[Processed.Any]): F[Unit] = unit
  }
}
