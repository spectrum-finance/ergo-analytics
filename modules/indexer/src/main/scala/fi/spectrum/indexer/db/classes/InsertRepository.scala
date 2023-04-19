package fi.spectrum.indexer.db.classes

import doobie.Update
import doobie.util.Write
import doobie.util.log.LogHandler

/** Describes the way to insert entity T
  */
trait InsertRepository[T] {

  val tableName: String

  val fields: List[String]

  final def insert(implicit lh: LogHandler, w: Write[T]): Update[T] =
    Update[T](s"insert into $tableName ($fieldsString) values ($holdersString)")

  final def insertNoConflict(implicit lh: LogHandler, w: Write[T]): Update[T] = {
    println(tableName)
    Update[T](s"insert into $tableName ($fieldsString) values ($holdersString) on conflict do nothing")
  }

  private def fieldsString: String =
    fields.mkString(", ")

  private def holdersString: String =
    fields.map(_ => "?").mkString(", ")

}
