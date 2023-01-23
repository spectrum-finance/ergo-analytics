package fi.spectrum.core.common

import tofu.Errors

object errors {

  final case class ResponseError(msg: String) extends Exception(msg)

  object ResponseError extends Errors.Companion[ResponseError]


  final case class RefinementFailed(details: String) extends Exception(s"Refinement failed: $details")
}
