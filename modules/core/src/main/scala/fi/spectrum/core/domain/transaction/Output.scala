package fi.spectrum.core.domain.transaction

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.{sigma, BoxId, SErgoTree, TxId}
import fi.spectrum.core.protocol.ErgoTreeSerializer.default._
import org.ergoplatform.ErgoBox.NonMandatoryRegisterId
import org.ergoplatform.{ErgoBox, ErgoBoxCandidate}
import scorex.util.ModifierId
import sigmastate.SType
import sigmastate.Values.EvaluatedValue
import sigmastate.eval.Colls
import tofu.logging.derivation.{loggable, show}

@derive(encoder, decoder, loggable, show)
final case class Output(
  boxId: BoxId,
  transactionId: TxId,
  value: Long,
  index: Int,
  creationHeight: Int,
  ergoTree: SErgoTree,
  assets: List[BoxAsset],
  additionalRegisters: Map[RegisterId, SConstant]
)

object Output {

  def fromErgoBox(box: ErgoBox, txId: ModifierId): Output =
    Output(
      BoxId.fromErgo(box.id),
      TxId(ModifierId !@@ txId),
      box.value,
      box.index,
      box.creationHeight,
      serialize(box.ergoTree),
      box.additionalTokens.toArray.toList.map { case (id, amount) => BoxAsset.fromErgo(id, amount) },
      parseRegisters(box.additionalRegisters)
    )

  def parseRegisters(
    additionalRegisters: Map[NonMandatoryRegisterId, _ <: EvaluatedValue[_ <: SType]]
  ): Map[RegisterId, SConstant] =
    additionalRegisters.flatMap { case (k, v) =>
      for {
        register  <- RegisterId.withNameOption(s"R${k.number}")
        sConstant <- sigma.renderEvaluatedValue(v).map { case (t, eval) => SConstant.fromRenderValue(t, eval) }
      } yield (register, sConstant)
    }
}
