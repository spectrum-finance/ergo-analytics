package fi.spectrum.mempool.cache

import cats.Monad
import cats.effect.kernel.GenConcurrent
import cats.effect.std.Semaphore
import dev.profunktor.redis4cats.RedisCommands
import dev.profunktor.redis4cats.tx.TxStore
import tofu.syntax.monadic._

import scala.concurrent.duration.FiniteDuration

trait RedisCache[F[_]] {
  def keys(key: String): F[List[String]]
  def transact_(fs: List[F[Unit]]): F[Unit]
  def del(key: String): F[Long]
  def setEx(key: String, value: String, expiresIn: FiniteDuration): F[Unit]
  def get(key: String): F[Option[String]]
}

object RedisCache {

  def make[F[_]: GenConcurrent[*[_], Throwable]](implicit redis: RedisCommands[F, String, String]): F[RedisCache[F]] =
    Semaphore[F](1).map(new Live[F](_))

  final private class Live[F[_]: Monad](semaphore: Semaphore[F])(implicit redis: RedisCommands[F, String, String])
    extends RedisCache[F] {

    def keys(key: String): F[List[String]] = redis.keys(key)

    def transact_(fs: List[F[Unit]]): F[Unit] =
      for {
        _ <- semaphore.acquire
        _ <- redis.transact_(fs)
        _ <- semaphore.release
      } yield ()

    def del(key: String): F[Long] = redis.del(key)

    def setEx(key: String, value: String, expiresIn: FiniteDuration): F[Unit] = redis.setEx(key, value, expiresIn)

    def get(key: String): F[Option[String]] = redis.get(key)
  }
}
