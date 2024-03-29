package fi.spectrum.parser.lm.compound.v1

import cats.effect.IO
import fi.spectrum.core.domain.{AssetAmount, PubKey, TokenId}
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.order.Order.Compound.CompoundV1
import fi.spectrum.core.domain.order.{LmCompoundParams, Order, OrderState, OrderStatus, PoolId}
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.models.{InputTest, OutputTest, TransactionTest}
import io.circe.parser.decode
import cats.syntax.option._
import fi.spectrum.core.domain.analytics.Processed
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.evaluation.ProcessedOrderParser

import java.io.File
import scala.io.Source

object Compound {

  val compoundNotLastEpochOutput = decode[Output](
    s"""
       |{
       |    "boxId": "24bf92d7af14a2e56d83fc1d9e335ec629672f89763f9e33cf30f45d53595b49",
       |    "transactionId": "38503bcdbca4925dbc99b2d94e17f8e4ab998fec6e56a418f846c54109025a35",
       |    "blockId": "7ccd3d91784b6873025b57b5260fa51f7635c157c2669e0616cc0518c194d1c2",
       |    "value": 300000,
       |    "index": 2,
       |    "globalIndex": 26483092,
       |    "creationHeight": 938652,
       |    "settlementHeight": 938657,
       |    "ergoTree": "19a3041f040004000404040404000402060101040005000402040404020400040004020502040405020402040005feffffffffffffffff010408050205000404040004060404040205fcffffffffffffffff010100d80cd601b2a5730000d602db63087201d603e4c6a7050ed604b2a4730100d605db63087204d6068cb2720573020002d607998cb27202730300027206d608e4c6a70408d609db6308a7d60ab27209730400d60bb27205730500d60c7306d1ed938cb27202730700017203959372077308d80cd60db2a5e4e3000400d60eb2a5e4e3010400d60fb2e4c672040410730900d610c672010804d61199720f95e67210e47210e4c672010704d6128cb27209730a0001d613db6308720ed614b27209730b00d6158c721402d6167e721105d6179972159c72168c720a02d618b2db6308720d730c00eded93c2720dd07208edededed93e4c6720e0408720893e4c6720e050e720393c2720ec2a795917211730dd801d619b27213730e00eded9386027212730fb27213731000938c7219018c721401939972158c721902721793860272127311b2721373120093b27213731300720aed938c7218018c720b01927e8c7218020699999d9c99997e8c720b02069d9c7ee4c672040505067e7211067e720f06720c7e7217067e999973148cb27205731500029c9972067316721606720c720c958f7207731793b2db6308b2a473180073190086029593b17209731a8cb27209731b00018cb27209731c0001731d731e",
       |    "address": "YyLFSfjMQw5AJEuchDNPzziDH7CpLX8EbtGbddMpTpDoYYCc5xKJUbDuFnwsJ4qcpARjLmTQdRUTDiftnkb4kuNAaM7enwfxbWvh9SAbpvxAtJzhQApmedrEH9BBrPYzHdWbR354ZXZJkazhvfWMLyijyFeJ7aTDYmhDn8YvF3HeBMJiLByT32wTfNVEWQf4SEp3eN66ppqZs7bJAxrsZo3PDGJgTtZ2qPypMv3x9fcE6zYoLiCzHbAWC8WCfn5wNno9NAwSgWgHKcz3Gx4kXAe5DJnMDZJeiAuckS4e8J5sUfgFSPHTysvkfPw5pxBBNoxrsQ8y2jHSjB3KhSKKsNoZLnWZpbLQ7TurFc34TZJn9FNsRzpmQc9ftcZNa1QxKNoBtFzCpRXwvKd1RaSEZc5er7TwiyL9ebcfh1x3b3k2qXZhi6eXnktGBhEcNiJoJe7FdRA7gTXs9MCQewX8r7u3ay6i94bT9BrdXZy7xnmFU1DvyCcNP3sHHC1gDbNxdzVYsyF9tuxuXhmmD3rTc7hCPX4uo1dahq5HFaaQifp8kwe6iVuzp3Qo5vNPgV8tJBzXhGx7wgYoozGznh1ecYdBtCpHdEwzbME4bQy8qLXsBRB7TVnhQ2GYUL8XyCpzaziWEe1tdKrgbFFE1CNnLuGR7FEFikx5PfJVFTmFvpEvvtM7Qq9TMFjgsaoP58M7BajhU74EgqVRX3hi8pmmzxnjAzVKK5wZR62RcpowjrYsNLGjcWL8B",
       |    "assets": [
       |        {
       |            "tokenId": "ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0",
       |            "index": 0,
       |            "amount": 444898,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        },
       |        {
       |            "tokenId": "bc58f841398f84fd0a72e2de50f8f3aed14e6234f7502bb7fe90729fba9c74f2",
       |            "index": 1,
       |            "amount": 889796,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        },
       |        {
       |            "tokenId": "00738c0e3c4d89a1a2d5e71a20e4a00262340f289e58d9d866719564a933bad5",
       |            "index": 2,
       |            "amount": 1,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        }
       |    ],
       |    "additionalRegisters": {
       |        "R4": {
       |            "serializedValue": "08cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec",
       |            "sigmaType": "SSigmaProp",
       |            "renderedValue": "03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec"
       |        },
       |        "R5": {
       |            "serializedValue": "0e200f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f",
       |            "sigmaType": "Coll[SByte]",
       |            "renderedValue": "0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f"
       |        }
       |    },
       |    "spentTransactionId": "3156e7598a5944accd90232d8eb18da3ab1b72e7340445d75ad23ce4c0951f7b",
       |    "mainChain": true
       |}
       |""".stripMargin
  ).toOption.get

  val compoundNotLastEpoch = CompoundV1(
    compoundNotLastEpochOutput,
    LmCompoundParams(
      AssetAmount(
        TokenId.unsafeFromString("ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0"),
        444898
      ),
      AssetAmount(
        TokenId.unsafeFromString("bc58f841398f84fd0a72e2de50f8f3aed14e6234f7502bb7fe90729fba9c74f2"),
        889796
      ).some,
      AssetAmount(
        TokenId.unsafeFromString("00738c0e3c4d89a1a2d5e71a20e4a00262340f289e58d9d866719564a933bad5"),
        1
      ).tokenId
    ),
    PoolId.unsafeFromString("0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f"),
    PublicKeyRedeemer(PubKey.unsafeFromString("03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec")),
    V1
  )

  val compoundLastEpochOutput = decode[Output](
    s"""
       |{
       |    "boxId": "24bf92d7af14a2e56d83fc1d9e335ec629672f89763f9e33cf30f45d53595b49",
       |    "transactionId": "38503bcdbca4925dbc99b2d94e17f8e4ab998fec6e56a418f846c54109025a35",
       |    "blockId": "7ccd3d91784b6873025b57b5260fa51f7635c157c2669e0616cc0518c194d1c2",
       |    "value": 300000,
       |    "index": 2,
       |    "globalIndex": 26483092,
       |    "creationHeight": 938652,
       |    "settlementHeight": 938657,
       |    "ergoTree": "19a3041f040004000404040404000402060101040005000402040404020400040004020502040405020402040005feffffffffffffffff010408050205000404040004060404040205fcffffffffffffffff010100d80cd601b2a5730000d602db63087201d603e4c6a7050ed604b2a4730100d605db63087204d6068cb2720573020002d607998cb27202730300027206d608e4c6a70408d609db6308a7d60ab27209730400d60bb27205730500d60c7306d1ed938cb27202730700017203959372077308d80cd60db2a5e4e3000400d60eb2a5e4e3010400d60fb2e4c672040410730900d610c672010804d61199720f95e67210e47210e4c672010704d6128cb27209730a0001d613db6308720ed614b27209730b00d6158c721402d6167e721105d6179972159c72168c720a02d618b2db6308720d730c00eded93c2720dd07208edededed93e4c6720e0408720893e4c6720e050e720393c2720ec2a795917211730dd801d619b27213730e00eded9386027212730fb27213731000938c7219018c721401939972158c721902721793860272127311b2721373120093b27213731300720aed938c7218018c720b01927e8c7218020699999d9c99997e8c720b02069d9c7ee4c672040505067e7211067e720f06720c7e7217067e999973148cb27205731500029c9972067316721606720c720c958f7207731793b2db6308b2a473180073190086029593b17209731a8cb27209731b00018cb27209731c0001731d731e",
       |    "address": "YyLFSfjMQw5AJEuchDNPzziDH7CpLX8EbtGbddMpTpDoYYCc5xKJUbDuFnwsJ4qcpARjLmTQdRUTDiftnkb4kuNAaM7enwfxbWvh9SAbpvxAtJzhQApmedrEH9BBrPYzHdWbR354ZXZJkazhvfWMLyijyFeJ7aTDYmhDn8YvF3HeBMJiLByT32wTfNVEWQf4SEp3eN66ppqZs7bJAxrsZo3PDGJgTtZ2qPypMv3x9fcE6zYoLiCzHbAWC8WCfn5wNno9NAwSgWgHKcz3Gx4kXAe5DJnMDZJeiAuckS4e8J5sUfgFSPHTysvkfPw5pxBBNoxrsQ8y2jHSjB3KhSKKsNoZLnWZpbLQ7TurFc34TZJn9FNsRzpmQc9ftcZNa1QxKNoBtFzCpRXwvKd1RaSEZc5er7TwiyL9ebcfh1x3b3k2qXZhi6eXnktGBhEcNiJoJe7FdRA7gTXs9MCQewX8r7u3ay6i94bT9BrdXZy7xnmFU1DvyCcNP3sHHC1gDbNxdzVYsyF9tuxuXhmmD3rTc7hCPX4uo1dahq5HFaaQifp8kwe6iVuzp3Qo5vNPgV8tJBzXhGx7wgYoozGznh1ecYdBtCpHdEwzbME4bQy8qLXsBRB7TVnhQ2GYUL8XyCpzaziWEe1tdKrgbFFE1CNnLuGR7FEFikx5PfJVFTmFvpEvvtM7Qq9TMFjgsaoP58M7BajhU74EgqVRX3hi8pmmzxnjAzVKK5wZR62RcpowjrYsNLGjcWL8B",
       |    "assets": [
       |        {
       |            "tokenId": "ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0",
       |            "index": 0,
       |            "amount": 444898,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        },
       |        {
       |            "tokenId": "00738c0e3c4d89a1a2d5e71a20e4a00262340f289e58d9d866719564a933bad5",
       |            "index": 1,
       |            "amount": 1,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        }
       |    ],
       |    "additionalRegisters": {
       |        "R4": {
       |            "serializedValue": "08cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec",
       |            "sigmaType": "SSigmaProp",
       |            "renderedValue": "03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec"
       |        },
       |        "R5": {
       |            "serializedValue": "0e200f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f",
       |            "sigmaType": "Coll[SByte]",
       |            "renderedValue": "0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f"
       |        }
       |    },
       |    "spentTransactionId": "3156e7598a5944accd90232d8eb18da3ab1b72e7340445d75ad23ce4c0951f7b",
       |    "mainChain": true
       |}
       |""".stripMargin
  ).toOption.get

  val compoundLastEpoch = CompoundV1(
    compoundLastEpochOutput,
    LmCompoundParams(
      AssetAmount(
        TokenId.unsafeFromString("ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0"),
        444898
      ),
      none,
      AssetAmount(
        TokenId.unsafeFromString("00738c0e3c4d89a1a2d5e71a20e4a00262340f289e58d9d866719564a933bad5"),
        1
      ).tokenId
    ),
    PoolId.unsafeFromString("0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f"),
    PublicKeyRedeemer(PubKey.unsafeFromString("03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec")),
    V1
  )

  val compoundTx = decode[TransactionTest](
    """{
      |    "id": "b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e",
      |    "blockId": "39a3222f60392d7fd0fc0069f919930e81a5bc722024dfefd490446220d0fd9e",
      |    "inclusionHeight": 923469,
      |    "timestamp": 1674355639070,
      |    "index": 9,
      |    "globalIndex": 4651974,
      |    "numConfirmations": 15431,
      |    "inputs": [
      |        {
      |            "boxId": "b2ca1990e7ae05e2398d72f5d3ffd8aadac4093a9be7372e854572a5e8171a6d",
      |            "value": 1250000,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "14fc13fd2aba4c0485f382275730bf6d38b9fae3729e276a76e8da1cc852b5d4",
      |            "outputTransactionId": "5661db740d5b51c60ae5a935f8192704f784f24565c0089badf7637184c68ae2",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 25800332,
      |            "outputCreatedAt": 922361,
      |            "outputSettledAt": 922365,
      |            "ergoTree": "19ec052404000400040204020404040404060406040804080404040204000400040204020400040a050005000404040204020e2074aeba0675c10c7fff46d3aa5e5a8efc55f0b0d87393dcb2f4b0a04be213cecb040004020500040204020406050005000402050205000500d81ed601b2a5730000d602db63087201d603db6308a7d604b27203730100d605e4c6a70410d606e4c6a70505d607e4c6a70605d608b27202730200d609b27203730300d60ab27202730400d60bb27203730500d60cb27202730600d60db27203730700d60e8c720d01d60fb27202730800d610b27203730900d6118c721001d6128c720b02d613998c720a027212d6148c720902d615b27205730a00d6169a99a37215730bd617b27205730c00d6189d72167217d61995919e72167217730d9a7218730e7218d61ab27205730f00d61b7e721a05d61c9d7206721bd61d998c720c028c720d02d61e998c720f028c721002d1ededededed93b272027310007204ededed93e4c672010410720593e4c672010505720693e4c6720106057207928cc77201018cc7a70193c27201c2a7ededed938c7208018c720901938c720a018c720b01938c720c01720e938c720f01721193b172027311959172137312d802d61f9c721399721ba273137e721905d620b2a5731400ededed929a997206721472079c7e9995907219721a72199a721a7315731605721c937213f0721d93721ff0721eedededed93cbc272207317938602720e7213b2db630872207318009386027211721fb2db63087220731900e6c67220040893e4c67220050e8c720401958f7213731aededec929a997206721472079c7e9995907219721a72199a721a731b731c05721c92a39a9a72159c721a7217b27205731d0093721df0721392721e95917219721a731e9c721d99721ba2731f7e721905d801d61fe4c672010704edededed90721f9972197320909972149c7e99721a721f05721c9a721c7207907ef0998c7208027214069d9c7e721c067e721e067e997212732106937213732293721d7323",
      |            "address": "59vY1ythmk1fP9eARqmN5oC6FfVtHJT39QEDaWQer7kfcLj36rqREDEumtssJkgDshwzfZ5DZ2AuJWBf9TjEHq7jhrV8hqM6nG9xrDME7MKSDN5AHPdFwnqEYJpQKN746bo8wL9VJmZxCDvx6q3xu2uoRT3MY5mJfwWfUbJfxy6AaFiFDbNdQK4MGUXJ5zkNsiNYYwoaieChFvsCAgeqTUJ1xr2MjR39ZzgxSiGD5oHMG2WWWaanfBh6SQZWTNzdru3btGbWYUb2Sn7QaCLrb74wzzAx77yFnFmoM8EvUFs3mMrB7Pi4KbRJgc4N16u8qetACg9DZfSsZLpcbxRLhCFD2y8dzfHvcfXv5dXrVGdpTnRw9vgrTC9uiim42fAudJrCZ6H8u6kTFQhXE4db16pgS7LWGCo2Zafn5uFhQWHRn4wYsvz9w6CkT4TXRgW88dqYG11Lu1h4WRdLVYkeJWsZ22Jt9HTyBpB1HGG4Spi6W2WDmJU6SnzPMZ1QgdFnc2aAtBB8etMCnmTWELj3gbBEfMCDb2V6UUa4BXQFvxQ5i1W8JrmByozg4AJojJoHVhd3MMUqopNsdM4p9wfWrbhHnZQjsCxKeDzjZdxj3g6sjj26RdNDQUNvdnpJtxP3m6F9U9pMJ3j75c87Jz7E3vogLE9oz9iuSDLvEXAG5xD9yz8SbSYf49gvHGApdUS8Ze8s5q3Wt9B9LUqpk5X1x2fUsmqyvqy53W3VyXh28KcUQWm2Liohj8WgcJ7Ge2CDcBp74r9KBjLjuwQLbvFqNWtLGTZDuRA3dx8153keEfANNiaeVG7zPG3vjg9adhXzkVpoP5A8WCdJ1wSuUR2LMa9iGaoFMKVncEQLmv8fzcjMTVn2bs9xzRKDvpxFH7KPS2ZUxThGbzw9Ud2Gc4ws8HhskKr7DrMC9L6WBCpkM1inPtZ9hRq9TYGLfShqvAVtBo1kyWJJH2PjUU5Asooy1Hzhg72CFJQLT9fuihmXpPd5cpXNapsiTLjRmrSGPKj3QhmfXW4Z",
      |            "assets": [
      |                {
      |                    "tokenId": "7956620de75192984d639cab2c989269d9a5310ad870ad547426952a9e660699",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "0779ec04f2fae64e87418a1ad917639d4668f78484f45df962b0dec14a2591d2",
      |                    "index": 1,
      |                    "amount": 300000,
      |                    "name": "Mi Goreng ",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "98da76cecb772029cfec3d53727d5ff37d5875691825fbba743464af0c89ce45",
      |                    "index": 2,
      |                    "amount": 283146,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "3fdce3da8d364f13bca60998c20660c79c19923f44e141df01349d2e63651e86",
      |                    "index": 3,
      |                    "amount": 99716855,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "c256908dd9fd477bde350be6a41c0884713a1b1d589357ae731763455ef28c10",
      |                    "index": 4,
      |                    "amount": 1496035870,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "100490031eeeca70c801",
      |                    "sigmaType": "Coll[SInt]",
      |                    "renderedValue": "[200,15,922295,100]"
      |                },
      |                "R5": {
      |                    "serializedValue": "05becf24",
      |                    "sigmaType": "SLong",
      |                    "renderedValue": "299999"
      |                },
      |                "R6": {
      |                    "serializedValue": "05d00f",
      |                    "sigmaType": "SLong",
      |                    "renderedValue": "1000"
      |                }
      |            }
      |        },
      |        {
      |            "boxId": "9b71fabbd71122fcd6e903bb5f2767eae646e21c507ad55bd9708cb9736c1130",
      |            "value": 9988750000,
      |            "index": 1,
      |            "spendingProof": "6146d8acbf405f8b601aeb7982da55c9f9341138331a54a9cbcf22915acce7e483b9eba3be7d748c3f50cc1c105d5434fdc261e51e4fe1c8",
      |            "outputBlockId": "14abe023efaaff3bcbeb62a72d73b728254d43a54a7cd18ef1b2cfb8d68ea062",
      |            "outputTransactionId": "ed304ddbd34a25c606f360622fb658f6d0ac543150c4e5834e106e470c72d738",
      |            "outputIndex": 1,
      |            "outputGlobalIndex": 25699056,
      |            "outputCreatedAt": 920017,
      |            "outputSettledAt": 920019,
      |            "ergoTree": "0008cd03b0b2ce2839747f5e4d3c304a0dd67b47f29649a48e19c5cbb7b93aad106911df",
      |            "address": "9hodDWDNFhzmpY6qQm8spYNpbbus2jYoGEe6yFvM5uC8CfZfnMi",
      |            "assets": [],
      |            "additionalRegisters": {}
      |        },
      |        {
      |            "boxId": "cd60e696446bc3dcbaeff50a44419e37f8747f5de6972fb23c080458da726c4c",
      |            "value": 1250000,
      |            "index": 2,
      |            "spendingProof": null,
      |            "outputBlockId": "9f59e382a37f438936dbb6505f8106def63893432fd6d14eff3a3b304a0b833f",
      |            "outputTransactionId": "a9681d918f889612fd5b40a99a0a5024a92ded161757be69f156d4bdbc264775",
      |            "outputIndex": 2,
      |            "outputGlobalIndex": 25793071,
      |            "outputCreatedAt": 922213,
      |            "outputSettledAt": 922219,
      |            "ergoTree": "19a3041f040004000404040404000402060101040005000402040404020400040004020502040405020402040005feffffffffffffffff010408050205000404040004060404040205fcffffffffffffffff010100d80cd601b2a5730000d602db63087201d603e4c6a7050ed604b2a4730100d605db63087204d6068cb2720573020002d607998cb27202730300027206d608e4c6a70408d609db6308a7d60ab27209730400d60bb27205730500d60c7306d1ed938cb27202730700017203959372077308d80cd60db2a5e4e3000400d60eb2a5e4e3010400d60fb2e4c672040410730900d610c672010804d61199720f95e67210e47210e4c672010704d6128cb27209730a0001d613db6308720ed614b27209730b00d6158c721402d6167e721105d6179972159c72168c720a02d618b2db6308720d730c00eded93c2720dd07208edededed93e4c6720e0408720893e4c6720e050e720393c2720ec2a795917211730dd801d619b27213730e00eded9386027212730fb27213731000938c7219018c721401939972158c721902721793860272127311b2721373120093b27213731300720aed938c7218018c720b01927e8c7218020699999d9c99997e8c720b02069d9c7ee4c672040505067e7211067e720f06720c7e7217067e999973148cb27205731500029c9972067316721606720c720c958f7207731793b2db6308b2a473180073190086029593b17209731a8cb27209731b00018cb27209731c0001731d731e",
      |            "address": "LDQVxoiCdZqP7uW7FP1DMaqiZQ9dfMob93zMYKX9X7sXupnJZ6Lmn8u5C3T2Qgiw9gQoiw9xQfLCn8zWagMECeQDUEsKUnbUk4fAYLeySrg2DRuC7BsUYM2Cwux55bz5QuvpEsQeE8iM9xxr4t4YNrhKrY9T8ytB4y8bWVjuSPHzTJSr4gfqieD3agt4Avf5Ctf461aBqS4v4H2XvHsfrbokmqEUE7G94PPLP6p4EsdNXqzX9jqQyGZSiXz2vuxx3H3WZM4ZcKFfva1T3NKyKxRQ9FfZM5wsEdL2WcrntSnRcNdjn541ewYoeYEHMV5Jbvu9sgb75Cm8EBpcj31Jox4TVdfQ87w94LCnvU2RvNdc5JnBq149KDnugY3SjNJ81ZZF57LuMWqm6JQfEUANjw3CiZQpdm6LFKmGy3jdk4tr2KiToZj71JNjRCnqH5VCSJJLERXHj6BKDM9V4rjycRxhCN79auYSWvGxgbSSeZRJD7xkeGSxbAG56hN9is3BGSaBBVa7TNDwYMDG7juTVVCCP3K1WNyVJpfZJK6EDANECWgeqrFLSVZ42JafrhdWjE1KwEttz39MF67nELBuQsXpWYQV2VmEhcv",
      |            "assets": [
      |                {
      |                    "tokenId": "3fdce3da8d364f13bca60998c20660c79c19923f44e141df01349d2e63651e86",
      |                    "index": 0,
      |                    "amount": 100,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "c256908dd9fd477bde350be6a41c0884713a1b1d589357ae731763455ef28c10",
      |                    "index": 1,
      |                    "amount": 1500,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "251177a50ed3d4df8fc8575b3d9e03a0ba81f506a329a3ba7d8bb20994303794",
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
      |                    "serializedValue": "0e207956620de75192984d639cab2c989269d9a5310ad870ad547426952a9e660699",
      |                    "sigmaType": "Coll[SByte]",
      |                    "renderedValue": "7956620de75192984d639cab2c989269d9a5310ad870ad547426952a9e660699"
      |                }
      |            }
      |        }
      |    ],
      |    "dataInputs": [],
      |    "outputs": [
      |        {
      |            "boxId": "02f3a00879812244911a4e3075470d605d100bb02c13d7f4152083a6c8f096ae",
      |            "transactionId": "b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e",
      |            "blockId": "39a3222f60392d7fd0fc0069f919930e81a5bc722024dfefd490446220d0fd9e",
      |            "value": 1250000,
      |            "index": 0,
      |            "globalIndex": 25847029,
      |            "creationHeight": 923467,
      |            "settlementHeight": 923469,
      |            "ergoTree": "19ec052404000400040204020404040404060406040804080404040204000400040204020400040a050005000404040204020e2074aeba0675c10c7fff46d3aa5e5a8efc55f0b0d87393dcb2f4b0a04be213cecb040004020500040204020406050005000402050205000500d81ed601b2a5730000d602db63087201d603db6308a7d604b27203730100d605e4c6a70410d606e4c6a70505d607e4c6a70605d608b27202730200d609b27203730300d60ab27202730400d60bb27203730500d60cb27202730600d60db27203730700d60e8c720d01d60fb27202730800d610b27203730900d6118c721001d6128c720b02d613998c720a027212d6148c720902d615b27205730a00d6169a99a37215730bd617b27205730c00d6189d72167217d61995919e72167217730d9a7218730e7218d61ab27205730f00d61b7e721a05d61c9d7206721bd61d998c720c028c720d02d61e998c720f028c721002d1ededededed93b272027310007204ededed93e4c672010410720593e4c672010505720693e4c6720106057207928cc77201018cc7a70193c27201c2a7ededed938c7208018c720901938c720a018c720b01938c720c01720e938c720f01721193b172027311959172137312d802d61f9c721399721ba273137e721905d620b2a5731400ededed929a997206721472079c7e9995907219721a72199a721a7315731605721c937213f0721d93721ff0721eedededed93cbc272207317938602720e7213b2db630872207318009386027211721fb2db63087220731900e6c67220040893e4c67220050e8c720401958f7213731aededec929a997206721472079c7e9995907219721a72199a721a731b731c05721c92a39a9a72159c721a7217b27205731d0093721df0721392721e95917219721a731e9c721d99721ba2731f7e721905d801d61fe4c672010704edededed90721f9972197320909972149c7e99721a721f05721c9a721c7207907ef0998c7208027214069d9c7e721c067e721e067e997212732106937213732293721d7323",
      |            "address": "59vY1ythmk1fP9eARqmN5oC6FfVtHJT39QEDaWQer7kfcLj36rqREDEumtssJkgDshwzfZ5DZ2AuJWBf9TjEHq7jhrV8hqM6nG9xrDME7MKSDN5AHPdFwnqEYJpQKN746bo8wL9VJmZxCDvx6q3xu2uoRT3MY5mJfwWfUbJfxy6AaFiFDbNdQK4MGUXJ5zkNsiNYYwoaieChFvsCAgeqTUJ1xr2MjR39ZzgxSiGD5oHMG2WWWaanfBh6SQZWTNzdru3btGbWYUb2Sn7QaCLrb74wzzAx77yFnFmoM8EvUFs3mMrB7Pi4KbRJgc4N16u8qetACg9DZfSsZLpcbxRLhCFD2y8dzfHvcfXv5dXrVGdpTnRw9vgrTC9uiim42fAudJrCZ6H8u6kTFQhXE4db16pgS7LWGCo2Zafn5uFhQWHRn4wYsvz9w6CkT4TXRgW88dqYG11Lu1h4WRdLVYkeJWsZ22Jt9HTyBpB1HGG4Spi6W2WDmJU6SnzPMZ1QgdFnc2aAtBB8etMCnmTWELj3gbBEfMCDb2V6UUa4BXQFvxQ5i1W8JrmByozg4AJojJoHVhd3MMUqopNsdM4p9wfWrbhHnZQjsCxKeDzjZdxj3g6sjj26RdNDQUNvdnpJtxP3m6F9U9pMJ3j75c87Jz7E3vogLE9oz9iuSDLvEXAG5xD9yz8SbSYf49gvHGApdUS8Ze8s5q3Wt9B9LUqpk5X1x2fUsmqyvqy53W3VyXh28KcUQWm2Liohj8WgcJ7Ge2CDcBp74r9KBjLjuwQLbvFqNWtLGTZDuRA3dx8153keEfANNiaeVG7zPG3vjg9adhXzkVpoP5A8WCdJ1wSuUR2LMa9iGaoFMKVncEQLmv8fzcjMTVn2bs9xzRKDvpxFH7KPS2ZUxThGbzw9Ud2Gc4ws8HhskKr7DrMC9L6WBCpkM1inPtZ9hRq9TYGLfShqvAVtBo1kyWJJH2PjUU5Asooy1Hzhg72CFJQLT9fuihmXpPd5cpXNapsiTLjRmrSGPKj3QhmfXW4Z",
      |            "assets": [
      |                {
      |                    "tokenId": "7956620de75192984d639cab2c989269d9a5310ad870ad547426952a9e660699",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "0779ec04f2fae64e87418a1ad917639d4668f78484f45df962b0dec14a2591d2",
      |                    "index": 1,
      |                    "amount": 299993,
      |                    "name": "Mi Goreng ",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "98da76cecb772029cfec3d53727d5ff37d5875691825fbba743464af0c89ce45",
      |                    "index": 2,
      |                    "amount": 283146,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "3fdce3da8d364f13bca60998c20660c79c19923f44e141df01349d2e63651e86",
      |                    "index": 3,
      |                    "amount": 99716855,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "c256908dd9fd477bde350be6a41c0884713a1b1d589357ae731763455ef28c10",
      |                    "index": 4,
      |                    "amount": 1496035970,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "100490031eeeca70c801",
      |                    "sigmaType": "Coll[SInt]",
      |                    "renderedValue": "[200,15,922295,100]"
      |                },
      |                "R5": {
      |                    "serializedValue": "05becf24",
      |                    "sigmaType": "SLong",
      |                    "renderedValue": "299999"
      |                },
      |                "R6": {
      |                    "serializedValue": "05d00f",
      |                    "sigmaType": "SLong",
      |                    "renderedValue": "1000"
      |                },
      |                "R7": {
      |                    "serializedValue": "0402",
      |                    "sigmaType": "SInt",
      |                    "renderedValue": "1"
      |                }
      |            },
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "1ce90811c331306c3ef345bfa6be07f6ceb4fc9a02b2bf029a47a30f9bc5fa86",
      |            "transactionId": "b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e",
      |            "blockId": "39a3222f60392d7fd0fc0069f919930e81a5bc722024dfefd490446220d0fd9e",
      |            "value": 9987500000,
      |            "index": 1,
      |            "globalIndex": 25847030,
      |            "creationHeight": 923467,
      |            "settlementHeight": 923469,
      |            "ergoTree": "0008cd03b0b2ce2839747f5e4d3c304a0dd67b47f29649a48e19c5cbb7b93aad106911df",
      |            "address": "9hodDWDNFhzmpY6qQm8spYNpbbus2jYoGEe6yFvM5uC8CfZfnMi",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "5d3e84116dbfa2e0139e728359a6ab6e46c68c5c19509e9d522b74b541cfe188",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "d43f9cd556853127181b2aa5f2bedc1c1e4719b5237caa53d1d6f851e988116b",
      |            "transactionId": "b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e",
      |            "blockId": "39a3222f60392d7fd0fc0069f919930e81a5bc722024dfefd490446220d0fd9e",
      |            "value": 1250000,
      |            "index": 2,
      |            "globalIndex": 25847031,
      |            "creationHeight": 923467,
      |            "settlementHeight": 923469,
      |            "ergoTree": "19a3041f040004000404040404000402060101040005000402040404020400040004020502040405020402040005feffffffffffffffff010408050205000404040004060404040205fcffffffffffffffff010100d80cd601b2a5730000d602db63087201d603e4c6a7050ed604b2a4730100d605db63087204d6068cb2720573020002d607998cb27202730300027206d608e4c6a70408d609db6308a7d60ab27209730400d60bb27205730500d60c7306d1ed938cb27202730700017203959372077308d80cd60db2a5e4e3000400d60eb2a5e4e3010400d60fb2e4c672040410730900d610c672010804d61199720f95e67210e47210e4c672010704d6128cb27209730a0001d613db6308720ed614b27209730b00d6158c721402d6167e721105d6179972159c72168c720a02d618b2db6308720d730c00eded93c2720dd07208edededed93e4c6720e0408720893e4c6720e050e720393c2720ec2a795917211730dd801d619b27213730e00eded9386027212730fb27213731000938c7219018c721401939972158c721902721793860272127311b2721373120093b27213731300720aed938c7218018c720b01927e8c7218020699999d9c99997e8c720b02069d9c7ee4c672040505067e7211067e720f06720c7e7217067e999973148cb27205731500029c9972067316721606720c720c958f7207731793b2db6308b2a473180073190086029593b17209731a8cb27209731b00018cb27209731c0001731d731e",
      |            "address": "LDQVxoiCdZqP7uW7FP1DMaqiZQ9dfMob93zMYKX9X7sXupnJZ6Lmn8u5C3T2Qgiw9gQoiw9xQfLCn8zWagMECeQDUEsKUnbUk4fAYLeySrg2DRuC7BsUYM2Cwux55bz5QuvpEsQeE8iM9xxr4t4YNrhKrY9T8ytB4y8bWVjuSPHzTJSr4gfqieD3agt4Avf5Ctf461aBqS4v4H2XvHsfrbokmqEUE7G94PPLP6p4EsdNXqzX9jqQyGZSiXz2vuxx3H3WZM4ZcKFfva1T3NKyKxRQ9FfZM5wsEdL2WcrntSnRcNdjn541ewYoeYEHMV5Jbvu9sgb75Cm8EBpcj31Jox4TVdfQ87w94LCnvU2RvNdc5JnBq149KDnugY3SjNJ81ZZF57LuMWqm6JQfEUANjw3CiZQpdm6LFKmGy3jdk4tr2KiToZj71JNjRCnqH5VCSJJLERXHj6BKDM9V4rjycRxhCN79auYSWvGxgbSSeZRJD7xkeGSxbAG56hN9is3BGSaBBVa7TNDwYMDG7juTVVCCP3K1WNyVJpfZJK6EDANECWgeqrFLSVZ42JafrhdWjE1KwEttz39MF67nELBuQsXpWYQV2VmEhcv",
      |            "assets": [
      |                {
      |                    "tokenId": "3fdce3da8d364f13bca60998c20660c79c19923f44e141df01349d2e63651e86",
      |                    "index": 0,
      |                    "amount": 100,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "c256908dd9fd477bde350be6a41c0884713a1b1d589357ae731763455ef28c10",
      |                    "index": 1,
      |                    "amount": 1400,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "251177a50ed3d4df8fc8575b3d9e03a0ba81f506a329a3ba7d8bb20994303794",
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
      |                    "serializedValue": "0e207956620de75192984d639cab2c989269d9a5310ad870ad547426952a9e660699",
      |                    "sigmaType": "Coll[SByte]",
      |                    "renderedValue": "7956620de75192984d639cab2c989269d9a5310ad870ad547426952a9e660699"
      |                }
      |            },
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "7e4b2a2ef6ed70e780e703a82dd9d56461be0a179def851e6e8c3ac8ff65c762",
      |            "transactionId": "b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e",
      |            "blockId": "39a3222f60392d7fd0fc0069f919930e81a5bc722024dfefd490446220d0fd9e",
      |            "value": 250000,
      |            "index": 3,
      |            "globalIndex": 25847032,
      |            "creationHeight": 923467,
      |            "settlementHeight": 923469,
      |            "ergoTree": "0008cd03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491",
      |            "address": "9iAYDwPWP7c9cKXB7P2bLivt75CBaXrbYoZrxpna1peB8cYFoHw",
      |            "assets": [
      |                {
      |                    "tokenId": "0779ec04f2fae64e87418a1ad917639d4668f78484f45df962b0dec14a2591d2",
      |                    "index": 0,
      |                    "amount": 7,
      |                    "name": "Mi Goreng ",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "521530fc949109e6ce219903edd86740aa3c97eb335182c9ee43cb88e9b814d3",
      |            "transactionId": "b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e",
      |            "blockId": "39a3222f60392d7fd0fc0069f919930e81a5bc722024dfefd490446220d0fd9e",
      |            "value": 1000000,
      |            "index": 4,
      |            "globalIndex": 25847033,
      |            "creationHeight": 923467,
      |            "settlementHeight": 923469,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "7ee4d7faf69a5fafe57af81fa5c68b3ecb09573c1c4d3df5e42ee77ad3c88bed",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 1891
      |}""".stripMargin
  ).toOption.get.toTransaction

  val expectedOutput = decode[Output](
    s"""
       |{
       |    "boxId": "d43f9cd556853127181b2aa5f2bedc1c1e4719b5237caa53d1d6f851e988116b",
       |    "transactionId": "b5038999043e6ecd617a0a292976fe339d0e4d9ec85296f13610be0c7b16752e",
       |    "blockId": "39a3222f60392d7fd0fc0069f919930e81a5bc722024dfefd490446220d0fd9e",
       |    "value": 1250000,
       |    "index": 2,
       |    "globalIndex": 25847031,
       |    "creationHeight": 923467,
       |    "settlementHeight": 923469,
       |    "ergoTree": "19a3041f040004000404040404000402060101040005000402040404020400040004020502040405020402040005feffffffffffffffff010408050205000404040004060404040205fcffffffffffffffff010100d80cd601b2a5730000d602db63087201d603e4c6a7050ed604b2a4730100d605db63087204d6068cb2720573020002d607998cb27202730300027206d608e4c6a70408d609db6308a7d60ab27209730400d60bb27205730500d60c7306d1ed938cb27202730700017203959372077308d80cd60db2a5e4e3000400d60eb2a5e4e3010400d60fb2e4c672040410730900d610c672010804d61199720f95e67210e47210e4c672010704d6128cb27209730a0001d613db6308720ed614b27209730b00d6158c721402d6167e721105d6179972159c72168c720a02d618b2db6308720d730c00eded93c2720dd07208edededed93e4c6720e0408720893e4c6720e050e720393c2720ec2a795917211730dd801d619b27213730e00eded9386027212730fb27213731000938c7219018c721401939972158c721902721793860272127311b2721373120093b27213731300720aed938c7218018c720b01927e8c7218020699999d9c99997e8c720b02069d9c7ee4c672040505067e7211067e720f06720c7e7217067e999973148cb27205731500029c9972067316721606720c720c958f7207731793b2db6308b2a473180073190086029593b17209731a8cb27209731b00018cb27209731c0001731d731e",
       |    "address": "LDQVxoiCdZqP7uW7FP1DMaqiZQ9dfMob93zMYKX9X7sXupnJZ6Lmn8u5C3T2Qgiw9gQoiw9xQfLCn8zWagMECeQDUEsKUnbUk4fAYLeySrg2DRuC7BsUYM2Cwux55bz5QuvpEsQeE8iM9xxr4t4YNrhKrY9T8ytB4y8bWVjuSPHzTJSr4gfqieD3agt4Avf5Ctf461aBqS4v4H2XvHsfrbokmqEUE7G94PPLP6p4EsdNXqzX9jqQyGZSiXz2vuxx3H3WZM4ZcKFfva1T3NKyKxRQ9FfZM5wsEdL2WcrntSnRcNdjn541ewYoeYEHMV5Jbvu9sgb75Cm8EBpcj31Jox4TVdfQ87w94LCnvU2RvNdc5JnBq149KDnugY3SjNJ81ZZF57LuMWqm6JQfEUANjw3CiZQpdm6LFKmGy3jdk4tr2KiToZj71JNjRCnqH5VCSJJLERXHj6BKDM9V4rjycRxhCN79auYSWvGxgbSSeZRJD7xkeGSxbAG56hN9is3BGSaBBVa7TNDwYMDG7juTVVCCP3K1WNyVJpfZJK6EDANECWgeqrFLSVZ42JafrhdWjE1KwEttz39MF67nELBuQsXpWYQV2VmEhcv",
       |    "assets": [
       |        {
       |            "tokenId": "3fdce3da8d364f13bca60998c20660c79c19923f44e141df01349d2e63651e86",
       |            "index": 0,
       |            "amount": 100,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        },
       |        {
       |            "tokenId": "c256908dd9fd477bde350be6a41c0884713a1b1d589357ae731763455ef28c10",
       |            "index": 1,
       |            "amount": 1400,
       |            "name": null,
       |            "decimals": null,
       |            "type": null
       |        },
       |        {
       |            "tokenId": "251177a50ed3d4df8fc8575b3d9e03a0ba81f506a329a3ba7d8bb20994303794",
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
       |            "serializedValue": "0e207956620de75192984d639cab2c989269d9a5310ad870ad547426952a9e660699",
       |            "sigmaType": "Coll[SByte]",
       |            "renderedValue": "7956620de75192984d639cab2c989269d9a5310ad870ad547426952a9e660699"
       |        }
       |    },
       |    "spentTransactionId": null,
       |    "mainChain": true
       |}
       |""".stripMargin
  ).toOption.get

  val expectedCompound = CompoundV1(
    expectedOutput,
    LmCompoundParams(
      AssetAmount(TokenId.unsafeFromString("3fdce3da8d364f13bca60998c20660c79c19923f44e141df01349d2e63651e86"), 100),
      Some(
        AssetAmount(TokenId.unsafeFromString("c256908dd9fd477bde350be6a41c0884713a1b1d589357ae731763455ef28c10"), 1400)
      ),
      TokenId.unsafeFromString("251177a50ed3d4df8fc8575b3d9e03a0ba81f506a329a3ba7d8bb20994303794")
    ),
    PoolId.unsafeFromString("7956620de75192984d639cab2c989269d9a5310ad870ad547426952a9e660699"),
    PublicKeyRedeemer(PubKey.unsafeFromString("03e02fa2bbd85e9298aa37fe2634602a0fba746234fe2a67f04d14deda55fac491")),
    V1
  )

  val compoundBatchTxTest = decode[TransactionTest] {
    val file   = new File("/Users/gusev.t/IdeaProjects/ergo-analytics/modules/parsers/src/test/scala/fi/spectrum/parser/evaluation/EvalTxCompound.json")
    val source = Source.fromFile(file)
    val res    = source.getLines().mkString("")
    source.close()
    res
  }.toOption.get

  val compoundBatchTx = compoundBatchTxTest.toTransaction

  val expectedBatchCompoundsEval = compoundBatchTxTest.inputs
    .flatMap { in =>
      CompoundParserV1.v1Compound.compound(InputTest.toOutput(in), ErgoTreeSerializer.default.deserialize(in.ergoTree))
    }
    .map { c =>
      Processed.make(
        OrderState(
          compoundBatchTx.id,
          0,
          OrderStatus.Evaluated
        ),
        c
      )
    }

  val expectedBatchCompoundsRegister = compoundBatchTxTest.outputs
    .flatMap { out =>
      CompoundParserV1.v1Compound.compound(OutputTest.fromExplorerOut(out), ErgoTreeSerializer.default.deserialize(out.ergoTree))
    }
    .map { c =>
      Processed.make(
        OrderState(
          compoundBatchTx.id,
          0,
          OrderStatus.Registered
        ),
        c
      )
    }

  val compoundTxOneOrder =
    decode[TransactionTest](
      """
        |{
        |  "id": "38503bcdbca4925dbc99b2d94e17f8e4ab998fec6e56a418f846c54109025a35",
        |  "blockId": "7ccd3d91784b6873025b57b5260fa51f7635c157c2669e0616cc0518c194d1c2",
        |  "inclusionHeight": 938657,
        |  "timestamp": 1676194166293,
        |  "index": 7,
        |  "globalIndex": 4748145,
        |  "numConfirmations": 672,
        |  "inputs": [
        |    {
        |      "boxId": "9d3a892256c84ca282fb37f3a8b68d5678fcd447da5297c85c41c06d9e49602f",
        |      "value": 1250000,
        |      "index": 0,
        |      "spendingProof": null,
        |      "outputBlockId": "a0b1a1ca7a2411230e91abf3cc8d4de895d287073b788c5744b47205c4c41254",
        |      "outputTransactionId": "4fffc1562aa9f2af3322fba8284489dab58d3e488d1b381f475e586fd0001a24",
        |      "outputIndex": 0,
        |      "outputGlobalIndex": 26451725,
        |      "outputCreatedAt": 937901,
        |      "outputSettledAt": 937903,
        |      "ergoTree": "19c0062904000400040204020404040404060406040804080404040204000400040204020601010400040a050005000404040204020e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c0400040205000402040204060500050005feffffffffffffffff010502050005000402050005000100d820d601b2a5730000d602db63087201d603db6308a7d604b27203730100d605e4c6a70410d606e4c6a70505d607e4c6a70605d608b27202730200d609b27203730300d60ab27202730400d60bb27203730500d60cb27202730600d60db27203730700d60e8c720d01d60fb27202730800d610b27203730900d6118c721001d6128c720b02d613998c720a027212d6148c720902d615b27205730a00d6169a99a37215730bd617b27205730c00d6189d72167217d61995919e72167217730d9a7218730e7218d61ab27205730f00d61b7e721a05d61c9d7206721bd61d998c720c028c720d02d61e8c721002d61f998c720f02721ed6207310d1ededededed93b272027311007204ededed93e4c672010410720593e4c672010505720693e4c6720106057207928cc77201018cc7a70193c27201c2a7ededed938c7208018c720901938c720a018c720b01938c720c01720e938c720f01721193b172027312959172137313d802d6219c721399721ba273147e721905d622b2a5731500ededed929a997206721472079c7e9995907219721a72199a721a7316731705721c937213f0721d937221f0721fedededed93cbc272227318938602720e7213b2db6308722273190093860272117221b2db63087222731a00e6c67222040893e4c67222050e8c720401958f7213731bededec929a997206721472079c7e9995907219721a72199a721a731c731d05721c92a39a9a72159c721a7217b27205731e0093721df0721392721f95917219721a731f9c721d99721ba273207e721905d804d621e4c672010704d62299721a7221d6237e722205d62499997321721e9c9972127322722395ed917224732391721f7324edededed9072219972197325909972149c7223721c9a721c7207907ef0998c7208027214069a9d9c99997e7214069d9c7e7206067e7222067e721a0672207e721f067e7224067220937213732693721d73277328",
        |      "address": "2JowFguNN2xRcYi3GANwoCnk46sVvc2D6oT27A2xbj5DsDEj2TMJbZmR8iqtWCbobi7hEvhkXQeMo9sjGh5jdBo133aJxr7ZTcfeVk8zN1MMDRqP64ehC7BNYGarJ5eaHpEq6DahnLC2khXpqU7dvfCczuPXd9qioB32tNqdCk3b9NBGUuQxNqK6FrHbLVi99KvuN2Nz9bh8HEZFukDKymuDpHwXF5JjMUW2YGDQCBFmLgKGFrLrvC2iQH18ftfmQoqUS4GCXpdJ5YtPMg8bLZ4GNP5QaXZXEQpRTz5fgRu9FR2ZgmZ5r1tSQV789H3Lf6RcA3RmkWoTw8RMEMdwE45e7yoiWVTHqkFMLz2qEnYgmzSyxv7Uxk1f9KXfvWPf4CbMfi9y6txGXh5MNhzPKzRUuNS8VxScAt7JYfDCvp1KhyyFLWy9ijtDbmUw2MshWAubzt2dAJaH2hAMiv1rGi6QWG9ru6LvPTH7Tr1aUhLac6DK2p2ycBwk8hp417K7sU2aVzRfWhreEZxnCH2aGQvp8iq4PDkRsEjgUpMzCu4wJDXRa9v3gKoBP7N3xWV2xUg1xmMTPC1LNomT86k5vQvBZpM74gZ3RWmJTFoq6dwPtHNHC2hdyfuqZDpHz2ATcbem4wmG1QJPig92R36WKmydN53RujrA3pXccxGNHqMqPsu2YexSURoSihtJYsrdGG4332DdGappSnAgDeETYJ69GJ6giTiddVQv3oKzuF7tZwdVrFzFoUkSWEGqd4jnWRg46cqTqJqT5RH4MJw7FsCi6gZD1X39X1ANgv9vC6pNV6QqdJktpN9CoM2DbEyVMyYdhpgotexAR9u6KEBJmEbfps7jus9X97arMmzHRwJR99X37uj8zT5g36SAPPwDyueEAsW7r1RNuwm4DGPAmdxYBRgi5iri9TCHpAeDBLCPU9tycSVPviDrNxKWVYEus6jqZdNENPrTeA83T4SGdFKmA8aVyNKPtzvejRLEwYCjdLAAgoF7oGY5R6gq9caatH23rrEJutNCS4csAgm8QyrETfF1nv9KMrGvA9E577dBcNYKHdLjfVKHi7op3LyNTgYTc2DkYakdG94tJzPbPsgAetpFKs3SjWcUkhhgCm8JoiQ8iFDXG4kgADJ",
        |      "assets": [
        |        {
        |          "tokenId": "0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f",
        |          "index": 0,
        |          "amount": 1,
        |          "name": null,
        |          "decimals": null,
        |          "type": null
        |        },
        |        {
        |          "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
        |          "index": 1,
        |          "amount": 6600011,
        |          "name": "EPOS",
        |          "decimals": 4,
        |          "type": "EIP-004"
        |        },
        |        {
        |          "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |          "index": 2,
        |          "amount": 4725332,
        |          "name": "Ergo_ErgoPOS_LP",
        |          "decimals": 0,
        |          "type": "EIP-004"
        |        },
        |        {
        |          "tokenId": "ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0",
        |          "index": 3,
        |          "amount": 9223372036850050476,
        |          "name": null,
        |          "decimals": null,
        |          "type": null
        |        },
        |        {
        |          "tokenId": "bc58f841398f84fd0a72e2de50f8f3aed14e6234f7502bb7fe90729fba9c74f2",
        |          "index": 4,
        |          "amount": 9223372036840599814,
        |          "name": null,
        |          "decimals": null,
        |          "type": null
        |        }
        |      ],
        |      "additionalRegisters": {
        |        "R4": {
        |          "serializedValue": "1004f4030a9cb672c801",
        |          "sigmaType": "Coll[SInt]",
        |          "renderedValue": "[250,5,937358,100]"
        |        },
        |        "R5": {
        |          "serializedValue": "05fee2be0a",
        |          "sigmaType": "SLong",
        |          "renderedValue": "10999999"
        |        },
        |        "R6": {
        |          "serializedValue": "05d00f",
        |          "sigmaType": "SLong",
        |          "renderedValue": "1000"
        |        },
        |        "R7": {
        |          "serializedValue": "0404",
        |          "sigmaType": "SInt",
        |          "renderedValue": "2"
        |        }
        |      }
        |    },
        |    {
        |      "boxId": "797bd0a5cb50d1ed6050db9a00475c876a25ab483fc3ee181a58e58e87895713",
        |      "value": 9963750000,
        |      "index": 1,
        |      "spendingProof": "1145077bbf04122d53ccd45ed5a3714ed7046fea15937442380145b0cc20c1072fc8b8c2e855a0242dbe04dd682d298001beec3dae03caaf",
        |      "outputBlockId": "a0b1a1ca7a2411230e91abf3cc8d4de895d287073b788c5744b47205c4c41254",
        |      "outputTransactionId": "4fffc1562aa9f2af3322fba8284489dab58d3e488d1b381f475e586fd0001a24",
        |      "outputIndex": 1,
        |      "outputGlobalIndex": 26451726,
        |      "outputCreatedAt": 937901,
        |      "outputSettledAt": 937903,
        |      "ergoTree": "0008cd03b0b2ce2839747f5e4d3c304a0dd67b47f29649a48e19c5cbb7b93aad106911df",
        |      "address": "9hodDWDNFhzmpY6qQm8spYNpbbus2jYoGEe6yFvM5uC8CfZfnMi",
        |      "assets": [],
        |      "additionalRegisters": {}
        |    },
        |    {
        |      "boxId": "a714f62b238096c289a9d60ec0228981ddc82a045f967757f377f63edacb6a86",
        |      "value": 300000,
        |      "index": 2,
        |      "spendingProof": null,
        |      "outputBlockId": "a0b1a1ca7a2411230e91abf3cc8d4de895d287073b788c5744b47205c4c41254",
        |      "outputTransactionId": "4fffc1562aa9f2af3322fba8284489dab58d3e488d1b381f475e586fd0001a24",
        |      "outputIndex": 2,
        |      "outputGlobalIndex": 26451727,
        |      "outputCreatedAt": 937901,
        |      "outputSettledAt": 937903,
        |      "ergoTree": "19a3041f040004000404040404000402060101040005000402040404020400040004020502040405020402040005feffffffffffffffff010408050205000404040004060404040205fcffffffffffffffff010100d80cd601b2a5730000d602db63087201d603e4c6a7050ed604b2a4730100d605db63087204d6068cb2720573020002d607998cb27202730300027206d608e4c6a70408d609db6308a7d60ab27209730400d60bb27205730500d60c7306d1ed938cb27202730700017203959372077308d80cd60db2a5e4e3000400d60eb2a5e4e3010400d60fb2e4c672040410730900d610c672010804d61199720f95e67210e47210e4c672010704d6128cb27209730a0001d613db6308720ed614b27209730b00d6158c721402d6167e721105d6179972159c72168c720a02d618b2db6308720d730c00eded93c2720dd07208edededed93e4c6720e0408720893e4c6720e050e720393c2720ec2a795917211730dd801d619b27213730e00eded9386027212730fb27213731000938c7219018c721401939972158c721902721793860272127311b2721373120093b27213731300720aed938c7218018c720b01927e8c7218020699999d9c99997e8c720b02069d9c7ee4c672040505067e7211067e720f06720c7e7217067e999973148cb27205731500029c9972067316721606720c720c958f7207731793b2db6308b2a473180073190086029593b17209731a8cb27209731b00018cb27209731c0001731d731e",
        |      "address": "YyLFSfjMQw5AJEuchDNPzziDH7CpLX8EbtGbddMpTpDoYYCc5xKJUbDuFnwsJ4qcpARjLmTQdRUTDiftnkb4kuNAaM7enwfxbWvh9SAbpvxAtJzhQApmedrEH9BBrPYzHdWbR354ZXZJkazhvfWMLyijyFeJ7aTDYmhDn8YvF3HeBMJiLByT32wTfNVEWQf4SEp3eN66ppqZs7bJAxrsZo3PDGJgTtZ2qPypMv3x9fcE6zYoLiCzHbAWC8WCfn5wNno9NAwSgWgHKcz3Gx4kXAe5DJnMDZJeiAuckS4e8J5sUfgFSPHTysvkfPw5pxBBNoxrsQ8y2jHSjB3KhSKKsNoZLnWZpbLQ7TurFc34TZJn9FNsRzpmQc9ftcZNa1QxKNoBtFzCpRXwvKd1RaSEZc5er7TwiyL9ebcfh1x3b3k2qXZhi6eXnktGBhEcNiJoJe7FdRA7gTXs9MCQewX8r7u3ay6i94bT9BrdXZy7xnmFU1DvyCcNP3sHHC1gDbNxdzVYsyF9tuxuXhmmD3rTc7hCPX4uo1dahq5HFaaQifp8kwe6iVuzp3Qo5vNPgV8tJBzXhGx7wgYoozGznh1ecYdBtCpHdEwzbME4bQy8qLXsBRB7TVnhQ2GYUL8XyCpzaziWEe1tdKrgbFFE1CNnLuGR7FEFikx5PfJVFTmFvpEvvtM7Qq9TMFjgsaoP58M7BajhU74EgqVRX3hi8pmmzxnjAzVKK5wZR62RcpowjrYsNLGjcWL8B",
        |      "assets": [
        |        {
        |          "tokenId": "ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0",
        |          "index": 0,
        |          "amount": 444898,
        |          "name": null,
        |          "decimals": null,
        |          "type": null
        |        },
        |        {
        |          "tokenId": "bc58f841398f84fd0a72e2de50f8f3aed14e6234f7502bb7fe90729fba9c74f2",
        |          "index": 1,
        |          "amount": 1334694,
        |          "name": null,
        |          "decimals": null,
        |          "type": null
        |        },
        |        {
        |          "tokenId": "00738c0e3c4d89a1a2d5e71a20e4a00262340f289e58d9d866719564a933bad5",
        |          "index": 2,
        |          "amount": 1,
        |          "name": null,
        |          "decimals": null,
        |          "type": null
        |        }
        |      ],
        |      "additionalRegisters": {
        |        "R4": {
        |          "serializedValue": "08cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec",
        |          "sigmaType": "SSigmaProp",
        |          "renderedValue": "03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec"
        |        },
        |        "R5": {
        |          "serializedValue": "0e200f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f",
        |          "sigmaType": "Coll[SByte]",
        |          "renderedValue": "0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f"
        |        }
        |      }
        |    }
        |  ],
        |  "dataInputs": [],
        |  "outputs": [
        |    {
        |      "boxId": "abd4a24a6547feb115e790c9e13c0ee621a60a228d9c738e534be48e6b78d721",
        |      "transactionId": "38503bcdbca4925dbc99b2d94e17f8e4ab998fec6e56a418f846c54109025a35",
        |      "blockId": "7ccd3d91784b6873025b57b5260fa51f7635c157c2669e0616cc0518c194d1c2",
        |      "value": 1250000,
        |      "index": 0,
        |      "globalIndex": 26483090,
        |      "creationHeight": 938652,
        |      "settlementHeight": 938657,
        |      "ergoTree": "19c0062904000400040204020404040404060406040804080404040204000400040204020601010400040a050005000404040204020e20fc3cdbfd1abc83f4a38ca3fb3dfe417a158b67d63e3c52137fdda4e66ad3956c0400040205000402040204060500050005feffffffffffffffff010502050005000402050005000100d820d601b2a5730000d602db63087201d603db6308a7d604b27203730100d605e4c6a70410d606e4c6a70505d607e4c6a70605d608b27202730200d609b27203730300d60ab27202730400d60bb27203730500d60cb27202730600d60db27203730700d60e8c720d01d60fb27202730800d610b27203730900d6118c721001d6128c720b02d613998c720a027212d6148c720902d615b27205730a00d6169a99a37215730bd617b27205730c00d6189d72167217d61995919e72167217730d9a7218730e7218d61ab27205730f00d61b7e721a05d61c9d7206721bd61d998c720c028c720d02d61e8c721002d61f998c720f02721ed6207310d1ededededed93b272027311007204ededed93e4c672010410720593e4c672010505720693e4c6720106057207928cc77201018cc7a70193c27201c2a7ededed938c7208018c720901938c720a018c720b01938c720c01720e938c720f01721193b172027312959172137313d802d6219c721399721ba273147e721905d622b2a5731500ededed929a997206721472079c7e9995907219721a72199a721a7316731705721c937213f0721d937221f0721fedededed93cbc272227318938602720e7213b2db6308722273190093860272117221b2db63087222731a00e6c67222040893e4c67222050e8c720401958f7213731bededec929a997206721472079c7e9995907219721a72199a721a731c731d05721c92a39a9a72159c721a7217b27205731e0093721df0721392721f95917219721a731f9c721d99721ba273207e721905d804d621e4c672010704d62299721a7221d6237e722205d62499997321721e9c9972127322722395ed917224732391721f7324edededed9072219972197325909972149c7223721c9a721c7207907ef0998c7208027214069a9d9c99997e7214069d9c7e7206067e7222067e721a0672207e721f067e7224067220937213732693721d73277328",
        |      "address": "2JowFguNN2xRcYi3GANwoCnk46sVvc2D6oT27A2xbj5DsDEj2TMJbZmR8iqtWCbobi7hEvhkXQeMo9sjGh5jdBo133aJxr7ZTcfeVk8zN1MMDRqP64ehC7BNYGarJ5eaHpEq6DahnLC2khXpqU7dvfCczuPXd9qioB32tNqdCk3b9NBGUuQxNqK6FrHbLVi99KvuN2Nz9bh8HEZFukDKymuDpHwXF5JjMUW2YGDQCBFmLgKGFrLrvC2iQH18ftfmQoqUS4GCXpdJ5YtPMg8bLZ4GNP5QaXZXEQpRTz5fgRu9FR2ZgmZ5r1tSQV789H3Lf6RcA3RmkWoTw8RMEMdwE45e7yoiWVTHqkFMLz2qEnYgmzSyxv7Uxk1f9KXfvWPf4CbMfi9y6txGXh5MNhzPKzRUuNS8VxScAt7JYfDCvp1KhyyFLWy9ijtDbmUw2MshWAubzt2dAJaH2hAMiv1rGi6QWG9ru6LvPTH7Tr1aUhLac6DK2p2ycBwk8hp417K7sU2aVzRfWhreEZxnCH2aGQvp8iq4PDkRsEjgUpMzCu4wJDXRa9v3gKoBP7N3xWV2xUg1xmMTPC1LNomT86k5vQvBZpM74gZ3RWmJTFoq6dwPtHNHC2hdyfuqZDpHz2ATcbem4wmG1QJPig92R36WKmydN53RujrA3pXccxGNHqMqPsu2YexSURoSihtJYsrdGG4332DdGappSnAgDeETYJ69GJ6giTiddVQv3oKzuF7tZwdVrFzFoUkSWEGqd4jnWRg46cqTqJqT5RH4MJw7FsCi6gZD1X39X1ANgv9vC6pNV6QqdJktpN9CoM2DbEyVMyYdhpgotexAR9u6KEBJmEbfps7jus9X97arMmzHRwJR99X37uj8zT5g36SAPPwDyueEAsW7r1RNuwm4DGPAmdxYBRgi5iri9TCHpAeDBLCPU9tycSVPviDrNxKWVYEus6jqZdNENPrTeA83T4SGdFKmA8aVyNKPtzvejRLEwYCjdLAAgoF7oGY5R6gq9caatH23rrEJutNCS4csAgm8QyrETfF1nv9KMrGvA9E577dBcNYKHdLjfVKHi7op3LyNTgYTc2DkYakdG94tJzPbPsgAetpFKs3SjWcUkhhgCm8JoiQ8iFDXG4kgADJ",
        |      "assets": [
        |        {
        |          "tokenId": "0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f",
        |          "index": 0,
        |          "amount": 1,
        |          "name": null,
        |          "decimals": null,
        |          "type": null
        |        },
        |        {
        |          "tokenId": "00bd762484086cf560d3127eb53f0769d76244d9737636b2699d55c56cd470bf",
        |          "index": 1,
        |          "amount": 4400011,
        |          "name": "EPOS",
        |          "decimals": 4,
        |          "type": "EIP-004"
        |        },
        |        {
        |          "tokenId": "e7021bda9872a7eb2aa69dd704e6a997dae9d1b40d1ff7a50e426ef78c6f6f87",
        |          "index": 2,
        |          "amount": 4725332,
        |          "name": "Ergo_ErgoPOS_LP",
        |          "decimals": 0,
        |          "type": "EIP-004"
        |        },
        |        {
        |          "tokenId": "ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0",
        |          "index": 3,
        |          "amount": 9223372036850050476,
        |          "name": null,
        |          "decimals": null,
        |          "type": null
        |        },
        |        {
        |          "tokenId": "bc58f841398f84fd0a72e2de50f8f3aed14e6234f7502bb7fe90729fba9c74f2",
        |          "index": 4,
        |          "amount": 9223372036845325145,
        |          "name": null,
        |          "decimals": null,
        |          "type": null
        |        }
        |      ],
        |      "additionalRegisters": {
        |        "R4": {
        |          "serializedValue": "1004f4030a9cb672c801",
        |          "sigmaType": "Coll[SInt]",
        |          "renderedValue": "[250,5,937358,100]"
        |        },
        |        "R5": {
        |          "serializedValue": "05fee2be0a",
        |          "sigmaType": "SLong",
        |          "renderedValue": "10999999"
        |        },
        |        "R6": {
        |          "serializedValue": "05d00f",
        |          "sigmaType": "SLong",
        |          "renderedValue": "1000"
        |        },
        |        "R7": {
        |          "serializedValue": "0406",
        |          "sigmaType": "SInt",
        |          "renderedValue": "3"
        |        }
        |      },
        |      "spentTransactionId": "3156e7598a5944accd90232d8eb18da3ab1b72e7340445d75ad23ce4c0951f7b",
        |      "mainChain": true
        |    },
        |    {
        |      "boxId": "0f961f51e1d36f82f90220f865e5fb7bc5cfeb5768ee775e993375e75661bd23",
        |      "transactionId": "38503bcdbca4925dbc99b2d94e17f8e4ab998fec6e56a418f846c54109025a35",
        |      "blockId": "7ccd3d91784b6873025b57b5260fa51f7635c157c2669e0616cc0518c194d1c2",
        |      "value": 9961000000,
        |      "index": 1,
        |      "globalIndex": 26483091,
        |      "creationHeight": 938652,
        |      "settlementHeight": 938657,
        |      "ergoTree": "0008cd03b0b2ce2839747f5e4d3c304a0dd67b47f29649a48e19c5cbb7b93aad106911df",
        |      "address": "9hodDWDNFhzmpY6qQm8spYNpbbus2jYoGEe6yFvM5uC8CfZfnMi",
        |      "assets": [],
        |      "additionalRegisters": {},
        |      "spentTransactionId": "3156e7598a5944accd90232d8eb18da3ab1b72e7340445d75ad23ce4c0951f7b",
        |      "mainChain": true
        |    },
        |    {
        |      "boxId": "24bf92d7af14a2e56d83fc1d9e335ec629672f89763f9e33cf30f45d53595b49",
        |      "transactionId": "38503bcdbca4925dbc99b2d94e17f8e4ab998fec6e56a418f846c54109025a35",
        |      "blockId": "7ccd3d91784b6873025b57b5260fa51f7635c157c2669e0616cc0518c194d1c2",
        |      "value": 300000,
        |      "index": 2,
        |      "globalIndex": 26483092,
        |      "creationHeight": 938652,
        |      "settlementHeight": 938657,
        |      "ergoTree": "19a3041f040004000404040404000402060101040005000402040404020400040004020502040405020402040005feffffffffffffffff010408050205000404040004060404040205fcffffffffffffffff010100d80cd601b2a5730000d602db63087201d603e4c6a7050ed604b2a4730100d605db63087204d6068cb2720573020002d607998cb27202730300027206d608e4c6a70408d609db6308a7d60ab27209730400d60bb27205730500d60c7306d1ed938cb27202730700017203959372077308d80cd60db2a5e4e3000400d60eb2a5e4e3010400d60fb2e4c672040410730900d610c672010804d61199720f95e67210e47210e4c672010704d6128cb27209730a0001d613db6308720ed614b27209730b00d6158c721402d6167e721105d6179972159c72168c720a02d618b2db6308720d730c00eded93c2720dd07208edededed93e4c6720e0408720893e4c6720e050e720393c2720ec2a795917211730dd801d619b27213730e00eded9386027212730fb27213731000938c7219018c721401939972158c721902721793860272127311b2721373120093b27213731300720aed938c7218018c720b01927e8c7218020699999d9c99997e8c720b02069d9c7ee4c672040505067e7211067e720f06720c7e7217067e999973148cb27205731500029c9972067316721606720c720c958f7207731793b2db6308b2a473180073190086029593b17209731a8cb27209731b00018cb27209731c0001731d731e",
        |      "address": "YyLFSfjMQw5AJEuchDNPzziDH7CpLX8EbtGbddMpTpDoYYCc5xKJUbDuFnwsJ4qcpARjLmTQdRUTDiftnkb4kuNAaM7enwfxbWvh9SAbpvxAtJzhQApmedrEH9BBrPYzHdWbR354ZXZJkazhvfWMLyijyFeJ7aTDYmhDn8YvF3HeBMJiLByT32wTfNVEWQf4SEp3eN66ppqZs7bJAxrsZo3PDGJgTtZ2qPypMv3x9fcE6zYoLiCzHbAWC8WCfn5wNno9NAwSgWgHKcz3Gx4kXAe5DJnMDZJeiAuckS4e8J5sUfgFSPHTysvkfPw5pxBBNoxrsQ8y2jHSjB3KhSKKsNoZLnWZpbLQ7TurFc34TZJn9FNsRzpmQc9ftcZNa1QxKNoBtFzCpRXwvKd1RaSEZc5er7TwiyL9ebcfh1x3b3k2qXZhi6eXnktGBhEcNiJoJe7FdRA7gTXs9MCQewX8r7u3ay6i94bT9BrdXZy7xnmFU1DvyCcNP3sHHC1gDbNxdzVYsyF9tuxuXhmmD3rTc7hCPX4uo1dahq5HFaaQifp8kwe6iVuzp3Qo5vNPgV8tJBzXhGx7wgYoozGznh1ecYdBtCpHdEwzbME4bQy8qLXsBRB7TVnhQ2GYUL8XyCpzaziWEe1tdKrgbFFE1CNnLuGR7FEFikx5PfJVFTmFvpEvvtM7Qq9TMFjgsaoP58M7BajhU74EgqVRX3hi8pmmzxnjAzVKK5wZR62RcpowjrYsNLGjcWL8B",
        |      "assets": [
        |        {
        |          "tokenId": "ab987cc7de3127055872d36d88256d854944fe541cf8d70e7cc5c92d4647daf0",
        |          "index": 0,
        |          "amount": 444898,
        |          "name": null,
        |          "decimals": null,
        |          "type": null
        |        },
        |        {
        |          "tokenId": "bc58f841398f84fd0a72e2de50f8f3aed14e6234f7502bb7fe90729fba9c74f2",
        |          "index": 1,
        |          "amount": 889796,
        |          "name": null,
        |          "decimals": null,
        |          "type": null
        |        },
        |        {
        |          "tokenId": "00738c0e3c4d89a1a2d5e71a20e4a00262340f289e58d9d866719564a933bad5",
        |          "index": 2,
        |          "amount": 1,
        |          "name": null,
        |          "decimals": null,
        |          "type": null
        |        }
        |      ],
        |      "additionalRegisters": {
        |        "R4": {
        |          "serializedValue": "08cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec",
        |          "sigmaType": "SSigmaProp",
        |          "renderedValue": "03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec"
        |        },
        |        "R5": {
        |          "serializedValue": "0e200f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f",
        |          "sigmaType": "Coll[SByte]",
        |          "renderedValue": "0f30b560b6c396cc3fc2bdbcf41dd036070ed8b1df2df31ca292cfc5bbb38c0f"
        |        }
        |      },
        |      "spentTransactionId": "3156e7598a5944accd90232d8eb18da3ab1b72e7340445d75ad23ce4c0951f7b",
        |      "mainChain": true
        |    }
        |  ],
        |  "size": 6642
        |}
        |""".stripMargin
    ).toOption.get.toTransaction

  def compoundRegisterForRedeem =
    decode[Output](
      """
        |{
        |    "boxId": "353fb1e32f52c4353daf8c66c8426b36f2d1989a88efd64bd38ac3173dc3c4ca",
        |    "transactionId": "37137705008bf57ee71f3133d4af515e2a631dc34f89d10657eb62ba24ac5f27",
        |    "blockId": "a5df1a7937b961e1e5c8bea01e8cbd8d954af98ef6fa880ec991959a13e26601",
        |    "value": 1250000,
        |    "index": 3,
        |    "globalIndex": 26418732,
        |    "creationHeight": 937187,
        |    "settlementHeight": 937189,
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
        |            "tokenId": "cba6fabbc040c49873d3dea062a7fc81ff3262e1799dfd41e05014c5e8d91109",
        |            "index": 1,
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
        |    "spentTransactionId": "3fed3dbde8efd9e913486269872cc85ba211a63b5045f1008a754044b658d941",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

}
