package fi.spectrum.parser

import cats.implicits.catsSyntaxEq
import fi.spectrum.core.domain.TokenId
import fi.spectrum.core.domain.order.Order
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.amm.order.anyAmmOrder
import fi.spectrum.parser.lock.v1.Lock
import org.ergoplatform.ErgoAddressEncoder
import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec

class OrderParserSpec extends AnyPropSpec with Matchers with CatsPlatform {

  implicit val e: ErgoAddressEncoder = new ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)
  implicit val spf: TokenId          = TokenId.unsafeFromString("")
  val parser: OrderParser            = OrderParser.make

  val out =
    """
      |{
      |    "boxId": "ff05c9f4b4ed2d3ed531771572c0c509a1795296be942158cd502ee85f9a8aeb",
      |    "transactionId": "dadcae4d12d3291e4935cec9b4c0800c8bf8a603d8574cf2316e2ece0452a81d",
      |    "blockId": "ce0d1031918995de14f026857a68782129420d7a152ce4028c967ffd2e83fc1b",
      |    "value": 310000,
      |    "index": 0,
      |    "globalIndex": 26541766,
      |    "creationHeight": 940086,
      |    "settlementHeight": 940090,
      |    "ergoTree": "199c041a0400052c04c60f08cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec0404040604020524058080a0f6f4acdbe01b05e8d2e4e9cf5804000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f0e240008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec05cad0b20906010004000e20003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d001010502040404d00f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d601b2a4730000d6029c73017e730205eb027303d195ed92b1a4730493b1db630872017305d804d603db63087201d604b2a5730600d60599c17204c1a7d606997e7307069d9c7e7205067e7308067e730906ededededed938cb27203730a0001730b93c27204730c927205730d95917206730ed801d607b2db63087204730f00ed938c7207017310927e8c7207020672067311909c7ec17201067e7202069c7e9a72057312069a9c7e8cb2720373130002067e7314067e72020690b0ada5d90107639593c272077315c1720773167317d90107599a8c7207018c72070273187319",
      |    "address": "4J8wkYtoeJ2gHH5jPzihSRcHcB5tgGFAEnL6kqJFeiAg3AcGcCbrqacccrU1rzJPfZLUyy7eBnbKUadAv7qnVboyYaJiaCc5FBTwMM7oH3RHDdC7qzsqDusXhoyWxFEM87G5bSS3Q5Y7F7bDfEQNcPevvH3CyoA6ZXK3Ba5uNzDifq66A4iKdAQ9zJKWBVR8d7DBbMVJmvkjk2LPAmNwbtNmBAuxoJY8EKiYidv8ETggU8tYYXeG5gkUEpeQ7bncy9UzTKm2ev9CMDN5FGtVNunZqpmhcnL1hTG8v2YPrvNKeevPg2uwiEYZ56cKSboL4BeVM9CEkQqxyhkYzYznSXshZHkXGXTwNNAH4on31BDEBn9xDTUHBhYUHRfb3yrW9SQTY7ncnhC89KTNQ6pLv5KSJHZYfBwA2sJnKrCHEKWHKNeNxPvnPSFcSkAJTJFb4o5yyhb7tDDdgmRG5p9q9sdqtjcCL7r1cEhreoQ8L166dnZ4xDUSRf8XRMWT9UUmuVdu9Kyds6ZanxoEeM4ncRmWf18TkkTZeyo54ydRMiUHrtVivb81Pnxdu7VyAZQQUXL6eBnWbd9yzrNFJBGNYLutik4SJ6rf3BfYq2FGbWhRoQ4Pw2GzFLda6TMF3Lh9KpQvsRUTwsr26Nj7gpM6ewQEY2uZCqufYMiqDoYhiBn2bp2KZbwmnZ7gx8JtMezy4PzKueNc4ZYWa6gFNeyPAALo9iDLLQQXpiPRYShF39kb",
      |    "assets": [
      |        {
      |            "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |            "index": 0,
      |            "amount": 40,
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

  property("Parse any order via OrderParser") {
    val a = parser.parse(io.circe.parser.decode[Output](out).toOption.get)
    println(a)
    println(a.get.redeemer)
    val anyOrder: List[(Output, Order)] = List(Lock.output -> Lock.lock) ::: anyAmmOrder
    anyOrder.foreach { case (output, expectedOrder) =>
      (parser.parse(output).get shouldEqual expectedOrder)
    }
  }
}
