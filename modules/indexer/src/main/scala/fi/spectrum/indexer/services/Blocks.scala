package fi.spectrum.indexer.services

import cats.Monad
import cats.data.NonEmptyList
import derevo.derive
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.streaming.domain.BlockEvent
import tofu.doobie.transactor.Txr
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.doobie.txr._
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait Blocks[F[_]] {
  def process(events: NonEmptyList[BlockEvent]): F[Unit]
}

object Blocks {

  def make[F[_]: Monad, D[_]: Monad](implicit
    bundle: PersistBundle[D],
    txr: Txr[F, D],
    assets: AssetsService[F],
    logs: Logging.Make[F]
  ): Blocks[F] =
    logs.forService[Blocks[F]].map(implicit __ => new Tracing[F] attach new Live[F, D])

  final private class Live[F[_]: Monad, D[_]: Monad](implicit
    bundle: PersistBundle[D],
    txr: Txr[F, D],
    assets: AssetsService[F]
  ) extends Blocks[F] {

    def process(events: NonEmptyList[BlockEvent]): F[Unit] = {
      def run: D[Int] = events
        .traverse {
          case BlockEvent.Apply(block)   => bundle.blocks.insert(block)
          case BlockEvent.Unapply(block) => bundle.blocks.resolve(block)
        }
        .map(_.toList.sum)

      run.trans.void
    }
  }

  final private class Tracing[F[_]: Monad: Logging] extends Blocks[Mid[F, *]] {

    def process(events: NonEmptyList[BlockEvent]): Mid[F, Unit] =
      for {
        _ <- info"Going to process next block events: $events"
        r <- _
        _ <- info"Blocks result is: $r"
      } yield r
  }
}
