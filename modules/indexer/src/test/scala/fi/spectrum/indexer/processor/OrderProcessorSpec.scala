package fi.spectrum.indexer.processor

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fi.spectrum.indexer.db.{Indexer, PGContainer}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import fi.spectrum.parser.lm.order.v1.LM
import fi.spectrum.parser.lm.compound.v1.Bundle
import fi.spectrum.common.process.OrderProcess._
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.domain.order.{OrderState, OrderStatus}
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.parser.lm.pool.v1.SelfHosted
import cats.syntax.option._

class OrderProcessorSpec extends AnyFlatSpec with Matchers with PGContainer with Indexer {

  implicit val parser = ProcessedOrderParser.make[IO]

  "Order processor" should "find LM deposit" in {
    val register =
      processOrder[IO](LM.deployDepositOrderTx, 1, 1, _ => IO.pure(List.empty), _ => IO.pure(None), IO.unit)
        .unsafeRunSync()

    register.length shouldEqual 1
    Processed.make(
      OrderState(LM.deployDepositOrderTx.id, 1, OrderStatus.Registered),
      LM.deposit
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
      Bundle.bundle
    ) shouldEqual eval.last

  }

  //todo !!!
  "Order processor" should "find LM compound" in {
    val order = List(Processed.make(OrderState(LM.deployDepositOrderTx.id, 1, OrderStatus.Registered), LM.deposit))
    val register =
      processOrder[IO](LM.tx, 1, 1, _ => IO.pure(order), _ => IO.pure(SelfHosted.pool.some), IO.unit)
        .unsafeRunSync()
        .last

    println("------")

    val eval =
      processOrder[IO](
        Bundle.compoundTx,
        1,
        1,
        _ => IO.pure(List(register)),
        _ => IO.pure(SelfHosted.pool.some),
        IO.unit
      )
        .unsafeRunSync()

    eval.length shouldEqual 2

    Processed.make(
      OrderState(Bundle.compoundTx.id, 1, OrderStatus.Evaluated),
      Bundle.bundle
    ) shouldEqual eval.head

    Processed.make(
      OrderState(Bundle.compoundTx.id, 1, OrderStatus.Registered),
      Bundle.bundle
    ) shouldEqual eval.last

  }



}
