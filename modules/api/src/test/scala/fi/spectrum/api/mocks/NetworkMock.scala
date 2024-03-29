package fi.spectrum.api.mocks

import cats.Applicative
import fi.spectrum.api.models.MempoolData
import fi.spectrum.api.services.Network
import fi.spectrum.core.domain
import tofu.syntax.monadic._
import tofu.syntax.foption._

object NetworkMock {

  def make[F[_]: Applicative](orders: List[MempoolData]): Network[F] = new Network[F] {
    def getErgPriceCMC: F[Option[BigDecimal]] = noneF

    def getVerifiedTokenList: F[List[domain.TokenId]] = List.empty[domain.TokenId].pure

    def getCurrentNetworkHeight: F[Int] = 0.pure

    def getMempoolData(addresses: List[domain.Address]): F[List[MempoolData]] = orders.pure
  }

}
