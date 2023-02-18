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
  implicit val spf: TokenId = TokenId.unsafeFromString("")
  implicit val e: ErgoAddressEncoder = new ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)

  val parser: AmmOrderParser[Version.V3, AmmType.T2T] = T2TAmmOrderParser.t2tV3

  property("e") {

    val out =
      """
        |{
        |    "boxId": "cac5c0cedda0549f40393995f2b9fe7e8abe4f6819c15957c25d22eeeb802a87",
        |    "transactionId": "4da068579b16e274314cb6ee87d114108d0e440b9174d5c0e0841adb571ed911",
        |    "blockId": "f5aa09083e42c5e505d378677dbfc19383950a4aed29071f6db93fcf23a74a38",
        |    "value": 310000,
        |    "index": 0,
        |    "globalIndex": 26668320,
        |    "creationHeight": 942941,
        |    "settlementHeight": 942945,
        |    "ergoTree": "19c2041704000580897a08cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec040404060402040205feffffffffffffffff0104040550040004000e205703a5b955c4902f165215f5ce1426816b4c6dca5300cfae35c8a356492871540e240008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec0404040205c0b80201000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d601b2a4730000d6027301eb027302d195ed92b1a4730393b1db630872017304d80bd603db63087201d604b2a5730500d605b27203730600d6067e9973078c72050206d6077ec1720106d6089d9c7e72020672067207d609b27203730800d60a7e8c72090206d60b9d9c7e7309067206720ad60cdb63087204d60db2720c730a00ededededed938cb27203730b0001730c93c27204730d95ed8f7208720b93b1720c730ed801d60eb2720c730f00eded92c1720499c1a77310938c720e018c720901927e8c720e02069d9c99720b7208720a720695927208720b927ec1720406997ec1a706997e7202069d9c997208720b720772067311938c720d018c720501927e8c720d0206a17208720b90b0ada5d9010e639593c2720e7312c1720e73137314d9010e599a8c720e018c720e0273157316",
        |    "address": "39pe55GaVvHc6EHaNNnUwrdy68Mwtm9Sc7hgSGqRZ5F9XNCEgQGfRWVatQNjzrEwWDw4Kdc1nsvCRL33Fqa4yaNnL83UcGLhoposxKEt5G3WTSUMssfXdUP8U9xARA6qW1ryoRwdmfcPCqfY6S2aBdpotrweek8W4TMkDKe9hA6m5w3W7x1cCeksAnVuTYGW47FuYXJorKBWTuZtzr8ZomsfNG8jv9qYvbWFrwCETjx14VyDAZwz4b4seU19vkkpg3xsWLH43iQbGq8Auzrdmjpc8Qiy7gjSRLYwthW4bvqt5jxyx931nveX1kc8s6xJc9Gj6KU4tyxZ6bT2LzRkWAsY9FXjog7qazWxXniAJ5pVATsit52s3nhTX7jVSHLFjQMAZu1AYqzqJ7uZcKiBmBVaY9niByh6ftKhbmLT8BUdKvx7gzbk5GezhvemaRNwWZ8HqU2rv2RmkhJWzVFewfVBeNnewLMJKDdDHDfWXZM4A7pL9nbLiEdTDPefMbv4UMhuvTLVUE8NRKnEQAGdwVuqFUKK6Mn1MHuXgr1gj6vn9n2aKmY9kAepW7ksEXrJEtmRQsMJYZ2BzUdQZ6Feug1ABgyBk3amfEoA5npp9wHUkPAG39tSvEkyKFB7XdkzCnqZa4QP87e7uFRmwNZKL7kX7r88k5opcLDpPmFBc43BMUohAKJj1gVJPvfhh2jh9enrgYn576gBYqgNKceefhumA1EUu1SQrHa84Kzs8wvf12isAnv3LWZBDJsa6VaRuUiwFUiMHU4inLpYoCcMtYhoTxruWoVf",
        |    "assets": [
        |        {
        |            "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
        |            "index": 0,
        |            "amount": 1000000,
        |            "name": "NETA",
        |            "decimals": 6,
        |            "type": "EIP-004"
        |        },
        |        {
        |            "tokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
        |            "index": 1,
        |            "amount": 40,
        |            "name": "ergopad",
        |            "decimals": 2,
        |            "type": "EIP-004"
        |        },
        |        {
        |            "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
        |            "index": 2,
        |            "amount": 15,
        |            "name": "SigRSV",
        |            "decimals": 0,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": null,
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
