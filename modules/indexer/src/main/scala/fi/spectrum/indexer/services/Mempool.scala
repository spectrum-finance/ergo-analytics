package fi.spectrum.indexer.services

import cats.Monad
import cats.syntax.show._
import derevo.derive
import dev.profunktor.redis4cats.RedisCommands
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.OrderStatus._
import fi.spectrum.indexer.config.MempoolConfig
import io.circe.syntax._
import tofu.higherKind.Mid
import tofu.higherKind.derived.representableK
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._

@derive(representableK)
trait Mempool[F[_]] {
  def put(processed: Processed.Any): F[Unit]

  def del(processed: Processed.Any): F[Long]

  def del(processed: List[Processed.Any]): F[Long]
}

object Mempool {

  def make[F[_]: Monad: MempoolConfig.Has](implicit
    redis: RedisCommands[F, String, String],
    logs: Logging.Make[F]
  ): Mempool[F] =
    logs.forService[Mempool[F]].map(implicit __ => new Tracing[F] attach new Live[F])

  final private class Live[F[_]: Monad: MempoolConfig.Has](implicit redis: RedisCommands[F, String, String])
    extends Mempool[F] {

    def put(processed: Processed.Any): F[Unit] =
      MempoolConfig.access >>= (config => redis.setEx(mkKey(processed), processed.asJson.noSpaces, config.ttl))

    def del(processed: Processed.Any): F[Long] =
      redis.del(mkKey(processed))

    def del(processed: List[Processed.Any]): F[Long] =
      redis.del(processed.map(mkKey): _*)

    private def mkKey(processed: Processed.Any): String =
      s"${processed.order.id}_${processed.order.redeemer.show}_${mapToMempool(processed.state.status).entryName}"
  }

  final private class Tracing[F[_]: Monad: Logging] extends Mempool[Mid[F, *]] {

    def put(processed: Processed.Any): Mid[F, Unit] =
      for {
        _ <- info"Going to insert order $processed"
        _ <- _
        _ <- info"Insert finished."
      } yield ()

    def del(processed: Processed.Any): Mid[F, Long] =
      for {
        _ <- info"Going to delete order $processed"
        r <- _
        _ <- info"Delete finished."
      } yield r

    def del(processed: List[Processed.Any]): Mid[F, Long] =
      for {
        _ <- info"Going to delete many orders $processed"
        r <- _
        _ <- info"Delete many finished."
      } yield r
  }

}
