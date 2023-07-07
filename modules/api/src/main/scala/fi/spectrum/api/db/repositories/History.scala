package fi.spectrum.api.db.repositories

import cats.tagless.syntax.functorK._
import cats.{FlatMap, Functor, Monad}
import doobie.ConnectionIO
import fi.spectrum.api.db.models.OrderDB._
import fi.spectrum.api.db.models._
import fi.spectrum.api.db.sql.HistorySql
import fi.spectrum.api.v1.endpoints.models.{Paging, TimeWindow}
import fi.spectrum.api.v1.models.history.HistoryApiQuery
import fi.spectrum.core.domain.{Address, PubKey}
import fi.spectrum.core.domain.address._
import fi.spectrum.core.domain.order.OrderId
import fi.spectrum.graphite.Metrics
import org.ergoplatform.ErgoAddressEncoder
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.higherKind.{Mid, RepresentableK}
import tofu.logging.{Logging, Logs}
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.time.Clock

trait History[F[_]] {
  def swapRegister(orderId: OrderId): F[Option[RegisterSwap]]
  def redeemRegister(orderId: OrderId): F[Option[RegisterRedeem]]
  def depositRegister(orderId: OrderId): F[Option[RegisterDeposit]]
  def lmDepositRegister(orderId: OrderId): F[Option[RegisterLmDeposit]]
  def lmRedeemRegister(orderId: OrderId): F[Option[RegisterLmRedeem]]

  def getAllAddresses(paging: Paging): F[List[PubKey]]

  def countAllAddresses: F[Long]

  def getAnyOrders(
    paging: Paging,
    tw: TimeWindow,
    request: HistoryApiQuery,
    skipOrders: List[OrderId]
  ): F[List[AnyOrderDB]]
  def getSwaps(paging: Paging, tw: TimeWindow, request: HistoryApiQuery, skipOrders: List[OrderId]): F[List[SwapDB]]

  def getAmmDeposits(
    paging: Paging,
    tw: TimeWindow,
    request: HistoryApiQuery,
    skipOrders: List[OrderId]
  ): F[List[AmmDepositDB]]

  def getLmDeposits(
    paging: Paging,
    tw: TimeWindow,
    request: HistoryApiQuery,
    skipOrders: List[OrderId]
  ): F[List[LmDepositDB]]

  def getAmmRedeems(
    paging: Paging,
    tw: TimeWindow,
    request: HistoryApiQuery,
    skipOrders: List[OrderId]
  ): F[List[AmmRedeemDB]]

  def getLmRedeems(
    paging: Paging,
    tw: TimeWindow,
    request: HistoryApiQuery,
    skipOrders: List[OrderId]
  ): F[List[LmRedeemsDB]]
  def getLocks(paging: Paging, tw: TimeWindow, request: HistoryApiQuery, skipOrders: List[OrderId]): F[List[LockDB]]
  def addressCount(list: List[Address]): F[Long]
}

object History {

  implicit def representableK: RepresentableK[History] =
    tofu.higherKind.derived.genRepresentableK

  def make[I[_]: Functor, D[_]: Monad: LiftConnectionIO: Clock](implicit
    elh: EmbeddableLogHandler[D],
    metrics: Metrics[D],
    e: ErgoAddressEncoder,
    logs: Logs[I, D]
  ): I[History[D]] =
    logs.forService[History[D]].map { implicit l =>
      elh.embed(implicit lh =>
        new HistoryMetrics[D] attach (
          new HistoryTracing[D] attach new Live(new HistorySql()).mapK(LiftConnectionIO[D].liftF)
        )
      )
    }

  final class Live(sql: HistorySql)(implicit e: ErgoAddressEncoder) extends History[ConnectionIO] {

    def addressCount(list: List[Address]): ConnectionIO[Long] = {
      val keys = list.flatMap(formPKRedeemer).map(_.value)
      sql.addressCount(keys).option.map(_.getOrElse(0L))
    }

    def getAllAddresses(paging: Paging): ConnectionIO[List[PubKey]] =
      sql.getAllAddresses(paging).to[List]

    def countAllAddresses: ConnectionIO[Long] =
      sql.countAllAddresses.option.map(_.getOrElse(0L))

    def lmDepositRegister(orderId: OrderId): ConnectionIO[Option[RegisterLmDeposit]] =
      sql.lmDepositRegister(orderId).option

    def lmRedeemRegister(orderId: OrderId): ConnectionIO[Option[RegisterLmRedeem]] =
      sql.lmRedeemRegister(orderId).option

    def swapRegister(orderId: OrderId): ConnectionIO[Option[RegisterSwap]] =
      sql.swapRegister(orderId).option

    def redeemRegister(orderId: OrderId): ConnectionIO[Option[RegisterRedeem]] =
      sql.redeemRegister(orderId).option

    def depositRegister(orderId: OrderId): ConnectionIO[Option[RegisterDeposit]] =
      sql.depositRegister(orderId).option

    def getAnyOrders(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): ConnectionIO[List[AnyOrderDB]] = {
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
        .to[List]
    }

    def getLmRedeems(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): ConnectionIO[List[LmRedeemsDB]] = {
      val keys = request.addresses.flatMap(formPKRedeemer).map(_.value)
      sql.getLmRedeems(keys, paging.offset, paging.limit, tw, request.orderStatus, request.txId, skipOrders).to[List]
    }

    def getLmDeposits(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): ConnectionIO[List[LmDepositDB]] = {
      val keys = request.addresses.flatMap(formPKRedeemer).map(_.value)
      sql.getLmDeposits(keys, paging.offset, paging.limit, tw, request.orderStatus, request.txId, skipOrders).to[List]
    }

    def getSwaps(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): ConnectionIO[List[SwapDB]] = {
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
        .to[List]
    }

    def getAmmDeposits(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): ConnectionIO[List[AmmDepositDB]] = {
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
        .to[List]
    }

    def getAmmRedeems(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): ConnectionIO[List[AmmRedeemDB]] = {
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
        .to[List]
    }

    def getLocks(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): ConnectionIO[List[LockDB]] = {
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
        .to[List]
    }
  }

  final class HistoryMetrics[F[_]: Monad: Clock](implicit metrics: Metrics[F]) extends History[Mid[F, *]] {

    def countAllAddresses: Mid[F, Long] =
      processMetric(_, s"db.history.addresses.count")

    def getAllAddresses(paging: Paging): Mid[F, List[PubKey]] =
      processMetric(_, s"db.history.addresses")

    def swapRegister(orderId: OrderId): Mid[F, Option[RegisterSwap]] =
      processMetric(_, s"db.history.mempool.swap")

    def lmDepositRegister(orderId: OrderId): Mid[F, Option[RegisterLmDeposit]] =
      processMetric(_, s"db.history.mempool.lm.deposit")

    def redeemRegister(orderId: OrderId): Mid[F, Option[RegisterRedeem]] =
      processMetric(_, s"db.history.mempool.redeem")

    def depositRegister(orderId: OrderId): Mid[F, Option[RegisterDeposit]] =
      processMetric(_, s"db.history.mempool.deposit")

    def getAnyOrders(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[AnyOrderDB]] =
      processMetric(_, s"db.history.any")

    def getSwaps(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[SwapDB]] =
      processMetric(_, s"db.history.swap")

    def getAmmDeposits(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[AmmDepositDB]] =
      processMetric(_, s"db.history.deposit")

    def getAmmRedeems(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[AmmRedeemDB]] =
      processMetric(_, s"db.history.redeem")

    def getLocks(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[LockDB]] =
      processMetric(_, s"db.history.lock")

    def addressCount(list: List[Address]): Mid[F, Long] =
      processMetric(_, s"db.history.order.count")

    def getLmDeposits(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[LmDepositDB]] =
      processMetric(_, s"db.history.lm.deposit")

    def lmRedeemRegister(orderId: OrderId): Mid[F, Option[RegisterLmRedeem]] =
      processMetric(_, s"db.history.lm.redeem")

    def getLmRedeems(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[LmRedeemsDB]] =
      processMetric(_, s"db.history.lm.redeems")
  }

  final class HistoryTracing[F[_]: FlatMap: Logging] extends History[Mid[F, *]] {

    def countAllAddresses: Mid[F, Long] =
      for {
        _ <- trace"countAllAddresses()"
        r <- _
        _ <- trace"countAllAddresses() -> $r"
      } yield r

    def getAllAddresses(paging: Paging): Mid[F, List[PubKey]] =
      for {
        _ <- trace"getAllAddresses(paging=$paging)"
        r <- _
        _ <- trace"getAllAddresses(paging=$paging) -> $r"
      } yield r

    def swapRegister(orderId: OrderId): Mid[F, Option[RegisterSwap]] =
      for {
        _ <- trace"swapRegister(orderId=$orderId)"
        r <- _
        _ <- trace"swapRegister(orderId=$orderId) -> $r"
      } yield r

    def redeemRegister(orderId: OrderId): Mid[F, Option[RegisterRedeem]] =
      for {
        _ <- trace"redeemRegister(orderId=$orderId)"
        r <- _
        _ <- trace"redeemRegister(orderId=$orderId) -> $r"
      } yield r

    def depositRegister(orderId: OrderId): Mid[F, Option[RegisterDeposit]] =
      for {
        _ <- trace"depositRegister(orderId=$orderId)"
        r <- _
        _ <- trace"depositRegister(orderId=$orderId) -> $r"
      } yield r

    def getAnyOrders(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[AnyOrderDB]] =
      for {
        _ <- info"getAnyOrders(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getAnyOrders(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.orderId)}"
      } yield r

    def getSwaps(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[SwapDB]] =
      for {
        _ <- info"getSwaps(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getSwaps(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.id)}"
      } yield r

    def getAmmDeposits(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[AmmDepositDB]] =
      for {
        _ <- info"getDeposits(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getDeposits(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.orderId)}"
      } yield r

    def getAmmRedeems(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[AmmRedeemDB]] =
      for {
        _ <- info"getRedeems(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getRedeems(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.id)}"
      } yield r

    def getLocks(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[LockDB]] =
      for {
        _ <- info"getLocks(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getLocks(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.id)}"
      } yield r

    def addressCount(list: List[Address]): Mid[F, Long] =
      for {
        _ <- info"totalAddressOrders(list=$list)"
        r <- _
        _ <- info"totalAddressOrders(list=$list) -> $r"
      } yield r

    def getLmDeposits(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[LmDepositDB]] =
      for {
        _ <- info"getLmDeposits(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getLmDeposits(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.orderId)}"
      } yield r

    def lmDepositRegister(orderId: OrderId): Mid[F, Option[RegisterLmDeposit]] =
      for {
        _ <- trace"lmDepositRegister(orderId=$orderId)"
        r <- _
        _ <- trace"lmDepositRegister(orderId=$orderId) -> $r"
      } yield r

    def lmRedeemRegister(orderId: OrderId): Mid[F, Option[RegisterLmRedeem]] =
      for {
        _ <- trace"lmRedeemRegister(orderId=$orderId)"
        r <- _
        _ <- trace"lmRedeemRegister(orderId=$orderId) -> $r"
      } yield r

    def getLmRedeems(
      paging: Paging,
      tw: TimeWindow,
      request: HistoryApiQuery,
      skipOrders: List[OrderId]
    ): Mid[F, List[LmRedeemsDB]] =
      for {
        _ <- info"getLmRedeems(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getLmRedeems(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.orderId)}"
      } yield r
  }
}
