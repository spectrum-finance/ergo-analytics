package fi.spectrum.parser.evaluation

import fi.spectrum.core.domain.analytics.{OffChainFee, ProcessedOrder}
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.{OrderId, PoolId}
import fi.spectrum.core.domain.{BoxId, PubKey}
import fi.spectrum.parser.models.TransactionTest
import io.circe.parser.decode

object Processed {

  val transactionSwap = decode[TransactionTest](
    """
      |{
      |    "id": "52f5c53103e2fe4108e25b2af16efa721ff8a7636d45e32d6e5128785c8e6c27",
      |    "blockId": "fb468a99f33444c94ca7c5842259b1f6d2b27ef6ab619b7f888d4fc501f5b596",
      |    "inclusionHeight": 553344,
      |    "timestamp": 1628811638631,
      |    "index": 2,
      |    "globalIndex": 1622679,
      |    "numConfirmations": 342558,
      |    "inputs": [
      |        {
      |            "boxId": "17f2e53f3f5e90b11db8997da7b871c086caf2728ff70624c2c5351d19654540",
      |            "value": 4000000,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "d4873ced1f9f87ab2750ac0ac44a79892cdc94997f7a8e347a07dead292f9b27",
      |            "outputTransactionId": "58d40c91a977b786eace659d145a942f90d2c08a9bd119a9fd924d621e4f5e89",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 6211599,
      |            "outputCreatedAt": 553281,
      |            "outputSettledAt": 553283,
      |            "ergoTree": "19a9030f040004020402040404040406040605feffffffffffffffff0105feffffffffffffffff01050004d00f0400040005000500d81ad601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d609b27203730500d60ab27204730600d60b9973078c720602d60c999973088c720502720bd60d8c720802d60e998c720702720dd60f91720e7309d6108c720a02d6117e721006d6127e720e06d613998c7209027210d6147e720d06d615730ad6167e721306d6177e720c06d6187e720b06d6199c72127218d61a9c72167218d1edededededed93c27201c2a793e4c672010404720292c17201c1a793b27203730b00b27204730c00938c7205018c720601ed938c7207018c720801938c7209018c720a019593720c730d95720f929c9c721172127e7202069c7ef07213069a9c72147e7215067e9c720e7e72020506929c9c721472167e7202069c7ef0720e069a9c72117e7215067e9c72137e7202050695ed720f917213730e907217a19d721972149d721a7211ed9272199c7217721492721a9c72177211",
      |            "address": "3gb1RZucekcRdda82TSNS4FZSREhGLoi1FxGDmMZdVeLtYYixPRviEdYireoM9RqC6Jf4kx85Y1jmUg5XzGgqdjpkhHm7kJZdgUR3VBwuLZuyHVqdSNv3eanqpknYsXtUwvUA16HFwNa3HgVRAnGC8zj8U7kksrfjycAM1yb19BB4TYR2BKWN7mpvoeoTuAKcAFH26cM46CEYsDRDn832wVNTLAmzz4Q6FqE29H9euwYzKiebgxQbWUxtupvfSbKaHpQcZAo5Dhyc6PFPyGVFZVRGZZ4Kftgi1NMRnGwKG7NTtXsFMsJP6A7yvLy8UZaMPe69BUAkpbSJdcWem3WpPUE7UpXv4itDkS5KVVaFtVyfx8PQxzi2eotP2uXtfairHuKinbpSFTSFKW3GxmXaw7vQs1JuVd8NhNShX6hxSqCP6sxojrqBxA48T2KcxNrmE3uFk7Pt4vPPdMAS4PW6UU82UD9rfhe3SMytK6DkjCocuRwuNqFoy4k25TXbGauTNgKuPKY3CxgkTpw9WfWsmtei178tLefhUEGJueueXSZo7negPYtmcYpoMhCuv4G1JZc283Q7f3mNXS",
      |            "assets": [
      |                {
      |                    "tokenId": "f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
      |                    "index": 1,
      |                    "amount": 9223371509578424871,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 2,
      |                    "amount": 1009748585414650,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 3,
      |                    "amount": 1731314327119765,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "04c80f",
      |                    "sigmaType": "SInt",
      |                    "renderedValue": "996"
      |                }
      |            }
      |        },
      |        {
      |            "boxId": "b7510c7cd1ff26bd61aec41a6ecb19fbbbcc38900f4451af3fcc127d424a24eb",
      |            "value": 16000000,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "b1b0e18db8afe5e7c2bcd5e4a80dea7d2e4c54098c75826f5989e8ad1c0b84b6",
      |            "outputTransactionId": "527046977fc5955c7f8c5fd27b08e593730b99b113c967b0dc8a32409509e4ec",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 6215413,
      |            "outputCreatedAt": 508928,
      |            "outputSettledAt": 553342,
      |            "ergoTree": "19a2031308cd02a97f8976bd6363bdba123d6325d71d17b1e53d22e233fdbfcadd65f4268e790904000e2030974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e04c80f04d00f040404080402040004040400040606010104000e20f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781059cdaed8d7f05d2d5b282c1a38501058080a0f6f4acdbe01b0100d805d6017300d602b2a4730100d6037302d6047303d6057304eb027201d195ed93b1a4730593b1db630872027306d80ad606db63087202d607b2a5730700d608b2db63087207730800d6098c720802d60a7e720906d60bb27206730900d60c7e8c720b0206d60d7e8cb2db6308a7730a000206d60e7e8cb27206730b000206d60f9a720a730cededededed938cb27206730d0001730e93c27207d07201938c7208017203927209730f927ec1720706997ec1a7069d9c720a7e7310067e73110695938c720b017203909c9c720c720d7e7204069c720f9a9c720e7e7205069c720d7e720406909c9c720e720d7e7204069c720f9a9c720c7e7205069c720d7e7204067312",
      |            "address": "H3AA3N1iexfbxFB3DUX1TxwqZssB87zYbcBqovvutcq6jFDNvi7ug42gkMnjfo7K14ZnCbNfxKRhn5aDd2NVSTkro4QJ5AttcEwdDa7QggGDYnGbcw3ZHM8VDAYQz3PoTSVyKByjAqiaFSBWU8jkKA5ruWJqwMRW84n2LFg9sek2KzrCcQ82zjjkpeLjMDwhpEqmdn4iscbw61xtgV1p5LeWEws6k5ARgJns5ZsmRr9cQFhbJ4cX5HzW6AJoKNEgvxVb26iMKmhKgjxvGpqLD2nXJgijk6jjGvuy39h3d2Hi1QwqGM4SB6He9aMZisWXjE8aHYuCHy1AzN2jxUUcEL3b3eXwDdgp3H2QvsqZrrqVVueEkL8qpKxzwmre55f7ccY4T6WZoDMqQjSPRdMRkF3BxZdpQNLwPgwJDV7oYtY3LWTSzPQxoRvt1BoWKzj2BQGc4i5VWqEpD3qMczSBHxt57yJXTb2Mxuth4urqornWJ6r2T3CMGTgWjjjqf7an1iheA8LUJjuFSv2f5kVMUbU97GvxvNDQkC9Vyn5BitnQrWXwtBMCbT8hvwhzRg6ABiJma",
      |            "assets": [
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 0,
      |                    "amount": 10000000000,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        }
      |    ],
      |    "dataInputs": [],
      |    "outputs": [
      |        {
      |            "boxId": "a73e348bac69ef67a8c4c64679dd8ee20a97b3168711f52b08ef7c15460b3948",
      |            "transactionId": "52f5c53103e2fe4108e25b2af16efa721ff8a7636d45e32d6e5128785c8e6c27",
      |            "blockId": "fb468a99f33444c94ca7c5842259b1f6d2b27ef6ab619b7f888d4fc501f5b596",
      |            "value": 4000000,
      |            "index": 0,
      |            "globalIndex": 6215545,
      |            "creationHeight": 553342,
      |            "settlementHeight": 553344,
      |            "ergoTree": "19a9030f040004020402040404040406040605feffffffffffffffff0105feffffffffffffffff01050004d00f0400040005000500d81ad601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d609b27203730500d60ab27204730600d60b9973078c720602d60c999973088c720502720bd60d8c720802d60e998c720702720dd60f91720e7309d6108c720a02d6117e721006d6127e720e06d613998c7209027210d6147e720d06d615730ad6167e721306d6177e720c06d6187e720b06d6199c72127218d61a9c72167218d1edededededed93c27201c2a793e4c672010404720292c17201c1a793b27203730b00b27204730c00938c7205018c720601ed938c7207018c720801938c7209018c720a019593720c730d95720f929c9c721172127e7202069c7ef07213069a9c72147e7215067e9c720e7e72020506929c9c721472167e7202069c7ef0720e069a9c72117e7215067e9c72137e7202050695ed720f917213730e907217a19d721972149d721a7211ed9272199c7217721492721a9c72177211",
      |            "address": "3gb1RZucekcRdda82TSNS4FZSREhGLoi1FxGDmMZdVeLtYYixPRviEdYireoM9RqC6Jf4kx85Y1jmUg5XzGgqdjpkhHm7kJZdgUR3VBwuLZuyHVqdSNv3eanqpknYsXtUwvUA16HFwNa3HgVRAnGC8zj8U7kksrfjycAM1yb19BB4TYR2BKWN7mpvoeoTuAKcAFH26cM46CEYsDRDn832wVNTLAmzz4Q6FqE29H9euwYzKiebgxQbWUxtupvfSbKaHpQcZAo5Dhyc6PFPyGVFZVRGZZ4Kftgi1NMRnGwKG7NTtXsFMsJP6A7yvLy8UZaMPe69BUAkpbSJdcWem3WpPUE7UpXv4itDkS5KVVaFtVyfx8PQxzi2eotP2uXtfairHuKinbpSFTSFKW3GxmXaw7vQs1JuVd8NhNShX6hxSqCP6sxojrqBxA48T2KcxNrmE3uFk7Pt4vPPdMAS4PW6UU82UD9rfhe3SMytK6DkjCocuRwuNqFoy4k25TXbGauTNgKuPKY3CxgkTpw9WfWsmtei178tLefhUEGJueueXSZo7negPYtmcYpoMhCuv4G1JZc283Q7f3mNXS",
      |            "assets": [
      |                {
      |                    "tokenId": "f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
      |                    "index": 1,
      |                    "amount": 9223371509578424871,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 2,
      |                    "amount": 1009758585414650,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 3,
      |                    "amount": 1731297249878106,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "04c80f",
      |                    "sigmaType": "SInt",
      |                    "renderedValue": "996"
      |                }
      |            },
      |            "spentTransactionId": "70565ff8fa560534e05e7c7209558fd6b403fd93df85fd54bd11db2b36aa3e13",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "704ef2e3c3b6f8c6a909594e97a8a904a61504ad80741db0a4c4f26d090c5b21",
      |            "transactionId": "52f5c53103e2fe4108e25b2af16efa721ff8a7636d45e32d6e5128785c8e6c27",
      |            "blockId": "fb468a99f33444c94ca7c5842259b1f6d2b27ef6ab619b7f888d4fc501f5b596",
      |            "value": 10995001,
      |            "index": 1,
      |            "globalIndex": 6215546,
      |            "creationHeight": 553342,
      |            "settlementHeight": 553344,
      |            "ergoTree": "0008cd02a97f8976bd6363bdba123d6325d71d17b1e53d22e233fdbfcadd65f4268e7909",
      |            "address": "9foi7qzy8L73BpsGeweyWXw29yH1NgfnyExY2QjTairZo5orabB",
      |            "assets": [
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 0,
      |                    "amount": 17077241659,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "096252e7c50b88403477849193d87966def51e3e38d71a3007b1578218b94db9",
      |            "transactionId": "52f5c53103e2fe4108e25b2af16efa721ff8a7636d45e32d6e5128785c8e6c27",
      |            "blockId": "fb468a99f33444c94ca7c5842259b1f6d2b27ef6ab619b7f888d4fc501f5b596",
      |            "value": 4004999,
      |            "index": 2,
      |            "globalIndex": 6215547,
      |            "creationHeight": 553342,
      |            "settlementHeight": 553344,
      |            "ergoTree": "0008cd02ddbe95b7f88d47bd8c2db823cc5dd1be69a650556a44d4c15ac65e1d3e34324c",
      |            "address": "9gCigPc9cZNRhKgbgdmTkVxo1ZKgw79G8DvLjCcYWAvEF3XRUKy",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "851bb3cd56024a30160dc7a7ff4b3291f6004e339653d6faaa4878952ca413e8",
      |            "transactionId": "52f5c53103e2fe4108e25b2af16efa721ff8a7636d45e32d6e5128785c8e6c27",
      |            "blockId": "fb468a99f33444c94ca7c5842259b1f6d2b27ef6ab619b7f888d4fc501f5b596",
      |            "value": 1000000,
      |            "index": 3,
      |            "globalIndex": 6215548,
      |            "creationHeight": 553342,
      |            "settlementHeight": 553344,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "df163b349923875b3f5e2710330329504728e37a3a1c494d8b2c5a9775f6d373",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 879
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  val transactionSwapRegister = decode[TransactionTest](
    """{
      |    "id": "9d2984a1e53397dacf90ce46b57bedad012c57f7d123d66765dfac2e037b3ae2",
      |    "blockId": "32c6a6150e65fb8d1a885698492db597e8900d21aaeb377dc99722cbe3f65666",
      |    "inclusionHeight": 801869,
      |    "timestamp": 1658824687930,
      |    "index": 1,
      |    "globalIndex": 3620172,
      |    "numConfirmations": 95574,
      |    "inputs": [
      |        {
      |            "boxId": "c845f0d525e47ee0b205b656cf01225d3af2cd28cd7cf941205096eabdd4f31a",
      |            "value": 10000000,
      |            "index": 0,
      |            "spendingProof": "5563145e5bb48266e7032e89658d392fbeef95620bab4d98e91f1b5ed665ca136320796ccb918282100539a3c0055fca8884b9a719eb0403",
      |            "outputBlockId": "1e7518aae1a9ff800362c4c37825d3a710d5f89e0f6e43dd367c5b72ec098ab0",
      |            "outputTransactionId": "0eab68cce8ac8d19553c7d35d70529796cfec438542b99948e0a9aabe0fd85b4",
      |            "outputIndex": 1,
      |            "outputGlobalIndex": 19489523,
      |            "outputCreatedAt": 801863,
      |            "outputSettledAt": 801865,
      |            "ergoTree": "0008cd02be94fad8518987c0e360d5d196ae92a34ca7be0c76e8cad9ca5f42e05b3b9d8a",
      |            "address": "9fxzg7gQXzaC54g5fjv3iPdNbc75eQU1WSMhwyLhHLQceKm6GqW",
      |            "assets": [
      |                {
      |                    "tokenId": "1fd6e032e8476c4aa54c18c1a308dce83940e8f4a28f576440513ed7326ad489",
      |                    "index": 0,
      |                    "amount": 41146967,
      |                    "name": "Paideia",
      |                    "decimals": 4,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        }
      |    ],
      |    "dataInputs": [],
      |    "outputs": [
      |        {
      |            "boxId": "0005dae129cc47fbac5540042ac138b34b94027c8b94d82059cfd5b379f1296f",
      |            "transactionId": "9d2984a1e53397dacf90ce46b57bedad012c57f7d123d66765dfac2e037b3ae2",
      |            "blockId": "32c6a6150e65fb8d1a885698492db597e8900d21aaeb377dc99722cbe3f65666",
      |            "value": 7260000,
      |            "index": 0,
      |            "globalIndex": 19489589,
      |            "creationHeight": 801866,
      |            "settlementHeight": 801869,
      |            "ergoTree": "19b5031508cd03ef0d763cb8634cf11868e50f90177a4c20f1d8871c6d02f7eca0318cabfc6d8c0400040404060402058080a0f6f4acdbe01b05f48cf4c0eb91fbdd1b040004000e20666be5df835a48b99c40a395a8aa3ea6ce39ede2cd77c02921d629b9baad82000596caecdb3904ca0f060101040404d00f04ca0f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d804d603db63087202d604b2a5730400d6059d9c7e99c17204c1a7067e7305067e730606d6068cb2db6308a773070002edededed938cb2720373080001730993c27204d072019272057e730a06909c9c7ec17202067e7206067e730b069c9a7205730c9a9c7e8cb27203730d0002067e730e067e9c72067e730f050690b0ada5d90107639593c272077310c1720773117312d90107599a8c7207018c72070273137314",
      |            "address": "Dxg1z3EWagHndnKUzwuwVZB97YN5BM5Y9B1SNfzHhLyt5RZSVU3q3kAxPdyBNN6zaYXFj5PcQscK7mc2DGTGxLqp4HmNjVeYBgaXe1KvP7U54zfr6jWMchfhbXRddaS9zLsE6wKK1E3uUZTQM12uDtGtwg1uN8XK47btXbW9t3mV6dEExbhJaHPNQaF4LwrfsQojRzBouUTDkb6hPT8ur7J2tbSwPVcW3S2EdcZ8b4i1JBMfxq4mxBNhyEbK5P31QXSf7Pmwe4Wjso66TL8bDfDZwDTkQFKa3CTR8eeuqF1ax1fWPJ3pEwKbWvHqiDhBWASaVGzK9Q2Kd7tdDuHsSJRewpQHiJgeGn7Lj9Hazyth1jvQSQtn4So8PDbopRba2QTDNGodF1ksGtuLmVL8Eu9RJjhPVQ1sK8qwJLiZ1iSSbwqxoMjPY5JdYZkyG3ZKcfvUNiyC1zqCHsStz1ZD4sPXeVsbJ6LtmQLao2k7Wsj6Lp15C3iGjzcs2CeQqM8S5jDTMcVkDwyjnHnUKLrsFk4D5v5rKoVQD2qu1yjdbCaqgZMQ55x13JzrvEBfQr49CqcoUbFHmjUaFYMhxVKQ7dmaF1582Qx",
      |            "assets": [
      |                {
      |                    "tokenId": "1fd6e032e8476c4aa54c18c1a308dce83940e8f4a28f576440513ed7326ad489",
      |                    "index": 0,
      |                    "amount": 15000000,
      |                    "name": "Paideia",
      |                    "decimals": 4,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "1d968bd9273142812b585e9eece9f1fbc7a4e225795d3f7ccd82ee85c992794f",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "87f30ba03f9ab64e5d160db83f112a4fc72e444a75cddb1affcc4f08056fb3ff",
      |            "transactionId": "9d2984a1e53397dacf90ce46b57bedad012c57f7d123d66765dfac2e037b3ae2",
      |            "blockId": "32c6a6150e65fb8d1a885698492db597e8900d21aaeb377dc99722cbe3f65666",
      |            "value": 740000,
      |            "index": 1,
      |            "globalIndex": 19489590,
      |            "creationHeight": 801866,
      |            "settlementHeight": 801869,
      |            "ergoTree": "0008cd03ef0d763cb8634cf11868e50f90177a4c20f1d8871c6d02f7eca0318cabfc6d8c",
      |            "address": "9iH5y9bRn3RhoAtbvusDJYMv3g8GWxrZYFBUbnESuEdmWJF23vD",
      |            "assets": [
      |                {
      |                    "tokenId": "1fd6e032e8476c4aa54c18c1a308dce83940e8f4a28f576440513ed7326ad489",
      |                    "index": 0,
      |                    "amount": 26146967,
      |                    "name": "Paideia",
      |                    "decimals": 4,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "00543e092a0494f6b447ea760dd9c57d7080c27cba2437e16b26c744a8d454f0",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "e5e161b313f089fd921d91c756b745a971af45d084230aae4d70eec91590ef0d",
      |            "transactionId": "9d2984a1e53397dacf90ce46b57bedad012c57f7d123d66765dfac2e037b3ae2",
      |            "blockId": "32c6a6150e65fb8d1a885698492db597e8900d21aaeb377dc99722cbe3f65666",
      |            "value": 2000000,
      |            "index": 2,
      |            "globalIndex": 19489591,
      |            "creationHeight": 801866,
      |            "settlementHeight": 801869,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "399c21df7cd0634d79fdfbef48d6322a5631a09a01b9f7f38126cecf9a9d70f5",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 742
      |}""".stripMargin
  ).toOption.get.toTransaction

  val transactionSwapExecuted = decode[TransactionTest](
    """
      |{
      |    "id": "1d968bd9273142812b585e9eece9f1fbc7a4e225795d3f7ccd82ee85c992794f",
      |    "blockId": "32c6a6150e65fb8d1a885698492db597e8900d21aaeb377dc99722cbe3f65666",
      |    "inclusionHeight": 801869,
      |    "timestamp": 1658824687930,
      |    "index": 2,
      |    "globalIndex": 3620173,
      |    "numConfirmations": 95689,
      |    "inputs": [
      |        {
      |            "boxId": "fea121950f103c794d9d30e70aa14c659fe4daefd562244aee943ee6ccb9cb5a",
      |            "value": 58456637432771,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "8556f8520bde04e44a49831e46be2e37d4799423ddfe10f8a8004fd6f05b6cac",
      |            "outputTransactionId": "7b273ee58a87d6fe7cf92d3e01f7c45186435a0ce53d2585c29411dbce20e005",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 19489183,
      |            "outputCreatedAt": 0,
      |            "outputSettledAt": 801858,
      |            "ergoTree": "1999030f0400040204020404040405feffffffffffffffff0105feffffffffffffffff01050004d00f040004000406050005000580dac409d819d601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d6099973058c720602d60a999973068c7205027209d60bc17201d60cc1a7d60d99720b720cd60e91720d7307d60f8c720802d6107e720f06d6117e720d06d612998c720702720fd6137e720c06d6147308d6157e721206d6167e720a06d6177e720906d6189c72117217d6199c72157217d1ededededededed93c27201c2a793e4c672010404720293b27203730900b27204730a00938c7205018c720601938c7207018c72080193b17203730b9593720a730c95720e929c9c721072117e7202069c7ef07212069a9c72137e7214067e9c720d7e72020506929c9c721372157e7202069c7ef0720d069a9c72107e7214067e9c72127e7202050695ed720e917212730d907216a19d721872139d72197210ed9272189c721672139272199c7216721091720b730e",
      |            "address": "5vSUZRZbdVbnk4sJWjg2uhL94VZWRg4iatK9VgMChufzUgdihgvhR8yWSUEJKszzV7Vmi6K8hCyKTNhUaiP8p5ko6YEU9yfHpjVuXdQ4i5p4cRCzch6ZiqWrNukYjv7Vs5jvBwqg5hcEJ8u1eerr537YLWUoxxi1M4vQxuaCihzPKMt8NDXP4WcbN6mfNxxLZeGBvsHVvVmina5THaECosCWozKJFBnscjhpr3AJsdaL8evXAvPfEjGhVMoTKXAb2ZGGRmR8g1eZshaHmgTg2imSiaoXU5eiF3HvBnDuawaCtt674ikZ3oZdekqswcVPGMwqqUKVsGY4QuFeQoGwRkMqEYTdV2UDMMsfrjrBYQYKUBFMwsQGMNBL1VoY78aotXzdeqJCBVKbQdD3ZZWvukhSe4xrz8tcF3PoxpysDLt89boMqZJtGEHTV9UBTBEac6sDyQP693qT3nKaErN8TCXrJBUmHPqKozAg9bwxTqMYkpmb9iVKLSoJxG7MjAj72SRbcqQfNCVTztSwN3cRxSrVtz4p87jNFbVtFzhPg7UqDwNFTaasySCqM",
      |            "assets": [
      |                {
      |                    "tokenId": "666be5df835a48b99c40a395a8aa3ea6ce39ede2cd77c02921d629b9baad8200",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "879c71d7d9ad213024962824e7f6f225b282dfb818326b46e80e155a11a90544",
      |                    "index": 1,
      |                    "amount": 9223369527057301742,
      |                    "name": "ERG_Paideia_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "1fd6e032e8476c4aa54c18c1a308dce83940e8f4a28f576440513ed7326ad489",
      |                    "index": 2,
      |                    "amount": 109548789936,
      |                    "name": "Paideia",
      |                    "decimals": 4,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "04ca0f",
      |                    "sigmaType": "SInt",
      |                    "renderedValue": "997"
      |                }
      |            }
      |        },
      |        {
      |            "boxId": "0005dae129cc47fbac5540042ac138b34b94027c8b94d82059cfd5b379f1296f",
      |            "value": 7260000,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "32c6a6150e65fb8d1a885698492db597e8900d21aaeb377dc99722cbe3f65666",
      |            "outputTransactionId": "9d2984a1e53397dacf90ce46b57bedad012c57f7d123d66765dfac2e037b3ae2",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 19489589,
      |            "outputCreatedAt": 801866,
      |            "outputSettledAt": 801869,
      |            "ergoTree": "19b5031508cd03ef0d763cb8634cf11868e50f90177a4c20f1d8871c6d02f7eca0318cabfc6d8c0400040404060402058080a0f6f4acdbe01b05f48cf4c0eb91fbdd1b040004000e20666be5df835a48b99c40a395a8aa3ea6ce39ede2cd77c02921d629b9baad82000596caecdb3904ca0f060101040404d00f04ca0f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d804d603db63087202d604b2a5730400d6059d9c7e99c17204c1a7067e7305067e730606d6068cb2db6308a773070002edededed938cb2720373080001730993c27204d072019272057e730a06909c9c7ec17202067e7206067e730b069c9a7205730c9a9c7e8cb27203730d0002067e730e067e9c72067e730f050690b0ada5d90107639593c272077310c1720773117312d90107599a8c7207018c72070273137314",
      |            "address": "Dxg1z3EWagHndnKUzwuwVZB97YN5BM5Y9B1SNfzHhLyt5RZSVU3q3kAxPdyBNN6zaYXFj5PcQscK7mc2DGTGxLqp4HmNjVeYBgaXe1KvP7U54zfr6jWMchfhbXRddaS9zLsE6wKK1E3uUZTQM12uDtGtwg1uN8XK47btXbW9t3mV6dEExbhJaHPNQaF4LwrfsQojRzBouUTDkb6hPT8ur7J2tbSwPVcW3S2EdcZ8b4i1JBMfxq4mxBNhyEbK5P31QXSf7Pmwe4Wjso66TL8bDfDZwDTkQFKa3CTR8eeuqF1ax1fWPJ3pEwKbWvHqiDhBWASaVGzK9Q2Kd7tdDuHsSJRewpQHiJgeGn7Lj9Hazyth1jvQSQtn4So8PDbopRba2QTDNGodF1ksGtuLmVL8Eu9RJjhPVQ1sK8qwJLiZ1iSSbwqxoMjPY5JdYZkyG3ZKcfvUNiyC1zqCHsStz1ZD4sPXeVsbJ6LtmQLao2k7Wsj6Lp15C3iGjzcs2CeQqM8S5jDTMcVkDwyjnHnUKLrsFk4D5v5rKoVQD2qu1yjdbCaqgZMQ55x13JzrvEBfQr49CqcoUbFHmjUaFYMhxVKQ7dmaF1582Qx",
      |            "assets": [
      |                {
      |                    "tokenId": "1fd6e032e8476c4aa54c18c1a308dce83940e8f4a28f576440513ed7326ad489",
      |                    "index": 0,
      |                    "amount": 15000000,
      |                    "name": "Paideia",
      |                    "decimals": 4,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        }
      |    ],
      |    "dataInputs": [],
      |    "outputs": [
      |        {
      |            "boxId": "a52b8cb2a70bfc247c118e9ffed3ebc36f3e662caaa9d572ece528b7494c5f3a",
      |            "transactionId": "1d968bd9273142812b585e9eece9f1fbc7a4e225795d3f7ccd82ee85c992794f",
      |            "blockId": "32c6a6150e65fb8d1a885698492db597e8900d21aaeb377dc99722cbe3f65666",
      |            "value": 58448658342486,
      |            "index": 0,
      |            "globalIndex": 19489592,
      |            "creationHeight": 0,
      |            "settlementHeight": 801869,
      |            "ergoTree": "1999030f0400040204020404040405feffffffffffffffff0105feffffffffffffffff01050004d00f040004000406050005000580dac409d819d601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d6099973058c720602d60a999973068c7205027209d60bc17201d60cc1a7d60d99720b720cd60e91720d7307d60f8c720802d6107e720f06d6117e720d06d612998c720702720fd6137e720c06d6147308d6157e721206d6167e720a06d6177e720906d6189c72117217d6199c72157217d1ededededededed93c27201c2a793e4c672010404720293b27203730900b27204730a00938c7205018c720601938c7207018c72080193b17203730b9593720a730c95720e929c9c721072117e7202069c7ef07212069a9c72137e7214067e9c720d7e72020506929c9c721372157e7202069c7ef0720d069a9c72107e7214067e9c72127e7202050695ed720e917212730d907216a19d721872139d72197210ed9272189c721672139272199c7216721091720b730e",
      |            "address": "5vSUZRZbdVbnk4sJWjg2uhL94VZWRg4iatK9VgMChufzUgdihgvhR8yWSUEJKszzV7Vmi6K8hCyKTNhUaiP8p5ko6YEU9yfHpjVuXdQ4i5p4cRCzch6ZiqWrNukYjv7Vs5jvBwqg5hcEJ8u1eerr537YLWUoxxi1M4vQxuaCihzPKMt8NDXP4WcbN6mfNxxLZeGBvsHVvVmina5THaECosCWozKJFBnscjhpr3AJsdaL8evXAvPfEjGhVMoTKXAb2ZGGRmR8g1eZshaHmgTg2imSiaoXU5eiF3HvBnDuawaCtt674ikZ3oZdekqswcVPGMwqqUKVsGY4QuFeQoGwRkMqEYTdV2UDMMsfrjrBYQYKUBFMwsQGMNBL1VoY78aotXzdeqJCBVKbQdD3ZZWvukhSe4xrz8tcF3PoxpysDLt89boMqZJtGEHTV9UBTBEac6sDyQP693qT3nKaErN8TCXrJBUmHPqKozAg9bwxTqMYkpmb9iVKLSoJxG7MjAj72SRbcqQfNCVTztSwN3cRxSrVtz4p87jNFbVtFzhPg7UqDwNFTaasySCqM",
      |            "assets": [
      |                {
      |                    "tokenId": "666be5df835a48b99c40a395a8aa3ea6ce39ede2cd77c02921d629b9baad8200",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "879c71d7d9ad213024962824e7f6f225b282dfb818326b46e80e155a11a90544",
      |                    "index": 1,
      |                    "amount": 9223369527057301742,
      |                    "name": "ERG_Paideia_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "1fd6e032e8476c4aa54c18c1a308dce83940e8f4a28f576440513ed7326ad489",
      |                    "index": 2,
      |                    "amount": 109563789936,
      |                    "name": "Paideia",
      |                    "decimals": 4,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "04ca0f",
      |                    "sigmaType": "SInt",
      |                    "renderedValue": "997"
      |                }
      |            },
      |            "spentTransactionId": "14018de5fb17fe6acfa0425e1c1f96931687e336660975c55bb1373c918037b8",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "186e3f0e7429bfce1ad5cc9252b5625e645b4895aac6b9c1e471cec5de7e2e7c",
      |            "transactionId": "1d968bd9273142812b585e9eece9f1fbc7a4e225795d3f7ccd82ee85c992794f",
      |            "blockId": "32c6a6150e65fb8d1a885698492db597e8900d21aaeb377dc99722cbe3f65666",
      |            "value": 7980170310,
      |            "index": 1,
      |            "globalIndex": 19489593,
      |            "creationHeight": 0,
      |            "settlementHeight": 801869,
      |            "ergoTree": "0008cd03ef0d763cb8634cf11868e50f90177a4c20f1d8871c6d02f7eca0318cabfc6d8c",
      |            "address": "9iH5y9bRn3RhoAtbvusDJYMv3g8GWxrZYFBUbnESuEdmWJF23vD",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "00543e092a0494f6b447ea760dd9c57d7080c27cba2437e16b26c744a8d454f0",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "9afbf0c486d578476136aae7805a674432933118da0f6d142fbe4c615bf422c0",
      |            "transactionId": "1d968bd9273142812b585e9eece9f1fbc7a4e225795d3f7ccd82ee85c992794f",
      |            "blockId": "32c6a6150e65fb8d1a885698492db597e8900d21aaeb377dc99722cbe3f65666",
      |            "value": 4179975,
      |            "index": 2,
      |            "globalIndex": 19489594,
      |            "creationHeight": 0,
      |            "settlementHeight": 801869,
      |            "ergoTree": "0008cd0273cbc003da723c0a5f416929692e8ec8c2b1e0d9aed69ff681f7581c63e70309",
      |            "address": "9fQ4NAwq8KyCHuVpwTDxK5yWqLgfv36Q83DAfsmvgSn8S4JtnPg",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "f5700fa2dd08fc4850a2350fc2d77a29a6b5b321bb28e0c459d375ae4d285200",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "bfc845aacf5861787dc27847b790c841e024e8de8f59f5be4d36774088fe4fdb",
      |            "transactionId": "1d968bd9273142812b585e9eece9f1fbc7a4e225795d3f7ccd82ee85c992794f",
      |            "blockId": "32c6a6150e65fb8d1a885698492db597e8900d21aaeb377dc99722cbe3f65666",
      |            "value": 2000000,
      |            "index": 3,
      |            "globalIndex": 19489595,
      |            "creationHeight": 0,
      |            "settlementHeight": 801869,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "399c21df7cd0634d79fdfbef48d6322a5631a09a01b9f7f38126cecf9a9d70f5",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 810
      |}
      |""".stripMargin).toOption.get.toTransaction

  val refundTx = decode[TransactionTest](
    """
      |{
      |    "id": "62f2c7a7d6042f4e33b62cf303c9a7e617ed433a7ecb0715cceeb0f89ab5e0d3",
      |    "blockId": "aa33e346d38c5dcabffc25f6e457128696e72381102ece19bf435e2596845e2e",
      |    "inclusionHeight": 620612,
      |    "timestamp": 1636980869447,
      |    "index": 1,
      |    "globalIndex": 2124350,
      |    "numConfirmations": 275400,
      |    "inputs": [
      |        {
      |            "boxId": "5e145f3cf77444e8c54f6e9c0b6fe35d59b23ac01bb9eca5fd27b19fc991502c",
      |            "value": 1007050000,
      |            "index": 0,
      |            "spendingProof": "2178dcb402bfbae451c2677b8ea63faa195a1a3b52e344d0f7cc3947fb7497b493fda09e4ccbc1f69a49c1f3883bb43b45c2092ee77ffcd2",
      |            "outputBlockId": "85cbdc14cb467e22ac2253e0bba91ee5e39ae413d3935f96422da6ba0490b888",
      |            "outputTransactionId": "3b3a3a168a5295e2fe23e43401007093ee3b1da358394bd9e632aca566b7ffad",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 9695386,
      |            "outputCreatedAt": 506880,
      |            "outputSettledAt": 620525,
      |            "ergoTree": "19a3031408cd02c3f56e66191a903758f53a4b90d07cef80f93e7a4f17d106098ad0caf189722a04000580a8d6b907040404060402040205feffffffffffffffff0104040400040004000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec040404020580bfd6060580a8d6b9070580bfd60601000100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d80bd604db63087202d605b2a5730500d606b27204730600d6077e9973078c72060206d6087ec1720206d6099d9c7e72030672077208d60ab27204730800d60b7e8c720a0206d60c9d9c7e8cb2db6308a773090002067207720bd60ddb63087205d60eb2720d730a00edededed938cb27204730b0001730c93c27205d0720195ed8f7209720c93b1720d730dd801d60fb2720d730e00eded92c172059999c1a7730f7310938c720f018c720a01927e8c720f02069d9c99720c7209720b720795927209720c927ec1720506997e99c1a7731106997e7203069d9c997209720c720872077312938c720e018c720601927e8c720e0206a17209720c7313",
      |            "address": "2DnYatBTAZCzvhuJMVK9Fp8JZ2et2xVVvQamZfPTuPxWe3cEnasrcg9veQfHvvppR8cKjsMBPmpoyzYVDwLrsFtkZ1Sg9APTaqsny9WqnEwNPoZgHT9JVDxqpb19XrXugG7ta2mQAbGVYDHVnyWYEtsDpij13YnqtiEvB9VB1Y1iiStgQqU4utqkefHpucGHJfcXubdz2DZbH7CNefoTjEwrEHpyuJN6Y2Vp9mwvBNzhoUzGQThg1XXkxNS624NkYCh5P48GF2Do8QX8w8oH8pvQ4Typ5sWH2r6CA3AXXykD4VHxHetyMNZU2TM8i4nH3d27n8u6Kwoa22PjV7aw5TSYk9xvpxiwuR8sxvb7sgarNqM1XWmsB2fYr8QWqL8p2La2NonXSvcb7gryQ9judx6WfsVoswFCY4pBr233EZLjansGv8c3Yuvg4yU2BRVyoEAf9Qt9jPGEDToaRuX9EDgHxvQBfTAryYP1p7Q3rPtFLezWygZ1HWSCjhz3HczkRM2UDvRCVnZ6m1cuDmozQ5DKRgx9c3h2oxMZn35CPQJ8JzSuskf1fJ2QER6kg4eEQkKmb1w",
      |            "assets": [
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 0,
      |                    "amount": 2000,
      |                    "name": "SigUSD",
      |                    "decimals": 2,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        }
      |    ],
      |    "dataInputs": [],
      |    "outputs": [
      |        {
      |            "boxId": "b0b0b8e42532a2ae24b3590f09a8435c3e3e624aee96c80770294d2530db4003",
      |            "transactionId": "62f2c7a7d6042f4e33b62cf303c9a7e617ed433a7ecb0715cceeb0f89ab5e0d3",
      |            "blockId": "aa33e346d38c5dcabffc25f6e457128696e72381102ece19bf435e2596845e2e",
      |            "value": 1005050000,
      |            "index": 0,
      |            "globalIndex": 9699959,
      |            "creationHeight": 506880,
      |            "settlementHeight": 620612,
      |            "ergoTree": "0008cd02c3f56e66191a903758f53a4b90d07cef80f93e7a4f17d106098ad0caf189722a",
      |            "address": "9g1N1xqhrNG1b2TkmFcQGTFZ47EquUYUZAiWWCBEbZaBcsMhXJU",
      |            "assets": [
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 0,
      |                    "amount": 2000,
      |                    "name": "SigUSD",
      |                    "decimals": 2,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "26148fb2bda5febc6f9cce2ee90b7437174e98e5086d04a8c23b01fd117417a7",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "03b403a4f458ba5a95a7e3622ba680eeade60847f432b0b92d34a724f41f7cde",
      |            "transactionId": "62f2c7a7d6042f4e33b62cf303c9a7e617ed433a7ecb0715cceeb0f89ab5e0d3",
      |            "blockId": "aa33e346d38c5dcabffc25f6e457128696e72381102ece19bf435e2596845e2e",
      |            "value": 2000000,
      |            "index": 1,
      |            "globalIndex": 9699960,
      |            "creationHeight": 506880,
      |            "settlementHeight": 620612,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "c3fa9f174f2dbef3adf7b6c916383237c9e2f9dfc38e1d83a4629f9bf6e8910a",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 288
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  val expectedFee = OffChainFee(
    PoolId.unsafeFromString("f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781"),
    OrderId("b7510c7cd1ff26bd61aec41a6ecb19fbbbcc38900f4451af3fcc127d424a24eb"),
    BoxId("096252e7c50b88403477849193d87966def51e3e38d71a3007b1578218b94db9"),
    PubKey.unsafeFromString("02ddbe95b7f88d47bd8c2db823cc5dd1be69a650556a44d4c15ac65e1d3e34324c"),
    ERG(4004999)
  )

  val transactionRedeem = decode[TransactionTest]("""
      |{
      |    "id": "883d7a8e5abd00779a098d3e191306e486f854c7de6b6a3bdbb071efb2bddbeb",
      |    "blockId": "1b3832dafc313efaaa3dc12b6f2ee97ab8ad8a0fa8056516ed067c5e17bbc375",
      |    "inclusionHeight": 551914,
      |    "timestamp": 1628621771945,
      |    "index": 8,
      |    "globalIndex": 1612034,
      |    "numConfirmations": 344095,
      |    "inputs": [
      |        {
      |            "boxId": "c16dd0cc0d77cad76c4dfae7edae6bc53f818bd021f6ed076bdfc3f617fe8ce2",
      |            "value": 4000000,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "08de70ef2227559f9203acf2cb4e863961117ae6d5eab716fc46f25e3c58155f",
      |            "outputTransactionId": "e745147642bead191a6e9b755f0d11c6b3c6fcfb5e59ad9de194b81cd03241fe",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 6149443,
      |            "outputCreatedAt": 551789,
      |            "outputSettledAt": 551791,
      |            "ergoTree": "19a9030f040004020402040404040406040605feffffffffffffffff0105feffffffffffffffff01050004d00f0400040005000500d81ad601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d609b27203730500d60ab27204730600d60b9973078c720602d60c999973088c720502720bd60d8c720802d60e998c720702720dd60f91720e7309d6108c720a02d6117e721006d6127e720e06d613998c7209027210d6147e720d06d615730ad6167e721306d6177e720c06d6187e720b06d6199c72127218d61a9c72167218d1edededededed93c27201c2a793e4c672010404720292c17201c1a793b27203730b00b27204730c00938c7205018c720601ed938c7207018c720801938c7209018c720a019593720c730d95720f929c9c721172127e7202069c7ef07213069a9c72147e7215067e9c720e7e72020506929c9c721472167e7202069c7ef0720e069a9c72117e7215067e9c72137e7202050695ed720f917213730e907217a19d721972149d721a7211ed9272199c7217721492721a9c72177211",
      |            "address": "3gb1RZucekcRdda82TSNS4FZSREhGLoi1FxGDmMZdVeLtYYixPRviEdYireoM9RqC6Jf4kx85Y1jmUg5XzGgqdjpkhHm7kJZdgUR3VBwuLZuyHVqdSNv3eanqpknYsXtUwvUA16HFwNa3HgVRAnGC8zj8U7kksrfjycAM1yb19BB4TYR2BKWN7mpvoeoTuAKcAFH26cM46CEYsDRDn832wVNTLAmzz4Q6FqE29H9euwYzKiebgxQbWUxtupvfSbKaHpQcZAo5Dhyc6PFPyGVFZVRGZZ4Kftgi1NMRnGwKG7NTtXsFMsJP6A7yvLy8UZaMPe69BUAkpbSJdcWem3WpPUE7UpXv4itDkS5KVVaFtVyfx8PQxzi2eotP2uXtfairHuKinbpSFTSFKW3GxmXaw7vQs1JuVd8NhNShX6hxSqCP6sxojrqBxA48T2KcxNrmE3uFk7Pt4vPPdMAS4PW6UU82UD9rfhe3SMytK6DkjCocuRwuNqFoy4k25TXbGauTNgKuPKY3CxgkTpw9WfWsmtei178tLefhUEGJueueXSZo7negPYtmcYpoMhCuv4G1JZc283Q7f3mNXS",
      |            "assets": [
      |                {
      |                    "tokenId": "f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
      |                    "index": 1,
      |                    "amount": 9223372034002758587,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 2,
      |                    "amount": 5194427367030,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 3,
      |                    "amount": 9844380057362,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "04c80f",
      |                    "sigmaType": "SInt",
      |                    "renderedValue": "996"
      |                }
      |            }
      |        },
      |        {
      |            "boxId": "4b7566339451a7feb4316b73607e1095b40dae86de72cf7c21016dd6ddbc3502",
      |            "value": 20000000,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "a6923960f72f724dd668d590d306fee080b2cac1f742b215667db3cb0a8e9a39",
      |            "outputTransactionId": "cf9a47326f68d1f3f51c839e002d7f37d65bc10c2724bf772cd795c7340c70e9",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 6154418,
      |            "outputCreatedAt": 506880,
      |            "outputSettledAt": 551912,
      |            "ergoTree": "19c3021108cd02c3f56e66191a903758f53a4b90d07cef80f93e7a4f17d106098ad0caf189722a04000404040804020400040404020406040005feffffffffffffffff01040204000e20f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc78105020580dac4090100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d809d603db63087202d604b2a5730400d605db63087204d606b27205730500d607b27203730600d608b27205730700d609b27203730800d60a7e8cb2db6308a77309000206d60b7e99730a8cb27203730b000206edededededed93b27203730c008602730d730e93c27204d0720192c1720499c1a7730f938c7206018c720701938c7208018c720901927e8c720602069d9c720a7e8c72070206720b927e8c720802069d9c720a7e8c72090206720b7310",
      |            "address": "oZfBf79wWTJbR3fqfvksQvzVy7A1rSPTbFdEPCCp7oNoXE6GUwRMVqCBipWdUkMswEHJZZVbpZPXzuJozSBfgCHUT82YVJoHb8dmpm4ae2mafBRazsjxv7em5VaLd9BVCPRy12b1JF3WbBdcj8usPJyu6SMe1bpmUTdRWk9RTerFfYtBKMY3C4a8xg94mwrepFcCuCHi3GxdvH64NaR2UZucKgBYfVjg4wPdDPqH2bgJrLxEzDFNBEyPfqgEaeg3suoDTV765ogfAhVMxJzD6rbsUzRRkNqzsvNs2vJnMp5PR34R25gaACa3EkoiRzDDjxfq8cewNo9cZgYHQtuEPm5G7FX6vqo9S96jPihv2ThK7c1mYrKWT4GjVqwWKEyCRK8vFqTjz2e5yh8GAU5hmHF4QzerY3sRYRiFwrxP2uApijpbKneumd3ToijiAtWuD68",
      |            "assets": [
      |                {
      |                    "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
      |                    "index": 0,
      |                    "amount": 18555857,
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
      |            "boxId": "5c2823136e07727f2604962566dae8bd7e5e4d881c7da1a6419040faa043142c",
      |            "transactionId": "883d7a8e5abd00779a098d3e191306e486f854c7de6b6a3bdbb071efb2bddbeb",
      |            "blockId": "1b3832dafc313efaaa3dc12b6f2ee97ab8ad8a0fa8056516ed067c5e17bbc375",
      |            "value": 4000000,
      |            "index": 0,
      |            "globalIndex": 6154469,
      |            "creationHeight": 551912,
      |            "settlementHeight": 551914,
      |            "ergoTree": "19a9030f040004020402040404040406040605feffffffffffffffff0105feffffffffffffffff01050004d00f0400040005000500d81ad601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d609b27203730500d60ab27204730600d60b9973078c720602d60c999973088c720502720bd60d8c720802d60e998c720702720dd60f91720e7309d6108c720a02d6117e721006d6127e720e06d613998c7209027210d6147e720d06d615730ad6167e721306d6177e720c06d6187e720b06d6199c72127218d61a9c72167218d1edededededed93c27201c2a793e4c672010404720292c17201c1a793b27203730b00b27204730c00938c7205018c720601ed938c7207018c720801938c7209018c720a019593720c730d95720f929c9c721172127e7202069c7ef07213069a9c72147e7215067e9c720e7e72020506929c9c721472167e7202069c7ef0720e069a9c72117e7215067e9c72137e7202050695ed720f917213730e907217a19d721972149d721a7211ed9272199c7217721492721a9c72177211",
      |            "address": "3gb1RZucekcRdda82TSNS4FZSREhGLoi1FxGDmMZdVeLtYYixPRviEdYireoM9RqC6Jf4kx85Y1jmUg5XzGgqdjpkhHm7kJZdgUR3VBwuLZuyHVqdSNv3eanqpknYsXtUwvUA16HFwNa3HgVRAnGC8zj8U7kksrfjycAM1yb19BB4TYR2BKWN7mpvoeoTuAKcAFH26cM46CEYsDRDn832wVNTLAmzz4Q6FqE29H9euwYzKiebgxQbWUxtupvfSbKaHpQcZAo5Dhyc6PFPyGVFZVRGZZ4Kftgi1NMRnGwKG7NTtXsFMsJP6A7yvLy8UZaMPe69BUAkpbSJdcWem3WpPUE7UpXv4itDkS5KVVaFtVyfx8PQxzi2eotP2uXtfairHuKinbpSFTSFKW3GxmXaw7vQs1JuVd8NhNShX6hxSqCP6sxojrqBxA48T2KcxNrmE3uFk7Pt4vPPdMAS4PW6UU82UD9rfhe3SMytK6DkjCocuRwuNqFoy4k25TXbGauTNgKuPKY3CxgkTpw9WfWsmtei178tLefhUEGJueueXSZo7negPYtmcYpoMhCuv4G1JZc283Q7f3mNXS",
      |            "assets": [
      |                {
      |                    "tokenId": "f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
      |                    "index": 1,
      |                    "amount": 9223372034021314444,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 2,
      |                    "amount": 5160631269748,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 3,
      |                    "amount": 9780330335882,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "04c80f",
      |                    "sigmaType": "SInt",
      |                    "renderedValue": "996"
      |                }
      |            },
      |            "spentTransactionId": "3ef3c7812a24f7d416f226cd43412cb984850c2f02572467e8c634910d62da5f",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "02a9a2872addc117a08b514e516d0d7265b837037902a8a25073bc7d6c98c74e",
      |            "transactionId": "883d7a8e5abd00779a098d3e191306e486f854c7de6b6a3bdbb071efb2bddbeb",
      |            "blockId": "1b3832dafc313efaaa3dc12b6f2ee97ab8ad8a0fa8056516ed067c5e17bbc375",
      |            "value": 10000000,
      |            "index": 1,
      |            "globalIndex": 6154470,
      |            "creationHeight": 551912,
      |            "settlementHeight": 551914,
      |            "ergoTree": "0008cd02c3f56e66191a903758f53a4b90d07cef80f93e7a4f17d106098ad0caf189722a",
      |            "address": "9g1N1xqhrNG1b2TkmFcQGTFZ47EquUYUZAiWWCBEbZaBcsMhXJU",
      |            "assets": [
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 0,
      |                    "amount": 33796097282,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 1,
      |                    "amount": 64049721480,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "a9851a458000d3599449a669f4b9c0bac408f450dfeff89fed7dad2e6780241c",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "f5765670c6844e97e293f877c539130a072c6296f79ca7b282e8677f95b17cc9",
      |            "transactionId": "883d7a8e5abd00779a098d3e191306e486f854c7de6b6a3bdbb071efb2bddbeb",
      |            "blockId": "1b3832dafc313efaaa3dc12b6f2ee97ab8ad8a0fa8056516ed067c5e17bbc375",
      |            "value": 9000000,
      |            "index": 2,
      |            "globalIndex": 6154471,
      |            "creationHeight": 551912,
      |            "settlementHeight": 551914,
      |            "ergoTree": "0008cd02ddbe95b7f88d47bd8c2db823cc5dd1be69a650556a44d4c15ac65e1d3e34324c",
      |            "address": "9gCigPc9cZNRhKgbgdmTkVxo1ZKgw79G8DvLjCcYWAvEF3XRUKy",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "e1cd45582d50b2ed75ca9ddb850d7cca66916807861e58967f5b7f545ba8a4e0",
      |            "transactionId": "883d7a8e5abd00779a098d3e191306e486f854c7de6b6a3bdbb071efb2bddbeb",
      |            "blockId": "1b3832dafc313efaaa3dc12b6f2ee97ab8ad8a0fa8056516ed067c5e17bbc375",
      |            "value": 1000000,
      |            "index": 3,
      |            "globalIndex": 6154472,
      |            "creationHeight": 551912,
      |            "settlementHeight": 551914,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "fa23a9cf4d35cf7a2e09a7c3040744d32eb54c3d2b59e4cf38b0fc3847d54854",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 884
      |}
      |""".stripMargin).toOption.get.toTransaction

  val transactionDeposit = decode[TransactionTest]("""
      |{
      |    "id": "db97e4753460b1a6b1a8144df49c320320a7f9d7ea0943a9e9cfdc11de548b7e",
      |    "blockId": "aceb49be7a7015c59d724d6897d2446a9c85dbab2efbb24d30f98ba21bfcf47d",
      |    "inclusionHeight": 627905,
      |    "timestamp": 1637870111006,
      |    "index": 1,
      |    "globalIndex": 2174188,
      |    "numConfirmations": 268105,
      |    "inputs": [
      |        {
      |            "boxId": "4e8fc829a6656f1e880f351e61a966412c1dbe970358daacb49a8cd4137e7bfe",
      |            "value": 18665472430147,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "de9f45e24aeb992373d01fb6967e468e5e38cad0f65f35db42e2a3a970f205c1",
      |            "outputTransactionId": "4adc7bb736efaeb62d11b8bb9a6928382700da4497a8c0207dacdb93ef80581c",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 10079426,
      |            "outputCreatedAt": 627889,
      |            "outputSettledAt": 627891,
      |            "ergoTree": "1999030f0400040204020404040405feffffffffffffffff0105feffffffffffffffff01050004d00f040004000406050005000580dac409d819d601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d6099973058c720602d60a999973068c7205027209d60bc17201d60cc1a7d60d99720b720cd60e91720d7307d60f8c720802d6107e720f06d6117e720d06d612998c720702720fd6137e720c06d6147308d6157e721206d6167e720a06d6177e720906d6189c72117217d6199c72157217d1ededededededed93c27201c2a793e4c672010404720293b27203730900b27204730a00938c7205018c720601938c7207018c72080193b17203730b9593720a730c95720e929c9c721072117e7202069c7ef07212069a9c72137e7214067e9c720d7e72020506929c9c721372157e7202069c7ef0720d069a9c72107e7214067e9c72127e7202050695ed720e917212730d907216a19d721872139d72197210ed9272189c721672139272199c7216721091720b730e",
      |            "address": "5vSUZRZbdVbnk4sJWjg2uhL94VZWRg4iatK9VgMChufzUgdihgvhR8yWSUEJKszzV7Vmi6K8hCyKTNhUaiP8p5ko6YEU9yfHpjVuXdQ4i5p4cRCzch6ZiqWrNukYjv7Vs5jvBwqg5hcEJ8u1eerr537YLWUoxxi1M4vQxuaCihzPKMt8NDXP4WcbN6mfNxxLZeGBvsHVvVmina5THaECosCWozKJFBnscjhpr3AJsdaL8evXAvPfEjGhVMoTKXAb2ZGGRmR8g1eZshaHmgTg2imSiaoXU5eiF3HvBnDuawaCtt674ikZ3oZdekqswcVPGMwqqUKVsGY4QuFeQoGwRkMqEYTdV2UDMMsfrjrBYQYKUBFMwsQGMNBL1VoY78aotXzdeqJCBVKbQdD3ZZWvukhSe4xrz8tcF3PoxpysDLt89boMqZJtGEHTV9UBTBEac6sDyQP693qT3nKaErN8TCXrJBUmHPqKozAg9bwxTqMYkpmb9iVKLSoJxG7MjAj72SRbcqQfNCVTztSwN3cRxSrVtz4p87jNFbVtFzhPg7UqDwNFTaasySCqM",
      |            "assets": [
      |                {
      |                    "tokenId": "9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 1,
      |                    "amount": 9223372021509593818,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 2,
      |                    "amount": 14586017,
      |                    "name": "SigUSD",
      |                    "decimals": 2,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "04c60f",
      |                    "sigmaType": "SInt",
      |                    "renderedValue": "995"
      |                }
      |            }
      |        },
      |        {
      |            "boxId": "8f02592ab29f6fa23521945cb4a6be442b7d82de4f7d44bdd81af3e60ebee0d6",
      |            "value": 4391252651,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "d06548a423ef41aef7b4a80f64552c1d383c4083845b4e617823efce43ae2653",
      |            "outputTransactionId": "2bf9834c6946d30dee3620c1df7d8c660ecb8b8e85e0e2c4d6ddedf7da81051e",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 10079873,
      |            "outputCreatedAt": 506880,
      |            "outputSettledAt": 627904,
      |            "ergoTree": "19a3031408cd03ac924c5ce32ee142e2506df85b06075ceb43fc235c665c4f4b354bfb997f918204000596e28bd520040404060402040205feffffffffffffffff0104040400040004000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec040404020580bfd6060596e28bd5200580bfd60601000100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d80bd604db63087202d605b2a5730500d606b27204730600d6077e9973078c72060206d6087ec1720206d6099d9c7e72030672077208d60ab27204730800d60b7e8c720a0206d60c9d9c7e8cb2db6308a773090002067207720bd60ddb63087205d60eb2720d730a00edededed938cb27204730b0001730c93c27205d0720195ed8f7209720c93b1720d730dd801d60fb2720d730e00eded92c172059999c1a7730f7310938c720f018c720a01927e8c720f02069d9c99720c7209720b720795927209720c927ec1720506997e99c1a7731106997e7203069d9c997209720c720872077312938c720e018c720601927e8c720e0206a17209720c7313",
      |            "address": "2DnYatBTAZF8xuhFwaPMU5p1JhwvCqrXnHGFcyvoyhckwtwwRRSTgD6UKBg44SmR3YXvmJXkaxkaWX4tUataHZzZUsAAjqFqe8P5u3gWNnen5xdHpWqydcRqF5XrMTHsXscXMJFSToy5VD1WpTvadHR9AjFNeZgHFJjU7qiYJ8vFzYcwryS4u7aRtFV4CnMRFg82tAy1Xj7iM6TQcwJMkng6hXTYU6gS3xh3jzJkWeZBkNLzLJeZRpPSBuERN4MvPWzFd6BTJ4w19u2ZibH6DSj4w3upF3o8eHY4DLADyEpxpNyXp9WFNQpxzc66btDWBUiH4D8HkD28KNRH4sVJSvYJf7stTY5KiasVcwz948wBG1mV5hXRJDK5yhLnTpEsWwf4KpPXAJhyoebRuFvduRXCihA8s7mJ2iwEYz2X3pAfT5fWjZ3Z3n2msv3rLTMRbrLeUj6FVCpKH8gTVGqQ8o3BAr8Xjo5gwGay1cWMZToRgiyLu6ZzxMAsGNxc5uV6XLfRy81c1fAwjBDbwUfTwAQMd8EZQLptjBChKiL2QcsFXnQsKSyd7zqfrdTcqGV6FjZwNwx",
      |            "assets": [
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 0,
      |                    "amount": 3426,
      |                    "name": "SigUSD",
      |                    "decimals": 2,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        }
      |    ],
      |    "dataInputs": [],
      |    "outputs": [
      |        {
      |            "boxId": "231c8a17b643e82fe6cdae4c6c97452e52c6b113981a087b0c16dd222dee3a08",
      |            "transactionId": "db97e4753460b1a6b1a8144df49c320320a7f9d7ea0943a9e9cfdc11de548b7e",
      |            "blockId": "aceb49be7a7015c59d724d6897d2446a9c85dbab2efbb24d30f98ba21bfcf47d",
      |            "value": 18669856622798,
      |            "index": 0,
      |            "globalIndex": 10079880,
      |            "creationHeight": 627903,
      |            "settlementHeight": 627905,
      |            "ergoTree": "1999030f0400040204020404040405feffffffffffffffff0105feffffffffffffffff01050004d00f040004000406050005000580dac409d819d601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d6099973058c720602d60a999973068c7205027209d60bc17201d60cc1a7d60d99720b720cd60e91720d7307d60f8c720802d6107e720f06d6117e720d06d612998c720702720fd6137e720c06d6147308d6157e721206d6167e720a06d6177e720906d6189c72117217d6199c72157217d1ededededededed93c27201c2a793e4c672010404720293b27203730900b27204730a00938c7205018c720601938c7207018c72080193b17203730b9593720a730c95720e929c9c721072117e7202069c7ef07212069a9c72137e7214067e9c720d7e72020506929c9c721372157e7202069c7ef0720d069a9c72107e7214067e9c72127e7202050695ed720e917212730d907216a19d721872139d72197210ed9272189c721672139272199c7216721091720b730e",
      |            "address": "5vSUZRZbdVbnk4sJWjg2uhL94VZWRg4iatK9VgMChufzUgdihgvhR8yWSUEJKszzV7Vmi6K8hCyKTNhUaiP8p5ko6YEU9yfHpjVuXdQ4i5p4cRCzch6ZiqWrNukYjv7Vs5jvBwqg5hcEJ8u1eerr537YLWUoxxi1M4vQxuaCihzPKMt8NDXP4WcbN6mfNxxLZeGBvsHVvVmina5THaECosCWozKJFBnscjhpr3AJsdaL8evXAvPfEjGhVMoTKXAb2ZGGRmR8g1eZshaHmgTg2imSiaoXU5eiF3HvBnDuawaCtt674ikZ3oZdekqswcVPGMwqqUKVsGY4QuFeQoGwRkMqEYTdV2UDMMsfrjrBYQYKUBFMwsQGMNBL1VoY78aotXzdeqJCBVKbQdD3ZZWvukhSe4xrz8tcF3PoxpysDLt89boMqZJtGEHTV9UBTBEac6sDyQP693qT3nKaErN8TCXrJBUmHPqKozAg9bwxTqMYkpmb9iVKLSoJxG7MjAj72SRbcqQfNCVTztSwN3cRxSrVtz4p87jNFbVtFzhPg7UqDwNFTaasySCqM",
      |            "assets": [
      |                {
      |                    "tokenId": "9916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 1,
      |                    "amount": 9223372021505989504,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 2,
      |                    "amount": 14589443,
      |                    "name": "SigUSD",
      |                    "decimals": 2,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "04c60f",
      |                    "sigmaType": "SInt",
      |                    "renderedValue": "995"
      |                }
      |            },
      |            "spentTransactionId": "6c8b36b77e67b7e0ef839c4f0105ba470f458b33deb3dd72707da0e4fb1d364d",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "a87d821913840ba29f51ce31d946231a9d3eb990c2680d166a0152d4623b8d9f",
      |            "transactionId": "db97e4753460b1a6b1a8144df49c320320a7f9d7ea0943a9e9cfdc11de548b7e",
      |            "blockId": "aceb49be7a7015c59d724d6897d2446a9c85dbab2efbb24d30f98ba21bfcf47d",
      |            "value": 60000,
      |            "index": 1,
      |            "globalIndex": 10079881,
      |            "creationHeight": 627903,
      |            "settlementHeight": 627905,
      |            "ergoTree": "0008cd03ac924c5ce32ee142e2506df85b06075ceb43fc235c665c4f4b354bfb997f9182",
      |            "address": "9hmooFTpv2njkLAbscLFdkyB18vc3fevH57ELFL2upYwWdE87PB",
      |            "assets": [
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 0,
      |                    "amount": 3604314,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "234669141789a2efa08f2bc18174e3abc4bad280bc91878bda0c30b3597ac80e",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "931700ed1334742813d5e1d75bc961cd58151acf2eae5822714e1eb4bcf8d7fd",
      |            "transactionId": "db97e4753460b1a6b1a8144df49c320320a7f9d7ea0943a9e9cfdc11de548b7e",
      |            "blockId": "aceb49be7a7015c59d724d6897d2446a9c85dbab2efbb24d30f98ba21bfcf47d",
      |            "value": 6000000,
      |            "index": 2,
      |            "globalIndex": 10079882,
      |            "creationHeight": 627903,
      |            "settlementHeight": 627905,
      |            "ergoTree": "0008cd0371f9794cee36ca9e9af0f0058f2c70beaa7312bbbc6312b60cb754c9ddc570f8",
      |            "address": "9hL11xHiZktWNpd2PHWSFadQrUifUTPR69bUbf5AwAggQJvdjDf",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "dcef0648827da4ae7dd69adea6fd635dd62c290217faf74da569ba14a0218421",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "55b0cb9dac281b1f929f5a264badc4363142135ba5c8800a5e4c66880f332582",
      |            "transactionId": "db97e4753460b1a6b1a8144df49c320320a7f9d7ea0943a9e9cfdc11de548b7e",
      |            "blockId": "aceb49be7a7015c59d724d6897d2446a9c85dbab2efbb24d30f98ba21bfcf47d",
      |            "value": 1000000,
      |            "index": 3,
      |            "globalIndex": 10079883,
      |            "creationHeight": 627903,
      |            "settlementHeight": 627905,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "059bca24ebf41da8a5f13ff3a17bce0d879cf4a805fb935c2ce38d5d6a433d8f",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 819
      |}
      |""".stripMargin).toOption.get.toTransaction

  val transactionSwapRegisterToRefund = decode[TransactionTest](
    """
      |{
      |    "id": "6a58d9014f9503c5c62f4696bcf3498e73639498fb9c6948bdf29d5ef2f161f7",
      |    "blockId": "531fdfa8d038a201f6e851bb37c1ce53f54401e8451042e28f43dd0f1746caa7",
      |    "inclusionHeight": 645798,
      |    "timestamp": 1640011665563,
      |    "index": 7,
      |    "globalIndex": 2331955,
      |    "numConfirmations": 251769,
      |    "inputs": [
      |        {
      |            "boxId": "1c002a69770ba70ec9ea063ae0673643f158182bc5ac0c3b1e7f1046b6cbec4c",
      |            "value": 1586359606,
      |            "index": 0,
      |            "spendingProof": "4271fcef36824cfb5346ed6c9b8b91ff991cc868d3387728797545cd98e8ba8ebea1cc89313f2ea4488a544ad889de8b12e9659e4e428989",
      |            "outputBlockId": "1a3a47a47de4fa87345b68447b7d13c0081ed2691d0c3de5426a26e6ab15eb9f",
      |            "outputTransactionId": "416e473fca9b4e1f095f42e3566e8aeb519b5543516c2839be1337ef3b727644",
      |            "outputIndex": 2,
      |            "outputGlobalIndex": 11110919,
      |            "outputCreatedAt": 508928,
      |            "outputSettledAt": 645774,
      |            "ergoTree": "0008cd03e8196967038a183915bd79c249385904a9264cf81183099d80254e6c0166d3a6",
      |            "address": "9iE2MadGSrn1ivHmRZJWRxzHffuAk6bPmEv6uJmPHuadBY8td5u",
      |            "assets": [
      |                {
      |                    "tokenId": "5a34d53ca483924b9a6aa0c771f11888881b516a8d1a9cdc535d063fe26d065e",
      |                    "index": 0,
      |                    "amount": 46155906644,
      |                    "name": "LunaDog",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 1,
      |                    "amount": 3172,
      |                    "name": "SigUSD",
      |                    "decimals": 2,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 2,
      |                    "amount": 177976064,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        },
      |        {
      |            "boxId": "de30867c784a0294793a58d319d81467770251a5c39b7e772c6e106f8a7c3336",
      |            "value": 150006460000,
      |            "index": 1,
      |            "spendingProof": "de599429f27a3e9cb5de636b515d7b049f21e185d8c1c4074b5099a8898dfae6f90e7e20829078a1502ebaac10eb51dfc82fb99da8128bd5",
      |            "outputBlockId": "a0d314b313022e497720b6ba0665505a77bbb412f110637603b1ec6181051f71",
      |            "outputTransactionId": "6b362d95dab6729663a672e37cb3ab54e1fbb49737182b4eb2aa5e545f799290",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 11111026,
      |            "outputCreatedAt": 508928,
      |            "outputSettledAt": 645781,
      |            "ergoTree": "0008cd03e8196967038a183915bd79c249385904a9264cf81183099d80254e6c0166d3a6",
      |            "address": "9iE2MadGSrn1ivHmRZJWRxzHffuAk6bPmEv6uJmPHuadBY8td5u",
      |            "assets": [],
      |            "additionalRegisters": {}
      |        }
      |    ],
      |    "dataInputs": [],
      |    "outputs": [
      |        {
      |            "boxId": "00919b633481e67da3197395972a5682ff6b027acc38aa6989be7acc4f136008",
      |            "transactionId": "6a58d9014f9503c5c62f4696bcf3498e73639498fb9c6948bdf29d5ef2f161f7",
      |            "blockId": "531fdfa8d038a201f6e851bb37c1ce53f54401e8451042e28f43dd0f1746caa7",
      |            "value": 150008460000,
      |            "index": 0,
      |            "globalIndex": 11111906,
      |            "creationHeight": 508928,
      |            "settlementHeight": 645798,
      |            "ergoTree": "19fc031808cd03e8196967038a183915bd79c249385904a9264cf81183099d80254e6c0166d3a604000580f092cbdd08040404060402040004000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec0e2003faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf0405b8aa0905d4828cf5a591c420058080d287e2bc2d040404c60f06010104d00f0580f092cbdd0804c60f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d804d604db63087202d605b2a5730500d606b2db63087205730600d6077e8c72060206edededededed938cb2720473070001730893c27205d07201938c72060173099272077e730a06927ec172050699997ec1a7069d9c72077e730b067e730c067e720306909c9c7e8cb27204730d0002067e7203067e730e069c9a7207730f9a9c7ec17202067e7310067e9c73117e7312050690b0ada5d90108639593c272087313c1720873147315d90108599a8c7208018c72080273167317",
      |            "address": "C6jFLuMPnPVzxH2GbtARRRiMjgzmDxBTCVLmD4jymARR5QwSRB8REdwNrKYc7diKUhsgzdb271tLjPM4Cmd92PN2rff5Wmf9KJybemkZghbnehfzmdPn98XLib24JsP29mSwTncDpKhtmKq3xqVZ1qtHqHQQGLrTuSn9H54kYMXKQBPgDoVx85BVcFbPeVN9Um7So28vUu6jph6aTa8EsFGcq4R25F2tp64vvqf2n7yvkge7NohrxVqXwJwJF1S1XqXoFSh6GusXd4kF7JaWbqWSwsha9rJWcG88nmvmTPr7Mscny8rmYXz65YAfmjWYLu8SjTz6E5E9xmv2VkEJoj7oAYC8GFcSZAJ8vg9eqXCBRcoWfVkdokvBgCHZDkxaxaJbGF7wUrUzPpnQUi9J9mx2hge1Jbic17Yj4KTJyNtKvaFpm8MHhuyuMydSxAz887fLxmCvQVFpMrEWRkrBayeTf9H6FeGtajsi6VmVQfFqwXrbAnmhaxCfyGZsQjGPTvbauUaiRiNRriE4RJE2VsQMKzx1PL8QLec279xT96ZXdLtkaswVYuU5eHGDkgsTZk1rQFUn8wY9cvCPCCYwPMtjWH8R28u8xAkBnmYAqof9iXkvC9kP2rC46zdNfdcdg6vshcrN5tRVP6Nq7b7VRLYzVzzZTpNVMC9pJw8xkFma73fnxuyvsM8EsoRG3bma",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "3172e174539be12500d038bfc0afa172cc80eb2a2a6563a7eeed9561be0e9cc7",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "40ddd0542673a1ff7ac404e2872ee02e5a792ad353c814439fb59d11d81afe4a",
      |            "transactionId": "6a58d9014f9503c5c62f4696bcf3498e73639498fb9c6948bdf29d5ef2f161f7",
      |            "blockId": "531fdfa8d038a201f6e851bb37c1ce53f54401e8451042e28f43dd0f1746caa7",
      |            "value": 10000000,
      |            "index": 1,
      |            "globalIndex": 11111907,
      |            "creationHeight": 508928,
      |            "settlementHeight": 645798,
      |            "ergoTree": "0008cd02ddbe95b7f88d47bd8c2db823cc5dd1be69a650556a44d4c15ac65e1d3e34324c",
      |            "address": "9gCigPc9cZNRhKgbgdmTkVxo1ZKgw79G8DvLjCcYWAvEF3XRUKy",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "f7d06f1d53fd5a4ea369d36694c3b3f5dbabcc000d5290748d21df7da3e580eb",
      |            "transactionId": "6a58d9014f9503c5c62f4696bcf3498e73639498fb9c6948bdf29d5ef2f161f7",
      |            "blockId": "531fdfa8d038a201f6e851bb37c1ce53f54401e8451042e28f43dd0f1746caa7",
      |            "value": 1572359606,
      |            "index": 2,
      |            "globalIndex": 11111908,
      |            "creationHeight": 508928,
      |            "settlementHeight": 645798,
      |            "ergoTree": "0008cd03e8196967038a183915bd79c249385904a9264cf81183099d80254e6c0166d3a6",
      |            "address": "9iE2MadGSrn1ivHmRZJWRxzHffuAk6bPmEv6uJmPHuadBY8td5u",
      |            "assets": [
      |                {
      |                    "tokenId": "5a34d53ca483924b9a6aa0c771f11888881b516a8d1a9cdc535d063fe26d065e",
      |                    "index": 0,
      |                    "amount": 46155906644,
      |                    "name": "LunaDog",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 1,
      |                    "amount": 3172,
      |                    "name": "SigUSD",
      |                    "decimals": 2,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 2,
      |                    "amount": 177976064,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "6ca2e27b21904f6d7144a02a48c1d44015f917bc2124d9c3afc1234bf19513a2",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "0410e486922ae1209787e15af6ba18c266a3db48bdac9fead02afa8f3699f293",
      |            "transactionId": "6a58d9014f9503c5c62f4696bcf3498e73639498fb9c6948bdf29d5ef2f161f7",
      |            "blockId": "531fdfa8d038a201f6e851bb37c1ce53f54401e8451042e28f43dd0f1746caa7",
      |            "value": 2000000,
      |            "index": 3,
      |            "globalIndex": 11111909,
      |            "creationHeight": 508928,
      |            "settlementHeight": 645798,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "5dbbe0c788cfd4269b767abc03170d601233139524b47722e375ac9a5e8132b9",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 1021
      |}
      |""".stripMargin).toOption.get.toTransaction

  val transactionSwapRefundRegister = decode[TransactionTest](
    """
      |{
      |    "id": "3172e174539be12500d038bfc0afa172cc80eb2a2a6563a7eeed9561be0e9cc7",
      |    "blockId": "46475e2ead547a3c9101ecff2468cecc2e9c5488684b6f74bc64917b62036511",
      |    "inclusionHeight": 645803,
      |    "timestamp": 1640012191087,
      |    "index": 4,
      |    "globalIndex": 2331995,
      |    "numConfirmations": 251764,
      |    "inputs": [
      |        {
      |            "boxId": "00919b633481e67da3197395972a5682ff6b027acc38aa6989be7acc4f136008",
      |            "value": 150008460000,
      |            "index": 0,
      |            "spendingProof": "6735e90c876ac47a7ad61e81cb4c424b3b31a31cba73596e5a3a409343b82815cffc4cb3c3f086b2b35a441b4507d58f31b29d6b042cb292",
      |            "outputBlockId": "531fdfa8d038a201f6e851bb37c1ce53f54401e8451042e28f43dd0f1746caa7",
      |            "outputTransactionId": "6a58d9014f9503c5c62f4696bcf3498e73639498fb9c6948bdf29d5ef2f161f7",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 11111906,
      |            "outputCreatedAt": 508928,
      |            "outputSettledAt": 645798,
      |            "ergoTree": "19fc031808cd03e8196967038a183915bd79c249385904a9264cf81183099d80254e6c0166d3a604000580f092cbdd08040404060402040004000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec0e2003faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf0405b8aa0905d4828cf5a591c420058080d287e2bc2d040404c60f06010104d00f0580f092cbdd0804c60f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d804d604db63087202d605b2a5730500d606b2db63087205730600d6077e8c72060206edededededed938cb2720473070001730893c27205d07201938c72060173099272077e730a06927ec172050699997ec1a7069d9c72077e730b067e730c067e720306909c9c7e8cb27204730d0002067e7203067e730e069c9a7207730f9a9c7ec17202067e7310067e9c73117e7312050690b0ada5d90108639593c272087313c1720873147315d90108599a8c7208018c72080273167317",
      |            "address": "C6jFLuMPnPVzxH2GbtARRRiMjgzmDxBTCVLmD4jymARR5QwSRB8REdwNrKYc7diKUhsgzdb271tLjPM4Cmd92PN2rff5Wmf9KJybemkZghbnehfzmdPn98XLib24JsP29mSwTncDpKhtmKq3xqVZ1qtHqHQQGLrTuSn9H54kYMXKQBPgDoVx85BVcFbPeVN9Um7So28vUu6jph6aTa8EsFGcq4R25F2tp64vvqf2n7yvkge7NohrxVqXwJwJF1S1XqXoFSh6GusXd4kF7JaWbqWSwsha9rJWcG88nmvmTPr7Mscny8rmYXz65YAfmjWYLu8SjTz6E5E9xmv2VkEJoj7oAYC8GFcSZAJ8vg9eqXCBRcoWfVkdokvBgCHZDkxaxaJbGF7wUrUzPpnQUi9J9mx2hge1Jbic17Yj4KTJyNtKvaFpm8MHhuyuMydSxAz887fLxmCvQVFpMrEWRkrBayeTf9H6FeGtajsi6VmVQfFqwXrbAnmhaxCfyGZsQjGPTvbauUaiRiNRriE4RJE2VsQMKzx1PL8QLec279xT96ZXdLtkaswVYuU5eHGDkgsTZk1rQFUn8wY9cvCPCCYwPMtjWH8R28u8xAkBnmYAqof9iXkvC9kP2rC46zdNfdcdg6vshcrN5tRVP6Nq7b7VRLYzVzzZTpNVMC9pJw8xkFma73fnxuyvsM8EsoRG3bma",
      |            "assets": [],
      |            "additionalRegisters": {}
      |        }
      |    ],
      |    "dataInputs": [],
      |    "outputs": [
      |        {
      |            "boxId": "3f1e39e2dadf7c5c71645aea9baabfc2eba28bfbdaac089f8fe813dd59c93bef",
      |            "transactionId": "3172e174539be12500d038bfc0afa172cc80eb2a2a6563a7eeed9561be0e9cc7",
      |            "blockId": "46475e2ead547a3c9101ecff2468cecc2e9c5488684b6f74bc64917b62036511",
      |            "value": 150006460000,
      |            "index": 0,
      |            "globalIndex": 11112026,
      |            "creationHeight": 508928,
      |            "settlementHeight": 645803,
      |            "ergoTree": "0008cd03e8196967038a183915bd79c249385904a9264cf81183099d80254e6c0166d3a6",
      |            "address": "9iE2MadGSrn1ivHmRZJWRxzHffuAk6bPmEv6uJmPHuadBY8td5u",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "6ca2e27b21904f6d7144a02a48c1d44015f917bc2124d9c3afc1234bf19513a2",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "1becfdeacf9825b3bdc36a130ab3518f1d053512e0f2900e5f8007401e75221e",
      |            "transactionId": "3172e174539be12500d038bfc0afa172cc80eb2a2a6563a7eeed9561be0e9cc7",
      |            "blockId": "46475e2ead547a3c9101ecff2468cecc2e9c5488684b6f74bc64917b62036511",
      |            "value": 2000000,
      |            "index": 1,
      |            "globalIndex": 11112027,
      |            "creationHeight": 508928,
      |            "settlementHeight": 645803,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "abc5c8a67fc5d80fd120ad016d543ca994d9ff640415aae8bd58c4a7bae0e325",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 254
      |}
      |""".stripMargin).toOption.get.toTransaction

}
