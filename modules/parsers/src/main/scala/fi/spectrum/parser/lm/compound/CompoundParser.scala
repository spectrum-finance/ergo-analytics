package fi.spectrum.parser.lm.compound

import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order.Compound
import fi.spectrum.core.domain.transaction.Output
import sigmastate.Values

trait CompoundParser[+V <: Version] { self =>
  def compound(box: Output, tree: Values.ErgoTree): Option[Compound]

  def or(that: => CompoundParser[Version]): CompoundParser[Version] =
    (box: Output, tree: Values.ErgoTree) => self.compound(box, tree) orElse that.compound(box, tree)
}

object CompoundParser {

  implicit def make(implicit
    v1: CompoundParser[V1]
  ): CompoundParser[Version] =
    List[CompoundParser[Version]](
      v1
    ).reduceLeft(_ or _)
}
