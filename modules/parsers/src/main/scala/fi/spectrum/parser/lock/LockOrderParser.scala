package fi.spectrum.parser.lock

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order.Lock
import Version.V1
import fi.spectrum.core.domain.transaction.Output
import sigmastate.Values

trait LockOrderParser[+V <: Version] { self =>
  def lock(box: Output, tree: Values.ErgoTree): Option[Lock[V]]

  def or(that: => LockOrderParser[Version]): LockOrderParser[Version] = (box: Output, tree: Values.ErgoTree) =>
    self.lock(box, tree) orElse that.lock(box, tree)
}

object LockOrderParser {

  def make(implicit lockV1: LockOrderParser[V1]): LockOrderParser[Version] =
    List[LockOrderParser[Version]](lockV1).reduceLeft(_ or _)
}
