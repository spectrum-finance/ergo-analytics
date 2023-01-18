package fi.spectrum.indexer.services

import cats.data.NonEmptyList
import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Ref}
import doobie.ConnectionIO
import fi.spectrum.core.domain.TokenId
import fi.spectrum.indexer.db.{Indexer, PGContainer}
import fi.spectrum.indexer.db.repositories.AssetsRepo
import fi.spectrum.indexer.mocks.{ExplorerMock, MetricsMock}
import fi.spectrum.indexer.models.TokenInfo
import org.scalatest.Assertion
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import tofu.syntax.doobie.txr._

class AssetsSpec extends AnyFlatSpec with Matchers with PGContainer with Indexer {

  "asset" should "work correct" in {
    implicit val metrics   = MetricsMock.make[IO]
    implicit val assetRepo = AssetsRepo.make[ConnectionIO]
    implicit val explorer  = ExplorerMock.make[IO]
    val ref                = Ref.of[IO, List[TokenId]](List.empty).unsafeRunSync()
    val assets             = Assets.make[IO, ConnectionIO](ref).unsafeRunSync()

    def run: IO[Assertion] =
      for {
        _ <- assets.process(
               NonEmptyList.of(
                 TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
                 TokenId.unsafeFromString("000db3f3b71ad1cfc1327ec9e132120096e24cf720f5ef6ecae897dfe72b1aa9"),
                 TokenId.unsafeFromString("003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0"),
                 TokenId.unsafeFromString("007fd64d1ee54d78dd269c8930a38286caa28d3f29d27cadcb796418ab15c283")
               )
             )
        state1   <- ref.get
        dbState1 <- assetRepo.getAll.trans
        _ <- assets.process(
               NonEmptyList.of(
                 TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
                 TokenId.unsafeFromString("000db3f3b71ad1cfc1327ec9e132120096e24cf720f5ef6ecae897dfe72b1aa9"),
                 TokenId.unsafeFromString("0d9ef46408f11aed2a7f840d3928baefaf8153032f42296cbe9d640845d4082c"),
                 TokenId.unsafeFromString("0cd8c9f416e5b1ca9f986a7f10a84191dfb85941619e49e53c0dc30ebf83324b")
               )
             )
        state2   <- ref.get
        dbState2 <- assetRepo.getAll.trans
        _ <- assets.process(
               NonEmptyList.of(
                 TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
                 TokenId.unsafeFromString("0cd8c9f416e5b1ca9f986a7f10a84191dfb85941619e49e53c0dc30ebf83324b")
               )
             )
        state3   <- ref.get
        dbState3 <- assetRepo.getAll.trans
      } yield {
        state1.sortBy(_.value.unwrapped) shouldEqual List(
          TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
          TokenId.unsafeFromString("000db3f3b71ad1cfc1327ec9e132120096e24cf720f5ef6ecae897dfe72b1aa9"),
          TokenId.unsafeFromString("003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0"),
          TokenId.unsafeFromString("007fd64d1ee54d78dd269c8930a38286caa28d3f29d27cadcb796418ab15c283")
        ).sortBy(_.value.unwrapped)
        dbState1.sortBy(_.value.unwrapped) shouldEqual List(
          TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
          TokenId.unsafeFromString("000db3f3b71ad1cfc1327ec9e132120096e24cf720f5ef6ecae897dfe72b1aa9"),
          TokenId.unsafeFromString("003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0"),
          TokenId.unsafeFromString("007fd64d1ee54d78dd269c8930a38286caa28d3f29d27cadcb796418ab15c283")
        ).sortBy(_.value.unwrapped)

        state2.sortBy(_.value.unwrapped) shouldEqual List(
          TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
          TokenId.unsafeFromString("000db3f3b71ad1cfc1327ec9e132120096e24cf720f5ef6ecae897dfe72b1aa9"),
          TokenId.unsafeFromString("003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0"),
          TokenId.unsafeFromString("007fd64d1ee54d78dd269c8930a38286caa28d3f29d27cadcb796418ab15c283"),
          TokenId.unsafeFromString("0d9ef46408f11aed2a7f840d3928baefaf8153032f42296cbe9d640845d4082c"),
          TokenId.unsafeFromString("0cd8c9f416e5b1ca9f986a7f10a84191dfb85941619e49e53c0dc30ebf83324b")
        ).sortBy(_.value.unwrapped)
        dbState2.sortBy(_.value.unwrapped) shouldEqual List(
          TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
          TokenId.unsafeFromString("000db3f3b71ad1cfc1327ec9e132120096e24cf720f5ef6ecae897dfe72b1aa9"),
          TokenId.unsafeFromString("003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0"),
          TokenId.unsafeFromString("007fd64d1ee54d78dd269c8930a38286caa28d3f29d27cadcb796418ab15c283"),
          TokenId.unsafeFromString("0d9ef46408f11aed2a7f840d3928baefaf8153032f42296cbe9d640845d4082c"),
          TokenId.unsafeFromString("0cd8c9f416e5b1ca9f986a7f10a84191dfb85941619e49e53c0dc30ebf83324b")
        ).sortBy(_.value.unwrapped)

        state3.sortBy(_.value.unwrapped) shouldEqual List(
          TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
          TokenId.unsafeFromString("000db3f3b71ad1cfc1327ec9e132120096e24cf720f5ef6ecae897dfe72b1aa9"),
          TokenId.unsafeFromString("003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0"),
          TokenId.unsafeFromString("007fd64d1ee54d78dd269c8930a38286caa28d3f29d27cadcb796418ab15c283"),
          TokenId.unsafeFromString("0d9ef46408f11aed2a7f840d3928baefaf8153032f42296cbe9d640845d4082c"),
          TokenId.unsafeFromString("0cd8c9f416e5b1ca9f986a7f10a84191dfb85941619e49e53c0dc30ebf83324b")
        ).sortBy(_.value.unwrapped)
        dbState3.sortBy(_.value.unwrapped) shouldEqual List(
          TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
          TokenId.unsafeFromString("000db3f3b71ad1cfc1327ec9e132120096e24cf720f5ef6ecae897dfe72b1aa9"),
          TokenId.unsafeFromString("003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0"),
          TokenId.unsafeFromString("007fd64d1ee54d78dd269c8930a38286caa28d3f29d27cadcb796418ab15c283"),
          TokenId.unsafeFromString("0d9ef46408f11aed2a7f840d3928baefaf8153032f42296cbe9d640845d4082c"),
          TokenId.unsafeFromString("0cd8c9f416e5b1ca9f986a7f10a84191dfb85941619e49e53c0dc30ebf83324b")
        ).sortBy(_.value.unwrapped)

      }

    assetRepo.getAll.trans.unsafeRunSync().contains(TokenInfo.ErgoTokenInfo.id) shouldEqual true

    run.unsafeRunSync()
  }
}
