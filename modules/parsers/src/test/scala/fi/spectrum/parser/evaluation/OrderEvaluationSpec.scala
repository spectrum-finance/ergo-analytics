package fi.spectrum.parser.evaluation

import fi.spectrum.core.domain.{AssetAmount, TokenId}
import fi.spectrum.core.domain.analytics.OrderEvaluation.LmDepositCompoundEvaluation
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.parser.evaluation.Transactions._
import fi.spectrum.parser.{e, CatsPlatform, OrderParser, PoolParser}
import org.ergoplatform.ErgoAddressEncoder
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec
import fi.spectrum.parser.lm.compound.v1.CompoundParserV1._

class OrderEvaluationSpec extends AnyPropSpec with Matchers with CatsPlatform {

  implicit def addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)

  val orderParser = OrderParser.make
  val poolParser  = PoolParser.make
  val parser      = OrderEvaluationParser.make

  property("Parse swap evaluation") {
    val order =
      swapRegisterTransaction.outputs.toList.map(o => orderParser.parse(o)).collectFirst { case Some(v) => v }.get
    val pool =
      swapEvaluateTransaction.outputs.toList.map(poolParser.parse(_, 0, 10)).collectFirst { case Some(v) => v }.get
    parser.parse(order, swapEvaluateTransaction.outputs.toList, pool, pool).get shouldEqual swapEval.copy(fee = ERG(0))
  }

  property("Parse redeem evaluation") {
    val order =
      redeemRegisterTransaction.outputs.toList.map(o => orderParser.parse(o)).collectFirst { case Some(v) => v }.get
    val pool =
      redeemEvaluateTransaction.outputs.toList.map(poolParser.parse(_, 0, 10)).collectFirst { case Some(v) => v }.get
    parser.parse(order, redeemEvaluateTransaction.outputs.toList, pool, pool).get shouldEqual redeemEval
  }

  property("Parse deposit evaluation") {
    val order =
      depositRegisterTransaction.outputs.toList.map(o => orderParser.parse(o)).collectFirst { case Some(v) => v }.get
    val pool =
      depositEvaluateTransaction.outputs.toList.map(poolParser.parse(_, 0, 10)).collectFirst { case Some(v) => v }.get

    val nextPool = pool.asInstanceOf[AmmPool]
    val newNext = nextPool.copy(
      x = nextPool.x.withAmount(nextPool.x.amount + 10),
      y = nextPool.y.withAmount(nextPool.y.amount + 11)
    )
    parser
      .parse(
        order,
        depositEvaluateTransaction.outputs.toList,
        pool,
        newNext
      )
      .get shouldEqual depositEval
  }

  property("Parse lm deposit evaluation") {
    import fi.spectrum.parser.lm.order.v1.LM._
    import fi.spectrum.parser.lm.pool.v1.SelfHosted._
    import fi.spectrum.parser.lm.compound.v1.Bundle._
    val order = orderParser.parse(depositOrder).get
    val eval  = parser.parse(order, tx.outputs.toList, pool, pool).get.asInstanceOf[LmDepositCompoundEvaluation]
    eval.bundle shouldEqual bundle
    eval.tokens shouldEqual AssetAmount(
      TokenId.unsafeFromString("cba6fabbc040c49873d3dea062a7fc81ff3262e1799dfd41e05014c5e8d91109"),
      9223372036854775806L
    )
  }
}
