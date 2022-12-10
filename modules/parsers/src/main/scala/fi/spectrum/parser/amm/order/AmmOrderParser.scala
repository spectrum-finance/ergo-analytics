package fi.spectrum.parser.amm.order

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.OrderType.AMM
import fi.spectrum.core.domain.analytics.Version._
import fi.spectrum.core.domain.order.{Operation, Order}
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.domain.AmmType
import fi.spectrum.parser.domain.AmmType._
import sigmastate.Values

trait AmmOrderParser[+V <: Version, +T <: AmmType] { self =>
  def swap(box: Output, tree: Values.ErgoTree): Option[Swap[V, AMM]]
  def deposit(box: Output, tree: Values.ErgoTree): Option[Deposit[V, AMM]]
  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem[V, AMM]]

  def order(box: Output, tree: Values.ErgoTree): Option[Order[V, AMM, Operation]] =
    swap(box, tree) orElse deposit(box, tree) orElse redeem(box, tree)

  def or(that: => AmmOrderParser[Version, AmmType]): AmmOrderParser[Version, AmmType] =
    new AmmOrderParser[Version, AmmType] {

      def swap(box: Output, tree: Values.ErgoTree): Option[Swap[Version, AMM]] =
        self.swap(box, tree) orElse that.swap(box, tree)

      def deposit(box: Output, tree: Values.ErgoTree): Option[Deposit[Version, AMM]] =
        self.deposit(box, tree) orElse that.deposit(box, tree)

      def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem[Version, AMM]] =
        self.redeem(box, tree) orElse that.redeem(box, tree)

      override def order(box: Output, tree: Values.ErgoTree): Option[Order[Version, AMM, Operation]] =
        self.order(box, tree) orElse that.order(box, tree)
    }
}

object AmmOrderParser {

  implicit def make(implicit
    legacyV1N2TA: AmmOrderParser[LegacyV1, N2T],
    legacyV1T2TA: AmmOrderParser[LegacyV1, T2T],
    legacyV2N2TA: AmmOrderParser[LegacyV2, N2T],
    legacyV2T2TA: AmmOrderParser[LegacyV2, T2T],
    v1N2TA: AmmOrderParser[V1, N2T],
    v1T2TA: AmmOrderParser[V1, T2T],
    v2N2TA: AmmOrderParser[V2, N2T],
    v2T2TA: AmmOrderParser[V2, T2T],
    v3N2TA: AmmOrderParser[V3, N2T],
    v3T2TA: AmmOrderParser[V3, T2T]
  ): AmmOrderParser[Version, AmmType] =
    List[AmmOrderParser[Version, AmmType]](
      legacyV1N2TA,
      legacyV1T2TA,
      legacyV2N2TA,
      legacyV2T2TA,
      v1N2TA,
      v1T2TA,
      v2N2TA,
      v2T2TA,
      v3N2TA,
      v3T2TA
    ).reduceLeft(_ or _)
}
