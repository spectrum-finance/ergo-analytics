package fi.spectrum.api.db.models

import derevo.derive
import fi.spectrum.core.domain.{AssetAmount, TokenId}
import tofu.logging.derivation.loggable

@derive(loggable)
final case class RegisterLmRedeem(bundleKeyId: TokenId, expectedLq: AssetAmount, info: TxInfo)
