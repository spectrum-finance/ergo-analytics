package fi.spectrum.api.classes

import org.ergoplatform.ErgoAddressEncoder
import simulacrum.typeclass

@typeclass
trait ToAPI[A, B, C] {
  def toAPI(a: A, now: Long)(implicit e: ErgoAddressEncoder): Option[B]

  def toAPI(a: A, c: C, now: Long)(implicit e: ErgoAddressEncoder): Option[B]
}

object ToAPI {

  implicit final class ToAPIOps[A](val a: A) extends AnyVal {
    def toApi[B](now: Long)(implicit toAPI: ToAPI[A, B, Any], e: ErgoAddressEncoder): Option[B] = toAPI.toAPI(a, now)

    def toApi[B, C](c: C, now: Long)(implicit toAPI: ToAPI[A, B, C], e: ErgoAddressEncoder): Option[B] =
      toAPI.toAPI(a, c, now)
  }
}
