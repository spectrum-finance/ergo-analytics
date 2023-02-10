package fi.spectrum.api.db

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.dimafeng.testcontainers.PostgreSQLContainer
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.flywaydb.core.Flyway
import org.scalactic.source.Position
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, TestSuite}
import org.testcontainers.utility.DockerImageName
import tofu.doobie.transactor.Txr
import tofu.doobie.transactor.Txr.Plain

trait PGContainer extends BeforeAndAfter with BeforeAndAfterAll {
  self: TestSuite =>

  implicit lazy val xa: Transactor[IO] =
    Transactor.fromDriverManager[IO](
      container.driverClassName,
      container.jdbcUrl,
      container.username,
      container.password
    )

  implicit lazy val txr: Plain[IO] = Txr.plain(xa)

  private lazy val container: PostgreSQLContainer =
    PostgreSQLContainer(
      DockerImageName.parse("postgres:11-alpine"),
      databaseName = "analytics",
      username     = "postgres"
    )

  private lazy val flyway: Flyway =
    Flyway
      .configure()
      .sqlMigrationSeparator("__")
      .locations("classpath:db")
      .dataSource(container.jdbcUrl, container.username, container.password)
      .load()

  override def beforeAll(): Unit = {
    container.container.start()
    flyway.migrate()
  }

  override def afterAll(): Unit =
    container.container.stop()

  override def after(fun: => Any)(implicit pos: Position): Unit =
    truncateAll()

  private def truncateAll(): Unit =
    sql"""
         |truncate pools;
         |truncate lm_pools;
         |truncate swaps;
         |truncate redeems;
         |truncate deposits;
         |truncate lq_locks;
         |truncate off_chain_fee;
         |truncate lm_deposits;
         |truncate lm_compound;
         |""".stripMargin.update.run.transact(xa).unsafeRunSync()
}
