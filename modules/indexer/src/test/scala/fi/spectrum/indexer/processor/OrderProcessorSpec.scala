package fi.spectrum.indexer.processor

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.syntax.option._
import fi.spectrum.common.process.OrderProcess._
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.{OrderId, OrderState, OrderStatus}
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.indexer.db.Indexer
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.parser.lm.compound.v1.{Compound, CompoundParserV1}
import fi.spectrum.parser.lm.order.v1.LM
import fi.spectrum.parser.lm.pool.v1.SelfHosted
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class OrderProcessorSpec extends AnyFlatSpec with Matchers with Indexer {

  implicit val parser = ProcessedOrderParser.make[IO]

  "Order processor" should "process Lm compound eval batch correct" in {
    val batch = processOrder[IO](
      Compound.compoundBatchTx,
      1,
      1,
      _ => IO.pure(Compound.expectedBatchCompoundsEval),
      _ => IO.pure(SelfHosted.pool.some),
      IO.unit
    )
      .unsafeRunSync()

    batch
      .map(_.order)
      .sortBy(_.id.value) shouldEqual (Compound.expectedBatchCompoundsEval ::: Compound.expectedBatchCompoundsRegister)
      .map(_.order)
      .sortBy(_.id.value)

    val one = processOrder[IO](
      Compound.compoundTxOneOrder,
      1,
      1,
      _ => IO.pure(Compound.expectedBatchCompoundsEval.take(1)),
      _ => IO.pure(SelfHosted.pool.some),
      IO.unit
    )
      .unsafeRunSync()

    one.length shouldEqual 2
  }

  "Order processor" should "find LM deposit" in {
    val register =
      processOrder[IO](LM.depositOrderRegister, 1, 1, _ => IO.pure(List.empty), _ => IO.pure(None), IO.unit)
        .unsafeRunSync()

    register.length shouldEqual 1
    Processed.make(
      OrderState(LM.depositOrderRegister.id, 1, OrderStatus.Registered),
      LM.deposit2
    ) shouldEqual register.head

    val eval =
      processOrder[IO](LM.tx, 1, 1, _ => IO.pure(List(register.head)), _ => IO.pure(SelfHosted.pool.some), IO.unit)
        .unsafeRunSync()

    eval.length shouldEqual 2

    ProcessedOrderParser
      .make[IO]
      .evaluated(LM.tx, 1, register.head, SelfHosted.pool, 1)
      .unsafeRunSync()
      .get shouldEqual eval.head

    Processed.make(
      OrderState(LM.tx.id, 1, OrderStatus.Registered),
      CompoundParserV1.v1Compound
        .compound(
          LM.compoundCreateForDeposit,
          ErgoTreeSerializer.default.deserialize(LM.compoundCreateForDeposit.ergoTree)
        )
        .get
    ) shouldEqual eval.last

  }

  "Order processor" should "find LM redeem" in {

    val registerCompound = Processed.make(
      OrderState(LM.deployRedeem.id, 0, OrderStatus.Registered),
      CompoundParserV1.v1Compound
        .compound(
          Compound.compoundRegisterForRedeem,
          ErgoTreeSerializer.default.deserialize(Compound.compoundRegisterForRedeem.ergoTree)
        )
        .get
    )

    val registerRedeem =
      processOrder[IO](LM.deployRedeem, 1, 1, _ => IO.pure(List.empty), _ => IO.pure(None), IO.unit)
        .unsafeRunSync()
        .head

    val eval =
      processOrder[IO](
        LM.redeemEvaluate,
        1,
        1,
        _ => IO.pure(List(registerRedeem, registerCompound)),
        _ => IO.pure(SelfHosted.pool.some),
        IO.unit
      )
        .unsafeRunSync()

    eval.length shouldEqual 2
    eval.head.order.id shouldEqual OrderId("6293e74ef80b60076ceba4372c9f68c8bcd610eb9ddd6b565e4adf624f4a9d7a")
    eval.last.order.id shouldEqual OrderId("353fb1e32f52c4353daf8c66c8426b36f2d1989a88efd64bd38ac3173dc3c4ca")
  }

}
