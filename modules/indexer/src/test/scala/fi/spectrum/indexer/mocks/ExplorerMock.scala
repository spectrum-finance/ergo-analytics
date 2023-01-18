package fi.spectrum.indexer.mocks

import cats.Applicative
import fi.spectrum.core.domain
import fi.spectrum.indexer.models.TokenInfo
import fi.spectrum.indexer.services.Explorer
import cats.syntax.applicative._

object ExplorerMock {

  def make[F[_]: Applicative]: Explorer[F] = new Explorer[F] {

    def getTokenInfo(tokenId: domain.TokenId): F[Option[TokenInfo]] =
      Option(
        TokenInfo(
          tokenId,
          Some(tokenId.value.unwrapped),
          Some(6)
        )
      ).pure
  }
}
