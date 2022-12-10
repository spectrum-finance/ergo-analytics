package fi.spectrum.parser.evaluation

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.OrderEvaluation
import fi.spectrum.core.domain.analytics.OrderEvaluation.{DepositEvaluation, RedeemEvaluation, SwapEvaluation}
import fi.spectrum.core.domain.order.OrderOptics._
import fi.spectrum.core.domain.order.{Order, Redeemer}
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.pool.PoolOptics._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, SErgoTree, TokenId}
import glass.Label
import glass.classic.Optional

final class OrderEvaluationParser {

  def parse(order: Order.Any, outputs: List[Output], pool: Pool.Any): Option[OrderEvaluation] = {
    val redeemer = order.redeemer match {
      case Redeemer.ErgoTreeRedeemer(value)  => value
      case Redeemer.PublicKeyRedeemer(value) => value.ergoTree
    }
    outputs
      .map { output =>
        redeem(redeemer, output, pool) orElse swap(redeemer, order, output) orElse deposit(redeemer, output, pool)
      }
      .collectFirst { case Some(v) => v }
  }

  def redeem(redeemer: SErgoTree, output: Output, pool: Pool.Any): Option[RedeemEvaluation] =
    if (redeemer == output.ergoTree) {
      val outputX = implicitly[Optional[Pool.Any, AssetAmount] with Label["x"]].getOption(pool) match {
        case Some(value) if value.isNative => AssetAmount.native(output.value).some
        case Some(value)                   => output.assets.find(_.tokenId == value.tokenId).map(AssetAmount.fromBoxAsset)
        case None                          => none
      }
      val outputY = implicitly[Optional[Pool.Any, AssetAmount] with Label["y"]].getOption(pool) match {
        case Some(value) if value.isNative => AssetAmount.native(output.value).some
        case Some(value)                   => output.assets.find(_.tokenId == value.tokenId).map(AssetAmount.fromBoxAsset)
        case None                          => none
      }

      (outputX, outputY) match {
        case (Some(x), Some(y)) => RedeemEvaluation(x, y).some
        case _                  => none
      }
    } else none

  def swap(redeemer: SErgoTree, order: Order.Any, output: Output): Option[SwapEvaluation] =
    Optional[Order.Any, AssetAmount].getOption(order) match {
      case Some(quote) if output.ergoTree == redeemer =>
        val out =
          if (quote.isNative) AssetAmount.native(output.value).some
          else output.assets.find(_.tokenId == quote.tokenId).map(AssetAmount.fromBoxAsset)
        out.map(SwapEvaluation(_))
      case None => none
    }

  def deposit(redeemer: SErgoTree, output: Output, pool: Pool.Any): Option[DepositEvaluation] =
    if (output.ergoTree == redeemer)
      Optional[Pool.Any, TokenId].getOption(pool) match {
        case Some(value) =>
          output.assets.find(_.tokenId == value).map(AssetAmount.fromBoxAsset).map(DepositEvaluation(_))
        case None => none
      }
    else none

}

object OrderEvaluationParser {
  implicit def make: OrderEvaluationParser = new OrderEvaluationParser
}