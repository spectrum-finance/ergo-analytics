package fi.spectrum.indexer.repository

import cats.data.NonEmptyList
import cats.effect.unsafe.implicits.global
import doobie.ConnectionIO
import doobie.implicits._
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.Order.AnySwap
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.indexer.db.{Indexer, PGContainer}
import fi.spectrum.indexer.models.{SwapDB, TxInfo}
import fi.spectrum.parser.evaluation.{Processed, ProcessedOrderParser}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import tofu.internal.ContextBase.unliftEffectCE3
import tofu.syntax.doobie.txr._

class PersistSpec extends AnyFlatSpec with Matchers with PGContainer with Indexer {

  val repo: PersistBundle[ConnectionIO] = PersistBundle.make[ConnectionIO]

  val parser: ProcessedOrderParser = ProcessedOrderParser.make(TokenId.unsafeFromString(""))

  "AMM persis" should "works correct" in {
    val register: ProcessedOrder.Any = parser.parse(Processed.transactionSwapRegister, 0).get
    val register2                    = parser.parse(Processed.transactionSwapRegisterToRefund, 0).get
    val expectedRegister: SwapDB =
      implicitly[ToDB[ProcessedOrder[AnySwap], SwapDB]]
        .toDB(register.asInstanceOf[ProcessedOrder[Order.AnySwap]])
    val expectedRegister2: SwapDB =
      implicitly[ToDB[ProcessedOrder[AnySwap], SwapDB]]
        .toDB(register2.asInstanceOf[ProcessedOrder[Order.AnySwap]])

    def run = for {
      insertRegister  <- repo.swaps.insert(NonEmptyList.of(register, register2)).trans
      resultRegister  <- sql"select * from swaps where order_id=${register.order.id}".query[SwapDB].unique.trans
      resultRegister2 <- sql"select * from swaps where order_id=${register2.order.id}".query[SwapDB].unique.trans
      executed         = parser.parse(Processed.transactionSwapExecuted, 0).get
      expectedExecuted = expectedRegister.copy(executedTx = Some(TxInfo(executed.state.txId, executed.state.timestamp)))
      insertExecuted <- repo.swaps.insert(NonEmptyList.one(executed)).trans
      resultExecuted <- sql"select * from swaps where order_id=${executed.order.id}".query[SwapDB].unique.trans
      refunded         = parser.parse(Processed.transactionSwapRefundRegister, 0).get
      expectedRefunded = resultRegister2.copy(refundedTx = Some(TxInfo(refunded.state.txId, refunded.state.timestamp)))
      insertRefunded <- repo.swaps.insert(NonEmptyList.one(refunded)).trans
      resultRefunded <- sql"select * from swaps where order_id=${refunded.order.id}".query[SwapDB].unique.trans

      deleteRefunded <- repo.swaps.resolve(NonEmptyList.one(refunded)).trans
      resultDelete1 <- sql"select * from swaps where order_id=${refunded.order.id}".query[SwapDB].unique.trans

      deleteExecuted <- repo.swaps.resolve(NonEmptyList.one(executed)).trans
      resultDelete2 <- sql"select * from swaps where order_id=${executed.order.id}".query[SwapDB].unique.trans

      deleteRegister <- repo.swaps.resolve(NonEmptyList.one(register)).trans
      resultDelete3 <- sql"select * from swaps where order_id=${register.order.id}".query[SwapDB].option.trans
    } yield {
      insertRegister shouldEqual 2
      insertExecuted shouldEqual 1
      insertRefunded shouldEqual 1

      resultRegister shouldEqual expectedRegister
      resultRegister2 shouldEqual expectedRegister2
      resultExecuted shouldEqual expectedExecuted
      resultRefunded shouldEqual expectedRefunded

      deleteRefunded shouldEqual 1
      deleteExecuted shouldEqual 1
      deleteRegister shouldEqual 1

      resultDelete1 shouldEqual expectedRegister2
      resultDelete2 shouldEqual expectedRegister
      resultDelete3 shouldEqual None
    }

    run.unsafeRunSync()
  }
}
