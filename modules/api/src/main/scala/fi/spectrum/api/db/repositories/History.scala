package fi.spectrum.api.db.repositories

import cats.tagless.syntax.functorK._
import cats.{FlatMap, Functor, Monad}
import doobie.ConnectionIO
import fi.spectrum.api.db.sql.HistorySql
import fi.spectrum.api.models.{RegisterDeposit, RegisterRedeem, RegisterSwap}
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.graphite.Metrics
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.time.Clock

trait History[F[_]] {
  def swapRegister(orderId: OrderId): F[Option[RegisterSwap]]
  def redeemRegister(orderId: OrderId): F[Option[RegisterRedeem]]
  def depositRegister(orderId: OrderId): F[Option[RegisterDeposit]]
}

object History {

  implicit def representableK: RepresentableK[History] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Functor, D[_]: Monad: LiftConnectionIO: Clock](implicit
    elh: EmbeddableLogHandler[D],
    metrics: Metrics[D],
    logs: Logs[I, D]
  ): I[History[D]] =
    logs.forService[History[D]].map { implicit l =>
      elh.embed(implicit lh =>
        new HistoryMetrics[D] attach (
          new HistoryTracing[D] attach new Live(new HistorySql()).mapK(LiftConnectionIO[D].liftF)
        )
      )
    }

  final class Live(sql: HistorySql) extends History[ConnectionIO] {

    def swapRegister(orderId: OrderId): ConnectionIO[Option[RegisterSwap]] =
      sql.swapRegister(orderId).option

    def redeemRegister(orderId: OrderId): ConnectionIO[Option[RegisterRedeem]] =
      sql.redeemRegister(orderId).option

    def depositRegister(orderId: OrderId): ConnectionIO[Option[RegisterDeposit]] =
      sql.depositRegister(orderId).option
  }

  final class HistoryMetrics[F[_]: Monad: Clock](implicit metrics: Metrics[F]) extends History[Mid[F, *]] {

    def swapRegister(orderId: OrderId): Mid[F, Option[RegisterSwap]] =
      processMetric(_, s"db.history.swap.$orderId")

    def redeemRegister(orderId: OrderId): Mid[F, Option[RegisterRedeem]] =
      processMetric(_, s"db.history.redeem.$orderId")

    def depositRegister(orderId: OrderId): Mid[F, Option[RegisterDeposit]] =
      processMetric(_, s"db.history.deposit.$orderId")
  }

  final class HistoryTracing[F[_]: FlatMap: Logging] extends History[Mid[F, *]] {

    def swapRegister(orderId: OrderId): Mid[F, Option[RegisterSwap]] =
      for {
        _ <- trace"swapRegister(orderId=$orderId)"
        r <- _
        _ <- trace"swapRegister(orderId=$orderId) -> $r"
      } yield r

    def redeemRegister(orderId: OrderId): Mid[F, Option[RegisterRedeem]] =
      for {
        _ <- trace"redeemRegister(orderId=$orderId)"
        r <- _
        _ <- trace"redeemRegister(orderId=$orderId) -> $r"
      } yield r

    def depositRegister(orderId: OrderId): Mid[F, Option[RegisterDeposit]] =
      for {
        _ <- trace"depositRegister(orderId=$orderId)"
        r <- _
        _ <- trace"depositRegister(orderId=$orderId) -> $r"
      } yield r
  }
}
