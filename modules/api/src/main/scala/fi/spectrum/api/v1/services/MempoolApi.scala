package fi.spectrum.api.v1.services

import cats.syntax.traverse._
import cats.{Functor, Monad}
import fi.spectrum.api.classes.ToAPI._
import fi.spectrum.api.db.repositories.History
import fi.spectrum.api.services.Network
import fi.spectrum.api.v1.models.history.ApiOrder.AmmDepositApi._
import fi.spectrum.api.v1.models.history.ApiOrder.AmmRedeemApi._
import fi.spectrum.api.v1.models.history.ApiOrder.LmDepositApi._
import fi.spectrum.api.v1.models.history.ApiOrder.LmCompoundApi._
import fi.spectrum.api.v1.models.history.ApiOrder.LmRedeemApi._
import fi.spectrum.api.v1.models.history.ApiOrder.Swap._
import fi.spectrum.api.v1.models.history.ApiOrder._
import fi.spectrum.api.v1.models.history.ApiOrder
import fi.spectrum.core.domain.Address
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.Order.Compound
import fi.spectrum.core.domain.order.Order.Deposit.{AmmDeposit, LmDeposit}
import fi.spectrum.core.domain.order.Order.Redeem.{AmmRedeem, LmRedeem}
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
    h: History[D],
    network: Network[F],
    txr: Txr[F, D],
    e: ErgoAddressEncoder
  ) extends MempoolApi[F] {

    //todo NEL
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
                  x.wined[AmmDeposit]
                    .traverse(d => h.depositRegister(d.order.id).flatMapIn(d.toApi(_)))
                    .orElseF(x.wined[AmmRedeem].traverse(r => h.redeemRegister(r.order.id).flatMapIn(r.toApi(_))))
                    .orElseF(x.wined[Order.Swap].traverse(s => h.swapRegister(s.order.id).flatMapIn(s.toApi(_))))
                    .orElseF(x.wined[LmDeposit].traverse(s => h.lmDepositRegister(s.order.id).flatMapIn(s.toApi(_))))
                    .orElseF(x.wined[Compound].traverse(s => h.lmCompoundRegister(s.order.id).flatMapIn(s.toApi(_))))
                    .orElseF(x.wined[LmRedeem].traverse(s => h.lmRedeemRegister(s.order.id).flatMapIn(s.toApi(_))))
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
