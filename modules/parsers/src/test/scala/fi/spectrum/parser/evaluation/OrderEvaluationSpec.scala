package fi.spectrum.parser.evaluation

import fi.spectrum.parser.evaluation.Processed._
import fi.spectrum.parser.{CatsPlatform, OrderParser, PoolParser}
import org.ergoplatform.ErgoAddressEncoder
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class OrderEvaluationSpec extends AnyPropSpec with Matchers with CatsPlatform {

  implicit def addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)

  val orderParser = OrderParser.make
  val poolParser  = PoolParser.make
  val parser      = OrderEvaluationParser.make

  property("Parse swap evaluation") {
    val order = transactionSwap.inputs.toList.map(i => orderParser.parse(i.output)).collectFirst { case Some(v) => v }.get
    val pool = transactionSwap.outputs.toList.map(poolParser.parse).collectFirst { case Some(v) => v }.get
    parser.parse(order, transactionSwap.outputs.toList, pool).get shouldEqual OrderEval.swap.eval
  }

  property("Parse redeem evaluation") {
    val order = transactionRedeem.inputs.toList.map(i => orderParser.parse(i.output)).collectFirst { case Some(v) => v }.get
    val pool = transactionRedeem.outputs.toList.map(poolParser.parse).collectFirst { case Some(v) => v }.get
    parser.parse(order, transactionRedeem.outputs.toList, pool).get shouldEqual OrderEval.redeem.eval
  }

  property("Parse deposit evaluation") {
    val order = transactionDeposit.inputs.toList.map(i => orderParser.parse(i.output)).collectFirst { case Some(v) => v }.get
    val pool = transactionDeposit.outputs.toList.map(poolParser.parse).collectFirst { case Some(v) => v }.get
    parser.parse(order, transactionDeposit.outputs.toList, pool).get shouldEqual OrderEval.deposit.eval
  }

}
