package fi.spectrum.parser.evaluation

import fi.spectrum.core.domain.TokenId
import fi.spectrum.parser.evaluation.OffChainFee._
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
    val order = transaction.inputs.toList.map(i => orderParser.parse(i.output)).collectFirst { case Some(v) => v }.get
    val pool  = transaction.outputs.toList.map(poolParser.parse).collectFirst { case Some(v) => v }.get
    val fee   = feeParser.parse(transaction.outputs.toList, order, pool.poolId).get
    fee shouldEqual expectedFee
  }
}
