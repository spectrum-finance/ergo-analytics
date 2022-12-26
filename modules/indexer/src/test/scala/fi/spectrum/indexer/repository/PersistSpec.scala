package fi.spectrum.indexer.repository

import cats.data.NonEmptyList
import cats.effect.unsafe.implicits.global
import doobie.ConnectionIO
import doobie.implicits._
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.analytics.ProcessedOrderOptics._
import fi.spectrum.core.domain.analytics.{OffChainFee, ProcessedOrder}
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.indexer.db.{Indexer, PGContainer}
import fi.spectrum.indexer.models._
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import glass.classic.Optional
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import tofu.internal.ContextBase.unliftEffectCE3
import tofu.syntax.doobie.txr._

class PersistSpec extends AnyFlatSpec with Matchers with PGContainer with Indexer {

  val repo: PersistBundle[ConnectionIO] = PersistBundle.make[ConnectionIO]

  val parser: ProcessedOrderParser = ProcessedOrderParser.make(TokenId.unsafeFromString(""))
  val poolsParser                  = PoolParser.make

  "Amm pool" should "works correct" in {

    val pool         = Redeems.executed.outputs.toList.map(poolsParser.parse(_, 0)).collectFirst { case Some(v) => v }.get
    val expectedPool = implicitly[ToDB[Pool, PoolDB]].toDB(pool)

    def run = for {
      insertResult  <- repo.pools.insert(NonEmptyList.of(pool)).trans
      expected1     <- sql"select * from pools where pool_id=${pool.poolId}".query[PoolDB].unique.trans
      resolveResult <- repo.pools.resolve(NonEmptyList.of(pool)).trans
      expected2     <- sql"select * from pools where pool_id=${pool.poolId}".query[PoolDB].option.trans
    } yield {
      insertResult shouldEqual 1
      resolveResult shouldEqual 1

      expected1 shouldEqual expectedPool
      expected2 shouldEqual None
    }

    run.unsafeRunSync()
  }

  "Off-chain fee" should "works correct" in {
    val register    = parser.parse(Redeems.redeem, 0).get
    val executed    = parser.parse(Redeems.executed, 0).get
    val expectedFee = implicitly[ToDB[OffChainFee, OffChainFeeDB]].toDB(executed.offChainFee.get)

    Optional[ProcessedOrder.Any, OffChainFee].getOption(register) shouldEqual None
    Optional[ProcessedOrder.Any, OffChainFee].getOption(executed) shouldEqual executed.offChainFee

    def run = for {
      insertResult <- repo.offChainFees.insert(NonEmptyList.of(register, executed)).trans
      expected1 <-
        sql"select * from off_chain_fee where order_id=${register.order.id}".query[OffChainFeeDB].unique.trans
      resolveResult <- repo.offChainFees.resolve(NonEmptyList.of(executed)).trans
      expected2 <-
        sql"select * from off_chain_fee where order_id=${register.order.id}".query[OffChainFeeDB].option.trans
    } yield {
      insertResult shouldEqual 1
      resolveResult shouldEqual 1

      expected1 shouldEqual expectedFee
      expected2 shouldEqual None
    }

    run.unsafeRunSync()
  }

  "AMM redeems persist" should "works correct" in {
    val register  = parser.parse(Redeems.redeemToRefund, 0).get
    val register2 = parser.parse(Redeems.redeem, 0).get
    val refund    = parser.parse(Redeems.refund, 0).get
    val executed  = parser.parse(Redeems.executed, 0).get

    val register1Expected =
      implicitly[ToDB[ProcessedOrder[Redeem], RedeemDB]].toDB(register.asInstanceOf[ProcessedOrder[Redeem]])
    val register2Expected =
      implicitly[ToDB[ProcessedOrder[Redeem], RedeemDB]].toDB(register2.asInstanceOf[ProcessedOrder[Redeem]])

    def run = for {
      insertResult  <- repo.redeems.insert(NonEmptyList.of(register, register2)).trans
      expected1     <- sql"select * from redeems where order_id=${register.order.id}".query[RedeemDB].unique.trans
      expected2     <- sql"select * from redeems where order_id=${register2.order.id}".query[RedeemDB].unique.trans
      resolveResult <- repo.redeems.insert(NonEmptyList.of(refund, executed)).trans
      expected3     <- sql"select * from redeems where order_id=${register.order.id}".query[RedeemDB].unique.trans
      expected4     <- sql"select * from redeems where order_id=${register2.order.id}".query[RedeemDB].unique.trans
      deleteResult  <- repo.redeems.resolve(NonEmptyList.of(register, register2)).trans
      expected5     <- sql"select * from redeems".query[RedeemDB].to[List].trans
    } yield {
      insertResult shouldEqual 2
      resolveResult shouldEqual 2
      deleteResult shouldEqual 2

      expected1 shouldEqual register1Expected
      expected2 shouldEqual register2Expected
      expected3 shouldEqual register1Expected.copy(refundedTx = Some(TxInfo(refund.state.txId, refund.state.timestamp)))
      expected4 shouldEqual register2Expected.copy(executedTx =
        Some(TxInfo(executed.state.txId, executed.state.timestamp))
      )
      expected5 shouldEqual List.empty
    }

    run.unsafeRunSync()
  }

  "AMM deposit persist" should "works correct" in {
    val register  = parser.parse(Deposits.depositsToRefund, 0).get
    val register2 = parser.parse(Deposits.depositToExecute, 0).get
    val refund    = parser.parse(Deposits.refund, 0).get
    val executed  = parser.parse(Deposits.execute, 0).get

    val register1Expected =
      implicitly[ToDB[ProcessedOrder[Deposit], DepositDB]].toDB(register.asInstanceOf[ProcessedOrder[Deposit]])
    val register2Expected =
      implicitly[ToDB[ProcessedOrder[Deposit], DepositDB]].toDB(register2.asInstanceOf[ProcessedOrder[Deposit]])

    def run = for {
      insertResult  <- repo.deposits.insert(NonEmptyList.of(register, register2)).trans
      expected1     <- sql"select * from deposits where order_id=${register.order.id}".query[DepositDB].unique.trans
      expected2     <- sql"select * from deposits where order_id=${register2.order.id}".query[DepositDB].unique.trans
      resolveResult <- repo.deposits.insert(NonEmptyList.of(refund, executed)).trans
      expected3     <- sql"select * from deposits where order_id=${register.order.id}".query[DepositDB].unique.trans
      expected4     <- sql"select * from deposits where order_id=${register2.order.id}".query[DepositDB].unique.trans
      deleteResult  <- repo.deposits.resolve(NonEmptyList.of(register, register2)).trans
      expected5     <- sql"select * from deposits".query[DepositDB].to[List].trans
    } yield {
      insertResult shouldEqual 2
      resolveResult shouldEqual 2
      deleteResult shouldEqual 2

      expected1 shouldEqual register1Expected
      expected2 shouldEqual register2Expected
      expected3 shouldEqual register1Expected.copy(refundedTx = Some(TxInfo(refund.state.txId, refund.state.timestamp)))
      expected4 shouldEqual register2Expected.copy(executedTx =
        Some(TxInfo(executed.state.txId, executed.state.timestamp))
      )
      expected5 shouldEqual List.empty
    }

    run.unsafeRunSync()
  }

  "AMM swap persis" should "works correct" in {
    val register: ProcessedOrder.Any = parser.parse(Swaps.transactionSwapRegister, 0).get
    val register2                    = parser.parse(Swaps.transactionSwapRegisterToRefund, 0).get
    val expectedRegister: SwapDB =
      implicitly[ToDB[ProcessedOrder[Swap], SwapDB]]
        .toDB(register.asInstanceOf[ProcessedOrder[Order.Swap]])
    val expectedRegister2: SwapDB =
      implicitly[ToDB[ProcessedOrder[Swap], SwapDB]]
        .toDB(register2.asInstanceOf[ProcessedOrder[Order.Swap]])

    def run = for {
      insertRegister  <- repo.swaps.insert(NonEmptyList.of(register, register2)).trans
      resultRegister  <- sql"select * from swaps where order_id=${register.order.id}".query[SwapDB].unique.trans
      resultRegister2 <- sql"select * from swaps where order_id=${register2.order.id}".query[SwapDB].unique.trans
      executed         = parser.parse(Swaps.transactionSwapExecuted, 0).get
      expectedExecuted = expectedRegister.copy(executedTx = Some(TxInfo(executed.state.txId, executed.state.timestamp)))
      insertExecuted <- repo.swaps.insert(NonEmptyList.one(executed)).trans
      resultExecuted <- sql"select * from swaps where order_id=${executed.order.id}".query[SwapDB].unique.trans
      refunded         = parser.parse(Swaps.transactionSwapRefundRegister, 0).get
      expectedRefunded = resultRegister2.copy(refundedTx = Some(TxInfo(refunded.state.txId, refunded.state.timestamp)))
      insertRefunded <- repo.swaps.insert(NonEmptyList.one(refunded)).trans
      resultRefunded <- sql"select * from swaps where order_id=${refunded.order.id}".query[SwapDB].unique.trans

      deleteRefunded <- repo.swaps.resolve(NonEmptyList.one(refunded)).trans
      resultDelete1  <- sql"select * from swaps where order_id=${refunded.order.id}".query[SwapDB].unique.trans

      deleteExecuted <- repo.swaps.resolve(NonEmptyList.one(executed)).trans
      resultDelete2  <- sql"select * from swaps where order_id=${executed.order.id}".query[SwapDB].unique.trans

      deleteRegister <- repo.swaps.resolve(NonEmptyList.one(register)).trans
      resultDelete3  <- sql"select * from swaps where order_id=${register.order.id}".query[SwapDB].option.trans
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
