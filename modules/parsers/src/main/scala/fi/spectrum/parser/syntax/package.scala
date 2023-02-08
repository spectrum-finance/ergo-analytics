package fi.spectrum.parser

import sigmastate.Values.{ByteArrayConstant, Constant, SigmaPropConstant}
import sigmastate.basics.DLogProtocol
import sigmastate.basics.DLogProtocol.ProveDlogProp
import sigmastate.{SBoolean, SLong, SType, Values, SInt}

package object syntax {

  implicit final class ConstantsOps(private val constants: IndexedSeq[Constant[SType]]) extends AnyVal {

    def parseLong(idx: Int): Option[Long] =
      constants.lift(idx).collect { case Values.ConstantNode(value, SLong) =>
        value.asInstanceOf[Long]
      }

    def parseInt(idx: Int): Option[Int] =
      constants.lift(idx).collect { case Values.ConstantNode(value, SInt) =>
        value.asInstanceOf[Int]
      }

    def parseBoolean(idx: Int): Option[Boolean] =
      constants.lift(idx).collect { case Values.ConstantNode(value, SBoolean) =>
        value.asInstanceOf[Boolean]
      }

    def parseBytea(idx: Int): Option[Array[Byte]] =
      constants.lift(idx).collect { case ByteArrayConstant(coll) => coll.toArray }

    def parsePk(idx: Int): Option[DLogProtocol.ProveDlog] =
      constants.lift(idx).collect { case SigmaPropConstant(ProveDlogProp(v)) => v }
  }
}
