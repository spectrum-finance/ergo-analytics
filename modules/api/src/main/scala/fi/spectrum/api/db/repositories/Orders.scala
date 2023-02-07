package fi.spectrum.api.db.repositories

import cats.tagless.syntax.functorK._
import cats.{FlatMap, Functor, Monad}
import doobie.ConnectionIO
import fi.spectrum.api.db.models.amm.{DepositInfo, SwapInfo}
import fi.spectrum.api.db.sql.AnalyticsSql
import fi.spectrum.api.v1.endpoints.models.TimeWindow
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
  }

  final class OrdersMetrics[F[_]: Monad: Clock](implicit metrics: Metrics[F]) extends Orders[Mid[F, *]] {

    def getSwapTxs(tw: TimeWindow): Mid[F, List[SwapInfo]] =
      processMetric(_, s"db.orders.swapTxs")

    def getDepositTxs(tw: TimeWindow): Mid[F, List[DepositInfo]] =
      processMetric(_, s"db.orders.depositTxs")
  }
}
