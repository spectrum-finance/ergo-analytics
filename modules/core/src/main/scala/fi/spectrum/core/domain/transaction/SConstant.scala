package fi.spectrum.core.domain.transaction

import fi.spectrum.core.domain.PubKey
import fi.spectrum.core.domain.TypeConstraints.HexString

sealed trait SConstant

object SConstant {

  final case class IntConstant(value: Int) extends SConstant

  final case class LongConstant(value: Long) extends SConstant

  final case class ByteaConstant(value: HexString) extends SConstant

  final case class SigmaPropConstant(value: PubKey) extends SConstant

  final case class UnresolvedConstant(raw: String) extends SConstant
}
