package fi.spectrum.indexer.services

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fi.spectrum.indexer.db.Indexer
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import fi.spectrum.common.process.OrderProcess.processOrder
import fi.spectrum.parser.lm.order.v1.LM._
import fi.spectrum.parser.lm.pool.v1.SelfHosted._
import fi.spectrum.parser.lm.compound.v1.Bundle._
import tofu.syntax.monadic._
import cats.syntax.option._
import fi.spectrum.core.domain.order.Order.Compound

class ProcessOrderSpec extends AnyFlatSpec with Matchers with Indexer {

  implicit val parser: ProcessedOrderParser[IO] = ProcessedOrderParser.make[IO]

  "process order func" should "work" in {
    val processed = parser.registered(deployDepositOrderTx, 0).unsafeRunSync().get

    val res = processOrder[IO](
      tx,
      0,
      0,
      _ => List(processed).pure[IO],
      _ => pool.some.pure[IO],
      unit[IO]
    ).unsafeRunSync()

    res(0).order shouldEqual deposit
    res(1).order shouldEqual bundle

    val res2 = processOrder[IO](
      compoundTx,
      0,
      0,
      _ => List(res(1)).pure[IO],
      _ => pool.some.pure[IO],
      unit[IO]
    ).unsafeRunSync()

//    val r = res2.asInstanceOf[List[Compound]]
//    r.length shouldEqual 2
  }
}
