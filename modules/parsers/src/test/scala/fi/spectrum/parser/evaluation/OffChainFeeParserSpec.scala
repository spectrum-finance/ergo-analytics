package fi.spectrum.parser.evaluation

import fi.spectrum.parser.{CatsPlatform, OrderParser, PoolParser}
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec
import OffChainFee._
import cats.implicits.catsSyntaxEq
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.transaction.Transaction
import org.ergoplatform.ErgoAddressEncoder
import io.circe.parser.decode
class OffChainFeeParserSpec extends AnyPropSpec with Matchers with CatsPlatform  {

  implicit def addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)

  val orderParser = OrderParser.make
  val poolParser = PoolParser.make
  val feeParser = OffChainFeeParser.make(TokenId.unsafeFromString(""))

  property("r") {

    println(transaction)
    val order = transaction.inputs.toList.map(i => orderParser.parse(i.output)).collectFirst { case Some(v) => v }.get
    val pool = transaction.outputs.toList.map(poolParser.parse).collectFirst { case Some(v) => v }.get

    val fee = feeParser.parse(transaction.outputs.toList, order, pool.poolId).get

    (fee eqv expectedFee) shouldBe true
  }
}
