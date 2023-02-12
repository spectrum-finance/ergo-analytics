package fi.spectrum.parser.lm.order.v1

import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.{AssetAmount, SErgoTree, TokenId}
import fi.spectrum.core.domain.order.Order.Deposit.LmDeposit.LmDepositV1
import fi.spectrum.core.domain.order.Order.Redeem.LmRedeem.LmRedeemV1
import fi.spectrum.core.domain.order.{LmDepositParams, PoolId}
import fi.spectrum.core.domain.order.Redeemer.ErgoTreeRedeemer
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.models.TransactionTest
import io.circe.parser.decode

object LM {

  def depositOrderEval =
    decode[TransactionTest](
      """
        |{
        |    "id": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
        |    "blockId": "fcc2c2a71eb1b1c5c0707e75d2ede46733782b14c5c247382ed8b0110871e0bd",
        |    "inclusionHeight": 937480,
        |    "timestamp": 1676051781005,
        |    "index": 4,
        |    "globalIndex": 4741226,
        |    "numConfirmations": 1913,
        |    "inputs": [
        |        {
        |            "boxId": "00738c0e3c4d89a1a2d5e71a20e4a00262340f289e58d9d866719564a933bad5",
        |            "value": 1250000,
        |            "index": 0,
        |            "spendingProof": null,
        |            "outputBlockId": "4c6f21c33c8e137b2a2b0194d7e720e47181f2637e0f7556f67981590ae661d7",
        |            "outputTransactionId": "f56f18d7cf8f2321dd8efd5fcad03d4f540122fed7f5f0bcaada1731ba80d397",
        |            "outputIndex": 0,
        |            "outputGlobalIndex": 26433065,
        |            "outputCreatedAt": 937208,
        |            "outputSettledAt": 937403,
        |            "ergoTree": "19c0062904000400040204020404040404060406040804080404040204000400040204020601010400040a050005000404040204020e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c0400040205000402040204060500050005feffffffffffffffff010502050005000402050005000100d820d601b2a5730000d602db63087201d603db6308a7d604b27203730100d605e4c6a70410d606e4c6a70505d607e4c6a70605d608b27202730200d609b27203730300d60ab27202730400d60bb27203730500d60cb27202730600d60db27203730700d60e8c720d01d60fb27202730800d610b27203730900d6118c721001d6128c720b02d613998c720a027212d6148c720902d615b27205730a00d6169a99a37215730bd617b27205730c00d6189d72167217d61995919e72167217730d9a7218730e7218d61ab27205730f00d61b7e721a05d61c9d7206721bd61d998c720c028c720d02d61e8c721002d61f998c720f02721ed6207310d1ededededed93b272027311007204ededed93e4c672010410720593e4c672010505720693e4c6720106057207928cc77201018cc7a70193c27201c2a7ededed938c7208018c720901938c720a018c720b01938c720c01720e938c720f01721193b172027312959172137313d802d6219c721399721ba273147e721905d622b2a5731500ededed929a997206721472079c7e9995907219721a72199a721a7316731705721c937213f0721d937221f0721fedededed93cbc272227318938602720e7213b2db6308722273190093860272117221b2db63087222731a00e6c67222040893e4c67222050e8c720401958f7213731bededec929a997206721472079c7e9995907219721a72199a721a731c731d05721c92a39a9a72159c721a7217b27205731e0093721df0721392721f95917219721a731f9c721d99721ba273207e721905d804d621e4c672010704d62299721a7221d6237e722205d62499997321721e9c9972127322722395ed917224732391721f7324edededed9072219972197325909972149c7223721c9a721c7207907ef0998c7208027214069a9d9c99997e7214069d9c7e7206067e7222067e721a0672207e721f067e7224067220937213732693721d73277328",
        |            "address": "2JowFguNN2xRcYi3GANwoCnk46sVvc2D6oT27A2xbj5DsDEj2TMJbZmR8iqtWCbobi7hEvhkXQeMo9sjGh5jdBo133aJxr7ZTcfeVk8zN1MMDRqP64ehC7BNYGarJ5eaHpEq6DahnLC2khXpqU7dvfCczuPXd9qioB32tNqdCk3b9NBGUuQxNqK6FrHbLVi99KvuN2Nz9bh8HEZFukDKymuDpHwXF5JjMUW2YGDQCBFmLgKGFrLrvC2iQH18ftfmQoqUS4GCXpdJ5YtPMg8bLZ4GNP5QaXZXEQpRTz5fgRu9FR2ZgmZ5r1tSQV789H3Lf6RcA3RmkWoTw8RMEMdwE45e7yoiWVTHqkFMLz2qEnYgmzSyxv7Uxk1f9KXfvWPf4CbMfi9y6txGXh5MNhzPKzRUuNS8VxScAt7JYfDCvp1KhyyFLWy9ijtDbmUw2MshWAubzt2dAJaH2hAMiv1rGi6QWG9ru6LvPTH7Tr1aUhLac6DK2p2ycBwk8hp417K7sU2aVzRfWhreEZxnCH2aGQvp8iq4PDkRsEjgUpMzCu4wJDXRa9v3gKoBP7N3xWV2xUg1xmMTPC1LNomT86k5vQvBZpM74gZ3RWmJTFoq6dwPtHNHC2hdyfuqZDpHz2ATcbem4wmG1QJPig92R36WKmydN53RujrA3pXccxGNHqMqPsu2YexSURoSihtJYsrdGG4332DdGappSnAgDeETYJ69GJ6giTiddVQv3oKzuF7tZwdVrFzFoUkSWEGqd4jnWRg46cqTqJqT5RH4MJw7FsCi6gZD1X39X1ANgv9vC6pNV6QqdJktpN9CoM2DbEyVMyYdhpgotexAR9u6KEBJmEbfps7jus9X97arMmzHRwJR99X37uj8zT5g36SAPPwDyueEAsW7r1RNuwm4DGPAmdxYBRgi5iri9TCHpAeDBLCPU9tycSVPviDrNxKWVYEus6jqZdNENPrTeA83T4SGdFKmA8aVyNKPtzvejRLEwYCjdLAAgoF7oGY5R6gq9caatH23rrEJutNCS4csAgm8QyrETfF1nv9KMrGvA9E577dBcNYKHdLjfVKHi7op3LyNTgYTc2DkYakdG94tJzPbPsgAetpFKs3SjWcUkhhgCm8JoiQ8iFDXG4kgADJ",
        |            "assets": [
        |                {
        |                    "tokenId": "0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f",
        |                    "index": 0,
        |                    "amount": 1,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
        |                    "index": 1,
        |                    "amount": 11000000,
        |                    "name": "EPOS",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 2,
        |                    "amount": 429849,
        |                    "name": "Ergo_ErgoPOS_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0",
        |                    "index": 3,
        |                    "amount": 9223372036854345959,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "bc58f841398f84fd0a72e2de50f8f3aed14e6234f7502bb7fe90729fba9c74f2",
        |                    "index": 4,
        |                    "amount": 9223372036853026415,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                }
        |            ],
        |            "additionalRegisters": {
        |                "R4": {
        |                    "serializedValue": "1004f4030a9cb672c801",
        |                    "sigmaType": "Coll[SInt]",
        |                    "renderedValue": "[250,5,937358,100]"
        |                },
        |                "R5": {
        |                    "serializedValue": "05fee2be0a",
        |                    "sigmaType": "SLong",
        |                    "renderedValue": "10999999"
        |                },
        |                "R6": {
        |                    "serializedValue": "05d00f",
        |                    "sigmaType": "SLong",
        |                    "renderedValue": "1000"
        |                }
        |            }
        |        },
        |        {
        |            "boxId": "55323416b5ebaa40e91428acf849d807d83a7c5f644fce7d14da780f507dfa7a",
        |            "value": 2750000,
        |            "index": 1,
        |            "spendingProof": null,
        |            "outputBlockId": "c14c5b8fe66ed18806fc568ac6254d9917c3605d0566b80e4f13f76371f3dabe",
        |            "outputTransactionId": "07125bda3ead0df3776de09749cd76ff97f8914663e11c88766aa5f3d931e0a5",
        |            "outputIndex": 0,
        |            "outputGlobalIndex": 26434675,
        |            "outputCreatedAt": 937457,
        |            "outputSettledAt": 937460,
        |            "ergoTree": "198d041604000e200f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f04020e240008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec0404040008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec040005fcffffffffffffffff0104000e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c04060400040804080402050204040e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f401d808d601b2a4730000d602db63087201d6037301d604b2a5730200d6057303d606c57201d607b2a5730400d6088cb2db6308a773050002eb027306d1ededed938cb27202730700017203ed93c27204720593860272067308b2db63087204730900ededededed93cbc27207730a93d0e4c672070408720593e4c67207050e72039386028cb27202730b00017208b2db63087207730c009386028cb27202730d00019c72087e730e05b2db63087207730f0093860272067310b2db6308720773110090b0ada5d90109639593c272097312c1720973137314d90109599a8c7209018c7209027315",
        |            "address": "TgkYJpD7df1quJhA1NULm4ojbZquBK5dN5o6fDmoWa12g6Y4dDo7vKCuPTv7Zhn1FnGyAsbiB1T6GwtVdBbK8ksZ8e3415maDECiffKRjrCooCyUuGf8q54rG93WU1pDzQHWNxDgSKx95itkhdiQ8mTq7qR6v3wXQRyfh1fQz23BKjLECP8jDP7jnzEeiNhvUJ28QKwD72j28K781pbWhSdSc6VbAZZyiFz3vXiTBCQbz5b3bgWHUhWmpFKrrE7P1NVUy3XV2bq4oJGEemLW8G3SaHqhaEaVtFUmMHT9Hmkje6HUUNMKEZgmJmUtB5PSihTH5eP6kw5g2QeKB1SvgfkSthU8eCmd4EZuMzUJbtHUxCUEF8GFptL3D512PpYEx33iw6zzgc85v3mFtYpxSPFJgW73QRH8gyf6XtWxbCVQwWxeGDarjpJ8wA45V1oFBL1v52Q9ZZ8fuMsi7YeAVrCV2XUHfAbL1pU6xx4GfSUM2v3ky89w7UCLrWE4vWByjJQa3mcfDXtAoiQDfZyyEeGeixE2FNCDxVVgPTNiNfGuuZY71sUwHdxxEqmYTW5uGQvowePpYzGkE5Jqknd9PigPk7r838FJbx8qSMwis4qFGt7f1wBubvJhiFBTCoZNEZDRoZXvy1D6EkT9aN8M39E4h2UoRyCGEoshXC1npgCBcSzohztgXx6j7TrSQ3JcsduAwerZhJ1xTwZKD6ysPZ8",
        |            "assets": [
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 0,
        |                    "amount": 444898,
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
        |            "boxId": "4bdbd9682c4023e573ce86343f95fc59c037e36ff06a81637dcf604145907397",
        |            "transactionId": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
        |            "blockId": "fcc2c2a71eb1b1c5c0707e75d2ede46733782b14c5c247382ed8b0110871e0bd",
        |            "value": 1250000,
        |            "index": 0,
        |            "globalIndex": 26435054,
        |            "creationHeight": 937478,
        |            "settlementHeight": 937480,
        |            "ergoTree": "19c0062904000400040204020404040404060406040804080404040204000400040204020601010400040a050005000404040204020e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c0400040205000402040204060500050005feffffffffffffffff010502050005000402050005000100d820d601b2a5730000d602db63087201d603db6308a7d604b27203730100d605e4c6a70410d606e4c6a70505d607e4c6a70605d608b27202730200d609b27203730300d60ab27202730400d60bb27203730500d60cb27202730600d60db27203730700d60e8c720d01d60fb27202730800d610b27203730900d6118c721001d6128c720b02d613998c720a027212d6148c720902d615b27205730a00d6169a99a37215730bd617b27205730c00d6189d72167217d61995919e72167217730d9a7218730e7218d61ab27205730f00d61b7e721a05d61c9d7206721bd61d998c720c028c720d02d61e8c721002d61f998c720f02721ed6207310d1ededededed93b272027311007204ededed93e4c672010410720593e4c672010505720693e4c6720106057207928cc77201018cc7a70193c27201c2a7ededed938c7208018c720901938c720a018c720b01938c720c01720e938c720f01721193b172027312959172137313d802d6219c721399721ba273147e721905d622b2a5731500ededed929a997206721472079c7e9995907219721a72199a721a7316731705721c937213f0721d937221f0721fedededed93cbc272227318938602720e7213b2db6308722273190093860272117221b2db63087222731a00e6c67222040893e4c67222050e8c720401958f7213731bededec929a997206721472079c7e9995907219721a72199a721a731c731d05721c92a39a9a72159c721a7217b27205731e0093721df0721392721f95917219721a731f9c721d99721ba273207e721905d804d621e4c672010704d62299721a7221d6237e722205d62499997321721e9c9972127322722395ed917224732391721f7324edededed9072219972197325909972149c7223721c9a721c7207907ef0998c7208027214069a9d9c99997e7214069d9c7e7206067e7222067e721a0672207e721f067e7224067220937213732693721d73277328",
        |            "address": "2JowFguNN2xRcYi3GANwoCnk46sVvc2D6oT27A2xbj5DsDEj2TMJbZmR8iqtWCbobi7hEvhkXQeMo9sjGh5jdBo133aJxr7ZTcfeVk8zN1MMDRqP64ehC7BNYGarJ5eaHpEq6DahnLC2khXpqU7dvfCczuPXd9qioB32tNqdCk3b9NBGUuQxNqK6FrHbLVi99KvuN2Nz9bh8HEZFukDKymuDpHwXF5JjMUW2YGDQCBFmLgKGFrLrvC2iQH18ftfmQoqUS4GCXpdJ5YtPMg8bLZ4GNP5QaXZXEQpRTz5fgRu9FR2ZgmZ5r1tSQV789H3Lf6RcA3RmkWoTw8RMEMdwE45e7yoiWVTHqkFMLz2qEnYgmzSyxv7Uxk1f9KXfvWPf4CbMfi9y6txGXh5MNhzPKzRUuNS8VxScAt7JYfDCvp1KhyyFLWy9ijtDbmUw2MshWAubzt2dAJaH2hAMiv1rGi6QWG9ru6LvPTH7Tr1aUhLac6DK2p2ycBwk8hp417K7sU2aVzRfWhreEZxnCH2aGQvp8iq4PDkRsEjgUpMzCu4wJDXRa9v3gKoBP7N3xWV2xUg1xmMTPC1LNomT86k5vQvBZpM74gZ3RWmJTFoq6dwPtHNHC2hdyfuqZDpHz2ATcbem4wmG1QJPig92R36WKmydN53RujrA3pXccxGNHqMqPsu2YexSURoSihtJYsrdGG4332DdGappSnAgDeETYJ69GJ6giTiddVQv3oKzuF7tZwdVrFzFoUkSWEGqd4jnWRg46cqTqJqT5RH4MJw7FsCi6gZD1X39X1ANgv9vC6pNV6QqdJktpN9CoM2DbEyVMyYdhpgotexAR9u6KEBJmEbfps7jus9X97arMmzHRwJR99X37uj8zT5g36SAPPwDyueEAsW7r1RNuwm4DGPAmdxYBRgi5iri9TCHpAeDBLCPU9tycSVPviDrNxKWVYEus6jqZdNENPrTeA83T4SGdFKmA8aVyNKPtzvejRLEwYCjdLAAgoF7oGY5R6gq9caatH23rrEJutNCS4csAgm8QyrETfF1nv9KMrGvA9E577dBcNYKHdLjfVKHi7op3LyNTgYTc2DkYakdG94tJzPbPsgAetpFKs3SjWcUkhhgCm8JoiQ8iFDXG4kgADJ",
        |            "assets": [
        |                {
        |                    "tokenId": "0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f",
        |                    "index": 0,
        |                    "amount": 1,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
        |                    "index": 1,
        |                    "amount": 11000000,
        |                    "name": "EPOS",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 2,
        |                    "amount": 874747,
        |                    "name": "Ergo_ErgoPOS_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0",
        |                    "index": 3,
        |                    "amount": 9223372036853901061,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "bc58f841398f84fd0a72e2de50f8f3aed14e6234f7502bb7fe90729fba9c74f2",
        |                    "index": 4,
        |                    "amount": 9223372036851246823,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                }
        |            ],
        |            "additionalRegisters": {
        |                "R4": {
        |                    "serializedValue": "1004f4030a9cb672c801",
        |                    "sigmaType": "Coll[SInt]",
        |                    "renderedValue": "[250,5,937358,100]"
        |                },
        |                "R5": {
        |                    "serializedValue": "05fee2be0a",
        |                    "sigmaType": "SLong",
        |                    "renderedValue": "10999999"
        |                },
        |                "R6": {
        |                    "serializedValue": "05d00f",
        |                    "sigmaType": "SLong",
        |                    "renderedValue": "1000"
        |                }
        |            },
        |            "spentTransactionId": "c952523a2e9651cf6d440109553430a11c31c28baeb48dc84adce3ab05404e11",
        |            "mainChain": true
        |        },
        |        {
        |            "boxId": "ae4822ba1ae63a49bdcec712803c164c5c22086dd7b421699befe1c4637ccc39",
        |            "transactionId": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
        |            "blockId": "fcc2c2a71eb1b1c5c0707e75d2ede46733782b14c5c247382ed8b0110871e0bd",
        |            "value": 250000,
        |            "index": 1,
        |            "globalIndex": 26435055,
        |            "creationHeight": 937478,
        |            "settlementHeight": 937480,
        |            "ergoTree": "0008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec",
        |            "address": "9hp1xXVF8VXkYEHdgvTJK7XSEW1vckcKqWx8JTHsAwwGzHH9hxq",
        |            "assets": [
        |                {
        |                    "tokenId": "00738c0e3c4d89a1a2d5e71a20e4a00262340f289e58d9d866719564a933bad5",
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
        |            "boxId": "5c814a77bb326120e5b43d62d5c0631970c09cc1cc2e144febd58adf71e3bce9",
        |            "transactionId": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
        |            "blockId": "fcc2c2a71eb1b1c5c0707e75d2ede46733782b14c5c247382ed8b0110871e0bd",
        |            "value": 300000,
        |            "index": 2,
        |            "globalIndex": 26435056,
        |            "creationHeight": 937478,
        |            "settlementHeight": 937480,
        |            "ergoTree": "19a3041f040004000404040404000402060101040005000402040404020400040004020502040405020402040005feffffffffffffffff010408050205000404040004060404040205fcffffffffffffffff010100d80cd601b2a5730000d602db63087201d603e4c6a7050ed604b2a4730100d605db63087204d6068cb2720573020002d607998cb27202730300027206d608e4c6a70408d609db6308a7d60ab27209730400d60bb27205730500d60c7306d1ed938cb27202730700017203959372077308d80cd60db2a5e4e3000400d60eb2a5e4e3010400d60fb2e4c672040410730900d610c672010804d61199720f95e67210e47210e4c672010704d6128cb27209730a0001d613db6308720ed614b27209730b00d6158c721402d6167e721105d6179972159c72168c720a02d618b2db6308720d730c00eded93c2720dd07208edededed93e4c6720e0408720893e4c6720e050e720393c2720ec2a795917211730dd801d619b27213730e00eded9386027212730fb27213731000938c7219018c721401939972158c721902721793860272127311b2721373120093b27213731300720aed938c7218018c720b01927e8c7218020699999d9c99997e8c720b02069d9c7ee4c672040505067e7211067e720f06720c7e7217067e999973148cb27205731500029c9972067316721606720c720c958f7207731793b2db6308b2a473180073190086029593b17209731a8cb27209731b00018cb27209731c0001731d731e",
        |            "address": "YyLFSfjMQw5AJEuchDNPzziDH7CpLX8EbtGbddMpTpDoYYCc5xKJUbDuFnwsJ4qcpARjLmTQdRUTDiftnkb4kuNAaM7enwfxbWvh9SAbpvxAtJzhQApmedrEH9BBrPYzHdWbR354ZXZJkazhvfWMLyijyFeJ7aTDYmhDn8YvF3HeBMJiLByT32wTfNVEWQf4SEp3eN66ppqZs7bJAxrsZo3PDGJgTtZ2qPypMv3x9fcE6zYoLiCzHbAWC8WCfn5wNno9NAwSgWgHKcz3Gx4kXAe5DJnMDZJeiAuckS4e8J5sUfgFSPHTysvkfPw5pxBBNoxrsQ8y2jHSjB3KhSKKsNoZLnWZpbLQ7TurFc34TZJn9FNsRzpmQc9ftcZNa1QxKNoBtFzCpRXwvKd1RaSEZc5er7TwiyL9ebcfh1x3b3k2qXZhi6eXnktGBhEcNiJoJe7FdRA7gTXs9MCQewX8r7u3ay6i94bT9BrdXZy7xnmFU1DvyCcNP3sHHC1gDbNxdzVYsyF9tuxuXhmmD3rTc7hCPX4uo1dahq5HFaaQifp8kwe6iVuzp3Qo5vNPgV8tJBzXhGx7wgYoozGznh1ecYdBtCpHdEwzbME4bQy8qLXsBRB7TVnhQ2GYUL8XyCpzaziWEe1tdKrgbFFE1CNnLuGR7FEFikx5PfJVFTmFvpEvvtM7Qq9TMFjgsaoP58M7BajhU74EgqVRX3hi8pmmzxnjAzVKK5wZR62RcpowjrYsNLGjcWL8B",
        |            "assets": [
        |                {
        |                    "tokenId": "ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0",
        |                    "index": 0,
        |                    "amount": 444898,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "bc58f841398f84fd0a72e2de50f8f3aed14e6234f7502bb7fe90729fba9c74f2",
        |                    "index": 1,
        |                    "amount": 1779592,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "00738c0e3c4d89a1a2d5e71a20e4a00262340f289e58d9d866719564a933bad5",
        |                    "index": 2,
        |                    "amount": 1,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                }
        |            ],
        |            "additionalRegisters": {
        |                "R4": {
        |                    "serializedValue": "08cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec",
        |                    "sigmaType": "SSigmaProp",
        |                    "renderedValue": "03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec"
        |                },
        |                "R5": {
        |                    "serializedValue": "0e200f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f",
        |                    "sigmaType": "Coll[SByte]",
        |                    "renderedValue": "0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f"
        |                }
        |            },
        |            "spentTransactionId": "4fffc1562aa9f2af3322fba8284489dab58d3e488d1b381f475e586fd0001a24",
        |            "mainChain": true
        |        },
        |        {
        |            "boxId": "9f8c944de15aa93a521a3d10fc9c2e45bea29640f2a0f48cd50cf69a9653b585",
        |            "transactionId": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
        |            "blockId": "fcc2c2a71eb1b1c5c0707e75d2ede46733782b14c5c247382ed8b0110871e0bd",
        |            "value": 1200000,
        |            "index": 3,
        |            "globalIndex": 26435057,
        |            "creationHeight": 937478,
        |            "settlementHeight": 937480,
        |            "ergoTree": "0008cd02d6b2141c21e4f337e9b065a031a6269fb5a49253094fc6243d38662eb765db00",
        |            "address": "9g9cdHhNZvtUvMveqEEfk28JZasEC8sJamV3E6d5JHv8VYUjjbX",
        |            "assets": [],
        |            "additionalRegisters": {},
        |            "spentTransactionId": null,
        |            "mainChain": true
        |        },
        |        {
        |            "boxId": "c787cdd83ec1c3e1f44e4a336b8536c051de3c9df9c5d2e017fcf942490b3027",
        |            "transactionId": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
        |            "blockId": "fcc2c2a71eb1b1c5c0707e75d2ede46733782b14c5c247382ed8b0110871e0bd",
        |            "value": 1000000,
        |            "index": 4,
        |            "globalIndex": 26435058,
        |            "creationHeight": 937478,
        |            "settlementHeight": 937480,
        |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
        |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
        |            "assets": [],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "fd5605035149345f4311a29b9abb81848a8a7346beebc75469bb128f41e9916f",
        |            "mainChain": true
        |        }
        |    ],
        |    "size": 2004
        |}
        |""".stripMargin
    ).toOption.get.toTransaction

  def depositOrderRegister =
    decode[TransactionTest](
      """
        |{
        |    "id": "07125bda3ead0df3776de09749cd76ff97f8914663e11c88766aa5f3d931e0a5",
        |    "blockId": "c14c5b8fe66ed18806fc568ac6254d9917c3605d0566b80e4f13f76371f3dabe",
        |    "inclusionHeight": 937460,
        |    "timestamp": 1676050033276,
        |    "index": 1,
        |    "globalIndex": 4741140,
        |    "numConfirmations": 1934,
        |    "inputs": [
        |        {
        |            "boxId": "a7f9e9e9b5af84fecc05294f31448fbf85b62dc9de70a521c577fc0772c4dc68",
        |            "value": 766244297,
        |            "index": 0,
        |            "spendingProof": "d6163d8ab74d79d7e3b4f8bb04e3e58d1ce98564594a2db22c1caead0ce744248e3986b624f597e6d63c2fa40cb02d6909308c6365e14743",
        |            "outputBlockId": "acc165286ff698f48c889bf78241974181c3430f856db604d8c7e6ebad5a5521",
        |            "outputTransactionId": "c710e0ca33ee7965ccbfdfede1de4f6c81ff827a8adff8d36db2ef10e9749fd1",
        |            "outputIndex": 1,
        |            "outputGlobalIndex": 26366981,
        |            "outputCreatedAt": 935937,
        |            "outputSettledAt": 935939,
        |            "ergoTree": "0008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec",
        |            "address": "9hp1xXVF8VXkYEHdgvTJK7XSEW1vckcKqWx8JTHsAwwGzHH9hxq",
        |            "assets": [
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 0,
        |                    "amount": 44489863,
        |                    "name": "Ergo_ErgoPOS_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
        |                    "index": 1,
        |                    "amount": 71,
        |                    "name": "SigUSD",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
        |                    "index": 2,
        |                    "amount": 1781494,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "e4a1031e24410208afbcd359648b51f28c4418147e05be6810c3a075537edd85",
        |                    "index": 3,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "f6bff4585a280b2569e2a2b8db2257bea04bf550df0ae6167997b24818ceb02b",
        |                    "index": 4,
        |                    "amount": 100000000000,
        |                    "name": "Test Spectrum Finance Test Unlock whitelist token",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "aab6dc7ae120b0a45a85ff03916d305eb25214de51e5223e8f5dcd6a834488bb",
        |                    "index": 5,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "b21377ae6fabbfbe008c9027589da19e4225e3192569e79ae70af0ec10fd5273",
        |                    "index": 6,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "31c438a026bd8e2c90e604bc6f680f01887e09b851307c42bfd5a1fdae5be6bd",
        |                    "index": 7,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "ae7af4a252179519c91b236add24913ba11fb5cc8722689e80472cd7828f2b02",
        |                    "index": 8,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "109873d1b385069eac47a7703f6d92f122df436e8615fa779f24532b555bb68e",
        |                    "index": 9,
        |                    "amount": 8830521,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "3fbaa8c7e5bd352eeff5ee99dca8caceb3495ec4a5bc51dafb61e1c605e8eb36",
        |                    "index": 10,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c8fd07ac08dfafb6e55e2df54de1c6ce05ca77310b4c9fccb262079511c8d9c0",
        |                    "index": 11,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "928676dea56ad57096bb73953e551bb6b4b0cedfe65e0f32a2ebe5978cefaacb",
        |                    "index": 12,
        |                    "amount": 1000000000,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "01dce8a5632d19799950ff90bca3b5d0ca3ebfa8aaafd06f0cc6dd1e97150e7f",
        |                    "index": 13,
        |                    "amount": 60390,
        |                    "name": "CYPX",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "beea40bf9ec7bd000a9eec4a9f19aaad6040ad6f5dd072be2c66e739860520ce",
        |                    "index": 14,
        |                    "amount": 1,
        |                    "name": "ergopad Stake Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "f45c4f0d95ce1c64defa607d94717a9a30c00fdd44827504be20db19f4dce36f",
        |                    "index": 15,
        |                    "amount": 50000,
        |                    "name": "TERG",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "2278724d4c1e69d497eb81d428e3261824312f2bc5b9ba3733f26afb27b52de4",
        |                    "index": 16,
        |                    "amount": 316227766,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "45f3a9798a4571c1566101b71efeecf3f57feb403689d25a23557c67d982bbe2",
        |                    "index": 17,
        |                    "amount": 316227766,
        |                    "name": "WT_ADA_WT_ERG_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c2d9f292506cbc93421ad88ec7edbede5642a1964adbcc5e16333cb8126b9b97",
        |                    "index": 18,
        |                    "amount": 1,
        |                    "name": "ergopad Stake Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "5a34d53ca483924b9a6aa0c771f11888881b516a8d1a9cdc535d063fe26d065e",
        |                    "index": 19,
        |                    "amount": 56708287,
        |                    "name": "LunaDog",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "46bd8c30598aa8fe6237722d5f32f56ad7aab43a3668dcc3cbde97d539e41e95",
        |                    "index": 20,
        |                    "amount": 810881524091,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "592e6d035c17ae80e0e94ef2de6e9ad7d04fa41a798ee05ce2a90d33461f0ed1",
        |                    "index": 21,
        |                    "amount": 316227766,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "246360521aed0dbc852bc38fcd068aafe4725fd4a17da15f723bade8276e017d",
        |                    "index": 22,
        |                    "amount": 316227766,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c319c2671c6e67f2b0e0aa5b03d2f023cdf8abb654f3b47f0493b6a377bcabd1",
        |                    "index": 23,
        |                    "amount": 3162,
        |                    "name": "Ergo_SigmaUSD_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e8b20745ee9d18817305f32eb21015831a48f02d40980de6e849f886dca7f807",
        |                    "index": 24,
        |                    "amount": 5422683,
        |                    "name": "Flux",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "cddbf2a3e52a66e56d6e9a1b5f05502c5a6870f3e7a6142e4f4b044895244364",
        |                    "index": 25,
        |                    "amount": 100000000,
        |                    "name": "Hello Community whitelist token",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c93a7dbc2e08ca359074b0f2c66f0d0c62b3d1adb4497c6bd81cc0dd2890052d",
        |                    "index": 26,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "240ec0fa648d1c5ec26ae9c9afd12e323347ba468b41dd37f5143a75053a6fef",
        |                    "index": 27,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "1f6bc0b7c1b89a42ce8b80330d49bf4fb1cd06a85ec74d60c1d8e8e05a21a42d",
        |                    "index": 28,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "498dd9e14a777f9a3b4a4669e292ae572feba186918e289dae2dd9fc89be9c40",
        |                    "index": 29,
        |                    "amount": 3162,
        |                    "name": "Ergo_SigmaUSD_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "7a6c3b36834663baaf82ae75c2c073e064215eb965a8cdb5e6d4269c960d3842",
        |                    "index": 30,
        |                    "amount": 1,
        |                    "name": "$COMET Copper",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "cf8d83a2803b99f4fbb596b29014a1e6b1e31c7d0817f2767617f1f6b1438a1e",
        |                    "index": 31,
        |                    "amount": 75751,
        |                    "name": "ERG_Ergopad_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "3c8f660644c502e88eda26cc82b1e2093634c4f048c9e4033420501fbc5d2f1a",
        |                    "index": 32,
        |                    "amount": 316227766,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "98da76cecb772029cfec3d53727d5ff37d5875691825fbba743464af0c89ce45",
        |                    "index": 33,
        |                    "amount": 13940381,
        |                    "name": "ERG_Mi Goreng _LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
        |                    "index": 34,
        |                    "amount": 2120924823032,
        |                    "name": "WT_ADA",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "007fd64d1ee54d78dd269c8930a38286caa28d3f29d27cadcb796418ab15c283",
        |                    "index": 35,
        |                    "amount": 7502,
        |                    "name": "EXLE",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "3b9fae1b6151ee5986a1531518582e2f66a315452f96604739ea02646e3cee65",
        |                    "index": 36,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "22ccd7c0ce393bd8637fdac8a0238807a4cbf9b7950732a8c37fdf3ae7929bc9",
        |                    "index": 37,
        |                    "amount": 17141898646024,
        |                    "name": "Test Spectrum Finance Community Round whitelist token",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "17c21d0dcde99d552a6430c9d96fae3247d88bc484306333060b46e2af7e953f",
        |                    "index": 38,
        |                    "amount": 9223372036854774807,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "dca5ced7c814761c9d0a75509d884e46e971ffaaa5497f4bf9476f4f35cfeb56",
        |                    "index": 39,
        |                    "amount": 9223372036854774807,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "f35e7302e09925c36103e63c9fd25030d78ab173ab2840115cfb53d2571c8b0e",
        |                    "index": 40,
        |                    "amount": 20000000000000,
        |                    "name": "Test Spectrum Finance Public Round whitelist token",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "5e3247418e2ac9c43819790287e554434cd35a74fad7682f2f8b0254fbdca072",
        |                    "index": 41,
        |                    "amount": 1000000000,
        |                    "name": "OMG",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
        |                    "index": 42,
        |                    "amount": 2710531558962,
        |                    "name": "WT_ERG",
        |                    "decimals": 9,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "1fd6e032e8476c4aa54c18c1a308dce83940e8f4a28f576440513ed7326ad489",
        |                    "index": 43,
        |                    "amount": 28477,
        |                    "name": "Paideia",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "3bdbba60cbc6d78d6ed11fb7f64d7fd604898eb439dd833d003650bee3097b3c",
        |                    "index": 44,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "9db3450951c37a5875ed493f19bfe03364fa4846cde63b1fee05dfaf59c837b1",
        |                    "index": 45,
        |                    "amount": 264394220,
        |                    "name": "ERG_LunaDog_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "f3b50913a649ece68b4c2ab345483820500e188a1098c2e259cd3eaf97065927",
        |                    "index": 46,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "36aba4b4a97b65be491cf9f5ca57b5408b0da8d0194f30ec8330d1e8946161c1",
        |                    "index": 47,
        |                    "amount": 2,
        |                    "name": "Erdoge",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
        |                    "index": 48,
        |                    "amount": 132414,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "7ec36582b469066179fc105b3f0f4edc876cf3576a755de441f74a4da1a07a2a",
        |                    "index": 49,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "da704a3e02307bad6fc1c7545bd9fc42e7fd6b97da7d0deb2dee760e4cc7be6d",
        |                    "index": 50,
        |                    "amount": 365148369,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "be92c018c6cf82cc7d56500b80c19fa5f5e5011c6a97e9ab50668ec8c61d3dc1",
        |                    "index": 51,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Community Round Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e249780a22e14279357103749102d0a7033e0459d10b7f277356522ae9df779c",
        |                    "index": 52,
        |                    "amount": 76829252,
        |                    "name": "ERG_NETA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "4d46c7d7a0ec8dd89d90424fab0442354bca3793135e115a4c9de922fdd51daa",
        |                    "index": 53,
        |                    "amount": 632455532,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "27a9c163d2d9acef611e7354f5dee8aaedbf4dd82c3e819d6f6bf58b3c54acc2",
        |                    "index": 54,
        |                    "amount": 50000000,
        |                    "name": "Hello Staker whitelist token",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "1f48fbe220d51cace0dbc9649bc29c929367191cbdfda4819250e9b53492111e",
        |                    "index": 55,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "6a928dad5999caeaf08396f9a40c37b1b09ce2b972df457f4afacb61ff69009a",
        |                    "index": 56,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "2f7070c00bc89fbbf190eae5069f8bc73216865452ed87a71f7ca3b385af2d29",
        |                    "index": 57,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "bdfe633145d9e5e8232cd14ff0c40f7d278b81bf987915edbde4e67d28ab2a7a",
        |                    "index": 58,
        |                    "amount": 223606797,
        |                    "name": "WT_ADA_WT_ERG_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40",
        |                    "index": 59,
        |                    "amount": 2119,
        |                    "name": "kushti",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "19e5b066e46094f99c39564208995fc0544f7a452082e2aaa1b5b090a7f104b9",
        |                    "index": 60,
        |                    "amount": 9223372036854774807,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
        |                    "index": 61,
        |                    "amount": 40000,
        |                    "name": "ergopad",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "a4d9964b4cca1b97d3d03047900a41b5de044d02a8357d0120cf2bc11c68cbdc",
        |                    "index": 62,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "d60d1836dacc7603ec9095d735dcdb3efbe9a5ab348726c3ed115cf85f314917",
        |                    "index": 63,
        |                    "amount": 223606797,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "18c938e1924fc3eadc266e75ec02d81fe73b56e4e9f4e268dffffcb30387c42d",
        |                    "index": 64,
        |                    "amount": 30392,
        |                    "name": "AHT",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "4d3a47d770d9f15111026d8a22e1f7d09d9465b1259db5c2279545c7bcee9870",
        |                    "index": 65,
        |                    "amount": 4285714000000,
        |                    "name": "Test Spectrum Finance Staker Round whitelist token",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "8b6c0c3b2dacf3a4fa68aedd8e66e513552b575fd0d7a6fc8feab249b3ced1d4",
        |                    "index": 66,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "41cd36f005170db748c305ca721780fd092dc2d31b5a02ab2cdca80c447ecafc",
        |                    "index": 67,
        |                    "amount": 9223372036854775807,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "7c483c8460fc3afaa14d570e88068022039e61f9b32fa68295c08b4c5c035696",
        |                    "index": 68,
        |                    "amount": 182574184,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "cc2726a5bb4a2132c99bb69be6f1d9a8bf7b09ad43db51bb6544b3e7db450f1f",
        |                    "index": 69,
        |                    "amount": 3850199989,
        |                    "name": "Eamd",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e0588d273c8183865cff31b3bfa766bc7b178e2362b45497b67e79662e3615b7",
        |                    "index": 70,
        |                    "amount": 54,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                }
        |            ],
        |            "additionalRegisters": {}
        |        }
        |    ],
        |    "dataInputs": [],
        |    "outputs": [
        |        {
        |            "boxId": "55323416b5ebaa40e91428acf849d807d83a7c5f644fce7d14da780f507dfa7a",
        |            "transactionId": "07125bda3ead0df3776de09749cd76ff97f8914663e11c88766aa5f3d931e0a5",
        |            "blockId": "c14c5b8fe66ed18806fc568ac6254d9917c3605d0566b80e4f13f76371f3dabe",
        |            "value": 2750000,
        |            "index": 0,
        |            "globalIndex": 26434675,
        |            "creationHeight": 937457,
        |            "settlementHeight": 937460,
        |            "ergoTree": "198d041604000e200f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f04020e240008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec0404040008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec040005fcffffffffffffffff0104000e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c04060400040804080402050204040e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f401d808d601b2a4730000d602db63087201d6037301d604b2a5730200d6057303d606c57201d607b2a5730400d6088cb2db6308a773050002eb027306d1ededed938cb27202730700017203ed93c27204720593860272067308b2db63087204730900ededededed93cbc27207730a93d0e4c672070408720593e4c67207050e72039386028cb27202730b00017208b2db63087207730c009386028cb27202730d00019c72087e730e05b2db63087207730f0093860272067310b2db6308720773110090b0ada5d90109639593c272097312c1720973137314d90109599a8c7209018c7209027315",
        |            "address": "TgkYJpD7df1quJhA1NULm4ojbZquBK5dN5o6fDmoWa12g6Y4dDo7vKCuPTv7Zhn1FnGyAsbiB1T6GwtVdBbK8ksZ8e3415maDECiffKRjrCooCyUuGf8q54rG93WU1pDzQHWNxDgSKx95itkhdiQ8mTq7qR6v3wXQRyfh1fQz23BKjLECP8jDP7jnzEeiNhvUJ28QKwD72j28K781pbWhSdSc6VbAZZyiFz3vXiTBCQbz5b3bgWHUhWmpFKrrE7P1NVUy3XV2bq4oJGEemLW8G3SaHqhaEaVtFUmMHT9Hmkje6HUUNMKEZgmJmUtB5PSihTH5eP6kw5g2QeKB1SvgfkSthU8eCmd4EZuMzUJbtHUxCUEF8GFptL3D512PpYEx33iw6zzgc85v3mFtYpxSPFJgW73QRH8gyf6XtWxbCVQwWxeGDarjpJ8wA45V1oFBL1v52Q9ZZ8fuMsi7YeAVrCV2XUHfAbL1pU6xx4GfSUM2v3ky89w7UCLrWE4vWByjJQa3mcfDXtAoiQDfZyyEeGeixE2FNCDxVVgPTNiNfGuuZY71sUwHdxxEqmYTW5uGQvowePpYzGkE5Jqknd9PigPk7r838FJbx8qSMwis4qFGt7f1wBubvJhiFBTCoZNEZDRoZXvy1D6EkT9aN8M39E4h2UoRyCGEoshXC1npgCBcSzohztgXx6j7TrSQ3JcsduAwerZhJ1xTwZKD6ysPZ8",
        |            "assets": [
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 0,
        |                    "amount": 444898,
        |                    "name": "Ergo_ErgoPOS_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
        |            "mainChain": true
        |        },
        |        {
        |            "boxId": "0e22dd337b766e0f3ca2e2e45e22c60b53231f7705a6e5b05d40d3137b0cc2c4",
        |            "transactionId": "07125bda3ead0df3776de09749cd76ff97f8914663e11c88766aa5f3d931e0a5",
        |            "blockId": "c14c5b8fe66ed18806fc568ac6254d9917c3605d0566b80e4f13f76371f3dabe",
        |            "value": 761494297,
        |            "index": 1,
        |            "globalIndex": 26434676,
        |            "creationHeight": 937457,
        |            "settlementHeight": 937460,
        |            "ergoTree": "0008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec",
        |            "address": "9hp1xXVF8VXkYEHdgvTJK7XSEW1vckcKqWx8JTHsAwwGzHH9hxq",
        |            "assets": [
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 0,
        |                    "amount": 44044965,
        |                    "name": "Ergo_ErgoPOS_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
        |                    "index": 1,
        |                    "amount": 71,
        |                    "name": "SigUSD",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
        |                    "index": 2,
        |                    "amount": 1781494,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "e4a1031e24410208afbcd359648b51f28c4418147e05be6810c3a075537edd85",
        |                    "index": 3,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "f6bff4585a280b2569e2a2b8db2257bea04bf550df0ae6167997b24818ceb02b",
        |                    "index": 4,
        |                    "amount": 100000000000,
        |                    "name": "Test Spectrum Finance Test Unlock whitelist token",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "aab6dc7ae120b0a45a85ff03916d305eb25214de51e5223e8f5dcd6a834488bb",
        |                    "index": 5,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "b21377ae6fabbfbe008c9027589da19e4225e3192569e79ae70af0ec10fd5273",
        |                    "index": 6,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "31c438a026bd8e2c90e604bc6f680f01887e09b851307c42bfd5a1fdae5be6bd",
        |                    "index": 7,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "ae7af4a252179519c91b236add24913ba11fb5cc8722689e80472cd7828f2b02",
        |                    "index": 8,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "109873d1b385069eac47a7703f6d92f122df436e8615fa779f24532b555bb68e",
        |                    "index": 9,
        |                    "amount": 8830521,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "3fbaa8c7e5bd352eeff5ee99dca8caceb3495ec4a5bc51dafb61e1c605e8eb36",
        |                    "index": 10,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c8fd07ac08dfafb6e55e2df54de1c6ce05ca77310b4c9fccb262079511c8d9c0",
        |                    "index": 11,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "928676dea56ad57096bb73953e551bb6b4b0cedfe65e0f32a2ebe5978cefaacb",
        |                    "index": 12,
        |                    "amount": 1000000000,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "01dce8a5632d19799950ff90bca3b5d0ca3ebfa8aaafd06f0cc6dd1e97150e7f",
        |                    "index": 13,
        |                    "amount": 60390,
        |                    "name": "CYPX",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "beea40bf9ec7bd000a9eec4a9f19aaad6040ad6f5dd072be2c66e739860520ce",
        |                    "index": 14,
        |                    "amount": 1,
        |                    "name": "ergopad Stake Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "f45c4f0d95ce1c64defa607d94717a9a30c00fdd44827504be20db19f4dce36f",
        |                    "index": 15,
        |                    "amount": 50000,
        |                    "name": "TERG",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "2278724d4c1e69d497eb81d428e3261824312f2bc5b9ba3733f26afb27b52de4",
        |                    "index": 16,
        |                    "amount": 316227766,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "45f3a9798a4571c1566101b71efeecf3f57feb403689d25a23557c67d982bbe2",
        |                    "index": 17,
        |                    "amount": 316227766,
        |                    "name": "WT_ADA_WT_ERG_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c2d9f292506cbc93421ad88ec7edbede5642a1964adbcc5e16333cb8126b9b97",
        |                    "index": 18,
        |                    "amount": 1,
        |                    "name": "ergopad Stake Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "5a34d53ca483924b9a6aa0c771f11888881b516a8d1a9cdc535d063fe26d065e",
        |                    "index": 19,
        |                    "amount": 56708287,
        |                    "name": "LunaDog",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "46bd8c30598aa8fe6237722d5f32f56ad7aab43a3668dcc3cbde97d539e41e95",
        |                    "index": 20,
        |                    "amount": 810881524091,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "592e6d035c17ae80e0e94ef2de6e9ad7d04fa41a798ee05ce2a90d33461f0ed1",
        |                    "index": 21,
        |                    "amount": 316227766,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "246360521aed0dbc852bc38fcd068aafe4725fd4a17da15f723bade8276e017d",
        |                    "index": 22,
        |                    "amount": 316227766,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c319c2671c6e67f2b0e0aa5b03d2f023cdf8abb654f3b47f0493b6a377bcabd1",
        |                    "index": 23,
        |                    "amount": 3162,
        |                    "name": "Ergo_SigmaUSD_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e8b20745ee9d18817305f32eb21015831a48f02d40980de6e849f886dca7f807",
        |                    "index": 24,
        |                    "amount": 5422683,
        |                    "name": "Flux",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "cddbf2a3e52a66e56d6e9a1b5f05502c5a6870f3e7a6142e4f4b044895244364",
        |                    "index": 25,
        |                    "amount": 100000000,
        |                    "name": "Hello Community whitelist token",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c93a7dbc2e08ca359074b0f2c66f0d0c62b3d1adb4497c6bd81cc0dd2890052d",
        |                    "index": 26,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "240ec0fa648d1c5ec26ae9c9afd12e323347ba468b41dd37f5143a75053a6fef",
        |                    "index": 27,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "1f6bc0b7c1b89a42ce8b80330d49bf4fb1cd06a85ec74d60c1d8e8e05a21a42d",
        |                    "index": 28,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "498dd9e14a777f9a3b4a4669e292ae572feba186918e289dae2dd9fc89be9c40",
        |                    "index": 29,
        |                    "amount": 3162,
        |                    "name": "Ergo_SigmaUSD_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "7a6c3b36834663baaf82ae75c2c073e064215eb965a8cdb5e6d4269c960d3842",
        |                    "index": 30,
        |                    "amount": 1,
        |                    "name": "$COMET Copper",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "cf8d83a2803b99f4fbb596b29014a1e6b1e31c7d0817f2767617f1f6b1438a1e",
        |                    "index": 31,
        |                    "amount": 75751,
        |                    "name": "ERG_Ergopad_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "3c8f660644c502e88eda26cc82b1e2093634c4f048c9e4033420501fbc5d2f1a",
        |                    "index": 32,
        |                    "amount": 316227766,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "98da76cecb772029cfec3d53727d5ff37d5875691825fbba743464af0c89ce45",
        |                    "index": 33,
        |                    "amount": 13940381,
        |                    "name": "ERG_Mi Goreng _LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
        |                    "index": 34,
        |                    "amount": 2120924823032,
        |                    "name": "WT_ADA",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "007fd64d1ee54d78dd269c8930a38286caa28d3f29d27cadcb796418ab15c283",
        |                    "index": 35,
        |                    "amount": 7502,
        |                    "name": "EXLE",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "3b9fae1b6151ee5986a1531518582e2f66a315452f96604739ea02646e3cee65",
        |                    "index": 36,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "22ccd7c0ce393bd8637fdac8a0238807a4cbf9b7950732a8c37fdf3ae7929bc9",
        |                    "index": 37,
        |                    "amount": 17141898646024,
        |                    "name": "Test Spectrum Finance Community Round whitelist token",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "17c21d0dcde99d552a6430c9d96fae3247d88bc484306333060b46e2af7e953f",
        |                    "index": 38,
        |                    "amount": 9223372036854774807,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "dca5ced7c814761c9d0a75509d884e46e971ffaaa5497f4bf9476f4f35cfeb56",
        |                    "index": 39,
        |                    "amount": 9223372036854774807,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "f35e7302e09925c36103e63c9fd25030d78ab173ab2840115cfb53d2571c8b0e",
        |                    "index": 40,
        |                    "amount": 20000000000000,
        |                    "name": "Test Spectrum Finance Public Round whitelist token",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "5e3247418e2ac9c43819790287e554434cd35a74fad7682f2f8b0254fbdca072",
        |                    "index": 41,
        |                    "amount": 1000000000,
        |                    "name": "OMG",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
        |                    "index": 42,
        |                    "amount": 2710531558962,
        |                    "name": "WT_ERG",
        |                    "decimals": 9,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "1fd6e032e8476c4aa54c18c1a308dce83940e8f4a28f576440513ed7326ad489",
        |                    "index": 43,
        |                    "amount": 28477,
        |                    "name": "Paideia",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "3bdbba60cbc6d78d6ed11fb7f64d7fd604898eb439dd833d003650bee3097b3c",
        |                    "index": 44,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "9db3450951c37a5875ed493f19bfe03364fa4846cde63b1fee05dfaf59c837b1",
        |                    "index": 45,
        |                    "amount": 264394220,
        |                    "name": "ERG_LunaDog_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "f3b50913a649ece68b4c2ab345483820500e188a1098c2e259cd3eaf97065927",
        |                    "index": 46,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "36aba4b4a97b65be491cf9f5ca57b5408b0da8d0194f30ec8330d1e8946161c1",
        |                    "index": 47,
        |                    "amount": 2,
        |                    "name": "Erdoge",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
        |                    "index": 48,
        |                    "amount": 132414,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "7ec36582b469066179fc105b3f0f4edc876cf3576a755de441f74a4da1a07a2a",
        |                    "index": 49,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Test Unlock Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "da704a3e02307bad6fc1c7545bd9fc42e7fd6b97da7d0deb2dee760e4cc7be6d",
        |                    "index": 50,
        |                    "amount": 365148369,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "be92c018c6cf82cc7d56500b80c19fa5f5e5011c6a97e9ab50668ec8c61d3dc1",
        |                    "index": 51,
        |                    "amount": 1,
        |                    "name": "Test Spectrum Finance Community Round Vesting Key",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e249780a22e14279357103749102d0a7033e0459d10b7f277356522ae9df779c",
        |                    "index": 52,
        |                    "amount": 76829252,
        |                    "name": "ERG_NETA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "4d46c7d7a0ec8dd89d90424fab0442354bca3793135e115a4c9de922fdd51daa",
        |                    "index": 53,
        |                    "amount": 632455532,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "27a9c163d2d9acef611e7354f5dee8aaedbf4dd82c3e819d6f6bf58b3c54acc2",
        |                    "index": 54,
        |                    "amount": 50000000,
        |                    "name": "Hello Staker whitelist token",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "1f48fbe220d51cace0dbc9649bc29c929367191cbdfda4819250e9b53492111e",
        |                    "index": 55,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "6a928dad5999caeaf08396f9a40c37b1b09ce2b972df457f4afacb61ff69009a",
        |                    "index": 56,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "2f7070c00bc89fbbf190eae5069f8bc73216865452ed87a71f7ca3b385af2d29",
        |                    "index": 57,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "bdfe633145d9e5e8232cd14ff0c40f7d278b81bf987915edbde4e67d28ab2a7a",
        |                    "index": 58,
        |                    "amount": 223606797,
        |                    "name": "WT_ADA_WT_ERG_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40",
        |                    "index": 59,
        |                    "amount": 2119,
        |                    "name": "kushti",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "19e5b066e46094f99c39564208995fc0544f7a452082e2aaa1b5b090a7f104b9",
        |                    "index": 60,
        |                    "amount": 9223372036854774807,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
        |                    "index": 61,
        |                    "amount": 40000,
        |                    "name": "ergopad",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "a4d9964b4cca1b97d3d03047900a41b5de044d02a8357d0120cf2bc11c68cbdc",
        |                    "index": 62,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "d60d1836dacc7603ec9095d735dcdb3efbe9a5ab348726c3ed115cf85f314917",
        |                    "index": 63,
        |                    "amount": 223606797,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "18c938e1924fc3eadc266e75ec02d81fe73b56e4e9f4e268dffffcb30387c42d",
        |                    "index": 64,
        |                    "amount": 30392,
        |                    "name": "AHT",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "4d3a47d770d9f15111026d8a22e1f7d09d9465b1259db5c2279545c7bcee9870",
        |                    "index": 65,
        |                    "amount": 4285714000000,
        |                    "name": "Test Spectrum Finance Staker Round whitelist token",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "8b6c0c3b2dacf3a4fa68aedd8e66e513552b575fd0d7a6fc8feab249b3ced1d4",
        |                    "index": 66,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "41cd36f005170db748c305ca721780fd092dc2d31b5a02ab2cdca80c447ecafc",
        |                    "index": 67,
        |                    "amount": 9223372036854775807,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "7c483c8460fc3afaa14d570e88068022039e61f9b32fa68295c08b4c5c035696",
        |                    "index": 68,
        |                    "amount": 182574184,
        |                    "name": "WT_ERG_WT_ADA_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "cc2726a5bb4a2132c99bb69be6f1d9a8bf7b09ad43db51bb6544b3e7db450f1f",
        |                    "index": 69,
        |                    "amount": 3850199989,
        |                    "name": "Eamd",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e0588d273c8183865cff31b3bfa766bc7b178e2362b45497b67e79662e3615b7",
        |                    "index": 70,
        |                    "amount": 54,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                }
        |            ],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "86b3169e4b8f413f644448c017d27cb0f7f896bb47657d32543a12e992fc52aa",
        |            "mainChain": true
        |        },
        |        {
        |            "boxId": "274a57ba9a77204ac1f57df652883f6f5bd2a2019a728af8b50a76ab5a437bbb",
        |            "transactionId": "07125bda3ead0df3776de09749cd76ff97f8914663e11c88766aa5f3d931e0a5",
        |            "blockId": "c14c5b8fe66ed18806fc568ac6254d9917c3605d0566b80e4f13f76371f3dabe",
        |            "value": 2000000,
        |            "index": 2,
        |            "globalIndex": 26434677,
        |            "creationHeight": 937457,
        |            "settlementHeight": 937460,
        |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
        |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
        |            "assets": [],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "be44afc5f790cbb2170fe1bf9f0d37d7170bf004a9c4f61e83a907f04c84f350",
        |            "mainChain": true
        |        }
        |    ],
        |    "size": 3461
        |}
        |""".stripMargin
    ).toOption.get.toTransaction

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

  val deposit2 = LmDepositV1(
    deployDepositOrder,
    PoolId.unsafeFromString("0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f"),
    ErgoTreeRedeemer(
      SErgoTree.unsafeFromString("0008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec")
    ),
    LmDepositParams(
      4,
      AssetAmount(
        TokenId.unsafeFromString("e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87"),
        444898
      )
    ),
    2000000,
    V1
  )

  def deployDepositOrder =
    decode[Output](
      """
        |{
        |            "boxId": "55323416b5ebaa40e91428acf849d807d83a7c5f644fce7d14da780f507dfa7a",
        |            "transactionId": "07125bda3ead0df3776de09749cd76ff97f8914663e11c88766aa5f3d931e0a5",
        |            "blockId": "c14c5b8fe66ed18806fc568ac6254d9917c3605d0566b80e4f13f76371f3dabe",
        |            "value": 2750000,
        |            "index": 0,
        |            "globalIndex": 26434675,
        |            "creationHeight": 937457,
        |            "settlementHeight": 937460,
        |            "ergoTree": "198d041604000e200f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f04020e240008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec0404040008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec040005fcffffffffffffffff0104000e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c04060400040804080402050204040e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f401d808d601b2a4730000d602db63087201d6037301d604b2a5730200d6057303d606c57201d607b2a5730400d6088cb2db6308a773050002eb027306d1ededed938cb27202730700017203ed93c27204720593860272067308b2db63087204730900ededededed93cbc27207730a93d0e4c672070408720593e4c67207050e72039386028cb27202730b00017208b2db63087207730c009386028cb27202730d00019c72087e730e05b2db63087207730f0093860272067310b2db6308720773110090b0ada5d90109639593c272097312c1720973137314d90109599a8c7209018c7209027315",
        |            "address": "TgkYJpD7df1quJhA1NULm4ojbZquBK5dN5o6fDmoWa12g6Y4dDo7vKCuPTv7Zhn1FnGyAsbiB1T6GwtVdBbK8ksZ8e3415maDECiffKRjrCooCyUuGf8q54rG93WU1pDzQHWNxDgSKx95itkhdiQ8mTq7qR6v3wXQRyfh1fQz23BKjLECP8jDP7jnzEeiNhvUJ28QKwD72j28K781pbWhSdSc6VbAZZyiFz3vXiTBCQbz5b3bgWHUhWmpFKrrE7P1NVUy3XV2bq4oJGEemLW8G3SaHqhaEaVtFUmMHT9Hmkje6HUUNMKEZgmJmUtB5PSihTH5eP6kw5g2QeKB1SvgfkSthU8eCmd4EZuMzUJbtHUxCUEF8GFptL3D512PpYEx33iw6zzgc85v3mFtYpxSPFJgW73QRH8gyf6XtWxbCVQwWxeGDarjpJ8wA45V1oFBL1v52Q9ZZ8fuMsi7YeAVrCV2XUHfAbL1pU6xx4GfSUM2v3ky89w7UCLrWE4vWByjJQa3mcfDXtAoiQDfZyyEeGeixE2FNCDxVVgPTNiNfGuuZY71sUwHdxxEqmYTW5uGQvowePpYzGkE5Jqknd9PigPk7r838FJbx8qSMwis4qFGt7f1wBubvJhiFBTCoZNEZDRoZXvy1D6EkT9aN8M39E4h2UoRyCGEoshXC1npgCBcSzohztgXx6j7TrSQ3JcsduAwerZhJ1xTwZKD6ysPZ8",
        |            "assets": [
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 0,
        |                    "amount": 444898,
        |                    "name": "Ergo_ErgoPOS_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
        |            "mainChain": true
        |        }
        |""".stripMargin
    ).toOption.get

  def evalDepositTx = decode[TransactionTest](
    """
      |{
      |    "id": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
      |    "blockId": "fcc2c2a71eb1b1c5c0707e75d2ede46733782b14c5c247382ed8b0110871e0bd",
      |    "inclusionHeight": 937480,
      |    "timestamp": 1676051781005,
      |    "index": 4,
      |    "globalIndex": 4741226,
      |    "numConfirmations": 1940,
      |    "inputs": [
      |        {
      |            "boxId": "00738c0e3c4d89a1a2d5e71a20e4a00262340f289e58d9d866719564a933bad5",
      |            "value": 1250000,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "4c6f21c33c8e137b2a2b0194d7e720e47181f2637e0f7556f67981590ae661d7",
      |            "outputTransactionId": "f56f18d7cf8f2321dd8efd5fcad03d4f540122fed7f5f0bcaada1731ba80d397",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 26433065,
      |            "outputCreatedAt": 937208,
      |            "outputSettledAt": 937403,
      |            "ergoTree": "19c0062904000400040204020404040404060406040804080404040204000400040204020601010400040a050005000404040204020e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c0400040205000402040204060500050005feffffffffffffffff010502050005000402050005000100d820d601b2a5730000d602db63087201d603db6308a7d604b27203730100d605e4c6a70410d606e4c6a70505d607e4c6a70605d608b27202730200d609b27203730300d60ab27202730400d60bb27203730500d60cb27202730600d60db27203730700d60e8c720d01d60fb27202730800d610b27203730900d6118c721001d6128c720b02d613998c720a027212d6148c720902d615b27205730a00d6169a99a37215730bd617b27205730c00d6189d72167217d61995919e72167217730d9a7218730e7218d61ab27205730f00d61b7e721a05d61c9d7206721bd61d998c720c028c720d02d61e8c721002d61f998c720f02721ed6207310d1ededededed93b272027311007204ededed93e4c672010410720593e4c672010505720693e4c6720106057207928cc77201018cc7a70193c27201c2a7ededed938c7208018c720901938c720a018c720b01938c720c01720e938c720f01721193b172027312959172137313d802d6219c721399721ba273147e721905d622b2a5731500ededed929a997206721472079c7e9995907219721a72199a721a7316731705721c937213f0721d937221f0721fedededed93cbc272227318938602720e7213b2db6308722273190093860272117221b2db63087222731a00e6c67222040893e4c67222050e8c720401958f7213731bededec929a997206721472079c7e9995907219721a72199a721a731c731d05721c92a39a9a72159c721a7217b27205731e0093721df0721392721f95917219721a731f9c721d99721ba273207e721905d804d621e4c672010704d62299721a7221d6237e722205d62499997321721e9c9972127322722395ed917224732391721f7324edededed9072219972197325909972149c7223721c9a721c7207907ef0998c7208027214069a9d9c99997e7214069d9c7e7206067e7222067e721a0672207e721f067e7224067220937213732693721d73277328",
      |            "address": "2JowFguNN2xRcYi3GANwoCnk46sVvc2D6oT27A2xbj5DsDEj2TMJbZmR8iqtWCbobi7hEvhkXQeMo9sjGh5jdBo133aJxr7ZTcfeVk8zN1MMDRqP64ehC7BNYGarJ5eaHpEq6DahnLC2khXpqU7dvfCczuPXd9qioB32tNqdCk3b9NBGUuQxNqK6FrHbLVi99KvuN2Nz9bh8HEZFukDKymuDpHwXF5JjMUW2YGDQCBFmLgKGFrLrvC2iQH18ftfmQoqUS4GCXpdJ5YtPMg8bLZ4GNP5QaXZXEQpRTz5fgRu9FR2ZgmZ5r1tSQV789H3Lf6RcA3RmkWoTw8RMEMdwE45e7yoiWVTHqkFMLz2qEnYgmzSyxv7Uxk1f9KXfvWPf4CbMfi9y6txGXh5MNhzPKzRUuNS8VxScAt7JYfDCvp1KhyyFLWy9ijtDbmUw2MshWAubzt2dAJaH2hAMiv1rGi6QWG9ru6LvPTH7Tr1aUhLac6DK2p2ycBwk8hp417K7sU2aVzRfWhreEZxnCH2aGQvp8iq4PDkRsEjgUpMzCu4wJDXRa9v3gKoBP7N3xWV2xUg1xmMTPC1LNomT86k5vQvBZpM74gZ3RWmJTFoq6dwPtHNHC2hdyfuqZDpHz2ATcbem4wmG1QJPig92R36WKmydN53RujrA3pXccxGNHqMqPsu2YexSURoSihtJYsrdGG4332DdGappSnAgDeETYJ69GJ6giTiddVQv3oKzuF7tZwdVrFzFoUkSWEGqd4jnWRg46cqTqJqT5RH4MJw7FsCi6gZD1X39X1ANgv9vC6pNV6QqdJktpN9CoM2DbEyVMyYdhpgotexAR9u6KEBJmEbfps7jus9X97arMmzHRwJR99X37uj8zT5g36SAPPwDyueEAsW7r1RNuwm4DGPAmdxYBRgi5iri9TCHpAeDBLCPU9tycSVPviDrNxKWVYEus6jqZdNENPrTeA83T4SGdFKmA8aVyNKPtzvejRLEwYCjdLAAgoF7oGY5R6gq9caatH23rrEJutNCS4csAgm8QyrETfF1nv9KMrGvA9E577dBcNYKHdLjfVKHi7op3LyNTgYTc2DkYakdG94tJzPbPsgAetpFKs3SjWcUkhhgCm8JoiQ8iFDXG4kgADJ",
      |            "assets": [
      |                {
      |                    "tokenId": "0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
      |                    "index": 1,
      |                    "amount": 11000000,
      |                    "name": "EPOS",
      |                    "decimals": 4,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
      |                    "index": 2,
      |                    "amount": 429849,
      |                    "name": "Ergo_ErgoPOS_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0",
      |                    "index": 3,
      |                    "amount": 9223372036854345959,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "bc58f841398f84fd0a72e2de50f8f3aed14e6234f7502bb7fe90729fba9c74f2",
      |                    "index": 4,
      |                    "amount": 9223372036853026415,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "1004f4030a9cb672c801",
      |                    "sigmaType": "Coll[SInt]",
      |                    "renderedValue": "[250,5,937358,100]"
      |                },
      |                "R5": {
      |                    "serializedValue": "05fee2be0a",
      |                    "sigmaType": "SLong",
      |                    "renderedValue": "10999999"
      |                },
      |                "R6": {
      |                    "serializedValue": "05d00f",
      |                    "sigmaType": "SLong",
      |                    "renderedValue": "1000"
      |                }
      |            }
      |        },
      |        {
      |            "boxId": "55323416b5ebaa40e91428acf849d807d83a7c5f644fce7d14da780f507dfa7a",
      |            "value": 2750000,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "c14c5b8fe66ed18806fc568ac6254d9917c3605d0566b80e4f13f76371f3dabe",
      |            "outputTransactionId": "07125bda3ead0df3776de09749cd76ff97f8914663e11c88766aa5f3d931e0a5",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 26434675,
      |            "outputCreatedAt": 937457,
      |            "outputSettledAt": 937460,
      |            "ergoTree": "198d041604000e200f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f04020e240008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec0404040008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec040005fcffffffffffffffff0104000e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c04060400040804080402050204040e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f401d808d601b2a4730000d602db63087201d6037301d604b2a5730200d6057303d606c57201d607b2a5730400d6088cb2db6308a773050002eb027306d1ededed938cb27202730700017203ed93c27204720593860272067308b2db63087204730900ededededed93cbc27207730a93d0e4c672070408720593e4c67207050e72039386028cb27202730b00017208b2db63087207730c009386028cb27202730d00019c72087e730e05b2db63087207730f0093860272067310b2db6308720773110090b0ada5d90109639593c272097312c1720973137314d90109599a8c7209018c7209027315",
      |            "address": "TgkYJpD7df1quJhA1NULm4ojbZquBK5dN5o6fDmoWa12g6Y4dDo7vKCuPTv7Zhn1FnGyAsbiB1T6GwtVdBbK8ksZ8e3415maDECiffKRjrCooCyUuGf8q54rG93WU1pDzQHWNxDgSKx95itkhdiQ8mTq7qR6v3wXQRyfh1fQz23BKjLECP8jDP7jnzEeiNhvUJ28QKwD72j28K781pbWhSdSc6VbAZZyiFz3vXiTBCQbz5b3bgWHUhWmpFKrrE7P1NVUy3XV2bq4oJGEemLW8G3SaHqhaEaVtFUmMHT9Hmkje6HUUNMKEZgmJmUtB5PSihTH5eP6kw5g2QeKB1SvgfkSthU8eCmd4EZuMzUJbtHUxCUEF8GFptL3D512PpYEx33iw6zzgc85v3mFtYpxSPFJgW73QRH8gyf6XtWxbCVQwWxeGDarjpJ8wA45V1oFBL1v52Q9ZZ8fuMsi7YeAVrCV2XUHfAbL1pU6xx4GfSUM2v3ky89w7UCLrWE4vWByjJQa3mcfDXtAoiQDfZyyEeGeixE2FNCDxVVgPTNiNfGuuZY71sUwHdxxEqmYTW5uGQvowePpYzGkE5Jqknd9PigPk7r838FJbx8qSMwis4qFGt7f1wBubvJhiFBTCoZNEZDRoZXvy1D6EkT9aN8M39E4h2UoRyCGEoshXC1npgCBcSzohztgXx6j7TrSQ3JcsduAwerZhJ1xTwZKD6ysPZ8",
      |            "assets": [
      |                {
      |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
      |                    "index": 0,
      |                    "amount": 444898,
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
      |            "boxId": "4bdbd9682c4023e573ce86343f95fc59c037e36ff06a81637dcf604145907397",
      |            "transactionId": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
      |            "blockId": "fcc2c2a71eb1b1c5c0707e75d2ede46733782b14c5c247382ed8b0110871e0bd",
      |            "value": 1250000,
      |            "index": 0,
      |            "globalIndex": 26435054,
      |            "creationHeight": 937478,
      |            "settlementHeight": 937480,
      |            "ergoTree": "19c0062904000400040204020404040404060406040804080404040204000400040204020601010400040a050005000404040204020e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c0400040205000402040204060500050005feffffffffffffffff010502050005000402050005000100d820d601b2a5730000d602db63087201d603db6308a7d604b27203730100d605e4c6a70410d606e4c6a70505d607e4c6a70605d608b27202730200d609b27203730300d60ab27202730400d60bb27203730500d60cb27202730600d60db27203730700d60e8c720d01d60fb27202730800d610b27203730900d6118c721001d6128c720b02d613998c720a027212d6148c720902d615b27205730a00d6169a99a37215730bd617b27205730c00d6189d72167217d61995919e72167217730d9a7218730e7218d61ab27205730f00d61b7e721a05d61c9d7206721bd61d998c720c028c720d02d61e8c721002d61f998c720f02721ed6207310d1ededededed93b272027311007204ededed93e4c672010410720593e4c672010505720693e4c6720106057207928cc77201018cc7a70193c27201c2a7ededed938c7208018c720901938c720a018c720b01938c720c01720e938c720f01721193b172027312959172137313d802d6219c721399721ba273147e721905d622b2a5731500ededed929a997206721472079c7e9995907219721a72199a721a7316731705721c937213f0721d937221f0721fedededed93cbc272227318938602720e7213b2db6308722273190093860272117221b2db63087222731a00e6c67222040893e4c67222050e8c720401958f7213731bededec929a997206721472079c7e9995907219721a72199a721a731c731d05721c92a39a9a72159c721a7217b27205731e0093721df0721392721f95917219721a731f9c721d99721ba273207e721905d804d621e4c672010704d62299721a7221d6237e722205d62499997321721e9c9972127322722395ed917224732391721f7324edededed9072219972197325909972149c7223721c9a721c7207907ef0998c7208027214069a9d9c99997e7214069d9c7e7206067e7222067e721a0672207e721f067e7224067220937213732693721d73277328",
      |            "address": "2JowFguNN2xRcYi3GANwoCnk46sVvc2D6oT27A2xbj5DsDEj2TMJbZmR8iqtWCbobi7hEvhkXQeMo9sjGh5jdBo133aJxr7ZTcfeVk8zN1MMDRqP64ehC7BNYGarJ5eaHpEq6DahnLC2khXpqU7dvfCczuPXd9qioB32tNqdCk3b9NBGUuQxNqK6FrHbLVi99KvuN2Nz9bh8HEZFukDKymuDpHwXF5JjMUW2YGDQCBFmLgKGFrLrvC2iQH18ftfmQoqUS4GCXpdJ5YtPMg8bLZ4GNP5QaXZXEQpRTz5fgRu9FR2ZgmZ5r1tSQV789H3Lf6RcA3RmkWoTw8RMEMdwE45e7yoiWVTHqkFMLz2qEnYgmzSyxv7Uxk1f9KXfvWPf4CbMfi9y6txGXh5MNhzPKzRUuNS8VxScAt7JYfDCvp1KhyyFLWy9ijtDbmUw2MshWAubzt2dAJaH2hAMiv1rGi6QWG9ru6LvPTH7Tr1aUhLac6DK2p2ycBwk8hp417K7sU2aVzRfWhreEZxnCH2aGQvp8iq4PDkRsEjgUpMzCu4wJDXRa9v3gKoBP7N3xWV2xUg1xmMTPC1LNomT86k5vQvBZpM74gZ3RWmJTFoq6dwPtHNHC2hdyfuqZDpHz2ATcbem4wmG1QJPig92R36WKmydN53RujrA3pXccxGNHqMqPsu2YexSURoSihtJYsrdGG4332DdGappSnAgDeETYJ69GJ6giTiddVQv3oKzuF7tZwdVrFzFoUkSWEGqd4jnWRg46cqTqJqT5RH4MJw7FsCi6gZD1X39X1ANgv9vC6pNV6QqdJktpN9CoM2DbEyVMyYdhpgotexAR9u6KEBJmEbfps7jus9X97arMmzHRwJR99X37uj8zT5g36SAPPwDyueEAsW7r1RNuwm4DGPAmdxYBRgi5iri9TCHpAeDBLCPU9tycSVPviDrNxKWVYEus6jqZdNENPrTeA83T4SGdFKmA8aVyNKPtzvejRLEwYCjdLAAgoF7oGY5R6gq9caatH23rrEJutNCS4csAgm8QyrETfF1nv9KMrGvA9E577dBcNYKHdLjfVKHi7op3LyNTgYTc2DkYakdG94tJzPbPsgAetpFKs3SjWcUkhhgCm8JoiQ8iFDXG4kgADJ",
      |            "assets": [
      |                {
      |                    "tokenId": "0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
      |                    "index": 1,
      |                    "amount": 11000000,
      |                    "name": "EPOS",
      |                    "decimals": 4,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
      |                    "index": 2,
      |                    "amount": 874747,
      |                    "name": "Ergo_ErgoPOS_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0",
      |                    "index": 3,
      |                    "amount": 9223372036853901061,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "bc58f841398f84fd0a72e2de50f8f3aed14e6234f7502bb7fe90729fba9c74f2",
      |                    "index": 4,
      |                    "amount": 9223372036851246823,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "1004f4030a9cb672c801",
      |                    "sigmaType": "Coll[SInt]",
      |                    "renderedValue": "[250,5,937358,100]"
      |                },
      |                "R5": {
      |                    "serializedValue": "05fee2be0a",
      |                    "sigmaType": "SLong",
      |                    "renderedValue": "10999999"
      |                },
      |                "R6": {
      |                    "serializedValue": "05d00f",
      |                    "sigmaType": "SLong",
      |                    "renderedValue": "1000"
      |                }
      |            },
      |            "spentTransactionId": "c952523a2e9651cf6d440109553430a11c31c28baeb48dc84adce3ab05404e11",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "ae4822ba1ae63a49bdcec712803c164c5c22086dd7b421699befe1c4637ccc39",
      |            "transactionId": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
      |            "blockId": "fcc2c2a71eb1b1c5c0707e75d2ede46733782b14c5c247382ed8b0110871e0bd",
      |            "value": 250000,
      |            "index": 1,
      |            "globalIndex": 26435055,
      |            "creationHeight": 937478,
      |            "settlementHeight": 937480,
      |            "ergoTree": "0008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec",
      |            "address": "9hp1xXVF8VXkYEHdgvTJK7XSEW1vckcKqWx8JTHsAwwGzHH9hxq",
      |            "assets": [
      |                {
      |                    "tokenId": "00738c0e3c4d89a1a2d5e71a20e4a00262340f289e58d9d866719564a933bad5",
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
      |            "boxId": "5c814a77bb326120e5b43d62d5c0631970c09cc1cc2e144febd58adf71e3bce9",
      |            "transactionId": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
      |            "blockId": "fcc2c2a71eb1b1c5c0707e75d2ede46733782b14c5c247382ed8b0110871e0bd",
      |            "value": 300000,
      |            "index": 2,
      |            "globalIndex": 26435056,
      |            "creationHeight": 937478,
      |            "settlementHeight": 937480,
      |            "ergoTree": "19a3041f040004000404040404000402060101040005000402040404020400040004020502040405020402040005feffffffffffffffff010408050205000404040004060404040205fcffffffffffffffff010100d80cd601b2a5730000d602db63087201d603e4c6a7050ed604b2a4730100d605db63087204d6068cb2720573020002d607998cb27202730300027206d608e4c6a70408d609db6308a7d60ab27209730400d60bb27205730500d60c7306d1ed938cb27202730700017203959372077308d80cd60db2a5e4e3000400d60eb2a5e4e3010400d60fb2e4c672040410730900d610c672010804d61199720f95e67210e47210e4c672010704d6128cb27209730a0001d613db6308720ed614b27209730b00d6158c721402d6167e721105d6179972159c72168c720a02d618b2db6308720d730c00eded93c2720dd07208edededed93e4c6720e0408720893e4c6720e050e720393c2720ec2a795917211730dd801d619b27213730e00eded9386027212730fb27213731000938c7219018c721401939972158c721902721793860272127311b2721373120093b27213731300720aed938c7218018c720b01927e8c7218020699999d9c99997e8c720b02069d9c7ee4c672040505067e7211067e720f06720c7e7217067e999973148cb27205731500029c9972067316721606720c720c958f7207731793b2db6308b2a473180073190086029593b17209731a8cb27209731b00018cb27209731c0001731d731e",
      |            "address": "YyLFSfjMQw5AJEuchDNPzziDH7CpLX8EbtGbddMpTpDoYYCc5xKJUbDuFnwsJ4qcpARjLmTQdRUTDiftnkb4kuNAaM7enwfxbWvh9SAbpvxAtJzhQApmedrEH9BBrPYzHdWbR354ZXZJkazhvfWMLyijyFeJ7aTDYmhDn8YvF3HeBMJiLByT32wTfNVEWQf4SEp3eN66ppqZs7bJAxrsZo3PDGJgTtZ2qPypMv3x9fcE6zYoLiCzHbAWC8WCfn5wNno9NAwSgWgHKcz3Gx4kXAe5DJnMDZJeiAuckS4e8J5sUfgFSPHTysvkfPw5pxBBNoxrsQ8y2jHSjB3KhSKKsNoZLnWZpbLQ7TurFc34TZJn9FNsRzpmQc9ftcZNa1QxKNoBtFzCpRXwvKd1RaSEZc5er7TwiyL9ebcfh1x3b3k2qXZhi6eXnktGBhEcNiJoJe7FdRA7gTXs9MCQewX8r7u3ay6i94bT9BrdXZy7xnmFU1DvyCcNP3sHHC1gDbNxdzVYsyF9tuxuXhmmD3rTc7hCPX4uo1dahq5HFaaQifp8kwe6iVuzp3Qo5vNPgV8tJBzXhGx7wgYoozGznh1ecYdBtCpHdEwzbME4bQy8qLXsBRB7TVnhQ2GYUL8XyCpzaziWEe1tdKrgbFFE1CNnLuGR7FEFikx5PfJVFTmFvpEvvtM7Qq9TMFjgsaoP58M7BajhU74EgqVRX3hi8pmmzxnjAzVKK5wZR62RcpowjrYsNLGjcWL8B",
      |            "assets": [
      |                {
      |                    "tokenId": "ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0",
      |                    "index": 0,
      |                    "amount": 444898,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "bc58f841398f84fd0a72e2de50f8f3aed14e6234f7502bb7fe90729fba9c74f2",
      |                    "index": 1,
      |                    "amount": 1779592,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "00738c0e3c4d89a1a2d5e71a20e4a00262340f289e58d9d866719564a933bad5",
      |                    "index": 2,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "08cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec",
      |                    "sigmaType": "SSigmaProp",
      |                    "renderedValue": "03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec"
      |                },
      |                "R5": {
      |                    "serializedValue": "0e200f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f",
      |                    "sigmaType": "Coll[SByte]",
      |                    "renderedValue": "0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f"
      |                }
      |            },
      |            "spentTransactionId": "4fffc1562aa9f2af3322fba8284489dab58d3e488d1b381f475e586fd0001a24",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "9f8c944de15aa93a521a3d10fc9c2e45bea29640f2a0f48cd50cf69a9653b585",
      |            "transactionId": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
      |            "blockId": "fcc2c2a71eb1b1c5c0707e75d2ede46733782b14c5c247382ed8b0110871e0bd",
      |            "value": 1200000,
      |            "index": 3,
      |            "globalIndex": 26435057,
      |            "creationHeight": 937478,
      |            "settlementHeight": 937480,
      |            "ergoTree": "0008cd02d6b2141c21e4f337e9b065a031a6269fb5a49253094fc6243d38662eb765db00",
      |            "address": "9g9cdHhNZvtUvMveqEEfk28JZasEC8sJamV3E6d5JHv8VYUjjbX",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "c787cdd83ec1c3e1f44e4a336b8536c051de3c9df9c5d2e017fcf942490b3027",
      |            "transactionId": "2c954d48caf1baec0a19793ae1b3ad673311488ea90e4884a7653771c8153831",
      |            "blockId": "fcc2c2a71eb1b1c5c0707e75d2ede46733782b14c5c247382ed8b0110871e0bd",
      |            "value": 1000000,
      |            "index": 4,
      |            "globalIndex": 26435058,
      |            "creationHeight": 937478,
      |            "settlementHeight": 937480,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "fd5605035149345f4311a29b9abb81848a8a7346beebc75469bb128f41e9916f",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 2004
      |}
      |""".stripMargin
  )

  def compoundCreateForDeposit = decode[Output](
    """
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

  def redeemOrderOutput = decode[Output](
    s"""{
       |    "boxId": "6293e74ef80b60076ceba4372c9f68c8bcd610eb9ddd6b565e4adf624f4a9d7a",
       |    "transactionId": "5de5c4de59a4592774ad07dbbc96e8ae81d9982cdf8e19e35a8725ba7c4a513e",
       |    "blockId": "4f0d05a28148c351e91e64059e2e9e3795a4c50fbca386c5f079e092c58d89da",
       |    "value": 1250000,
       |    "index": 0,
       |    "globalIndex": 26421837,
       |    "creationHeight": 937208,
       |    "settlementHeight": 937235,
       |    "ergoTree": "19b4020a040208cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac4910e240008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac4910e20e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f8705a09c0104000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304050005000580f882ad16d801d601b2a5730000eb027301d1eded93c27201730293860273037304b2db6308720173050090b0ada5d90102639593c272027306c1720273077308d90102599a8c7202018c7202027309",
       |    "address": "7W3rNYwfSSjEb41BHn4rnLHX2TKSbXL71t5aaTnrHYWpQ9PbHQ6JZPu88cvkzQdduescZUrKtq6aiU4ajN8G946Zixo9YxV1XBKyerjMWJdRATCm4afpWbtA57Y6USgSeHxLjpRgFa3sRFA4ZziyN1rFosaVCbSa6YeLCEPpZGwfTGpgtHxDYVZtAsxcMQyaUNsAeWE84nDDyzZnNMJKbFUq1zrE3LG3hFeViZRuZN5nbCrZZPMcwsUr8pPjcUzA3fvWqKsd2tt7AmpvrPeYEKAnK2CZGx31cyccmq9RfimMfvt6ShA6s2afU3GCsQQvhmDNeA8jjjF85VAh9d2CBqqEtX5uDTx36MFueA4dFrcKw5ihMFF1HbwqfbVfQSRAcNBmqv73SNTBp2JsVTbyX6PFZFwtqFavjKFXZxHEKZqQ1pK",
       |    "assets": [
       |        {
       |            "tokenId": "cba6fabbc040c49873d3dea062a7fc81ff3262e1799dfd41e05014c5e8d91109",
       |            "index": 0,
       |            "amount": 9223372036854775806,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        }
       |    ],
       |    "additionalRegisters": {},
       |    "spentTransactionId": "3fed3dbde8efd9e913486269872cc85ba211a63b5045f1008a754044b658d941",
       |    "mainChain": true
       |}""".stripMargin
  ).toOption.get

  val redeem = LmRedeemV1(
    redeemOrderOutput,
    TokenId.unsafeFromString("cba6fabbc040c49873d3dea062a7fc81ff3262e1799dfd41e05014c5e8d91109"),
    AssetAmount(
      TokenId.unsafeFromString("e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87"),
      10000
    ),
    3000000000L,
    ErgoTreeRedeemer(SErgoTree.unsafeFromString("0008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491")),
    V1
  )

  def redeemEvaluate =
    decode[TransactionTest](
      """
        |{
        |    "id": "3fed3dbde8efd9e913486269872cc85ba211a63b5045f1008a754044b658d941",
        |    "blockId": "0e817065617f39692ab8855742f6a7b290c38d06969452455c309d4052f7156e",
        |    "inclusionHeight": 937237,
        |    "timestamp": 1676023907117,
        |    "index": 7,
        |    "globalIndex": 4738352,
        |    "numConfirmations": 2208,
        |    "inputs": [
        |        {
        |            "boxId": "526d895139bd0fd4ef5f7f8f3f803d5f36f23057865d3c4cf3a25fbe305a4604",
        |            "value": 1250000,
        |            "index": 0,
        |            "spendingProof": "159d2b2f29ac451da6fafa4c0fc0f043684d84e2dc770ab4628ab53caaa2f327eae78f2f5535f4725864712632b1bd29ed160588f9b0c7d6",
        |            "outputBlockId": "a5df1a7937b961e1e5c8bea01e8cbd8d954af98ef6fa880ec991959a13e26601",
        |            "outputTransactionId": "37137705008bf57ee71f3133d4af515e2a631dc34f89d10657eb62ba24ac5f27",
        |            "outputIndex": 0,
        |            "outputGlobalIndex": 26418729,
        |            "outputCreatedAt": 937187,
        |            "outputSettledAt": 937189,
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
        |                    "amount": 3,
        |                    "name": "EPOS",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 2,
        |                    "amount": 30001,
        |                    "name": "Ergo_ErgoPOS_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "81f307da6c294bb9ee1c8789dfeff5b97c2399451e099ab6c9985a55551e41dd",
        |                    "index": 3,
        |                    "amount": 9223372036854745807,
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
        |                },
        |                "R7": {
        |                    "serializedValue": "040a",
        |                    "sigmaType": "SInt",
        |                    "renderedValue": "5"
        |                }
        |            }
        |        },
        |        {
        |            "boxId": "353fb1e32f52c4353daf8c66c8426b36f2d1989a88efd64bd38ac3173dc3c4ca",
        |            "value": 1250000,
        |            "index": 1,
        |            "spendingProof": "159d2b2f29ac451da6fafa4c0fc0f043684d84e2dc770ab4628ab53caaa2f327eae78f2f5535f4725864712632b1bd29ed160588f9b0c7d6",
        |            "outputBlockId": "a5df1a7937b961e1e5c8bea01e8cbd8d954af98ef6fa880ec991959a13e26601",
        |            "outputTransactionId": "37137705008bf57ee71f3133d4af515e2a631dc34f89d10657eb62ba24ac5f27",
        |            "outputIndex": 3,
        |            "outputGlobalIndex": 26418732,
        |            "outputCreatedAt": 937187,
        |            "outputSettledAt": 937189,
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
        |                    "tokenId": "cba6fabbc040c49873d3dea062a7fc81ff3262e1799dfd41e05014c5e8d91109",
        |                    "index": 1,
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
        |            }
        |        },
        |        {
        |            "boxId": "6293e74ef80b60076ceba4372c9f68c8bcd610eb9ddd6b565e4adf624f4a9d7a",
        |            "value": 1250000,
        |            "index": 2,
        |            "spendingProof": "159d2b2f29ac451da6fafa4c0fc0f043684d84e2dc770ab4628ab53caaa2f327eae78f2f5535f4725864712632b1bd29ed160588f9b0c7d6",
        |            "outputBlockId": "4f0d05a28148c351e91e64059e2e9e3795a4c50fbca386c5f079e092c58d89da",
        |            "outputTransactionId": "5de5c4de59a4592774ad07dbbc96e8ae81d9982cdf8e19e35a8725ba7c4a513e",
        |            "outputIndex": 0,
        |            "outputGlobalIndex": 26421837,
        |            "outputCreatedAt": 937208,
        |            "outputSettledAt": 937235,
        |            "ergoTree": "19b4020a040208cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac4910e240008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac4910e20e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f8705a09c0104000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304050005000580f882ad16d801d601b2a5730000eb027301d1eded93c27201730293860273037304b2db6308720173050090b0ada5d90102639593c272027306c1720273077308d90102599a8c7202018c7202027309",
        |            "address": "7W3rNYwfSSjEb41BHn4rnLHX2TKSbXL71t5aaTnrHYWpQ9PbHQ6JZPu88cvkzQdduescZUrKtq6aiU4ajN8G946Zixo9YxV1XBKyerjMWJdRATCm4afpWbtA57Y6USgSeHxLjpRgFa3sRFA4ZziyN1rFosaVCbSa6YeLCEPpZGwfTGpgtHxDYVZtAsxcMQyaUNsAeWE84nDDyzZnNMJKbFUq1zrE3LG3hFeViZRuZN5nbCrZZPMcwsUr8pPjcUzA3fvWqKsd2tt7AmpvrPeYEKAnK2CZGx31cyccmq9RfimMfvt6ShA6s2afU3GCsQQvhmDNeA8jjjF85VAh9d2CBqqEtX5uDTx36MFueA4dFrcKw5ihMFF1HbwqfbVfQSRAcNBmqv73SNTBp2JsVTbyX6PFZFwtqFavjKFXZxHEKZqQ1pK",
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
        |            "additionalRegisters": {}
        |        }
        |    ],
        |    "dataInputs": [],
        |    "outputs": [
        |        {
        |            "boxId": "ab0e2e6e262b28b9481e84a854f5df3efe033d0e813982f04e011a3e1d291886",
        |            "transactionId": "3fed3dbde8efd9e913486269872cc85ba211a63b5045f1008a754044b658d941",
        |            "blockId": "0e817065617f39692ab8855742f6a7b290c38d06969452455c309d4052f7156e",
        |            "value": 1250000,
        |            "index": 0,
        |            "globalIndex": 26421891,
        |            "creationHeight": 937208,
        |            "settlementHeight": 937237,
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
        |                    "amount": 3,
        |                    "name": "EPOS",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 2,
        |                    "amount": 20001,
        |                    "name": "Ergo_ErgoPOS_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "81f307da6c294bb9ee1c8789dfeff5b97c2399451e099ab6c9985a55551e41dd",
        |                    "index": 3,
        |                    "amount": 9223372036854755807,
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
        |            },
        |            "spentTransactionId": "1e266b4652024fe6c4973b9b7d0e2cc2122010c4d60bd4d499854ddbef09f237",
        |            "mainChain": true
        |        },
        |        {
        |            "boxId": "db6d0e20f1b744795ee23bc38b752769c8368cd3b94823a916430524b4befc1c",
        |            "transactionId": "3fed3dbde8efd9e913486269872cc85ba211a63b5045f1008a754044b658d941",
        |            "blockId": "0e817065617f39692ab8855742f6a7b290c38d06969452455c309d4052f7156e",
        |            "value": 1250000,
        |            "index": 1,
        |            "globalIndex": 26421892,
        |            "creationHeight": 937208,
        |            "settlementHeight": 937237,
        |            "ergoTree": "0008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491",
        |            "address": "9iAYDwPWP7c9cKXB7P2bLivt75CBaXrbYoZrxpna1peB8cYFoHw",
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
        |            "spentTransactionId": null,
        |            "mainChain": true
        |        },
        |        {
        |            "boxId": "d06231e9282776f947ec7a4ea188c8a08b4b979fe042a353eb7518a034a481eb",
        |            "transactionId": "3fed3dbde8efd9e913486269872cc85ba211a63b5045f1008a754044b658d941",
        |            "blockId": "0e817065617f39692ab8855742f6a7b290c38d06969452455c309d4052f7156e",
        |            "value": 1250000,
        |            "index": 2,
        |            "globalIndex": 26421893,
        |            "creationHeight": 937208,
        |            "settlementHeight": 937237,
        |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
        |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
        |            "assets": [],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "e60b7c6ba8313dd0410841f4406338998a51f6a0ed95aafd3e90ab1455dee8db",
        |            "mainChain": true
        |        }
        |    ],
        |    "size": 1483
        |}
        |""".stripMargin
    ).toOption.get.toTransaction

  def deployRedeem =
    decode[TransactionTest](
      """
        |{
        |    "id": "5de5c4de59a4592774ad07dbbc96e8ae81d9982cdf8e19e35a8725ba7c4a513e",
        |    "blockId": "4f0d05a28148c351e91e64059e2e9e3795a4c50fbca386c5f079e092c58d89da",
        |    "inclusionHeight": 937235,
        |    "timestamp": 1676023627430,
        |    "index": 3,
        |    "globalIndex": 4738335,
        |    "numConfirmations": 2308,
        |    "inputs": [
        |        {
        |            "boxId": "e6539b9b5a1ba24aee00f1a5682bd930f2cf9a36a66c5d6fc4863f0d0d63fee0",
        |            "value": 1250000,
        |            "index": 0,
        |            "spendingProof": "8e45a2077da8d2b67c09182ad47459a4507e164dacd139d6e1183d206d8cb456d6ac86b491947e21e695b43b233ba0d5262cd37dd3ec2471",
        |            "outputBlockId": "6967cee75df994f539b1b16b684efc20c184d4781a341065fe55c50c431e6f12",
        |            "outputTransactionId": "6c2106bee6af77659b85006cf4cf365b4c3b944928d8fe3ba695cdce14d2bb3e",
        |            "outputIndex": 1,
        |            "outputGlobalIndex": 26335925,
        |            "outputCreatedAt": 935194,
        |            "outputSettledAt": 935204,
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
        |            "additionalRegisters": {}
        |        },
        |        {
        |            "boxId": "475c7c884582948217491600b0085428c4a6a582b2b5ddf0d17f0503e92e2071",
        |            "value": 2912500000,
        |            "index": 1,
        |            "spendingProof": "8e45a2077da8d2b67c09182ad47459a4507e164dacd139d6e1183d206d8cb456d6ac86b491947e21e695b43b233ba0d5262cd37dd3ec2471",
        |            "outputBlockId": "1ea9769b31be603ea9d8bca368ac9d4d3964d0f4adf7e26c0c8628234f25f9e8",
        |            "outputTransactionId": "74af7a3e7ba84e7da05b3c9492839d948d72b5666efb419990ff06765d9e2934",
        |            "outputIndex": 1,
        |            "outputGlobalIndex": 26421099,
        |            "outputCreatedAt": 937208,
        |            "outputSettledAt": 937220,
        |            "ergoTree": "0008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491",
        |            "address": "9iAYDwPWP7c9cKXB7P2bLivt75CBaXrbYoZrxpna1peB8cYFoHw",
        |            "assets": [
        |                {
        |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
        |                    "index": 0,
        |                    "amount": 13000000,
        |                    "name": "EPOS",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 1,
        |                    "amount": 49904991,
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
        |            "boxId": "6293e74ef80b60076ceba4372c9f68c8bcd610eb9ddd6b565e4adf624f4a9d7a",
        |            "transactionId": "5de5c4de59a4592774ad07dbbc96e8ae81d9982cdf8e19e35a8725ba7c4a513e",
        |            "blockId": "4f0d05a28148c351e91e64059e2e9e3795a4c50fbca386c5f079e092c58d89da",
        |            "value": 1250000,
        |            "index": 0,
        |            "globalIndex": 26421837,
        |            "creationHeight": 937208,
        |            "settlementHeight": 937235,
        |            "ergoTree": "19b4020a040208cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac4910e240008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac4910e20e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f8705a09c0104000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304050005000580f882ad16d801d601b2a5730000eb027301d1eded93c27201730293860273037304b2db6308720173050090b0ada5d90102639593c272027306c1720273077308d90102599a8c7202018c7202027309",
        |            "address": "7W3rNYwfSSjEb41BHn4rnLHX2TKSbXL71t5aaTnrHYWpQ9PbHQ6JZPu88cvkzQdduescZUrKtq6aiU4ajN8G946Zixo9YxV1XBKyerjMWJdRATCm4afpWbtA57Y6USgSeHxLjpRgFa3sRFA4ZziyN1rFosaVCbSa6YeLCEPpZGwfTGpgtHxDYVZtAsxcMQyaUNsAeWE84nDDyzZnNMJKbFUq1zrE3LG3hFeViZRuZN5nbCrZZPMcwsUr8pPjcUzA3fvWqKsd2tt7AmpvrPeYEKAnK2CZGx31cyccmq9RfimMfvt6ShA6s2afU3GCsQQvhmDNeA8jjjF85VAh9d2CBqqEtX5uDTx36MFueA4dFrcKw5ihMFF1HbwqfbVfQSRAcNBmqv73SNTBp2JsVTbyX6PFZFwtqFavjKFXZxHEKZqQ1pK",
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
        |            "spentTransactionId": "3fed3dbde8efd9e913486269872cc85ba211a63b5045f1008a754044b658d941",
        |            "mainChain": true
        |        },
        |        {
        |            "boxId": "23df12ad2288459adc5b388129ffc3e1762e6a14dbc857efabf21676cefd99b2",
        |            "transactionId": "5de5c4de59a4592774ad07dbbc96e8ae81d9982cdf8e19e35a8725ba7c4a513e",
        |            "blockId": "4f0d05a28148c351e91e64059e2e9e3795a4c50fbca386c5f079e092c58d89da",
        |            "value": 2911250000,
        |            "index": 1,
        |            "globalIndex": 26421838,
        |            "creationHeight": 937208,
        |            "settlementHeight": 937235,
        |            "ergoTree": "0008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491",
        |            "address": "9iAYDwPWP7c9cKXB7P2bLivt75CBaXrbYoZrxpna1peB8cYFoHw",
        |            "assets": [
        |                {
        |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
        |                    "index": 0,
        |                    "amount": 13000000,
        |                    "name": "EPOS",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 1,
        |                    "amount": 49904991,
        |                    "name": "Ergo_ErgoPOS_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "d0038d3d5f7310b279f0baab7cb7fc4af8aa82274056ec068d1fac007462e8cb",
        |            "mainChain": true
        |        },
        |        {
        |            "boxId": "9e5f2688a228b124cb678519294296d03bb1c469f415c4bd3e8a3546556eade3",
        |            "transactionId": "5de5c4de59a4592774ad07dbbc96e8ae81d9982cdf8e19e35a8725ba7c4a513e",
        |            "blockId": "4f0d05a28148c351e91e64059e2e9e3795a4c50fbca386c5f079e092c58d89da",
        |            "value": 1250000,
        |            "index": 2,
        |            "globalIndex": 26421839,
        |            "creationHeight": 937208,
        |            "settlementHeight": 937235,
        |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
        |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
        |            "assets": [],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "167c6bb46914aa07fe6f1df704e6b4084b68364a604f61c43749fe23449dff09",
        |            "mainChain": true
        |        }
        |    ],
        |    "size": 778
        |}
        |""".stripMargin
    ).toOption.get.toTransaction

}
