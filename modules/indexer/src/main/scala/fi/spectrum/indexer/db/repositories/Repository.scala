package fi.spectrum.indexer.db.repositories

import doobie.Update
import doobie.util.Write
import doobie.util.log.LogHandler

trait Repository[T, I] {
  def insertNoConflict(implicit lh: LogHandler, w: Write[T]): Update[T]

  def delete(implicit lh: LogHandler, w: Write[I]): Update[I]
}

object Repository
