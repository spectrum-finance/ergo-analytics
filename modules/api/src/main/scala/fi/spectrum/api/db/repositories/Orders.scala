package fi.spectrum.api.db.repositories

import cats.tagless.syntax.functorK._
import cats.{FlatMap, Functor, Monad}
import doobie.ConnectionIO
import fi.spectrum.api.db.models.amm.{DepositInfo, SwapInfo}
import fi.spectrum.api.db.sql.AnalyticsSql
import fi.spectrum.api.v1.endpoints.models.{Paging, TimeWindow}
import fi.spectrum.api.v1.models.amm.Order._
import fi.spectrum.api.v1.models.amm.OrdersRequest
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.graphite.Metrics
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.time.Clock

trait Orders[F[_]] {
  def getSwapTxs(tw: TimeWindow): F[List[SwapInfo]]

  def getDepositTxs(tw: TimeWindow): F[List[DepositInfo]]

  def getAllOrders(paging: Paging, tw: TimeWindow, request: OrdersRequest): F[List[AnyOrder]]

  def getSwaps(paging: Paging, tw: TimeWindow, request: OrdersRequest): F[List[Swap]]

  def getDeposits(paging: Paging, tw: TimeWindow, request: OrdersRequest): F[List[Deposit]]

  def getRedeems(paging: Paging, tw: TimeWindow, request: OrdersRequest): F[List[Redeem]]

  def getLocks(paging: Paging, tw: TimeWindow, request: OrdersRequest): F[List[Lock]]

}

object Orders {

  implicit def representableK: RepresentableK[Orders] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Functor, D[_]: Monad: LiftConnectionIO: Clock](implicit
    elh: EmbeddableLogHandler[D],
    metrics: Metrics[D],
    logs: Logs[I, D]
  ): I[Orders[D]] =
    logs.forService[Orders[D]].map { implicit l =>
      elh.embed { implicit lh =>
        new OrdersMetrics[D] attach (
          new OrdersTracing[D] attach new Live(new AnalyticsSql()).mapK(LiftConnectionIO[D].liftF)
        )
      }
    }

  final class Live(sql: AnalyticsSql) extends Orders[ConnectionIO] {

    def getSwapTxs(tw: TimeWindow): ConnectionIO[List[SwapInfo]] =
      sql.getSwapTransactions(tw).to[List]

    def getDepositTxs(tw: TimeWindow): ConnectionIO[List[DepositInfo]] =
      sql.getDepositTransactions(tw).to[List]

    def getAllOrders(paging: Paging, tw: TimeWindow, request: OrdersRequest): ConnectionIO[List[AnyOrder]] =
      sql
        .getAllOrders(
          request.addresses,
          paging.offset,
          paging.limit,
          tw,
          request.orderStatus,
          request.assetId,
          request.txId
        )
        .to[List]

    def getSwaps(paging: Paging, tw: TimeWindow, request: OrdersRequest): ConnectionIO[List[Swap]] =
      sql
        .getSwaps(
          request.addresses,
          paging.offset,
          paging.limit,
          tw,
          request.orderStatus,
          request.assetId,
          request.txId
        )
        .to[List]

    def getDeposits(paging: Paging, tw: TimeWindow, request: OrdersRequest): ConnectionIO[List[Deposit]] =
      sql
        .getDeposits(
          request.addresses,
          paging.offset,
          paging.limit,
          tw,
          request.orderStatus,
          request.assetId,
          request.txId
        )
        .to[List]

    def getRedeems(paging: Paging, tw: TimeWindow, request: OrdersRequest): ConnectionIO[List[Redeem]] =
      sql
        .getRedeems(
          request.addresses,
          paging.offset,
          paging.limit,
          tw,
          request.orderStatus,
          request.assetId,
          request.txId
        )
        .to[List]

    def getLocks(paging: Paging, tw: TimeWindow, request: OrdersRequest): ConnectionIO[List[Lock]] =
      sql
        .getLocks(
          request.addresses,
          paging.offset,
          paging.limit,
          tw,
          request.orderStatus,
          request.assetId,
          request.txId
        )
        .to[List]

  }

  final class OrdersTracing[F[_]: FlatMap: Logging] extends Orders[Mid[F, *]] {

    def getSwapTxs(tw: TimeWindow): Mid[F, List[SwapInfo]] =
      for {
        _ <- trace"swaps(window=$tw)"
        r <- _
        _ <- trace"swaps(window=$tw) -> ${r.size} swap entities selected"
      } yield r

    def getDepositTxs(tw: TimeWindow): Mid[F, List[DepositInfo]] =
      for {
        _ <- trace"deposits(window=$tw)"
        r <- _
        _ <- trace"deposits(window=$tw) -> ${r.size} deposit entities selected"
      } yield r

    def getAllOrders(paging: Paging, tw: TimeWindow, request: OrdersRequest): Mid[F, List[AnyOrder]] =
      for {
        _ <- trace"allOrders(window=$tw, paging=$paging, request=$request)"
        r <- _
        _ <- trace"allOrders(window=$tw, paging=$paging, request=$request) -> ${r.size} orders entities selected"
      } yield r

    def getSwaps(paging: Paging, tw: TimeWindow, request: OrdersRequest): Mid[F, List[Swap]] =
      for {
        _ <- trace"swaps(window=$tw, paging=$paging, request=$request)"
        r <- _
        _ <- trace"swaps(window=$tw, paging=$paging, request=$request) -> ${r.size} swap entities selected"
      } yield r

    def getDeposits(paging: Paging, tw: TimeWindow, request: OrdersRequest): Mid[F, List[Deposit]] =
      for {
        _ <- trace"deposits(window=$tw, paging=$paging, request=$request)"
        r <- _
        _ <- trace"deposits(window=$tw, paging=$paging, request=$request) -> ${r.size} deposit entities selected"
      } yield r

    def getRedeems(paging: Paging, tw: TimeWindow, request: OrdersRequest): Mid[F, List[Redeem]] =
      for {
        _ <- trace"redeems(window=$tw, paging=$paging, request=$request)"
        r <- _
        _ <- trace"redeems(window=$tw, paging=$paging, request=$request) -> ${r.size} redeem entities selected"
      } yield r

    def getLocks(paging: Paging, tw: TimeWindow, request: OrdersRequest): Mid[F, List[Lock]] =
      for {
        _ <- trace"locks(window=$tw, paging=$paging, request=$request)"
        r <- _
        _ <- trace"locks(window=$tw, paging=$paging, request=$request) -> ${r.size} lock entities selected"
      } yield r
  }

  final class OrdersMetrics[F[_]: Monad: Clock](implicit metrics: Metrics[F]) extends Orders[Mid[F, *]] {

    def getSwapTxs(tw: TimeWindow): Mid[F, List[SwapInfo]] =
      processMetric(_, s"db.orders.swapTxs")

    def getDepositTxs(tw: TimeWindow): Mid[F, List[DepositInfo]] =
      processMetric(_, s"db.orders.depositTxs")

    def getAllOrders(paging: Paging, tw: TimeWindow, request: OrdersRequest): Mid[F, List[AnyOrder]] =
      processMetric(_, s"db.orders.allOrders")

    def getSwaps(paging: Paging, tw: TimeWindow, request: OrdersRequest): Mid[F, List[Swap]] =
      processMetric(_, s"db.orders.swaps")

    def getDeposits(paging: Paging, tw: TimeWindow, request: OrdersRequest): Mid[F, List[Deposit]] =
      processMetric(_, s"db.orders.deposits")

    def getRedeems(paging: Paging, tw: TimeWindow, request: OrdersRequest): Mid[F, List[Redeem]] =
      processMetric(_, s"db.orders.redeems")

    def getLocks(paging: Paging, tw: TimeWindow, request: OrdersRequest): Mid[F, List[Lock]] =
      processMetric(_, s"db.orders.locks")
  }
}
