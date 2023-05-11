package fi.spectrum.mempool.cache

import dev.profunktor.redis4cats.RedisCommands
import dev.profunktor.redis4cats.effects.ScriptOutputType

trait RedisCache[F[_]] {
  def keys(key: String): F[List[String]]
  def get(key: String): F[Option[String]]
  def eval(script: String, keys: List[String], values: List[String]): F[Unit]
}

object RedisCache {

  def make[F[_]](implicit redis: RedisCommands[F, String, String]): RedisCache[F] =
    new Live[F]

  final private class Live[F[_]](implicit redis: RedisCommands[F, String, String]) extends RedisCache[F] {

    def keys(key: String): F[List[String]] = redis.keys(key)

    def get(key: String): F[Option[String]] = redis.get(key)

    def eval(script: String, keys: List[String], values: List[String]): F[Unit] =
      redis.eval(script, ScriptOutputType.Status, keys, values)
  }
}
