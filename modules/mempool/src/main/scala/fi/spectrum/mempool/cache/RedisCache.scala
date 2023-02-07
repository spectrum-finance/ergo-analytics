package fi.spectrum.mempool.cache

import dev.profunktor.redis4cats.RedisCommands

import scala.concurrent.duration.FiniteDuration

trait RedisCache[F[_]] {
  def keys(key: String): F[List[String]]
  def transact_(fs: List[F[Unit]]): F[Unit]
  def del(key: String): F[Long]
  def setEx(key: String, value: String, expiresIn: FiniteDuration): F[Unit]
  def get(key: String): F[Option[String]]
}

object RedisCache {

  def make[F[_]](implicit redis: RedisCommands[F, String, String]): RedisCache[F] = new Live[F]

  final private class Live[F[_]](implicit redis: RedisCommands[F, String, String]) extends RedisCache[F] {

    def keys(key: String): F[List[String]] = redis.keys(key)

    def transact_(fs: List[F[Unit]]): F[Unit] = redis.transact_(fs)

    def del(key: String): F[Long] = redis.del(key)

    def setEx(key: String, value: String, expiresIn: FiniteDuration): F[Unit] = redis.setEx(key, value, expiresIn)

    def get(key: String): F[Option[String]] = redis.get(key)
  }
}
