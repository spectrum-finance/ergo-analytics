package fi.spectrum.indexer.mocks

import cats.Applicative
import fi.spectrum.indexer.db.local.storage.RedisCache
import tofu.syntax.monadic._

import scala.concurrent.duration.FiniteDuration

object RedisCacheMock {

  def make[F[_]: Applicative]: RedisCache[F] = new RedisCache[F] {
    def keys(key: String): F[List[String]] = List.empty[String].pure

    def transact_(fs: List[F[Unit]]): F[Unit] = unit

    def del(key: String): F[Long] = 0L.pure

    def setEx(key: String, value: String, expiresIn: FiniteDuration): F[Unit] = unit

    def get(key: String): F[Option[String]] = Option.empty[String].pure
  }
}
