package fi.spectrum.api.v1.services

import cats.{Functor, Monad}
import fi.spectrum.api.db.repositories.History
import fi.spectrum.api.v1.endpoints.models.{Paging, TimeWindow}
import fi.spectrum.api.v1.models.history.{ApiOrder, HistoryApiQuery, OrderHistoryResponse, OrderType}
import tofu.doobie.transactor.Txr
import tofu.syntax.monadic._
import tofu.syntax.doobie.txr._
import fi.spectrum.api.classes.ToAPI._
import fi.spectrum.api.v1.models.history.ApiOrder.{Deposit, Lock, Redeem, Swap}
import org.ergoplatform.ErgoAddressEncoder
import tofu.logging.Logs

trait HistoryApi[F[_]] {
  def orderHistory(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): F[OrderHistoryResponse]
}

object HistoryApi {

  def make[I[_]: Functor, F[_]: Monad, D[_]: Monad](implicit
    history: History[D],
    txr: Txr[F, D],
    e: ErgoAddressEncoder,
    logs: Logs[I, F]
  ): I[HistoryApi[F]] =
    logs.forService[MempoolApi[F]].map(implicit __ => new Live[F, D])

  final private class Live[F[_]: Monad, D[_]: Monad](implicit
    history: History[D],
    txr: Txr[F, D],
    e: ErgoAddressEncoder
  ) extends HistoryApi[F] {

    def orderHistory(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): F[OrderHistoryResponse] = {
      for {
        count <- history.totalAddressOrders(request.addresses)
        orders <- resolveOrderType(paging, tw, request)
      } yield OrderHistoryResponse(orders.sortBy(_.registerTx.ts)(Ordering[Long].reverse), count)
    }.trans

    private def resolveOrderType(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery
    ): D[List[ApiOrder]] = request.orderType match {
      case Some(OrderType.Swap) =>
        history
          .getSwaps(paging, tw, request)
          .map(_.flatMap(_.toApi[Swap]))
      case Some(OrderType.Deposit) => history.getDeposits(paging, tw, request).map(_.flatMap(_.toApi[Deposit]))
      case Some(OrderType.Redeem)  => history.getRedeems(paging, tw, request).map(_.flatMap(_.toApi[Redeem]))
      case Some(OrderType.Lock)    => history.getLocks(paging, tw, request).map(_.flatMap(_.toApi[Lock]))
      case None                    => history.getAnyOrders(paging, tw, request).map(_.flatMap(_.toApi[ApiOrder]))
    }
  }
}
