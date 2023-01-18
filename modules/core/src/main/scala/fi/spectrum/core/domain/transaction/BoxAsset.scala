package fi.spectrum.core.domain.transaction

import derevo.circe.{decoder, encoder}
import derevo.derive
import fi.spectrum.core.domain.TokenId
import org.ergoplatform.ErgoBox
import scorex.util.encode.Base16
import supertagged.PostfixSugar
import tofu.logging.derivation.{loggable, show}

@derive(encoder, decoder, loggable, show)
final case class BoxAsset(tokenId: TokenId, amount: Long)

object BoxAsset {

  def fromErgo(id: ErgoBox.TokenId, amount: Long): BoxAsset =
    BoxAsset(TokenId.unsafeFromString(Base16.encode(id)), amount)
}
