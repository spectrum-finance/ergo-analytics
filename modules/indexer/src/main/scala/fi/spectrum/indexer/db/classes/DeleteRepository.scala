package fi.spectrum.indexer.db.classes

import doobie.Update
import doobie.util.Write
import doobie.util.log.LogHandler

/** Describes the way to delete T entity using I field
  */
trait DeleteRepository[I] {
  val tableName: String
  val field: String

  def delete(implicit lh: LogHandler, w: Write[I]): Update[I] =
    Update[I](s"delete from $tableName where $field=?")
}
