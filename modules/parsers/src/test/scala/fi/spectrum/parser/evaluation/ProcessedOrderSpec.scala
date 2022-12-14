package fi.spectrum.parser.evaluation

import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.order.{OrderState, OrderStatus}
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.evaluation.Processed._
import org.ergoplatform.ErgoAddressEncoder
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class ProcessedOrderSpec extends AnyPropSpec with Matchers with CatsPlatform {
  implicit def addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)
  val processedParser                             = ProcessedOrderParser.make(TokenId.unsafeFromString(""))

  property("Parse executed order") {
    val ts = System.currentTimeMillis()
    val processed = processedParser.parse(transactionSwap, ts).get

    processed.pool.nonEmpty shouldBe true
    processed.evaluation.nonEmpty shouldBe true
    processed.offChainFee.nonEmpty shouldBe true

    processed.state shouldEqual OrderState(transactionSwap.id, ts, OrderStatus.Executed)
  }

  property("Parse registered order") {
    val ts = System.currentTimeMillis()
    val processed = processedParser.parse(transactionSwapRegister, ts).get

    processed.pool.nonEmpty shouldBe false
    processed.evaluation.nonEmpty shouldBe false
    processed.offChainFee.nonEmpty shouldBe false

    processed.state shouldEqual OrderState(transactionSwapRegister.id, ts, OrderStatus.Registered)
  }

  property("Parse refund order") {
    val ts = System.currentTimeMillis()
    val processed = processedParser.parse(refundTx, ts).get

    processed.pool.nonEmpty shouldBe false
    processed.evaluation.nonEmpty shouldBe false
    processed.offChainFee.nonEmpty shouldBe false

    processed.state shouldEqual OrderState(refundTx.id, ts, OrderStatus.Refunded)
  }
}
