package fi.spectrum.api.classes

import org.ergoplatform.ErgoAddressEncoder
import simulacrum.typeclass

@typeclass
trait ToAPI[A, B, C] {
  def toAPI(a: A)(implicit e: ErgoAddressEncoder): Option[B]

  def toAPI(a: A, c: C)(implicit e: ErgoAddressEncoder): Option[B]
}

object ToAPI {

  implicit final class ToAPIOps[A](val a: A) extends AnyVal {
    def toApi[B](implicit toAPI: ToAPI[A, B, Any], e: ErgoAddressEncoder): Option[B] = toAPI.toAPI(a)

    def toApi[B, C](c: C)(implicit toAPI: ToAPI[A, B, C], e: ErgoAddressEncoder): Option[B] = toAPI.toAPI(a, c)
  }
}
