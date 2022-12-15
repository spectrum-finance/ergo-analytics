package fi.spectrum.indexer.services

import cats.Monad
import cats.data.NonEmptyList
import cats.syntax.traverse._
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.indexer.classes.syntax._
import fi.spectrum.indexer.db.persistence.Persist
import fi.spectrum.indexer.models.OffChainFeeDB
import fi.spectrum.indexer.models.OffChainFeeDB._
import tofu.doobie.transactor.Txr
import tofu.syntax.monadic._

//todo pools
trait ProcessOrder[F[_]] {
  def process(processedOrders: NonEmptyList[ProcessedOrder]): F[Unit]
}

object ProcessOrder {

  def make[F[_]: Monad, D[_]: Monad](
    inserts: List[InsertOrder[D]],
    offChainFee: Persist[OffChainFeeDB, D]
  )(implicit txr: Txr[F, D]): ProcessOrder[F] = new Live[F, D](inserts, offChainFee)

  final private class Live[F[_]: Monad, D[_]: Monad](
    inserts: List[InsertOrder[D]],
    offChainFee: Persist[OffChainFeeDB, D]
  )(implicit txr: Txr[F, D])
    extends ProcessOrder[F] {

    def process(processedOrders: NonEmptyList[ProcessedOrder]): F[Unit] = {

      def orders: D[List[Int]] =
        inserts.traverse(_.insert(processedOrders.toList))

      def fee: D[Int] =
        NonEmptyList.fromList(processedOrders.toList.flatMap(_.offChainFee).map(_.transform)) match {
          case Some(fee) => offChainFee.persist(fee)
          case None      => 0.pure[D]
        }

      txr.trans(orders >> fee).void
    }
  }
}
