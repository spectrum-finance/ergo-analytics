package fi.spectrum.api.services

import cats.{Functor, Monad}
import cats.syntax.traverse._
import fi.spectrum.api.classes.ToAPI._
import fi.spectrum.api.db.repositories.History
import fi.spectrum.api.v1.models.amm.ApiOrder
import fi.spectrum.api.v1.models.amm.ApiOrder.Deposit._
import fi.spectrum.api.v1.models.amm.ApiOrder.Redeem._
import fi.spectrum.api.v1.models.amm.ApiOrder.Swap._
import fi.spectrum.api.v1.models.amm.ApiOrder._
import fi.spectrum.core.domain.Address
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.OrderStatus.{Registered, WaitingRegistration}
import org.ergoplatform.ErgoAddressEncoder
import tofu.doobie.transactor.Txr
import tofu.logging.Logs
import tofu.syntax.doobie.txr._
import tofu.syntax.foption._
import tofu.syntax.monadic._

trait MempoolApi[F[_]] {
  def ordersByAddress(addresses: List[Address]): F[List[ApiOrder]]
}

object MempoolApi {

  def make[I[_]: Functor, F[_]: Monad, D[_]: Monad](implicit
    history: History[D],
    network: Network[F],
    txr: Txr[F, D],
    e: ErgoAddressEncoder,
    logs: Logs[I, F]
  ): I[MempoolApi[F]] =
    logs.forService[MempoolApi[F]].map(implicit __ => new Live[F, D])

  final private class Live[F[_]: Monad, D[_]: Monad](implicit
    history: History[D],
    network: Network[F],
    txr: Txr[F, D],
    e: ErgoAddressEncoder
  ) extends MempoolApi[F] {

    def ordersByAddress(addresses: List[Address]): F[List[ApiOrder]] =
      network.getMempoolData(addresses).flatMap { orders =>
        orders
          .traverse { data =>
            val grouped = data.orders.groupBy(_.order.id)
            grouped
              .map(_._2.toList)
              .toList
              .map {
                case x :: Nil if x.state.status.in(WaitingRegistration, Registered) => // process register
                  x.toApi.pure[D]
                case x :: Nil => // query db for register order
                  x.wined[Order.Deposit]
                    .traverse(d => history.depositRegister(d.order.id).flatMapIn(d.toApi(_)))
                    .orElseF(
                      x.wined[Order.Redeem].traverse(r => history.redeemRegister(r.order.id).flatMapIn(r.toApi(_)))
                    )
                    .orElseF(x.wined[Order.Swap].traverse(s => history.swapRegister(s.order.id).flatMapIn(s.toApi(_))))
                    .map(_.flatten)
                case x :: y :: Nil => // process as completed order
                  x.toApi(y).pure[D]
                case _ => // nothing to process
                  noneF[D, ApiOrder]
              }
              .sequence
              .map(_.flatten.sortBy(_.registerTx.ts)(Ordering[Long].reverse))
              .trans
          }
          .map(_.flatten)
      }
  }
}
