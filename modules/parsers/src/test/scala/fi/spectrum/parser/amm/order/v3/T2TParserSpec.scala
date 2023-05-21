package fi.spectrum.parser.amm.order.v3

import cats.syntax.eq._
import fi.spectrum.core.domain.{ErgoTreeTemplate, TokenId}
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.CatsPlatform
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType
import org.ergoplatform.ErgoAddressEncoder
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class T2TParserSpec extends AnyPropSpec with Matchers with CatsPlatform {
  implicit val spf: TokenId = TokenId.unsafeFromString("9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d")
  implicit val e: ErgoAddressEncoder = new ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)

  val parser: AmmOrderParser[Version.V3, AmmType.T2T] = T2TAmmOrderParser.t2tV3

  property("e") {

    val out =
      """
        |{
        |    "boxId": "4774de4f9dab4d60fcf654c09faa0decfd08dd94ac4c58b1a04ac1e7de3abdaa",
        |    "transactionId": "e3d6c3d3775776b4660d29343db62fea55faafec32f53122dbfd08137071f9be",
        |    "blockId": "b7f55bee53d2930ed703a6805069a123fb9dc391cf61e27443c3970ce2419d2d",
        |    "value": 400000,
        |    "index": 0,
        |    "globalIndex": 26939118,
        |    "creationHeight": 948753,
        |    "settlementHeight": 948755,
        |    "ergoTree": "19e0041a040008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec040404080402040205feffffffffffffffff010404050204060536040004000e20080e453271ff4d2f85f97569b09755e67537bf2a1bc1cd09411b459cf901b9020e240008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec0404040204040402010101000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d801d601b2a4730000eb027301d195ed92b1a4730293b1db630872017303d80cd602db63087201d603b2a5730400d604b27202730500d6057e9973068c72040206d606b27202730700d6077e8c72060206d6089d9c7e73080672057207d609b27202730900d60a7e8c72090206d60b9d9c7e730a067205720ad60cdb63087203d60db2720c730b00edededededed938cb27202730c0001730d93c27203730e92c17203c1a795ed8f7208720b93b1720c730fd801d60eb2720c731000ed938c720e018c720901927e8c720e02069d9c99720b7208720a720595ed917208720b93b1720c7311d801d60eb2720c731200ed938c720e018c720601927e8c720e02069d9c997208720b7207720595937208720b73137314938c720d018c720401927e8c720d0206a17208720b90b0ada5d9010e639593c2720e7315c1720e73167317d9010e599a8c720e018c720e0273187319",
        |    "address": "2uQihBXBJDVbjpuuS1HEue3WtBhs9VepJMcevcLgHVxyprbLjnudeeQEKCQkAgX22t9sNJuH4UTvdDJe9jbbxR9sWoYPSEsJbMVXzo5RJFFxWRQePbiutcNHE1PZtGQ87NWhKKSA4TevwjeL8neoQDzaHL4tS8GdrVo5pdJsDfm5a4faip5x8HpNAjYFCgE3aRVjRbZA7giPKquZANr9CUqsXcZuHqw4EAjjs8Ht9DDWt3ovHTLzRFYAuRZhfepUNW5roxTrHBwWAFSAt8w5Mhs2cxue3jma9pc7t3964y4UiTPsPNAdq2nuwhDyqiCB1c5UWUJ1BW7qdnsmJCTNC5vvptLjzKQLHWSLwEP69GYpFv2mzon2M4iBSWLqH4ndX6wA4gBdbrqy9dB2bKgBG68whkw94QGVBgT7uQCukh6jSZ6CKNzbPVi7oHDvbKf4GreMQNKgGCkjb33qiQJSfKnCCeMSwonx5THBLFGBp3xHwq6mHgDnV4pZJxJndCQUzY25gHvq5AfJSxGKHWjrZzUSMVr1LP8sqQEdDs8osGbHxVLYhrXbY633CKXQM4PGvttk7e675koyznpQoZ51ffDPWUQv6yesyffZ9U3sffr2C9JJWi761vkeksutGSJX7fE8DoZq3H2mNam7m2xHfrNv318kZGGwGMSxbwFHzf9QCgs83KhmGst1fvhzF7kLiP9MveKSnyhakf3kvZun4a4Fd1uxU3djyiDWartapzzNPeZgVf4R5jyF6WbtywZ1uykaXbpTVYmuFsBYew2rpekGYkNknfKHBuQPDfJ541v2EoNe3sw6cteDzVyasy1v21Mf4BTgq",
        |    "assets": [
        |        {
        |            "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
        |            "index": 0,
        |            "amount": 1,
        |            "name": "SigUSD",
        |            "decimals": 2,
        |            "type": "EIP-004"
        |        },
        |        {
        |            "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
        |            "index": 1,
        |            "amount": 27,
        |            "name": "SigRSV",
        |            "decimals": 0,
        |            "type": "EIP-004"
        |        },
        |        {
        |            "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
        |            "index": 2,
        |            "amount": 134568,
        |            "name": "SPF",
        |            "decimals": 6,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "394e6eca34ba5427e4f70cdf9432ef3b557c91c1eca386059511cb9289d90b3e",
        |    "mainChain": true
        |}
        |""".stripMargin

    val box = io.circe.parser.decode[Output](out).toOption.get

    println( ErgoTreeTemplate.fromBytes(ErgoTreeSerializer.default.deserialize(box.ergoTree).template))

    val o = parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    println(o)
  }

  property("Parse t2t swap v3 no spf contract") {
    val box = T2T.swap.outputNoSpf
    val swapResult: Order.Swap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult shouldEqual (T2T.swap.swapNoSpf: Order.Swap))
  }

  property("Parse t2t swap v3 spf contract") {
    val box = T2T.swap.outputSpf
    val swapResult: Order.Swap =
      parser.swap(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    (swapResult shouldEqual (T2T.swap.swapSpf: Order.Swap))
  }

  property("Parse t2t deposit v3 y is spf contract") {
    val box = T2T.deposit.outputSpf
    val depositResult: Order.Deposit =
      parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Deposit = T2T.deposit.depositSpf
    (depositResult shouldEqual expected)
  }

  property("Parse t2t deposit v3 x is spf contract") {
    val box = T2T.deposit.outputSpfIsX
    val depositResult: Order.Deposit =
      parser.deposit(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Deposit = T2T.deposit.depositSpfIsX
    (depositResult shouldEqual expected)
  }

  property("Parse t2t redeem v3 contract") {
    val box = T2T.redeem.output
    val redeemResult: Order.Redeem =
      parser.redeem(box, ErgoTreeSerializer.default.deserialize(box.ergoTree)).get
    val expected: Order.Redeem = T2T.redeem.order
    (redeemResult shouldEqual expected)
  }
}