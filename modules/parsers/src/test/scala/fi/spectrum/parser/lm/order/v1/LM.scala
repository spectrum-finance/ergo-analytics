package fi.spectrum.parser.lm.order.v1

import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.{AssetAmount, SErgoTree, TokenId}
import fi.spectrum.core.domain.order.Order.Deposit.LmDeposit.LmDepositV1
import fi.spectrum.core.domain.order.{LmDepositParams, PoolId}
import fi.spectrum.core.domain.order.Redeemer.ErgoTreeRedeemer
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.models.TransactionTest
import io.circe.parser.decode

object LM {

  def depositOrder = {
    val a = decode[Output](
      s"""
         |{
         |    "boxId": "0a1c38904a276908885e5b1e50956c7b667ca2e398166e6d6d7bdf4569e8af89",
         |    "transactionId": "2f839ac334649bb8d0ba9717181948228c70e6a8082bfa920420a6fbaf7cec20",
         |    "blockId": "1409e7bf1c1551e4e5c0a04af8c1819accf99e4aae3fb7a09c03a854a7873a0b",
         |    "value": 1250000,
         |    "index": 0,
         |    "globalIndex": 26335814,
         |    "creationHeight": 935193,
         |    "settlementHeight": 935202,
         |    "ergoTree": "198e041604000e2048e744055c9e49b26d1c70eca3c848afc8f50eddf8962a33f3d4b5df3d771ac204020e240008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac4910404040008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491040005fcffffffffffffffff0104000e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c040604000408040a0402050204040e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304050005000580f882ad16d808d601b2a4730000d602db63087201d6037301d604b2a5730200d6057303d606c57201d607b2a5730400d6088cb2db6308a773050002eb027306d1ededed938cb27202730700017203ed93c27204720593860272067308b2db63087204730900ededededed93cbc27207730a93d0e4c672070408720593e4c67207050e72039386028cb27202730b00017208b2db63087207730c009386028cb27202730d00019c72087e730e05b2db63087207730f0093860272067310b2db6308720773110090b0ada5d90109639593c272097312c1720973137314d90109599a8c7209018c7209027315",
         |    "address": "32nV8Jp4LFefRfzGwDRGcRVqLtQrravuoftrJmeydHY5HioZkX7xPsSFtjkQXCAcZo7A368EcNPseA9dn6KNfbaZ6PpYAdgzUn1csW81VDa6rXda1sszSpxMe2bNwKP98nzHVEMMBNdMVR8y2avNKPG9Zi9wrmMg3iCN1ZMuCGGLenhTqk2AXsxnsoT3UYX7ijtVY1D7aZHBt1R3ZXAznKn4jUad17oNEM4mNp4UqzzzCa7h1cBgTqJdLLHatnp1ahnAgrY6rZFD9oCDRYYBQ67A5dhkbggLQhAGnTvwtS4Mf6jZxafh8i82RynVLgVahQiMFC2yvb9jbqyjkqXoR9xLRWHeaJuqX6VEySoKXqpMQzQTn5agkhfrJHyNAsQPVrTNfMPoe2P5uYs4z9yT2tyMBQSnHr4GSUcaUkrNpXT5JpgzeoABSne7H1wrEgDRomHc2UWJC3299z5ajkyaoWcGLP8xg5SCnQktq4herUyeWnYxz73whLsihsZLp6LCE94iPUSKV7PPnqTCYgejXCXsuUwe6jsBAbQCknLPdni4GrtJUZyuHYCe2Yd9Mjh4Qr3orHrViMog2YsZqHwnibrpLGtJrDoQTbaf35rpcuPBryFC8HGo4KqC3LwmKim8wg4YJ4m2Py5qew2zjoaNqxCnyHRsgfyCxFJFMtrPWHZ391P7JFDjWd27pps4E1hCmjf8Bt5vtKbW7Lutx2ACjFvFb",
         |    "assets": [
         |        {
         |            "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
         |            "index": 0,
         |            "amount": 10000,
         |            "name": "Ergo_ErgoPOS_LP",
         |            "decimals": 0,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "6c2106bee6af77659b85006cf4cf365b4c3b944928d8fe3ba695cdce14d2bb3e",
         |    "mainChain": true
         |}""".stripMargin
    )
    a.toOption.get
  }



  val deposit = LmDepositV1(
    depositOrder,
    PoolId.unsafeFromString("48e744055c9e49b26d1c70eca3c848afc8f50eddf8962a33f3d4b5df3d771ac2"),
    ErgoTreeRedeemer(
      SErgoTree.unsafeFromString("0008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491")
    ),
    LmDepositParams(
      5,
      AssetAmount(
        TokenId.unsafeFromString("e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87"),
        10000
      )
    ),
    3000000000L,
    V1
  )

  val tx = decode[TransactionTest](
    s"""
       |{
       |    "id": "6c2106bee6af77659b85006cf4cf365b4c3b944928d8fe3ba695cdce14d2bb3e",
       |    "blockId": "6967cee75df994f539b1b16b684efc20c184d4781a341065fe55c50c431e6f12",
       |    "inclusionHeight": 935204,
       |    "timestamp": 1675779063816,
       |    "index": 13,
       |    "globalIndex": 4725307,
       |    "numConfirmations": 781,
       |    "inputs": [
       |        {
       |            "boxId": "cba6fabbc040c49873d3dea062a7fc81ff3262e1799dfd41e05014c5e8d91109",
       |            "value": 1250000,
       |            "index": 0,
       |            "spendingProof": "f7fb2c0e624a861d033f9f68884e5b023202f814c34c1e5acb86ee041a9167d603fc0237fce15623798efa581d90c94f099eb1ab8ec4bb38",
       |            "outputBlockId": "9f8807e226c3bcd4c8789148860543c85ed33f6ab3add8c40c56da4bac06cd8f",
       |            "outputTransactionId": "682419b7a732e0c216c52be64c7b84d9fccb89e3fb627f6cb199371614b9b699",
       |            "outputIndex": 0,
       |            "outputGlobalIndex": 26335623,
       |            "outputCreatedAt": 935193,
       |            "outputSettledAt": 935197,
       |            "ergoTree": "19c0062904000400040204020404040404060406040804080404040204000400040204020601010400040a050005000404040204020e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c0400040205000402040204060500050005feffffffffffffffff010502050005000402050005000100d820d601b2a5730000d602db63087201d603db6308a7d604b27203730100d605e4c6a70410d606e4c6a70505d607e4c6a70605d608b27202730200d609b27203730300d60ab27202730400d60bb27203730500d60cb27202730600d60db27203730700d60e8c720d01d60fb27202730800d610b27203730900d6118c721001d6128c720b02d613998c720a027212d6148c720902d615b27205730a00d6169a99a37215730bd617b27205730c00d6189d72167217d61995919e72167217730d9a7218730e7218d61ab27205730f00d61b7e721a05d61c9d7206721bd61d998c720c028c720d02d61e8c721002d61f998c720f02721ed6207310d1ededededed93b272027311007204ededed93e4c672010410720593e4c672010505720693e4c6720106057207928cc77201018cc7a70193c27201c2a7ededed938c7208018c720901938c720a018c720b01938c720c01720e938c720f01721193b172027312959172137313d802d6219c721399721ba273147e721905d622b2a5731500ededed929a997206721472079c7e9995907219721a72199a721a7316731705721c937213f0721d937221f0721fedededed93cbc272227318938602720e7213b2db6308722273190093860272117221b2db63087222731a00e6c67222040893e4c67222050e8c720401958f7213731bededec929a997206721472079c7e9995907219721a72199a721a731c731d05721c92a39a9a72159c721a7217b27205731e0093721df0721392721f95917219721a731f9c721d99721ba273207e721905d804d621e4c672010704d62299721a7221d6237e722205d62499997321721e9c9972127322722395ed917224732391721f7324edededed9072219972197325909972149c7223721c9a721c7207907ef0998c7208027214069a9d9c99997e7214069d9c7e7206067e7222067e721a0672207e721f067e7224067220937213732693721d73277328",
       |            "address": "2JowFguNN2xRcYi3GANwoCnk46sVvc2D6oT27A2xbj5DsDEj2TMJbZmR8iqtWCbobi7hEvhkXQeMo9sjGh5jdBo133aJxr7ZTcfeVk8zN1MMDRqP64ehC7BNYGarJ5eaHpEq6DahnLC2khXpqU7dvfCczuPXd9qioB32tNqdCk3b9NBGUuQxNqK6FrHbLVi99KvuN2Nz9bh8HEZFukDKymuDpHwXF5JjMUW2YGDQCBFmLgKGFrLrvC2iQH18ftfmQoqUS4GCXpdJ5YtPMg8bLZ4GNP5QaXZXEQpRTz5fgRu9FR2ZgmZ5r1tSQV789H3Lf6RcA3RmkWoTw8RMEMdwE45e7yoiWVTHqkFMLz2qEnYgmzSyxv7Uxk1f9KXfvWPf4CbMfi9y6txGXh5MNhzPKzRUuNS8VxScAt7JYfDCvp1KhyyFLWy9ijtDbmUw2MshWAubzt2dAJaH2hAMiv1rGi6QWG9ru6LvPTH7Tr1aUhLac6DK2p2ycBwk8hp417K7sU2aVzRfWhreEZxnCH2aGQvp8iq4PDkRsEjgUpMzCu4wJDXRa9v3gKoBP7N3xWV2xUg1xmMTPC1LNomT86k5vQvBZpM74gZ3RWmJTFoq6dwPtHNHC2hdyfuqZDpHz2ATcbem4wmG1QJPig92R36WKmydN53RujrA3pXccxGNHqMqPsu2YexSURoSihtJYsrdGG4332DdGappSnAgDeETYJ69GJ6giTiddVQv3oKzuF7tZwdVrFzFoUkSWEGqd4jnWRg46cqTqJqT5RH4MJw7FsCi6gZD1X39X1ANgv9vC6pNV6QqdJktpN9CoM2DbEyVMyYdhpgotexAR9u6KEBJmEbfps7jus9X97arMmzHRwJR99X37uj8zT5g36SAPPwDyueEAsW7r1RNuwm4DGPAmdxYBRgi5iri9TCHpAeDBLCPU9tycSVPviDrNxKWVYEus6jqZdNENPrTeA83T4SGdFKmA8aVyNKPtzvejRLEwYCjdLAAgoF7oGY5R6gq9caatH23rrEJutNCS4csAgm8QyrETfF1nv9KMrGvA9E577dBcNYKHdLjfVKHi7op3LyNTgYTc2DkYakdG94tJzPbPsgAetpFKs3SjWcUkhhgCm8JoiQ8iFDXG4kgADJ",
       |            "assets": [
       |                {
       |                    "tokenId": "48e744055c9e49b26d1c70eca3c848afc8f50eddf8962a33f3d4b5df3d771ac2",
       |                    "index": 0,
       |                    "amount": 1,
       |                    "name": null,
       |                    "decimals": null,
       |                    "type": null
       |                },
       |                {
       |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
       |                    "index": 1,
       |                    "amount": 5000000,
       |                    "name": "EPOS",
       |                    "decimals": 4,
       |                    "type": "EIP-004"
       |                },
       |                {
       |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
       |                    "index": 2,
       |                    "amount": 1,
       |                    "name": "Ergo_ErgoPOS_LP",
       |                    "decimals": 0,
       |                    "type": "EIP-004"
       |                },
       |                {
       |                    "tokenId": "81f307da6c294bb9ee1c8789dfeff5b97c2399451e099ab6c9985a55551e41dd",
       |                    "index": 3,
       |                    "amount": 9223372036854775807,
       |                    "name": null,
       |                    "decimals": null,
       |                    "type": null
       |                },
       |                {
       |                    "tokenId": "b19b810cc4dbc4bfaca74f88bb3797dcd8bab766ab360c275f3bc5b0476a50a9",
       |                    "index": 4,
       |                    "amount": 9223372036854775807,
       |                    "name": null,
       |                    "decimals": null,
       |                    "type": null
       |                }
       |            ],
       |            "additionalRegisters": {
       |                "R4": {
       |                    "serializedValue": "1004f4030aa69872c801",
       |                    "sigmaType": "Coll[SInt]",
       |                    "renderedValue": "[250,5,935443,100]"
       |                },
       |                "R5": {
       |                    "serializedValue": "05feace204",
       |                    "sigmaType": "SLong",
       |                    "renderedValue": "4999999"
       |                },
       |                "R6": {
       |                    "serializedValue": "05d00f",
       |                    "sigmaType": "SLong",
       |                    "renderedValue": "1000"
       |                }
       |            }
       |        },
       |        {
       |            "boxId": "0a1c38904a276908885e5b1e50956c7b667ca2e398166e6d6d7bdf4569e8af89",
       |            "value": 1250000,
       |            "index": 1,
       |            "spendingProof": "f7fb2c0e624a861d033f9f68884e5b023202f814c34c1e5acb86ee041a9167d603fc0237fce15623798efa581d90c94f099eb1ab8ec4bb38",
       |            "outputBlockId": "1409e7bf1c1551e4e5c0a04af8c1819accf99e4aae3fb7a09c03a854a7873a0b",
       |            "outputTransactionId": "2f839ac334649bb8d0ba9717181948228c70e6a8082bfa920420a6fbaf7cec20",
       |            "outputIndex": 0,
       |            "outputGlobalIndex": 26335814,
       |            "outputCreatedAt": 935193,
       |            "outputSettledAt": 935202,
       |            "ergoTree": "198e041604000e2048e744055c9e49b26d1c70eca3c848afc8f50eddf8962a33f3d4b5df3d771ac204020e240008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac4910404040008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491040005fcffffffffffffffff0104000e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c040604000408040a0402050204040e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304050005000580f882ad16d808d601b2a4730000d602db63087201d6037301d604b2a5730200d6057303d606c57201d607b2a5730400d6088cb2db6308a773050002eb027306d1ededed938cb27202730700017203ed93c27204720593860272067308b2db63087204730900ededededed93cbc27207730a93d0e4c672070408720593e4c67207050e72039386028cb27202730b00017208b2db63087207730c009386028cb27202730d00019c72087e730e05b2db63087207730f0093860272067310b2db6308720773110090b0ada5d90109639593c272097312c1720973137314d90109599a8c7209018c7209027315",
       |            "address": "32nV8Jp4LFefRfzGwDRGcRVqLtQrravuoftrJmeydHY5HioZkX7xPsSFtjkQXCAcZo7A368EcNPseA9dn6KNfbaZ6PpYAdgzUn1csW81VDa6rXda1sszSpxMe2bNwKP98nzHVEMMBNdMVR8y2avNKPG9Zi9wrmMg3iCN1ZMuCGGLenhTqk2AXsxnsoT3UYX7ijtVY1D7aZHBt1R3ZXAznKn4jUad17oNEM4mNp4UqzzzCa7h1cBgTqJdLLHatnp1ahnAgrY6rZFD9oCDRYYBQ67A5dhkbggLQhAGnTvwtS4Mf6jZxafh8i82RynVLgVahQiMFC2yvb9jbqyjkqXoR9xLRWHeaJuqX6VEySoKXqpMQzQTn5agkhfrJHyNAsQPVrTNfMPoe2P5uYs4z9yT2tyMBQSnHr4GSUcaUkrNpXT5JpgzeoABSne7H1wrEgDRomHc2UWJC3299z5ajkyaoWcGLP8xg5SCnQktq4herUyeWnYxz73whLsihsZLp6LCE94iPUSKV7PPnqTCYgejXCXsuUwe6jsBAbQCknLPdni4GrtJUZyuHYCe2Yd9Mjh4Qr3orHrViMog2YsZqHwnibrpLGtJrDoQTbaf35rpcuPBryFC8HGo4KqC3LwmKim8wg4YJ4m2Py5qew2zjoaNqxCnyHRsgfyCxFJFMtrPWHZ391P7JFDjWd27pps4E1hCmjf8Bt5vtKbW7Lutx2ACjFvFb",
       |            "assets": [
       |                {
       |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
       |                    "index": 0,
       |                    "amount": 10000,
       |                    "name": "Ergo_ErgoPOS_LP",
       |                    "decimals": 0,
       |                    "type": "EIP-004"
       |                }
       |            ],
       |            "additionalRegisters": {}
       |        },
       |        {
       |            "boxId": "4324490fd85a948746bf4ae8d66672093953c96ba780e6245acb0c3347fc6f6f",
       |            "value": 2927500000,
       |            "index": 2,
       |            "spendingProof": "f7fb2c0e624a861d033f9f68884e5b023202f814c34c1e5acb86ee041a9167d603fc0237fce15623798efa581d90c94f099eb1ab8ec4bb38",
       |            "outputBlockId": "1409e7bf1c1551e4e5c0a04af8c1819accf99e4aae3fb7a09c03a854a7873a0b",
       |            "outputTransactionId": "2f839ac334649bb8d0ba9717181948228c70e6a8082bfa920420a6fbaf7cec20",
       |            "outputIndex": 1,
       |            "outputGlobalIndex": 26335815,
       |            "outputCreatedAt": 935193,
       |            "outputSettledAt": 935202,
       |            "ergoTree": "0008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491",
       |            "address": "9iAYDwPWP7c9cKXB7P2bLivt75CBaXrbYoZrxpna1peB8cYFoHw",
       |            "assets": [
       |                {
       |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
       |                    "index": 0,
       |                    "amount": 24000000,
       |                    "name": "EPOS",
       |                    "decimals": 4,
       |                    "type": "EIP-004"
       |                },
       |                {
       |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
       |                    "index": 1,
       |                    "amount": 49954992,
       |                    "name": "Ergo_ErgoPOS_LP",
       |                    "decimals": 0,
       |                    "type": "EIP-004"
       |                }
       |            ],
       |            "additionalRegisters": {}
       |        }
       |    ],
       |    "dataInputs": [],
       |    "outputs": [
       |        {
       |            "boxId": "250c76c79dc87ff13272b8f5e59d8b66a7aa2fcd7ae76d0cf7bb0edc8ce0b7db",
       |            "transactionId": "6c2106bee6af77659b85006cf4cf365b4c3b944928d8fe3ba695cdce14d2bb3e",
       |            "blockId": "6967cee75df994f539b1b16b684efc20c184d4781a341065fe55c50c431e6f12",
       |            "value": 1250000,
       |            "index": 0,
       |            "globalIndex": 26335924,
       |            "creationHeight": 935194,
       |            "settlementHeight": 935204,
       |            "ergoTree": "19c0062904000400040204020404040404060406040804080404040204000400040204020601010400040a050005000404040204020e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c0400040205000402040204060500050005feffffffffffffffff010502050005000402050005000100d820d601b2a5730000d602db63087201d603db6308a7d604b27203730100d605e4c6a70410d606e4c6a70505d607e4c6a70605d608b27202730200d609b27203730300d60ab27202730400d60bb27203730500d60cb27202730600d60db27203730700d60e8c720d01d60fb27202730800d610b27203730900d6118c721001d6128c720b02d613998c720a027212d6148c720902d615b27205730a00d6169a99a37215730bd617b27205730c00d6189d72167217d61995919e72167217730d9a7218730e7218d61ab27205730f00d61b7e721a05d61c9d7206721bd61d998c720c028c720d02d61e8c721002d61f998c720f02721ed6207310d1ededededed93b272027311007204ededed93e4c672010410720593e4c672010505720693e4c6720106057207928cc77201018cc7a70193c27201c2a7ededed938c7208018c720901938c720a018c720b01938c720c01720e938c720f01721193b172027312959172137313d802d6219c721399721ba273147e721905d622b2a5731500ededed929a997206721472079c7e9995907219721a72199a721a7316731705721c937213f0721d937221f0721fedededed93cbc272227318938602720e7213b2db6308722273190093860272117221b2db63087222731a00e6c67222040893e4c67222050e8c720401958f7213731bededec929a997206721472079c7e9995907219721a72199a721a731c731d05721c92a39a9a72159c721a7217b27205731e0093721df0721392721f95917219721a731f9c721d99721ba273207e721905d804d621e4c672010704d62299721a7221d6237e722205d62499997321721e9c9972127322722395ed917224732391721f7324edededed9072219972197325909972149c7223721c9a721c7207907ef0998c7208027214069a9d9c99997e7214069d9c7e7206067e7222067e721a0672207e721f067e7224067220937213732693721d73277328",
       |            "address": "2JowFguNN2xRcYi3GANwoCnk46sVvc2D6oT27A2xbj5DsDEj2TMJbZmR8iqtWCbobi7hEvhkXQeMo9sjGh5jdBo133aJxr7ZTcfeVk8zN1MMDRqP64ehC7BNYGarJ5eaHpEq6DahnLC2khXpqU7dvfCczuPXd9qioB32tNqdCk3b9NBGUuQxNqK6FrHbLVi99KvuN2Nz9bh8HEZFukDKymuDpHwXF5JjMUW2YGDQCBFmLgKGFrLrvC2iQH18ftfmQoqUS4GCXpdJ5YtPMg8bLZ4GNP5QaXZXEQpRTz5fgRu9FR2ZgmZ5r1tSQV789H3Lf6RcA3RmkWoTw8RMEMdwE45e7yoiWVTHqkFMLz2qEnYgmzSyxv7Uxk1f9KXfvWPf4CbMfi9y6txGXh5MNhzPKzRUuNS8VxScAt7JYfDCvp1KhyyFLWy9ijtDbmUw2MshWAubzt2dAJaH2hAMiv1rGi6QWG9ru6LvPTH7Tr1aUhLac6DK2p2ycBwk8hp417K7sU2aVzRfWhreEZxnCH2aGQvp8iq4PDkRsEjgUpMzCu4wJDXRa9v3gKoBP7N3xWV2xUg1xmMTPC1LNomT86k5vQvBZpM74gZ3RWmJTFoq6dwPtHNHC2hdyfuqZDpHz2ATcbem4wmG1QJPig92R36WKmydN53RujrA3pXccxGNHqMqPsu2YexSURoSihtJYsrdGG4332DdGappSnAgDeETYJ69GJ6giTiddVQv3oKzuF7tZwdVrFzFoUkSWEGqd4jnWRg46cqTqJqT5RH4MJw7FsCi6gZD1X39X1ANgv9vC6pNV6QqdJktpN9CoM2DbEyVMyYdhpgotexAR9u6KEBJmEbfps7jus9X97arMmzHRwJR99X37uj8zT5g36SAPPwDyueEAsW7r1RNuwm4DGPAmdxYBRgi5iri9TCHpAeDBLCPU9tycSVPviDrNxKWVYEus6jqZdNENPrTeA83T4SGdFKmA8aVyNKPtzvejRLEwYCjdLAAgoF7oGY5R6gq9caatH23rrEJutNCS4csAgm8QyrETfF1nv9KMrGvA9E577dBcNYKHdLjfVKHi7op3LyNTgYTc2DkYakdG94tJzPbPsgAetpFKs3SjWcUkhhgCm8JoiQ8iFDXG4kgADJ",
       |            "assets": [
       |                {
       |                    "tokenId": "48e744055c9e49b26d1c70eca3c848afc8f50eddf8962a33f3d4b5df3d771ac2",
       |                    "index": 0,
       |                    "amount": 1,
       |                    "name": null,
       |                    "decimals": null,
       |                    "type": null
       |                },
       |                {
       |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
       |                    "index": 1,
       |                    "amount": 5000000,
       |                    "name": "EPOS",
       |                    "decimals": 4,
       |                    "type": "EIP-004"
       |                },
       |                {
       |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
       |                    "index": 2,
       |                    "amount": 10001,
       |                    "name": "Ergo_ErgoPOS_LP",
       |                    "decimals": 0,
       |                    "type": "EIP-004"
       |                },
       |                {
       |                    "tokenId": "81f307da6c294bb9ee1c8789dfeff5b97c2399451e099ab6c9985a55551e41dd",
       |                    "index": 3,
       |                    "amount": 9223372036854765807,
       |                    "name": null,
       |                    "decimals": null,
       |                    "type": null
       |                },
       |                {
       |                    "tokenId": "b19b810cc4dbc4bfaca74f88bb3797dcd8bab766ab360c275f3bc5b0476a50a9",
       |                    "index": 4,
       |                    "amount": 9223372036854725807,
       |                    "name": null,
       |                    "decimals": null,
       |                    "type": null
       |                }
       |            ],
       |            "additionalRegisters": {
       |                "R4": {
       |                    "serializedValue": "1004f4030aa69872c801",
       |                    "sigmaType": "Coll[SInt]",
       |                    "renderedValue": "[250,5,935443,100]"
       |                },
       |                "R5": {
       |                    "serializedValue": "05feace204",
       |                    "sigmaType": "SLong",
       |                    "renderedValue": "4999999"
       |                },
       |                "R6": {
       |                    "serializedValue": "05d00f",
       |                    "sigmaType": "SLong",
       |                    "renderedValue": "1000"
       |                }
       |            },
       |            "spentTransactionId": "6252fdbe214e0f98984e08590fce6e2eb58e1d70016d98d9a2e8a5a396c2a204",
       |            "mainChain": true
       |        },
       |        {
       |            "boxId": "e6539b9b5a1ba24aee00f1a5682bd930f2cf9a36a66c5d6fc4863f0d0d63fee0",
       |            "transactionId": "6c2106bee6af77659b85006cf4cf365b4c3b944928d8fe3ba695cdce14d2bb3e",
       |            "blockId": "6967cee75df994f539b1b16b684efc20c184d4781a341065fe55c50c431e6f12",
       |            "value": 1250000,
       |            "index": 1,
       |            "globalIndex": 26335925,
       |            "creationHeight": 935194,
       |            "settlementHeight": 935204,
       |            "ergoTree": "0008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491",
       |            "address": "9iAYDwPWP7c9cKXB7P2bLivt75CBaXrbYoZrxpna1peB8cYFoHw",
       |            "assets": [
       |                {
       |                    "tokenId": "cba6fabbc040c49873d3dea062a7fc81ff3262e1799dfd41e05014c5e8d91109",
       |                    "index": 0,
       |                    "amount": 9223372036854775806,
       |                    "name": null,
       |                    "decimals": null,
       |                    "type": null
       |                }
       |            ],
       |            "additionalRegisters": {},
       |            "spentTransactionId": null,
       |            "mainChain": true
       |        },
       |        {
       |            "boxId": "8b13011ef535ed36797407334023dec3d9b27f141d45d675c1ba4ee02d97982d",
       |            "transactionId": "6c2106bee6af77659b85006cf4cf365b4c3b944928d8fe3ba695cdce14d2bb3e",
       |            "blockId": "6967cee75df994f539b1b16b684efc20c184d4781a341065fe55c50c431e6f12",
       |            "value": 1250000,
       |            "index": 2,
       |            "globalIndex": 26335926,
       |            "creationHeight": 935194,
       |            "settlementHeight": 935204,
       |            "ergoTree": "19a3041f040004000404040404000402060101040005000402040404020400040004020502040405020402040005feffffffffffffffff010408050205000404040004060404040205fcffffffffffffffff010100d80cd601b2a5730000d602db63087201d603e4c6a7050ed604b2a4730100d605db63087204d6068cb2720573020002d607998cb27202730300027206d608e4c6a70408d609db6308a7d60ab27209730400d60bb27205730500d60c7306d1ed938cb27202730700017203959372077308d80cd60db2a5e4e3000400d60eb2a5e4e3010400d60fb2e4c672040410730900d610c672010804d61199720f95e67210e47210e4c672010704d6128cb27209730a0001d613db6308720ed614b27209730b00d6158c721402d6167e721105d6179972159c72168c720a02d618b2db6308720d730c00eded93c2720dd07208edededed93e4c6720e0408720893e4c6720e050e720393c2720ec2a795917211730dd801d619b27213730e00eded9386027212730fb27213731000938c7219018c721401939972158c721902721793860272127311b2721373120093b27213731300720aed938c7218018c720b01927e8c7218020699999d9c99997e8c720b02069d9c7ee4c672040505067e7211067e720f06720c7e7217067e999973148cb27205731500029c9972067316721606720c720c958f7207731793b2db6308b2a473180073190086029593b17209731a8cb27209731b00018cb27209731c0001731d731e",
       |            "address": "YyLFSfjMQw5AJEuchDNPzziDH7CpLX8EbtGbddMpTpDoYYCc5xKJUbDuFnwsJ4qcpARjLmTQdRUTDiftnkb4kuNAaM7enwfxbWvh9SAbpvxAtJzhQApmedrEH9BBrPYzHdWbR354ZXZJkazhvfWMLyijyFeJ7aTDYmhDn8YvF3HeBMJiLByT32wTfNVEWQf4SEp3eN66ppqZs7bJAxrsZo3PDGJgTtZ2qPypMv3x9fcE6zYoLiCzHbAWC8WCfn5wNno9NAwSgWgHKcz3Gx4kXAe5DJnMDZJeiAuckS4e8J5sUfgFSPHTysvkfPw5pxBBNoxrsQ8y2jHSjB3KhSKKsNoZLnWZpbLQ7TurFc34TZJn9FNsRzpmQc9ftcZNa1QxKNoBtFzCpRXwvKd1RaSEZc5er7TwiyL9ebcfh1x3b3k2qXZhi6eXnktGBhEcNiJoJe7FdRA7gTXs9MCQewX8r7u3ay6i94bT9BrdXZy7xnmFU1DvyCcNP3sHHC1gDbNxdzVYsyF9tuxuXhmmD3rTc7hCPX4uo1dahq5HFaaQifp8kwe6iVuzp3Qo5vNPgV8tJBzXhGx7wgYoozGznh1ecYdBtCpHdEwzbME4bQy8qLXsBRB7TVnhQ2GYUL8XyCpzaziWEe1tdKrgbFFE1CNnLuGR7FEFikx5PfJVFTmFvpEvvtM7Qq9TMFjgsaoP58M7BajhU74EgqVRX3hi8pmmzxnjAzVKK5wZR62RcpowjrYsNLGjcWL8B",
       |            "assets": [
       |                {
       |                    "tokenId": "81f307da6c294bb9ee1c8789dfeff5b97c2399451e099ab6c9985a55551e41dd",
       |                    "index": 0,
       |                    "amount": 10000,
       |                    "name": null,
       |                    "decimals": null,
       |                    "type": null
       |                },
       |                {
       |                    "tokenId": "b19b810cc4dbc4bfaca74f88bb3797dcd8bab766ab360c275f3bc5b0476a50a9",
       |                    "index": 1,
       |                    "amount": 50000,
       |                    "name": null,
       |                    "decimals": null,
       |                    "type": null
       |                },
       |                {
       |                    "tokenId": "cba6fabbc040c49873d3dea062a7fc81ff3262e1799dfd41e05014c5e8d91109",
       |                    "index": 2,
       |                    "amount": 1,
       |                    "name": null,
       |                    "decimals": null,
       |                    "type": null
       |                }
       |            ],
       |            "additionalRegisters": {
       |                "R4": {
       |                    "serializedValue": "08cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491",
       |                    "sigmaType": "SSigmaProp",
       |                    "renderedValue": "03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491"
       |                },
       |                "R5": {
       |                    "serializedValue": "0e2048e744055c9e49b26d1c70eca3c848afc8f50eddf8962a33f3d4b5df3d771ac2",
       |                    "sigmaType": "Coll[SByte]",
       |                    "renderedValue": "48e744055c9e49b26d1c70eca3c848afc8f50eddf8962a33f3d4b5df3d771ac2"
       |                }
       |            },
       |            "spentTransactionId": "ee380e605ed7d6e333f0db5762eae827aecc633c0d330245be39bf599c801637",
       |            "mainChain": true
       |        },
       |        {
       |            "boxId": "508f5775f7c889ed0f27098de227d4bbbb672e256988f2b9896ddaf7ca5dfde5",
       |            "transactionId": "6c2106bee6af77659b85006cf4cf365b4c3b944928d8fe3ba695cdce14d2bb3e",
       |            "blockId": "6967cee75df994f539b1b16b684efc20c184d4781a341065fe55c50c431e6f12",
       |            "value": 1250000,
       |            "index": 3,
       |            "globalIndex": 26335927,
       |            "creationHeight": 935194,
       |            "settlementHeight": 935204,
       |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
       |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
       |            "assets": [],
       |            "additionalRegisters": {},
       |            "spentTransactionId": "edbe89d395496bb24eccefec910c4d9624097365728ac116df2aa80a621c19cd",
       |            "mainChain": true
       |        },
       |        {
       |            "boxId": "9640285fe15155efe1605b3bce0dcbcabe4a2a285c8c663a65f6cf50d191a97f",
       |            "transactionId": "6c2106bee6af77659b85006cf4cf365b4c3b944928d8fe3ba695cdce14d2bb3e",
       |            "blockId": "6967cee75df994f539b1b16b684efc20c184d4781a341065fe55c50c431e6f12",
       |            "value": 2925000000,
       |            "index": 4,
       |            "globalIndex": 26335928,
       |            "creationHeight": 935194,
       |            "settlementHeight": 935204,
       |            "ergoTree": "0008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491",
       |            "address": "9iAYDwPWP7c9cKXB7P2bLivt75CBaXrbYoZrxpna1peB8cYFoHw",
       |            "assets": [
       |                {
       |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
       |                    "index": 0,
       |                    "amount": 24000000,
       |                    "name": "EPOS",
       |                    "decimals": 4,
       |                    "type": "EIP-004"
       |                },
       |                {
       |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
       |                    "index": 1,
       |                    "amount": 49954992,
       |                    "name": "Ergo_ErgoPOS_LP",
       |                    "decimals": 0,
       |                    "type": "EIP-004"
       |                }
       |            ],
       |            "additionalRegisters": {},
       |            "spentTransactionId": "a0b20b9b90f0ede6635a7619799ad43dba59463362a5d8ed0dd0e3a682359aa7",
       |            "mainChain": true
       |        }
       |    ],
       |    "size": 2216
       |}
       |""".stripMargin
  ).toOption.get.toTransaction

  val deployDepositOrderTx =
    decode[TransactionTest](
      s"""
         |{
         |    "id": "2f839ac334649bb8d0ba9717181948228c70e6a8082bfa920420a6fbaf7cec20",
         |    "blockId": "1409e7bf1c1551e4e5c0a04af8c1819accf99e4aae3fb7a09c03a854a7873a0b",
         |    "inclusionHeight": 935202,
         |    "timestamp": 1675778340274,
         |    "index": 7,
         |    "globalIndex": 4725286,
         |    "numConfirmations": 883,
         |    "inputs": [
         |        {
         |            "boxId": "71971c431a1b75f0bdb08dc05f575659376814a83fec08368e26db57fee8b40e",
         |            "value": 2930000000,
         |            "index": 0,
         |            "spendingProof": "1d48527eec3a0e840d2153d3b98a9d3f2b24341daefed27a7f61d02bb7d4da1ad6af85da202701b6d9581454f3f04bce9175897479a4b551",
         |            "outputBlockId": "65bdd9aeda98186a9f439287b4695861b5c62b394e845d0b3ee8b3dacf6660cb",
         |            "outputTransactionId": "e92b4d14453a675f2e8a70cf1d5e818c243a2cea74c6611bfc323b3ea059ffed",
         |            "outputIndex": 1,
         |            "outputGlobalIndex": 26335760,
         |            "outputCreatedAt": 935193,
         |            "outputSettledAt": 935199,
         |            "ergoTree": "0008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491",
         |            "address": "9iAYDwPWP7c9cKXB7P2bLivt75CBaXrbYoZrxpna1peB8cYFoHw",
         |            "assets": [
         |                {
         |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
         |                    "index": 0,
         |                    "amount": 24000000,
         |                    "name": "EPOS",
         |                    "decimals": 4,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
         |                    "index": 1,
         |                    "amount": 49964992,
         |                    "name": "Ergo_ErgoPOS_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                }
         |            ],
         |            "additionalRegisters": {}
         |        }
         |    ],
         |    "dataInputs": [],
         |    "outputs": [
         |        {
         |            "boxId": "0a1c38904a276908885e5b1e50956c7b667ca2e398166e6d6d7bdf4569e8af89",
         |            "transactionId": "2f839ac334649bb8d0ba9717181948228c70e6a8082bfa920420a6fbaf7cec20",
         |            "blockId": "1409e7bf1c1551e4e5c0a04af8c1819accf99e4aae3fb7a09c03a854a7873a0b",
         |            "value": 1250000,
         |            "index": 0,
         |            "globalIndex": 26335814,
         |            "creationHeight": 935193,
         |            "settlementHeight": 935202,
         |            "ergoTree": "198e041604000e2048e744055c9e49b26d1c70eca3c848afc8f50eddf8962a33f3d4b5df3d771ac204020e240008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac4910404040008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491040005fcffffffffffffffff0104000e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c040604000408040a0402050204040e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304050005000580f882ad16d808d601b2a4730000d602db63087201d6037301d604b2a5730200d6057303d606c57201d607b2a5730400d6088cb2db6308a773050002eb027306d1ededed938cb27202730700017203ed93c27204720593860272067308b2db63087204730900ededededed93cbc27207730a93d0e4c672070408720593e4c67207050e72039386028cb27202730b00017208b2db63087207730c009386028cb27202730d00019c72087e730e05b2db63087207730f0093860272067310b2db6308720773110090b0ada5d90109639593c272097312c1720973137314d90109599a8c7209018c7209027315",
         |            "address": "32nV8Jp4LFefRfzGwDRGcRVqLtQrravuoftrJmeydHY5HioZkX7xPsSFtjkQXCAcZo7A368EcNPseA9dn6KNfbaZ6PpYAdgzUn1csW81VDa6rXda1sszSpxMe2bNwKP98nzHVEMMBNdMVR8y2avNKPG9Zi9wrmMg3iCN1ZMuCGGLenhTqk2AXsxnsoT3UYX7ijtVY1D7aZHBt1R3ZXAznKn4jUad17oNEM4mNp4UqzzzCa7h1cBgTqJdLLHatnp1ahnAgrY6rZFD9oCDRYYBQ67A5dhkbggLQhAGnTvwtS4Mf6jZxafh8i82RynVLgVahQiMFC2yvb9jbqyjkqXoR9xLRWHeaJuqX6VEySoKXqpMQzQTn5agkhfrJHyNAsQPVrTNfMPoe2P5uYs4z9yT2tyMBQSnHr4GSUcaUkrNpXT5JpgzeoABSne7H1wrEgDRomHc2UWJC3299z5ajkyaoWcGLP8xg5SCnQktq4herUyeWnYxz73whLsihsZLp6LCE94iPUSKV7PPnqTCYgejXCXsuUwe6jsBAbQCknLPdni4GrtJUZyuHYCe2Yd9Mjh4Qr3orHrViMog2YsZqHwnibrpLGtJrDoQTbaf35rpcuPBryFC8HGo4KqC3LwmKim8wg4YJ4m2Py5qew2zjoaNqxCnyHRsgfyCxFJFMtrPWHZ391P7JFDjWd27pps4E1hCmjf8Bt5vtKbW7Lutx2ACjFvFb",
         |            "assets": [
         |                {
         |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
         |                    "index": 0,
         |                    "amount": 10000,
         |                    "name": "Ergo_ErgoPOS_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                }
         |            ],
         |            "additionalRegisters": {},
         |            "spentTransactionId": "6c2106bee6af77659b85006cf4cf365b4c3b944928d8fe3ba695cdce14d2bb3e",
         |            "mainChain": true
         |        },
         |        {
         |            "boxId": "4324490fd85a948746bf4ae8d66672093953c96ba780e6245acb0c3347fc6f6f",
         |            "transactionId": "2f839ac334649bb8d0ba9717181948228c70e6a8082bfa920420a6fbaf7cec20",
         |            "blockId": "1409e7bf1c1551e4e5c0a04af8c1819accf99e4aae3fb7a09c03a854a7873a0b",
         |            "value": 2927500000,
         |            "index": 1,
         |            "globalIndex": 26335815,
         |            "creationHeight": 935193,
         |            "settlementHeight": 935202,
         |            "ergoTree": "0008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491",
         |            "address": "9iAYDwPWP7c9cKXB7P2bLivt75CBaXrbYoZrxpna1peB8cYFoHw",
         |            "assets": [
         |                {
         |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
         |                    "index": 0,
         |                    "amount": 24000000,
         |                    "name": "EPOS",
         |                    "decimals": 4,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
         |                    "index": 1,
         |                    "amount": 49954992,
         |                    "name": "Ergo_ErgoPOS_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                }
         |            ],
         |            "additionalRegisters": {},
         |            "spentTransactionId": "6c2106bee6af77659b85006cf4cf365b4c3b944928d8fe3ba695cdce14d2bb3e",
         |            "mainChain": true
         |        },
         |        {
         |            "boxId": "9b566cfca454e2e7889df6298584713c3c36da4c1957971a6514672b89c7f8ff",
         |            "transactionId": "2f839ac334649bb8d0ba9717181948228c70e6a8082bfa920420a6fbaf7cec20",
         |            "blockId": "1409e7bf1c1551e4e5c0a04af8c1819accf99e4aae3fb7a09c03a854a7873a0b",
         |            "value": 1250000,
         |            "index": 2,
         |            "globalIndex": 26335816,
         |            "creationHeight": 935193,
         |            "settlementHeight": 935202,
         |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
         |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
         |            "assets": [],
         |            "additionalRegisters": {},
         |            "spentTransactionId": "350af42647367cb1c8827b66890f8a599ab3db6377a717215a8c8f6dd852b1d5",
         |            "mainChain": true
         |        }
         |    ],
         |    "size": 867
         |}
         |""".stripMargin
    ).toOption.get.toTransaction

}
