package fi.spectrum.parser.lm.compound.v1

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fi.spectrum.parser.lm.compound.v1.Compound.expectedCompound
import org.ergoplatform.ErgoAddressEncoder
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class CompoundSpec extends AnyPropSpec with Matchers with CatsPlatform {
  implicit def addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)

  val parser = CompoundParserV1.v1Compound

  property("Parse lm compound v1 contract") {
    val box      = Compound.compoundNotLastEpochOutput
    val compound = parser.compound(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    compound shouldEqual Compound.compoundNotLastEpoch

    val box2      = Compound.compoundLastEpochOutput
    val compound2 = parser.compound(box2, ErgoTreeSerializer.default.deserialize(box2.ergoTree)).get
    compound2 shouldEqual Compound.compoundLastEpoch
  }

  property("Parse lm compound contract") {
    val box      = Compound.compoundNotLastEpochOutput
    val compound = parser.compound(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    compound shouldEqual Compound.compoundNotLastEpoch

    ProcessedOrderParser
      .make[IO]
      .registered(Compound.compoundTx, 0)
      .unsafeRunSync()
      .head
      .order shouldEqual expectedCompound
  }
}
