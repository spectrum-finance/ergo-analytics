package fi.spectrum.indexer.services

import cats.Monad
import cats.syntax.show._
import derevo.derive
import dev.profunktor.redis4cats.RedisCommands
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.core.domain.order.OrderStatus._
import fi.spectrum.indexer.config.MempoolConfig
import io.circe.syntax._
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait OrdersMempool[F[_]] {
  def put(processed: ProcessedOrder.Any): F[Unit]

  def del(processed: ProcessedOrder.Any): F[Long]

  def del(processed: List[ProcessedOrder.Any]): F[Long]
}

object OrdersMempool {

  def make[F[_]: Monad: MempoolConfig.Has](implicit
    redis: RedisCommands[F, String, String],
    logs: Logging.Make[F]
  ): OrdersMempool[F] =
    logs.forService[OrdersMempool[F]].map(implicit __ => new Tracing[F] attach new Live[F])

  final private class Live[F[_]: Monad: MempoolConfig.Has](implicit redis: RedisCommands[F, String, String])
    extends OrdersMempool[F] {

    def put(processed: ProcessedOrder.Any): F[Unit] =
      MempoolConfig.access >>= (config => redis.setEx(mkKey(processed), processed.asJson.noSpaces, config.ttl))

    def del(processed: ProcessedOrder.Any): F[Long] =
      redis.del(mkKey(processed))

    def del(processed: List[ProcessedOrder.Any]): F[Long] =
      redis.del(processed.map(mkKey): _*)

    private def mkKey(processed: ProcessedOrder.Any): String =
      s"${processed.order.id}_${processed.order.redeemer.show}_${mapToMempool(processed.state.status).entryName}"
  }

  final private class Tracing[F[_]: Monad: Logging] extends OrdersMempool[Mid[F, *]] {

    def put(processed: ProcessedOrder.Any): Mid[F, Unit] =
      for {
        _ <- info"Going to insert order $processed"
        _ <- _
        _ <- info"Insert finished."
      } yield ()

    def del(processed: ProcessedOrder.Any): Mid[F, Long] =
      for {
        _ <- info"Going to delete order $processed"
        r <- _
        _ <- info"Delete finished."
      } yield r

    def del(processed: List[ProcessedOrder.Any]): Mid[F, Long] =
      for {
        _ <- info"Going to delete many orders $processed"
        r <- _
        _ <- info"Delete many finished."
      } yield r
  }

}
