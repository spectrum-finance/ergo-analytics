package fi.spectrum.parser.amm.order.v3

import cats.syntax.eq._
import fi.spectrum.core.domain.{ErgoTreeTemplate, PubKey, TokenId}
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.order.Redeemer.{ErgoTreeRedeemer, PublicKeyRedeemer}
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType
import org.ergoplatform.{ErgoAddressEncoder, P2PKAddress, Pay2SAddress, Pay2SHAddress}
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class N2TParserSpec extends AnyPropSpec with Matchers with CatsPlatform {
  implicit val spf: TokenId          = TokenId.unsafeFromString("")
  implicit val e: ErgoAddressEncoder = new ErgoAddressEncoder((ErgoAddressEncoder.MainnetNetworkPrefix))

  val parser: AmmOrderParser[Version.V3, AmmType.N2T] = N2TAmmOrderParser.n2tV3

  property("e") {

    val out =
      """
        |{
        |    "boxId": "29caadfbee140a978bd6200d807292160eb69b67a605fe597e887d8aae870faf",
        |    "transactionId": "dfdd1038cd12ad5615f0dc915de1f522359e45ab0fb1e54e6ee3f2a30ade7d24",
        |    "blockId": "7103bfc791be82d4b392292d23dc4b9808dd857ff728d019640c86490df41dc2",
        |    "value": 310000,
        |    "index": 0,
        |    "globalIndex": 26672272,
        |    "creationHeight": 943026,
        |    "settlementHeight": 943028,
        |    "ergoTree": "19ca0312040008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec04040406040204000404040005feffffffffffffffff01040204000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec0e240008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d801d601b2a4730000eb027301d195ed92b1a4730293b1db630872017303d806d602db63087201d603b2a5730400d604b2db63087203730500d605b27202730600d6067e8cb2db6308a77307000206d6077e9973088cb272027309000206ededededed938cb27202730a0001730b93c27203730c938c7204018c720501927e99c17203c1a7069d9c72067ec17201067207927e8c720402069d9c72067e8c72050206720790b0ada5d90108639593c27208730dc17208730e730fd90108599a8c7208018c72080273107311",
        |    "address": "4X2w8UZHL14TJyPYMRq5ABA4gUfetNKCQbgStifCP1zk8qmjxapNrQshQJ7ojuJwY3mRwBcxjYEBNNdnxPXkzExRnYi4bJJ86gD7eMEotAh2TnwGZKQ9wC8Gf3Faat6K2x8Lq4xJWZ1AYcJkXPDbEv5JTbtTRcudjfYkybdWMgbsB3Yz8dW3Yquju6L7t1y18qQZBjo4BcYuAsHUGmWpAvMYV31ywJsfuEMuxtMB8qE7PjW8mGqA1oQmxLc3wWC5yd17P7VArXGNoMdESKSs8EXA8wHBag2iR7Uy3wbQkTt3mXzi1KNm7PJgCw2QbGxvhgcwNQGtS8qJV4RkEabqKprfMozv2gECbv3371HrEhNVZHd4enejj3LphZfrTBSyNPWpAoGmxsLNVN7cakeCUApVhdebazfR8TAKXR6922QRojn7JGEuQauhARASTSqYsHTZZj11romGqRH5DY4YMZrcZvKJPB3fubyHwhCicirfYX745k2ZgjfsRCuQPiXrEaEziLRnpkbT8BDHoEGPX8to5kNH5TAhG1C2n24PeXhmUL9s3mrZ5XSvhY1WD6BXHXmHof6SouSnFiTMEWmtJ2FZeoizYVxS3McgPznyfioZCK1EYqZ7Tq3endLC",
        |    "assets": [
        |        {
        |            "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
        |            "index": 0,
        |            "amount": 11439,
        |            "name": null,
        |            "decimals": null,
        |            "type": null
        |        },
        |        {
        |            "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
        |            "index": 1,
        |            "amount": 15,
        |            "name": "SigRSV",
        |            "decimals": 0,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "bcd6c04442b8b5161d0a69c0ac6fe2ed20f812a135984824e6ca83e9b58aade1",
        |    "mainChain": true
        |}
        |""".stripMargin

    val box = io.circe.parser.decode[Output](out).toOption.get

    println(ErgoTreeTemplate.fromBytes(ErgoTreeSerializer.default.deserialize(box.ergoTree).template))

    val o = parser.redeem(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    println(o.redeemer)
    import cats.syntax.either._
    val s =
      "19ca0312040008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec04040406040204000404040005feffffffffffffffff01040204000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec0e240008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d801d601b2a4730000eb027301d195ed92b1a4730293b1db630872017303d806d602db63087201d603b2a5730400d604b2db63087203730500d605b27202730600d6067e8cb2db6308a77307000206d6077e9973088cb272027309000206ededededed938cb27202730a0001730b93c27203730c938c7204018c720501927e99c17203c1a7069d9c72067ec17201067207927e8c720402069d9c72067e8c72050206720790b0ada5d90108639593c27208730dc17208730e730fd90108599a8c7208018c72080273107311"
    val a = Either
      .catchNonFatal(PubKey.fromBytes(o.redeemer.hexString.toBytes))
      .map { key =>
        e.fromProposition(ErgoTreeSerializer.default.deserialize(key.ergoTree)).toOption.map {
          case _: P2PKAddress => (true, PublicKeyRedeemer(key))
          case _              => (false, o.redeemer)
        }
      }

    println(a)

    val b = Either
      .catchNonFatal(PubKey.unsafeFromString(s))
      .flatMap { key =>
        Either
          .catchNonFatal {
            e.fromProposition(ErgoTreeSerializer.default.deserialize(key.ergoTree)).toOption.map {
              case _: P2PKAddress => (true, PublicKeyRedeemer(key))
              case _              => (false, o.redeemer)
            }
          }
      }
      .toOption
      .flatten

    println(b)

  }

  property("Parse n2t swap buy v3 spf contract") {
    val box = N2T.swap.outputBuySpf
    val swapResult: Order.Swap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get

    (swapResult shouldEqual (N2T.swap.swapBuySpf: Order.Swap))
  }

  property("Parse n2t swap buy v3 no spf contract") {
    val box = N2T.swap.outputBuyNoSpf
    val swapResult: Order.Swap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get

    (swapResult shouldEqual (N2T.swap.swapBuyNoSpf: Order.Swap))
  }

  property("Parse n2t swap sell v3 no spf contract") {
    val box = N2T.swap.outputSellNotY
    val swapResult: Order.Swap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult shouldEqual (N2T.swap.swapSellNotY: Order.Swap))
  }

  property("Parse n2t swap sell v3 spf contract") {
    val box = N2T.swap.outputSellSpf
    val swapResult: Order.Swap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult shouldEqual (N2T.swap.swapSellSpf: Order.Swap))
  }

  property("Parse n2t deposit v3 y is spf contract") {
    val box = N2T.deposit.outputSpfY
    val depositResult: Order.Deposit =
      parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Deposit = N2T.deposit.depositSpfY
    (depositResult shouldEqual expected)
  }

  property("Parse n2t deposit v3 y is not spf contract") {
    val box = N2T.deposit.outputSpfNotY
    val depositResult: Order.Deposit =
      parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Deposit = N2T.deposit.depositSpfNotY
    (depositResult shouldEqual expected)
  }

  property("Parse n2t redeem v3 contract") {
    val box = N2T.redeem.output
    val redeemResult: Order.Redeem =
      parser.redeem(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Redeem = N2T.redeem.order
    (redeemResult shouldEqual expected)
  }
}
