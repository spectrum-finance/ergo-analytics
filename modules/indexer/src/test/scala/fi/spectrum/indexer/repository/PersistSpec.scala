package fi.spectrum.indexer.repository

import cats.data.NonEmptyList
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie.ConnectionIO
import doobie.implicits._
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.analytics.ProcessedOrderOptics._
import fi.spectrum.core.domain.analytics.{OffChainFee, Processed}
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.Order.Lock.LockV1
import fi.spectrum.core.domain.order.Order.Swap.SwapV1
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.db.models.{DepositDB, LockDB, OffChainFeeDB, PoolDB, RedeemDB, SwapDB, TxInfo}
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.indexer.db.{Indexer, PGContainer}
import fi.spectrum.parser.{CatsPlatform, PoolParser}
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.parser.evaluation.Transactions._
import glass.classic.Optional
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import tofu.internal.ContextBase.unliftEffectCE3
import tofu.syntax.doobie.txr._
import fi.spectrum.parser.lock.v1.{LockParser, Lock => L}

class PersistSpec extends AnyFlatSpec with Matchers with PGContainer with Indexer {

  val repo: PersistBundle[ConnectionIO] = PersistBundle.make[ConnectionIO]

  val parser: ProcessedOrderParser[IO] = ProcessedOrderParser.make[IO]
  val poolsParser                      = PoolParser.make

  "Amm pool" should "work correct" in {
    val pool: Pool   = redeemPool.get
    val expectedPool = implicitly[ToDB[Pool, PoolDB]].toDB(pool).copy(height = 751721)

    def run = for {
      insertResult  <- repo.pools.insert(pool).trans
      expected1     <- sql"select * from pools where pool_id=${pool.poolId}".query[PoolDB].unique.trans
      resolveResult <- repo.pools.resolve(pool).trans
      expected2     <- sql"select * from pools where pool_id=${pool.poolId}".query[PoolDB].option.trans
    } yield {
      insertResult shouldEqual 1
      resolveResult shouldEqual 1

      expected1 shouldEqual expectedPool
      expected2 shouldEqual None
    }

    run.unsafeRunSync()
  }

  "Off-chain fee" should "work correct" in {
    val register    = parser.registered(swapRegisterTransaction, 0).unsafeRunSync().get
    val executed    = parser.evaluated(swapEvaluateTransaction, 0, register, swapPool.get, 10).unsafeRunSync().get
    val expectedFee = implicitly[ToDB[OffChainFee, OffChainFeeDB]].toDB(executed.offChainFee.get)

    Optional[Processed.Any, OffChainFee].getOption(register) shouldEqual None
    Optional[Processed.Any, OffChainFee].getOption(executed) shouldEqual executed.offChainFee

    def run = for {
      insertResult  <- repo.offChainFees.insert(register).trans
      insertResult1 <- repo.offChainFees.insert(executed).trans
      expected1 <-
        sql"select * from off_chain_fee where order_id=${register.order.id}".query[OffChainFeeDB].unique.trans
      resolveResult <- repo.offChainFees.resolve(executed).trans
      expected2 <-
        sql"select * from off_chain_fee where order_id=${register.order.id}".query[OffChainFeeDB].option.trans
    } yield {
      insertResult shouldEqual 0
      insertResult1 shouldEqual 1
      resolveResult shouldEqual 1

      expected1 shouldEqual expectedFee
      expected2 shouldEqual None
    }

    run.unsafeRunSync()
  }

  "AMM redeems persist" should "work correct" in {
    val register  = parser.registered(redeemRegisterTransaction, 0).unsafeRunSync().get
    val register2 = parser.registered(redeemRegisterRefundTransaction, 0).unsafeRunSync().get
    val refund    = parser.refunded(redeemRefundTransaction, 0, register2).unsafeRunSync()
    val executed  = parser.evaluated(redeemEvaluateTransaction, 0, register, redeemPool.get, 10).unsafeRunSync().get

    val register1Expected =
      implicitly[ToDB[Processed[Redeem], RedeemDB]].toDB(register.asInstanceOf[Processed[Redeem]])
    val register2Expected =
      implicitly[ToDB[Processed[Redeem], RedeemDB]].toDB(register2.asInstanceOf[Processed[Redeem]])
    val evalExpected =
      implicitly[ToDB[Processed[Redeem], RedeemDB]].toDB(executed.asInstanceOf[Processed[Redeem]])

    def run = for {
      insertResult   <- repo.redeems.insert(register).trans
      insertResult1  <- repo.redeems.insert(register2).trans
      expected1      <- sql"select * from redeems where order_id=${register.order.id}".query[RedeemDB].unique.trans
      expected2      <- sql"select * from redeems where order_id=${register2.order.id}".query[RedeemDB].unique.trans
      resolveResult  <- repo.redeems.insert(refund).trans
      resolveResult1 <- repo.redeems.insert(executed).trans
      expected3      <- sql"select * from redeems where order_id=${register.order.id}".query[RedeemDB].unique.trans
      expected4      <- sql"select * from redeems where order_id=${register2.order.id}".query[RedeemDB].unique.trans
      deleteResult   <- repo.redeems.resolve(register).trans
      deleteResult1  <- repo.redeems.resolve(register2).trans
      expected5      <- sql"select * from redeems".query[RedeemDB].to[List].trans
    } yield {
      insertResult shouldEqual 1
      insertResult1 shouldEqual 1
      resolveResult shouldEqual 1
      resolveResult1 shouldEqual 1
      deleteResult shouldEqual 1
      deleteResult1 shouldEqual 1

      expected1 shouldEqual register1Expected
      expected2 shouldEqual register2Expected
      expected3 shouldEqual evalExpected.copy(
        registeredTx = Some(TxInfo(register.state.txId, register.state.timestamp))
      )

      expected4 shouldEqual register2Expected.copy(refundedTx = Some(TxInfo(refund.state.txId, refund.state.timestamp)))
      expected5 shouldEqual List.empty
    }

    run.unsafeRunSync()
  }

  "AMM deposit persist" should "work correct" in {
    val register  = parser.registered(depositRegisterTransaction, 0).unsafeRunSync().get
    val register2 = parser.registered(depositRegisterRefundTransaction, 0).unsafeRunSync().get
    val refund    = parser.refunded(depositRefundTransaction, 0, register2).unsafeRunSync()
    val executed  = parser.evaluated(depositEvaluateTransaction, 0, register, depositPool.get, 10).unsafeRunSync().get

    val register1Expected =
      implicitly[ToDB[Processed[Deposit], DepositDB]].toDB(register.asInstanceOf[Processed[Deposit]])
    val register2Expected =
      implicitly[ToDB[Processed[Deposit], DepositDB]].toDB(register2.asInstanceOf[Processed[Deposit]])
    val executedExpected =
      implicitly[ToDB[Processed[Deposit], DepositDB]].toDB(executed.asInstanceOf[Processed[Deposit]])

    def run = for {
      insertResult   <- repo.deposits.insert(register).trans
      insertResult1  <- repo.deposits.insert(register2).trans
      expected1      <- sql"select * from deposits where order_id=${register.order.id}".query[DepositDB].unique.trans
      expected2      <- sql"select * from deposits where order_id=${register2.order.id}".query[DepositDB].unique.trans
      resolveResult  <- repo.deposits.insert(refund).trans
      resolveResult1 <- repo.deposits.insert(executed).trans
      expected3      <- sql"select * from deposits where order_id=${register.order.id}".query[DepositDB].unique.trans
      expected4      <- sql"select * from deposits where order_id=${register2.order.id}".query[DepositDB].unique.trans
      deleteResult   <- repo.deposits.resolve(register).trans
      deleteResult1  <- repo.deposits.resolve(register2).trans
      expected5      <- sql"select * from deposits".query[DepositDB].to[List].trans
    } yield {
      insertResult shouldEqual 1
      insertResult1 shouldEqual 1
      resolveResult shouldEqual 1
      resolveResult1 shouldEqual 1
      deleteResult shouldEqual 1
      deleteResult1 shouldEqual 1

      expected1 shouldEqual register1Expected
      expected2 shouldEqual register2Expected
      expected3 shouldEqual executedExpected.copy(registeredTx =
        Some(TxInfo(register.state.txId, register.state.timestamp))
      )
      expected4 shouldEqual register2Expected.copy(refundedTx = Some(TxInfo(refund.state.txId, refund.state.timestamp)))
      expected5 shouldEqual List.empty
    }

    run.unsafeRunSync()
  }

  "AMM swap persist" should "work correct" in {
    val register  = parser.registered(swapRegisterTransaction, 0).unsafeRunSync().get
    val register2 = parser.registered(swapRegisterRefundTransaction, 0).unsafeRunSync().get
    val refunded  = parser.refunded(swapRefundTransaction, 0, register2).unsafeRunSync()
    val executed  = parser.evaluated(swapEvaluateTransaction, 0, register, swapPool.get, 10).unsafeRunSync().get

    val expectedRegister: SwapDB =
      implicitly[ToDB[Processed[Swap], SwapDB]]
        .toDB(register.asInstanceOf[Processed[Order.Swap]])
    val expectedRegister2: SwapDB =
      implicitly[ToDB[Processed[Swap], SwapDB]]
        .toDB(register2.asInstanceOf[Processed[Order.Swap]])
    val executedExpected: SwapDB =
      implicitly[ToDB[Processed[Swap], SwapDB]]
        .toDB(executed.asInstanceOf[Processed[Order.Swap]])

    def run = for {
      insertRegister  <- repo.swaps.insert(register).trans
      insertRegister1 <- repo.swaps.insert(register2).trans
      resultRegister  <- sql"select * from swaps where order_id=${register.order.id}".query[SwapDB].unique.trans
      resultRegister2 <- sql"select * from swaps where order_id=${register2.order.id}".query[SwapDB].unique.trans
      insertExecuted  <- repo.swaps.insert(executed).trans
      resultExecuted  <- sql"select * from swaps where order_id=${executed.order.id}".query[SwapDB].unique.trans
      expectedRefunded = resultRegister2.copy(refundedTx = Some(TxInfo(refunded.state.txId, refunded.state.timestamp)))
      insertRefunded <- repo.swaps.insert(refunded).trans
      resultRefunded <- sql"select * from swaps where order_id=${refunded.order.id}".query[SwapDB].unique.trans
      deleteRefunded <- repo.swaps.resolve(refunded).trans
      resultDelete1  <- sql"select * from swaps where order_id=${refunded.order.id}".query[SwapDB].unique.trans
      deleteExecuted <- repo.swaps.resolve(executed).trans
      resultDelete2  <- sql"select * from swaps where order_id=${executed.order.id}".query[SwapDB].unique.trans
      deleteRegister <- repo.swaps.resolve(register).trans
      resultDelete3  <- sql"select * from swaps where order_id=${register.order.id}".query[SwapDB].option.trans
    } yield {
      insertRegister shouldEqual 1
      insertRegister1 shouldEqual 1
      insertExecuted shouldEqual 1
      insertRefunded shouldEqual 1

      resultRegister shouldEqual expectedRegister
      resultRegister2 shouldEqual expectedRegister2
      resultExecuted shouldEqual executedExpected.copy(registeredTx =
        Some(TxInfo(register.state.txId, register.state.timestamp))
      )
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

  "LQ locks persist" should "work correct" in {
    val result = parser.registered(lockTransaction, 0).unsafeRunSync().get
    val expected =
      implicitly[ToDB[Processed[Lock], LockDB]]
        .toDB(result.asInstanceOf[Processed[Order.Lock]])

    def run = for {
      _      <- repo.locks.insert(result).trans
      result <- sql"select * from lq_locks where order_id=${result.order.id}".query[LockDB].option.trans
    } yield result.get shouldEqual expected
    run.unsafeRunSync()

  }
}
