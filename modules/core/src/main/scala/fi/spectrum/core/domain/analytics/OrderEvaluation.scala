package fi.spectrum.core.domain.analytics

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.AssetAmount

@derive(encoder, decoder)
sealed trait OrderEvaluation

object OrderEvaluation {

  @derive(encoder, decoder)
  final case class SwapEvaluation(output: AssetAmount) extends OrderEvaluation

  @derive(encoder, decoder)
  final case class DepositEvaluation(outputLP: AssetAmount) extends OrderEvaluation

  @derive(encoder, decoder)
  final case class RedeemEvaluation(outputX: AssetAmount, outputY: AssetAmount) extends OrderEvaluation
}
