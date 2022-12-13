package fi.spectrum.indexer

import doobie.util.Put
import doobie.{Get, Meta}
import io.circe.Json
import io.circe.parser.parse
import org.postgresql.util.PGobject

package object db {

  implicit val getBigInt: Get[BigInt] = implicitly[Get[BigDecimal]]
    .temap(x => x.toBigIntExact.fold[Either[String, BigInt]](Left(s"Failed to convert '$x' to BigInt"))(Right(_)))

  implicit val putBigInt: Put[BigInt] = implicitly[Put[BigDecimal]].contramap[BigInt](BigDecimal(_))

  implicit val metaBigInt: Meta[BigInt] = new Meta(getBigInt, putBigInt)

  implicit val jsonMeta: Meta[Json] =
    Meta.Advanced
      .other[PGobject]("json")
      .imap[Json](a => parse(a.getValue).getOrElse(Json.Null))(mkPgJson)

  private def mkPgJson(a: Json): PGobject = {
    val o = new PGobject
    o.setType("json")
    o.setValue(a.noSpaces)
    o
  }
}
