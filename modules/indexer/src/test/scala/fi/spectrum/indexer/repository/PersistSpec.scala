package fi.spectrum.indexer.repository

import cats.effect.IO
import cats.effect.std.Dispatcher
import cats.effect.unsafe.implicits.global
import cats.syntax.traverse._
import doobie.ConnectionIO
import doobie.implicits._
import fi.spectrum.core.db.doobieLogging
import fi.spectrum.core.domain.TxId
import fi.spectrum.core.domain.analytics.ProcessedOrderOptics._
import fi.spectrum.core.domain.analytics.{OffChainFee, Processed}
import fi.spectrum.core.domain.order.{Order, OrderState}
import fi.spectrum.core.domain.order.Order.Deposit.{AmmDeposit, LmDeposit}
import fi.spectrum.core.domain.order.Order.Redeem.{AmmRedeem, LmRedeem}
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.OrderStatus.Registered
import fi.spectrum.core.domain.pool.Pool.{AmmPool, LmPool}
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.indexer.classes.ToDB
import fi.spectrum.indexer.db.models.LmPoolDB._
import fi.spectrum.indexer.db.models._
import fi.spectrum.indexer.db.persist.PersistBundle
import fi.spectrum.indexer.db.{Indexer, PGContainer}
import fi.spectrum.parser.PoolParser
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.parser.evaluation.Transactions._
import fi.spectrum.parser.lm.order.v1.{LM, LmOrderParserV1}
import fi.spectrum.parser.lm.compound.v1.Compound
import fi.spectrum.parser.lm.pool.v1.SelfHosted
import glass.classic.Optional
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import tofu.WithContext
import tofu.internal.ContextBase.unliftEffectCE3
import tofu.syntax.doobie.txr._

class PersistSpec extends AnyFlatSpec with Matchers with PGContainer with Indexer {

  val repo: PersistBundle[ConnectionIO] = PersistBundle.make[ConnectionIO]

  val parser: ProcessedOrderParser[IO] = ProcessedOrderParser.make[IO]
  val poolsParser                      = PoolParser.make

  "Amm pool" should "work correct" in {
    val pool: AmmPool = redeemPool.get.asInstanceOf[AmmPool]
    val expectedPool  = implicitly[ToDB[AmmPool, PoolDB]].toDB(pool).copy(height = 10)

    def run = for {
      insertResult  <- repo.ammPools.insert(pool).trans
      expected1     <- sql"select * from pools where pool_id=${pool.poolId}".query[PoolDB].unique.trans
      resolveResult <- repo.ammPools.resolve(pool).trans
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
    val register    = parser.registered(swapRegisterTransaction, 0).unsafeRunSync().head
    val executed    = parser.evaluated(swapEvaluateTransaction, 0, register, swapPool.get, 10, List.empty).unsafeRunSync().get
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

  "AMM swap v3" should "work correct" in {
    val register = parser.registered(v3SwapRegister.toTransaction, 0).unsafeRunSync().head

    println(register)

    val register1Expected =
      implicitly[ToDB[Processed[Swap], SwapDB]].toDB(register.asInstanceOf[Processed[Swap]])
    val res = repo.swaps.insert(register).trans.unsafeRunSync()
    println(res)
    val res2 = sql"select * from swaps where order_id=${register.order.id}"
      .query[SwapDB]
      .unique
      .trans
      .unsafeRunSync()
    println(res2)
  }

  "AMM redeems persist" should "work correct" in {
    val register  = parser.registered(redeemRegisterTransaction, 0).unsafeRunSync().head
    val register2 = parser.registered(redeemRegisterRefundTransaction, 0).unsafeRunSync().head
    val refund    = parser.refunded(redeemRefundTransaction, 0, register2).unsafeRunSync()
    val executed  = parser.evaluated(redeemEvaluateTransaction, 0, register, redeemPool.get, 10, List.empty).unsafeRunSync().get

    val register1Expected =
      implicitly[ToDB[Processed[AmmRedeem], AmmRedeemDB]].toDB(register.asInstanceOf[Processed[AmmRedeem]])
    val register2Expected =
      implicitly[ToDB[Processed[AmmRedeem], AmmRedeemDB]].toDB(register2.asInstanceOf[Processed[AmmRedeem]])
    val evalExpected =
      implicitly[ToDB[Processed[AmmRedeem], AmmRedeemDB]].toDB(executed.asInstanceOf[Processed[AmmRedeem]])

    def run = for {
      insertResult   <- repo.ammRedeems.insert(register).trans
      insertResult1  <- repo.ammRedeems.insert(register2).trans
      expected1      <- sql"select * from redeems where order_id=${register.order.id}".query[AmmRedeemDB].unique.trans
      expected2      <- sql"select * from redeems where order_id=${register2.order.id}".query[AmmRedeemDB].unique.trans
      resolveResult  <- repo.ammRedeems.insert(refund).trans
      resolveResult1 <- repo.ammRedeems.insert(executed).trans
      expected3      <- sql"select * from redeems where order_id=${register.order.id}".query[AmmRedeemDB].unique.trans
      expected4      <- sql"select * from redeems where order_id=${register2.order.id}".query[AmmRedeemDB].unique.trans
      deleteResult   <- repo.ammRedeems.resolve(register).trans
      deleteResult1  <- repo.ammRedeems.resolve(register2).trans
      expected5      <- sql"select * from redeems".query[AmmRedeemDB].to[List].trans
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
    val register  = parser.registered(depositRegisterTransaction, 0).unsafeRunSync().head
    val register2 = parser.registered(depositRegisterRefundTransaction, 0).unsafeRunSync().head
    val refund    = parser.refunded(depositRefundTransaction, 0, register2).unsafeRunSync()
    val executed  = parser.evaluated(depositEvaluateTransaction, 0, register, depositPool.get, 10, List.empty).unsafeRunSync().get

    val register1Expected =
      implicitly[ToDB[Processed[AmmDeposit], AmmDepositDB]].toDB(register.asInstanceOf[Processed[AmmDeposit]])
    val register2Expected =
      implicitly[ToDB[Processed[AmmDeposit], AmmDepositDB]].toDB(register2.asInstanceOf[Processed[AmmDeposit]])
    val executedExpected =
      implicitly[ToDB[Processed[AmmDeposit], AmmDepositDB]].toDB(executed.asInstanceOf[Processed[AmmDeposit]])

    def run = for {
      insertResult   <- repo.ammDeposits.insert(register).trans
      insertResult1  <- repo.ammDeposits.insert(register2).trans
      expected1      <- sql"select * from deposits where order_id=${register.order.id}".query[AmmDepositDB].unique.trans
      expected2      <- sql"select * from deposits where order_id=${register2.order.id}".query[AmmDepositDB].unique.trans
      resolveResult  <- repo.ammDeposits.insert(refund).trans
      resolveResult1 <- repo.ammDeposits.insert(executed).trans
      expected3      <- sql"select * from deposits where order_id=${register.order.id}".query[AmmDepositDB].unique.trans
      expected4      <- sql"select * from deposits where order_id=${register2.order.id}".query[AmmDepositDB].unique.trans
      deleteResult   <- repo.ammDeposits.resolve(register).trans
      deleteResult1  <- repo.ammDeposits.resolve(register2).trans
      expected5      <- sql"select * from deposits".query[AmmDepositDB].to[List].trans
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
    val register  = parser.registered(swapRegisterTransaction, 0).unsafeRunSync().head
    val register2 = parser.registered(swapRegisterRefundTransaction, 0).unsafeRunSync().head
    val refunded  = parser.refunded(swapRefundTransaction, 0, register2).unsafeRunSync()
    val executed  = parser.evaluated(swapEvaluateTransaction, 0, register, swapPool.get, 10, List.empty).unsafeRunSync().get

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
    val result = parser.registered(lockTransaction, 0).unsafeRunSync().head
    val expected =
      implicitly[ToDB[Processed[Lock], LockDB]]
        .toDB(result.asInstanceOf[Processed[Order.Lock]])

    def run = for {
      r      <- repo.locks.insert(result).trans
      result <- sql"select * from lq_locks where order_id=${result.order.id}".query[LockDB].option.trans
    } yield result.get shouldEqual expected
    run.unsafeRunSync()
  }

  "LM pool persist" should "work correct" in {

    val pool: LmPool = SelfHosted.pool.asInstanceOf[LmPool]
    val expectedPool = implicitly[ToDB[LmPool, LmPoolDB]].toDB(pool)

    Dispatcher
      .parallel[IO]
      .use { dispatcher =>
        implicit val cxt: WithContext[IO, Dispatcher[IO]] = WithContext.const(dispatcher)

        implicit val elh = doobieLogging.makeEmbeddableHandler[IO, IO]("").self.unsafeRunSync()

        def run = for {
          insertResult  <- repo.lmPools.insert(pool).trans
          expected1     <- sql"select * from lm_pools where pool_id=${pool.poolId}".query[LmPoolDB].option.trans
          resolveResult <- repo.lmPools.resolve(pool).trans
          expected2     <- sql"select * from lm_pools where pool_id=${pool.poolId}".query[LmPoolDB].option.trans
        } yield {
          insertResult shouldEqual 1
          resolveResult shouldEqual 1

          expected1.get shouldEqual expectedPool
          expected2 shouldEqual None
        }

        run
      }
      .unsafeRunSync()
  }

  "Lm deposit persist" should "work correct" in {
    val register = ProcessedOrderParser.make[IO].registered(LM.deployDepositOrderTx, 0).unsafeRunSync().head
    val eval     = ProcessedOrderParser.make[IO].evaluated(LM.tx, 0, register, SelfHosted.pool, 0, List.empty).unsafeRunSync().head

    def run = for {
      insertResult  <- repo.insertAnyOrder.traverse(_(register)).trans
      expected1     <- sql"select * from lm_deposits where order_id=${register.order.id}".query[LmDepositDB].option.trans
      resolveResult <- repo.resolveAnyOrder.traverse(_(register)).trans
      expected2     <- sql"select * from lm_deposits where order_id=${register.order.id}".query[LmDepositDB].option.trans
      _             <- repo.insertAnyOrder.traverse(_(register)).trans
      insertResult2 <- repo.insertAnyOrder.traverse(_(eval)).trans
      expected3     <- sql"select * from lm_deposits where order_id=${register.order.id}".query[LmDepositDB].option.trans
      resolveResul2 <- repo.resolveAnyOrder.traverse(_(eval)).trans
      expected4     <- sql"select * from lm_deposits where order_id=${register.order.id}".query[LmDepositDB].option.trans
    } yield {
      insertResult.sum shouldEqual 1
      resolveResult.sum shouldEqual 1
      insertResult2.sum shouldEqual 1
      resolveResul2.sum shouldEqual 1
      import fi.spectrum.indexer.db.models.LmDepositDB._

      expected1.get shouldEqual implicitly[ToDB[Processed[LmDeposit], LmDepositDB]]
        .toDB(register.asInstanceOf[Processed[LmDeposit]])
      expected2 shouldEqual None
      expected3.get shouldEqual implicitly[ToDB[Processed[LmDeposit], LmDepositDB]]
        .toDB(eval.asInstanceOf[Processed[LmDeposit]])
        .copy(registeredTx = expected1.get.registeredTx)
      expected4.get shouldEqual implicitly[ToDB[Processed[LmDeposit], LmDepositDB]]
        .toDB(register.asInstanceOf[Processed[LmDeposit]])
    }

    run.unsafeRunSync()
  }

  "Compound persist" should "work correct" in {
    val register = Processed(
      Compound.compoundNotLastEpoch,
      OrderState(TxId("b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e"), 0, Registered),
      None,
      None,
      None
    ).asInstanceOf[Processed.Any]
    val eval =
      ProcessedOrderParser.make[IO].evaluated(Compound.compoundTx, 0, register, SelfHosted.pool, 0, List.empty).unsafeRunSync().get

    def run = for {
      insertResult  <- repo.insertAnyOrder.traverse(_(register)).trans
      expected1     <- sql"select * from lm_compound where order_id=${register.order.id}".query[LmCompoundDB].option.trans
      resolveResult <- repo.resolveAnyOrder.traverse(_(register)).trans
      expected2     <- sql"select * from lm_compound where order_id=${register.order.id}".query[LmCompoundDB].option.trans
      _             <- repo.insertAnyOrder.traverse(_(register)).trans
      insertResult2 <- repo.insertAnyOrder.traverse(_(eval)).trans
      expected3     <- sql"select * from lm_compound where order_id=${register.order.id}".query[LmCompoundDB].option.trans
      resolveResul2 <- repo.resolveAnyOrder.traverse(_(eval)).trans
      expected4     <- sql"select * from lm_compound where order_id=${register.order.id}".query[LmCompoundDB].option.trans
    } yield {
      insertResult.sum shouldEqual 1
      resolveResult.sum shouldEqual 1
      insertResult2.sum shouldEqual 2
      resolveResul2.sum shouldEqual 2

      expected1.get shouldEqual implicitly[ToDB[Processed[Compound], LmCompoundDB]]
        .toDB(register.asInstanceOf[Processed[Compound]])
      expected2 shouldEqual None
      expected3.get shouldEqual implicitly[ToDB[Processed[Compound], LmCompoundDB]]
        .toDB(eval.asInstanceOf[Processed[Compound]])
        .copy(registeredTx = expected1.get.registeredTx)
      expected4.get shouldEqual implicitly[ToDB[Processed[Compound], LmCompoundDB]]
        .toDB(register.asInstanceOf[Processed[Compound]])
    }

    run.unsafeRunSync()
  }

  "Lm redeem persist" should "work correct" in {
    val register = Processed(
      LM.redeem,
      OrderState(TxId("b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e"), 0, Registered),
      None,
      None,
      None
    ).asInstanceOf[Processed.Any]
    val eval =
      ProcessedOrderParser.make[IO].evaluated(LM.redeemEvaluate, 0, register, SelfHosted.pool, 0, List.empty).unsafeRunSync().get

    def run = for {
      insertResult  <- repo.insertAnyOrder.traverse(_(register)).trans
      expected1     <- sql"select * from lm_redeems where order_id=${register.order.id}".query[LmRedeemDB].option.trans
      resolveResult <- repo.resolveAnyOrder.traverse(_(register)).trans
      expected2     <- sql"select * from lm_redeems where order_id=${register.order.id}".query[LmRedeemDB].option.trans
      _             <- repo.insertAnyOrder.traverse(_(register)).trans
      insertResult2 <- repo.insertAnyOrder.traverse(_(eval)).trans
      expected3     <- sql"select * from lm_redeems where order_id=${register.order.id}".query[LmRedeemDB].option.trans
      resolveResul2 <- repo.resolveAnyOrder.traverse(_(eval)).trans
      expected4     <- sql"select * from lm_redeems where order_id=${register.order.id}".query[LmRedeemDB].option.trans
    } yield {
      insertResult.sum shouldEqual 1
      resolveResult.sum shouldEqual 1
      insertResult2.sum shouldEqual 1
      resolveResul2.sum shouldEqual 1

      expected1.get shouldEqual implicitly[ToDB[Processed[LmRedeem], LmRedeemDB]]
        .toDB(register.asInstanceOf[Processed[LmRedeem]])
      expected2 shouldEqual None
      expected3.get shouldEqual implicitly[ToDB[Processed[LmRedeem], LmRedeemDB]]
        .toDB(eval.asInstanceOf[Processed[LmRedeem]])
        .copy(registeredTx = expected1.get.registeredTx)
      expected4.get shouldEqual implicitly[ToDB[Processed[LmRedeem], LmRedeemDB]]
        .toDB(register.asInstanceOf[Processed[LmRedeem]])
    }

    run.unsafeRunSync()
  }
}
