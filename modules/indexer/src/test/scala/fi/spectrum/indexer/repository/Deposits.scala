package fi.spectrum.indexer.repository

import fi.spectrum.parser.models.TransactionTest
import io.circe.parser.decode

object Deposits {

  val refund = decode[TransactionTest](
    """
      |{
      |    "id": "cc4f87759fc058fa6d5ad4a2e5f2b82a85bb808fb18e9df9630cb23534e6e475",
      |    "blockId": "fca974d2e93fce1b53993c0f550952d7fcddf626fc51364ff1773912e68e817d",
      |    "inclusionHeight": 557141,
      |    "timestamp": 1629285652097,
      |    "index": 1,
      |    "globalIndex": 1645030,
      |    "numConfirmations": 341733,
      |    "inputs": [
      |        {
      |            "boxId": "0007142a2e9da285f092c1cb9befd4a90b30c59a6fd21667b4baa590827d5ca9",
      |            "value": 20000000,
      |            "index": 0,
      |            "spendingProof": "b968880ee2451bbee44659539ec82d5c5321744a1333911f30eeb7b8c9e44384536a1bedb55e5e3709415be49e38cc58e187f5b441847015",
      |            "outputBlockId": "1bfdd77b1a22288cfe3f34ec986e9471f48a91057cbdcd2973cb3751d8e6aa0b",
      |            "outputTransactionId": "f3a9bcb8809e71ce66bfd76bd685ddb5cb87da27d61781903e9a6bb9db5138a7",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 6362971,
      |            "outputCreatedAt": 508928,
      |            "outputSettledAt": 557136,
      |            "ergoTree": "19aa021108cd03d3ac61e16feececcf448845091319c842ea83198d89ac37698f90a36644e717004000404040804020400040205feffffffffffffffff0104000e20f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc78105020580dac40904000404040204060100d803d6017300d602b2a4730100d603db6308a7eb027201d195ed93b1a4730293b1db630872027303d805d604db63087202d605b2a5730400d606b2db63087205730500d607b27204730600d6087e9973078c72070206edededed93b2720473080086027309730a93c27205d0720192c1720599c1a7730b938c7206018c720701927e8c72060206a19d9c7e8cb27203730c00020672087e8cb27204730d0002069d9c7e8cb27203730e00020672087e8cb27204730f0002067310",
      |            "address": "TDSS8J9hjXBdTxGBnpwwZzXXeLAMomXrokvQWL7CYDxxcWv2qZFrsJQP6B6g9Uzp1gNCYCqQt6AXHuqfooxJk3J2S65KKhwFp4795fw7einN2tqHsJ8Fi3UYMM4QCKm9eRbFCk9dyyYRr4gcbeAu4i4kaqAC1HFMEeRtF4ynVN2Cq8E7tVrNmK7eRktrfJNf3K13FjUV5DvecvzPzpF8PLcUaDKSKgUTpz6H3yURAgKuXbs11nqcUqPtkao4NcUApadtVWHws3gqake7FoW3BTpMawpz1K32dQcZUpJ25xJXWqvD2gmMdnQGrrPrhNu4Ui4zzwwTJyPG87VhGPG3hCWVosD9jnPSZStLxdSHyq4qt71VQB9qm5jwRWQTqwQLeoYyhzJxddrWvXAJxxrGytXS9NKUq5frC",
      |            "assets": [
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 0,
      |                    "amount": 10000000,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 1,
      |                    "amount": 16757608,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        }
      |    ],
      |    "dataInputs": [],
      |    "outputs": [
      |        {
      |            "boxId": "5fd881f4116f26ef2976d85e782c1b55fd8d9bd88c4422fcd55060444a8dec9b",
      |            "transactionId": "cc4f87759fc058fa6d5ad4a2e5f2b82a85bb808fb18e9df9630cb23534e6e475",
      |            "blockId": "fca974d2e93fce1b53993c0f550952d7fcddf626fc51364ff1773912e68e817d",
      |            "value": 10000000,
      |            "index": 0,
      |            "globalIndex": 6363170,
      |            "creationHeight": 506880,
      |            "settlementHeight": 557141,
      |            "ergoTree": "0008cd03d3ac61e16feececcf448845091319c842ea83198d89ac37698f90a36644e7170",
      |            "address": "9i52bxZdsnqLKhMqHNCc38nsFEdj6aVHdXcHFwKZ58AYvtWmNvg",
      |            "assets": [
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 0,
      |                    "amount": 10000000,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 1,
      |                    "amount": 16757608,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "99af9aa1eec2472571824a80bb4899d42d76aa6ca17b94e0e497f97ee13d8b4a",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "5a679990bcc268735db285c4da147855cbccebb4e393702f4bdbdc0f8be51e92",
      |            "transactionId": "cc4f87759fc058fa6d5ad4a2e5f2b82a85bb808fb18e9df9630cb23534e6e475",
      |            "blockId": "fca974d2e93fce1b53993c0f550952d7fcddf626fc51364ff1773912e68e817d",
      |            "value": 10000000,
      |            "index": 1,
      |            "globalIndex": 6363171,
      |            "creationHeight": 506880,
      |            "settlementHeight": 557141,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "32dd63146b1889062d1e77dbff3729599ca80cacf3bbd00e8384995f3ab4b206",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 327
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  val depositsToRefund = decode[TransactionTest](
    """
      |{
      |    "id": "f3a9bcb8809e71ce66bfd76bd685ddb5cb87da27d61781903e9a6bb9db5138a7",
      |    "blockId": "1bfdd77b1a22288cfe3f34ec986e9471f48a91057cbdcd2973cb3751d8e6aa0b",
      |    "inclusionHeight": 557136,
      |    "timestamp": 1629285014359,
      |    "index": 1,
      |    "globalIndex": 1645005,
      |    "numConfirmations": 341738,
      |    "inputs": [
      |        {
      |            "boxId": "da83426f2092172f0fd11afa5c15ed276b5a34c9f57c0283f041c6857c1ab75d",
      |            "value": 4896100000,
      |            "index": 0,
      |            "spendingProof": "71d31734c73297f4d0c98697e3981c17f565358a5dc697a40aa1a809f8cdac4e7f126bb44073ebb4c3fa84520e4b60fe53b68176088d6848",
      |            "outputBlockId": "7fdae739d1878b671af5cb7b540e8c4275202eaf65b7cc88459b551047aa8a9c",
      |            "outputTransactionId": "b4460a8217610751819a3f5a804372909dbe29804872f31d7131883a99039860",
      |            "outputIndex": 1,
      |            "outputGlobalIndex": 6362918,
      |            "outputCreatedAt": 506880,
      |            "outputSettledAt": 557134,
      |            "ergoTree": "0008cd03d3ac61e16feececcf448845091319c842ea83198d89ac37698f90a36644e7170",
      |            "address": "9i52bxZdsnqLKhMqHNCc38nsFEdj6aVHdXcHFwKZ58AYvtWmNvg",
      |            "assets": [
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 0,
      |                    "amount": 1000000000000,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 1,
      |                    "amount": 9993850000000,
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
      |            "boxId": "0007142a2e9da285f092c1cb9befd4a90b30c59a6fd21667b4baa590827d5ca9",
      |            "transactionId": "f3a9bcb8809e71ce66bfd76bd685ddb5cb87da27d61781903e9a6bb9db5138a7",
      |            "blockId": "1bfdd77b1a22288cfe3f34ec986e9471f48a91057cbdcd2973cb3751d8e6aa0b",
      |            "value": 20000000,
      |            "index": 0,
      |            "globalIndex": 6362971,
      |            "creationHeight": 508928,
      |            "settlementHeight": 557136,
      |            "ergoTree": "19aa021108cd03d3ac61e16feececcf448845091319c842ea83198d89ac37698f90a36644e717004000404040804020400040205feffffffffffffffff0104000e20f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc78105020580dac40904000404040204060100d803d6017300d602b2a4730100d603db6308a7eb027201d195ed93b1a4730293b1db630872027303d805d604db63087202d605b2a5730400d606b2db63087205730500d607b27204730600d6087e9973078c72070206edededed93b2720473080086027309730a93c27205d0720192c1720599c1a7730b938c7206018c720701927e8c72060206a19d9c7e8cb27203730c00020672087e8cb27204730d0002069d9c7e8cb27203730e00020672087e8cb27204730f0002067310",
      |            "address": "TDSS8J9hjXBdTxGBnpwwZzXXeLAMomXrokvQWL7CYDxxcWv2qZFrsJQP6B6g9Uzp1gNCYCqQt6AXHuqfooxJk3J2S65KKhwFp4795fw7einN2tqHsJ8Fi3UYMM4QCKm9eRbFCk9dyyYRr4gcbeAu4i4kaqAC1HFMEeRtF4ynVN2Cq8E7tVrNmK7eRktrfJNf3K13FjUV5DvecvzPzpF8PLcUaDKSKgUTpz6H3yURAgKuXbs11nqcUqPtkao4NcUApadtVWHws3gqake7FoW3BTpMawpz1K32dQcZUpJ25xJXWqvD2gmMdnQGrrPrhNu4Ui4zzwwTJyPG87VhGPG3hCWVosD9jnPSZStLxdSHyq4qt71VQB9qm5jwRWQTqwQLeoYyhzJxddrWvXAJxxrGytXS9NKUq5frC",
      |            "assets": [
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 0,
      |                    "amount": 10000000,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 1,
      |                    "amount": 16757608,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "cc4f87759fc058fa6d5ad4a2e5f2b82a85bb808fb18e9df9630cb23534e6e475",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "8ade0e566e9e9fc74a0800159735d2fe0e886354b381bf1d2f3632b2b6c22371",
      |            "transactionId": "f3a9bcb8809e71ce66bfd76bd685ddb5cb87da27d61781903e9a6bb9db5138a7",
      |            "blockId": "1bfdd77b1a22288cfe3f34ec986e9471f48a91057cbdcd2973cb3751d8e6aa0b",
      |            "value": 4866100000,
      |            "index": 1,
      |            "globalIndex": 6362972,
      |            "creationHeight": 508928,
      |            "settlementHeight": 557136,
      |            "ergoTree": "0008cd03d3ac61e16feececcf448845091319c842ea83198d89ac37698f90a36644e7170",
      |            "address": "9i52bxZdsnqLKhMqHNCc38nsFEdj6aVHdXcHFwKZ58AYvtWmNvg",
      |            "assets": [
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 0,
      |                    "amount": 999983242392,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 1,
      |                    "amount": 9993840000000,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "716ccdc1fafd6871980b39ddd034b1cf65919d6a938b668602b449e5ec1b8891",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "0e463ca676001c2d1233cef1dcf02a7f23a96788043ae5cd4c2b51db5d68a3dc",
      |            "transactionId": "f3a9bcb8809e71ce66bfd76bd685ddb5cb87da27d61781903e9a6bb9db5138a7",
      |            "blockId": "1bfdd77b1a22288cfe3f34ec986e9471f48a91057cbdcd2973cb3751d8e6aa0b",
      |            "value": 10000000,
      |            "index": 2,
      |            "globalIndex": 6362973,
      |            "creationHeight": 508928,
      |            "settlementHeight": 557136,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "7c1af8f36944a14d1ba0f43fdffee27bf1db364228ebafb79390432286a867b4",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 653
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  val depositToExecute = decode[TransactionTest]("""
      |{
      |    "id": "f4f378be4978bc8b99f8feff7669d12f89ceb447535df3812af683853c6cbecb",
      |    "blockId": "8d7dc915bed488779ca54c8cef980344835f571466f119f5f2188cf87f1f0068",
      |    "inclusionHeight": 661647,
      |    "timestamp": 1641941427683,
      |    "index": 2,
      |    "globalIndex": 2433199,
      |    "numConfirmations": 237228,
      |    "inputs": [
      |        {
      |            "boxId": "ac7b82172671b135cab98d69acbe02011514e63ef79265456852546935221ccd",
      |            "value": 507417031,
      |            "index": 0,
      |            "spendingProof": "79532ec11db2fd8f23d62b5114625b29c9820316978bc5c653aa7c3282b9248bf1d3c7c6491fbe1b2d2325e682750374a45acd77ff2ebb81",
      |            "outputBlockId": "6b7e7bf3eb253a2aaab483698dd388c945a1ab667c97232efd9d3ef35a6a294d",
      |            "outputTransactionId": "f0b3ff78855e1bef8d10acbf364ccf94a3741fd55ee4ac74c84d6ac8ce4aee4e",
      |            "outputIndex": 1,
      |            "outputGlobalIndex": 11871681,
      |            "outputCreatedAt": 506880,
      |            "outputSettledAt": 661643,
      |            "ergoTree": "0008cd0295d0031c6b506aaeb1d146b21e1e19059a7a4392aceba9aaf045a85aebe9a21b",
      |            "address": "9ff3GvyDcswQnV6dfZnvJ8XJwyg1dqYngJKqoL1fVK74v8pD9Vi",
      |            "assets": [
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 0,
      |                    "amount": 6582157,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 1,
      |                    "amount": 400000000000,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 2,
      |                    "amount": 1000000000000,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "e91cbc48016eb390f8f872aa2962772863e2e840708517d1ab85e57451f91bed",
      |                    "index": 3,
      |                    "amount": 2400,
      |                    "name": "Ergold",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40",
      |                    "index": 4,
      |                    "amount": 2000,
      |                    "name": "kushti",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "151f9e9e0cb4aca29b8c8eaa765661c9f6cb4e018131b09bc63680e3e2585576",
      |                    "index": 5,
      |                    "amount": 100000,
      |                    "name": "Melange",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "5ed7b971363d38c3c0345bc257e47efe8bec8e4cc8767f390dbaefdf7e6a8f4e",
      |                    "index": 6,
      |                    "amount": 1,
      |                    "name": "Golden Ticket",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 7,
      |                    "amount": 1752543,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 8,
      |                    "amount": 935,
      |                    "name": "SigRSV",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |                    "index": 9,
      |                    "amount": 1000000000,
      |                    "name": "NETA",
      |                    "decimals": 6,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        },
      |        {
      |            "boxId": "06bfff72035054b991e628f2d8ac23f09bdfff0d81d44eac885ba98116cc65c1",
      |            "value": 965938833,
      |            "index": 1,
      |            "spendingProof": "a8c055dc4fdb758534d81297ffab0acc9f39b71d8a6c4c5da66cbacbf90fbaf49c89b05d8e5f0e049c3693dcefda1d657fcc3eedd237c648",
      |            "outputBlockId": "6b7e7bf3eb253a2aaab483698dd388c945a1ab667c97232efd9d3ef35a6a294d",
      |            "outputTransactionId": "5212c28ac8cd6620ee8b129600ec85d1a869cb58e55a4a2f60feb1927e239326",
      |            "outputIndex": 1,
      |            "outputGlobalIndex": 11871690,
      |            "outputCreatedAt": 661641,
      |            "outputSettledAt": 661643,
      |            "ergoTree": "0008cd0295d0031c6b506aaeb1d146b21e1e19059a7a4392aceba9aaf045a85aebe9a21b",
      |            "address": "9ff3GvyDcswQnV6dfZnvJ8XJwyg1dqYngJKqoL1fVK74v8pD9Vi",
      |            "assets": [
      |                {
      |                    "tokenId": "5a34d53ca483924b9a6aa0c771f11888881b516a8d1a9cdc535d063fe26d065e",
      |                    "index": 0,
      |                    "amount": 86338000386,
      |                    "name": "LunaDog",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        }
      |    ],
      |    "dataInputs": [],
      |    "outputs": [
      |        {
      |            "boxId": "0014655fd62c0668e3646c49833cd02df630959b7b1390d36d396e5cd1960636",
      |            "transactionId": "f4f378be4978bc8b99f8feff7669d12f89ceb447535df3812af683853c6cbecb",
      |            "blockId": "8d7dc915bed488779ca54c8cef980344835f571466f119f5f2188cf87f1f0068",
      |            "value": 745118814,
      |            "index": 0,
      |            "globalIndex": 11871821,
      |            "creationHeight": 506880,
      |            "settlementHeight": 661647,
      |            "ergoTree": "19bd041808cd0295d0031c6b506aaeb1d146b21e1e19059a7a4392aceba9aaf045a85aebe9a21b040005fc91e9c005040404060402040205feffffffffffffffff0104040400040004000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f040404020580b6dc0505fc91e9c0050580b6dc0501000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d80bd604db63087202d605b2a5730500d606b27204730600d6077e9973078c72060206d6087ec1720206d6099d9c7e72030672077208d60ab27204730800d60b7e8c720a0206d60c9d9c7e8cb2db6308a773090002067207720bd60ddb63087205d60eb2720d730a00ededededed938cb27204730b0001730c93c27205d0720195ed8f7209720c93b1720d730dd801d60fb2720d730e00eded92c172059999c1a7730f7310938c720f018c720a01927e8c720f02069d9c99720c7209720b720795927209720c927ec1720506997e99c1a7731106997e7203069d9c997209720c720872077312938c720e018c720601927e8c720e0206a17209720c90b0ada5d9010f639593c2720f7313c1720f73147315d9010f599a8c720f018c720f0273167317",
      |            "address": "5KevXenwFgBWY8DXPxvBn61PpLJj3T9uhx5h89T6bfvHscrJSoci5QMvYFVEUGvhXWRgUo5as8qxgxBYg53Ut7CrL8YyDnqndYVdfAsv5wgJPugxtxQuRf9cPy8jNtFErYaL2fiuTFm2Ctgsrh2xdwHn2cTW9WmV3XW6nTsUdmyTKUfhHQkseDGGxdSS8HH9F5A15XuEAZaFdze215DU5E8GiaYYktt6U2GR51A84qN6Egw8JX3TT7uYYLQcYghf5sxqPcP6qC4XXTkqpUfQGWDdABDxBRennJMye3QVGW5m2YBUNgLK333hSSyHBpJweodDta2mv11xMCXzon2eZZ3bT4GN8YHrKie95EVJ194KQkc32E53ngFmY4aGgkCYeqQLiFK4Qt3Tg7QXCbGmjx6cRiY6xyzLH7BrshXx7nUw1m1kQ6rd1KCRccappKgT9Hm8AoYBAksKXpr1A1yVcqP8W7wyFk1wshyW9Mij2YHufbm2A4fBkGsh6LJaTpafoVxNn4ezXQWocuQK6iZXe8CG5LTKKxshrxGMRu4ibU4L4tUBXL3sZQy5cvQ9HgcKY78ypDEZ5Hjcoq4goQG8rwvfdAkdSk7JaDXTYhJPeA1Nfob8RVyQSDoH7PQArUzJPBPf2F3JecGkf52WJCGDPi8oYQ2CTJDEb3pgVzdhLHt1NHqQNdb64RBTxe8SVPAwQo3aFw5ZsLnGk2hDHi9T6MtbKxzTy6SeddBKMbsLJ6VWDzgWVPbCJ48vk3rLNJXy8yGjFQhgW2seM4gp2ituAQ4wK",
      |            "assets": [
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 0,
      |                    "amount": 935,
      |                    "name": "SigRSV",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "325f0dc5c094afe51a8324c0652476417c7e6f088e9261b9477fe23727da1fc2",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "479ade18ba39ef1cba47ec1361e3b1ad11611ee29b56aea28ca034690cdee8a7",
      |            "transactionId": "f4f378be4978bc8b99f8feff7669d12f89ceb447535df3812af683853c6cbecb",
      |            "blockId": "8d7dc915bed488779ca54c8cef980344835f571466f119f5f2188cf87f1f0068",
      |            "value": 726237050,
      |            "index": 1,
      |            "globalIndex": 11871822,
      |            "creationHeight": 506880,
      |            "settlementHeight": 661647,
      |            "ergoTree": "0008cd0295d0031c6b506aaeb1d146b21e1e19059a7a4392aceba9aaf045a85aebe9a21b",
      |            "address": "9ff3GvyDcswQnV6dfZnvJ8XJwyg1dqYngJKqoL1fVK74v8pD9Vi",
      |            "assets": [
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 0,
      |                    "amount": 6582157,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 1,
      |                    "amount": 400000000000,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 2,
      |                    "amount": 1000000000000,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "e91cbc48016eb390f8f872aa2962772863e2e840708517d1ab85e57451f91bed",
      |                    "index": 3,
      |                    "amount": 2400,
      |                    "name": "Ergold",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40",
      |                    "index": 4,
      |                    "amount": 2000,
      |                    "name": "kushti",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "151f9e9e0cb4aca29b8c8eaa765661c9f6cb4e018131b09bc63680e3e2585576",
      |                    "index": 5,
      |                    "amount": 100000,
      |                    "name": "Melange",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "5ed7b971363d38c3c0345bc257e47efe8bec8e4cc8767f390dbaefdf7e6a8f4e",
      |                    "index": 6,
      |                    "amount": 1,
      |                    "name": "Golden Ticket",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 7,
      |                    "amount": 1752543,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |                    "index": 8,
      |                    "amount": 1000000000,
      |                    "name": "NETA",
      |                    "decimals": 6,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "5a34d53ca483924b9a6aa0c771f11888881b516a8d1a9cdc535d063fe26d065e",
      |                    "index": 9,
      |                    "amount": 86338000386,
      |                    "name": "LunaDog",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "35812093dcd58ca29080e74739c1d4a63aa26871ed7baf73b5adc6e65dd6bec0",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "102895ec5da2e7fee0c1a770eded51c06a34e8ceb7d4e05239dff053621e2225",
      |            "transactionId": "f4f378be4978bc8b99f8feff7669d12f89ceb447535df3812af683853c6cbecb",
      |            "blockId": "8d7dc915bed488779ca54c8cef980344835f571466f119f5f2188cf87f1f0068",
      |            "value": 2000000,
      |            "index": 2,
      |            "globalIndex": 11871823,
      |            "creationHeight": 506880,
      |            "settlementHeight": 661647,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "82ede4a48be909e9a6ec32bef6aa3981eb46cfd83ba7aba27776adffa374e37a",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 1332
      |}
      |""".stripMargin).toOption.get.toTransaction

  val execute = decode[TransactionTest]("""
      |{
      |    "id": "325f0dc5c094afe51a8324c0652476417c7e6f088e9261b9477fe23727da1fc2",
      |    "blockId": "8d7dc915bed488779ca54c8cef980344835f571466f119f5f2188cf87f1f0068",
      |    "inclusionHeight": 661647,
      |    "timestamp": 1641941427683,
      |    "index": 7,
      |    "globalIndex": 2433204,
      |    "numConfirmations": 237228,
      |    "inputs": [
      |        {
      |            "boxId": "33715ef13350bbc75499e3509d3c30fc8519922f9e19dc4464cd211bbf57597d",
      |            "value": 44014371824529,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "2610043c5322ea5568aec84498c77452036a0e9a97b7cc9d2a80d1b8ae487d07",
      |            "outputTransactionId": "0004ee92ec47c02552f208dc507231cebb33090dd8913a4448a3c6a6ecb99ddb",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 11871139,
      |            "outputCreatedAt": 661623,
      |            "outputSettledAt": 661627,
      |            "ergoTree": "1999030f0400040204020404040405feffffffffffffffff0105feffffffffffffffff01050004d00f040004000406050005000580dac409d819d601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d6099973058c720602d60a999973068c7205027209d60bc17201d60cc1a7d60d99720b720cd60e91720d7307d60f8c720802d6107e720f06d6117e720d06d612998c720702720fd6137e720c06d6147308d6157e721206d6167e720a06d6177e720906d6189c72117217d6199c72157217d1ededededededed93c27201c2a793e4c672010404720293b27203730900b27204730a00938c7205018c720601938c7207018c72080193b17203730b9593720a730c95720e929c9c721072117e7202069c7ef07212069a9c72137e7214067e9c720d7e72020506929c9c721372157e7202069c7ef0720d069a9c72107e7214067e9c72127e7202050695ed720e917212730d907216a19d721872139d72197210ed9272189c721672139272199c7216721091720b730e",
      |            "address": "5vSUZRZbdVbnk4sJWjg2uhL94VZWRg4iatK9VgMChufzUgdihgvhR8yWSUEJKszzV7Vmi6K8hCyKTNhUaiP8p5ko6YEU9yfHpjVuXdQ4i5p4cRCzch6ZiqWrNukYjv7Vs5jvBwqg5hcEJ8u1eerr537YLWUoxxi1M4vQxuaCihzPKMt8NDXP4WcbN6mfNxxLZeGBvsHVvVmina5THaECosCWozKJFBnscjhpr3AJsdaL8evXAvPfEjGhVMoTKXAb2ZGGRmR8g1eZshaHmgTg2imSiaoXU5eiF3HvBnDuawaCtt674ikZ3oZdekqswcVPGMwqqUKVsGY4QuFeQoGwRkMqEYTdV2UDMMsfrjrBYQYKUBFMwsQGMNBL1VoY78aotXzdeqJCBVKbQdD3ZZWvukhSe4xrz8tcF3PoxpysDLt89boMqZJtGEHTV9UBTBEac6sDyQP693qT3nKaErN8TCXrJBUmHPqKozAg9bwxTqMYkpmb9iVKLSoJxG7MjAj72SRbcqQfNCVTztSwN3cRxSrVtz4p87jNFbVtFzhPg7UqDwNFTaasySCqM",
      |            "assets": [
      |                {
      |                    "tokenId": "1d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 1,
      |                    "amount": 9223372013068994886,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 2,
      |                    "amount": 55683576,
      |                    "name": "SigRSV",
      |                    "decimals": 0,
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
      |            "boxId": "0014655fd62c0668e3646c49833cd02df630959b7b1390d36d396e5cd1960636",
      |            "value": 745118814,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "8d7dc915bed488779ca54c8cef980344835f571466f119f5f2188cf87f1f0068",
      |            "outputTransactionId": "f4f378be4978bc8b99f8feff7669d12f89ceb447535df3812af683853c6cbecb",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 11871821,
      |            "outputCreatedAt": 506880,
      |            "outputSettledAt": 661647,
      |            "ergoTree": "19bd041808cd0295d0031c6b506aaeb1d146b21e1e19059a7a4392aceba9aaf045a85aebe9a21b040005fc91e9c005040404060402040205feffffffffffffffff0104040400040004000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f040404020580b6dc0505fc91e9c0050580b6dc0501000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d80bd604db63087202d605b2a5730500d606b27204730600d6077e9973078c72060206d6087ec1720206d6099d9c7e72030672077208d60ab27204730800d60b7e8c720a0206d60c9d9c7e8cb2db6308a773090002067207720bd60ddb63087205d60eb2720d730a00ededededed938cb27204730b0001730c93c27205d0720195ed8f7209720c93b1720d730dd801d60fb2720d730e00eded92c172059999c1a7730f7310938c720f018c720a01927e8c720f02069d9c99720c7209720b720795927209720c927ec1720506997e99c1a7731106997e7203069d9c997209720c720872077312938c720e018c720601927e8c720e0206a17209720c90b0ada5d9010f639593c2720f7313c1720f73147315d9010f599a8c720f018c720f0273167317",
      |            "address": "5KevXenwFgBWY8DXPxvBn61PpLJj3T9uhx5h89T6bfvHscrJSoci5QMvYFVEUGvhXWRgUo5as8qxgxBYg53Ut7CrL8YyDnqndYVdfAsv5wgJPugxtxQuRf9cPy8jNtFErYaL2fiuTFm2Ctgsrh2xdwHn2cTW9WmV3XW6nTsUdmyTKUfhHQkseDGGxdSS8HH9F5A15XuEAZaFdze215DU5E8GiaYYktt6U2GR51A84qN6Egw8JX3TT7uYYLQcYghf5sxqPcP6qC4XXTkqpUfQGWDdABDxBRennJMye3QVGW5m2YBUNgLK333hSSyHBpJweodDta2mv11xMCXzon2eZZ3bT4GN8YHrKie95EVJ194KQkc32E53ngFmY4aGgkCYeqQLiFK4Qt3Tg7QXCbGmjx6cRiY6xyzLH7BrshXx7nUw1m1kQ6rd1KCRccappKgT9Hm8AoYBAksKXpr1A1yVcqP8W7wyFk1wshyW9Mij2YHufbm2A4fBkGsh6LJaTpafoVxNn4ezXQWocuQK6iZXe8CG5LTKKxshrxGMRu4ibU4L4tUBXL3sZQy5cvQ9HgcKY78ypDEZ5Hjcoq4goQG8rwvfdAkdSk7JaDXTYhJPeA1Nfob8RVyQSDoH7PQArUzJPBPf2F3JecGkf52WJCGDPi8oYQ2CTJDEb3pgVzdhLHt1NHqQNdb64RBTxe8SVPAwQo3aFw5ZsLnGk2hDHi9T6MtbKxzTy6SeddBKMbsLJ6VWDzgWVPbCJ48vk3rLNJXy8yGjFQhgW2seM4gp2ituAQ4wK",
      |            "assets": [
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 0,
      |                    "amount": 935,
      |                    "name": "SigRSV",
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
      |            "boxId": "ffa8d9849f06016c21b6917cfd56cc8558701cbdc776abeeebcbb652694df543",
      |            "transactionId": "325f0dc5c094afe51a8324c0652476417c7e6f088e9261b9477fe23727da1fc2",
      |            "blockId": "8d7dc915bed488779ca54c8cef980344835f571466f119f5f2188cf87f1f0068",
      |            "value": 44015110883343,
      |            "index": 0,
      |            "globalIndex": 11871837,
      |            "creationHeight": 661644,
      |            "settlementHeight": 661647,
      |            "ergoTree": "1999030f0400040204020404040405feffffffffffffffff0105feffffffffffffffff01050004d00f040004000406050005000580dac409d819d601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d6099973058c720602d60a999973068c7205027209d60bc17201d60cc1a7d60d99720b720cd60e91720d7307d60f8c720802d6107e720f06d6117e720d06d612998c720702720fd6137e720c06d6147308d6157e721206d6167e720a06d6177e720906d6189c72117217d6199c72157217d1ededededededed93c27201c2a793e4c672010404720293b27203730900b27204730a00938c7205018c720601938c7207018c72080193b17203730b9593720a730c95720e929c9c721072117e7202069c7ef07212069a9c72137e7214067e9c720d7e72020506929c9c721372157e7202069c7ef0720d069a9c72107e7214067e9c72127e7202050695ed720e917212730d907216a19d721872139d72197210ed9272189c721672139272199c7216721091720b730e",
      |            "address": "5vSUZRZbdVbnk4sJWjg2uhL94VZWRg4iatK9VgMChufzUgdihgvhR8yWSUEJKszzV7Vmi6K8hCyKTNhUaiP8p5ko6YEU9yfHpjVuXdQ4i5p4cRCzch6ZiqWrNukYjv7Vs5jvBwqg5hcEJ8u1eerr537YLWUoxxi1M4vQxuaCihzPKMt8NDXP4WcbN6mfNxxLZeGBvsHVvVmina5THaECosCWozKJFBnscjhpr3AJsdaL8evXAvPfEjGhVMoTKXAb2ZGGRmR8g1eZshaHmgTg2imSiaoXU5eiF3HvBnDuawaCtt674ikZ3oZdekqswcVPGMwqqUKVsGY4QuFeQoGwRkMqEYTdV2UDMMsfrjrBYQYKUBFMwsQGMNBL1VoY78aotXzdeqJCBVKbQdD3ZZWvukhSe4xrz8tcF3PoxpysDLt89boMqZJtGEHTV9UBTBEac6sDyQP693qT3nKaErN8TCXrJBUmHPqKozAg9bwxTqMYkpmb9iVKLSoJxG7MjAj72SRbcqQfNCVTztSwN3cRxSrVtz4p87jNFbVtFzhPg7UqDwNFTaasySCqM",
      |            "assets": [
      |                {
      |                    "tokenId": "1d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 1,
      |                    "amount": 9223372013068595492,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 2,
      |                    "amount": 55684511,
      |                    "name": "SigRSV",
      |                    "decimals": 0,
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
      |            "spentTransactionId": "7ba284035d41339641306269690a5cf8279bf71573ab177f3228c7ee284e9111",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "4596ece54af9cb00dd3014bf9c215a6f2c687b0d75c7a6eabfd15ebfca61cb6c",
      |            "transactionId": "325f0dc5c094afe51a8324c0652476417c7e6f088e9261b9477fe23727da1fc2",
      |            "blockId": "8d7dc915bed488779ca54c8cef980344835f571466f119f5f2188cf87f1f0068",
      |            "value": 60000,
      |            "index": 1,
      |            "globalIndex": 11871838,
      |            "creationHeight": 661644,
      |            "settlementHeight": 661647,
      |            "ergoTree": "0008cd0295d0031c6b506aaeb1d146b21e1e19059a7a4392aceba9aaf045a85aebe9a21b",
      |            "address": "9ff3GvyDcswQnV6dfZnvJ8XJwyg1dqYngJKqoL1fVK74v8pD9Vi",
      |            "assets": [
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 0,
      |                    "amount": 399394,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "35812093dcd58ca29080e74739c1d4a63aa26871ed7baf73b5adc6e65dd6bec0",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "aed5eab06f83d733890534969352dd86b7057b76440f0310bcd87595553720e3",
      |            "transactionId": "325f0dc5c094afe51a8324c0652476417c7e6f088e9261b9477fe23727da1fc2",
      |            "blockId": "8d7dc915bed488779ca54c8cef980344835f571466f119f5f2188cf87f1f0068",
      |            "value": 4000000,
      |            "index": 2,
      |            "globalIndex": 11871839,
      |            "creationHeight": 661644,
      |            "settlementHeight": 661647,
      |            "ergoTree": "0008cd0273cbc003da723c0a5f416929692e8ec8c2b1e0d9aed69ff681f7581c63e70309",
      |            "address": "9fQ4NAwq8KyCHuVpwTDxK5yWqLgfv36Q83DAfsmvgSn8S4JtnPg",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "ab8a30141b66b14ce8bc2c46a7cec464ab4fad8411a1abe3fcc128c26e3a77e9",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "6c5350f286008ba712af7b20b16442b2ce8c7c4f8249ec9e51b07cb2374c0f3d",
      |            "transactionId": "325f0dc5c094afe51a8324c0652476417c7e6f088e9261b9477fe23727da1fc2",
      |            "blockId": "8d7dc915bed488779ca54c8cef980344835f571466f119f5f2188cf87f1f0068",
      |            "value": 2000000,
      |            "index": 3,
      |            "globalIndex": 11871840,
      |            "creationHeight": 661644,
      |            "settlementHeight": 661647,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "82ede4a48be909e9a6ec32bef6aa3981eb46cfd83ba7aba27776adffa374e37a",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 818
      |}
      |""".stripMargin).toOption.get.toTransaction

}
