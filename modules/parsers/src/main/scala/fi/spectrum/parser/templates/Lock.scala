package fi.spectrum.parser.templates

import fi.spectrum.core.domain.ErgoTreeTemplate

object Lock {

  def lockV1: ErgoTreeTemplate =
    ErgoTreeTemplate.unsafeFromString(
      "d802d601b2a5730000d602e4c6a70404ea02e4c6a70508d19593c27201c2a7d802d603b2db63087201730100d604b2db6308a7730200" +
      "eded92e4c6720104047202938c7203018c720401928c7203028c7204028f7202a3"
    )
}
