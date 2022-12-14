package fi.spectrum.parser.evaluation

import fi.spectrum.core.domain.analytics.OffChainOperatorFee
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.{OrderId, PoolId}
import fi.spectrum.core.domain.{BoxId, PubKey}
import fi.spectrum.parser.models.TransactionTest
import io.circe.parser.decode

object OffChainFee {

  val transaction = decode[TransactionTest](
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

  val expectedFee = OffChainOperatorFee(
    PoolId.unsafeFromString("f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781"),
    OrderId("b7510c7cd1ff26bd61aec41a6ecb19fbbbcc38900f4451af3fcc127d424a24eb"),
    BoxId("096252e7c50b88403477849193d87966def51e3e38d71a3007b1578218b94db9"),
    PubKey.unsafeFromString("02ddbe95b7f88d47bd8c2db823cc5dd1be69a650556a44d4c15ac65e1d3e34324c"),
    ERG(4004999)
  )

}
