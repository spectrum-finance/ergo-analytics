package fi.spectrum.core.domain.transaction

import derevo.cats.eqv
import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TokenId
import tofu.logging.derivation.{loggable, show}

@derive(encoder, decoder, loggable, show, eqv)
final case class BoxAsset(tokenId: TokenId, amount: Long)

object BoxAsset