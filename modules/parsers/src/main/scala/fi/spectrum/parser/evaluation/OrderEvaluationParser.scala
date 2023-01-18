package fi.spectrum.parser.evaluation

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.OrderEvaluation
import fi.spectrum.core.domain.analytics.OrderEvaluation.{DepositEvaluation, RedeemEvaluation, SwapEvaluation}
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.OrderOptics._
import fi.spectrum.core.domain.order.{Order, Redeemer}
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.pool.PoolOptics._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, SErgoTree, TokenId}
import glass.Label
import glass.classic.Optional

/** Parse order evaluation result
  */
final class OrderEvaluationParser {

  def parse(order: Order, outputs: List[Output], pool: Pool): Option[OrderEvaluation] = {
    val redeemer = order.redeemer match {
      case Redeemer.ErgoTreeRedeemer(value)  => value
      case Redeemer.PublicKeyRedeemer(value) => value.ergoTree
    }
    outputs
      .map { output =>
        order match {
          case _: Deposit => deposit(redeemer, output, pool)
          case _: Redeem  => redeem(redeemer, output, pool)
          case _: Swap    => swap(redeemer, order, output)
          case _: Lock    => none
        }

      }
      .collectFirst { case Some(v) => v }
  }

  def redeem(redeemer: SErgoTree, output: Output, pool: Pool): Option[RedeemEvaluation] =
    if (redeemer == output.ergoTree) {
      val outputX = implicitly[Optional[Pool, AssetAmount] with Label["x"]].getOption(pool) match {
        case Some(value) if value.isNative => AssetAmount.native(output.value).some
        case Some(value)                   => output.assets.find(_.tokenId == value.tokenId).map(AssetAmount.fromBoxAsset)
        case None                          => none
      }
      val outputY = implicitly[Optional[Pool, AssetAmount] with Label["y"]].getOption(pool) match {
        case Some(value) if value.isNative => AssetAmount.native(output.value).some
        case Some(value)                   => output.assets.find(_.tokenId == value.tokenId).map(AssetAmount.fromBoxAsset)
        case None                          => none
      }

      (outputX, outputY) match {
        case (Some(x), Some(y)) => RedeemEvaluation(x, y).some
        case _                  => none
      }
    } else none

  def swap(redeemer: SErgoTree, order: Order, output: Output): Option[SwapEvaluation] =
    Optional[Order, AssetAmount].getOption(order) match {
      case Some(quote) if output.ergoTree == redeemer =>
        val out =
          if (quote.isNative) AssetAmount.native(output.value).some
          else output.assets.find(_.tokenId == quote.tokenId).map(AssetAmount.fromBoxAsset)
        out.map(SwapEvaluation.apply)
      case _ => none
    }

  def deposit(redeemer: SErgoTree, output: Output, pool: Pool): Option[DepositEvaluation] =
    if (output.ergoTree == redeemer)
      Optional[Pool, TokenId].getOption(pool) match {
        case Some(poolLP) =>
          output.assets.find(_.tokenId == poolLP).map(AssetAmount.fromBoxAsset).map(DepositEvaluation.apply)
        case None => none
      }
    else none

}

object OrderEvaluationParser {
  implicit def make: OrderEvaluationParser = new OrderEvaluationParser
}
