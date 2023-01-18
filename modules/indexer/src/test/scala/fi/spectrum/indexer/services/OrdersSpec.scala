package fi.spectrum.indexer.services

import cats.data.NonEmptyList
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie.ConnectionIO
import doobie.implicits._
import doobie.util.update.Update0
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.indexer.db.models.{DepositDB, SwapDB, TxInfo}
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.indexer.db.{Indexer, PGContainer}
import fi.spectrum.indexer.mocks.OrdersMempoolMock
import fi.spectrum.indexer.repository.{Deposits, Swaps}
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.streaming.domain.OrderEvent
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import tofu.syntax.doobie.txr._

class OrdersSpec extends AnyFlatSpec with Matchers with PGContainer with Indexer {

  "Orders service" should "process forks correct" in {
    implicit val repo: PersistBundle[ConnectionIO] = PersistBundle.make[ConnectionIO]
    implicit val mempool                           = OrdersMempoolMock.mock[IO]

    val parser: ProcessedOrderParser = ProcessedOrderParser.make(TokenId.unsafeFromString(""))
    val service                      = Orders.make[IO, ConnectionIO]

    val depositRegister: ProcessedOrder.Any = parser.parse(Deposits.depositToExecute, 0L).get
    val depositExecute                      = parser.parse(Deposits.execute, 0L).get

    val swapRegister = parser.parse(Swaps.transactionSwapRegisterToRefund, 0L).get
    val swapRefunded = parser.parse(Swaps.transactionSwapRefundRegister, 0L).get

    val eventsCase1 = List(
      OrderEvent.Apply(depositRegister),
      OrderEvent.Apply(depositExecute),
      OrderEvent.Unapply(depositExecute),
      OrderEvent.Apply(depositExecute)
    )

    val eventsCase2 = List(
      OrderEvent.Apply(depositRegister),
      OrderEvent.Apply(depositExecute),
      OrderEvent.Unapply(depositExecute)
    )

    val eventsCase3 = List(
      OrderEvent.Apply(depositRegister),
      OrderEvent.Apply(depositExecute),
      OrderEvent.Unapply(depositExecute),
      OrderEvent.Unapply(depositRegister)
    )

    val eventsCase4 = List(
      OrderEvent.Apply(depositRegister),
      OrderEvent.Apply(swapRegister),
      OrderEvent.Unapply(swapRegister),
      OrderEvent.Unapply(depositRegister),
      OrderEvent.Apply(swapRegister),
      OrderEvent.Apply(swapRefunded),
      OrderEvent.Apply(depositRegister),
      OrderEvent.Unapply(swapRefunded)
    )

    val (r1, r2, r3, r4D, r4S) =
      (for {
        _  <- service.process(NonEmptyList.fromListUnsafe(eventsCase1))
        r1 <- sql"select * from deposits where order_id = ${depositRegister.order.id}".query[DepositDB].unique.trans
        _  <- Update0(s"delete from deposits", None).run.trans

        _  <- service.process(NonEmptyList.fromListUnsafe(eventsCase2))
        r2 <- sql"select * from deposits where order_id = ${depositRegister.order.id}".query[DepositDB].unique.trans
        _  <- Update0(s"delete from deposits", None).run.trans

        _  <- service.process(NonEmptyList.fromListUnsafe(eventsCase3))
        r3 <- sql"select * from deposits where order_id = ${depositRegister.order.id}".query[DepositDB].option.trans
        _  <- Update0(s"delete from deposits", None).run.trans

        _   <- service.process(NonEmptyList.fromListUnsafe(eventsCase4))
        r4D <- sql"select * from deposits where order_id = ${depositRegister.order.id}".query[DepositDB].unique.trans
        r4S <- sql"select * from swaps where order_id = ${swapRegister.order.id}".query[SwapDB].unique.trans
      } yield (r1, r2, r3, r4D, r4S)).unsafeRunSync()

    r1.registeredTx shouldEqual Some(TxInfo(depositRegister.state.txId, depositRegister.state.timestamp))
    r1.executedTx shouldEqual Some(TxInfo(depositExecute.state.txId, depositExecute.state.timestamp))
    r1.refundedTx shouldEqual None

    r2.registeredTx shouldEqual Some(TxInfo(depositRegister.state.txId, depositRegister.state.timestamp))
    r2.executedTx shouldEqual None
    r2.refundedTx shouldEqual None

    r3 shouldEqual None

    r4D.registeredTx shouldEqual Some(TxInfo(depositRegister.state.txId, depositRegister.state.timestamp))
    r4D.executedTx shouldEqual None
    r4D.refundedTx shouldEqual None

    r4S.registeredTx shouldEqual Some(TxInfo(swapRegister.state.txId, swapRegister.state.timestamp))
    r4S.executedTx shouldEqual None
    r4S.refundedTx shouldEqual None
  }
}
