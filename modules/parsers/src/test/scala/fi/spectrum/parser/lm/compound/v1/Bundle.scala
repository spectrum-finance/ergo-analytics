package fi.spectrum.parser.lm.compound.v1

import fi.spectrum.core.domain.{AssetAmount, PubKey, TokenId}
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.order.Order.LmBundle.CompoundV1
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.transaction.Output
import io.circe.parser.decode

object Bundle {

  val output = decode[Output](
    s"""
       |{
       |    "boxId": "8b13011ef535ed36797407334023dec3d9b27f141d45d675c1ba4ee02d97982d",
       |    "transactionId": "6c2106bee6af77659b85006cf4cf365b4c3b944928d8fe3ba695cdce14d2bb3e",
       |    "blockId": "6967cee75df994f539b1b16b684efc20c184d4781a341065fe55c50c431e6f12",
       |    "value": 1250000,
       |    "index": 2,
       |    "globalIndex": 26335926,
       |    "creationHeight": 935194,
       |    "settlementHeight": 935204,
       |    "ergoTree": "19a3041f040004000404040404000402060101040005000402040404020400040004020502040405020402040005feffffffffffffffff010408050205000404040004060404040205fcffffffffffffffff010100d80cd601b2a5730000d602db63087201d603e4c6a7050ed604b2a4730100d605db63087204d6068cb2720573020002d607998cb27202730300027206d608e4c6a70408d609db6308a7d60ab27209730400d60bb27205730500d60c7306d1ed938cb27202730700017203959372077308d80cd60db2a5e4e3000400d60eb2a5e4e3010400d60fb2e4c672040410730900d610c672010804d61199720f95e67210e47210e4c672010704d6128cb27209730a0001d613db6308720ed614b27209730b00d6158c721402d6167e721105d6179972159c72168c720a02d618b2db6308720d730c00eded93c2720dd07208edededed93e4c6720e0408720893e4c6720e050e720393c2720ec2a795917211730dd801d619b27213730e00eded9386027212730fb27213731000938c7219018c721401939972158c721902721793860272127311b2721373120093b27213731300720aed938c7218018c720b01927e8c7218020699999d9c99997e8c720b02069d9c7ee4c672040505067e7211067e720f06720c7e7217067e999973148cb27205731500029c9972067316721606720c720c958f7207731793b2db6308b2a473180073190086029593b17209731a8cb27209731b00018cb27209731c0001731d731e",
       |    "address": "YyLFSfjMQw5AJEuchDNPzziDH7CpLX8EbtGbddMpTpDoYYCc5xKJUbDuFnwsJ4qcpARjLmTQdRUTDiftnkb4kuNAaM7enwfxbWvh9SAbpvxAtJzhQApmedrEH9BBrPYzHdWbR354ZXZJkazhvfWMLyijyFeJ7aTDYmhDn8YvF3HeBMJiLByT32wTfNVEWQf4SEp3eN66ppqZs7bJAxrsZo3PDGJgTtZ2qPypMv3x9fcE6zYoLiCzHbAWC8WCfn5wNno9NAwSgWgHKcz3Gx4kXAe5DJnMDZJeiAuckS4e8J5sUfgFSPHTysvkfPw5pxBBNoxrsQ8y2jHSjB3KhSKKsNoZLnWZpbLQ7TurFc34TZJn9FNsRzpmQc9ftcZNa1QxKNoBtFzCpRXwvKd1RaSEZc5er7TwiyL9ebcfh1x3b3k2qXZhi6eXnktGBhEcNiJoJe7FdRA7gTXs9MCQewX8r7u3ay6i94bT9BrdXZy7xnmFU1DvyCcNP3sHHC1gDbNxdzVYsyF9tuxuXhmmD3rTc7hCPX4uo1dahq5HFaaQifp8kwe6iVuzp3Qo5vNPgV8tJBzXhGx7wgYoozGznh1ecYdBtCpHdEwzbME4bQy8qLXsBRB7TVnhQ2GYUL8XyCpzaziWEe1tdKrgbFFE1CNnLuGR7FEFikx5PfJVFTmFvpEvvtM7Qq9TMFjgsaoP58M7BajhU74EgqVRX3hi8pmmzxnjAzVKK5wZR62RcpowjrYsNLGjcWL8B",
       |    "assets": [
       |        {
       |            "tokenId": "81f307da6c294bb9ee1c8789dfeff5b97c2399451e099ab6c9985a55551e41dd",
       |            "index": 0,
       |            "amount": 10000,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        },
       |        {
       |            "tokenId": "b19b810cc4dbc4bfaca74f88bb3797dcd8bab766ab360c275f3bc5b0476a50a9",
       |            "index": 1,
       |            "amount": 50000,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        },
       |        {
       |            "tokenId": "cba6fabbc040c49873d3dea062a7fc81ff3262e1799dfd41e05014c5e8d91109",
       |            "index": 2,
       |            "amount": 1,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        }
       |    ],
       |    "additionalRegisters": {
       |        "R4": {
       |            "serializedValue": "08cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491",
       |            "sigmaType": "SSigmaProp",
       |            "renderedValue": "03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491"
       |        },
       |        "R5": {
       |            "serializedValue": "0e2048e744055c9e49b26d1c70eca3c848afc8f50eddf8962a33f3d4b5df3d771ac2",
       |            "sigmaType": "Coll[SByte]",
       |            "renderedValue": "48e744055c9e49b26d1c70eca3c848afc8f50eddf8962a33f3d4b5df3d771ac2"
       |        }
       |    },
       |    "spentTransactionId": "ee380e605ed7d6e333f0db5762eae827aecc633c0d330245be39bf599c801637",
       |    "mainChain": true
       |}
       |""".stripMargin
  ).toOption.get

  val bundle = CompoundV1(
    output,
    AssetAmount(
      TokenId.unsafeFromString("81f307da6c294bb9ee1c8789dfeff5b97c2399451e099ab6c9985a55551e41dd"),
      10000
    ),
    AssetAmount(
      TokenId.unsafeFromString("b19b810cc4dbc4bfaca74f88bb3797dcd8bab766ab360c275f3bc5b0476a50a9"),
      50000
    ),
    AssetAmount(
      TokenId.unsafeFromString("cba6fabbc040c49873d3dea062a7fc81ff3262e1799dfd41e05014c5e8d91109"),
      1
    ),
    PoolId.unsafeFromString("48e744055c9e49b26d1c70eca3c848afc8f50eddf8962a33f3d4b5df3d771ac2"),
    PublicKeyRedeemer(PubKey.unsafeFromString("03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491")),
    V1
  )

}