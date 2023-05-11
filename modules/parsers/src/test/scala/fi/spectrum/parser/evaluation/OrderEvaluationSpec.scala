package fi.spectrum.parser.evaluation

import fi.spectrum.core.domain.{AssetAmount, TokenId}
import fi.spectrum.core.domain.analytics.OrderEvaluation.LmDepositCompoundEvaluation
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.evaluation.Transactions._
import fi.spectrum.parser.lm.compound.v1.CompoundParserV1
import fi.spectrum.parser.{e, CatsPlatform, OrderParser, PoolParser}
import org.ergoplatform.ErgoAddressEncoder
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec
import fi.spectrum.parser.lm.compound.v1.CompoundParserV1._
import fi.spectrum.parser.lm.order.v1.LM

class OrderEvaluationSpec extends AnyPropSpec with Matchers with CatsPlatform {

  implicit def addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)
  implicit val spf: TokenId = TokenId.unsafeFromString("")

  val orderParser = OrderParser.make
  val poolParser  = PoolParser.make
  val parser      = OrderEvaluationParser.make

  property("Parse swap evaluation") {
    val order =
      swapRegisterTransaction.outputs.toList.map(o => orderParser.parse(o)).collectFirst { case Some(v) => v }.get
    val pool =
      swapEvaluateTransaction.outputs.toList.map(poolParser.parse(_, 0, 10)).collectFirst { case Some(v) => v }.get
    parser.parse(order, swapEvaluateTransaction.outputs.toList, pool, pool, List.empty).get shouldEqual swapEval.copy(fee = ERG(0))
  }

  property("Parse redeem evaluation") {
    val order =
      redeemRegisterTransaction.outputs.toList.map(o => orderParser.parse(o)).collectFirst { case Some(v) => v }.get
    val pool =
      redeemEvaluateTransaction.outputs.toList.map(poolParser.parse(_, 0, 10)).collectFirst { case Some(v) => v }.get
    parser.parse(order, redeemEvaluateTransaction.outputs.toList, pool, pool, List.empty).get shouldEqual redeemEval
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
        newNext, List.empty
      )
      .get shouldEqual depositEval
  }

  property("Parse lm deposit evaluation") {
    import fi.spectrum.parser.lm.order.v1.LM._
    import fi.spectrum.parser.lm.pool.v1.SelfHosted._
    import fi.spectrum.parser.lm.compound.v1.Compound._
    val order = orderParser.parse(depositOrder).get
    val eval  = parser.parse(order, compoundBatchTx.outputs.toList, pool, pool, List.empty).get.asInstanceOf[LmDepositCompoundEvaluation]
    eval.bundle shouldEqual CompoundParserV1.v1Compound
      .compound(
        LM.depositCompoundEval,
        ErgoTreeSerializer.default.deserialize(LM.depositCompoundEval.ergoTree)
      )
      .get
    eval.tokens shouldEqual AssetAmount(
      TokenId.unsafeFromString("50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00"),
      1823507
    )
  }
}
