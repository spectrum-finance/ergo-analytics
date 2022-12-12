package fi.spectrum.indexer.db.schema

import doobie.Update
import doobie.util.Write
import doobie.util.log.LogHandler
import fi.spectrum.indexer.models.{DepositDB, LockDB, RedeemDB, SwapDB}

trait Schema[T] {

  val tableName: String

  val fields: List[String]

  final def insert(implicit lh: LogHandler, w: Write[T]): Update[T] =
    Update[T](s"insert into $tableName ($fieldsString) values ($holdersString)")

  final def insertNoConflict(implicit lh: LogHandler, w: Write[T]): Update[T] =
    Update[T](s"insert into $tableName ($fieldsString) values ($holdersString) on conflict do nothing")

  private def fieldsString: String =
    fields.mkString(", ")

  private def holdersString: String =
    fields.map(_ => "?").mkString(", ")
}

object Schema {
  implicit val swapSchema: Schema[SwapDB]       = new SwapSchema
  implicit val redeemSchema: Schema[RedeemDB]   = new RedeemSchema
  implicit val depositSchema: Schema[DepositDB] = new DepositSchema
  implicit val lockSchema: Schema[LockDB]       = new LockSchema
}
