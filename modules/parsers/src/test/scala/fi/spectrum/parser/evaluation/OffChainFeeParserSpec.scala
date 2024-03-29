package fi.spectrum.parser.evaluation

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fi.spectrum.core.domain.TokenId
import fi.spectrum.parser.evaluation.Transactions._
import fi.spectrum.parser.{CatsPlatform, OrderParser, PoolParser}
import org.ergoplatform.ErgoAddressEncoder
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class OffChainFeeParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  implicit def addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)
  implicit val spf: TokenId                       = TokenId.unsafeFromString("")

  val orderParser = OrderParser.make
  val poolParser  = PoolParser.make
  val feeParser   = OffChainFeeParser.make

  property("Parse off-chain fee") {

    val register = ProcessedOrderParser.make[IO].registered(swapRegisterTransaction, 0).unsafeRunSync().head
    val pool =
      swapEvaluateTransaction.outputs.toList.map(poolParser.parse(_, 0, 10)).collectFirst { case Some(v) => v }.get
    val processed = ProcessedOrderParser
      .make[IO]
      .evaluated(
        swapEvaluateTransaction,
        0,
        register,
        pool,
        1, List.empty
      )
      .unsafeRunSync()
      .get

    val fee = feeParser.parse(swapEvaluateTransaction.outputs.toList, processed, processed.evaluation, pool.poolId).get
    fee shouldEqual expectedFee
  }

  property("Parse off-chain fee swap v3") {

    val register = ProcessedOrderParser.make[IO].registered(swapRegisterV3Tx, 0).unsafeRunSync().head
    val pool =
      evalV3SwapTx.outputs.toList.map(poolParser.parse(_, 0, 10)).collectFirst { case Some(v) => v }.get
    val processed = ProcessedOrderParser
      .make[IO]
      .evaluated(
        evalV3SwapTx,
        0,
        register,
        pool,
        1, List.empty
      )
      .unsafeRunSync()
      .get

    val fee = feeParser.parse(evalV3SwapTx.outputs.toList, processed, processed.evaluation, pool.poolId).get
    println(fee.fee)
    println(fee.outputId)
  }
}
