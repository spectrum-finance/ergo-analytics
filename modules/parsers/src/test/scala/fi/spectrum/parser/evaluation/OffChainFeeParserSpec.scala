package fi.spectrum.parser.evaluation

import fi.spectrum.core.domain.TokenId
import fi.spectrum.parser.evaluation.Transactions._
import fi.spectrum.parser.{CatsPlatform, OrderParser, PoolParser}
import org.ergoplatform.ErgoAddressEncoder
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class OffChainFeeParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  implicit def addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)

  val orderParser = OrderParser.make
  val poolParser  = PoolParser.make
  val feeParser   = OffChainFeeParser.make(TokenId.unsafeFromString(""))

  property("Parse off-chain fee") {
    val order =
      swapRegisterTransaction.outputs.toList.map(o => orderParser.parse(o)).collectFirst { case Some(v) => v }.get
    val pool = swapEvaluateTransaction.outputs.toList.map(poolParser.parse(_, 0)).collectFirst { case Some(v) => v }.get
    val fee  = feeParser.parse(swapEvaluateTransaction.outputs.toList, order, pool.poolId).get
    fee shouldEqual expectedFee
  }
}
