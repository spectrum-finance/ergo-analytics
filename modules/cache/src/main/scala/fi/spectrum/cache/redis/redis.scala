package fi.spectrum.cache

import cats.effect.kernel.Async
import cats.effect.syntax.resource._
import cats.effect.{Resource, Sync}
import dev.profunktor.redis4cats.connection.RedisClient
import dev.profunktor.redis4cats.data.RedisCodec
import dev.profunktor.redis4cats.{Redis, RedisCommands}
import io.lettuce.core.{ClientOptions, TimeoutOptions}

import scala.jdk.DurationConverters.ScalaDurationOps

package object redis {

  def mkRedis[K, V, F[_]: Async: RedisConfig.Has](implicit
    codec: RedisCodec[K, V]
  ): Resource[F, RedisCommands[F, K, V]] = {
    import dev.profunktor.redis4cats.effect.Log.Stdout._
    for {
      config         <- RedisConfig.access.toResource
      timeoutOptions <- Sync[F].delay(TimeoutOptions.builder().fixedTimeout(config.timeout.toJava).build()).toResource
      clientOptions  <- Sync[F].delay(ClientOptions.builder().timeoutOptions(timeoutOptions).build()).toResource
      client         <- RedisClient[F].withOptions(s"redis://${config.password}@${config.host}:${config.port}", clientOptions)
      redisCmd       <- Redis[F].fromClient(client, codec)
    } yield redisCmd
  }
}
