package fi.spectrum.parser.amm.order

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.analytics.Version._
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.domain.AmmType
import fi.spectrum.parser.domain.AmmType._
import sigmastate.Values

/** Parser which parse any amm order. Depends on amm order version and amm type.
  *
  * Gives access to parse swap/redeem/deposit using appropriate methods operation
  * or any of them using 'order' method.
  *
  * @tparam V - operation version, e.g. Swap v1, Swap v2 etc.
  * @tparam T - amm type, e.g. N2T or T2T
  */
trait AmmOrderParser[+V <: Version, +T <: AmmType] { self =>

  /** Parse exactly swap order
    */
  def swap(box: Output, tree: Values.ErgoTree): Option[Swap]

  /** Parse exactly deposit order
    */
  def deposit(box: Output, tree: Values.ErgoTree): Option[Deposit]

  /** Parse exactly redeem order
    */
  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem]

  /** Parse any order of V version and T type
    */
  def order(box: Output, tree: Values.ErgoTree): Option[Order] =
    swap(box, tree) orElse deposit(box, tree) orElse redeem(box, tree)

  def or(that: => AmmOrderParser[Version, AmmType]): AmmOrderParser[Version, AmmType] =
    new AmmOrderParser[Version, AmmType] {

      def swap(box: Output, tree: Values.ErgoTree): Option[Swap] =
        self.swap(box, tree) orElse that.swap(box, tree)

      def deposit(box: Output, tree: Values.ErgoTree): Option[Deposit] =
        self.deposit(box, tree) orElse that.deposit(box, tree)

      def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem] =
        self.redeem(box, tree) orElse that.redeem(box, tree)

      override def order(box: Output, tree: Values.ErgoTree): Option[Order] =
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
