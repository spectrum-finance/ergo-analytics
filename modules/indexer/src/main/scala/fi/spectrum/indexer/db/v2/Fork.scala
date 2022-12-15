package fi.spectrum.indexer.db.v2

import cats.Monad
import cats.data.NonEmptyList
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.core.domain.order.OrderStatus.{Executed, Refunded, Registered}
import fi.spectrum.core.domain.order.{Order, OrderId}
import tofu.doobie.transactor.Txr
import tofu.syntax.monadic._
import tofu.syntax.doobie.txr._

//todo pools
trait Fork[F[_]] {
  def resolve(nel: NonEmptyList[ProcessedOrder]): F[Unit]
}

object Fork {

  def make[F[_]: Monad, D[_]: Monad](implicit persists: PersistBundle[D], txr: Txr[F, D]): Fork[F] =
    new Live[F, D]

  final private class Live[F[_]: Monad, D[_]: Monad](implicit persists: PersistBundle[D], txr: Txr[F, D])
    extends Fork[F] {
    import persists._

    def resolve(nel: NonEmptyList[ProcessedOrder]): F[Unit] = {
      for {
        _ <- process(nel, persists.swaps)
        _ <- process(nel, persists.deposits)
        _ <- process(nel, persists.redeems)
        _ <- process(nel, persists.locks)
        _ <- offChainFee.delete(nel.toList.flatMap(_.offChainFee).map(_.outputId))
      } yield ()
    }.trans

    //todo drop Order.AnySwap
    private def process[O](
      elems: NonEmptyList[ProcessedOrder],
      persist: Persist[O, OrderId, D]
    ): D[Unit] = {

      val (registered, refunded, executed) =
        elems.foldLeft((List.empty[OrderId], List.empty[OrderId], List.empty[OrderId])) {
          case ((acc1, acc2, acc3), ProcessedOrder(order: Order.AnySwap, state, _, _, _))
              if state.status.in(Registered) =>
            (order.id :: acc1, acc2, acc3)
          case ((acc1, acc2, acc3), ProcessedOrder(order: Order.AnySwap, state, _, _, _))
              if state.status.in(Refunded) =>
            (acc1, order.id :: acc2, acc3)
          case ((acc1, acc2, acc3), ProcessedOrder(order: Order.AnySwap, state, _, _, _))
              if state.status.in(Executed) =>
            (acc1, acc2, order.id :: acc3)
          case (acc, _) => acc
        }

      for {
        _ <- persist.delete(registered)
        _ <- persist.deleteExecuted(executed)
        _ <- persist.deleteRefunded(refunded)
      } yield ()
    }
  }
}
