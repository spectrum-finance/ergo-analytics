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
        |    "id": "f21c01217489a4a1455fa6ec1c2b8642de2a1632be4a121ccca52da8c4a75278",
        |    "blockId": "778cdf70a9a7a53e7fc8b8eeca97f07d3cd806b53f0e6b0d99d4fc6fb4150cfb",
        |    "inclusionHeight": 979535,
        |    "timestamp": 1681141876198,
        |    "index": 4,
        |    "globalIndex": 5027526,
        |    "numConfirmations": 2766,
        |    "inputs": [
        |        {
        |            "boxId": "6305f6e9590eb7f8c223d9dc68c075fa1a8cb198c87df1269b798619573a8585",
        |            "value": 988626125398,
        |            "index": 0,
        |            "spendingProof": "404024b21757aafe1773d04afb0c5dd6458d77e6f72f8f3a7ffcc501bf48576998bf7c8b1dfe3c6ce775c3b823eb14dd14ef5f05e0d1fa6c",
        |            "outputBlockId": "4c5396cfddfe3f8d044f4621abe22fdf6b0ecdc3c32d64aa2946b3256666ad28",
        |            "outputTransactionId": "76a2bff0308e8d90b397859a85599e911d6c0d3ed4798c2b14c2e4687a2bf92a",
        |            "outputIndex": 1,
        |            "outputGlobalIndex": 28166712,
        |            "outputCreatedAt": 979530,
        |            "outputSettledAt": 979532,
        |            "ergoTree": "0008cd02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f8",
        |            "address": "9fSNgwa3YdXdjd1UsgSTY2qtvwZD8LTgRXdgHeFdEeTdxfa3TFJ",
        |            "assets": [
        |                {
        |                    "tokenId": "243999d854d574b1b7400df190636bc10ebdf9fa36ce1225444d1e09e54e1bfe",
        |                    "index": 0,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
        |                    "index": 1,
        |                    "amount": 17515360950256,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "8e93957e2d2db47a7c8c2550a4d17e7b4f8b39e51ceb03ba20efed62d28ecf47",
        |                    "index": 2,
        |                    "amount": 1382092830,
        |                    "name": "706fb118_c07bde48_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "ee96929c1d3617e756a1bb19028c2128cdafcfc0054c5042a88763ea029e35b6",
        |                    "index": 3,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "6f6576f80e196ba85f8404ec6a3fa8562d00197414ae8e1b9424020a0417abb3",
        |                    "index": 4,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "01dbdee0333655a38ce14e737e864064228939716b216b324f1f662b9b7f9a6c",
        |                    "index": 5,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "7e320c730ffaccbfcca99af0eeb9a4c050aa3ff7957f4bfd05c43fbed6961410",
        |                    "index": 6,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "88aa95af919a9fdac837ac517903f22c4c6baf872cef76cdf6aa0add7f41299d",
        |                    "index": 7,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
        |                    "index": 8,
        |                    "amount": 26843658,
        |                    "name": "EPOS",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "b064131bda64afb940db2fb4ba63853645df3109a85125c30fcc79e49adb690e",
        |                    "index": 9,
        |                    "amount": 279331061215,
        |                    "name": "706fb118_f3ad0c62_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c07bde48465708577d0e0b401300245987d297de2a6004116c825ba09dd26b7d",
        |                    "index": 10,
        |                    "amount": 21200000,
        |                    "name": "tSigRSV",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "f3ad0c625e63a784f42355edbee61344b012caaeab478142aa9a87e147c9533e",
        |                    "index": 11,
        |                    "amount": 2571322605,
        |                    "name": "tSigUSD",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "706fb118782c421b0b00e01e5403efb64b68e31e01048dc398f3a143eaa05b26",
        |                    "index": 12,
        |                    "amount": 3086468543046359,
        |                    "name": "tERG",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "b277d2383e091ba1eaf354ac68665789d858fe0679aae2dc956a7d5dc9d57143",
        |                    "index": 13,
        |                    "amount": 610970464225,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c7e22029868ecba3d43c385bb42b8a1c96d0114ad5261ec7e0d27a6f24254f92",
        |                    "index": 14,
        |                    "amount": 8233081,
        |                    "name": "Ergo_Spectrum Finance_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
        |                    "index": 15,
        |                    "amount": 175323667,
        |                    "name": "WT_ADA",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "04928901c08363208a29e334af73cd428f3d92d3cce8e379bcd0aee6231a421d",
        |                    "index": 16,
        |                    "amount": 1986477267,
        |                    "name": "ef802b47_30974274_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "a33c9c6677493a734a7b916a9c0d3433f4dd85c1bd791ad365878ef20d72f969",
        |                    "index": 17,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "4985602633ec1ace9ed9d7e43c8efad904e948e71ab385af4eeb13ee882f5bb7",
        |                    "index": 18,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
        |                    "index": 19,
        |                    "amount": 1632288741,
        |                    "name": "WT_ERG",
        |                    "decimals": 9,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
        |                    "index": 20,
        |                    "amount": 796981782,
        |                    "name": "SPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "ef8b8067973e3f96e3b39d54a9b53039414986392b1861ed572db67ac96f7f60",
        |                    "index": 21,
        |                    "amount": 6,
        |                    "name": "SigUSD_SigRSV_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
        |                    "index": 22,
        |                    "amount": 711,
        |                    "name": "SigUSD",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
        |                    "index": 23,
        |                    "amount": 1168,
        |                    "name": "SigRSV",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "352c644e551c7827abd5478479d7b29f1c5ab9fab24cfdaf85fad0507b7280ac",
        |                    "index": 24,
        |                    "amount": 100000,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "7ec052af1b858a64a4b492426c41f91f7d6ceba4ef235ed718a31afc5ec28e27",
        |                    "index": 25,
        |                    "amount": 200000,
        |                    "name": "546573744e616d65",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "29d10ea0b949f12ecfe9dde4d13dd83a494cd24a2c8d084ac23cb6ac87ce4a5c",
        |                    "index": 26,
        |                    "amount": 10,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "0779ec04f2fae64e87418a1ad917639d4668f78484f45df962b0dec14a2591d2",
        |                    "index": 27,
        |                    "amount": 41627,
        |                    "name": "Mi Goreng ",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "98da76cecb772029cfec3d53727d5ff37d5875691825fbba743464af0c89ce45",
        |                    "index": 28,
        |                    "amount": 476398,
        |                    "name": "ERG_Mi Goreng _LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
        |                    "index": 29,
        |                    "amount": 749202,
        |                    "name": "ergopad",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 30,
        |                    "amount": 41158943,
        |                    "name": "Ergo_ErgoPOS_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c0bfa5ebb35e9f12d072ed938349d938cb0fd07cb25da7df1de21c9113cb58db",
        |                    "index": 31,
        |                    "amount": 40,
        |                    "name": "Man-Hour",
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
        |            "boxId": "55323416b5ebaa40e91428acf849d807d83a7c5f644fce7d14da780f507dfa7a",
        |            "transactionId": "07125bda3ead0df3776de09749cd76ff97f8914663e11c88766aa5f3d931e0a5",
        |            "blockId": "778cdf70a9a7a53e7fc8b8eeca97f07d3cd806b53f0e6b0d99d4fc6fb4150cfb",
        |            "value": 2750000,
        |            "index": 0,
        |            "globalIndex": 28166946,
        |            "creationHeight": 937457,
        |            "settlementHeight": 979535,
        |            "ergoTree": "19a8041904000e208a82a413c451fec826c8d39e87b95b6104d7de30e5d883a3c5ba4236d44b58370e240008cd02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f808cd02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f80404040a040204040400040005fcffffffffffffffff0104000e200508f3623d4b2be3bdb9737b3e65644f011167eefb830d9965205f022ceda40d04060400040804e2030402050204040e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d601b2a4730000d6027301d6037302eb027303d195ed92b1a4730493b1db630872017305d805d604db63087201d605b2a5730600d606c57201d607b2a5730700d6088cb2db6308a773080002ededed938cb27204730900017202ed93c2720572039386027206730ab2db63087205730b00ededededed93cbc27207730c93d0e4c672070608720393e4c67207070e72029386028cb27204730d00017208b2db63087207730e009386028cb27204730f00019c72087e731005b2db6308720773110093860272067312b2db6308720773130090b0ada5d90109639593c272097314c1720973157316d90109599a8c7209018c72090273177318",
        |            "address": "GvRhXpGpssbggKmptUxGMJkcFX6upPZkJiYmd8KKkK6DddqRqRnVRaZGuihffBSxztiQPsTHvEe6QWSYWnDkdkMCbrDmWjgVkWcydGx9GK13F5LdfzE8HVfbL5uroLXpgE5bLg9ypF549CWF1QhhA1sipSVmond362UeRNEygWqSU56PjxyJgVBBuhVpED14dJ4hBiHJbFxBJUbXgiX6mAhekkt7WTKmPeqihvr3LjYypmfvBisB5K4CWGHW3EcedAba23JBYfbcEhm64m5zrafYuCAjHYb7pR9zduauj7iLi1hq2QZH8pxRy5PvGfhD7HLGxGVCZYXb8yixf9MMxf4aLw54AQPLnUAUgte1rbv4eD7LxfqttCF43n58VDQAvzBAkoegMChopry7vKGmcABaxuVzMDCxSKnuM5V8J1gMRy9wgkmgnZzo6UKNW6nkwKTBGEvtzwRpyrpdcy9r5KntvXePhcmyeRKUQyd3Q67bhfqx4AW1PHmBx2L3c4zBjXdoLBPTWnXHRSTDSxx2xXsZQWxVadc1eTEHNhUHQqmFo1vUn3anmvUZryF4TMec2HNbwuRa1qccJ3p5AYak6s5ybdWTk3GFU2aNtKGkHezvSEY9UxrpJksJ138xcWn85uLmw7NQG1jMvCS2AvaDQamRPdr81xzoDUddbENDfDoHFVhRXoLZeBBTJTxqws7h2wZzsR2Z3mXsXJty4sDFkwpi5DdXh3Wa7A7NB7zuWc7ZnpKBv33wiJUwSmxT",
        |            "assets": [
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 0,
        |                    "amount": 444898,
        |                    "name": "706fb118_f3ad0c62_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "79f8ced371d21cbd865608ddbe5bb398ab98bf32bef4e870ac9d993d432ecb8e",
        |            "mainChain": true
        |        },
        |        {
        |            "boxId": "d996db796bdd3021c0d6c9ae2e51aa8818b3b298eace6e625ded18fe4ac55cdc",
        |            "transactionId": "f21c01217489a4a1455fa6ec1c2b8642de2a1632be4a121ccca52da8c4a75278",
        |            "blockId": "778cdf70a9a7a53e7fc8b8eeca97f07d3cd806b53f0e6b0d99d4fc6fb4150cfb",
        |            "value": 988620925398,
        |            "index": 1,
        |            "globalIndex": 28166947,
        |            "creationHeight": 979533,
        |            "settlementHeight": 979535,
        |            "ergoTree": "0008cd02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f8",
        |            "address": "9fSNgwa3YdXdjd1UsgSTY2qtvwZD8LTgRXdgHeFdEeTdxfa3TFJ",
        |            "assets": [
        |                {
        |                    "tokenId": "243999d854d574b1b7400df190636bc10ebdf9fa36ce1225444d1e09e54e1bfe",
        |                    "index": 0,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
        |                    "index": 1,
        |                    "amount": 17515360950256,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "8e93957e2d2db47a7c8c2550a4d17e7b4f8b39e51ceb03ba20efed62d28ecf47",
        |                    "index": 2,
        |                    "amount": 1382092830,
        |                    "name": "706fb118_c07bde48_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "ee96929c1d3617e756a1bb19028c2128cdafcfc0054c5042a88763ea029e35b6",
        |                    "index": 3,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "6f6576f80e196ba85f8404ec6a3fa8562d00197414ae8e1b9424020a0417abb3",
        |                    "index": 4,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "01dbdee0333655a38ce14e737e864064228939716b216b324f1f662b9b7f9a6c",
        |                    "index": 5,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "7e320c730ffaccbfcca99af0eeb9a4c050aa3ff7957f4bfd05c43fbed6961410",
        |                    "index": 6,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "88aa95af919a9fdac837ac517903f22c4c6baf872cef76cdf6aa0add7f41299d",
        |                    "index": 7,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
        |                    "index": 8,
        |                    "amount": 26843658,
        |                    "name": "EPOS",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "b064131bda64afb940db2fb4ba63853645df3109a85125c30fcc79e49adb690e",
        |                    "index": 9,
        |                    "amount": 273744439991,
        |                    "name": "706fb118_f3ad0c62_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c07bde48465708577d0e0b401300245987d297de2a6004116c825ba09dd26b7d",
        |                    "index": 10,
        |                    "amount": 21200000,
        |                    "name": "tSigRSV",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "f3ad0c625e63a784f42355edbee61344b012caaeab478142aa9a87e147c9533e",
        |                    "index": 11,
        |                    "amount": 2571322605,
        |                    "name": "tSigUSD",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "706fb118782c421b0b00e01e5403efb64b68e31e01048dc398f3a143eaa05b26",
        |                    "index": 12,
        |                    "amount": 3086468543046359,
        |                    "name": "tERG",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "b277d2383e091ba1eaf354ac68665789d858fe0679aae2dc956a7d5dc9d57143",
        |                    "index": 13,
        |                    "amount": 610970464225,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c7e22029868ecba3d43c385bb42b8a1c96d0114ad5261ec7e0d27a6f24254f92",
        |                    "index": 14,
        |                    "amount": 8233081,
        |                    "name": "Ergo_Spectrum Finance_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
        |                    "index": 15,
        |                    "amount": 175323667,
        |                    "name": "WT_ADA",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "04928901c08363208a29e334af73cd428f3d92d3cce8e379bcd0aee6231a421d",
        |                    "index": 16,
        |                    "amount": 1986477267,
        |                    "name": "ef802b47_30974274_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "a33c9c6677493a734a7b916a9c0d3433f4dd85c1bd791ad365878ef20d72f969",
        |                    "index": 17,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "4985602633ec1ace9ed9d7e43c8efad904e948e71ab385af4eeb13ee882f5bb7",
        |                    "index": 18,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
        |                    "index": 19,
        |                    "amount": 1632288741,
        |                    "name": "WT_ERG",
        |                    "decimals": 9,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
        |                    "index": 20,
        |                    "amount": 796981782,
        |                    "name": "SPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "ef8b8067973e3f96e3b39d54a9b53039414986392b1861ed572db67ac96f7f60",
        |                    "index": 21,
        |                    "amount": 6,
        |                    "name": "SigUSD_SigRSV_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
        |                    "index": 22,
        |                    "amount": 711,
        |                    "name": "SigUSD",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
        |                    "index": 23,
        |                    "amount": 1168,
        |                    "name": "SigRSV",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "352c644e551c7827abd5478479d7b29f1c5ab9fab24cfdaf85fad0507b7280ac",
        |                    "index": 24,
        |                    "amount": 100000,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "7ec052af1b858a64a4b492426c41f91f7d6ceba4ef235ed718a31afc5ec28e27",
        |                    "index": 25,
        |                    "amount": 200000,
        |                    "name": "546573744e616d65",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "29d10ea0b949f12ecfe9dde4d13dd83a494cd24a2c8d084ac23cb6ac87ce4a5c",
        |                    "index": 26,
        |                    "amount": 10,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "0779ec04f2fae64e87418a1ad917639d4668f78484f45df962b0dec14a2591d2",
        |                    "index": 27,
        |                    "amount": 41627,
        |                    "name": "Mi Goreng ",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "98da76cecb772029cfec3d53727d5ff37d5875691825fbba743464af0c89ce45",
        |                    "index": 28,
        |                    "amount": 476398,
        |                    "name": "ERG_Mi Goreng _LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
        |                    "index": 29,
        |                    "amount": 749202,
        |                    "name": "ergopad",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |                    "index": 30,
        |                    "amount": 41158943,
        |                    "name": "Ergo_ErgoPOS_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c0bfa5ebb35e9f12d072ed938349d938cb0fd07cb25da7df1de21c9113cb58db",
        |                    "index": 31,
        |                    "amount": 40,
        |                    "name": "Man-Hour",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "b849d107b71c06e42b81ad2f0e1793c10c21ebbe2ca546b23bfd207258012bc8",
        |            "mainChain": true
        |        },
        |        {
        |            "boxId": "f027e872211e178d633ea1afc5e7aab4cfe79844f71a8f9d0373b13e34ac448b",
        |            "transactionId": "f21c01217489a4a1455fa6ec1c2b8642de2a1632be4a121ccca52da8c4a75278",
        |            "blockId": "778cdf70a9a7a53e7fc8b8eeca97f07d3cd806b53f0e6b0d99d4fc6fb4150cfb",
        |            "value": 2000000,
        |            "index": 2,
        |            "globalIndex": 28166948,
        |            "creationHeight": 979533,
        |            "settlementHeight": 979535,
        |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
        |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
        |            "assets": [],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "fafaba1bb9863204d45249674aa31c17650bb1378e2b9cf175ee27f0f9560cdc",
        |            "mainChain": true
        |        }
        |    ],
        |    "size": 2046
        |}
        |""".stripMargin
    ).toOption.get.toTransaction

  def depositOrder = {
    val a = decode[Output](
      s"""
         |{
         |    "boxId": "e69097fcf40b8173421399ff42d85678fcfdd99d33e96b6971c9d6a6d0691cde",
         |    "transactionId": "89a288c8430bbbc864bb9bd6479eb65f4e41a5307f88f4057613ca7629c0a694",
         |    "blockId": "68c4c2ed28ed909cc8331ee766d7930d6789e59bdf0e2c54822c023656910f06",
         |    "value": 3200000,
         |    "index": 0,
         |    "globalIndex": 28334348,
         |    "creationHeight": 983766,
         |    "settlementHeight": 983768,
         |    "ergoTree": "19a8041904000e20f61da4f7d651fc7a1c1bb586c91ec1fcea1ef9611461fd437176c49d9db37bb20e240008cd036508e64097e59da0a50d676e435b5d3558c761a4f91a5934e3e96f8834b3051908cd036508e64097e59da0a50d676e435b5d3558c761a4f91a5934e3e96f8834b305190404040a040204040400040005fcffffffffffffffff0104000e200508f3623d4b2be3bdb9737b3e65644f011167eefb830d9965205f022ceda40d04060400040804ba030402050204040e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d601b2a4730000d6027301d6037302eb027303d195ed92b1a4730493b1db630872017305d805d604db63087201d605b2a5730600d606c57201d607b2a5730700d6088cb2db6308a773080002ededed938cb27204730900017202ed93c2720572039386027206730ab2db63087205730b00ededededed93cbc27207730c93d0e4c672070608720393e4c67207070e72029386028cb27204730d00017208b2db63087207730e009386028cb27204730f00019c72087e731005b2db6308720773110093860272067312b2db6308720773130090b0ada5d90109639593c272097314c1720973157316d90109599a8c7209018c72090273177318",
         |    "address": "GvRhXpGpssbjbuoxujNemcKgtZz6hDVYir3wYgWzx82opcsHioTeDdKKuLsdUfJ9vL8boYX86uZ46BNK1oe87476828hCZMQwnu95Ngu4ECZSX7ZEMd3zQpA26q99KhdNKemHQgMXc8qrAo1G57FNAfn6gKW1mM8YfvaKT9bfeuGUpHnEeKuQQ95bJcjLkc3bxcs4aPvxHQvWLNnunu4TUr1hrnbSVc3xpaH6JJ2xqwRpEFTTuQjwmAtAcEPTk4yfo5ATwNMJscqfAQX6YTPJaic7bkrV5ENZKptQjnDZXGGNS3uMv7hFWWtuEHx2U8YUgXkyUSuGHXPD7awfUD8Hk9EdgBd1nVcH9JYFUDQRqgDHYgH5SqFiAVTo7Yshb7JPhf48jKEquQccVF2i2g9aMURLcsLnpG9ry4M6XG3HWQhCFrvYfUVyCqwmcpNarCb1p3fqHHavrxRo4UGtXnYWfvzhw3z1dFV8RarfRLAui6Vg57EciX96JjrYMMz2KX3Tprr7c1qRGZGfNk2ZwswJV9EebhdAC52HFYHJEvRKHKFSHs9a6aKLcA1CvwteSNdHo1xNuLLf3oEkqXbKfRVTs2Ue7J4aWx6v7TV3VH4H1Rb1HqvNf9hn5UDK1RkVkC9UjinxzAiUfZTqcEMZ99edUJBBJiKCPX64eHWENWhim4MbQYmamVxq82924aRzr5txsbHkLbkfD1UTN6ggNh7YVCPqPYG5G5jhFXPrNbs93WgnSZJpN2XtJkyuFLa",
         |    "assets": [
         |        {
         |            "tokenId": "8e93957e2d2db47a7c8c2550a4d17e7b4f8b39e51ceb03ba20efed62d28ecf47",
         |            "index": 0,
         |            "amount": 59148,
         |            "name": "706fb118_c07bde48_LP",
         |            "decimals": 0,
         |            "type": "EIP-004"
         |        }
         |    ],
         |    "additionalRegisters": {},
         |    "spentTransactionId": "2eb65a1877dbc1d5c662a13f3b42c1616ae5c789fad61f06bc349b039efe47d7",
         |    "mainChain": true
         |}
         |    """.stripMargin
    )
    a.toOption.get
  }

  val deposit2 = LmDepositV1(
    deployDepositOrder,
    PoolId.unsafeFromString("8a82a413c451fec826c8d39e87b95b6104d7de30e5d883a3c5ba4236d44b5837"),
    ErgoTreeRedeemer(
      SErgoTree.unsafeFromString("0008cd02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f8")
    ),
    LmDepositParams(
      241,
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
        |            "ergoTree": "19a8041904000e208a82a413c451fec826c8d39e87b95b6104d7de30e5d883a3c5ba4236d44b58370e240008cd02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f808cd02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f80404040a040204040400040005fcffffffffffffffff0104000e200508f3623d4b2be3bdb9737b3e65644f011167eefb830d9965205f022ceda40d04060400040804e2030402050204040e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d601b2a4730000d6027301d6037302eb027303d195ed92b1a4730493b1db630872017305d805d604db63087201d605b2a5730600d606c57201d607b2a5730700d6088cb2db6308a773080002ededed938cb27204730900017202ed93c2720572039386027206730ab2db63087205730b00ededededed93cbc27207730c93d0e4c672070608720393e4c67207070e72029386028cb27204730d00017208b2db63087207730e009386028cb27204730f00019c72087e731005b2db6308720773110093860272067312b2db6308720773130090b0ada5d90109639593c272097314c1720973157316d90109599a8c7209018c72090273177318",
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

  def depositCompoundEval = decode[Output](
    """
      |{
      |    "boxId": "95fac03fe9a0b9bd7d6d8da5c88eaf450f5448ea0678e0050bd8b6776e56199e",
      |    "transactionId": "5675e607f52a6551ba30fd12413783b72067d9a825b802f113eece58c85ae5a3",
      |    "blockId": "a1542f96d66b9397c62de3981f658b2aaa67746ca5a83e844a0e21f5b7380e4e",
      |    "value": 522000,
      |    "index": 2,
      |    "globalIndex": 28521374,
      |    "creationHeight": 988634,
      |    "settlementHeight": 988636,
      |    "ergoTree": "19bc04210400040004040404040004020601010601000400050004020404040205feffffffffffffffff010408040004020502040405020402040004000101010005000404040004060404040205fcffffffffffffffff010100d80dd601b2a5730000d602db63087201d603e4c6a7070ed604b2a4730100d605db63087204d6068cb2720573020002d607998cb27202730300027206d608e4c6a70608d609db6308a7d60ab27209730400d60bb27205730500d60c7306d60d7307d1ed938cb27202730800017203959372077309d80cd60eb2a5e4e3000400d60fb2a5e4e3010400d610b2e4c672040410730a00d611c672010804d61299721095e67211e47211e4c672010704d6138cb27209730b0001d614db6308720fd615b27209730c00d6168c721502d6177e721205d6189972169c72178c720a02d6199d9c99997e8c720b02069d9c7ee4c672040505067e7212067e721006720c7e7218067e9999730d8cb27205730e00029c7206721706eded93c2720ed07208edededed93e4c6720f0608720893e4c6720f070e720393c2720fc2a795917212730fd801d61ab27214731000eded93860272137311b27214731200938c721a018c721501939972168c721a02721893860272137313b2721473140093b27214731500720a95917219720dd801d61ab2db6308720e731600ed938c721a018c720b01927e8c721a0206997219720c95937219720d73177318958f7207731993b2db6308b2a4731a00731b0086029593b17209731c8cb27209731d00018cb27209731e0001731f7320",
      |    "address": "ynjTHNBGREHKAAK1CCraEPaaoVNSPeHDkyLv7AwgxCCeQc3wNUp36gHN7YwDVyg3M3gpwNgAvtswazW4aHC3gmTTnfgZMPrKnyVq3uhWGSchyjDpHc5B4YXstUvS6EBrxoZ8wYqiMUed3hAKfFA7Ug1fy7vE5T4SiuWbFj4sfY1o8DJZbyNYbxm4N6pALh87qvmFg3NJY6aFNpZ295LJ5R3D2U69uMrrwa2KSoHD9uCaJxwwiXp6GZoAh8LvyDibg4PT8dZFg8S4q9a3oPYJP84aJaAJiJF2u7BFw9peGiD8VJQHCCbvMd8P4vD2mEXpEhEdpNgaQT55v9RigTbayN93RnvV6SFQGHvmRoXAT2RDvXM7ZJjn7nTqzJGjZ5dQ3NKc91sRqeyGBwUysrFSWBq6tKexf4KMhcM1eX13BNrjqszDmchjgVFAzjXoxwyX5juDjwA1zwob8rcAjL8C9rF8E4KVAQBhnsAMzue6n5XqdSncuLEGjxnQAsutni8uKBtsq6nmft3zLFvdTkSFvYT811anBigszrFvnKz5ZyGaE7nZahsSUbMaag9cTxCB36iQQLSDdfzgxvJVFibF6sZ2A47Hc1eqgziM2o5NChd9GtHysRN2VjzmpZEbkXBnuc1wziKTAMLWRLvKa3vHuJtV4yfC3UTbvuBm3ScM51Cw8oi1JugqWSZGgYS2hrXgC5iumVpssBXnKgo4CTyDWP2wP7Fyyt1Trc1C5raEYcNoMisQDrtcPcpunsRvCYPAGEbAcxbGvYewh3BzHtdQD2P",
      |    "assets": [
      |        {
      |            "tokenId": "a875b8dd63b401d988ab8af0f46b89afe5c6c264edde02f6b8819f094e24cf5e",
      |            "index": 0,
      |            "amount": 10654937968,
      |            "name": "",
      |            "decimals": 0,
      |            "type": "EIP-004"
      |        },
      |        {
      |            "tokenId": "a6822e28f865c39f96004ded6f63c4fda4bcef48714d374775b886bc9f0dc313",
      |            "index": 1,
      |            "amount": 1097458610704,
      |            "name": "",
      |            "decimals": 0,
      |            "type": "EIP-004"
      |        },
      |        {
      |            "tokenId": "4f719347f6ced486fa34fc5b7c5e4d535de422051dff7da1cc52c520aa6f1f36",
      |            "index": 2,
      |            "amount": 1,
      |            "name": "Spectrum YF staking bundle",
      |            "decimals": 0,
      |            "type": "EIP-004"
      |        }
      |    ],
      |    "additionalRegisters": {
      |        "R4": {
      |            "serializedValue": "0e1a537065637472756d205946207374616b696e672062756e646c65",
      |            "sigmaType": "Coll[SByte]",
      |            "renderedValue": "537065637472756d205946207374616b696e672062756e646c65"
      |        },
      |        "R5": {
      |            "serializedValue": "0ea80154686520726570726573656e746174696f6e206f6620796f757220736861726520696e20746865207969656c64206661726d696e6720706f6f6c2028706f6f6c2069643a203864343965663730616230313564373963623961623532336164663363636230623764303535333463353938653464646638616362386232623432306234363329206f6e2074686520537065637472756d2046696e616e636520706c6174666f726d2e",
      |            "sigmaType": "Coll[SByte]",
      |            "renderedValue": "54686520726570726573656e746174696f6e206f6620796f757220736861726520696e20746865207969656c64206661726d696e6720706f6f6c2028706f6f6c2069643a203864343965663730616230313564373963623961623532336164663363636230623764303535333463353938653464646638616362386232623432306234363329206f6e2074686520537065637472756d2046696e616e636520706c6174666f726d2e"
      |        },
      |        "R6": {
      |            "serializedValue": "08cd02d800b2515acdd8c3784cf6f7e1e36eede5410c002d819cf61c187fa96ec83c05",
      |            "sigmaType": "SSigmaProp",
      |            "renderedValue": "02d800b2515acdd8c3784cf6f7e1e36eede5410c002d819cf61c187fa96ec83c05"
      |        },
      |        "R7": {
      |            "serializedValue": "0e208d49ef70ab015d79cb9ab523adf3ccb0b7d05534c598e4ddf8acb8b2b420b463",
      |            "sigmaType": "Coll[SByte]",
      |            "renderedValue": "8d49ef70ab015d79cb9ab523adf3ccb0b7d05534c598e4ddf8acb8b2b420b463"
      |        }
      |    },
      |    "spentTransactionId": "e627e21da80df01a2022419cd0dcc7cb6f2d26573bfaa06029e92dab8e538855",
      |    "mainChain": true
      |}
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
      |    "boxId": "fc7495dc9471798b5371ee0701baf0912209eccd234cadb0d80c3fb69f6031f7",
      |    "transactionId": "5675e607f52a6551ba30fd12413783b72067d9a825b802f113eece58c85ae5a3",
      |    "blockId": "a1542f96d66b9397c62de3981f658b2aaa67746ca5a83e844a0e21f5b7380e4e",
      |    "value": 522000,
      |    "index": 11,
      |    "globalIndex": 28521383,
      |    "creationHeight": 988634,
      |    "settlementHeight": 988636,
      |    "ergoTree": "19bc04210400040004040404040004020601010601000400050004020404040205feffffffffffffffff010408040004020502040405020402040004000101010005000404040004060404040205fcffffffffffffffff010100d80dd601b2a5730000d602db63087201d603e4c6a7070ed604b2a4730100d605db63087204d6068cb2720573020002d607998cb27202730300027206d608e4c6a70608d609db6308a7d60ab27209730400d60bb27205730500d60c7306d60d7307d1ed938cb27202730800017203959372077309d80cd60eb2a5e4e3000400d60fb2a5e4e3010400d610b2e4c672040410730a00d611c672010804d61299721095e67211e47211e4c672010704d6138cb27209730b0001d614db6308720fd615b27209730c00d6168c721502d6177e721205d6189972169c72178c720a02d6199d9c99997e8c720b02069d9c7ee4c672040505067e7212067e721006720c7e7218067e9999730d8cb27205730e00029c7206721706eded93c2720ed07208edededed93e4c6720f0608720893e4c6720f070e720393c2720fc2a795917212730fd801d61ab27214731000eded93860272137311b27214731200938c721a018c721501939972168c721a02721893860272137313b2721473140093b27214731500720a95917219720dd801d61ab2db6308720e731600ed938c721a018c720b01927e8c721a0206997219720c95937219720d73177318958f7207731993b2db6308b2a4731a00731b0086029593b17209731c8cb27209731d00018cb27209731e0001731f7320",
      |    "address": "ynjTHNBGREHKAAK1CCraEPaaoVNSPeHDkyLv7AwgxCCeQc3wNUp36gHN7YwDVyg3M3gpwNgAvtswazW4aHC3gmTTnfgZMPrKnyVq3uhWGSchyjDpHc5B4YXstUvS6EBrxoZ8wYqiMUed3hAKfFA7Ug1fy7vE5T4SiuWbFj4sfY1o8DJZbyNYbxm4N6pALh87qvmFg3NJY6aFNpZ295LJ5R3D2U69uMrrwa2KSoHD9uCaJxwwiXp6GZoAh8LvyDibg4PT8dZFg8S4q9a3oPYJP84aJaAJiJF2u7BFw9peGiD8VJQHCCbvMd8P4vD2mEXpEhEdpNgaQT55v9RigTbayN93RnvV6SFQGHvmRoXAT2RDvXM7ZJjn7nTqzJGjZ5dQ3NKc91sRqeyGBwUysrFSWBq6tKexf4KMhcM1eX13BNrjqszDmchjgVFAzjXoxwyX5juDjwA1zwob8rcAjL8C9rF8E4KVAQBhnsAMzue6n5XqdSncuLEGjxnQAsutni8uKBtsq6nmft3zLFvdTkSFvYT811anBigszrFvnKz5ZyGaE7nZahsSUbMaag9cTxCB36iQQLSDdfzgxvJVFibF6sZ2A47Hc1eqgziM2o5NChd9GtHysRN2VjzmpZEbkXBnuc1wziKTAMLWRLvKa3vHuJtV4yfC3UTbvuBm3ScM51Cw8oi1JugqWSZGgYS2hrXgC5iumVpssBXnKgo4CTyDWP2wP7Fyyt1Trc1C5raEYcNoMisQDrtcPcpunsRvCYPAGEbAcxbGvYewh3BzHtdQD2P",
      |    "assets": [
      |        {
      |            "tokenId": "a875b8dd63b401d988ab8af0f46b89afe5c6c264edde02f6b8819f094e24cf5e",
      |            "index": 0,
      |            "amount": 21309708902,
      |            "name": "",
      |            "decimals": 0,
      |            "type": "EIP-004"
      |        },
      |        {
      |            "tokenId": "a6822e28f865c39f96004ded6f63c4fda4bcef48714d374775b886bc9f0dc313",
      |            "index": 1,
      |            "amount": 2194900016906,
      |            "name": "",
      |            "decimals": 0,
      |            "type": "EIP-004"
      |        },
      |        {
      |            "tokenId": "62c8554720d8f396169dbfc2fd9b8ce44a82e000eb7d3363f861d15fe0387929",
      |            "index": 2,
      |            "amount": 1,
      |            "name": "Spectrum YF staking bundle",
      |            "decimals": 0,
      |            "type": "EIP-004"
      |        }
      |    ],
      |    "additionalRegisters": {
      |        "R4": {
      |            "serializedValue": "0e1a537065637472756d205946207374616b696e672062756e646c65",
      |            "sigmaType": "Coll[SByte]",
      |            "renderedValue": "537065637472756d205946207374616b696e672062756e646c65"
      |        },
      |        "R5": {
      |            "serializedValue": "0ea80154686520726570726573656e746174696f6e206f6620796f757220736861726520696e20746865207969656c64206661726d696e6720706f6f6c2028706f6f6c2069643a203864343965663730616230313564373963623961623532336164663363636230623764303535333463353938653464646638616362386232623432306234363329206f6e2074686520537065637472756d2046696e616e636520706c6174666f726d2e",
      |            "sigmaType": "Coll[SByte]",
      |            "renderedValue": "54686520726570726573656e746174696f6e206f6620796f757220736861726520696e20746865207969656c64206661726d696e6720706f6f6c2028706f6f6c2069643a203864343965663730616230313564373963623961623532336164663363636230623764303535333463353938653464646638616362386232623432306234363329206f6e2074686520537065637472756d2046696e616e636520706c6174666f726d2e"
      |        },
      |        "R6": {
      |            "serializedValue": "08cd03eaf5c35c863a98f174c6bcbb1c0b51045aa34453c137eb35b4baf96c39ec85c2",
      |            "sigmaType": "SSigmaProp",
      |            "renderedValue": "03eaf5c35c863a98f174c6bcbb1c0b51045aa34453c137eb35b4baf96c39ec85c2"
      |        },
      |        "R7": {
      |            "serializedValue": "0e208d49ef70ab015d79cb9ab523adf3ccb0b7d05534c598e4ddf8acb8b2b420b463",
      |            "sigmaType": "Coll[SByte]",
      |            "renderedValue": "8d49ef70ab015d79cb9ab523adf3ccb0b7d05534c598e4ddf8acb8b2b420b463"
      |        }
      |    },
      |    "spentTransactionId": "e627e21da80df01a2022419cd0dcc7cb6f2d26573bfaa06029e92dab8e538855",
      |    "mainChain": true
      |}
      |""".stripMargin
  ).toOption.get

  val deposit = LmDepositV1(
    depositOrder,
    PoolId.unsafeFromString("f61da4f7d651fc7a1c1bb586c91ec1fcea1ef9611461fd437176c49d9db37bb2"),
    ErgoTreeRedeemer(
      SErgoTree.unsafeFromString("0008cd036508e64097e59da0a50d676e435b5d3558c761a4f91a5934e3e96f8834b30519")
    ),
    LmDepositParams(
      221,
      AssetAmount(
        TokenId.unsafeFromString("8e93957e2d2db47a7c8c2550a4d17e7b4f8b39e51ceb03ba20efed62d28ecf47"),
        59148
      )
    ),
    2000000,
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
         |    "id": "f21c01217489a4a1455fa6ec1c2b8642de2a1632be4a121ccca52da8c4a75278",
         |    "blockId": "778cdf70a9a7a53e7fc8b8eeca97f07d3cd806b53f0e6b0d99d4fc6fb4150cfb",
         |    "inclusionHeight": 979535,
         |    "timestamp": 1681141876198,
         |    "index": 4,
         |    "globalIndex": 5027526,
         |    "numConfirmations": 2766,
         |    "inputs": [
         |        {
         |            "boxId": "6305f6e9590eb7f8c223d9dc68c075fa1a8cb198c87df1269b798619573a8585",
         |            "value": 988626125398,
         |            "index": 0,
         |            "spendingProof": "404024b21757aafe1773d04afb0c5dd6458d77e6f72f8f3a7ffcc501bf48576998bf7c8b1dfe3c6ce775c3b823eb14dd14ef5f05e0d1fa6c",
         |            "outputBlockId": "4c5396cfddfe3f8d044f4621abe22fdf6b0ecdc3c32d64aa2946b3256666ad28",
         |            "outputTransactionId": "76a2bff0308e8d90b397859a85599e911d6c0d3ed4798c2b14c2e4687a2bf92a",
         |            "outputIndex": 1,
         |            "outputGlobalIndex": 28166712,
         |            "outputCreatedAt": 979530,
         |            "outputSettledAt": 979532,
         |            "ergoTree": "0008cd02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f8",
         |            "address": "9fSNgwa3YdXdjd1UsgSTY2qtvwZD8LTgRXdgHeFdEeTdxfa3TFJ",
         |            "assets": [
         |                {
         |                    "tokenId": "243999d854d574b1b7400df190636bc10ebdf9fa36ce1225444d1e09e54e1bfe",
         |                    "index": 0,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
         |                    "index": 1,
         |                    "amount": 17515360950256,
         |                    "name": "tSPF",
         |                    "decimals": 6,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "8e93957e2d2db47a7c8c2550a4d17e7b4f8b39e51ceb03ba20efed62d28ecf47",
         |                    "index": 2,
         |                    "amount": 1382092830,
         |                    "name": "706fb118_c07bde48_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "ee96929c1d3617e756a1bb19028c2128cdafcfc0054c5042a88763ea029e35b6",
         |                    "index": 3,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "6f6576f80e196ba85f8404ec6a3fa8562d00197414ae8e1b9424020a0417abb3",
         |                    "index": 4,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "01dbdee0333655a38ce14e737e864064228939716b216b324f1f662b9b7f9a6c",
         |                    "index": 5,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "7e320c730ffaccbfcca99af0eeb9a4c050aa3ff7957f4bfd05c43fbed6961410",
         |                    "index": 6,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "88aa95af919a9fdac837ac517903f22c4c6baf872cef76cdf6aa0add7f41299d",
         |                    "index": 7,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
         |                    "index": 8,
         |                    "amount": 26843658,
         |                    "name": "EPOS",
         |                    "decimals": 4,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "b064131bda64afb940db2fb4ba63853645df3109a85125c30fcc79e49adb690e",
         |                    "index": 9,
         |                    "amount": 279331061215,
         |                    "name": "706fb118_f3ad0c62_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "c07bde48465708577d0e0b401300245987d297de2a6004116c825ba09dd26b7d",
         |                    "index": 10,
         |                    "amount": 21200000,
         |                    "name": "tSigRSV",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "f3ad0c625e63a784f42355edbee61344b012caaeab478142aa9a87e147c9533e",
         |                    "index": 11,
         |                    "amount": 2571322605,
         |                    "name": "tSigUSD",
         |                    "decimals": 2,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "706fb118782c421b0b00e01e5403efb64b68e31e01048dc398f3a143eaa05b26",
         |                    "index": 12,
         |                    "amount": 3086468543046359,
         |                    "name": "tERG",
         |                    "decimals": 8,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "b277d2383e091ba1eaf354ac68665789d858fe0679aae2dc956a7d5dc9d57143",
         |                    "index": 13,
         |                    "amount": 610970464225,
         |                    "name": "tSPF",
         |                    "decimals": 6,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "c7e22029868ecba3d43c385bb42b8a1c96d0114ad5261ec7e0d27a6f24254f92",
         |                    "index": 14,
         |                    "amount": 8233081,
         |                    "name": "Ergo_Spectrum Finance_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
         |                    "index": 15,
         |                    "amount": 175323667,
         |                    "name": "WT_ADA",
         |                    "decimals": 8,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "04928901c08363208a29e334af73cd428f3d92d3cce8e379bcd0aee6231a421d",
         |                    "index": 16,
         |                    "amount": 1986477267,
         |                    "name": "ef802b47_30974274_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "a33c9c6677493a734a7b916a9c0d3433f4dd85c1bd791ad365878ef20d72f969",
         |                    "index": 17,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "4985602633ec1ace9ed9d7e43c8efad904e948e71ab385af4eeb13ee882f5bb7",
         |                    "index": 18,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
         |                    "index": 19,
         |                    "amount": 1632288741,
         |                    "name": "WT_ERG",
         |                    "decimals": 9,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |                    "index": 20,
         |                    "amount": 796981782,
         |                    "name": "SPF",
         |                    "decimals": 6,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "ef8b8067973e3f96e3b39d54a9b53039414986392b1861ed572db67ac96f7f60",
         |                    "index": 21,
         |                    "amount": 6,
         |                    "name": "SigUSD_SigRSV_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
         |                    "index": 22,
         |                    "amount": 711,
         |                    "name": "SigUSD",
         |                    "decimals": 2,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
         |                    "index": 23,
         |                    "amount": 1168,
         |                    "name": "SigRSV",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "352c644e551c7827abd5478479d7b29f1c5ab9fab24cfdaf85fad0507b7280ac",
         |                    "index": 24,
         |                    "amount": 100000,
         |                    "name": null,
         |                    "decimals": null,
         |                    "type": null
         |                },
         |                {
         |                    "tokenId": "7ec052af1b858a64a4b492426c41f91f7d6ceba4ef235ed718a31afc5ec28e27",
         |                    "index": 25,
         |                    "amount": 200000,
         |                    "name": "546573744e616d65",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "29d10ea0b949f12ecfe9dde4d13dd83a494cd24a2c8d084ac23cb6ac87ce4a5c",
         |                    "index": 26,
         |                    "amount": 10,
         |                    "name": null,
         |                    "decimals": null,
         |                    "type": null
         |                },
         |                {
         |                    "tokenId": "0779ec04f2fae64e87418a1ad917639d4668f78484f45df962b0dec14a2591d2",
         |                    "index": 27,
         |                    "amount": 41627,
         |                    "name": "Mi Goreng ",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "98da76cecb772029cfec3d53727d5ff37d5875691825fbba743464af0c89ce45",
         |                    "index": 28,
         |                    "amount": 476398,
         |                    "name": "ERG_Mi Goreng _LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
         |                    "index": 29,
         |                    "amount": 749202,
         |                    "name": "ergopad",
         |                    "decimals": 2,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
         |                    "index": 30,
         |                    "amount": 41158943,
         |                    "name": "Ergo_ErgoPOS_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "c0bfa5ebb35e9f12d072ed938349d938cb0fd07cb25da7df1de21c9113cb58db",
         |                    "index": 31,
         |                    "amount": 40,
         |                    "name": "Man-Hour",
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
         |            "boxId": "0b48acc6447f50e01fcb035e189c3517aa7447cfa198a2079c033963dd86d885",
         |            "transactionId": "f21c01217489a4a1455fa6ec1c2b8642de2a1632be4a121ccca52da8c4a75278",
         |            "blockId": "778cdf70a9a7a53e7fc8b8eeca97f07d3cd806b53f0e6b0d99d4fc6fb4150cfb",
         |            "value": 3200000,
         |            "index": 0,
         |            "globalIndex": 28166946,
         |            "creationHeight": 979533,
         |            "settlementHeight": 979535,
         |            "ergoTree": "19a8041904000e208a82a413c451fec826c8d39e87b95b6104d7de30e5d883a3c5ba4236d44b58370e240008cd02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f808cd02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f80404040a040204040400040005fcffffffffffffffff0104000e200508f3623d4b2be3bdb9737b3e65644f011167eefb830d9965205f022ceda40d04060400040804e2030402050204040e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d601b2a4730000d6027301d6037302eb027303d195ed92b1a4730493b1db630872017305d805d604db63087201d605b2a5730600d606c57201d607b2a5730700d6088cb2db6308a773080002ededed938cb27204730900017202ed93c2720572039386027206730ab2db63087205730b00ededededed93cbc27207730c93d0e4c672070608720393e4c67207070e72029386028cb27204730d00017208b2db63087207730e009386028cb27204730f00019c72087e731005b2db6308720773110093860272067312b2db6308720773130090b0ada5d90109639593c272097314c1720973157316d90109599a8c7209018c72090273177318",
         |            "address": "GvRhXpGpssbggKmptUxGMJkcFX6upPZkJiYmd8KKkK6DddqRqRnVRaZGuihffBSxztiQPsTHvEe6QWSYWnDkdkMCbrDmWjgVkWcydGx9GK13F5LdfzE8HVfbL5uroLXpgE5bLg9ypF549CWF1QhhA1sipSVmond362UeRNEygWqSU56PjxyJgVBBuhVpED14dJ4hBiHJbFxBJUbXgiX6mAhekkt7WTKmPeqihvr3LjYypmfvBisB5K4CWGHW3EcedAba23JBYfbcEhm64m5zrafYuCAjHYb7pR9zduauj7iLi1hq2QZH8pxRy5PvGfhD7HLGxGVCZYXb8yixf9MMxf4aLw54AQPLnUAUgte1rbv4eD7LxfqttCF43n58VDQAvzBAkoegMChopry7vKGmcABaxuVzMDCxSKnuM5V8J1gMRy9wgkmgnZzo6UKNW6nkwKTBGEvtzwRpyrpdcy9r5KntvXePhcmyeRKUQyd3Q67bhfqx4AW1PHmBx2L3c4zBjXdoLBPTWnXHRSTDSxx2xXsZQWxVadc1eTEHNhUHQqmFo1vUn3anmvUZryF4TMec2HNbwuRa1qccJ3p5AYak6s5ybdWTk3GFU2aNtKGkHezvSEY9UxrpJksJ138xcWn85uLmw7NQG1jMvCS2AvaDQamRPdr81xzoDUddbENDfDoHFVhRXoLZeBBTJTxqws7h2wZzsR2Z3mXsXJty4sDFkwpi5DdXh3Wa7A7NB7zuWc7ZnpKBv33wiJUwSmxT",
         |            "assets": [
         |                {
         |                    "tokenId": "b064131bda64afb940db2fb4ba63853645df3109a85125c30fcc79e49adb690e",
         |                    "index": 0,
         |                    "amount": 5586621224,
         |                    "name": "706fb118_f3ad0c62_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                }
         |            ],
         |            "additionalRegisters": {},
         |            "spentTransactionId": "79f8ced371d21cbd865608ddbe5bb398ab98bf32bef4e870ac9d993d432ecb8e",
         |            "mainChain": true
         |        },
         |        {
         |            "boxId": "d996db796bdd3021c0d6c9ae2e51aa8818b3b298eace6e625ded18fe4ac55cdc",
         |            "transactionId": "f21c01217489a4a1455fa6ec1c2b8642de2a1632be4a121ccca52da8c4a75278",
         |            "blockId": "778cdf70a9a7a53e7fc8b8eeca97f07d3cd806b53f0e6b0d99d4fc6fb4150cfb",
         |            "value": 988620925398,
         |            "index": 1,
         |            "globalIndex": 28166947,
         |            "creationHeight": 979533,
         |            "settlementHeight": 979535,
         |            "ergoTree": "0008cd02790df2ef284bbec57fb8c39a58919fa6d08ea566b3a500f20255d9ebe2ec38f8",
         |            "address": "9fSNgwa3YdXdjd1UsgSTY2qtvwZD8LTgRXdgHeFdEeTdxfa3TFJ",
         |            "assets": [
         |                {
         |                    "tokenId": "243999d854d574b1b7400df190636bc10ebdf9fa36ce1225444d1e09e54e1bfe",
         |                    "index": 0,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
         |                    "index": 1,
         |                    "amount": 17515360950256,
         |                    "name": "tSPF",
         |                    "decimals": 6,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "8e93957e2d2db47a7c8c2550a4d17e7b4f8b39e51ceb03ba20efed62d28ecf47",
         |                    "index": 2,
         |                    "amount": 1382092830,
         |                    "name": "706fb118_c07bde48_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "ee96929c1d3617e756a1bb19028c2128cdafcfc0054c5042a88763ea029e35b6",
         |                    "index": 3,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "6f6576f80e196ba85f8404ec6a3fa8562d00197414ae8e1b9424020a0417abb3",
         |                    "index": 4,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "01dbdee0333655a38ce14e737e864064228939716b216b324f1f662b9b7f9a6c",
         |                    "index": 5,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "7e320c730ffaccbfcca99af0eeb9a4c050aa3ff7957f4bfd05c43fbed6961410",
         |                    "index": 6,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "88aa95af919a9fdac837ac517903f22c4c6baf872cef76cdf6aa0add7f41299d",
         |                    "index": 7,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
         |                    "index": 8,
         |                    "amount": 26843658,
         |                    "name": "EPOS",
         |                    "decimals": 4,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "b064131bda64afb940db2fb4ba63853645df3109a85125c30fcc79e49adb690e",
         |                    "index": 9,
         |                    "amount": 273744439991,
         |                    "name": "706fb118_f3ad0c62_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "c07bde48465708577d0e0b401300245987d297de2a6004116c825ba09dd26b7d",
         |                    "index": 10,
         |                    "amount": 21200000,
         |                    "name": "tSigRSV",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "f3ad0c625e63a784f42355edbee61344b012caaeab478142aa9a87e147c9533e",
         |                    "index": 11,
         |                    "amount": 2571322605,
         |                    "name": "tSigUSD",
         |                    "decimals": 2,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "706fb118782c421b0b00e01e5403efb64b68e31e01048dc398f3a143eaa05b26",
         |                    "index": 12,
         |                    "amount": 3086468543046359,
         |                    "name": "tERG",
         |                    "decimals": 8,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "b277d2383e091ba1eaf354ac68665789d858fe0679aae2dc956a7d5dc9d57143",
         |                    "index": 13,
         |                    "amount": 610970464225,
         |                    "name": "tSPF",
         |                    "decimals": 6,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "c7e22029868ecba3d43c385bb42b8a1c96d0114ad5261ec7e0d27a6f24254f92",
         |                    "index": 14,
         |                    "amount": 8233081,
         |                    "name": "Ergo_Spectrum Finance_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
         |                    "index": 15,
         |                    "amount": 175323667,
         |                    "name": "WT_ADA",
         |                    "decimals": 8,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "04928901c08363208a29e334af73cd428f3d92d3cce8e379bcd0aee6231a421d",
         |                    "index": 16,
         |                    "amount": 1986477267,
         |                    "name": "ef802b47_30974274_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "a33c9c6677493a734a7b916a9c0d3433f4dd85c1bd791ad365878ef20d72f969",
         |                    "index": 17,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "4985602633ec1ace9ed9d7e43c8efad904e948e71ab385af4eeb13ee882f5bb7",
         |                    "index": 18,
         |                    "amount": 9223372036854775806,
         |                    "name": "Spectrum YF staking bundle",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
         |                    "index": 19,
         |                    "amount": 1632288741,
         |                    "name": "WT_ERG",
         |                    "decimals": 9,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
         |                    "index": 20,
         |                    "amount": 796981782,
         |                    "name": "SPF",
         |                    "decimals": 6,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "ef8b8067973e3f96e3b39d54a9b53039414986392b1861ed572db67ac96f7f60",
         |                    "index": 21,
         |                    "amount": 6,
         |                    "name": "SigUSD_SigRSV_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
         |                    "index": 22,
         |                    "amount": 711,
         |                    "name": "SigUSD",
         |                    "decimals": 2,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
         |                    "index": 23,
         |                    "amount": 1168,
         |                    "name": "SigRSV",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "352c644e551c7827abd5478479d7b29f1c5ab9fab24cfdaf85fad0507b7280ac",
         |                    "index": 24,
         |                    "amount": 100000,
         |                    "name": null,
         |                    "decimals": null,
         |                    "type": null
         |                },
         |                {
         |                    "tokenId": "7ec052af1b858a64a4b492426c41f91f7d6ceba4ef235ed718a31afc5ec28e27",
         |                    "index": 25,
         |                    "amount": 200000,
         |                    "name": "546573744e616d65",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "29d10ea0b949f12ecfe9dde4d13dd83a494cd24a2c8d084ac23cb6ac87ce4a5c",
         |                    "index": 26,
         |                    "amount": 10,
         |                    "name": null,
         |                    "decimals": null,
         |                    "type": null
         |                },
         |                {
         |                    "tokenId": "0779ec04f2fae64e87418a1ad917639d4668f78484f45df962b0dec14a2591d2",
         |                    "index": 27,
         |                    "amount": 41627,
         |                    "name": "Mi Goreng ",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "98da76cecb772029cfec3d53727d5ff37d5875691825fbba743464af0c89ce45",
         |                    "index": 28,
         |                    "amount": 476398,
         |                    "name": "ERG_Mi Goreng _LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
         |                    "index": 29,
         |                    "amount": 749202,
         |                    "name": "ergopad",
         |                    "decimals": 2,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
         |                    "index": 30,
         |                    "amount": 41158943,
         |                    "name": "Ergo_ErgoPOS_LP",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                },
         |                {
         |                    "tokenId": "c0bfa5ebb35e9f12d072ed938349d938cb0fd07cb25da7df1de21c9113cb58db",
         |                    "index": 31,
         |                    "amount": 40,
         |                    "name": "Man-Hour",
         |                    "decimals": 0,
         |                    "type": "EIP-004"
         |                }
         |            ],
         |            "additionalRegisters": {},
         |            "spentTransactionId": "b849d107b71c06e42b81ad2f0e1793c10c21ebbe2ca546b23bfd207258012bc8",
         |            "mainChain": true
         |        },
         |        {
         |            "boxId": "f027e872211e178d633ea1afc5e7aab4cfe79844f71a8f9d0373b13e34ac448b",
         |            "transactionId": "f21c01217489a4a1455fa6ec1c2b8642de2a1632be4a121ccca52da8c4a75278",
         |            "blockId": "778cdf70a9a7a53e7fc8b8eeca97f07d3cd806b53f0e6b0d99d4fc6fb4150cfb",
         |            "value": 2000000,
         |            "index": 2,
         |            "globalIndex": 28166948,
         |            "creationHeight": 979533,
         |            "settlementHeight": 979535,
         |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
         |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
         |            "assets": [],
         |            "additionalRegisters": {},
         |            "spentTransactionId": "fafaba1bb9863204d45249674aa31c17650bb1378e2b9cf175ee27f0f9560cdc",
         |            "mainChain": true
         |        }
         |    ],
         |    "size": 2046
         |}
         |""".stripMargin
    ).toOption.get.toTransaction

  def redeemOrderOutput = decode[Output](
    s"""{
       |    "boxId": "64e208695208d3e6b53f557627e9aa25ba715351644f497108151a282f5a3801",
       |    "transactionId": "823e62fa52388bfe1ebfef1016f3985ff71e17799e30f765d8d76a6a076387c2",
       |    "blockId": "00d1fc7cce357a444e3708a2e328147827d6e6c0892b76c3450e5b0c37653e3c",
       |    "value": 2800000,
       |    "index": 0,
       |    "globalIndex": 28294294,
       |    "creationHeight": 982657,
       |    "settlementHeight": 982659,
       |    "ergoTree": "19d8020e08cd038e59c2aa7cc9153cb1acd6a51a31558312806d3d6786a9615091efd63d47732e04040400040a04020e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010e240008cd038e59c2aa7cc9153cb1acd6a51a31558312806d3d6786a9615091efd63d47732e0e20b064131bda64afb940db2fb4ba63853645df3109a85125c30fcc79e49adb690e05e4aeae2704000100eb027300d195ed92b1a4730193b1db6308b2a47302007303d802d601b2a5730400d60290b0ada5d90102639593c272027305c1720273067307d90102599a8c7202018c7202027308ededed93c272017309938602730a730bb2db63087201730c0072027202730d",
       |    "address": "De4ip5fBoDjFMie97R5Hh249Jx4jb7cPeWJ4FGYAdi3YGQzkaZ7fHF3ZYZDZp3MhTUwQp8pFAceMRni3sikP4khWtBfquey8Xezn3RRHWJmJBUnjqh2KCSx8ijsC5y1ysewsfF6rxq2H92nT6abHsmi8Mahf6FSqmuiPq5dBfXkJYh1izFQdRdznjoDGosBrRs8PkNTPTrjDpU2CxXv2urhJScfhUfG49akR8W5iJ6J6pRVTNpzovKcrodL7e7kLVds7yD3YMgA7BrTA8uB1D65L9ZJTJFEj86H1jrB54ZGnVVDyQtDWr2tchfuLwMTzX6CFFQ3uVA2JJqHnAGKCQaRxDtz2jibUYPwZp18DBd86xvAisSW7uZrsZqJ1Zp4v35UPhDi3DAkAZhne6rxGSRJCYg4h1sRddcj8opVUNdBkqMpnb1GpQArQrebGCATeDEHTjqDGRXo9cnWWKp3dsTizdGNFPirA",
       |    "assets": [
       |        {
       |            "tokenId": "f2828c999bc2d4a1f10d39f8f830e3a4231917f652c71c2d264dbfbd1825861c",
       |            "index": 0,
       |            "amount": 9223372036854775806,
       |            "name": "Spectrum YF staking bundle",
       |            "decimals": 0,
       |            "type": "EIP-004"
       |        }
       |    ],
       |    "additionalRegisters": {},
       |    "spentTransactionId": "da1f0f8a9a30f62ad5d70a197ab350fd61564d39e698210eb43d56f823f6a326",
       |    "mainChain": true
       |}""".stripMargin
  ).toOption.get

  val redeem = LmRedeemV1(
    redeemOrderOutput,
    TokenId.unsafeFromString("f2828c999bc2d4a1f10d39f8f830e3a4231917f652c71c2d264dbfbd1825861c"),
    AssetAmount(
      TokenId.unsafeFromString("b064131bda64afb940db2fb4ba63853645df3109a85125c30fcc79e49adb690e"),
      41274290
    ),
    2000000,
    ErgoTreeRedeemer(SErgoTree.unsafeFromString("0008cd038e59c2aa7cc9153cb1acd6a51a31558312806d3d6786a9615091efd63d47732e")),
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
        |    "id": "4a20c9847c24107ab9c8ccf2d3f0e9ecc7069b8a622da9325c7b075afde89209",
        |    "blockId": "4df5ca097145cbc27a51892b7a12230d77deb7b46e026505f789743fd6a9236e",
        |    "inclusionHeight": 983222,
        |    "timestamp": 1681586340067,
        |    "index": 6,
        |    "globalIndex": 5054319,
        |    "numConfirmations": 18252,
        |    "inputs": [
        |        {
        |            "boxId": "4aac11d3426902a4c273abbd489ca4ea026d2a92dbf0aa0cbb8be1d09c78e2ca",
        |            "value": 522000,
        |            "index": 0,
        |            "spendingProof": "6f74d09b184c5094057b9a0dd9dc2b9819c4bb2ed7c0aca157b95c0e9415473eeca54a7796ed718e693f21042c9ca5a0ad04f1228ff30811",
        |            "outputBlockId": "ea8a33596cc4ad515d00eb3e83fabc181f0d52cea65d3a9e2fba83f65815ee29",
        |            "outputTransactionId": "fbb217ddfa5ba89fe65e8207470d9bad73dfad0c33f592651578b7152f16adc0",
        |            "outputIndex": 1,
        |            "outputGlobalIndex": 28247382,
        |            "outputCreatedAt": 981571,
        |            "outputSettledAt": 981573,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "410821b5fa2caaf1d63582e4462936f652e24a7390780e4a1fcccd7f1113b30d",
        |                    "index": 0,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {
        |                "R4": {
        |                    "serializedValue": "0e1a537065637472756d205946207374616b696e672062756e646c65",
        |                    "sigmaType": "Coll[SByte]",
        |                    "renderedValue": "537065637472756d205946207374616b696e672062756e646c65"
        |                },
        |                "R5": {
        |                    "serializedValue": "0ea80154686520726570726573656e746174696f6e206f6620796f757220736861726520696e20746865207969656c64206661726d696e6720706f6f6c2028706f6f6c2069643a203438323339633962663133346434613835316463376263353332653437643863386139623164336334346163643431383533393961366633383266363533643029206f6e2074686520537065637472756d2046696e616e636520706c6174666f726d2e",
        |                    "sigmaType": "Coll[SByte]",
        |                    "renderedValue": "54686520726570726573656e746174696f6e206f6620796f757220736861726520696e20746865207969656c64206661726d696e6720706f6f6c2028706f6f6c2069643a203438323339633962663133346434613835316463376263353332653437643863386139623164336334346163643431383533393961366633383266363533643029206f6e2074686520537065637472756d2046696e616e636520706c6174666f726d2e"
        |                }
        |            }
        |        },
        |        {
        |            "boxId": "7d94b523c7fd6f929bc18c39c695f45a6bb9921f523fa1a7f5cbd31f2d75fb24",
        |            "value": 522000,
        |            "index": 1,
        |            "spendingProof": "8932e93b08a3efb36bf982bba32ebcd2774597da47ea4e8241ac57a7f0cc5cb0613c86bde4a32536c9f5995f4362fe6fd0e8f7e4d7cd32f9",
        |            "outputBlockId": "7e45a8200f0ef8258c913cf2e99453451d56b28bd85d9560ee0666b8a7d24c9c",
        |            "outputTransactionId": "8649aa25737c41776d57b1db783edddccb9ee5003922e5e5c6bf143d8b56a2c3",
        |            "outputIndex": 1,
        |            "outputGlobalIndex": 28085998,
        |            "outputCreatedAt": 977520,
        |            "outputSettledAt": 977522,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "194d26482444a142fdb75917f7db6e2a0c63478f8b11bde8c0c72c71227abf5c",
        |                    "index": 0,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {
        |                "R4": {
        |                    "serializedValue": "0e1a537065637472756d205946207374616b696e672062756e646c65",
        |                    "sigmaType": "Coll[SByte]",
        |                    "renderedValue": "537065637472756d205946207374616b696e672062756e646c65"
        |                },
        |                "R5": {
        |                    "serializedValue": "0ea80154686520726570726573656e746174696f6e206f6620796f757220736861726520696e20746865207969656c64206661726d696e6720706f6f6c2028706f6f6c2069643a206636316461346637643635316663376131633162623538366339316563316663656131656639363131343631666434333731373663343964396462333762623229206f6e2074686520537065637472756d2046696e616e636520706c6174666f726d2e",
        |                    "sigmaType": "Coll[SByte]",
        |                    "renderedValue": "54686520726570726573656e746174696f6e206f6620796f757220736861726520696e20746865207969656c64206661726d696e6720706f6f6c2028706f6f6c2069643a206636316461346637643635316663376131633162623538366339316563316663656131656639363131343631666434333731373663343964396462333762623229206f6e2074686520537065637472756d2046696e616e636520706c6174666f726d2e"
        |                }
        |            }
        |        },
        |        {
        |            "boxId": "e29c8351c870697b734ece5258ac35004dee68b8e9589312033db8d6c8b4ddf4",
        |            "value": 522000,
        |            "index": 2,
        |            "spendingProof": "bf85b1c1c0cd6e6b5327304bdfb387291fb811f45d80182472b8b09ccd938ae6a29d2e019e533da18ebecc1945b66e2ecbf36f48ea911710",
        |            "outputBlockId": "7e45a8200f0ef8258c913cf2e99453451d56b28bd85d9560ee0666b8a7d24c9c",
        |            "outputTransactionId": "99a54d90843aba355478bed5a54dcd99dce1b8f6fa7212a774556e4e38581806",
        |            "outputIndex": 1,
        |            "outputGlobalIndex": 28086003,
        |            "outputCreatedAt": 977520,
        |            "outputSettledAt": 977522,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "ed3180856e7935bdc6d1e986535358c6e69fa3f2bf3a5b41986d86aae6ad000c",
        |                    "index": 0,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {
        |                "R4": {
        |                    "serializedValue": "0e1a537065637472756d205946207374616b696e672062756e646c65",
        |                    "sigmaType": "Coll[SByte]",
        |                    "renderedValue": "537065637472756d205946207374616b696e672062756e646c65"
        |                },
        |                "R5": {
        |                    "serializedValue": "0ea80154686520726570726573656e746174696f6e206f6620796f757220736861726520696e20746865207969656c64206661726d696e6720706f6f6c2028706f6f6c2069643a203864343965663730616230313564373963623961623532336164663363636230623764303535333463353938653464646638616362386232623432306234363329206f6e2074686520537065637472756d2046696e616e636520706c6174666f726d2e",
        |                    "sigmaType": "Coll[SByte]",
        |                    "renderedValue": "54686520726570726573656e746174696f6e206f6620796f757220736861726520696e20746865207969656c64206661726d696e6720706f6f6c2028706f6f6c2069643a203864343965663730616230313564373963623961623532336164663363636230623764303535333463353938653464646638616362386232623432306234363329206f6e2074686520537065637472756d2046696e616e636520706c6174666f726d2e"
        |                }
        |            }
        |        },
        |        {
        |            "boxId": "6f33568d53c3dfd64e3e53f10547e0a3d61cd31ba277cd054aad4d7b1821f163",
        |            "value": 400000,
        |            "index": 3,
        |            "spendingProof": "af3b94593e447c75ce09f02b1cfa8d838f6318f3d54b33a8f63d29b82b0344fb58b2390de308a393d87db1c5cee0245facc67784613fb8c0",
        |            "outputBlockId": "64009244b869ca43d555a50d219daa8e8e8c1b9c17d54995830b5af05b8d4e0f",
        |            "outputTransactionId": "d5558bf173828d5cf04f439555ad4e7d76612cb9254c96e25f383a13a757e2b9",
        |            "outputIndex": 1,
        |            "outputGlobalIndex": 28086555,
        |            "outputCreatedAt": 977541,
        |            "outputSettledAt": 977543,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
        |                    "index": 0,
        |                    "amount": 22106,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                }
        |            ],
        |            "additionalRegisters": {}
        |        },
        |        {
        |            "boxId": "9385b310995cd215c3798ed444736736f3505fac0eb74810e0a58bbafd02ea45",
        |            "value": 250000,
        |            "index": 4,
        |            "spendingProof": "4de9f2bd4693a5df1acd648b16b8684534837c01d5fcea0e646daac9d54aa1515be53cf142b46ddc831728c6254ca9893685b29d8bc74258",
        |            "outputBlockId": "5249f08a8c807bbd0beefd40ce42c714607e549a4b2b21800f14bd6932e31532",
        |            "outputTransactionId": "d941d0b7c8de520620a8bd37e7fe0abe407f9946706b6223de8d6ebc756906fb",
        |            "outputIndex": 14,
        |            "outputGlobalIndex": 28094546,
        |            "outputCreatedAt": 977850,
        |            "outputSettledAt": 977853,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
        |                    "index": 0,
        |                    "amount": 47047535,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {}
        |        },
        |        {
        |            "boxId": "400cc845964a4c98927136bae9ce610762fa0feb9d5ef1280327bf900e699f4d",
        |            "value": 250000,
        |            "index": 5,
        |            "spendingProof": "43cd89881cec46221e7c25b356fbd3a034cbe6ec452a6ce0ada40aef51f4e6942e56fa8638ab9ad4f52476d23c78943fd5877e869eb85e71",
        |            "outputBlockId": "7e44d05c8f9d6b9b06de568dac14196e01eefb7c857574f2f4757a38298b33d7",
        |            "outputTransactionId": "ada2f9cbd02d346bac055f65fc466225c8a246ad63f84e6c74570db546443bfa",
        |            "outputIndex": 14,
        |            "outputGlobalIndex": 28102111,
        |            "outputCreatedAt": 978100,
        |            "outputSettledAt": 978102,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
        |                    "index": 0,
        |                    "amount": 47224664,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {}
        |        },
        |        {
        |            "boxId": "3b108dd94634fc3b9d16d4c7b50f504192b02b05d2d22f6963d1d015289c936a",
        |            "value": 250000,
        |            "index": 6,
        |            "spendingProof": "b619339167313a46c7d488c30fcb219536c60c99b82dca1ce9e332ca4b5f01be49d4861e4bfbc93ba696b8b5559497b19c6d6b6fcf30fda5",
        |            "outputBlockId": "a4231b0292c16191742833769ef82620d02b6893f9482d4fdb1f5752ef5e15c0",
        |            "outputTransactionId": "18dadad391a6431fb22667d9a67d5e5adcb4f42324df267ada617b7be8aa6b0e",
        |            "outputIndex": 14,
        |            "outputGlobalIndex": 28113571,
        |            "outputCreatedAt": 978352,
        |            "outputSettledAt": 978354,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
        |                    "index": 0,
        |                    "amount": 47224663,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {}
        |        },
        |        {
        |            "boxId": "50941f43490dccec19d70c7a56160c5a765bd1523de038f2a30eb0b04705f505",
        |            "value": 250000,
        |            "index": 7,
        |            "spendingProof": "3e42744446aa1636c80c98770a205cb802e42650ea40f873f962240094ae5da8fd619df5728d6b1cd501237ae61d07af580ba59741e22b84",
        |            "outputBlockId": "8255f707234fbc14bffd2d517dfb7c459ed950fbe5dc5e190de79cd9cc6b478f",
        |            "outputTransactionId": "87a6693ab31d1706088498f036f640503bed7264030f8b32af914b7c9f5fdd26",
        |            "outputIndex": 13,
        |            "outputGlobalIndex": 28115551,
        |            "outputCreatedAt": 978400,
        |            "outputSettledAt": 978403,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
        |                    "index": 0,
        |                    "amount": 349453,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {}
        |        },
        |        {
        |            "boxId": "bf16674255cac14caaaacb34ab5413d7baef6cec4a42eef23042b3dc7cb6c58a",
        |            "value": 250000,
        |            "index": 8,
        |            "spendingProof": "b7e20e536825f1e1e59c8c735451a774008b877aa9d3dabebe0c1ce8609666b7a417d5c87ecf4f85047ad7450c86c192e79694d64cdd6b11",
        |            "outputBlockId": "f2f4853c504cd6bbb5e09cdb974f69fb4fd4c9eaf31d06ffa8eeb8aa97ff5d1d",
        |            "outputTransactionId": "d2ba987d9727dee94a67e4319adf2da2ebbaf62b195003f005c915d5bc270e06",
        |            "outputIndex": 14,
        |            "outputGlobalIndex": 28127262,
        |            "outputCreatedAt": 978601,
        |            "outputSettledAt": 978603,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
        |                    "index": 0,
        |                    "amount": 47224664,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {}
        |        },
        |        {
        |            "boxId": "64d9d3f5e75030805a0aeb83dfe5b5dc533942623fc0b5ee37292f5d442cf2cd",
        |            "value": 250000,
        |            "index": 9,
        |            "spendingProof": "57867e0c2282c0f4deb7738b52fb478c9af34eb122d1fe8a9664067038de9422044a228926eb41652316d5bbce5647b176fc41d880ee8251",
        |            "outputBlockId": "7a3375df56db29982549e51e69e30ec247f38df60050e889439c3e40c164d06f",
        |            "outputTransactionId": "bf64e930dd34754c17f3c77b0796d55174ae902ce46d89e72caf6efaa2d8be87",
        |            "outputIndex": 14,
        |            "outputGlobalIndex": 28143648,
        |            "outputCreatedAt": 978850,
        |            "outputSettledAt": 978853,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
        |                    "index": 0,
        |                    "amount": 47224664,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {}
        |        },
        |        {
        |            "boxId": "0c3d491d51c207b3ace385e0ce0237f00e7e35ebdba4843446f8a0922e1c11e3",
        |            "value": 250000,
        |            "index": 10,
        |            "spendingProof": "89b887e26c69b04d86dc6542c6efa227778dbd0f20fc57f4ced3b9800520c5fc53d9ad49b432b31a39c5924f93e08b33b92a6c4cc95da64d",
        |            "outputBlockId": "9403599f966f9ea7ec28e48df86e8b5ffc0700578fa5e0a653ddbb6e681d72af",
        |            "outputTransactionId": "bb8bad454f06592f7c9f2f926247421828aa34da65d01f233dd4678ea20341a8",
        |            "outputIndex": 15,
        |            "outputGlobalIndex": 28145265,
        |            "outputCreatedAt": 978900,
        |            "outputSettledAt": 978902,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
        |                    "index": 0,
        |                    "amount": 349411,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {}
        |        },
        |        {
        |            "boxId": "90561fea7fa87ab55765fc9b1b8f1be1a16d09ac2d96aebacbd3acc66a9f0f31",
        |            "value": 250000,
        |            "index": 11,
        |            "spendingProof": "652fa6d1bc4ee8c8d807e7ba6dd7ac81805a900c6ba3bf0b4f581687578fe640a79c2f0767038738e3f97eb5acf26a153eee3078a109af79",
        |            "outputBlockId": "da17641d2ef09be6e6fba4cf0fab79b62e3a30948f1520f12b5d846ef867a9c7",
        |            "outputTransactionId": "0669d5b7b793d8eed4c7376cdfe5173907a825ec48bcddb3ef5571b2d6c64675",
        |            "outputIndex": 14,
        |            "outputGlobalIndex": 28151108,
        |            "outputCreatedAt": 979100,
        |            "outputSettledAt": 979102,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
        |                    "index": 0,
        |                    "amount": 47224663,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {}
        |        },
        |        {
        |            "boxId": "116d01e8921bc7d8619427172f3e573cc6aa0fa8a811f5a0b373a85d8b81ed22",
        |            "value": 28990327,
        |            "index": 12,
        |            "spendingProof": "f917419e52478028366bd9c39c00686133ed4b7fa2b7d98654acb82ed473d3cead44a0d61001c9330cbc6fc3827436e8223a83ea227aa2ef",
        |            "outputBlockId": "6f7f653bba9032e7217756bd6536e5b0b5fb934ab9cb91516dc80dbb83593787",
        |            "outputTransactionId": "97c6e2a54641d878e5c6f3da6666062bdd68a0c21a5b792995ddf63ee039efc6",
        |            "outputIndex": 1,
        |            "outputGlobalIndex": 28160857,
        |            "outputCreatedAt": 979336,
        |            "outputSettledAt": 979338,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "f3ad0c625e63a784f42355edbee61344b012caaeab478142aa9a87e147c9533e",
        |                    "index": 0,
        |                    "amount": 95010984580,
        |                    "name": "tSigUSD",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
        |                    "index": 1,
        |                    "amount": 2314522,
        |                    "name": "SPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "01dce8a5632d19799950ff90bca3b5d0ca3ebfa8aaafd06f0cc6dd1e97150e7f",
        |                    "index": 2,
        |                    "amount": 998766,
        |                    "name": "CYPX",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
        |                    "index": 3,
        |                    "amount": 250088805418,
        |                    "name": "WT_ADA",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "b277d2383e091ba1eaf354ac68665789d858fe0679aae2dc956a7d5dc9d57143",
        |                    "index": 4,
        |                    "amount": 10064065736,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "04928901c08363208a29e334af73cd428f3d92d3cce8e379bcd0aee6231a421d",
        |                    "index": 5,
        |                    "amount": 271263979133,
        |                    "name": "ef802b47_30974274_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
        |                    "index": 6,
        |                    "amount": 10281500,
        |                    "name": "NETA",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c7e22029868ecba3d43c385bb42b8a1c96d0114ad5261ec7e0d27a6f24254f92",
        |                    "index": 7,
        |                    "amount": 6671801,
        |                    "name": "Ergo_Spectrum Finance_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "27a9c163d2d9acef611e7354f5dee8aaedbf4dd82c3e819d6f6bf58b3c54acc2",
        |                    "index": 8,
        |                    "amount": 10000000,
        |                    "name": "Hello Staker whitelist token",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "00b1e236b60b95c2c6f8007a9d89bc460fc9e78f98b09faec9449007b40bccf3",
        |                    "index": 9,
        |                    "amount": 16893818,
        |                    "name": "EGIO",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "abaf9df62c6810b0198333b122d130c29d76d64c6d7f4d57108afc5aaa95efb5",
        |                    "index": 10,
        |                    "amount": 284028,
        |                    "name": "AirdropTest",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "59ee24951ce668f0ed32bdb2e2e5731b6c36128748a3b23c28407c5f8ccbf0f6",
        |                    "index": 11,
        |                    "amount": 180,
        |                    "name": "WALRUS",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
        |                    "index": 12,
        |                    "amount": 50029,
        |                    "name": "ergopad",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e7c57701553d04f4c23bf157fff61bfe749f0296404bbd7578233764734e6702",
        |                    "index": 13,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
        |                    "index": 14,
        |                    "amount": 2,
        |                    "name": "WT_ERG",
        |                    "decimals": 9,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
        |                    "index": 15,
        |                    "amount": 50,
        |                    "name": "SigUSD",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "4d876819dd5517c56c525c5cb9e2428a7affa0741c0464379e6c130a6be730ea",
        |                    "index": 16,
        |                    "amount": 3467497641905,
        |                    "name": "Spectrum Finance Community whitelist token",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
        |                    "index": 17,
        |                    "amount": 81,
        |                    "name": "SigRSV",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "089990451bb430f05a85f4ef3bcb6ebf852b3d6ee68d86d78658b9ccef20074f",
        |                    "index": 18,
        |                    "amount": 762397,
        |                    "name": "QUACKS",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "cddbf2a3e52a66e56d6e9a1b5f05502c5a6870f3e7a6142e4f4b044895244364",
        |                    "index": 19,
        |                    "amount": 20000000,
        |                    "name": "Hello Community whitelist token",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40",
        |                    "index": 20,
        |                    "amount": 100,
        |                    "name": "kushti",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "706fb118782c421b0b00e01e5403efb64b68e31e01048dc398f3a143eaa05b26",
        |                    "index": 21,
        |                    "amount": 94983877800000000,
        |                    "name": "tERG",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c07bde48465708577d0e0b401300245987d297de2a6004116c825ba09dd26b7d",
        |                    "index": 22,
        |                    "amount": 947556000,
        |                    "name": "tSigRSV",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
        |                    "index": 23,
        |                    "amount": 949797350000000,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {}
        |        }
        |    ],
        |    "dataInputs": [],
        |    "outputs": [
        |        {
        |            "boxId": "00165c410829ff0cc2d176acb1fe8e37d7b65d597faf11f5ce210ef8457cd780",
        |            "transactionId": "4a20c9847c24107ab9c8ccf2d3f0e9ecc7069b8a622da9325c7b075afde89209",
        |            "blockId": "4df5ca097145cbc27a51892b7a12230d77deb7b46e026505f789743fd6a9236e",
        |            "value": 2800000,
        |            "index": 0,
        |            "globalIndex": 28316319,
        |            "creationHeight": 983220,
        |            "settlementHeight": 983222,
        |            "ergoTree": "19d9020e08cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd04040400040a04020e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010e240008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd0e20cb0b42a58d38104c906444b43d1e03c8d553f79b06244cb4c98d93b46875040e058cd2f6800c04000100eb027300d195ed92b1a4730193b1db6308b2a47302007303d802d601b2a5730400d60290b0ada5d90102639593c272027305c1720273067307d90102599a8c7202018c7202027308ededed93c272017309938602730a730bb2db63087201730c0072027202730d",
        |            "address": "xncM7F4awrrW8csJeHFxuPAZioboYcuF9R5rqfWykGccrRUqru9UGt8PDyqEXb61sVopZ7KTpYFWUPiuKJsrmpZfaCUPdGi1wCM3tQLgCjJGb3WU4BX1rphmTEFZy5v3iWX8J6rFhz8dv4EpPznVpFhV2enwkmjqsHyhdPLytD1vfWTa2WRfzb9HtSzm2t8wTYzLab2EftY9EWLsnLBN8zdvjivTj9NBMwZRwGZ2F584DLjYX7kpA9SsukzjfDtdhYTsogqA1HHNohKTfKJ8ojEhcMVMjwbCZYrNBWXMzG7uFtwHKXjX8GJNpP3eu4QxQ6BrVdmFBgniq8zUPwgZ925QHhm6RunbxFVpvmfN8gMGbe4QCQ3CCNjzDPqrThAcBqrKaBDbZv54PKs7Vm1TU5bdeME28n1gNStY5ThLi12tZQUaM7rf7jCeoK2a1BPAU8Mg3J5k48Wa1Tpe6oRCEs7bPFYg78ovs",
        |            "assets": [
        |                {
        |                    "tokenId": "410821b5fa2caaf1d63582e4462936f652e24a7390780e4a1fcccd7f1113b30d",
        |                    "index": 0,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "f69bc9a8b82012e3eebba33e17501facd3023f4ab690bb6b6d8d39846f180d2b",
        |            "mainChain": true
        |        },
        |        {
        |            "boxId": "d487a171be42f51708760f45c87653b786b238949a8fa7e077706193374c8ec9",
        |            "transactionId": "4a20c9847c24107ab9c8ccf2d3f0e9ecc7069b8a622da9325c7b075afde89209",
        |            "blockId": "4df5ca097145cbc27a51892b7a12230d77deb7b46e026505f789743fd6a9236e",
        |            "value": 28156327,
        |            "index": 1,
        |            "globalIndex": 28316320,
        |            "creationHeight": 983220,
        |            "settlementHeight": 983222,
        |            "ergoTree": "0008cd03cd01fd142c909e45108695c46ada3b4c2793dfdebf5609730ca88a8cf066e5fd",
        |            "address": "9i26Lf4u2nNKBSqzXqCmHe5JZBxV1B7AcrhvtLHAq7TniW1THBY",
        |            "assets": [
        |                {
        |                    "tokenId": "194d26482444a142fdb75917f7db6e2a0c63478f8b11bde8c0c72c71227abf5c",
        |                    "index": 0,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "ed3180856e7935bdc6d1e986535358c6e69fa3f2bf3a5b41986d86aae6ad000c",
        |                    "index": 1,
        |                    "amount": 9223372036854775806,
        |                    "name": "Spectrum YF staking bundle",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
        |                    "index": 2,
        |                    "amount": 22106,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "50eab1afc495420c17c0df5154584cf09d9167263fffc99c25e3e0ae4b26fe00",
        |                    "index": 3,
        |                    "amount": 949797633869717,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "f3ad0c625e63a784f42355edbee61344b012caaeab478142aa9a87e147c9533e",
        |                    "index": 4,
        |                    "amount": 95010984580,
        |                    "name": "tSigUSD",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "9a06d9e545a41fd51eeffc5e20d818073bf820c635e2a9d922269913e0de369d",
        |                    "index": 5,
        |                    "amount": 2314522,
        |                    "name": "SPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "01dce8a5632d19799950ff90bca3b5d0ca3ebfa8aaafd06f0cc6dd1e97150e7f",
        |                    "index": 6,
        |                    "amount": 998766,
        |                    "name": "CYPX",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
        |                    "index": 7,
        |                    "amount": 250088805418,
        |                    "name": "WT_ADA",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "b277d2383e091ba1eaf354ac68665789d858fe0679aae2dc956a7d5dc9d57143",
        |                    "index": 8,
        |                    "amount": 10064065736,
        |                    "name": "tSPF",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "04928901c08363208a29e334af73cd428f3d92d3cce8e379bcd0aee6231a421d",
        |                    "index": 9,
        |                    "amount": 271263979133,
        |                    "name": "ef802b47_30974274_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
        |                    "index": 10,
        |                    "amount": 10281500,
        |                    "name": "NETA",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c7e22029868ecba3d43c385bb42b8a1c96d0114ad5261ec7e0d27a6f24254f92",
        |                    "index": 11,
        |                    "amount": 6671801,
        |                    "name": "Ergo_Spectrum Finance_LP",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "27a9c163d2d9acef611e7354f5dee8aaedbf4dd82c3e819d6f6bf58b3c54acc2",
        |                    "index": 12,
        |                    "amount": 10000000,
        |                    "name": "Hello Staker whitelist token",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "00b1e236b60b95c2c6f8007a9d89bc460fc9e78f98b09faec9449007b40bccf3",
        |                    "index": 13,
        |                    "amount": 16893818,
        |                    "name": "EGIO",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "abaf9df62c6810b0198333b122d130c29d76d64c6d7f4d57108afc5aaa95efb5",
        |                    "index": 14,
        |                    "amount": 284028,
        |                    "name": "AirdropTest",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "59ee24951ce668f0ed32bdb2e2e5731b6c36128748a3b23c28407c5f8ccbf0f6",
        |                    "index": 15,
        |                    "amount": 180,
        |                    "name": "WALRUS",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
        |                    "index": 16,
        |                    "amount": 50029,
        |                    "name": "ergopad",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "e7c57701553d04f4c23bf157fff61bfe749f0296404bbd7578233764734e6702",
        |                    "index": 17,
        |                    "amount": 9223372036854775806,
        |                    "name": null,
        |                    "decimals": null,
        |                    "type": null
        |                },
        |                {
        |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
        |                    "index": 18,
        |                    "amount": 2,
        |                    "name": "WT_ERG",
        |                    "decimals": 9,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
        |                    "index": 19,
        |                    "amount": 50,
        |                    "name": "SigUSD",
        |                    "decimals": 2,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "4d876819dd5517c56c525c5cb9e2428a7affa0741c0464379e6c130a6be730ea",
        |                    "index": 20,
        |                    "amount": 3467497641905,
        |                    "name": "Spectrum Finance Community whitelist token",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
        |                    "index": 21,
        |                    "amount": 81,
        |                    "name": "SigRSV",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "089990451bb430f05a85f4ef3bcb6ebf852b3d6ee68d86d78658b9ccef20074f",
        |                    "index": 22,
        |                    "amount": 762397,
        |                    "name": "QUACKS",
        |                    "decimals": 6,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "cddbf2a3e52a66e56d6e9a1b5f05502c5a6870f3e7a6142e4f4b044895244364",
        |                    "index": 23,
        |                    "amount": 20000000,
        |                    "name": "Hello Community whitelist token",
        |                    "decimals": 4,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40",
        |                    "index": 24,
        |                    "amount": 100,
        |                    "name": "kushti",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "706fb118782c421b0b00e01e5403efb64b68e31e01048dc398f3a143eaa05b26",
        |                    "index": 25,
        |                    "amount": 94983877800000000,
        |                    "name": "tERG",
        |                    "decimals": 8,
        |                    "type": "EIP-004"
        |                },
        |                {
        |                    "tokenId": "c07bde48465708577d0e0b401300245987d297de2a6004116c825ba09dd26b7d",
        |                    "index": 26,
        |                    "amount": 947556000,
        |                    "name": "tSigRSV",
        |                    "decimals": 0,
        |                    "type": "EIP-004"
        |                }
        |            ],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "fdef45ff8837b05c224d79ae30122b397430310bfecd026ca5c8bb02b9789765",
        |            "mainChain": true
        |        },
        |        {
        |            "boxId": "f91eafb023455e1d1005688df02efb2e71f8043d37c1e8fc9f1134cc1a261f84",
        |            "transactionId": "4a20c9847c24107ab9c8ccf2d3f0e9ecc7069b8a622da9325c7b075afde89209",
        |            "blockId": "4df5ca097145cbc27a51892b7a12230d77deb7b46e026505f789743fd6a9236e",
        |            "value": 2000000,
        |            "index": 2,
        |            "globalIndex": 28316321,
        |            "creationHeight": 983220,
        |            "settlementHeight": 983222,
        |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
        |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
        |            "assets": [],
        |            "additionalRegisters": {},
        |            "spentTransactionId": "0b7f12e3dd5fb8b74d0f658604bb741b4fb99bcc5a3f9e3d4b4f35e789e8fd6e",
        |            "mainChain": true
        |        }
        |    ],
        |    "size": 2745
        |}
        |""".stripMargin
    ).toOption.get.toTransaction

}
