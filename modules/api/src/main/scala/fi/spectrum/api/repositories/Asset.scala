package fi.spectrum.api.repositories

import fi.spectrum.api.db.models.amm.AssetInfo
import fi.spectrum.core.domain.TokenId

trait Asset[F[_]] {
  def assetById(id: TokenId): F[Option[AssetInfo]]
}
