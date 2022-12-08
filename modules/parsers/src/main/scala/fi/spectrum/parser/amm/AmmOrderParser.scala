package fi.spectrum.parser.amm

import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.OrderType.AMM
import fi.spectrum.core.domain.order.Version
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.domain.AmmType
import sigmastate.Values
import sigmastate.Values.ErgoTree

trait AmmOrderParser[V <: Version, T <: AmmType] {
  def swap(box: Output, tree: Values.ErgoTree): Option[Swap[V, AMM]]
  def deposit(box: Output, tree: Values.ErgoTree): Option[Deposit[V, AMM]]
  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem[V, AMM]]
}
