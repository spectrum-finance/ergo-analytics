package fi.spectrum.parser.lm.compound.v1

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import org.ergoplatform.ErgoAddressEncoder
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class CompoundSpec extends AnyPropSpec with Matchers with CatsPlatform {
  implicit def addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)

  val parser = CompoundParserV1.v1Compound

  property("Parse lm compound v1 contract") {
    val box     = Bundle.output
    val deposit = parser.compound(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    deposit shouldEqual Bundle.bundle
  }

  property("Parse lm compound contract") {
    val box = Bundle.registerOutputCompound
    val compound = parser.compound(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    compound shouldEqual Bundle.bundle

    ProcessedOrderParser.make[IO].registered(Bundle.compoundTx, 0).unsafeRunSync().get.order shouldEqual Bundle.bundle
  }
}
