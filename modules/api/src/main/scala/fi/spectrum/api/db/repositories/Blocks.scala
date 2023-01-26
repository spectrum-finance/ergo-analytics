package fi.spectrum.api.db.repositories

import cats.tagless.syntax.functorK._
import cats.{FlatMap, Functor}
import doobie.ConnectionIO
import fi.spectrum.api.db.sql.BlocksSql
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.logging._
import tofu.syntax.monadic._

trait Blocks[F[_]] {
  def getCurrentHeight: F[Int]
}

object Blocks {

  implicit def representableK: RepresentableK[Blocks] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Functor, D[_]: FlatMap: LiftConnectionIO](implicit
    elh: EmbeddableLogHandler[D],
    logs: Logs[I, D]
  ): I[Blocks[D]] =
    logs.forService[Blocks[D]].map { implicit l =>
      elh.embed(implicit lh => new Tracing[D] attach new Live(new BlocksSql()).mapK(LiftConnectionIO[D].liftF))
    }

  final class Live(sql: BlocksSql) extends Blocks[ConnectionIO] {

    def getCurrentHeight: ConnectionIO[Int] =
      sql.getBestHeight.option.map(_.getOrElse(0))
  }

  final class Tracing[F[_]: FlatMap: Logging] extends Blocks[Mid[F, *]] {

    def getCurrentHeight: Mid[F, Int] =
      for {
        _ <- trace"getCurrentHeight()"
        r <- _
        _ <- trace"getCurrentHeight() -> $r"
      } yield r
  }
}
