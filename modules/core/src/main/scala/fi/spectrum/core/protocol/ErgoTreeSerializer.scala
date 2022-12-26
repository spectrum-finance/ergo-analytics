package fi.spectrum.core.protocol

import fi.spectrum.core.domain.SErgoTree
import sigmastate.Values
import sigmastate.serialization.{ErgoTreeSerializer => TreeSerializer}

trait ErgoTreeSerializer {

  def serialize(tree: sigmastate.Values.ErgoTree): SErgoTree

  def deserialize(raw: SErgoTree): sigmastate.Values.ErgoTree
}

object ErgoTreeSerializer {

  object default extends ErgoTreeSerializer {

    def serialize(tree: Values.ErgoTree): SErgoTree =
      SErgoTree.fromBytes(TreeSerializer.DefaultSerializer.serializeErgoTree(tree))

    def deserialize(raw: SErgoTree): Values.ErgoTree =
      TreeSerializer.DefaultSerializer.deserializeErgoTree(raw.toBytea)
  }
}
