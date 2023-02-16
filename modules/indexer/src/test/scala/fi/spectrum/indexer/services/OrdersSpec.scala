package fi.spectrum.indexer.services

import cats.data.NonEmptyList
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie.ConnectionIO
import doobie.implicits._
import doobie.util.update.Update0
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.indexer.db.models.{AmmDepositDB, SwapDB, TxInfo}
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.indexer.db.{Indexer, PGContainer}
import fi.spectrum.indexer.mocks.MetricsMock
import fi.spectrum.indexer.models.BlockChainEvent
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import tofu.syntax.doobie.txr._
import fi.spectrum.parser.evaluation.Transactions._

class OrdersSpec extends AnyFlatSpec with Matchers with PGContainer with Indexer {

  "Orders service" should "process forks correct" in {
    implicit val repo: PersistBundle[ConnectionIO] = PersistBundle.make[ConnectionIO]
    implicit val metrics                           = MetricsMock.make[ConnectionIO]

    val parser = ProcessedOrderParser.make[IO]

    val service = Orders.make[IO, ConnectionIO]

    val depositRegister: Processed.Any = parser.registered(depositRegisterTransaction, 0L).unsafeRunSync().head
    val depositExecute =
      parser.evaluated(depositEvaluateTransaction, 0L, depositRegister, depositPool.get, 10).unsafeRunSync().get

    val swapRegister = parser.registered(swapRegisterRefundTransaction, 0L).unsafeRunSync().head
    val swapRefunded = parser.refunded(swapRefundTransaction, 0L, swapRegister).unsafeRunSync()

    val eventsCase1 = List(
      BlockChainEvent.Apply(depositRegister),
      BlockChainEvent.Apply(depositExecute),
      BlockChainEvent.Unapply(depositExecute),
      BlockChainEvent.Apply(depositExecute)
    )

    val eventsCase2 = List(
      BlockChainEvent.Apply(depositRegister),
      BlockChainEvent.Apply(depositExecute),
      BlockChainEvent.Unapply(depositExecute)
    )

    val eventsCase3 = List(
      BlockChainEvent.Apply(depositRegister),
      BlockChainEvent.Apply(depositExecute),
      BlockChainEvent.Unapply(depositExecute),
      BlockChainEvent.Unapply(depositRegister)
    )

    val eventsCase4 = List(
      BlockChainEvent.Apply(depositRegister),
      BlockChainEvent.Apply(swapRegister),
      BlockChainEvent.Unapply(swapRegister),
      BlockChainEvent.Unapply(depositRegister),
      BlockChainEvent.Apply(swapRegister),
      BlockChainEvent.Apply(swapRefunded),
      BlockChainEvent.Apply(depositRegister),
      BlockChainEvent.Unapply(swapRefunded)
    )

    val (r1, r2, r3, r4D, r4S) =
      (for {
        _  <- service.process(NonEmptyList.fromListUnsafe(eventsCase1))
        r1 <- sql"select * from deposits where order_id = ${depositRegister.order.id}".query[AmmDepositDB].unique.trans
        _  <- Update0(s"delete from deposits", None).run.trans
        _  <- service.process(NonEmptyList.fromListUnsafe(eventsCase2))
        r2 <- sql"select * from deposits where order_id = ${depositRegister.order.id}".query[AmmDepositDB].unique.trans
        _  <- Update0(s"delete from deposits", None).run.trans

        _  <- service.process(NonEmptyList.fromListUnsafe(eventsCase3))
        r3 <- sql"select * from deposits where order_id = ${depositRegister.order.id}".query[AmmDepositDB].option.trans
        _  <- Update0(s"delete from deposits", None).run.trans

        _   <- service.process(NonEmptyList.fromListUnsafe(eventsCase4))
        r4D <- sql"select * from deposits where order_id = ${depositRegister.order.id}".query[AmmDepositDB].unique.trans
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
