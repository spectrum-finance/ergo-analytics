package fi.spectrum.api.db.repositories

import cats.tagless.syntax.functorK._
import cats.{FlatMap, Functor, Monad}
import doobie.ConnectionIO
import fi.spectrum.api.db.models.OrderDB._
import fi.spectrum.api.db.models._
import fi.spectrum.api.db.sql.HistorySql
import fi.spectrum.api.v1.endpoints.models.{Paging, TimeWindow}
import fi.spectrum.api.v1.models.history.HistoryApiQuery
import fi.spectrum.core.domain.Address
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
  def lmCompoundRegister(orderId: OrderId): F[Option[RegisterCompound]]
  def getAnyOrders(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): F[List[AnyOrderDB]]
  def getSwaps(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): F[List[SwapDB]]
  def getAmmDeposits(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): F[List[AmmDepositDB]]
  def getLmDeposits(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): F[List[LmDepositDB]]
  def getAmmRedeems(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): F[List[AmmRedeemDB]]
  def getLmCompounds(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): F[List[LmCompoundDB]]
  def getLocks(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): F[List[LockDB]]
  def totalAddressOrders(list: List[Address]): F[Long]
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

    def totalAddressOrders(list: List[Address]): ConnectionIO[Long] = {
      val keys = list.flatMap(formPKRedeemer).map(_.value)
      sql.totalAddressOrders(keys).option.map(_.getOrElse(0L))
    }

    def lmDepositRegister(orderId: OrderId): ConnectionIO[Option[RegisterLmDeposit]] =
      sql.lmDepositRegister(orderId).option

    def swapRegister(orderId: OrderId): ConnectionIO[Option[RegisterSwap]] =
      sql.swapRegister(orderId).option

    def redeemRegister(orderId: OrderId): ConnectionIO[Option[RegisterRedeem]] =
      sql.redeemRegister(orderId).option

    def lmCompoundRegister(orderId: OrderId): ConnectionIO[Option[RegisterCompound]] =
      sql.lmCompoundRegister(orderId).option

    def depositRegister(orderId: OrderId): ConnectionIO[Option[RegisterDeposit]] =
      sql.depositRegister(orderId).option

    def getAnyOrders(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): ConnectionIO[List[AnyOrderDB]] = {
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
          request.tokenPair
        )
        .to[List]
    }

    def getLmDeposits(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): ConnectionIO[List[LmDepositDB]] = {
      val keys = request.addresses.flatMap(formPKRedeemer).map(_.value)
      sql.getLmDeposits(keys, paging.offset, paging.limit, tw, request.orderStatus, request.txId).to[List]
    }

    def getLmCompounds(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): ConnectionIO[List[LmCompoundDB]] = {
      val keys = request.addresses.flatMap(formPKRedeemer).map(_.value)
      sql.getLmCompound(keys, paging.offset, paging.limit, tw, request.orderStatus, request.txId).to[List]
    }

    def getSwaps(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): ConnectionIO[List[SwapDB]] = {
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
          request.tokenPair
        )
        .to[List]
    }

    def getAmmDeposits(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): ConnectionIO[List[AmmDepositDB]] = {
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
          request.tokenPair
        )
        .to[List]
    }

    def getAmmRedeems(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): ConnectionIO[List[AmmRedeemDB]] = {
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
          request.tokenPair
        )
        .to[List]
    }

    def getLocks(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): ConnectionIO[List[LockDB]] = {
      val keys = request.addresses.flatMap(formPKRedeemer).map(_.value)
      sql
        .getLocks(
          keys,
          paging.offset,
          paging.limit,
          tw,
          request.txId
        )
        .to[List]
    }
  }

  final class HistoryMetrics[F[_]: Monad: Clock](implicit metrics: Metrics[F]) extends History[Mid[F, *]] {

    def swapRegister(orderId: OrderId): Mid[F, Option[RegisterSwap]] =
      processMetric(_, s"db.history.mempool.swap")

    def lmDepositRegister(orderId: OrderId): Mid[F, Option[RegisterLmDeposit]] =
      processMetric(_, s"db.history.mempool.lm.deposit")

    def lmCompoundRegister(orderId: OrderId): Mid[F, Option[RegisterCompound]] =
      processMetric(_, s"db.history.mempool.lm.compound}")

    def redeemRegister(orderId: OrderId): Mid[F, Option[RegisterRedeem]] =
      processMetric(_, s"db.history.mempool.redeem")

    def depositRegister(orderId: OrderId): Mid[F, Option[RegisterDeposit]] =
      processMetric(_, s"db.history.mempool.deposit")

    def getAnyOrders(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[AnyOrderDB]] =
      processMetric(_, s"db.history.any")

    def getSwaps(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[SwapDB]] =
      processMetric(_, s"db.history.swap")

    def getAmmDeposits(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[AmmDepositDB]] =
      processMetric(_, s"db.history.deposit")

    def getAmmRedeems(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[AmmRedeemDB]] =
      processMetric(_, s"db.history.redeem")

    def getLocks(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[LockDB]] =
      processMetric(_, s"db.history.lock")

    def totalAddressOrders(list: List[Address]): Mid[F, Long] =
      processMetric(_, s"db.history.order.count")

    def getLmDeposits(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[LmDepositDB]] =
      processMetric(_, s"db.history.lm.deposit")

    def getLmCompounds(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[LmCompoundDB]] =
      processMetric(_, s"db.history.lm.compound")
  }

  final class HistoryTracing[F[_]: FlatMap: Logging] extends History[Mid[F, *]] {

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

    def getAnyOrders(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[AnyOrderDB]] =
      for {
        _ <- info"getAnyOrders(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getAnyOrders(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.orderId)}"
      } yield r

    def getSwaps(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[SwapDB]] =
      for {
        _ <- info"getSwaps(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getSwaps(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.id)}"
      } yield r

    def getAmmDeposits(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[AmmDepositDB]] =
      for {
        _ <- info"getDeposits(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getDeposits(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.orderId)}"
      } yield r

    def getAmmRedeems(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[AmmRedeemDB]] =
      for {
        _ <- info"getRedeems(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getRedeems(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.id)}"
      } yield r

    def getLocks(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[LockDB]] =
      for {
        _ <- info"getLocks(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getLocks(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.id)}"
      } yield r

    def totalAddressOrders(list: List[Address]): Mid[F, Long] =
      for {
        _ <- info"totalAddressOrders(list=$list)"
        r <- _
        _ <- info"totalAddressOrders(list=$list) -> $r"
      } yield r

    def getLmDeposits(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[LmDepositDB]] =
      for {
        _ <- info"getLmDeposits(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getLmDeposits(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.orderId)}"
      } yield r

    def getLmCompounds(paging: Paging, tw: TimeWindow, request: HistoryApiQuery): Mid[F, List[LmCompoundDB]] =
      for {
        _ <- info"getLmCompounds(paging=$paging, tw=$tw, request=$request)"
        r <- _
        _ <- info"getLmCompounds(paging=$paging, tw=$tw, request=$request) -> ${r.map(_.orderId)}"
      } yield r

    def lmDepositRegister(orderId: OrderId): Mid[F, Option[RegisterLmDeposit]] =
      for {
        _ <- trace"lmDepositRegister(orderId=$orderId)"
        r <- _
        _ <- trace"lmDepositRegister(orderId=$orderId) -> $r"
      } yield r

    def lmCompoundRegister(orderId: OrderId): Mid[F, Option[RegisterCompound]] =
      for {
        _ <- trace"lmCompoundRegister(orderId=$orderId)"
        r <- _
        _ <- trace"lmCompoundRegister(orderId=$orderId) -> $r"
      } yield r
  }
}
