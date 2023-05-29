package fi.spectrum.api.services

import cats.Applicative
import fi.spectrum.api.models.MempoolData
import fi.spectrum.api.services.models.explorer.{ExplorerOutput, ExplorerTx}
import fi.spectrum.core.domain
import fi.spectrum.core.domain.{Address, BoxId, TokenId, TxId}
import tofu.syntax.foption._
import tofu.syntax.monadic._

object NetworkMock {
  def make[F[_]: Applicative]: Network[F] = new Network[F] {
    def getErgPriceCMC: F[Option[BigDecimal]] = BigDecimal(1.12).someF

    def getVerifiedTokenList: F[List[domain.TokenId]] = List.empty[TokenId].pure

    def getCurrentNetworkHeight: F[Int] = 1.pure

    def getMempoolData(addresses: List[domain.Address]): F[List[MempoolData]] = List.empty[MempoolData].pure

    def getUnspentBoxes(address: Address): F[List[ExplorerOutput]] = ???

    def getBoxById(id: BoxId): F[Option[ExplorerOutput]] = ???

    def getTransactionById(id: TxId): F[Option[ExplorerTx]] = ???
  }
}
