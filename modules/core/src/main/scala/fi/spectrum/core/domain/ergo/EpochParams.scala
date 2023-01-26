package fi.spectrum.core.domain.ergo

import derevo.circe.decoder
import derevo.derive
import tofu.logging.derivation.loggable

@derive(decoder, loggable)
final case class EpochParams(
  height: Int,
  storageFeeFactor: Int,
  minValuePerByte: Int,
  maxBlockSize: Int,
  maxBlockCost: Int,
  blockVersion: Byte,
  tokenAccessCost: Int,
  inputCost: Int,
  dataInputCost: Int,
  outputCost: Int
) {
  val safeMinValue: Long = (minValuePerByte * 1500).toLong
}