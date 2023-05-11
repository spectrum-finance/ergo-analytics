package fi.spectrum.indexer.processor

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.syntax.option._
import fi.spectrum.common.process.OrderProcess._
import fi.spectrum.core.domain.analytics.OrderEvaluation.LmDepositCompoundEvaluation
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.{OrderId, OrderState, OrderStatus}
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.indexer.db.Indexer
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.parser.lm.compound.v1.Compound.compoundBatchTx
import fi.spectrum.parser.lm.compound.v1.{Compound, CompoundParserV1}
import fi.spectrum.parser.lm.order.v1.LM
import fi.spectrum.parser.lm.pool.v1.SelfHosted
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class OrderProcessorSpec extends AnyFlatSpec with Matchers with Indexer {

  implicit val parser = ProcessedOrderParser.make[IO]

  "Order processor" should "process Lm compound eval batch correct" in {
    Compound.expectedBatchCompoundsEval
      .foreach { order =>
        println("-----------")
        println(order.order.id)
        println("-----------")
      }
    val batch = processOrder[IO](
      Compound.compoundBatchTx,
      1,
      1,
      _ => IO.pure(Compound.expectedBatchCompoundsEval),
      _ => IO.pure(SelfHosted.pool.some),
      IO.unit
    )
      .unsafeRunSync()

    val list = List(
      OrderId("f49603442d7f8688356f917ac9463e03412f0631a2f53add6418c8656ec0f0d4"),
      OrderId("8b0f0181de985c06d4ac9d82518f4e9252c4244ab8b3a3e75e90ed1386e5ef4f"),
      OrderId("80293a3d0068b6c9022832444f98e20abf51bbf759e3c6af201199bcc640e409"),
    )

    //10147946 + 12439419 + 10147946

    batch.foreach { order =>
      if (list.contains(order.order.id)) {

        println("-----------")
        println(order.order.id)
        println(order.evaluation.get.widen[LmDepositCompoundEvaluation].get.tokens)
        println("-----------")
      }

    }

    //    batch
//      .map(_.order)
//      .sortBy(_.id.value) shouldEqual (Compound.expectedBatchCompoundsEval ::: Compound.expectedBatchCompoundsRegister)
//      .map(_.order)
//      .sortBy(_.id.value)

    val one = processOrder[IO](
      compoundBatchTx,
      1,
      1,
      _ => IO.pure(Compound.expectedBatchCompoundsEval.take(1)),
      _ => IO.pure(SelfHosted.pool.some),
      IO.unit
    )
      .unsafeRunSync()

    println(one)

    one.length shouldEqual 11
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
      processOrder[IO](compoundBatchTx, 1, 1, _ => IO.pure(List(register.head)), _ => IO.pure(SelfHosted.pool.some), IO.unit)
        .unsafeRunSync()

    eval.length shouldEqual 11

    ProcessedOrderParser
      .make[IO]
      .evaluated(compoundBatchTx, 1, register.head, SelfHosted.pool, 1, List.empty)
      .unsafeRunSync()
      .get shouldEqual eval.head

    Processed.make(
      OrderState(compoundBatchTx.id, 1, OrderStatus.Registered),
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
    eval.head.order.id shouldEqual OrderId("00165c410829ff0cc2d176acb1fe8e37d7b65d597faf11f5ce210ef8457cd780")
    eval.last.order.id shouldEqual OrderId("c622e40595f7edfdb26ae3709fbcbc8584f26ef258461e247d2516015ae42a43")
  }

}
