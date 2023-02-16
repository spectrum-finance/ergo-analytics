package fi.spectrum.api.v1.services

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.syntax.option._
import doobie.ConnectionIO
import doobie.implicits._
import fi.spectrum.api.db.repositories.History
import fi.spectrum.api.db.{Api, PGContainer}
import fi.spectrum.api.mocks.{InsertLmCompound, InsertLmDeposit, MetricsMock, NetworkMock}
import fi.spectrum.api.models.MempoolData
import fi.spectrum.api.v1.models.history.ApiOrder.{LmCompoundApi, LmDepositApi, LmRedeemApi}
import fi.spectrum.api.v1.models.history.{OrderStatus, TxData}
import fi.spectrum.core.domain.analytics.OrderEvaluation.{LmDepositCompoundEvaluation, LmRedeemEvaluation}
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.Order.Compound.CompoundV1
import fi.spectrum.core.domain.order.Order.Deposit.LmDeposit.LmDepositV1
import fi.spectrum.core.domain.order.OrderState
import fi.spectrum.core.domain.order.OrderStatus.Registered
import fi.spectrum.core.domain.{Address, TokenId, TxId}
import fi.spectrum.graphite.Metrics
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.parser.lm.compound.v1.Compound
import fi.spectrum.parser.lm.order.v1.LM
import fi.spectrum.parser.lm.pool.v1.SelfHosted
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import tofu.syntax.doobie.txr._

final class MempoolApiSpec extends AnyFlatSpec with Matchers with PGContainer with Api {

  implicit val metrics: Metrics[ConnectionIO] = MetricsMock.make

  implicit val history: History[ConnectionIO] = History.make[IO, ConnectionIO].unsafeRunSync()

  "Mempool api" should "process lm register deposit correct" in {
    val registerOrder = Processed(
      LM.deposit,
      OrderState(TxId("b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e"), 0, Registered),
      None,
      None,
      None
    ).asInstanceOf[Processed.Any]

    val data = MempoolData(
      Address.fromStringUnsafe("9i245HdmSYwYsVR2qudtULu3BRBenPagNbT6uLi2Np9QZzdQWGH"),
      List(registerOrder)
    )
    implicit val network = NetworkMock.make[IO](List(data))

    val mempool = MempoolApi.make[IO, IO, ConnectionIO].unsafeRunSync()

    val res = mempool.ordersByAddress(List.empty).unsafeRunSync().head.asInstanceOf[LmDepositApi]

    val expected = LmDepositApi(
      registerOrder.order.id,
      OrderStatus.Mempool,
      SelfHosted.pool.poolId,
      LM.deposit.params.expectedNumEpochs,
      LM.deposit.params.tokens,
      none,
      none,
      TxData(registerOrder.state.txId, registerOrder.state.timestamp),
      none,
      none
    )

    res shouldEqual expected

  }

  "Mempool api" should "process lm eval (both orders are in mempool) deposit correct" in {
    val registerOrder = Processed(
      LM.deposit,
      OrderState(TxId("b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e"), 0, Registered),
      None,
      None,
      None
    ).asInstanceOf[Processed.Any]
    val evalOrder =
      ProcessedOrderParser.make[IO].evaluated(LM.tx, 0, registerOrder, SelfHosted.pool, 0).unsafeRunSync().get

    val data = MempoolData(
      Address.fromStringUnsafe("9i245HdmSYwYsVR2qudtULu3BRBenPagNbT6uLi2Np9QZzdQWGH"),
      List(registerOrder, evalOrder)
    )
    implicit val network = NetworkMock.make[IO](List(data))

    val mempool = MempoolApi.make[IO, IO, ConnectionIO].unsafeRunSync()

    val res = mempool.ordersByAddress(List.empty).unsafeRunSync().head.asInstanceOf[LmDepositApi]

    val expected = LmDepositApi(
      registerOrder.order.id,
      OrderStatus.Mempool,
      SelfHosted.pool.poolId,
      LM.deposit.params.expectedNumEpochs,
      LM.deposit.params.tokens,
      evalOrder.evaluation.flatMap(_.widen[LmDepositCompoundEvaluation]).map(_.tokens),
      evalOrder.evaluation
        .flatMap(_.widen[LmDepositCompoundEvaluation])
        .map(_.bundle.id)
        .map(o => TokenId.unsafeFromString(o.value)),
      TxData(registerOrder.state.txId, registerOrder.state.timestamp),
      none,
      TxData(evalOrder.state.txId, evalOrder.state.timestamp).some
    )

    res shouldEqual expected
  }

  "Mempool api" should "process lm eval (only eval order is in mempool) deposit correct" in {
    val registerOrder = Processed(
      LM.deposit,
      OrderState(TxId("b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e"), 0, Registered),
      None,
      None,
      None
    ).asInstanceOf[Processed.Any]
    val evalOrder =
      ProcessedOrderParser.make[IO].evaluated(LM.tx, 0, registerOrder, SelfHosted.pool, 0).unsafeRunSync().get

    val data = MempoolData(
      Address.fromStringUnsafe("9i245HdmSYwYsVR2qudtULu3BRBenPagNbT6uLi2Np9QZzdQWGH"),
      List(
        evalOrder.copy(state =
          evalOrder.state.copy(status = fi.spectrum.core.domain.order.OrderStatus.WaitingEvaluation)
        )
      )
    )
    implicit val network = NetworkMock.make[IO](List(data))

    val mempool = MempoolApi.make[IO, IO, ConnectionIO].unsafeRunSync()

    val insert = InsertLmDeposit.insert(registerOrder.asInstanceOf[Processed[LmDepositV1]]).trans.unsafeRunSync()

    println(insert)

    val res = mempool.ordersByAddress(List.empty).unsafeRunSync().head.asInstanceOf[LmDepositApi]

    val expected = LmDepositApi(
      registerOrder.order.id,
      OrderStatus.Mempool,
      SelfHosted.pool.poolId,
      LM.deposit.params.expectedNumEpochs,
      LM.deposit.params.tokens,
      evalOrder.evaluation.flatMap(_.widen[LmDepositCompoundEvaluation]).map(_.tokens),
      evalOrder.evaluation
        .flatMap(_.widen[LmDepositCompoundEvaluation])
        .map(_.bundle.id)
        .map(o => TokenId.unsafeFromString(o.value)),
      TxData(registerOrder.state.txId, registerOrder.state.timestamp),
      none,
      TxData(evalOrder.state.txId, evalOrder.state.timestamp).some
    )

    res shouldEqual expected
  }

  "Mempool api" should "process lm register compound correct" in {
    val registerOrder = Processed(
      Compound.compoundLastEpoch,
      OrderState(TxId("b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e"), 0, Registered),
      None,
      None,
      None
    ).asInstanceOf[Processed.Any]

    val data = MempoolData(
      Address.fromStringUnsafe("9i245HdmSYwYsVR2qudtULu3BRBenPagNbT6uLi2Np9QZzdQWGH"),
      List(registerOrder)
    )
    implicit val network = NetworkMock.make[IO](List(data))

    val mempool = MempoolApi.make[IO, IO, ConnectionIO].unsafeRunSync()

    val res = mempool.ordersByAddress(List.empty).unsafeRunSync().head.asInstanceOf[LmCompoundApi]

    val expected = LmCompoundApi(
      registerOrder.order.id,
      OrderStatus.Mempool,
      Compound.compoundLastEpoch.poolId,
      Compound.compoundLastEpoch.params.vLq,
      Compound.compoundLastEpoch.params.tmp,
      Compound.compoundLastEpoch.params.bundleKeyId,
      TxData(registerOrder.state.txId, registerOrder.state.timestamp),
      none
    )

    res shouldEqual expected

  }

  "Mempool api" should "process lm eval (both orders are in mempool) compound correct" in {
    val registerOrder = Processed(
      Compound.compoundNotLastEpoch,
      OrderState(TxId("b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e"), 0, Registered),
      None,
      None,
      None
    ).asInstanceOf[Processed.Any]
    val evalOrder =
      ProcessedOrderParser
        .make[IO]
        .evaluated(Compound.compoundTx, 0, registerOrder, SelfHosted.pool, 0)
        .unsafeRunSync()
        .get

    val data = MempoolData(
      Address.fromStringUnsafe("9i245HdmSYwYsVR2qudtULu3BRBenPagNbT6uLi2Np9QZzdQWGH"),
      List(registerOrder, evalOrder)
    )
    implicit val network = NetworkMock.make[IO](List(data))

    val mempool = MempoolApi.make[IO, IO, ConnectionIO].unsafeRunSync()

    val res = mempool.ordersByAddress(List.empty).unsafeRunSync().head.asInstanceOf[LmCompoundApi]

    val expected = LmCompoundApi(
      registerOrder.order.id,
      OrderStatus.Mempool,
      Compound.compoundNotLastEpoch.poolId,
      Compound.compoundNotLastEpoch.params.vLq,
      Compound.compoundNotLastEpoch.params.tmp,
      Compound.compoundNotLastEpoch.params.bundleKeyId,
      TxData(registerOrder.state.txId, registerOrder.state.timestamp),
      TxData(evalOrder.state.txId, evalOrder.state.timestamp).some
    )

    res shouldEqual expected
  }

  "Mempool api" should "process lm eval (only eval order is in mempool) compound correct" in {
    val registerOrder = Processed(
      Compound.compoundNotLastEpoch,
      OrderState(TxId("b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e"), 0, Registered),
      None,
      None,
      None
    ).asInstanceOf[Processed.Any]
    val evalOrder =
      ProcessedOrderParser
        .make[IO]
        .evaluated(Compound.compoundTx, 0, registerOrder, SelfHosted.pool, 0)
        .unsafeRunSync()
        .get

    val data = MempoolData(
      Address.fromStringUnsafe("9i245HdmSYwYsVR2qudtULu3BRBenPagNbT6uLi2Np9QZzdQWGH"),
      List(
        evalOrder.copy(state =
          evalOrder.state.copy(status = fi.spectrum.core.domain.order.OrderStatus.WaitingEvaluation)
        )
      )
    )
    implicit val network = NetworkMock.make[IO](List(data))

    val mempool = MempoolApi.make[IO, IO, ConnectionIO].unsafeRunSync()

    val insert = InsertLmCompound.insert(registerOrder.asInstanceOf[Processed[CompoundV1]]).trans.unsafeRunSync()

    println(insert)

    val res = mempool.ordersByAddress(List.empty).unsafeRunSync().head.asInstanceOf[LmCompoundApi]

    val expected = LmCompoundApi(
      registerOrder.order.id,
      OrderStatus.Mempool,
      Compound.compoundNotLastEpoch.poolId,
      Compound.compoundNotLastEpoch.params.vLq,
      Compound.compoundNotLastEpoch.params.tmp,
      Compound.compoundNotLastEpoch.params.bundleKeyId,
      TxData(registerOrder.state.txId, registerOrder.state.timestamp),
      TxData(evalOrder.state.txId, evalOrder.state.timestamp).some
    )

    res shouldEqual expected
  }

  "Mempool api" should "process lm register redeem correct" in {
    val registerOrder = Processed(
      LM.redeem,
      OrderState(TxId("b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e"), 0, Registered),
      None,
      None,
      None
    ).asInstanceOf[Processed.Any]

    val data = MempoolData(
      Address.fromStringUnsafe("9i245HdmSYwYsVR2qudtULu3BRBenPagNbT6uLi2Np9QZzdQWGH"),
      List(registerOrder)
    )
    implicit val network = NetworkMock.make[IO](List(data))

    val mempool = MempoolApi.make[IO, IO, ConnectionIO].unsafeRunSync()

    val res = mempool.ordersByAddress(List.empty).unsafeRunSync().head.asInstanceOf[LmRedeemApi]

    val expected = LmRedeemApi(
      registerOrder.order.id,
      OrderStatus.Mempool,
      none,
      LM.redeem.bundleKeyId,
      LM.redeem.expectedLq,
      none,
      none,
      TxData(registerOrder.state.txId, registerOrder.state.timestamp),
      none,
      none
    )

    res shouldEqual expected
  }

  "Mempool api" should "process lm eval (both orders are in mempool) redeem correct" in {
    val registerOrder = Processed(
      LM.redeem,
      OrderState(TxId("b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e"), 0, Registered),
      None,
      None,
      None
    ).asInstanceOf[Processed.Any]
    val evalOrder =
      ProcessedOrderParser
        .make[IO]
        .evaluated(LM.redeemEvaluate, 0, registerOrder, SelfHosted.pool, 0)
        .unsafeRunSync()
        .get

    val data = MempoolData(
      Address.fromStringUnsafe("9i245HdmSYwYsVR2qudtULu3BRBenPagNbT6uLi2Np9QZzdQWGH"),
      List(registerOrder, evalOrder)
    )
    implicit val network = NetworkMock.make[IO](List(data))

    val mempool = MempoolApi.make[IO, IO, ConnectionIO].unsafeRunSync()

    val res = mempool.ordersByAddress(List.empty).unsafeRunSync().head.asInstanceOf[LmRedeemApi]

    val expected = LmRedeemApi(
      registerOrder.order.id,
      OrderStatus.Mempool,
      SelfHosted.pool.poolId.some,
      LM.redeem.bundleKeyId,
      LM.redeem.expectedLq,
      evalOrder.evaluation.flatMap(_.widen[LmRedeemEvaluation]).map(_.out),
      evalOrder.evaluation
        .flatMap(_.widen[LmRedeemEvaluation])
        .map(_.boxId)
        .map(b => TokenId.unsafeFromString(b.value)),
      TxData(registerOrder.state.txId, registerOrder.state.timestamp),
      none,
      TxData(evalOrder.state.txId, evalOrder.state.timestamp).some
    )

    res shouldEqual expected
  }


  "Mempool api" should "process lm eval (only eval order is in mempool) redeem correct" in {
    val registerOrder = Processed(
      LM.redeem,
      OrderState(TxId("b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e"), 0, Registered),
      None,
      None,
      None
    ).asInstanceOf[Processed.Any]
    val evalOrder =
      ProcessedOrderParser
        .make[IO]
        .evaluated(LM.redeemEvaluate, 0, registerOrder, SelfHosted.pool, 0)
        .unsafeRunSync()
        .get

    val data = MempoolData(
      Address.fromStringUnsafe("9i245HdmSYwYsVR2qudtULu3BRBenPagNbT6uLi2Np9QZzdQWGH"),
      List(
        evalOrder.copy(state =
          evalOrder.state.copy(status = fi.spectrum.core.domain.order.OrderStatus.WaitingEvaluation)
        )
      )
    )
    implicit val network = NetworkMock.make[IO](List(data))

    val mempool = MempoolApi.make[IO, IO, ConnectionIO].unsafeRunSync()

    val insert = InsertLmDeposit.insert(registerOrder.asInstanceOf[Processed[LmDepositV1]]).trans.unsafeRunSync()

    println(insert)

    val res = mempool.ordersByAddress(List.empty).unsafeRunSync().head.asInstanceOf[LmRedeemApi]

    val expected = LmRedeemApi(
      registerOrder.order.id,
      OrderStatus.Mempool,
      SelfHosted.pool.poolId.some,
      LM.redeem.bundleKeyId,
      LM.redeem.expectedLq,
      evalOrder.evaluation.flatMap(_.widen[LmRedeemEvaluation]).map(_.out),
      evalOrder.evaluation
        .flatMap(_.widen[LmRedeemEvaluation])
        .map(_.boxId)
        .map(b => TokenId.unsafeFromString(b.value)),
      TxData(registerOrder.state.txId, registerOrder.state.timestamp),
      none,
      TxData(evalOrder.state.txId, evalOrder.state.timestamp).some
    )

    res shouldEqual expected
  }
}