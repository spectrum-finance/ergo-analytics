package fi.spectrum.parser.lm.pool.v1

import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.{AssetAmount, TokenId}
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.pool.Pool.LmPoolSelfHosted
import fi.spectrum.core.domain.transaction.Output
import io.circe.parser.decode

object SelfHosted {

  val selfHostedPool = decode[Output](s"""{
       |    "boxId": "cba6fabbc040c49873d3dea062a7fc81ff3262e1799dfd41e05014c5e8d91109",
       |    "transactionId": "682419b7a732e0c216c52be64c7b84d9fccb89e3fb627f6cb199371614b9b699",
       |    "blockId": "9f8807e226c3bcd4c8789148860543c85ed33f6ab3add8c40c56da4bac06cd8f",
       |    "value": 1250000,
       |    "index": 0,
       |    "globalIndex": 26335623,
       |    "creationHeight": 935193,
       |    "settlementHeight": 935197,
       |    "ergoTree": "19c0062904000400040204020404040404060406040804080404040204000400040204020601010400040a050005000404040204020e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c0400040205000402040204060500050005feffffffffffffffff010502050005000402050005000100d820d601b2a5730000d602db63087201d603db6308a7d604b27203730100d605e4c6a70410d606e4c6a70505d607e4c6a70605d608b27202730200d609b27203730300d60ab27202730400d60bb27203730500d60cb27202730600d60db27203730700d60e8c720d01d60fb27202730800d610b27203730900d6118c721001d6128c720b02d613998c720a027212d6148c720902d615b27205730a00d6169a99a37215730bd617b27205730c00d6189d72167217d61995919e72167217730d9a7218730e7218d61ab27205730f00d61b7e721a05d61c9d7206721bd61d998c720c028c720d02d61e8c721002d61f998c720f02721ed6207310d1ededededed93b272027311007204ededed93e4c672010410720593e4c672010505720693e4c6720106057207928cc77201018cc7a70193c27201c2a7ededed938c7208018c720901938c720a018c720b01938c720c01720e938c720f01721193b172027312959172137313d802d6219c721399721ba273147e721905d622b2a5731500ededed929a997206721472079c7e9995907219721a72199a721a7316731705721c937213f0721d937221f0721fedededed93cbc272227318938602720e7213b2db6308722273190093860272117221b2db63087222731a00e6c67222040893e4c67222050e8c720401958f7213731bededec929a997206721472079c7e9995907219721a72199a721a731c731d05721c92a39a9a72159c721a7217b27205731e0093721df0721392721f95917219721a731f9c721d99721ba273207e721905d804d621e4c672010704d62299721a7221d6237e722205d62499997321721e9c9972127322722395ed917224732391721f7324edededed9072219972197325909972149c7223721c9a721c7207907ef0998c7208027214069a9d9c99997e7214069d9c7e7206067e7222067e721a0672207e721f067e7224067220937213732693721d73277328",
       |    "address": "2JowFguNN2xRcYi3GANwoCnk46sVvc2D6oT27A2xbj5DsDEj2TMJbZmR8iqtWCbobi7hEvhkXQeMo9sjGh5jdBo133aJxr7ZTcfeVk8zN1MMDRqP64ehC7BNYGarJ5eaHpEq6DahnLC2khXpqU7dvfCczuPXd9qioB32tNqdCk3b9NBGUuQxNqK6FrHbLVi99KvuN2Nz9bh8HEZFukDKymuDpHwXF5JjMUW2YGDQCBFmLgKGFrLrvC2iQH18ftfmQoqUS4GCXpdJ5YtPMg8bLZ4GNP5QaXZXEQpRTz5fgRu9FR2ZgmZ5r1tSQV789H3Lf6RcA3RmkWoTw8RMEMdwE45e7yoiWVTHqkFMLz2qEnYgmzSyxv7Uxk1f9KXfvWPf4CbMfi9y6txGXh5MNhzPKzRUuNS8VxScAt7JYfDCvp1KhyyFLWy9ijtDbmUw2MshWAubzt2dAJaH2hAMiv1rGi6QWG9ru6LvPTH7Tr1aUhLac6DK2p2ycBwk8hp417K7sU2aVzRfWhreEZxnCH2aGQvp8iq4PDkRsEjgUpMzCu4wJDXRa9v3gKoBP7N3xWV2xUg1xmMTPC1LNomT86k5vQvBZpM74gZ3RWmJTFoq6dwPtHNHC2hdyfuqZDpHz2ATcbem4wmG1QJPig92R36WKmydN53RujrA3pXccxGNHqMqPsu2YexSURoSihtJYsrdGG4332DdGappSnAgDeETYJ69GJ6giTiddVQv3oKzuF7tZwdVrFzFoUkSWEGqd4jnWRg46cqTqJqT5RH4MJw7FsCi6gZD1X39X1ANgv9vC6pNV6QqdJktpN9CoM2DbEyVMyYdhpgotexAR9u6KEBJmEbfps7jus9X97arMmzHRwJR99X37uj8zT5g36SAPPwDyueEAsW7r1RNuwm4DGPAmdxYBRgi5iri9TCHpAeDBLCPU9tycSVPviDrNxKWVYEus6jqZdNENPrTeA83T4SGdFKmA8aVyNKPtzvejRLEwYCjdLAAgoF7oGY5R6gq9caatH23rrEJutNCS4csAgm8QyrETfF1nv9KMrGvA9E577dBcNYKHdLjfVKHi7op3LyNTgYTc2DkYakdG94tJzPbPsgAetpFKs3SjWcUkhhgCm8JoiQ8iFDXG4kgADJ",
       |    "assets": [
       |        {
       |            "tokenId": "48e744055c9e49b26d1c70eca3c848afc8f50eddf8962a33f3d4b5df3d771ac2",
       |            "index": 0,
       |            "amount": 1,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        },
       |        {
       |            "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
       |            "index": 1,
       |            "amount": 5000000,
       |            "name": "EPOS",
       |            "decimals": 4,
       |            "type": "EIP-004"
       |        },
       |        {
       |            "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
       |            "index": 2,
       |            "amount": 1,
       |            "name": "Ergo_ErgoPOS_LP",
       |            "decimals": 0,
       |            "type": "EIP-004"
       |        },
       |        {
       |            "tokenId": "81f307da6c294bb9ee1c8789dfeff5b97c2399451e099ab6c9985a55551e41dd",
       |            "index": 3,
       |            "amount": 9223372036854775807,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        },
       |        {
       |            "tokenId": "b19b810cc4dbc4bfaca74f88bb3797dcd8bab766ab360c275f3bc5b0476a50a9",
       |            "index": 4,
       |            "amount": 9223372036854775807,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        }
       |    ],
       |    "additionalRegisters": {
       |        "R4": {
       |            "serializedValue": "1004f4030aa69872c801",
       |            "sigmaType": "Coll[SInt]",
       |            "renderedValue": "[250,5,935443,100]"
       |        },
       |        "R5": {
       |            "serializedValue": "05feace204",
       |            "sigmaType": "SLong",
       |            "renderedValue": "4999999"
       |        },
       |        "R6": {
       |            "serializedValue": "05d00f",
       |            "sigmaType": "SLong",
       |            "renderedValue": "1000"
       |        }
       |    },
       |    "spentTransactionId": "6c2106bee6af77659b85006cf4cf365b4c3b944928d8fe3ba695cdce14d2bb3e",
       |    "mainChain": true
       |}""".stripMargin).toOption.get

  val pool = LmPoolSelfHosted(
    PoolId.unsafeFromString("48e744055c9e49b26d1c70eca3c848afc8f50eddf8962a33f3d4b5df3d771ac2"),
    AssetAmount(
      TokenId.unsafeFromString("00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf"),
      5000000
    ),
    AssetAmount(
      TokenId.unsafeFromString("e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87"),
      1
    ),
    AssetAmount(
      TokenId.unsafeFromString("81f307da6c294bb9ee1c8789dfeff5b97c2399451e099ab6c9985a55551e41dd"),
      9223372036854775807L
    ),
    AssetAmount(
      TokenId.unsafeFromString("b19b810cc4dbc4bfaca74f88bb3797dcd8bab766ab360c275f3bc5b0476a50a9"),
      9223372036854775807L
    ),
    250,
    5,
    935443,
    100,
    4999999,
    1000,
    None,
    0,
    selfHostedPool,
    V1,
    10
  )

}
