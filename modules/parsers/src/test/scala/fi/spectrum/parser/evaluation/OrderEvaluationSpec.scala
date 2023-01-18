package fi.spectrum.parser.evaluation

import fi.spectrum.parser.evaluation.Transactions._
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
    val order =
      swapRegisterTransaction.outputs.toList.map(o => orderParser.parse(o)).collectFirst { case Some(v) => v }.get
    val pool = swapEvaluateTransaction.outputs.toList.map(poolParser.parse(_, 0)).collectFirst { case Some(v) => v }.get
    parser.parse(order, swapEvaluateTransaction.outputs.toList, pool).get shouldEqual swapEval
  }

  property("Parse redeem evaluation") {
    val order =
      redeemRegisterTransaction.outputs.toList.map(o => orderParser.parse(o)).collectFirst { case Some(v) => v }.get
    val pool =
      redeemEvaluateTransaction.outputs.toList.map(poolParser.parse(_, 0)).collectFirst { case Some(v) => v }.get
    parser.parse(order, redeemEvaluateTransaction.outputs.toList, pool).get shouldEqual redeemEval
  }

  property("Parse deposit evaluation") {
    val order =
      depositRegisterTransaction.outputs.toList.map(o => orderParser.parse(o)).collectFirst { case Some(v) => v }.get
    val pool =
      depositEvaluateTransaction.outputs.toList.map(poolParser.parse(_, 0)).collectFirst { case Some(v) => v }.get
    parser.parse(order, depositEvaluateTransaction.outputs.toList, pool).get shouldEqual depositEval
  }

}
