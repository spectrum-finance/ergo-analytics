package fi.spectrum.core

import fi.spectrum.core.domain.PubKey
import sigmastate.Values.{ErgoTree, SigmaPropConstant}
import sigmastate.basics.DLogProtocol.ProveDlog
import sigmastate.serialization.{GroupElementSerializer, SigmaSerializer}

package object syntax {

  implicit final class PubKeyOps(private val pk: PubKey) extends AnyVal {

    def toErgoTree: ErgoTree = {
      val r = SigmaSerializer.startReader(pk.toBytes)
      val p = GroupElementSerializer.parse(r)
      ErgoTree(ErgoTree.DefaultHeader, ErgoTree.EmptyConstants, SigmaPropConstant(ProveDlog(p)))
    }
  }

}
