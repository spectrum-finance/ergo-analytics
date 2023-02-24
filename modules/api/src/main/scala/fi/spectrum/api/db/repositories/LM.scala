package fi.spectrum.api.db.repositories

import cats.data.NonEmptyList
import cats.tagless.syntax.functorK._
import cats.{FlatMap, Functor, Monad}
import doobie.ConnectionIO
import fi.spectrum.api.db.models.lm.{LmPoolSnapshot, UserDeposit, UserInterest}
import fi.spectrum.api.db.sql.LmAnalyticsSql
import fi.spectrum.core.domain.SErgoTree
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.graphite.Metrics
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.time.Clock

trait LM[F[_]] {
  def lmPoolsSnapshots: F[List[LmPoolSnapshot]]
  def userDeposit(trees: NonEmptyList[SErgoTree]): F[List[UserDeposit]]
  def userInterest(addresses: NonEmptyList[PublicKeyRedeemer]): F[List[UserInterest]]
}

object LM {

  implicit def representableK: RepresentableK[LM] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Functor, D[_]: Monad: LiftConnectionIO: Clock](implicit
    elh: EmbeddableLogHandler[D],
    metrics: Metrics[D],
    logs: Logs[I, D]
  ): I[LM[D]] =
    logs.forService[LM[D]].map { implicit l =>
      elh.embed(implicit lh =>
        new LMMetrics[D] attach (
          new Tracing[D] attach new Live(new LmAnalyticsSql()).mapK(LiftConnectionIO[D].liftF)
        )
      )
    }

  final class Live(sql: LmAnalyticsSql) extends LM[ConnectionIO] {

    def lmPoolsSnapshots: ConnectionIO[List[LmPoolSnapshot]] =
      sql.lmPoolSnapshots.to[List]

    def userDeposit(trees: NonEmptyList[SErgoTree]): ConnectionIO[List[UserDeposit]] =
      sql.userDeposit(trees).to[List]

    def userInterest(addresses: NonEmptyList[PublicKeyRedeemer]): ConnectionIO[List[UserInterest]] =
      sql.userInterest(addresses).to[List]
  }

  final class LMMetrics[F[_]: Monad: Clock](implicit metrics: Metrics[F]) extends LM[Mid[F, *]] {

    def lmPoolsSnapshots: Mid[F, List[LmPoolSnapshot]] =
      processMetric(_, s"db.lm.snapshots")

    def userDeposit(trees: NonEmptyList[SErgoTree]): Mid[F, List[UserDeposit]] =
      processMetric(_, s"db.lm.deposit")

    def userInterest(addresses: NonEmptyList[PublicKeyRedeemer]): Mid[F, List[UserInterest]] =
      processMetric(_, s"db.lm.compound")
  }

  final class Tracing[F[_]: FlatMap: Logging] extends LM[Mid[F, *]] {

    def lmPoolsSnapshots: Mid[F, List[LmPoolSnapshot]] =
      for {
        _ <- info"lmPoolsSnapshots()"
        r <- _
        _ <- info"lmPoolsSnapshots() -> ${r.length}"
      } yield r

    def userDeposit(trees: NonEmptyList[SErgoTree]): Mid[F, List[UserDeposit]] =
      for {
        _ <- info"userDeposit()"
        r <- _
        _ <- info"userDeposit() -> ${r.length}"
      } yield r

    def userInterest(addresses: NonEmptyList[PublicKeyRedeemer]): Mid[F, List[UserInterest]] =
      for {
        _ <- info"userInterest()"
        r <- _
        _ <- info"userInterest() -> ${r.length}"
      } yield r
  }
}
