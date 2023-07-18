package fi.spectrum.api.db.repositories

import cats.tagless.syntax.functorK._
import cats.{FlatMap, Functor, Monad}
import doobie.ConnectionIO
import fi.spectrum.api.db.models.OrderDB._
import fi.spectrum.api.db.models._
import fi.spectrum.api.db.sql.HistorySql
import fi.spectrum.api.v1.endpoints.models.{Paging, TimeWindow}
import fi.spectrum.api.v1.models.history.HistoryApiQuery
import fi.spectrum.core.domain.address._
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.graphite.Metrics
import org.ergoplatform.ErgoAddressEncoder
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.fs2.LiftStream
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.time.Clock
import tofu.time._
import tofu.fs2Instances
import tofu.syntax.lift._
import tofu.internal._
import tofu.streams._
import tofu.syntax.funk.funK

trait HistoryStreaming[S[_]] {

  def streamAnyOrders(
    paging: Paging,
    tw: TimeWindow,
    request: HistoryApiQuery,
    skipOrders: List[OrderId]
  ): S[AnyOrderDB]

  def streamSwaps(
    paging: Paging,
    tw: TimeWindow,
    request: HistoryApiQuery,
    skipOrders: List[OrderId]
  ): S[SwapDB]

  def streamAmmDeposits(
    paging: Paging,
    tw: TimeWindow,
    request: HistoryApiQuery,
    skipOrders: List[OrderId]
  ): S[AmmDepositDB]

  def streamLmDeposits(
    paging: Paging,
    tw: TimeWindow,
    request: HistoryApiQuery,
    skipOrders: List[OrderId]
  ): S[LmDepositDB]

  def streamAmmRedeems(
    paging: Paging,
    tw: TimeWindow,
    request: HistoryApiQuery,
    skipOrders: List[OrderId]
  ): S[AmmRedeemDB]

  def streamLmRedeems(
    paging: Paging,
    tw: TimeWindow,
    request: HistoryApiQuery,
    skipOrders: List[OrderId]
  ): S[LmRedeemsDB]

  def streamLocks(
    paging: Paging,
    tw: TimeWindow,
    request: HistoryApiQuery,
    skipOrders: List[OrderId]
  ): S[LockDB]
}

object HistoryStreaming {

  implicit def representableK: RepresentableK[HistoryStreaming] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Functor, S[_]: LiftStream[*[_], D], D[_]: Monad: LiftConnectionIO: Clock](implicit
    elh: EmbeddableLogHandler[D],
    metrics: Metrics[D],
    e: ErgoAddressEncoder,
    logs: Logs[I, D]
  ): I[HistoryStreaming[S]] =
    logs.forService[HistoryStreaming[S]].map { implicit l =>
      elh
        .embed(implicit lh =>
          new HistoryStreamingMetrics[D] attach (
            new HistoryStreamingTracing[D] attach new Live(new HistorySql())
              .mapK(funK[fs2.Stream[ConnectionIO, *], fs2.Stream[D, *]](_.translate(LiftConnectionIO[D].liftF)))
          )
        )
        .mapK(LiftStream[S, D].liftF)
    }

  final class Live(sql: HistorySql)(implicit e: ErgoAddressEncoder)
    extends HistoryStreaming[fs2.Stream[ConnectionIO, *]] {

    def streamAnyOrders(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): fs2.Stream[ConnectionIO, AnyOrderDB] = {
      val keys = request.addresses.flatMap(formPKRedeemer).map(_.value)
      sql
        .getAnyOrders(
          keys,
          paging.offset,
          paging.limit,
          tw,
          request.orderStatus,
          request.txId,
          request.tokenIds,
          request.tokenPair,
          skipOrders
        )
        .stream
    }

    def streamLmRedeems(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): fs2.Stream[ConnectionIO, LmRedeemsDB] = {
      val keys = request.addresses.flatMap(formPKRedeemer).map(_.value)
      sql.getLmRedeems(keys, paging.offset, paging.limit, tw, request.orderStatus, request.txId, skipOrders).stream
    }

    def streamLmDeposits(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): fs2.Stream[ConnectionIO, LmDepositDB] = {
      val keys = request.addresses.flatMap(formPKRedeemer).map(_.value)
      sql.getLmDeposits(keys, paging.offset, paging.limit, tw, request.orderStatus, request.txId, skipOrders).stream
    }

    def streamSwaps(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): fs2.Stream[ConnectionIO, SwapDB] = {
      val keys = request.addresses.flatMap(formPKRedeemer).map(_.value)
      sql
        .getSwaps(
          keys,
          paging.offset,
          paging.limit,
          tw,
          request.orderStatus,
          request.txId,
          request.tokenIds,
          request.tokenPair,
          skipOrders
        )
        .stream
    }

    def streamAmmDeposits(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): fs2.Stream[ConnectionIO, AmmDepositDB] = {
      val keys = request.addresses.flatMap(formPKRedeemer).map(_.value)
      sql
        .getDeposits(
          keys,
          paging.offset,
          paging.limit,
          tw,
          request.orderStatus,
          request.txId,
          request.tokenIds,
          request.tokenPair,
          skipOrders
        )
        .stream
    }

    def streamAmmRedeems(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): fs2.Stream[ConnectionIO, AmmRedeemDB] = {
      val keys = request.addresses.flatMap(formPKRedeemer).map(_.value)
      sql
        .getRedeems(
          keys,
          paging.offset,
          paging.limit,
          tw,
          request.orderStatus,
          request.txId,
          request.tokenIds,
          request.tokenPair,
          skipOrders
        )
        .stream
    }

    def streamLocks(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): fs2.Stream[ConnectionIO, LockDB] = {
      val keys = request.addresses.flatMap(formPKRedeemer).map(_.value)
      sql
        .getLocks(
          keys,
          paging.offset,
          paging.limit,
          tw,
          request.txId,
          skipOrders
        )
        .stream
    }
  }

  final class HistoryStreamingMetrics[F[_]: Monad: Clock](implicit metrics: Metrics[F])
    extends HistoryStreaming[Mid[fs2.Stream[F, *], *]] {

    override def streamAnyOrders(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], AnyOrderDB] =
      processMetric(_, s"db.history.stream.any")

    override def streamAmmDeposits(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], AmmDepositDB] =
      processMetric(_, s"db.history.stream.deposit")

    override def streamAmmRedeems(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], AmmRedeemDB] =
      processMetric(_, s"db.history.stream.redeem")

    override def streamLmDeposits(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], LmDepositDB] =
      processMetric(_, s"db.history.stream.lm.deposit")

    override def streamLocks(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], LockDB] =
      processMetric(_, s"db.history.stream.lock")

    override def streamLmRedeems(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], LmRedeemsDB] =
      processMetric(_, s"db.history.stream.lm.lock")

    override def streamSwaps(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], SwapDB] =
      processMetric(_, s"db.history.stream.swap")
  }

  final class HistoryStreamingTracing[F[_]: FlatMap: Logging] extends HistoryStreaming[Mid[fs2.Stream[F, *], *]] {

    override def streamAnyOrders(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], AnyOrderDB] =
      for {
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
        r <- _
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
      } yield r

    override def streamAmmDeposits(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], AmmDepositDB] =
      for {
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
        r <- _
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
      } yield r

    override def streamAmmRedeems(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], AmmRedeemDB] =
      for {
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
        r <- _
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
      } yield r

    override def streamLmDeposits(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], LmDepositDB] =
      for {
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
        r <- _
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
      } yield r

    override def streamLocks(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], LockDB] =
      for {
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
        r <- _
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
      } yield r

    override def streamLmRedeems(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], LmRedeemsDB] =
      for {
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
        r <- _
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
      } yield r

    override def streamSwaps(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[fs2.Stream[F, *], SwapDB] =
      for {
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
        r <- _
        _ <- fs2.Stream.eval(info"streamAnyOrders(paging=$paging, tw=$tw, request=$request)")
      } yield r
  }
}
