package fi.spectrum.core.db

import cats.effect.syntax.resource._
import cats.effect.{Async, Resource, Sync}
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import fi.spectrum.core.db.PgConfig.access

object PostgresTransactor {

  def make[F[_]: Async: PgConfig.Has](poolName: String): Resource[F, HikariTransactor[F]] =
    for {
      config <- access.toResource
      cp     <- ExecutionContexts.fixedThreadPool(size = config.maxConnections)
      xa <- HikariTransactor.newHikariTransactor[F](
              driverClassName = "org.postgresql.Driver",
              config.url,
              config.user,
              config.pass,
              cp
            )
      _ <- Resource.eval(configure(xa)(poolName, config))
    } yield xa

  private def configure[F[_]: Sync](
    xa: HikariTransactor[F]
  )(name: String, config: PgConfig): F[Unit] =
    xa.configure { c =>
      Sync[F].delay {
        c.setAutoCommit(false)
        c.setPoolName(name)
        c.setMaxLifetime(600000)
        c.setIdleTimeout(30000)
        c.setMaximumPoolSize(config.maxConnections)
        c.setMinimumIdle(config.minConnections)
      }
    }
}
