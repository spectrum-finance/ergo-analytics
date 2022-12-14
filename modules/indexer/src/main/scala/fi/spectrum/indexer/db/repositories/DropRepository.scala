package fi.spectrum.indexer.db.repositories

import fi.spectrum.core.domain.TxId
import doobie.implicits._
trait DropRepository[F[_]] {
  def drop(id: TxId): F[Int]
}

object DropRepository {

}