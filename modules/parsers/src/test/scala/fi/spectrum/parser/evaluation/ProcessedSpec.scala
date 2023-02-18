package fi.spectrum.parser.evaluation

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fi.spectrum.core.domain.analytics.OrderEvaluation.SwapEvaluation
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.{OrderState, OrderStatus}
import fi.spectrum.core.domain.{BoxId, TokenId}
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.evaluation.Transactions._
import org.ergoplatform.ErgoAddressEncoder
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class ProcessedSpec extends AnyPropSpec with Matchers with CatsPlatform {
  implicit def addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)
  implicit val spf: TokenId = TokenId.unsafeFromString("")
  val processedParser                             = ProcessedOrderParser.make[IO]

  property("Parse executed order") {
    val ts         = System.currentTimeMillis()
    val registered = processedParser.registered(swapRegisterTransaction, ts).unsafeRunSync().head
    val processed  = processedParser.evaluated(swapEvaluateTransaction, ts, registered, swapPool.get, 10).unsafeRunSync().get

    processed.poolBoxId shouldBe Some(BoxId("000bde68859c4ca2430c992604d80ea3bd0bf1c97bfa26ed60e8b9bccbffa79f"))
    processed.evaluation shouldBe Some(swapEval)
    processed.offChainFee shouldBe Some(expectedFee)
    processed.state shouldEqual OrderState(swapEvaluateTransaction.id, ts, OrderStatus.Evaluated)
  }

  property("Parse registered order") {
    val ts        = System.currentTimeMillis()
    val processed = processedParser.registered(swapRegisterTransaction, ts).unsafeRunSync().head

    processed.poolBoxId.nonEmpty shouldBe false
    processed.evaluation.nonEmpty shouldBe false
    processed.offChainFee.nonEmpty shouldBe false

    processed.state shouldEqual OrderState(swapRegisterTransaction.id, ts, OrderStatus.Registered)
  }

  property("Parse refund order") {
    val ts        = System.currentTimeMillis()
    val register  = processedParser.registered(swapRegisterRefundTransaction, ts).unsafeRunSync().head
    val processed = processedParser.refunded(swapRefundTransaction, ts, register).unsafeRunSync()

    processed.poolBoxId.nonEmpty shouldBe false
    processed.evaluation.nonEmpty shouldBe false
    processed.offChainFee.nonEmpty shouldBe false

    processed.state shouldEqual OrderState(swapRefundTransaction.id, ts, OrderStatus.Refunded)
  }
}
