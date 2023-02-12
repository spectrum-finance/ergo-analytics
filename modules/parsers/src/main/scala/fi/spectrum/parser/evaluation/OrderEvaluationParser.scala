package fi.spectrum.parser.evaluation

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.OrderEvaluation._
import fi.spectrum.core.domain.analytics.{OrderEvaluation, Version}
import fi.spectrum.core.domain.order.Order.Deposit.{AmmDeposit, LmDeposit}
import fi.spectrum.core.domain.order.Order.Redeem.{AmmRedeem, LmRedeem}
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.OrderOptics._
import fi.spectrum.core.domain.order.{Order, Redeemer}
import fi.spectrum.core.domain.pool.Pool
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.core.domain.pool.PoolOptics._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, PubKey, SErgoTree, TokenId}
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.lm.compound.CompoundParser
import glass.Label
import glass.classic.{Optional, Prism}
import org.ergoplatform.{ErgoAddressEncoder, P2PKAddress}
import cats.syntax.eq._

/** Parse order evaluation result
  */
final class OrderEvaluationParser(bundleParser: CompoundParser[Version])(implicit e: ErgoAddressEncoder) {

  def parse(order: Order, outputs: List[Output], pool: Pool, nextPool: Pool): Option[OrderEvaluation] = {
    val redeemer = order.redeemer match {
      case Redeemer.ErgoTreeRedeemer(value)  => value
      case Redeemer.PublicKeyRedeemer(value) => value.ergoTree
    }
    outputs
      .map { output =>
        order match {
          case _: AmmDeposit => ammDeposit(redeemer, output, pool, nextPool)
          case _: LmDeposit  => lmDepositCompound(outputs)
          case c: Compound   => lmCompound(c, outputs)
          case _: AmmRedeem  => ammRedeem(redeemer, output, pool)
          case _: LmRedeem   => lmRedeem(redeemer, output, pool)
          case _: Swap       => swap(redeemer, order, output)
          case _: Lock       => none
        }
      }
      .collectFirst { case Some(v) => v }
  }

  def ammRedeem(redeemer: SErgoTree, output: Output, pool: Pool): Option[AmmRedeemEvaluation] =
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
        case (Some(x), Some(y)) => AmmRedeemEvaluation(x, y).some
        case _                  => none
      }
    } else none

  def swap(redeemer: SErgoTree, order: Order, output: Output): Option[SwapEvaluation] =
    Optional[Order, AssetAmount].getOption(order) match {
      case Some(quote) if output.ergoTree == redeemer =>
        val out =
          if (quote.isNative) AssetAmount.native(output.value).some
          else output.assets.find(_.tokenId == quote.tokenId).map(AssetAmount.fromBoxAsset)
        out.map(SwapEvaluation.emptyFee)
      case _ => none
    }

  def ammDeposit(redeemer: SErgoTree, output: Output, pool: Pool, nextPool: Pool): Option[AmmDepositEvaluation] =
    for {
      poolLp   <- Optional[Pool, TokenId].getOption(pool) if output.ergoTree == redeemer
      poolPrev <- Prism[Pool, AmmPool].getOption(pool)
      poolNext <- Prism[Pool, AmmPool].getOption(nextPool)
      lp       <- output.assets.find(_.tokenId == poolLp).map(AssetAmount.fromBoxAsset)
    } yield {
      val actualX = poolNext.x.amount - poolPrev.x.amount
      val actualY = poolNext.y.amount - poolPrev.y.amount
      AmmDepositEvaluation(lp, actualX, actualY)
    }

  def lmRedeem(redeemer: SErgoTree, output: Output, pool: Pool): Option[LmRedeemEvaluation] =
    if (redeemer == output.ergoTree)
      output.assets.headOption.map(b => LmRedeemEvaluation(AssetAmount.fromBoxAsset(b), output.boxId, pool.poolId))
    else none

  def lmCompound(order: Compound, outputs: List[Output]): Option[LmDepositCompoundEvaluation] =
    outputs
      .flatMap(out => bundleParser.compound(out, ErgoTreeSerializer.default.deserialize(out.ergoTree)))
      .collectFirst { case o if o === order => o }
      .flatMap { compound =>
        outputs
          .find { out =>
            e.fromProposition(ErgoTreeSerializer.default.deserialize(out.ergoTree))
              .toOption
              .collect { case address: P2PKAddress => PubKey.fromBytes(address.pubkeyBytes) }
              .exists(x => x.value == order.redeemer.hexString && out.boxId != compound.box.boxId)
          }
          .flatMap { out =>
            out.assets.headOption
              .map { asset =>
                LmDepositCompoundEvaluation(AssetAmount.fromBoxAsset(asset), out.boxId, compound)
              }
          }
      }

  def lmDepositCompound(outputs: List[Output]): Option[LmDepositCompoundEvaluation] =
    outputs
      .flatMap(out => bundleParser.compound(out, ErgoTreeSerializer.default.deserialize(out.ergoTree)))
      .headOption
      .flatMap { bundle =>
        outputs
          .find { out =>
            e.fromProposition(ErgoTreeSerializer.default.deserialize(out.ergoTree))
              .toOption
              .collect { case address: P2PKAddress => PubKey.fromBytes(address.pubkeyBytes) }
              .exists(_.value == bundle.redeemer.hexString)
          }
          .flatMap { out =>
            out.assets.headOption
              .map { asset =>
                LmDepositCompoundEvaluation(AssetAmount.fromBoxAsset(asset), out.boxId, bundle)
              }
          }
      }
}

object OrderEvaluationParser {

  implicit def make(implicit bundleParser: CompoundParser[Version], e: ErgoAddressEncoder): OrderEvaluationParser =
    new OrderEvaluationParser(
      bundleParser
    )
}
