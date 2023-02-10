package fi.spectrum.parser.evaluation

import fi.spectrum.core.domain.analytics.OffChainFee
import fi.spectrum.core.domain.analytics.OrderEvaluation.{AmmDepositEvaluation, AmmRedeemEvaluation, SwapEvaluation}
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.{Fee, OrderId, PoolId}
import fi.spectrum.core.domain.{Address, AssetAmount, BoxId, TokenId}
import fi.spectrum.parser._
import fi.spectrum.parser.models.TransactionTest
import fi.spectrum.parser.models.TransactionTest._
import io.circe.parser.decode

object Transactions {

  def lockTransaction = decode[TransactionTest](
    """
      |{
      |    "id": "c6d6f462271d66c302a3abbf2a5a4c17b1d4444296a8fb42642a63d3c3ab8e88",
      |    "blockId": "1416e77ed28e46bec7f044e6bfcebefa8ea41fce507b9cc3221f7e0b3d0cb8e4",
      |    "inclusionHeight": 671446,
      |    "timestamp": 1643107765307,
      |    "index": 28,
      |    "globalIndex": 2502335,
      |    "numConfirmations": 253887,
      |    "inputs": [
      |        {
      |            "boxId": "ca47831d30708c6a11120fd3773b6f06474165048b5ddfd0e0f94634160bc30d",
      |            "value": 1239052466,
      |            "index": 0,
      |            "spendingProof": "e07ca0572209563724a7a774d880362f344a4100abd9555b04afeb62b451caa5f59f4618db049bb8decd97839c620637a505e120956e9aea",
      |            "outputBlockId": "363904d6b66c14f4108c8534959ce52d6a2889abb6bd258ec750d8847d72bd51",
      |            "outputTransactionId": "7a8507a580ca6846aa7147024225668d4f9ffaaa0abff2002088b04a711694fc",
      |            "outputIndex": 1,
      |            "outputGlobalIndex": 12366314,
      |            "outputCreatedAt": 506880,
      |            "outputSettledAt": 671440,
      |            "ergoTree": "0008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec",
      |            "address": "9hp1xXVF8VXkYEHdgvTJK7XSEW1vckcKqWx8JTHsAwwGzHH9hxq",
      |            "assets": [
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 0,
      |                    "amount": 119,
      |                    "name": "SigUSD",
      |                    "decimals": 2,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
      |                    "index": 1,
      |                    "amount": 10000,
      |                    "name": "ergopad",
      |                    "decimals": 2,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 2,
      |                    "amount": 278,
      |                    "name": "SigRSV",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 3,
      |                    "amount": 9379503012346,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 4,
      |                    "amount": 80719900900172,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "5a34d53ca483924b9a6aa0c771f11888881b516a8d1a9cdc535d063fe26d065e",
      |                    "index": 5,
      |                    "amount": 92062556,
      |                    "name": "LunaDog",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40",
      |                    "index": 6,
      |                    "amount": 2219,
      |                    "name": "kushti",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "f45c4f0d95ce1c64defa607d94717a9a30c00fdd44827504be20db19f4dce36f",
      |                    "index": 7,
      |                    "amount": 50000,
      |                    "name": "TERG",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "e0588d273c8183865cff31b3bfa766bc7b178e2362b45497b67e79662e3615b7",
      |                    "index": 8,
      |                    "amount": 54,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 9,
      |                    "amount": 248509,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
      |                    "index": 10,
      |                    "amount": 4008362,
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
      |            "boxId": "553699ea402eab6d7bf7547ac796d2d06146a454abe83276e11718efebac7eca",
      |            "transactionId": "c6d6f462271d66c302a3abbf2a5a4c17b1d4444296a8fb42642a63d3c3ab8e88",
      |            "blockId": "1416e77ed28e46bec7f044e6bfcebefa8ea41fce507b9cc3221f7e0b3d0cb8e4",
      |            "value": 2060000,
      |            "index": 0,
      |            "globalIndex": 12366818,
      |            "creationHeight": 671442,
      |            "settlementHeight": 671446,
      |            "ergoTree": "195e03040004000400d802d601b2a5730000d602e4c6a70404ea02e4c6a70508d19593c27201c2a7d802d603b2db63087201730100d604b2db6308a7730200eded92e4c6720104047202938c7203018c720401928c7203028c7204028f7202a3",
      |            "address": "XqM6yyAmxNgCcRzvutWwtdSvKqqaEtd4cZRsVvJu1xeu4y5T9tZexJpPf1XMMWCZdv8zVK1XUbmM5gjB9KzWXQCMEWJcdNas6HYJFYf47m63kU3xZMHjUA3vNKRZWEj8AvQ75YBUx",
      |            "assets": [
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 0,
      |                    "amount": 59642,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {
      |                "R4": {
      |                    "serializedValue": "04c48652",
      |                    "sigmaType": "SInt",
      |                    "renderedValue": "672162"
      |                },
      |                "R5": {
      |                    "serializedValue": "08cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec",
      |                    "sigmaType": "SSigmaProp",
      |                    "renderedValue": "03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec"
      |                }
      |            },
      |            "spentTransactionId": "4c5995c5f3b7ca25987c373940176f5b557186baf2269a7206751755f29d680c",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "f0c6b792ee11f3f45c503280e34f8c7289446ea563328e4fdf15e3ac93d1ee1d",
      |            "transactionId": "c6d6f462271d66c302a3abbf2a5a4c17b1d4444296a8fb42642a63d3c3ab8e88",
      |            "blockId": "1416e77ed28e46bec7f044e6bfcebefa8ea41fce507b9cc3221f7e0b3d0cb8e4",
      |            "value": 1234992466,
      |            "index": 1,
      |            "globalIndex": 12366819,
      |            "creationHeight": 671442,
      |            "settlementHeight": 671446,
      |            "ergoTree": "0008cd03b196b978d77488fba3138876a40a40b9a046c2fbb5ecfa13d4ecf8f1eec52aec",
      |            "address": "9hp1xXVF8VXkYEHdgvTJK7XSEW1vckcKqWx8JTHsAwwGzHH9hxq",
      |            "assets": [
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 0,
      |                    "amount": 119,
      |                    "name": "SigUSD",
      |                    "decimals": 2,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "d71693c49a84fbbecd4908c94813b46514b18b67a99952dc1e6e4791556de413",
      |                    "index": 1,
      |                    "amount": 10000,
      |                    "name": "ergopad",
      |                    "decimals": 2,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 2,
      |                    "amount": 278,
      |                    "name": "SigRSV",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 3,
      |                    "amount": 9379503012346,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 4,
      |                    "amount": 80719900900172,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "5a34d53ca483924b9a6aa0c771f11888881b516a8d1a9cdc535d063fe26d065e",
      |                    "index": 5,
      |                    "amount": 92062556,
      |                    "name": "LunaDog",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40",
      |                    "index": 6,
      |                    "amount": 2219,
      |                    "name": "kushti",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "f45c4f0d95ce1c64defa607d94717a9a30c00fdd44827504be20db19f4dce36f",
      |                    "index": 7,
      |                    "amount": 50000,
      |                    "name": "TERG",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "e0588d273c8183865cff31b3bfa766bc7b178e2362b45497b67e79662e3615b7",
      |                    "index": 8,
      |                    "amount": 54,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 9,
      |                    "amount": 188867,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
      |                    "index": 10,
      |                    "amount": 4008362,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "752ae0d3078166aae0e6d516724daf3c8fec3e1aa0e88a250fdfeb05d975accd",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "487c49bd9e555e52816fb5c6f25ac80887afe162158d0971145e0e03d09777af",
      |            "transactionId": "c6d6f462271d66c302a3abbf2a5a4c17b1d4444296a8fb42642a63d3c3ab8e88",
      |            "blockId": "1416e77ed28e46bec7f044e6bfcebefa8ea41fce507b9cc3221f7e0b3d0cb8e4",
      |            "value": 2000000,
      |            "index": 2,
      |            "globalIndex": 12366820,
      |            "creationHeight": 671442,
      |            "settlementHeight": 671446,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "e5927b717ef7d3c020cae002b314e055abae7febde969aa4118ff13bce63ef66",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 799
      |}
      |""".stripMargin
  )
    .toOption.get.toTransaction

  def swapRegisterTransaction = decode[TransactionTest](
    """
      |{
      |    "id": "daedb49321cef71705afd26a755a76cb48d85b6c62cd913028424e417ad4db32",
      |    "blockId": "d581a9566f1a425e130d12f16bf5e373d53642e0a1beb506fb2d3769d652f203",
      |    "inclusionHeight": 711357,
      |    "timestamp": 1647914130937,
      |    "index": 10,
      |    "globalIndex": 2846177,
      |    "numConfirmations": 210447,
      |    "inputs": [
      |        {
      |            "boxId": "935200765076d79ef9016d8725c6dfbb28a8248a2dffd75adca7b23eadb53602",
      |            "value": 340000000,
      |            "index": 0,
      |            "spendingProof": "c6474e0af7016c6d6a8e76fd2f5db2f41047dd2724830c74cdc593906e9f00403520482c422cdb794de0b979078ed51a3e2c342cd2916a53",
      |            "outputBlockId": "54967cf26c950b5228bec1829368e06277eeef30a7b5d1ce430da4e83f1b2c61",
      |            "outputTransactionId": "f41899d1a9f427db680595c8c8dd3dfd34bd3273a627df7b14027c6c93bd27c6",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 14567339,
      |            "outputCreatedAt": 711352,
      |            "outputSettledAt": 711354,
      |            "ergoTree": "0008cd035debeab9bdfe7f534af5f2b288e86e372989f7703be4defe31b11888634c82e5",
      |            "address": "9hBAnrUPmqWV9o7fG5RrsxP8yFqCPB92NCSpn2ELnLTJwDpnhX2",
      |            "assets": [
      |                {
      |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |                    "index": 0,
      |                    "amount": 2300000000,
      |                    "name": "NETA",
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
      |            "boxId": "8f61ff76f14838990acf9bc54c75a8bee7f3951a11defef28305f342b9f81a32",
      |            "transactionId": "daedb49321cef71705afd26a755a76cb48d85b6c62cd913028424e417ad4db32",
      |            "blockId": "d581a9566f1a425e130d12f16bf5e373d53642e0a1beb506fb2d3769d652f203",
      |            "value": 7260000,
      |            "index": 0,
      |            "globalIndex": 14567890,
      |            "creationHeight": 711354,
      |            "settlementHeight": 711357,
      |            "ergoTree": "19b6031508cd035debeab9bdfe7f534af5f2b288e86e372989f7703be4defe31b11888634c82e50400040404060402058080a0f6f4acdbe01b058aa3fbf188c888e01b040004000e207d2e28431063cbb1e9e14468facc47b984d962532c19b0b14f74d0ce9ed459be05deebe1c6f50104c80f060101040404d00f04c80f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d804d603db63087202d604b2a5730400d6059d9c7e99c17204c1a7067e7305067e730606d6068cb2db6308a773070002edededed938cb2720373080001730993c27204d072019272057e730a06909c9c7ec17202067e7206067e730b069c9a7205730c9a9c7e8cb27203730d0002067e730e067e9c72067e730f050690b0ada5d90107639593c272077310c1720773117312d90107599a8c7207018c72070273137314",
      |            "address": "zCkAGbp4Tno2b1yqQ6sdUZvqEPSPGbxn2S5JaRBYzuBkk6tVVh6dMov1xRt1MCZkyLkXs7YdA8aQedP94aXjfANz2TqqedagVoFixKjnZ9coYq43GDVts8iZhTLoTMeJuNFPwZjwzFfqgYHgz29yCtd7SvnfcrwA6oiT3aN6aHTkV2FeC1o8azfx9QXKB4QbmHjL8XL9G2TJkCCzXuBWKBpsjTugLYMQvNdsaeYFVutDMt5wQC3co9kJU7Rfgayg8nvvPw3gWfP8PVYkoYaWNVVeqXWgJpaAzE1kWGfKvGYVrB7Fpno5jUYGn3yqfNrgaSmPaktYmw35hXhFbxc1gXAKsQURVL3h3PCR445XQUmchJLUaxWHiFg53cAPKjb9rXMxfKutCnp6uEdJhTrY559yrDxN4Tv1MzVW9j21uyCYEzB3fuo3gei7vR8w5XKwPA4VRgQJeK6G6R8KCgdurgcYzQoScXh33eaXzgUWzdE9UWtAhtPgaJBbii8mHRgTLUUG5GptNgfLRW4XkTFwfi8Tiz7fG3jA2iqsh2yTywXECpnBdsQehxad6nXfRQCaxnR9XLr3eEqnWcbhSAh1LCdE8wMd1LFL",
      |            "assets": [
      |                {
      |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |                    "index": 0,
      |                    "amount": 2300000000,
      |                    "name": "NETA",
      |                    "decimals": 6,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "3d14cdc67a2f6ca4929a229a5ac480c192d7b493757dadcd28472f075ef14c9a",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "a66e6b59eff0d3c0f26a46c1afd77fa2b4f01a0ffaf2558b87e226e4364a91aa",
      |            "transactionId": "daedb49321cef71705afd26a755a76cb48d85b6c62cd913028424e417ad4db32",
      |            "blockId": "d581a9566f1a425e130d12f16bf5e373d53642e0a1beb506fb2d3769d652f203",
      |            "value": 330740000,
      |            "index": 1,
      |            "globalIndex": 14567891,
      |            "creationHeight": 711354,
      |            "settlementHeight": 711357,
      |            "ergoTree": "0008cd035debeab9bdfe7f534af5f2b288e86e372989f7703be4defe31b11888634c82e5",
      |            "address": "9hBAnrUPmqWV9o7fG5RrsxP8yFqCPB92NCSpn2ELnLTJwDpnhX2",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "7cb34106fee9d128621b60a90cd8d608e80c92fb7310720873cc058e5e841d09",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "d1375ebeb3af9296c5d3b65a8a12ee57d6ec7c33a6e3aeb9eb53a6de269153ad",
      |            "transactionId": "daedb49321cef71705afd26a755a76cb48d85b6c62cd913028424e417ad4db32",
      |            "blockId": "d581a9566f1a425e130d12f16bf5e373d53642e0a1beb506fb2d3769d652f203",
      |            "value": 2000000,
      |            "index": 2,
      |            "globalIndex": 14567892,
      |            "creationHeight": 711354,
      |            "settlementHeight": 711357,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "c6fd9308274c8b41caa6a47a85170f3fa1dfd19ed81f8b7a265c58742344e7d3",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 741
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  def swapEvaluateTransaction = decode[TransactionTest](
    """
      |{
      |    "id": "3d14cdc67a2f6ca4929a229a5ac480c192d7b493757dadcd28472f075ef14c9a",
      |    "blockId": "81176ca4914d2c04298e15972d7500990a0b9fe4c192b165362481560f2b22ff",
      |    "inclusionHeight": 711359,
      |    "timestamp": 1647914481333,
      |    "index": 5,
      |    "globalIndex": 2846217,
      |    "numConfirmations": 210448,
      |    "inputs": [
      |        {
      |            "boxId": "000bde68859c4ca2430c992604d80ea3bd0bf1c97bfa26ed60e8b9bccbffa79f",
      |            "value": 50429702171826,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "92db4d07e854103caa60f7c6a36644cd8f0b441900f8770c68ace3217c5e5d00",
      |            "outputTransactionId": "a52e0466abf7bb8c6ed2128d4bf0ede0678b554ef6de7e129057f460e888fddc",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 14566713,
      |            "outputCreatedAt": 711340,
      |            "outputSettledAt": 711342,
      |            "ergoTree": "1999030f0400040204020404040405feffffffffffffffff0105feffffffffffffffff01050004d00f040004000406050005000580dac409d819d601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d6099973058c720602d60a999973068c7205027209d60bc17201d60cc1a7d60d99720b720cd60e91720d7307d60f8c720802d6107e720f06d6117e720d06d612998c720702720fd6137e720c06d6147308d6157e721206d6167e720a06d6177e720906d6189c72117217d6199c72157217d1ededededededed93c27201c2a793e4c672010404720293b27203730900b27204730a00938c7205018c720601938c7207018c72080193b17203730b9593720a730c95720e929c9c721072117e7202069c7ef07212069a9c72137e7214067e9c720d7e72020506929c9c721372157e7202069c7ef0720d069a9c72107e7214067e9c72127e7202050695ed720e917212730d907216a19d721872139d72197210ed9272189c721672139272199c7216721091720b730e",
      |            "address": "5vSUZRZbdVbnk4sJWjg2uhL94VZWRg4iatK9VgMChufzUgdihgvhR8yWSUEJKszzV7Vmi6K8hCyKTNhUaiP8p5ko6YEU9yfHpjVuXdQ4i5p4cRCzch6ZiqWrNukYjv7Vs5jvBwqg5hcEJ8u1eerr537YLWUoxxi1M4vQxuaCihzPKMt8NDXP4WcbN6mfNxxLZeGBvsHVvVmina5THaECosCWozKJFBnscjhpr3AJsdaL8evXAvPfEjGhVMoTKXAb2ZGGRmR8g1eZshaHmgTg2imSiaoXU5eiF3HvBnDuawaCtt674ikZ3oZdekqswcVPGMwqqUKVsGY4QuFeQoGwRkMqEYTdV2UDMMsfrjrBYQYKUBFMwsQGMNBL1VoY78aotXzdeqJCBVKbQdD3ZZWvukhSe4xrz8tcF3PoxpysDLt89boMqZJtGEHTV9UBTBEac6sDyQP693qT3nKaErN8TCXrJBUmHPqKozAg9bwxTqMYkpmb9iVKLSoJxG7MjAj72SRbcqQfNCVTztSwN3cRxSrVtz4p87jNFbVtFzhPg7UqDwNFTaasySCqM",
      |            "assets": [
      |                {
      |                    "tokenId": "7d2e28431063cbb1e9e14468facc47b984d962532c19b0b14f74d0ce9ed459be",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "e249780a22e14279357103749102d0a7033e0459d10b7f277356522ae9df779c",
      |                    "index": 1,
      |                    "amount": 9223359590880616642,
      |                    "name": "ERG_NETA_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |                    "index": 2,
      |                    "amount": 3400929094768,
      |                    "name": "NETA",
      |                    "decimals": 6,
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
      |            "boxId": "8f61ff76f14838990acf9bc54c75a8bee7f3951a11defef28305f342b9f81a32",
      |            "value": 7260000,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "d581a9566f1a425e130d12f16bf5e373d53642e0a1beb506fb2d3769d652f203",
      |            "outputTransactionId": "daedb49321cef71705afd26a755a76cb48d85b6c62cd913028424e417ad4db32",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 14567890,
      |            "outputCreatedAt": 711354,
      |            "outputSettledAt": 711357,
      |            "ergoTree": "19b6031508cd035debeab9bdfe7f534af5f2b288e86e372989f7703be4defe31b11888634c82e50400040404060402058080a0f6f4acdbe01b058aa3fbf188c888e01b040004000e207d2e28431063cbb1e9e14468facc47b984d962532c19b0b14f74d0ce9ed459be05deebe1c6f50104c80f060101040404d00f04c80f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d804d603db63087202d604b2a5730400d6059d9c7e99c17204c1a7067e7305067e730606d6068cb2db6308a773070002edededed938cb2720373080001730993c27204d072019272057e730a06909c9c7ec17202067e7206067e730b069c9a7205730c9a9c7e8cb27203730d0002067e730e067e9c72067e730f050690b0ada5d90107639593c272077310c1720773117312d90107599a8c7207018c72070273137314",
      |            "address": "zCkAGbp4Tno2b1yqQ6sdUZvqEPSPGbxn2S5JaRBYzuBkk6tVVh6dMov1xRt1MCZkyLkXs7YdA8aQedP94aXjfANz2TqqedagVoFixKjnZ9coYq43GDVts8iZhTLoTMeJuNFPwZjwzFfqgYHgz29yCtd7SvnfcrwA6oiT3aN6aHTkV2FeC1o8azfx9QXKB4QbmHjL8XL9G2TJkCCzXuBWKBpsjTugLYMQvNdsaeYFVutDMt5wQC3co9kJU7Rfgayg8nvvPw3gWfP8PVYkoYaWNVVeqXWgJpaAzE1kWGfKvGYVrB7Fpno5jUYGn3yqfNrgaSmPaktYmw35hXhFbxc1gXAKsQURVL3h3PCR445XQUmchJLUaxWHiFg53cAPKjb9rXMxfKutCnp6uEdJhTrY559yrDxN4Tv1MzVW9j21uyCYEzB3fuo3gei7vR8w5XKwPA4VRgQJeK6G6R8KCgdurgcYzQoScXh33eaXzgUWzdE9UWtAhtPgaJBbii8mHRgTLUUG5GptNgfLRW4XkTFwfi8Tiz7fG3jA2iqsh2yTywXECpnBdsQehxad6nXfRQCaxnR9XLr3eEqnWcbhSAh1LCdE8wMd1LFL",
      |            "assets": [
      |                {
      |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |                    "index": 0,
      |                    "amount": 2300000000,
      |                    "name": "NETA",
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
      |            "boxId": "0535db1c2e9d8d33a59a819f2b57b81753f41d3a3c3b6cba3cffcc34c3609c17",
      |            "transactionId": "3d14cdc67a2f6ca4929a229a5ac480c192d7b493757dadcd28472f075ef14c9a",
      |            "blockId": "81176ca4914d2c04298e15972d7500990a0b9fe4c192b165362481560f2b22ff",
      |            "value": 50395756565810,
      |            "index": 0,
      |            "globalIndex": 14568147,
      |            "creationHeight": 711355,
      |            "settlementHeight": 711359,
      |            "ergoTree": "1999030f0400040204020404040405feffffffffffffffff0105feffffffffffffffff01050004d00f040004000406050005000580dac409d819d601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d6099973058c720602d60a999973068c7205027209d60bc17201d60cc1a7d60d99720b720cd60e91720d7307d60f8c720802d6107e720f06d6117e720d06d612998c720702720fd6137e720c06d6147308d6157e721206d6167e720a06d6177e720906d6189c72117217d6199c72157217d1ededededededed93c27201c2a793e4c672010404720293b27203730900b27204730a00938c7205018c720601938c7207018c72080193b17203730b9593720a730c95720e929c9c721072117e7202069c7ef07212069a9c72137e7214067e9c720d7e72020506929c9c721372157e7202069c7ef0720d069a9c72107e7214067e9c72127e7202050695ed720e917212730d907216a19d721872139d72197210ed9272189c721672139272199c7216721091720b730e",
      |            "address": "5vSUZRZbdVbnk4sJWjg2uhL94VZWRg4iatK9VgMChufzUgdihgvhR8yWSUEJKszzV7Vmi6K8hCyKTNhUaiP8p5ko6YEU9yfHpjVuXdQ4i5p4cRCzch6ZiqWrNukYjv7Vs5jvBwqg5hcEJ8u1eerr537YLWUoxxi1M4vQxuaCihzPKMt8NDXP4WcbN6mfNxxLZeGBvsHVvVmina5THaECosCWozKJFBnscjhpr3AJsdaL8evXAvPfEjGhVMoTKXAb2ZGGRmR8g1eZshaHmgTg2imSiaoXU5eiF3HvBnDuawaCtt674ikZ3oZdekqswcVPGMwqqUKVsGY4QuFeQoGwRkMqEYTdV2UDMMsfrjrBYQYKUBFMwsQGMNBL1VoY78aotXzdeqJCBVKbQdD3ZZWvukhSe4xrz8tcF3PoxpysDLt89boMqZJtGEHTV9UBTBEac6sDyQP693qT3nKaErN8TCXrJBUmHPqKozAg9bwxTqMYkpmb9iVKLSoJxG7MjAj72SRbcqQfNCVTztSwN3cRxSrVtz4p87jNFbVtFzhPg7UqDwNFTaasySCqM",
      |            "assets": [
      |                {
      |                    "tokenId": "7d2e28431063cbb1e9e14468facc47b984d962532c19b0b14f74d0ce9ed459be",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "e249780a22e14279357103749102d0a7033e0459d10b7f277356522ae9df779c",
      |                    "index": 1,
      |                    "amount": 9223359590880616642,
      |                    "name": "ERG_NETA_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |                    "index": 2,
      |                    "amount": 3403229094768,
      |                    "name": "NETA",
      |                    "decimals": 6,
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
      |            "spentTransactionId": "c6134bf7ad190ffa9a1ceed51c5eb80c80cfd9246440dc3579169cf3f2c375ae",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "ef2e086655c0e4fed62e702aa1df4f7bdd5d007bae12c52aa2514d102999a6f6",
      |            "transactionId": "3d14cdc67a2f6ca4929a229a5ac480c192d7b493757dadcd28472f075ef14c9a",
      |            "blockId": "81176ca4914d2c04298e15972d7500990a0b9fe4c192b165362481560f2b22ff",
      |            "value": 33946866016,
      |            "index": 1,
      |            "globalIndex": 14568148,
      |            "creationHeight": 711355,
      |            "settlementHeight": 711359,
      |            "ergoTree": "0008cd035debeab9bdfe7f534af5f2b288e86e372989f7703be4defe31b11888634c82e5",
      |            "address": "9hBAnrUPmqWV9o7fG5RrsxP8yFqCPB92NCSpn2ELnLTJwDpnhX2",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "7cb34106fee9d128621b60a90cd8d608e80c92fb7310720873cc058e5e841d09",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "8ec534a350787f31c8ad869ce3f30ec451cd00afe634747dbaa65e8ba979dd25",
      |            "transactionId": "3d14cdc67a2f6ca4929a229a5ac480c192d7b493757dadcd28472f075ef14c9a",
      |            "blockId": "81176ca4914d2c04298e15972d7500990a0b9fe4c192b165362481560f2b22ff",
      |            "value": 4000000,
      |            "index": 2,
      |            "globalIndex": 14568149,
      |            "creationHeight": 711355,
      |            "settlementHeight": 711359,
      |            "ergoTree": "0008cd0273cbc003da723c0a5f416929692e8ec8c2b1e0d9aed69ff681f7581c63e70309",
      |            "address": "9fQ4NAwq8KyCHuVpwTDxK5yWqLgfv36Q83DAfsmvgSn8S4JtnPg",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "44786a2663543ec30a13ff47f35299dbf2b344ef348e22236d0922a0c7a3e5a6",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "5f0bd1d76c035b2cd90f0d5a8929dbfb59f9ff43ea5c6e8d770ccd8d64bd1193",
      |            "transactionId": "3d14cdc67a2f6ca4929a229a5ac480c192d7b493757dadcd28472f075ef14c9a",
      |            "blockId": "81176ca4914d2c04298e15972d7500990a0b9fe4c192b165362481560f2b22ff",
      |            "value": 2000000,
      |            "index": 3,
      |            "globalIndex": 14568150,
      |            "creationHeight": 711355,
      |            "settlementHeight": 711359,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "5d7d127fdaaf9b980554279c01fe4321bf66551c045f726fd544c98458206335",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 818
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  def swapPool = PoolParser.make.parse(swapPoolTx.outputs.head, 0, 10)

  def swapPoolTx = decode[TransactionTest](
    """
      |{
      |    "id": "a52e0466abf7bb8c6ed2128d4bf0ede0678b554ef6de7e129057f460e888fddc",
      |    "blockId": "92db4d07e854103caa60f7c6a36644cd8f0b441900f8770c68ace3217c5e5d00",
      |    "inclusionHeight": 711342,
      |    "timestamp": 1647911997376,
      |    "index": 10,
      |    "globalIndex": 2845991,
      |    "numConfirmations": 210481,
      |    "inputs": [
      |    {
      |            "boxId": "8f61ff76f14838990acf9bc54c75a8bee7f3951a11defef28305f342b9f81a32",
      |            "value": 7260000,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "d581a9566f1a425e130d12f16bf5e373d53642e0a1beb506fb2d3769d652f203",
      |            "outputTransactionId": "daedb49321cef71705afd26a755a76cb48d85b6c62cd913028424e417ad4db32",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 14567890,
      |            "outputCreatedAt": 711354,
      |            "outputSettledAt": 711357,
      |            "ergoTree": "19b6031508cd035debeab9bdfe7f534af5f2b288e86e372989f7703be4defe31b11888634c82e50400040404060402058080a0f6f4acdbe01b058aa3fbf188c888e01b040004000e207d2e28431063cbb1e9e14468facc47b984d962532c19b0b14f74d0ce9ed459be05deebe1c6f50104c80f060101040404d00f04c80f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d804d603db63087202d604b2a5730400d6059d9c7e99c17204c1a7067e7305067e730606d6068cb2db6308a773070002edededed938cb2720373080001730993c27204d072019272057e730a06909c9c7ec17202067e7206067e730b069c9a7205730c9a9c7e8cb27203730d0002067e730e067e9c72067e730f050690b0ada5d90107639593c272077310c1720773117312d90107599a8c7207018c72070273137314",
      |            "address": "zCkAGbp4Tno2b1yqQ6sdUZvqEPSPGbxn2S5JaRBYzuBkk6tVVh6dMov1xRt1MCZkyLkXs7YdA8aQedP94aXjfANz2TqqedagVoFixKjnZ9coYq43GDVts8iZhTLoTMeJuNFPwZjwzFfqgYHgz29yCtd7SvnfcrwA6oiT3aN6aHTkV2FeC1o8azfx9QXKB4QbmHjL8XL9G2TJkCCzXuBWKBpsjTugLYMQvNdsaeYFVutDMt5wQC3co9kJU7Rfgayg8nvvPw3gWfP8PVYkoYaWNVVeqXWgJpaAzE1kWGfKvGYVrB7Fpno5jUYGn3yqfNrgaSmPaktYmw35hXhFbxc1gXAKsQURVL3h3PCR445XQUmchJLUaxWHiFg53cAPKjb9rXMxfKutCnp6uEdJhTrY559yrDxN4Tv1MzVW9j21uyCYEzB3fuo3gei7vR8w5XKwPA4VRgQJeK6G6R8KCgdurgcYzQoScXh33eaXzgUWzdE9UWtAhtPgaJBbii8mHRgTLUUG5GptNgfLRW4XkTFwfi8Tiz7fG3jA2iqsh2yTywXECpnBdsQehxad6nXfRQCaxnR9XLr3eEqnWcbhSAh1LCdE8wMd1LFL",
      |            "assets": [
      |                {
      |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |                    "index": 0,
      |                    "amount": 2300000000,
      |                    "name": "NETA",
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
      |            "boxId": "000bde68859c4ca2430c992604d80ea3bd0bf1c97bfa26ed60e8b9bccbffa79f",
      |            "transactionId": "a52e0466abf7bb8c6ed2128d4bf0ede0678b554ef6de7e129057f460e888fddc",
      |            "blockId": "92db4d07e854103caa60f7c6a36644cd8f0b441900f8770c68ace3217c5e5d00",
      |            "value": 50429702171826,
      |            "index": 0,
      |            "globalIndex": 14566713,
      |            "creationHeight": 711340,
      |            "settlementHeight": 711342,
      |            "ergoTree": "1999030f0400040204020404040405feffffffffffffffff0105feffffffffffffffff01050004d00f040004000406050005000580dac409d819d601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d6099973058c720602d60a999973068c7205027209d60bc17201d60cc1a7d60d99720b720cd60e91720d7307d60f8c720802d6107e720f06d6117e720d06d612998c720702720fd6137e720c06d6147308d6157e721206d6167e720a06d6177e720906d6189c72117217d6199c72157217d1ededededededed93c27201c2a793e4c672010404720293b27203730900b27204730a00938c7205018c720601938c7207018c72080193b17203730b9593720a730c95720e929c9c721072117e7202069c7ef07212069a9c72137e7214067e9c720d7e72020506929c9c721372157e7202069c7ef0720d069a9c72107e7214067e9c72127e7202050695ed720e917212730d907216a19d721872139d72197210ed9272189c721672139272199c7216721091720b730e",
      |            "address": "5vSUZRZbdVbnk4sJWjg2uhL94VZWRg4iatK9VgMChufzUgdihgvhR8yWSUEJKszzV7Vmi6K8hCyKTNhUaiP8p5ko6YEU9yfHpjVuXdQ4i5p4cRCzch6ZiqWrNukYjv7Vs5jvBwqg5hcEJ8u1eerr537YLWUoxxi1M4vQxuaCihzPKMt8NDXP4WcbN6mfNxxLZeGBvsHVvVmina5THaECosCWozKJFBnscjhpr3AJsdaL8evXAvPfEjGhVMoTKXAb2ZGGRmR8g1eZshaHmgTg2imSiaoXU5eiF3HvBnDuawaCtt674ikZ3oZdekqswcVPGMwqqUKVsGY4QuFeQoGwRkMqEYTdV2UDMMsfrjrBYQYKUBFMwsQGMNBL1VoY78aotXzdeqJCBVKbQdD3ZZWvukhSe4xrz8tcF3PoxpysDLt89boMqZJtGEHTV9UBTBEac6sDyQP693qT3nKaErN8TCXrJBUmHPqKozAg9bwxTqMYkpmb9iVKLSoJxG7MjAj72SRbcqQfNCVTztSwN3cRxSrVtz4p87jNFbVtFzhPg7UqDwNFTaasySCqM",
      |            "assets": [
      |                {
      |                    "tokenId": "7d2e28431063cbb1e9e14468facc47b984d962532c19b0b14f74d0ce9ed459be",
      |                    "index": 0,
      |                    "amount": 1,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "e249780a22e14279357103749102d0a7033e0459d10b7f277356522ae9df779c",
      |                    "index": 1,
      |                    "amount": 9223359590880616642,
      |                    "name": "ERG_NETA_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |                    "index": 2,
      |                    "amount": 3400929094768,
      |                    "name": "NETA",
      |                    "decimals": 6,
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
      |            "spentTransactionId": "3d14cdc67a2f6ca4929a229a5ac480c192d7b493757dadcd28472f075ef14c9a",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 822
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  def expectedFee = OffChainFee(
    PoolId.unsafeFromString("7d2e28431063cbb1e9e14468facc47b984d962532c19b0b14f74d0ce9ed459be"),
    OrderId("8f61ff76f14838990acf9bc54c75a8bee7f3951a11defef28305f342b9f81a32"),
    BoxId("8ec534a350787f31c8ad869ce3f30ec451cd00afe634747dbaa65e8ba979dd25"),
    mkPubKey(Address.fromStringUnsafe("9fQ4NAwq8KyCHuVpwTDxK5yWqLgfv36Q83DAfsmvgSn8S4JtnPg")).get,
    ERG(4000000)
  )

  def swapEval = SwapEvaluation(
    AssetAmount(
      TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
      33946866016L
    ),
    Fee.ERG(4000000)
  )

  def redeemRegisterTransaction = decode[TransactionTest](
    """{
      |    "id": "e82c8bee39fc1b5179d3637801ab27621eee447b09bfbc4f989ec489384c72fc",
      |    "blockId": "0778aecaf3094cd3eceac4669fb063ad2df6ad0191cbb775391024488dbabb9c",
      |    "inclusionHeight": 751751,
      |    "timestamp": 1652795147515,
      |    "index": 3,
      |    "globalIndex": 3202251,
      |    "numConfirmations": 170064,
      |    "inputs": [
      |        {
      |            "boxId": "ede8661e339bb275f86624903fb6a230e8c6cd30853499a957aa016dfeb01521",
      |            "value": 590348332,
      |            "index": 0,
      |            "spendingProof": "5d7b0541c24713116dcd768b9215eec77a96d8efd688ada105169d055ec66e03bcefe3e361ddf4362de9e71781ced066a282f963a147ea8d",
      |            "outputBlockId": "4c8a12d0f4f7c19c86c9d825eb34baccffe9949a94f045da26b448123baa4c5f",
      |            "outputTransactionId": "a08180379bb4927930be1c4ffa62e28f5eab96506809e8d8390eedb8a114d9d6",
      |            "outputIndex": 1,
      |            "outputGlobalIndex": 16699749,
      |            "outputCreatedAt": 749013,
      |            "outputSettledAt": 749018,
      |            "ergoTree": "0008cd033d6ab05cfb8a65938e116cb863cad577e560bb8e110113bf395fbe98649dbb59",
      |            "address": "9gvrVxBDGUj3ofauSbfkBECFLHsR48uwgKC5ACm4TUzz8sbPoaR",
      |            "assets": [
      |                {
      |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |                    "index": 0,
      |                    "amount": 347255311,
      |                    "name": "NETA",
      |                    "decimals": 6,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 1,
      |                    "amount": 25473628,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 2,
      |                    "amount": 49,
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
      |            "boxId": "00304fca6ca7d7e7d3e0aff757dbf4b8354249534418d239f94840fa2e65555b",
      |            "transactionId": "e82c8bee39fc1b5179d3637801ab27621eee447b09bfbc4f989ec489384c72fc",
      |            "blockId": "0778aecaf3094cd3eceac4669fb063ad2df6ad0191cbb775391024488dbabb9c",
      |            "value": 6060000,
      |            "index": 0,
      |            "globalIndex": 16837517,
      |            "creationHeight": 751749,
      |            "settlementHeight": 751751,
      |            "ergoTree": "19b1031208cd033d6ab05cfb8a65938e116cb863cad577e560bb8e110113bf395fbe98649dbb59040004040406040204000404040005feffffffffffffffff01040204000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec0580b6dc050e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d806d603db63087202d604b2a5730400d605b2db63087204730500d606b27203730600d6077e8cb2db6308a77307000206d6087e9973088cb272037309000206ededededed938cb27203730a0001730b93c27204d07201938c7205018c720601927e9a99c17204c1a7730c069d9c72077ec17202067208927e8c720502069d9c72077e8c72060206720890b0ada5d90109639593c27209730dc17209730e730fd90109599a8c7209018c72090273107311",
      |            "address": "2ysN6BW5GJ8bTdCiJLrhggYPN9nFgd2q8gbTV8owUKYrKPfYtaaTnqxkv41gviV62cTxRDUbBGrg8aFZn5RSZAeLZxHsbEwD6vnxvuXEWy94yc5Sx5cT7XiqWtwZUAATP3MrGoUuYL5HGW61tGsgWKS9KJWseT3izsYhZj3r1y6G8gKgKt9oE8xDaDqh87C3jm4F3V1f9c8hTxPGeseHzF3utsS7Vf6CfbqVHq2Yz6fqNtDNgbQgZoDC66xwGAVFyLcD99WLeo764DbbHHrhufFPEhEWahTPYfNb5PCQv12c8SP618mKGYe8zmqyThYL62T6B5GQUv7VbaeQUWnwS1S8d6mLNGk3f8RUsKCYFGVWnHNd9zzyxbxuAvokLUDwx8taSzPfEeTu1Ey3fMGfgSJJwr8BwhdMvDeKnVcYFKYXrNv74wRN3vMBAffypdT2Wni9YSZXLsM8KsLuaVyzM9hWV6mDqxmPyMcthk6qnpkisy5WoJ3MYCRUHn3sMGVmrnSDEVYMdZvx7Tbg5iGPfWSUKiVQLR9wfFSunZCThyrxvdo3dcmqNRawLgdNXt6gLuVXr1Wddiks5AFrSFLC65Xe9K",
      |            "assets": [
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 0,
      |                    "amount": 25473628,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "c199b736fb0a2d1d9d4f8286b26629b274116ff2a228d73ac942df13ccc96774",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "17c59b0f0b127bff26df8eee38d7449660636ba733ba16afd3b87838b82e8a9f",
      |            "transactionId": "e82c8bee39fc1b5179d3637801ab27621eee447b09bfbc4f989ec489384c72fc",
      |            "blockId": "0778aecaf3094cd3eceac4669fb063ad2df6ad0191cbb775391024488dbabb9c",
      |            "value": 582288332,
      |            "index": 1,
      |            "globalIndex": 16837518,
      |            "creationHeight": 751749,
      |            "settlementHeight": 751751,
      |            "ergoTree": "0008cd033d6ab05cfb8a65938e116cb863cad577e560bb8e110113bf395fbe98649dbb59",
      |            "address": "9gvrVxBDGUj3ofauSbfkBECFLHsR48uwgKC5ACm4TUzz8sbPoaR",
      |            "assets": [
      |                {
      |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |                    "index": 0,
      |                    "amount": 347255311,
      |                    "name": "NETA",
      |                    "decimals": 6,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 1,
      |                    "amount": 49,
      |                    "name": "SigUSD",
      |                    "decimals": 2,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "cf0abd6c861b02910263de502c759d126d3a6019dfcc23c3152573d24de63b3a",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "98efad9d1654f8b3abd86a8a5bebca1e725fc544ab492aa17895d223b72bfe9e",
      |            "transactionId": "e82c8bee39fc1b5179d3637801ab27621eee447b09bfbc4f989ec489384c72fc",
      |            "blockId": "0778aecaf3094cd3eceac4669fb063ad2df6ad0191cbb775391024488dbabb9c",
      |            "value": 2000000,
      |            "index": 2,
      |            "globalIndex": 16837519,
      |            "creationHeight": 751749,
      |            "settlementHeight": 751751,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "d359d941a7ffce32a22c711cc50764eb028085b9402491ff9171dfa5dce82a75",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 807
      |}""".stripMargin
  ).toOption.get.toTransaction

  def redeemEvaluateTransaction = decode[TransactionTest]("""
      |{
      |    "id": "c199b736fb0a2d1d9d4f8286b26629b274116ff2a228d73ac942df13ccc96774",
      |    "blockId": "0778aecaf3094cd3eceac4669fb063ad2df6ad0191cbb775391024488dbabb9c",
      |    "inclusionHeight": 751751,
      |    "timestamp": 1652795147515,
      |    "index": 4,
      |    "globalIndex": 3202252,
      |    "numConfirmations": 170064,
      |    "inputs": [
      |        {
      |            "boxId": "1c43c58a95ab638acfec55c2108de9200d3fd455e1a9299997c0d5a9dc10201e",
      |            "value": 177395245835549,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "2061b1f46886b10c8bad7b17d5d0233972d655cfce63f8b4e61cda50f552cb19",
      |            "outputTransactionId": "72f2645b3d4a25152846b11f42e3e6542992302af750d84c03f230f05c575eef",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 16836367,
      |            "outputCreatedAt": 751721,
      |            "outputSettledAt": 751723,
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
      |                    "amount": 9223371958322496024,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 2,
      |                    "amount": 43097286,
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
      |            "boxId": "00304fca6ca7d7e7d3e0aff757dbf4b8354249534418d239f94840fa2e65555b",
      |            "value": 6060000,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "0778aecaf3094cd3eceac4669fb063ad2df6ad0191cbb775391024488dbabb9c",
      |            "outputTransactionId": "e82c8bee39fc1b5179d3637801ab27621eee447b09bfbc4f989ec489384c72fc",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 16837517,
      |            "outputCreatedAt": 751749,
      |            "outputSettledAt": 751751,
      |            "ergoTree": "19b1031208cd033d6ab05cfb8a65938e116cb863cad577e560bb8e110113bf395fbe98649dbb59040004040406040204000404040005feffffffffffffffff01040204000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec0580b6dc050e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d806d603db63087202d604b2a5730400d605b2db63087204730500d606b27203730600d6077e8cb2db6308a77307000206d6087e9973088cb272037309000206ededededed938cb27203730a0001730b93c27204d07201938c7205018c720601927e9a99c17204c1a7730c069d9c72077ec17202067208927e8c720502069d9c72077e8c72060206720890b0ada5d90109639593c27209730dc17209730e730fd90109599a8c7209018c72090273107311",
      |            "address": "2ysN6BW5GJ8bTdCiJLrhggYPN9nFgd2q8gbTV8owUKYrKPfYtaaTnqxkv41gviV62cTxRDUbBGrg8aFZn5RSZAeLZxHsbEwD6vnxvuXEWy94yc5Sx5cT7XiqWtwZUAATP3MrGoUuYL5HGW61tGsgWKS9KJWseT3izsYhZj3r1y6G8gKgKt9oE8xDaDqh87C3jm4F3V1f9c8hTxPGeseHzF3utsS7Vf6CfbqVHq2Yz6fqNtDNgbQgZoDC66xwGAVFyLcD99WLeo764DbbHHrhufFPEhEWahTPYfNb5PCQv12c8SP618mKGYe8zmqyThYL62T6B5GQUv7VbaeQUWnwS1S8d6mLNGk3f8RUsKCYFGVWnHNd9zzyxbxuAvokLUDwx8taSzPfEeTu1Ey3fMGfgSJJwr8BwhdMvDeKnVcYFKYXrNv74wRN3vMBAffypdT2Wni9YSZXLsM8KsLuaVyzM9hWV6mDqxmPyMcthk6qnpkisy5WoJ3MYCRUHn3sMGVmrnSDEVYMdZvx7Tbg5iGPfWSUKiVQLR9wfFSunZCThyrxvdo3dcmqNRawLgdNXt6gLuVXr1Wddiks5AFrSFLC65Xe9K",
      |            "assets": [
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 0,
      |                    "amount": 25473628,
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
      |            "boxId": "75a387142ca9643adc84caddc5159d379cee56f1d95b020f1e3d302400c4cdad",
      |            "transactionId": "c199b736fb0a2d1d9d4f8286b26629b274116ff2a228d73ac942df13ccc96774",
      |            "blockId": "0778aecaf3094cd3eceac4669fb063ad2df6ad0191cbb775391024488dbabb9c",
      |            "value": 177337703885744,
      |            "index": 0,
      |            "globalIndex": 16837520,
      |            "creationHeight": 751749,
      |            "settlementHeight": 751751,
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
      |                    "amount": 9223371958347969652,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 2,
      |                    "amount": 43083307,
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
      |            "spentTransactionId": "23edc397bd0753853f8447fc50e9ce32c767a4eb62b0ae3ec921d5028ac3b458",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "956d409d7d043612168b2382c230cc6e2338b64de2d8276c17f82da171b54e69",
      |            "transactionId": "c199b736fb0a2d1d9d4f8286b26629b274116ff2a228d73ac942df13ccc96774",
      |            "blockId": "0778aecaf3094cd3eceac4669fb063ad2df6ad0191cbb775391024488dbabb9c",
      |            "value": 57542009805,
      |            "index": 1,
      |            "globalIndex": 16837521,
      |            "creationHeight": 751749,
      |            "settlementHeight": 751751,
      |            "ergoTree": "0008cd033d6ab05cfb8a65938e116cb863cad577e560bb8e110113bf395fbe98649dbb59",
      |            "address": "9gvrVxBDGUj3ofauSbfkBECFLHsR48uwgKC5ACm4TUzz8sbPoaR",
      |            "assets": [
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 0,
      |                    "amount": 13979,
      |                    "name": "SigUSD",
      |                    "decimals": 2,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "cf0abd6c861b02910263de502c759d126d3a6019dfcc23c3152573d24de63b3a",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "ffd36dbd35500681e6a61bdcc864ded918de1b1123065a5da9755771c1ebec60",
      |            "transactionId": "c199b736fb0a2d1d9d4f8286b26629b274116ff2a228d73ac942df13ccc96774",
      |            "blockId": "0778aecaf3094cd3eceac4669fb063ad2df6ad0191cbb775391024488dbabb9c",
      |            "value": 4000000,
      |            "index": 2,
      |            "globalIndex": 16837522,
      |            "creationHeight": 751749,
      |            "settlementHeight": 751751,
      |            "ergoTree": "0008cd03fcdbc9be95dec63105e1698db0ec07cc3790145c65a8b2144143624cef394c07",
      |            "address": "9iPAd8DTstT6H3hF3L7MfjdrphGRmXzh8g93JdGbT6K7Px48faw",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "d98cf115bc0e5e6465bdf6cf3079a695287b752f460d4c5197a6bae11a2eca9c",
      |            "transactionId": "c199b736fb0a2d1d9d4f8286b26629b274116ff2a228d73ac942df13ccc96774",
      |            "blockId": "0778aecaf3094cd3eceac4669fb063ad2df6ad0191cbb775391024488dbabb9c",
      |            "value": 2000000,
      |            "index": 3,
      |            "globalIndex": 16837523,
      |            "creationHeight": 751749,
      |            "settlementHeight": 751751,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "d359d941a7ffce32a22c711cc50764eb028085b9402491ff9171dfa5dce82a75",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 820
      |}
      |""".stripMargin).toOption.get.toTransaction

  def redeemEval = AmmRedeemEvaluation(
    AssetAmount(
      TokenId.unsafeFromString(
        "0000000000000000000000000000000000000000000000000000000000000000"
      ),
      57542009805L
    ),
    AssetAmount(
      TokenId.unsafeFromString(
        "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04"
      ),
      13979
    )
  )

  def redeemPool = PoolParser.make.parse(
    decode[TransactionTest](
      """
      |{
      |    "id": "72f2645b3d4a25152846b11f42e3e6542992302af750d84c03f230f05c575eef",
      |    "blockId": "2061b1f46886b10c8bad7b17d5d0233972d655cfce63f8b4e61cda50f552cb19",
      |    "inclusionHeight": 751723,
      |    "timestamp": 1652792571913,
      |    "index": 6,
      |    "globalIndex": 3202020,
      |    "numConfirmations": 170110,
      |    "inputs": [
      |        {
      |            "boxId": "5f8113ae2aa92d5708be68934512cadb736496868237657d156fb542522d572b",
      |            "value": 175747247240574,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "16bd93c645fa0e9aa64b7da30fe344f92ca3a3d3f012d9e7a157274e8fb114f9",
      |            "outputTransactionId": "be90df98ad2afa18d4f56f5a45b3df9ec81d9aaf0bfaaa76defc20657b2caa54",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 16835930,
      |            "outputCreatedAt": 751718,
      |            "outputSettledAt": 751720,
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
      |                    "amount": 9223371959052059469,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 2,
      |                    "amount": 42696913,
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
      |            "boxId": "237059583eaed0f75b0e75f723ccb6efe034ee53f482a44c0145614527628f8a",
      |            "value": 1648006060000,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "2061b1f46886b10c8bad7b17d5d0233972d655cfce63f8b4e61cda50f552cb19",
      |            "outputTransactionId": "3dbd792f438f79d38284d356246a1b55a3b14a1a2589b2228f2fa281c1c5f729",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 16836342,
      |            "outputCreatedAt": 751721,
      |            "outputSettledAt": 751723,
      |            "ergoTree": "19bf041808cd0399126f6b97cc9ee4e63879c5bc4e076d977d4914e6d0f97304c0d276ea511d630400058080a3c7f65f040404060402040205feffffffffffffffff0104040400040004000e209916d75132593c8b07fe18bd8d583bda1652eed7565cf41a4738ddd90fc992ec040404020580b6dc05058080a3c7f65f0580b6dc0501000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d80bd604db63087202d605b2a5730500d606b27204730600d6077e9973078c72060206d6087ec1720206d6099d9c7e72030672077208d60ab27204730800d60b7e8c720a0206d60c9d9c7e8cb2db6308a773090002067207720bd60ddb63087205d60eb2720d730a00ededededed938cb27204730b0001730c93c27205d0720195ed8f7209720c93b1720d730dd801d60fb2720d730e00eded92c172059999c1a7730f7310938c720f018c720a01927e8c720f02069d9c99720c7209720b720795927209720c927ec1720506997e99c1a7731106997e7203069d9c997209720c720872077312938c720e018c720601927e8c720e0206a17209720c90b0ada5d9010f639593c2720f7313c1720f73147315d9010f599a8c720f018c720f0273167317",
      |            "address": "2TCBLxA4P6vLtmDuvZHvUJCnDwHHBQ3A4wqokXRn1sWm5dqrHB3vqpcLjB8CfCiUxhwbpyxn8Qa3J7o9EFDY9E1o17rsTonRAHE1yxGDo9ccTmVSeapcHgSjYKFrRe26tMJKPnZWW7Lg5AjpgPiF7BpHoynTCw4PMXAf7AQ82gKLoCUhaqMMLqe7411NRvgwu5HLhN2YHNiAdN2xPGtKsX8RMhRQWnzXkV4NaeiM1SzTxfCXXCJbarx8v6nRU2akCPHp6ESvjDptxMDiApsAuwqT8bA8wtoTtzrvVbUTNj3ovq5C8mivECKh9H7odSikSWZQbVDMmi9zaAnDPVoLBBraw8sErM9bNBhXuYtNLM5gbwiCFKbjD2h5C27nji2iGfcamF6awFDzMueTXTF41r9hcAWT4brcKuLWwiCDUHZj1JQj3f9PjdtL3nbBSFuVT1eTtsDD7NNTuEVUyn5UjygkqA8Yx7M9APtL9b4JMaEYjcS1S939JJDT2MGZU43iUFqjCghuHCkfNvnhFV9ozraGuMVkBHTc243e8v5LmMGDN64siRcZBCVAtDmEStyrejYMdTSmZBbuBCbJrKNXrypM4MXsX89Yjbv92SgbwPxjQ37TMdUtA2VfAdd5KQeUuDCXcc474hQxr7uXEa6vtDfg2MvGY7dGEsweYoXRJoGpR7TKiDwqAyT9qktmpLKHSgGTr8GKC99DqH52PQVLGJnsqBAVY7TXt8csHAr58DrE4d9gJiCM97ksZvXoWdZH6w4TsJvU4JzC8LdnfvVTwemU9714",
      |            "assets": [
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 0,
      |                    "amount": 400373,
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
      |            "boxId": "1c43c58a95ab638acfec55c2108de9200d3fd455e1a9299997c0d5a9dc10201e",
      |            "transactionId": "72f2645b3d4a25152846b11f42e3e6542992302af750d84c03f230f05c575eef",
      |            "blockId": "2061b1f46886b10c8bad7b17d5d0233972d655cfce63f8b4e61cda50f552cb19",
      |            "value": 177395245835549,
      |            "index": 0,
      |            "globalIndex": 16836367,
      |            "creationHeight": 751721,
      |            "settlementHeight": 751723,
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
      |                    "amount": 9223371958322496024,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04",
      |                    "index": 2,
      |                    "amount": 43097286,
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
      |            "spentTransactionId": "c199b736fb0a2d1d9d4f8286b26629b274116ff2a228d73ac942df13ccc96774",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "e0edd75ea441db614a5be1ca3397dab47ebcbab173ed6126b77a31b8471c2069",
      |            "transactionId": "72f2645b3d4a25152846b11f42e3e6542992302af750d84c03f230f05c575eef",
      |            "blockId": "2061b1f46886b10c8bad7b17d5d0233972d655cfce63f8b4e61cda50f552cb19",
      |            "value": 1465025,
      |            "index": 1,
      |            "globalIndex": 16836368,
      |            "creationHeight": 751721,
      |            "settlementHeight": 751723,
      |            "ergoTree": "0008cd0399126f6b97cc9ee4e63879c5bc4e076d977d4914e6d0f97304c0d276ea511d63",
      |            "address": "9hdDiAZWxd2G2RWnXUGVX6x24H8gPsJMSxcdBiK9Jp6APtudCH9",
      |            "assets": [
      |                {
      |                    "tokenId": "303f39026572bcb4060b51fafc93787a236bb243744babaa99fceb833d61e198",
      |                    "index": 0,
      |                    "amount": 729563445,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "82df24ff860b8aa1351990ae1721ce18f0b92c71d9bdbfea123ff249f22679a9",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "f088e9e14628c02709bf56532c2a8975808c0b3e66b79506de739fa7b0c1a95a",
      |            "transactionId": "72f2645b3d4a25152846b11f42e3e6542992302af750d84c03f230f05c575eef",
      |            "blockId": "2061b1f46886b10c8bad7b17d5d0233972d655cfce63f8b4e61cda50f552cb19",
      |            "value": 4000000,
      |            "index": 2,
      |            "globalIndex": 16836369,
      |            "creationHeight": 751721,
      |            "settlementHeight": 751723,
      |            "ergoTree": "0008cd03fcdbc9be95dec63105e1698db0ec07cc3790145c65a8b2144143624cef394c07",
      |            "address": "9iPAd8DTstT6H3hF3L7MfjdrphGRmXzh8g93JdGbT6K7Px48faw",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "45fe50d39f56a41bc2bc21856a0812b6be8c12d1a26347b2ff47b2d6d15865d3",
      |            "transactionId": "72f2645b3d4a25152846b11f42e3e6542992302af750d84c03f230f05c575eef",
      |            "blockId": "2061b1f46886b10c8bad7b17d5d0233972d655cfce63f8b4e61cda50f552cb19",
      |            "value": 2000000,
      |            "index": 3,
      |            "globalIndex": 16836370,
      |            "creationHeight": 751721,
      |            "settlementHeight": 751723,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "3a1655624365921806bcdd645dd217b5479b9f04c485f9c0f52cea75b70eb6d0",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 820
      |}
      |""".stripMargin
    ).toOption.get.toTransaction.outputs.head,
    0, 10
  )

  def redeemRegisterRefundTransaction = decode[TransactionTest](
    """
      |{
      |    "id": "ea9cb7ed7f8bd07a4e1e968bd50219ce67d37d596e95d2c3963d7dde46af6c76",
      |    "blockId": "c02c3d447332018fc8958c2d3466fe9b892fb548f339909575826d1a4947ac4e",
      |    "inclusionHeight": 614917,
      |    "timestamp": 1636343505560,
      |    "index": 4,
      |    "globalIndex": 2080115,
      |    "numConfirmations": 306916,
      |    "inputs": [
      |        {
      |            "boxId": "1970148a5b0fff2d74e1a9263982772aee3bf367d40ee28f6a6d12cf9177bf69",
      |            "value": 50000,
      |            "index": 0,
      |            "spendingProof": "3d0e1b814898eb4f6de06b605128e53bc0a34f79d12734afc4d88b39043b1ef8ff2411dfa1df2d43ea15f7599d1090e7d686759670c17824",
      |            "outputBlockId": "1c7f62bc373416f0ebac1bd5cd527792a52e027c374333d561a1e6d9b0f84fe2",
      |            "outputTransactionId": "07f12b948146254dad87ab9e7fdace030bcb6386c2c04a8f24902f661f868157",
      |            "outputIndex": 1,
      |            "outputGlobalIndex": 9265757,
      |            "outputCreatedAt": 612645,
      |            "outputSettledAt": 612647,
      |            "ergoTree": "0008cd03b68b7c3890355cbf5757e6c66938806c06d9f5dac14d9f8ac67e63d4654c20e6",
      |            "address": "9hrCZ9FFC3vB1m7AuPkxNnh42Lm5ReF3KvoXQpErmyNzq6Mvryg",
      |            "assets": [
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 0,
      |                    "amount": 97050,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        },
      |        {
      |            "boxId": "11e2c581d9366fd1bfc80caa8c4d0e55c2f1617668fef2f0c4e360d60bc7eecf",
      |            "value": 154459544,
      |            "index": 1,
      |            "spendingProof": "4b11e6057586eff7676e4089de2dd4fcfda6deca85bc73f44fe3d8f7ac410dfccbf121bef03802050527cc107d957d646850945ad5afd34c",
      |            "outputBlockId": "3e25ef1b37074bc4685de89ac0def1dd6d9c860c1bfc9091e0832639948942b7",
      |            "outputTransactionId": "9bfc0de1376050becf986168bcaff54bed55151459dc93606543911e402fca34",
      |            "outputIndex": 2,
      |            "outputGlobalIndex": 9265604,
      |            "outputCreatedAt": 506880,
      |            "outputSettledAt": 612646,
      |            "ergoTree": "0008cd03b68b7c3890355cbf5757e6c66938806c06d9f5dac14d9f8ac67e63d4654c20e6",
      |            "address": "9hrCZ9FFC3vB1m7AuPkxNnh42Lm5ReF3KvoXQpErmyNzq6Mvryg",
      |            "assets": [],
      |            "additionalRegisters": {}
      |        }
      |    ],
      |    "dataInputs": [],
      |    "outputs": [
      |        {
      |            "boxId": "002dc20c44f00f9f62fab96522e80ea65b561de7b14d3a8ed5c84d44825774e3",
      |            "transactionId": "ea9cb7ed7f8bd07a4e1e968bd50219ce67d37d596e95d2c3963d7dde46af6c76",
      |            "blockId": "c02c3d447332018fc8958c2d3466fe9b892fb548f339909575826d1a4947ac4e",
      |            "value": 7050000,
      |            "index": 0,
      |            "globalIndex": 9386573,
      |            "creationHeight": 506880,
      |            "settlementHeight": 614917,
      |            "ergoTree": "1997020e08cd03b68b7c3890355cbf5757e6c66938806c06d9f5dac14d9f8ac67e63d4654c20e6040004040406040204000404040005feffffffffffffffff01040204000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f0580bfd6060100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d806d603db63087202d604b2a5730400d605b2db63087204730500d606b27203730600d6077e8cb2db6308a77307000206d6087e9973088cb272037309000206edededed938cb27203730a0001730b93c27204d07201938c7205018c720601927e9a99c17204c1a7730c069d9c72077ec17202067208927e8c720502069d9c72077e8c720602067208730d",
      |            "address": "ZSU7trPRbjfefAPmm8DQiXnUDr81CB343Lu3vT92DYoskEvq1K8Smb8bzVgX6U8ZciLLKbESEA79HqhjbCSYo8VTtREyCDF7peruK2WeeSrG5gF3uPpXq163VfkTpmvB5Huz3Bv9pukcSisHWYFs3EM2iDzy3MCHXNemnivLAKPENzuaC6AknZQvjJnq4uxVHiQ5f5B5e1RiwiRrrDjbPocCGgeLDGTe4G4dUVKUeTcTPE1vJpEggJyiwuV7XGjzgWuuj2rnXGJ15hbDE3JyEz23HoSTGyfhHpkmdVr5Lm5GhVjoMBD7QEG8wF61FEvyoXYj8Wz8PpQpQguanuT7L19n2Bs1d5KEdsUXNpBZzwCKuLe3YyjLZZ6XVks7YYjxNcJnuQ8",
      |            "assets": [
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 0,
      |                    "amount": 97050,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "985bd88039045fa6a67bf6f521bf73a9f07d547dfc1962c8983fd60f4593816e",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "86cadf7ee8c0cedd1c9771b7516600e3977a43042a0932a902b51c0be7a745dc",
      |            "transactionId": "ea9cb7ed7f8bd07a4e1e968bd50219ce67d37d596e95d2c3963d7dde46af6c76",
      |            "blockId": "c02c3d447332018fc8958c2d3466fe9b892fb548f339909575826d1a4947ac4e",
      |            "value": 10000000,
      |            "index": 1,
      |            "globalIndex": 9386574,
      |            "creationHeight": 506880,
      |            "settlementHeight": 614917,
      |            "ergoTree": "0008cd02ddbe95b7f88d47bd8c2db823cc5dd1be69a650556a44d4c15ac65e1d3e34324c",
      |            "address": "9gCigPc9cZNRhKgbgdmTkVxo1ZKgw79G8DvLjCcYWAvEF3XRUKy",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "a8a04a280196e1d6b69c643f6dacd7ef73605682d511c9b48ab122482f71a796",
      |            "transactionId": "ea9cb7ed7f8bd07a4e1e968bd50219ce67d37d596e95d2c3963d7dde46af6c76",
      |            "blockId": "c02c3d447332018fc8958c2d3466fe9b892fb548f339909575826d1a4947ac4e",
      |            "value": 135459544,
      |            "index": 2,
      |            "globalIndex": 9386575,
      |            "creationHeight": 506880,
      |            "settlementHeight": 614917,
      |            "ergoTree": "0008cd03b68b7c3890355cbf5757e6c66938806c06d9f5dac14d9f8ac67e63d4654c20e6",
      |            "address": "9hrCZ9FFC3vB1m7AuPkxNnh42Lm5ReF3KvoXQpErmyNzq6Mvryg",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "1efbbcb4a4100a3a35cfa330d3f9003a314a8bc53b4cad1eec6ef29808ec1aaa",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "bfcfb4cf50fce64789da4698f64035568209055363b5a8535d7b41cac4f40493",
      |            "transactionId": "ea9cb7ed7f8bd07a4e1e968bd50219ce67d37d596e95d2c3963d7dde46af6c76",
      |            "blockId": "c02c3d447332018fc8958c2d3466fe9b892fb548f339909575826d1a4947ac4e",
      |            "value": 2000000,
      |            "index": 3,
      |            "globalIndex": 9386576,
      |            "creationHeight": 506880,
      |            "settlementHeight": 614917,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "6e571239e4b228a1ccd13a1e90f7af0f15cb174e8ae04ad64ee3429e772247bf",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 714
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  def redeemRefundTransaction = decode[TransactionTest](
    """
      |{
      |    "id": "985bd88039045fa6a67bf6f521bf73a9f07d547dfc1962c8983fd60f4593816e",
      |    "blockId": "c02c3d447332018fc8958c2d3466fe9b892fb548f339909575826d1a4947ac4e",
      |    "inclusionHeight": 614917,
      |    "timestamp": 1636343505560,
      |    "index": 16,
      |    "globalIndex": 2080127,
      |    "numConfirmations": 306916,
      |    "inputs": [
      |        {
      |            "boxId": "31f67f26af8ad996b0a033196dc018895a023d0980b7dcba51124615bd4ac9fb",
      |            "value": 26325518798583,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "354b8dca07af115d3cbbb65aa8a7b374694d77f84eda4d62f39a9ed11f436851",
      |            "outputTransactionId": "ead33eea4ec95cdc73f0f576dd6c8725b575a7ab84d73b5946dfdc2be0f60803",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 9386152,
      |            "outputCreatedAt": 614909,
      |            "outputSettledAt": 614911,
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
      |                    "amount": 9223372024007264472,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 2,
      |                    "amount": 26923013,
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
      |            "boxId": "002dc20c44f00f9f62fab96522e80ea65b561de7b14d3a8ed5c84d44825774e3",
      |            "value": 7050000,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "c02c3d447332018fc8958c2d3466fe9b892fb548f339909575826d1a4947ac4e",
      |            "outputTransactionId": "ea9cb7ed7f8bd07a4e1e968bd50219ce67d37d596e95d2c3963d7dde46af6c76",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 9386573,
      |            "outputCreatedAt": 506880,
      |            "outputSettledAt": 614917,
      |            "ergoTree": "1997020e08cd03b68b7c3890355cbf5757e6c66938806c06d9f5dac14d9f8ac67e63d4654c20e6040004040406040204000404040005feffffffffffffffff01040204000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f0580bfd6060100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d806d603db63087202d604b2a5730400d605b2db63087204730500d606b27203730600d6077e8cb2db6308a77307000206d6087e9973088cb272037309000206edededed938cb27203730a0001730b93c27204d07201938c7205018c720601927e9a99c17204c1a7730c069d9c72077ec17202067208927e8c720502069d9c72077e8c720602067208730d",
      |            "address": "ZSU7trPRbjfefAPmm8DQiXnUDr81CB343Lu3vT92DYoskEvq1K8Smb8bzVgX6U8ZciLLKbESEA79HqhjbCSYo8VTtREyCDF7peruK2WeeSrG5gF3uPpXq163VfkTpmvB5Huz3Bv9pukcSisHWYFs3EM2iDzy3MCHXNemnivLAKPENzuaC6AknZQvjJnq4uxVHiQ5f5B5e1RiwiRrrDjbPocCGgeLDGTe4G4dUVKUeTcTPE1vJpEggJyiwuV7XGjzgWuuj2rnXGJ15hbDE3JyEz23HoSTGyfhHpkmdVr5Lm5GhVjoMBD7QEG8wF61FEvyoXYj8Wz8PpQpQguanuT7L19n2Bs1d5KEdsUXNpBZzwCKuLe3YyjLZZ6XVks7YYjxNcJnuQ8",
      |            "assets": [
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 0,
      |                    "amount": 97050,
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
      |            "boxId": "51bd241e4f5b9567a1e77b94b0c4e8eb0ec609d3196398fe4eaecdcd91d197e9",
      |            "transactionId": "985bd88039045fa6a67bf6f521bf73a9f07d547dfc1962c8983fd60f4593816e",
      |            "blockId": "c02c3d447332018fc8958c2d3466fe9b892fb548f339909575826d1a4947ac4e",
      |            "value": 26325319935821,
      |            "index": 0,
      |            "globalIndex": 9386641,
      |            "creationHeight": 614915,
      |            "settlementHeight": 614917,
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
      |                    "amount": 9223372024007361522,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 2,
      |                    "amount": 26922810,
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
      |            "spentTransactionId": "65dc26cb5e107ee6aa24772c65d902161797759662d03b54694b029b2234eb17",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "1fb06f171593f478dad57138914f207f4217263e6cbfebe8b82a9615caf67c7f",
      |            "transactionId": "985bd88039045fa6a67bf6f521bf73a9f07d547dfc1962c8983fd60f4593816e",
      |            "blockId": "c02c3d447332018fc8958c2d3466fe9b892fb548f339909575826d1a4947ac4e",
      |            "value": 198912762,
      |            "index": 1,
      |            "globalIndex": 9386642,
      |            "creationHeight": 614915,
      |            "settlementHeight": 614917,
      |            "ergoTree": "0008cd03b68b7c3890355cbf5757e6c66938806c06d9f5dac14d9f8ac67e63d4654c20e6",
      |            "address": "9hrCZ9FFC3vB1m7AuPkxNnh42Lm5ReF3KvoXQpErmyNzq6Mvryg",
      |            "assets": [
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 0,
      |                    "amount": 203,
      |                    "name": "SigRSV",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "1efbbcb4a4100a3a35cfa330d3f9003a314a8bc53b4cad1eec6ef29808ec1aaa",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "c1350a5ca6d9ff99ab00ee6662abb988321314955bb63559e125c00f6af9a142",
      |            "transactionId": "985bd88039045fa6a67bf6f521bf73a9f07d547dfc1962c8983fd60f4593816e",
      |            "blockId": "c02c3d447332018fc8958c2d3466fe9b892fb548f339909575826d1a4947ac4e",
      |            "value": 6000000,
      |            "index": 2,
      |            "globalIndex": 9386643,
      |            "creationHeight": 614915,
      |            "settlementHeight": 614917,
      |            "ergoTree": "0008cd02870028db3cfa1cbaef31bd920842d118de32a796db8889cff125562bfd915215",
      |            "address": "9fYWvb9W6VNbHrw6jYzLtW2mNr9a7JBnGHHxvJTR2aDS4Pv6Yif",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "7e2996980d130a81945c5ad7cf0d4fc6496ccff974dd00de9d5f86107efcc24f",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "95fcd463a4259383f03ee8df6af2c0dc70f45484dc2959866d60fe58cee196a8",
      |            "transactionId": "985bd88039045fa6a67bf6f521bf73a9f07d547dfc1962c8983fd60f4593816e",
      |            "blockId": "c02c3d447332018fc8958c2d3466fe9b892fb548f339909575826d1a4947ac4e",
      |            "value": 1000000,
      |            "index": 3,
      |            "globalIndex": 9386644,
      |            "creationHeight": 614915,
      |            "settlementHeight": 614917,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "6e571239e4b228a1ccd13a1e90f7af0f15cb174e8ae04ad64ee3429e772247bf",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 818
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  def depositRegisterTransaction = decode[TransactionTest](
    """
      |{
      |    "id": "d934ad8c2c83d1591e9995500a8ea5e47fca1c18f8f27e4ffe8f772264820434",
      |    "blockId": "598a9d7f0801a366d1020cc2650bc385b1dd5cfcc584c20a06792393a9f680c9",
      |    "inclusionHeight": 900249,
      |    "timestamp": 1671543753053,
      |    "index": 2,
      |    "globalIndex": 4496189,
      |    "numConfirmations": 21567,
      |    "inputs": [
      |        {
      |            "boxId": "144e09e855ab8321f5f3d1a01609a42fc68622b41e4fbc1842d772c141effeeb",
      |            "value": 1080723,
      |            "index": 0,
      |            "spendingProof": "2211b976ea35f07d9f2421360990f33f98b88b4cdba9ea902717765adf670b71a0bb5b80bb3f982d9c3ada53dcc3545fa896a39b4c628510",
      |            "outputBlockId": "b7293f107f3c3d8aaee9e2a8abdc17adbf570ec1bd0b8a7bf9fd999e68dd1792",
      |            "outputTransactionId": "a7fed538255930595efdbd386598d194ec69d43401b2c3672b9e8cfd3c3d7a88",
      |            "outputIndex": 1,
      |            "outputGlobalIndex": 24076769,
      |            "outputCreatedAt": 885312,
      |            "outputSettledAt": 885314,
      |            "ergoTree": "0008cd02413d9de2ebe606be3f1120db10f29d53e91f0d20572db092ea536fffb1f73bf6",
      |            "address": "9f1o11H7cuD95sgzTSBgA5GEWKx9aBLMt98if1vsxtEao1RaL22",
      |            "assets": [
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 0,
      |                    "amount": 2137,
      |                    "name": "SigRSV",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        },
      |        {
      |            "boxId": "0e229f1ec7337b68ef074bb0ee9ea5a0845f4a97b1a646fa55be057645a04b2d",
      |            "value": 60000,
      |            "index": 1,
      |            "spendingProof": "120d401f6120062fc3e03d2502acc9912a3baa80d25e11b84fe0d5aa17bbe5328707416213a118810f80f0f7f3ece0fa72625f30901e913b",
      |            "outputBlockId": "2e3180eeffdb1d26231fe76f17be812b53796715e63deb3bcc4fb97ff19327ae",
      |            "outputTransactionId": "c30bb127d4729c76d5c5c27999811eb864bc18b5f35138c62fbc74d131b63f46",
      |            "outputIndex": 1,
      |            "outputGlobalIndex": 24076214,
      |            "outputCreatedAt": 885299,
      |            "outputSettledAt": 885301,
      |            "ergoTree": "0008cd02413d9de2ebe606be3f1120db10f29d53e91f0d20572db092ea536fffb1f73bf6",
      |            "address": "9f1o11H7cuD95sgzTSBgA5GEWKx9aBLMt98if1vsxtEao1RaL22",
      |            "assets": [
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 0,
      |                    "amount": 4331220,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        },
      |        {
      |            "boxId": "83e0aed0bced1775cc4d79b93cbfd74cc36ced025c703f607857cf74aedaeb6d",
      |            "value": 294183639,
      |            "index": 2,
      |            "spendingProof": "c89f50dfc67a5ea7e785c0d8adb4cc8bca800de89332841206d0c8eb4f572820d90e938268a0bf023e4a27b5f8bc6f2e6581cc1120a5d4ed",
      |            "outputBlockId": "b7293f107f3c3d8aaee9e2a8abdc17adbf570ec1bd0b8a7bf9fd999e68dd1792",
      |            "outputTransactionId": "d7691d9e1c6670e7b9f0048eda9e2d31843e5ed4868463d8c36275e1e03f7071",
      |            "outputIndex": 1,
      |            "outputGlobalIndex": 24076763,
      |            "outputCreatedAt": 885312,
      |            "outputSettledAt": 885314,
      |            "ergoTree": "0008cd02413d9de2ebe606be3f1120db10f29d53e91f0d20572db092ea536fffb1f73bf6",
      |            "address": "9f1o11H7cuD95sgzTSBgA5GEWKx9aBLMt98if1vsxtEao1RaL22",
      |            "assets": [
      |                {
      |                    "tokenId": "1fd6e032e8476c4aa54c18c1a308dce83940e8f4a28f576440513ed7326ad489",
      |                    "index": 0,
      |                    "amount": 10644424,
      |                    "name": "Paideia",
      |                    "decimals": 4,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "4dcda5b0c8e60ee5bb23c53edc43555cca02aeaa066901ecaa9f57d3b89c191e",
      |                    "index": 1,
      |                    "amount": 1,
      |                    "name": "ergopad Stake Key",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "3813fe93744a19d837a5367cd7a4a28a7e5e29816a50ff99b7f36c61580eeb5f",
      |                    "index": 2,
      |                    "amount": 1,
      |                    "name": "EGIO Stake Key",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "3c3e2a348be7486727926d24f9d1a652a2222b9c168c511c51f671cf48ae5e19",
      |                    "index": 3,
      |                    "amount": 1,
      |                    "name": "ergopad Stake Key",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "6fd4227cd53448bd734b90157062e1cbaa0d07b1752e63bfd1bb52dbd6cc581e",
      |                    "index": 4,
      |                    "amount": 1,
      |                    "name": "ergopad Stake Key",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 5,
      |                    "amount": 3959912,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        },
      |        {
      |            "boxId": "8b27ca01d1e39e9c3622f1439e0c6be17ba3bc052d1194ce635b3cbf4076da41",
      |            "value": 524413980,
      |            "index": 3,
      |            "spendingProof": "2b3828b97a561c9a35a62818d2d3e8bd3e2e91f3fab476273134de8ca7334a2b1b085b0d1a77718f4eae997485e9ed76f605ae60f7eb8979",
      |            "outputBlockId": "bb970bc38e15fed94d6298754845572f3663147d937277f5d4142ee91297f207",
      |            "outputTransactionId": "baf949b8c10ea0880bcf3cdc1dc19c0e443800f9193a0be8cdcf1c3cdc208c63",
      |            "outputIndex": 277,
      |            "outputGlobalIndex": 24679388,
      |            "outputCreatedAt": 898196,
      |            "outputSettledAt": 898199,
      |            "ergoTree": "0008cd02413d9de2ebe606be3f1120db10f29d53e91f0d20572db092ea536fffb1f73bf6",
      |            "address": "9f1o11H7cuD95sgzTSBgA5GEWKx9aBLMt98if1vsxtEao1RaL22",
      |            "assets": [],
      |            "additionalRegisters": {}
      |        },
      |        {
      |            "boxId": "a1c0753eff4c986553a0da4c389de5db11be8161cef73407dbb705d7561a4444",
      |            "value": 515074098,
      |            "index": 4,
      |            "spendingProof": "f07ea977bf717d8cc6dc4f7c69ae2ddfc07d7c328dca0c94ecbf35d45b6336408b838faa8d8ebe356368e7b7f13dff8a585fd6f5de44dd49",
      |            "outputBlockId": "02b317c1974c36ed10e1c29a5baf1cc53add840e02947924f2b40440d2c7f4b9",
      |            "outputTransactionId": "7509ff769696a0b4b19309328153ee0142420523853be8613e4e0841565b488e",
      |            "outputIndex": 152,
      |            "outputGlobalIndex": 24732621,
      |            "outputCreatedAt": 899387,
      |            "outputSettledAt": 899390,
      |            "ergoTree": "0008cd02413d9de2ebe606be3f1120db10f29d53e91f0d20572db092ea536fffb1f73bf6",
      |            "address": "9f1o11H7cuD95sgzTSBgA5GEWKx9aBLMt98if1vsxtEao1RaL22",
      |            "assets": [],
      |            "additionalRegisters": {}
      |        }
      |    ],
      |    "dataInputs": [],
      |    "outputs": [
      |        {
      |            "boxId": "004fc3740dde5f52efe1035059762410c70ed88f00aaad93b5b75d68640610bc",
      |            "transactionId": "d934ad8c2c83d1591e9995500a8ea5e47fca1c18f8f27e4ffe8f772264820434",
      |            "blockId": "598a9d7f0801a366d1020cc2650bc385b1dd5cfcc584c20a06792393a9f680c9",
      |            "value": 959654465,
      |            "index": 0,
      |            "globalIndex": 24773568,
      |            "creationHeight": 900247,
      |            "settlementHeight": 900249,
      |            "ergoTree": "19bd041808cd02413d9de2ebe606be3f1120db10f29d53e91f0d20572db092ea536fffb1f73bf6040005c2c9b58d07040404060402040205feffffffffffffffff0104040400040004000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f040404020580b6dc0505c2c9b58d070580b6dc0501000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d80bd604db63087202d605b2a5730500d606b27204730600d6077e9973078c72060206d6087ec1720206d6099d9c7e72030672077208d60ab27204730800d60b7e8c720a0206d60c9d9c7e8cb2db6308a773090002067207720bd60ddb63087205d60eb2720d730a00ededededed938cb27204730b0001730c93c27205d0720195ed8f7209720c93b1720d730dd801d60fb2720d730e00eded92c172059999c1a7730f7310938c720f018c720a01927e8c720f02069d9c99720c7209720b720795927209720c927ec1720506997e99c1a7731106997e7203069d9c997209720c720872077312938c720e018c720601927e8c720e0206a17209720c90b0ada5d9010f639593c2720f7313c1720f73147315d9010f599a8c720f018c720f0273167317",
      |            "address": "5KevXenwFg8ksGg2cMc9iEx9jvmrqfBMhfzwE8PayScttVSP2WU77QDfZCysRtHqSaKZBTbmGg5CRq5wNXbtS3XY3jW3DpAiY96iYEZsvyoTgw17CEHTumGxFAwi1xuQcsgAzA5Vh3UmJhAbkdpwx6KbZEXY9twVAtJgbN7YZiZgtGtWoCSh1fRyE8KVrRYFkyStBEBmgRCuh8k2kHVDYht8kZ441eSZ1jQF8hmtiuDVNbCYjcUBuYtzCn4QboKsfaH69sQT32DydHDufirm3yorL4DSpoUUP6PbtrgyEWdvMeU1tEH9F2q5P98jfz1QKofuCsWQmLjk2jNg7Ex7YcCaWhcu3PcMM8NL51cBsgZSgsiMfQrMuhwW6gjuufk8YHNAh4uZbb5DRZtYiTv37yEoyEKBKQrKGBpTm3m8FiAESzQtCDNMg1oBfvjfdtjm5Pt4CyUrcSHS442dGGHQ4X53sMnhXw8w6fpa3n54tTMMvar1xRnrWX1x6wxPQ4jtYg41pUrYWbtiPnvFuW8KzjPstMSNKGttHyezEy7qEyjP9uvfnqdxgJTyMe4NuX71TTTtQZ2EZnGBRVF1KW7b4sLc83VfsRmgL2hdkyioouYaMufqqMGnUp7pbBLTf6HhLnNCWHswfvETZWkyVQ5Hr1T9u8gXTvo2UajFz3GTNgK74AJXnWyedN77RnmeSwgBHDCXSkkDeTKnSxsvBoa2cQMY54rFzhZAzYYhNyWdfRUGenciGKznVnst38FxUdbSosvyx8pjrmCTVY8NkSorPKjQ5",
      |            "assets": [
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 0,
      |                    "amount": 2137,
      |                    "name": "SigRSV",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "61cfc5d5379e61665e87efe291baef8fedd3c28fcd5a4650c6c8297e8fbfcaff",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "62c91e5d96828e182d0f1a6328301cdf42e11f783984babc1660549cd2922db4",
      |            "transactionId": "d934ad8c2c83d1591e9995500a8ea5e47fca1c18f8f27e4ffe8f772264820434",
      |            "blockId": "598a9d7f0801a366d1020cc2650bc385b1dd5cfcc584c20a06792393a9f680c9",
      |            "value": 373157975,
      |            "index": 1,
      |            "globalIndex": 24773569,
      |            "creationHeight": 900247,
      |            "settlementHeight": 900249,
      |            "ergoTree": "0008cd02413d9de2ebe606be3f1120db10f29d53e91f0d20572db092ea536fffb1f73bf6",
      |            "address": "9f1o11H7cuD95sgzTSBgA5GEWKx9aBLMt98if1vsxtEao1RaL22",
      |            "assets": [
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 0,
      |                    "amount": 8291132,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "1fd6e032e8476c4aa54c18c1a308dce83940e8f4a28f576440513ed7326ad489",
      |                    "index": 1,
      |                    "amount": 10644424,
      |                    "name": "Paideia",
      |                    "decimals": 4,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "4dcda5b0c8e60ee5bb23c53edc43555cca02aeaa066901ecaa9f57d3b89c191e",
      |                    "index": 2,
      |                    "amount": 1,
      |                    "name": "ergopad Stake Key",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "3813fe93744a19d837a5367cd7a4a28a7e5e29816a50ff99b7f36c61580eeb5f",
      |                    "index": 3,
      |                    "amount": 1,
      |                    "name": "EGIO Stake Key",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "3c3e2a348be7486727926d24f9d1a652a2222b9c168c511c51f671cf48ae5e19",
      |                    "index": 4,
      |                    "amount": 1,
      |                    "name": "ergopad Stake Key",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "6fd4227cd53448bd734b90157062e1cbaa0d07b1752e63bfd1bb52dbd6cc581e",
      |                    "index": 5,
      |                    "amount": 1,
      |                    "name": "ergopad Stake Key",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "91fbb7df388a78957e9c8d0bbce247702657093f02d6039e8d3031611c17ac99",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "a4ce299e73de81db3840b6653f283573fcdbad1ebbd2d4b0d8bf75f1455ce08d",
      |            "transactionId": "d934ad8c2c83d1591e9995500a8ea5e47fca1c18f8f27e4ffe8f772264820434",
      |            "blockId": "598a9d7f0801a366d1020cc2650bc385b1dd5cfcc584c20a06792393a9f680c9",
      |            "value": 2000000,
      |            "index": 2,
      |            "globalIndex": 24773570,
      |            "creationHeight": 900247,
      |            "settlementHeight": 900249,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "b95c65e875e9f9b726a0c8626ca9fd05cb941a133de85f2089d4a73fcc63e4f4",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 1444
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  def depositEvaluateTransaction = decode[TransactionTest](
    """{
      |    "id": "61cfc5d5379e61665e87efe291baef8fedd3c28fcd5a4650c6c8297e8fbfcaff",
      |    "blockId": "598a9d7f0801a366d1020cc2650bc385b1dd5cfcc584c20a06792393a9f680c9",
      |    "inclusionHeight": 900249,
      |    "timestamp": 1671543753053,
      |    "index": 3,
      |    "globalIndex": 4496190,
      |    "numConfirmations": 21568,
      |    "inputs": [
      |        {
      |            "boxId": "585d591eb1a6488b5933c7aec4cebe32c889b6c016e83cc26912a5513d29e6a7",
      |            "value": 39688285739051,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "fdf65c72f5725b40385d4006b44e4085471f718fe4057d96eef0ae1f0e1e532f",
      |            "outputTransactionId": "b8b6fb47e4a015aeed07b8ac6e275390ae5f7c807e6c94f77ebc8b2c0df2b36c",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 24759099,
      |            "outputCreatedAt": 899968,
      |            "outputSettledAt": 899970,
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
      |                    "amount": 9223372009701369859,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 2,
      |                    "amount": 88941232,
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
      |            "boxId": "004fc3740dde5f52efe1035059762410c70ed88f00aaad93b5b75d68640610bc",
      |            "value": 959654465,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "598a9d7f0801a366d1020cc2650bc385b1dd5cfcc584c20a06792393a9f680c9",
      |            "outputTransactionId": "d934ad8c2c83d1591e9995500a8ea5e47fca1c18f8f27e4ffe8f772264820434",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 24773568,
      |            "outputCreatedAt": 900247,
      |            "outputSettledAt": 900249,
      |            "ergoTree": "19bd041808cd02413d9de2ebe606be3f1120db10f29d53e91f0d20572db092ea536fffb1f73bf6040005c2c9b58d07040404060402040205feffffffffffffffff0104040400040004000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f040404020580b6dc0505c2c9b58d070580b6dc0501000e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d80bd604db63087202d605b2a5730500d606b27204730600d6077e9973078c72060206d6087ec1720206d6099d9c7e72030672077208d60ab27204730800d60b7e8c720a0206d60c9d9c7e8cb2db6308a773090002067207720bd60ddb63087205d60eb2720d730a00ededededed938cb27204730b0001730c93c27205d0720195ed8f7209720c93b1720d730dd801d60fb2720d730e00eded92c172059999c1a7730f7310938c720f018c720a01927e8c720f02069d9c99720c7209720b720795927209720c927ec1720506997e99c1a7731106997e7203069d9c997209720c720872077312938c720e018c720601927e8c720e0206a17209720c90b0ada5d9010f639593c2720f7313c1720f73147315d9010f599a8c720f018c720f0273167317",
      |            "address": "5KevXenwFg8ksGg2cMc9iEx9jvmrqfBMhfzwE8PayScttVSP2WU77QDfZCysRtHqSaKZBTbmGg5CRq5wNXbtS3XY3jW3DpAiY96iYEZsvyoTgw17CEHTumGxFAwi1xuQcsgAzA5Vh3UmJhAbkdpwx6KbZEXY9twVAtJgbN7YZiZgtGtWoCSh1fRyE8KVrRYFkyStBEBmgRCuh8k2kHVDYht8kZ441eSZ1jQF8hmtiuDVNbCYjcUBuYtzCn4QboKsfaH69sQT32DydHDufirm3yorL4DSpoUUP6PbtrgyEWdvMeU1tEH9F2q5P98jfz1QKofuCsWQmLjk2jNg7Ex7YcCaWhcu3PcMM8NL51cBsgZSgsiMfQrMuhwW6gjuufk8YHNAh4uZbb5DRZtYiTv37yEoyEKBKQrKGBpTm3m8FiAESzQtCDNMg1oBfvjfdtjm5Pt4CyUrcSHS442dGGHQ4X53sMnhXw8w6fpa3n54tTMMvar1xRnrWX1x6wxPQ4jtYg41pUrYWbtiPnvFuW8KzjPstMSNKGttHyezEy7qEyjP9uvfnqdxgJTyMe4NuX71TTTtQZ2EZnGBRVF1KW7b4sLc83VfsRmgL2hdkyioouYaMufqqMGnUp7pbBLTf6HhLnNCWHswfvETZWkyVQ5Hr1T9u8gXTvo2UajFz3GTNgK74AJXnWyedN77RnmeSwgBHDCXSkkDeTKnSxsvBoa2cQMY54rFzhZAzYYhNyWdfRUGenciGKznVnst38FxUdbSosvyx8pjrmCTVY8NkSorPKjQ5",
      |            "assets": [
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 0,
      |                    "amount": 2137,
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
      |            "boxId": "66af097edcf0c8197e4d7c3c22c28b1d2cb39c4ae626b61676a08bd9f7a93df6",
      |            "transactionId": "61cfc5d5379e61665e87efe291baef8fedd3c28fcd5a4650c6c8297e8fbfcaff",
      |            "blockId": "598a9d7f0801a366d1020cc2650bc385b1dd5cfcc584c20a06792393a9f680c9",
      |            "value": 39689239333516,
      |            "index": 0,
      |            "globalIndex": 24773571,
      |            "creationHeight": 900247,
      |            "settlementHeight": 900249,
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
      |                    "amount": 9223372009700717442,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 2,
      |                    "amount": 88943369,
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
      |            "spentTransactionId": "1be0be2cdf4d5a2a650e28dd82d83ffc849b750354fd47e9c2e5c42b79ce509b",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "75f6a137be7f2f25138e48bbf0a66c0cbeab30c26f25a56e9f7a98904b2cb3e7",
      |            "transactionId": "61cfc5d5379e61665e87efe291baef8fedd3c28fcd5a4650c6c8297e8fbfcaff",
      |            "blockId": "598a9d7f0801a366d1020cc2650bc385b1dd5cfcc584c20a06792393a9f680c9",
      |            "value": 60000,
      |            "index": 1,
      |            "globalIndex": 24773572,
      |            "creationHeight": 900247,
      |            "settlementHeight": 900249,
      |            "ergoTree": "0008cd02413d9de2ebe606be3f1120db10f29d53e91f0d20572db092ea536fffb1f73bf6",
      |            "address": "9f1o11H7cuD95sgzTSBgA5GEWKx9aBLMt98if1vsxtEao1RaL22",
      |            "assets": [
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 0,
      |                    "amount": 652417,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "91fbb7df388a78957e9c8d0bbce247702657093f02d6039e8d3031611c17ac99",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "f5444dfa3e2d8d09895f237ec9ff2febc49e0fd02b48384fba18458506a675da",
      |            "transactionId": "61cfc5d5379e61665e87efe291baef8fedd3c28fcd5a4650c6c8297e8fbfcaff",
      |            "blockId": "598a9d7f0801a366d1020cc2650bc385b1dd5cfcc584c20a06792393a9f680c9",
      |            "value": 4000000,
      |            "index": 2,
      |            "globalIndex": 24773573,
      |            "creationHeight": 900247,
      |            "settlementHeight": 900249,
      |            "ergoTree": "0008cd02fe552aed1237bf4f8a02aaa772a68bbbef5be1ee97abde42820c7386cc64a3c7",
      |            "address": "9gT56ssLNiUFwy58XrXeAjYz76etN6W1xVFHvm4KpnrTLcEEoVD",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "04f177bc00441af8910a56244d4fca0419a34e5dfaa5a487e15d558f2273b554",
      |            "transactionId": "61cfc5d5379e61665e87efe291baef8fedd3c28fcd5a4650c6c8297e8fbfcaff",
      |            "blockId": "598a9d7f0801a366d1020cc2650bc385b1dd5cfcc584c20a06792393a9f680c9",
      |            "value": 2000000,
      |            "index": 3,
      |            "globalIndex": 24773574,
      |            "creationHeight": 900247,
      |            "settlementHeight": 900249,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "b95c65e875e9f9b726a0c8626ca9fd05cb941a133de85f2089d4a73fcc63e4f4",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 818
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  def depositEval = AmmDepositEvaluation(
    AssetAmount(
      TokenId.unsafeFromString("fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6"),
      652417
    ), 10, 11
  )

  def depositPool = PoolParser.make.parse(
    decode[TransactionTest](
      """{
      |    "id": "b8b6fb47e4a015aeed07b8ac6e275390ae5f7c807e6c94f77ebc8b2c0df2b36c",
      |    "blockId": "fdf65c72f5725b40385d4006b44e4085471f718fe4057d96eef0ae1f0e1e532f",
      |    "inclusionHeight": 899970,
      |    "timestamp": 1671505672067,
      |    "index": 30,
      |    "globalIndex": 4493457,
      |    "numConfirmations": 21867,
      |    "inputs": [
      |        {
      |            "boxId": "0fe0c5a22584709a73b9c0c3e5b214bc98594d39bce9ee4928caaa628542b203",
      |            "value": 39688173621164,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "77edd699febe5247ab2e4745467fd5155ce8eeae33a651b864cce2a08b95daca",
      |            "outputTransactionId": "df9df0e0a5bc721094b672162299957287254f0b8f355bfae0464df1c837592e",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 24757488,
      |            "outputCreatedAt": 899937,
      |            "outputSettledAt": 899939,
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
      |                    "amount": 9223372009701369859,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 2,
      |                    "amount": 88941482,
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
      |            "boxId": "cdbb26ea22dc1cab66ecbdf50d5322da95c5e5b989cf60399714e367bf7de407",
      |            "value": 119377887,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "fdf65c72f5725b40385d4006b44e4085471f718fe4057d96eef0ae1f0e1e532f",
      |            "outputTransactionId": "6586167950d249f2458432217c24414e5af673704c05f2782ec6c986b1c516f8",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 24759040,
      |            "outputCreatedAt": 899967,
      |            "outputSettledAt": 899970,
      |            "ergoTree": "19f6031808cd03fb5a2f8b1b43eee922902a9f33105a3249f9c5001ddf148abbebcf09f78e5aec040005fea1f66a040404060402040004000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f0e20003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d005e4030586dc91e292dd8a580580c0a8ca9a3a040404c60f06010104d00f05fea1f66a04c60f0e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d803d6017300d602b2a4730100d6037302eb027201d195ed93b1a4730393b1db630872027304d804d604db63087202d605b2a5730500d606b2db63087205730600d6077e8c72060206edededededed938cb2720473070001730893c27205d07201938c72060173099272077e730a06927ec172050699997ec1a7069d9c72077e730b067e730c067e720306909c9c7e8cb27204730d0002067e7203067e730e069c9a7207730f9a9c7ec17202067e7310067e9c73117e7312050690b0ada5d90108639593c272087313c1720873147315d90108599a8c7208018c72080273167317",
      |            "address": "63sRgge7knVAKUUHrSHhJyhn8WMTcnCoKuAWrQE2cy7dR514oX6bNDeUjeG7HDxb2X9fstFPV3U4Hks6mvv96xhCbmrra318ZxtNxED9YBL8Fc54mzjFr2jxEjvyc6nTVJoqLQbCpBWe9uvJyQS8ZQ1Cmz9sT8BnL6fYj5a4d5Dzjqnra6Laavrkz2Gy7qc74SwkHcPNoodNiVEthatVS4WkQHX2HzFyaRx9goDdupMwWzvhKV26f7BJAipoGjttr9Hq6eRVspDYpXrHDWcnAWNuTQs1A8ejoio6cRMZpkS1jDDgUGXY3MW9nVuSu6KZVAMjxRBj93ngjQsBJiiaZXsKyZ4QnUgQsZWc8q8HTdxiQhWKFNXiKdRkajFwi436kHCHmEwEv8nk4vz2z4KwFyAy3p2LkdoX8CLXXwHSwQh3oDAEH3Lv38LQrbCKqgYKet1V4uJZMXLF5dG8SunSVhHkwn8kuFRuP4H7T3aG5qZg8nBGNTxsywRi4fNGmFn2nSwhNBgeJop2v7h2EMqiiJRpfQnchuDBLxnnnjBdmiYH48GPM4vTSbAPPWhnknK4rExQBhFSG42ptC9BzSbKir6nFCkVou5uRULLamVxwD3r4YXb3LFQnDt12mm6GBbWd7QoA2RiguhpwgPWyZSmUc4gpzEmW7ZLEJPL7Jim5H9Qu6f3uJJmfGtt",
      |            "assets": [],
      |            "additionalRegisters": {}
      |        }
      |    ],
      |    "dataInputs": [],
      |    "outputs": [
      |        {
      |            "boxId": "585d591eb1a6488b5933c7aec4cebe32c889b6c016e83cc26912a5513d29e6a7",
      |            "transactionId": "b8b6fb47e4a015aeed07b8ac6e275390ae5f7c807e6c94f77ebc8b2c0df2b36c",
      |            "blockId": "fdf65c72f5725b40385d4006b44e4085471f718fe4057d96eef0ae1f0e1e532f",
      |            "value": 39688285739051,
      |            "index": 0,
      |            "globalIndex": 24759099,
      |            "creationHeight": 899968,
      |            "settlementHeight": 899970,
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
      |                    "amount": 9223372009701369859,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 2,
      |                    "amount": 88941232,
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
      |            "spentTransactionId": "61cfc5d5379e61665e87efe291baef8fedd3c28fcd5a4650c6c8297e8fbfcaff",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "1eb1cab62a4dcde10c66e1b876105d162763a0543550be99e6ae51fbbfcdc30e",
      |            "transactionId": "b8b6fb47e4a015aeed07b8ac6e275390ae5f7c807e6c94f77ebc8b2c0df2b36c",
      |            "blockId": "fdf65c72f5725b40385d4006b44e4085471f718fe4057d96eef0ae1f0e1e532f",
      |            "value": 1061653,
      |            "index": 1,
      |            "globalIndex": 24759100,
      |            "creationHeight": 899968,
      |            "settlementHeight": 899970,
      |            "ergoTree": "0008cd03fb5a2f8b1b43eee922902a9f33105a3249f9c5001ddf148abbebcf09f78e5aec",
      |            "address": "9iNW9Z5HFj3qo35twwDbAWMutsedwfbsxHNq1ufSXgm5xGrfUKX",
      |            "assets": [
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 0,
      |                    "amount": 250,
      |                    "name": "SigRSV",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "e4b19c07f2547da0a13633ed5de8113a2b047848903efe72b2109591dabe0f49",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "261df6b3ecfadf557e2bc67ef6c713f98b11dedf7e7f5d30dcb1e27117032548",
      |            "transactionId": "b8b6fb47e4a015aeed07b8ac6e275390ae5f7c807e6c94f77ebc8b2c0df2b36c",
      |            "blockId": "fdf65c72f5725b40385d4006b44e4085471f718fe4057d96eef0ae1f0e1e532f",
      |            "value": 4198347,
      |            "index": 2,
      |            "globalIndex": 24759101,
      |            "creationHeight": 899968,
      |            "settlementHeight": 899970,
      |            "ergoTree": "0008cd0325a8e46f0c1c674462c6de11ddd90c1c4e436ca1d0ecaf65c7a0932a50a68817",
      |            "address": "9gkPfAay2Ehs8Bk7JFPSJGT4izD13GzNKMbyq6Hd3PbCEEMYSNR",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "5f2469a5403b0b1cd8bca6f54ae9787380fcaaac108ff7b70c83809d336d4518",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "d26a5f660ff6e2da5b75d339ee75fca631fd70b10b75055cd6e6a64b33cc9cab",
      |            "transactionId": "b8b6fb47e4a015aeed07b8ac6e275390ae5f7c807e6c94f77ebc8b2c0df2b36c",
      |            "blockId": "fdf65c72f5725b40385d4006b44e4085471f718fe4057d96eef0ae1f0e1e532f",
      |            "value": 2000000,
      |            "index": 3,
      |            "globalIndex": 24759102,
      |            "creationHeight": 899968,
      |            "settlementHeight": 899970,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "a7c82773fb3335eb6de6b65211120c66825b9b365a07baacbee5b9eb3f472d7e",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 817
      |}""".stripMargin
    ).toOption.get.toTransaction.outputs.head,
    0, 10
  )

  def swapRegisterRefundTransaction = decode[TransactionTest](
    """
      |{
      |    "id": "7b0b2249013b9b77790cb2a5a1955c10dd6cbee59f945a4d962eb1d091d8fe69",
      |    "blockId": "6292f85a7b02146e101485d5c7420655f16239c0acb5b7638d1927d528fda461",
      |    "inclusionHeight": 552620,
      |    "timestamp": 1628719367332,
      |    "index": 4,
      |    "globalIndex": 1616508,
      |    "numConfirmations": 369209,
      |    "inputs": [
      |        {
      |            "boxId": "30f80e88787f2970266af622157c63a0ea3416dfa5270ba2b34dbfd2b36080a5",
      |            "value": 5000000000,
      |            "index": 0,
      |            "spendingProof": "669c77ce6f854d49a6b235268c847105b16ec5000f42ce21bd2ad8f9d5c10d3eda82b92d856fe6b8a3c6583638616855478d865c098b4238",
      |            "outputBlockId": "c6b843dd3ed58b6bf834a2fcf4154c933f2a76317274707c49631297c27bf399",
      |            "outputTransactionId": "253151d075975a40c784b7e751038bf432b38d49b096d4934cd5388d5cd4322a",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 6181419,
      |            "outputCreatedAt": 552607,
      |            "outputSettledAt": 552609,
      |            "ergoTree": "0008cd02417a11ae2447b6d7cb66bb6095fb1d2207e24d16b36f747ecc622161d1ade4ea",
      |            "address": "9f1u2rgVwT7vNGxEkx8vsJVDgQSkiGzT3wWza4FvjJSxV6ipmpA",
      |            "assets": [],
      |            "additionalRegisters": {}
      |        },
      |        {
      |            "boxId": "67871b3e3e43ae3bfb359eab2a6c1db101c2cf258d6b514fcc91aae93cd7ef6e",
      |            "value": 10000000,
      |            "index": 1,
      |            "spendingProof": "f18fa6c06c13c5c1ae6557251a468688e27291e71ae2d7d7def2c85da5680c73a7324ebccbfdae4161de175d6a54b9cea13b9dbcbeb38f8a",
      |            "outputBlockId": "0cbfe3d7e336013119c20d6c5990985baa0a4d7b24889f233d86867e9416ec83",
      |            "outputTransactionId": "87359d906a103f63e41f16d0da217c8730ede0d0f686d709ed282a96ea6302ab",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 6181461,
      |            "outputCreatedAt": 552608,
      |            "outputSettledAt": 552610,
      |            "ergoTree": "0008cd02417a11ae2447b6d7cb66bb6095fb1d2207e24d16b36f747ecc622161d1ade4ea",
      |            "address": "9f1u2rgVwT7vNGxEkx8vsJVDgQSkiGzT3wWza4FvjJSxV6ipmpA",
      |            "assets": [
      |                {
      |                    "tokenId": "fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40",
      |                    "index": 0,
      |                    "amount": 100000,
      |                    "name": "kushti",
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
      |            "boxId": "06602094c7fc27753b105c7cca432b488bc7d874e316aa0a857ec255a7423fed",
      |            "transactionId": "7b0b2249013b9b77790cb2a5a1955c10dd6cbee59f945a4d962eb1d091d8fe69",
      |            "blockId": "6292f85a7b02146e101485d5c7420655f16239c0acb5b7638d1927d528fda461",
      |            "value": 16000000,
      |            "index": 0,
      |            "globalIndex": 6182169,
      |            "creationHeight": 508928,
      |            "settlementHeight": 552620,
      |            "ergoTree": "199c031308cd02417a11ae2447b6d7cb66bb6095fb1d2207e24d16b36f747ecc622161d1ade4ea04000e2036aba4b4a97b65be491cf9f5ca57b5408b0da8d0194f30ec8330d1e8946161c104c80f04d00f040404080402040004040400040606010104000e2065fa572bc4a7007e5a6450c9af2bfa1594e6dfb43b667027f1930eefddeac7bf05d00905daf2ebede390eb1c0580c0a8ca9a3a0100d805d6017300d602b2a4730100d6037302d6047303d6057304eb027201d195ed93b1a4730593b1db630872027306d80ad606db63087202d607b2a5730700d608b2db63087207730800d6098c720802d60a7e720906d60bb27206730900d60c7e8c720b0206d60d7e8cb2db6308a7730a000206d60e7e8cb27206730b000206d60f9a720a730cededededed938cb27206730d0001730e93c27207d07201938c7208017203927209730f927ec1720706997ec1a7069d9c720a7e7310067e73110695938c720b017203909c9c720c720d7e7204069c720f9a9c720e7e7205069c720d7e720406909c9c720e720d7e7204069c720f9a9c720c7e7205069c720d7e7204067312",
      |            "address": "8JBbtKAXNiufTvfs4RA1QNrwzUUnKRqfgVGJHCxKiby6kYosi6gtf9HZD5xothhCB9EoVGppZAUWjk2h5GcYDEsvrttL2b36QK2dqvS2LpN7hzTWynBYb2aogSjrFZJBy3kDFB5h6zGmeWnzSNTchPKVDE1kvpvBbD8WsHMUhtpeCHtu8Y4RF4zMSxaLzSsd4a6bsWUgBXe3aHpfdD7oFBeExo82xeEWU2pW9kyFuHw8M1rJcS9yRkv5aoFq5qoGAmnQWdQGKMxq5AGNobDdH3Sn2At7crhu2aUnqU2CKnDAejJPCtxNthnb8Ywz5sEbftK1mva9fjuU938oiDkmFvQWZLShkzAtohvy8FSFBjyuM3EpepAGTgarusmrpiKJM3c7zvnqvS7CwcnJtg6dGjtRbkbnNAAyLer2WXvgTDGq6Vy1SSKEWfsfELQnww6VjbEDehkZDs5Qbj8QQch5bqtosK4UwBDq8Hs5JtrMQDBrk6jGgru8ZoE4jWg4TfMXyFKknCpwqV4EzQAhbaubUaedvSDtU6qe2c76vhEeNbCjsmWsNnkPhdkFbSgoh",
      |            "assets": [
      |                {
      |                    "tokenId": "fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40",
      |                    "index": 0,
      |                    "amount": 100000,
      |                    "name": "kushti",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "9bef2a720d125a704ca869842374265ded1c83b631c2d828d326bc82b66cb813",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "1b1f67c2afffc56f1418d1360fda338169002036243fbc7996e4b0f9254ac377",
      |            "transactionId": "7b0b2249013b9b77790cb2a5a1955c10dd6cbee59f945a4d962eb1d091d8fe69",
      |            "blockId": "6292f85a7b02146e101485d5c7420655f16239c0acb5b7638d1927d528fda461",
      |            "value": 4984000000,
      |            "index": 1,
      |            "globalIndex": 6182170,
      |            "creationHeight": 508928,
      |            "settlementHeight": 552620,
      |            "ergoTree": "0008cd02417a11ae2447b6d7cb66bb6095fb1d2207e24d16b36f747ecc622161d1ade4ea",
      |            "address": "9f1u2rgVwT7vNGxEkx8vsJVDgQSkiGzT3wWza4FvjJSxV6ipmpA",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "7d29b6a716dac718035fac85abdc43b92f71e8b837fad922a2bf635f8e125a7d",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "db8dd97531a8bc370eeec58cd94f5db81631cf4c0b2577452a93fc9c8bc4739b",
      |            "transactionId": "7b0b2249013b9b77790cb2a5a1955c10dd6cbee59f945a4d962eb1d091d8fe69",
      |            "blockId": "6292f85a7b02146e101485d5c7420655f16239c0acb5b7638d1927d528fda461",
      |            "value": 10000000,
      |            "index": 2,
      |            "globalIndex": 6182171,
      |            "creationHeight": 508928,
      |            "settlementHeight": 552620,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "3720f5b83f1b11ce020add8bc5e2a12ddbffa380790adce5a50c0c08028206fd",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 804
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  def swapRefundTransaction = decode[TransactionTest](
    """
      |{
      |    "id": "9bef2a720d125a704ca869842374265ded1c83b631c2d828d326bc82b66cb813",
      |    "blockId": "e122d9d3bb91609e62a21db9f444cf684b430e06c550b93f564cc68d4f118cfb",
      |    "inclusionHeight": 553040,
      |    "timestamp": 1628772889074,
      |    "index": 2,
      |    "globalIndex": 1619574,
      |    "numConfirmations": 368789,
      |    "inputs": [
      |        {
      |            "boxId": "06602094c7fc27753b105c7cca432b488bc7d874e316aa0a857ec255a7423fed",
      |            "value": 16000000,
      |            "index": 0,
      |            "spendingProof": "a22ea9b1803792d38ca0a7f7baad67659adecace51035621548d5c8ea7e958a45b32bb6549af608dd427aeeb2298ee1faafe60d9bbacf173",
      |            "outputBlockId": "6292f85a7b02146e101485d5c7420655f16239c0acb5b7638d1927d528fda461",
      |            "outputTransactionId": "7b0b2249013b9b77790cb2a5a1955c10dd6cbee59f945a4d962eb1d091d8fe69",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 6182169,
      |            "outputCreatedAt": 508928,
      |            "outputSettledAt": 552620,
      |            "ergoTree": "199c031308cd02417a11ae2447b6d7cb66bb6095fb1d2207e24d16b36f747ecc622161d1ade4ea04000e2036aba4b4a97b65be491cf9f5ca57b5408b0da8d0194f30ec8330d1e8946161c104c80f04d00f040404080402040004040400040606010104000e2065fa572bc4a7007e5a6450c9af2bfa1594e6dfb43b667027f1930eefddeac7bf05d00905daf2ebede390eb1c0580c0a8ca9a3a0100d805d6017300d602b2a4730100d6037302d6047303d6057304eb027201d195ed93b1a4730593b1db630872027306d80ad606db63087202d607b2a5730700d608b2db63087207730800d6098c720802d60a7e720906d60bb27206730900d60c7e8c720b0206d60d7e8cb2db6308a7730a000206d60e7e8cb27206730b000206d60f9a720a730cededededed938cb27206730d0001730e93c27207d07201938c7208017203927209730f927ec1720706997ec1a7069d9c720a7e7310067e73110695938c720b017203909c9c720c720d7e7204069c720f9a9c720e7e7205069c720d7e720406909c9c720e720d7e7204069c720f9a9c720c7e7205069c720d7e7204067312",
      |            "address": "8JBbtKAXNiufTvfs4RA1QNrwzUUnKRqfgVGJHCxKiby6kYosi6gtf9HZD5xothhCB9EoVGppZAUWjk2h5GcYDEsvrttL2b36QK2dqvS2LpN7hzTWynBYb2aogSjrFZJBy3kDFB5h6zGmeWnzSNTchPKVDE1kvpvBbD8WsHMUhtpeCHtu8Y4RF4zMSxaLzSsd4a6bsWUgBXe3aHpfdD7oFBeExo82xeEWU2pW9kyFuHw8M1rJcS9yRkv5aoFq5qoGAmnQWdQGKMxq5AGNobDdH3Sn2At7crhu2aUnqU2CKnDAejJPCtxNthnb8Ywz5sEbftK1mva9fjuU938oiDkmFvQWZLShkzAtohvy8FSFBjyuM3EpepAGTgarusmrpiKJM3c7zvnqvS7CwcnJtg6dGjtRbkbnNAAyLer2WXvgTDGq6Vy1SSKEWfsfELQnww6VjbEDehkZDs5Qbj8QQch5bqtosK4UwBDq8Hs5JtrMQDBrk6jGgru8ZoE4jWg4TfMXyFKknCpwqV4EzQAhbaubUaedvSDtU6qe2c76vhEeNbCjsmWsNnkPhdkFbSgoh",
      |            "assets": [
      |                {
      |                    "tokenId": "fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40",
      |                    "index": 0,
      |                    "amount": 100000,
      |                    "name": "kushti",
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
      |            "boxId": "058ef71cbca04aba0cf764287ec2749c7743df3cc614a61969e65d4cca3860d2",
      |            "transactionId": "9bef2a720d125a704ca869842374265ded1c83b631c2d828d326bc82b66cb813",
      |            "blockId": "e122d9d3bb91609e62a21db9f444cf684b430e06c550b93f564cc68d4f118cfb",
      |            "value": 6000000,
      |            "index": 0,
      |            "globalIndex": 6199788,
      |            "creationHeight": 506880,
      |            "settlementHeight": 553040,
      |            "ergoTree": "0008cd02417a11ae2447b6d7cb66bb6095fb1d2207e24d16b36f747ecc622161d1ade4ea",
      |            "address": "9f1u2rgVwT7vNGxEkx8vsJVDgQSkiGzT3wWza4FvjJSxV6ipmpA",
      |            "assets": [
      |                {
      |                    "tokenId": "fbbaac7337d051c10fc3da0ccb864f4d32d40027551e1c3ea3ce361f39b91e40",
      |                    "index": 0,
      |                    "amount": 100000,
      |                    "name": "kushti",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "26ade909b1378d41af7560c323476c5995b60b1dc2126d19f2529c6c3be93aaa",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "da4ab2beab8b6cf08588dc3496dc7f90ea84d5ff670dd400c0b1fdad61330f17",
      |            "transactionId": "9bef2a720d125a704ca869842374265ded1c83b631c2d828d326bc82b66cb813",
      |            "blockId": "e122d9d3bb91609e62a21db9f444cf684b430e06c550b93f564cc68d4f118cfb",
      |            "value": 10000000,
      |            "index": 1,
      |            "globalIndex": 6199789,
      |            "creationHeight": 506880,
      |            "settlementHeight": 553040,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "aeace25de81016a4dd731005f0df5650d44672c5cb141f5a112cd7bb12873c75",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 289
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  def depositRegisterRefundTransaction = decode[TransactionTest](
    """
      |{
      |    "id": "a8dc6799c0a16373f97d0575897d7e8ceb11228a994f726a7ece8cbd087b89f1",
      |    "blockId": "0c0f5d5205415c22583db2537c1ec6ab0c099e001ccc6552c6a78f0efa185ed9",
      |    "inclusionHeight": 603924,
      |    "timestamp": 1634999850646,
      |    "index": 1,
      |    "globalIndex": 1987081,
      |    "numConfirmations": 317913,
      |    "inputs": [
      |        {
      |            "boxId": "8c36f393639c8acf804bff7fdec8659da5b194a3a64b3db9f8ba5ea8a721a3e2",
      |            "value": 42648081,
      |            "index": 0,
      |            "spendingProof": "6c26aa655932087c03fd3ee509e041be7aec9bbd0c3b0b13b6fa6558dd58b39e0f51bb52f09e4d59583bf4066bc8bddce00b32a325fa39dc",
      |            "outputBlockId": "279e7922ad1542df367e32db3929fed571e16bc62c414883d583abce4949e3a4",
      |            "outputTransactionId": "938f3f586ed050942dfd52dee2d981099911f3d2124efeebcdbe4e70a722485f",
      |            "outputIndex": 2,
      |            "outputGlobalIndex": 8738275,
      |            "outputCreatedAt": 508928,
      |            "outputSettledAt": 603922,
      |            "ergoTree": "0008cd03f9687cd6c90cb7721c181ca0b67a47fc5e1c01187f129b4faa5fedabf6ef6436",
      |            "address": "9iMeVFvixhy2JMHyyEwRRPvkgJyx2pd6tRWT6P29CATB9HZzBhw",
      |            "assets": [
      |                {
      |                    "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
      |                    "index": 0,
      |                    "amount": 34113289,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 1,
      |                    "amount": 1020903086029,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 2,
      |                    "amount": 9696950981134,
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
      |            "boxId": "005f2cad2fe126d24e1779cc9a242b273b788b305260dc50a0602e814f471b2b",
      |            "transactionId": "a8dc6799c0a16373f97d0575897d7e8ceb11228a994f726a7ece8cbd087b89f1",
      |            "blockId": "0c0f5d5205415c22583db2537c1ec6ab0c099e001ccc6552c6a78f0efa185ed9",
      |            "value": 7050000,
      |            "index": 0,
      |            "globalIndex": 8738328,
      |            "creationHeight": 506880,
      |            "settlementHeight": 603924,
      |            "ergoTree": "19aa021108cd03f9687cd6c90cb7721c181ca0b67a47fc5e1c01187f129b4faa5fedabf6ef643604000404040804020400040205feffffffffffffffff0104000e20f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc78105020580bfd60604000404040204060100d803d6017300d602b2a4730100d603db6308a7eb027201d195ed93b1a4730293b1db630872027303d805d604db63087202d605b2a5730400d606b2db63087205730500d607b27204730600d6087e9973078c72070206edededed93b2720473080086027309730a93c27205d0720192c1720599c1a7730b938c7206018c720701927e8c72060206a19d9c7e8cb27203730c00020672087e8cb27204730d0002069d9c7e8cb27203730e00020672087e8cb27204730f0002067310",
      |            "address": "TDSS8J9hjXK5ewhgdRjbh4pBc7Dk4PVmePuj23WjoWC6dtCFwCe8PWvDAPDRMmtXupxTuKDvrVZfqGbEreGBtmUh4pdvXu1JyWwb5SJy6ZeLWrqGDmzTCV75f89swxTBYoidZ841XYMXT41Sfcxzz4a2LVdsafJthQc3km6kAbHi48CZpFpM5JdqZPhTC5e3YXdwUUoAaoKUNk8ug5TUCGL1qmwUze6KsA8Di4m5xUrAmpvFwwxUx8CQ52rJc44stE5vMVYnZ7Fw5dvRSLgqDn2scGnkZPM1UZavHA5LN8PY5WqKdtJJGRRj5gjKzuz7NSUpvyQoJLt8MALknzJXFDZNzWyBfSyyNx1sbDn8c6SE2eVvCk79Sor479pLsHQb5n2zsc5515yDYTtTgJ24y2aJXr5ZVnU34",
      |            "assets": [
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 0,
      |                    "amount": 565000000000,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 1,
      |                    "amount": 335985320975,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "ec115f6f9a153af1515c210c39704831cbab73deb300b7fb81a2926816b8bd99",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "1705beb9f5149f75822341ffadb97e3b7decb2a3b8952e1ea43389565dcf8494",
      |            "transactionId": "a8dc6799c0a16373f97d0575897d7e8ceb11228a994f726a7ece8cbd087b89f1",
      |            "blockId": "0c0f5d5205415c22583db2537c1ec6ab0c099e001ccc6552c6a78f0efa185ed9",
      |            "value": 10000000,
      |            "index": 1,
      |            "globalIndex": 8738329,
      |            "creationHeight": 506880,
      |            "settlementHeight": 603924,
      |            "ergoTree": "0008cd02ddbe95b7f88d47bd8c2db823cc5dd1be69a650556a44d4c15ac65e1d3e34324c",
      |            "address": "9gCigPc9cZNRhKgbgdmTkVxo1ZKgw79G8DvLjCcYWAvEF3XRUKy",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "2bc9eb3caf27ef444b0faa96c871b9cbea13b627e4e55acdb33cae6549bb8b84",
      |            "transactionId": "a8dc6799c0a16373f97d0575897d7e8ceb11228a994f726a7ece8cbd087b89f1",
      |            "blockId": "0c0f5d5205415c22583db2537c1ec6ab0c099e001ccc6552c6a78f0efa185ed9",
      |            "value": 23598081,
      |            "index": 2,
      |            "globalIndex": 8738330,
      |            "creationHeight": 506880,
      |            "settlementHeight": 603924,
      |            "ergoTree": "0008cd03f9687cd6c90cb7721c181ca0b67a47fc5e1c01187f129b4faa5fedabf6ef6436",
      |            "address": "9iMeVFvixhy2JMHyyEwRRPvkgJyx2pd6tRWT6P29CATB9HZzBhw",
      |            "assets": [
      |                {
      |                    "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
      |                    "index": 0,
      |                    "amount": 34113289,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 1,
      |                    "amount": 684917765054,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 2,
      |                    "amount": 9131950981134,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "2d8ee3401ad07c7c2e7b0e627eafb0312ccc9e313ade27a2ba7d59898589737e",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "b5874389a010059914b96596b60a7f88dea84c97533bec3a71a9f3b1c71d66db",
      |            "transactionId": "a8dc6799c0a16373f97d0575897d7e8ceb11228a994f726a7ece8cbd087b89f1",
      |            "blockId": "0c0f5d5205415c22583db2537c1ec6ab0c099e001ccc6552c6a78f0efa185ed9",
      |            "value": 2000000,
      |            "index": 3,
      |            "globalIndex": 8738331,
      |            "creationHeight": 506880,
      |            "settlementHeight": 603924,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "3457524b9015eedef404004a0a80241b0695689a710788764275f0333fb45ba2",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 737
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  def depositRefundTransaction = decode[TransactionTest](
    """
      |{
      |    "id": "ec115f6f9a153af1515c210c39704831cbab73deb300b7fb81a2926816b8bd99",
      |    "blockId": "0c0f5d5205415c22583db2537c1ec6ab0c099e001ccc6552c6a78f0efa185ed9",
      |    "inclusionHeight": 603924,
      |    "timestamp": 1634999850646,
      |    "index": 4,
      |    "globalIndex": 1987084,
      |    "numConfirmations": 317913,
      |    "inputs": [
      |        {
      |            "boxId": "ff65f92cd16f8523aeee1edfa1d69fd1af491fd4b2bcfc9d74924e9237d2687c",
      |            "value": 4000000,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "279e7922ad1542df367e32db3929fed571e16bc62c414883d583abce4949e3a4",
      |            "outputTransactionId": "19d0b6ac3778dace0de9c2658044545bfa976206adbf05797eb63d9624df3768",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 8738313,
      |            "outputCreatedAt": 603920,
      |            "outputSettledAt": 603922,
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
      |                    "amount": 9223371216656708485,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 2,
      |                    "amount": 2744546774473717,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 3,
      |                    "amount": 1632083945049966,
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
      |            "boxId": "005f2cad2fe126d24e1779cc9a242b273b788b305260dc50a0602e814f471b2b",
      |            "value": 7050000,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "0c0f5d5205415c22583db2537c1ec6ab0c099e001ccc6552c6a78f0efa185ed9",
      |            "outputTransactionId": "a8dc6799c0a16373f97d0575897d7e8ceb11228a994f726a7ece8cbd087b89f1",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 8738328,
      |            "outputCreatedAt": 506880,
      |            "outputSettledAt": 603924,
      |            "ergoTree": "19aa021108cd03f9687cd6c90cb7721c181ca0b67a47fc5e1c01187f129b4faa5fedabf6ef643604000404040804020400040205feffffffffffffffff0104000e20f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc78105020580bfd60604000404040204060100d803d6017300d602b2a4730100d603db6308a7eb027201d195ed93b1a4730293b1db630872027303d805d604db63087202d605b2a5730400d606b2db63087205730500d607b27204730600d6087e9973078c72070206edededed93b2720473080086027309730a93c27205d0720192c1720599c1a7730b938c7206018c720701927e8c72060206a19d9c7e8cb27203730c00020672087e8cb27204730d0002069d9c7e8cb27203730e00020672087e8cb27204730f0002067310",
      |            "address": "TDSS8J9hjXK5ewhgdRjbh4pBc7Dk4PVmePuj23WjoWC6dtCFwCe8PWvDAPDRMmtXupxTuKDvrVZfqGbEreGBtmUh4pdvXu1JyWwb5SJy6ZeLWrqGDmzTCV75f89swxTBYoidZ841XYMXT41Sfcxzz4a2LVdsafJthQc3km6kAbHi48CZpFpM5JdqZPhTC5e3YXdwUUoAaoKUNk8ug5TUCGL1qmwUze6KsA8Di4m5xUrAmpvFwwxUx8CQ52rJc44stE5vMVYnZ7Fw5dvRSLgqDn2scGnkZPM1UZavHA5LN8PY5WqKdtJJGRRj5gjKzuz7NSUpvyQoJLt8MALknzJXFDZNzWyBfSyyNx1sbDn8c6SE2eVvCk79Sor479pLsHQb5n2zsc5515yDYTtTgJ24y2aJXr5ZVnU34",
      |            "assets": [
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 0,
      |                    "amount": 565000000000,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 1,
      |                    "amount": 335985320975,
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
      |            "boxId": "fe9d76da39639abd2f962bb14478874d8a8ee4209344a8c4e92a09ac5d107307",
      |            "transactionId": "ec115f6f9a153af1515c210c39704831cbab73deb300b7fb81a2926816b8bd99",
      |            "blockId": "0c0f5d5205415c22583db2537c1ec6ab0c099e001ccc6552c6a78f0efa185ed9",
      |            "value": 4000000,
      |            "index": 0,
      |            "globalIndex": 8738336,
      |            "creationHeight": 603922,
      |            "settlementHeight": 603924,
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
      |                    "amount": 9223371216487860240,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 2,
      |                    "amount": 2745111774473717,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 3,
      |                    "amount": 1632419930370941,
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
      |            "spentTransactionId": "42b0fcd0fc15ead89264a9964a6bae571858ad7f73f26bb21bc59baaae00c5cd",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "304ac183a1f07a25bd649f5caec5ef350377aa1ef3c07042aa5762e7aa70c2dd",
      |            "transactionId": "ec115f6f9a153af1515c210c39704831cbab73deb300b7fb81a2926816b8bd99",
      |            "blockId": "0c0f5d5205415c22583db2537c1ec6ab0c099e001ccc6552c6a78f0efa185ed9",
      |            "value": 50000,
      |            "index": 1,
      |            "globalIndex": 8738337,
      |            "creationHeight": 603922,
      |            "settlementHeight": 603924,
      |            "ergoTree": "0008cd03f9687cd6c90cb7721c181ca0b67a47fc5e1c01187f129b4faa5fedabf6ef6436",
      |            "address": "9iMeVFvixhy2JMHyyEwRRPvkgJyx2pd6tRWT6P29CATB9HZzBhw",
      |            "assets": [
      |                {
      |                    "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
      |                    "index": 0,
      |                    "amount": 168848245,
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
      |            "boxId": "2a5c58e25090ea7d5005970fe86f9ca9c051fc664418ffa12248ba3ed23d1971",
      |            "transactionId": "ec115f6f9a153af1515c210c39704831cbab73deb300b7fb81a2926816b8bd99",
      |            "blockId": "0c0f5d5205415c22583db2537c1ec6ab0c099e001ccc6552c6a78f0efa185ed9",
      |            "value": 6000000,
      |            "index": 2,
      |            "globalIndex": 8738338,
      |            "creationHeight": 603922,
      |            "settlementHeight": 603924,
      |            "ergoTree": "0008cd02870028db3cfa1cbaef31bd920842d118de32a796db8889cff125562bfd915215",
      |            "address": "9fYWvb9W6VNbHrw6jYzLtW2mNr9a7JBnGHHxvJTR2aDS4Pv6Yif",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "a7fa3d154ca3129c9ae4dba86ca34cd84b8f7085e3160d8b1bc95cdc3a3a77d4",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "b3716d0fb1e208a92e2a5fe81c9cc41f84fe73d27538d347af41978937eb0651",
      |            "transactionId": "ec115f6f9a153af1515c210c39704831cbab73deb300b7fb81a2926816b8bd99",
      |            "blockId": "0c0f5d5205415c22583db2537c1ec6ab0c099e001ccc6552c6a78f0efa185ed9",
      |            "value": 1000000,
      |            "index": 3,
      |            "globalIndex": 8738339,
      |            "creationHeight": 603922,
      |            "settlementHeight": 603924,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "3457524b9015eedef404004a0a80241b0695689a710788764275f0333fb45ba2",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 877
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  def pooltestTx = decode[TransactionTest](
    """
      |{
      |    "id": "003618e751e11776327c069b9707ccdcf48c09492217d514f08286092f3fe636",
      |    "blockId": "804ce9a022ce7a045c50738a4f2e291512ed63f23a7541e51ecd95a529db9512",
      |    "inclusionHeight": 553214,
      |    "timestamp": 1628794968308,
      |    "index": 19,
      |    "globalIndex": 1621121,
      |    "numConfirmations": 373268,
      |    "inputs": [
      |        {
      |            "boxId": "0192f905741e43e0df81a34c18b96012e7d29918d9d2c5c96721833959cda50f",
      |            "value": 4000000,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "726aa833f19e3209d617471b4f669d02ab5934c44e31cffcde610383f884cb1a",
      |            "outputTransactionId": "5693654175e7beb2210ec2225d9fd49af140592cb883adf2191931a7f499c1ec",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 6207374,
      |            "outputCreatedAt": 553200,
      |            "outputSettledAt": 553203,
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
      |                    "amount": 9223371509696191593,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 2,
      |                    "amount": 998407715799066,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 3,
      |                    "amount": 1750120963131429,
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
      |            "boxId": "4174f7e8ef8d1d0286dcc2dd7a6ff063bed1d97be27f80e3ce3ba21399bd67c4",
      |            "value": 20000000,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "f61f05a7a3e5aec25ed4681c8014309dd27ea122a9716bec2ba21aa999a468c6",
      |            "outputTransactionId": "d42e755a734d7270542b7a30e979d3926c6285bf1e597183af2e6ec5bc84a389",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 6207906,
      |            "outputCreatedAt": 506880,
      |            "outputSettledAt": 553212,
      |            "ergoTree": "19aa021108cd0368fbca247b2637b447e09c74a96cf29e04112fbd46a3f309bf22185dd969044304000404040804020400040205feffffffffffffffff0104000e20f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc78105020580dac40904000404040204060100d803d6017300d602b2a4730100d603db6308a7eb027201d195ed93b1a4730293b1db630872027303d805d604db63087202d605b2a5730400d606b2db63087205730500d607b27204730600d6087e9973078c72070206edededed93b2720473080086027309730a93c27205d0720192c1720599c1a7730b938c7206018c720701927e8c72060206a19d9c7e8cb27203730c00020672087e8cb27204730d0002069d9c7e8cb27203730e00020672087e8cb27204730f0002067310",
      |            "address": "TDSS8J9hjWpZWZ7s9cA6jEEF4uVQeJFD7peajk5tydBbhrNXTvE91fLsfyqSYfYvyBqQrEp5h1P9Gc3pKtg4obSHDb4KVi6wN2rPA3ESsvJQKo1LXi4w8yNoJ2DMPsSJpwAqfXSDvbtX7MMnftZ8w1JCPP53vNzSNpD9LoDacWCsESVozjFLCYqfLov1QPZyQyGnhM4B3MJa6ym6FL9htnY2qyhBSx7jDweqKkga3QoTqyBciUaUMK7gy2N8YFdmYPQRwzQKuH4k77ZabLyaDq9dV1NHkSoD32zscH5zVXdsZjtqnhjtzFaM8wusWAY7iaxvsprTkG7BiA4KxUZM19qo6m9srN6KV9ZPiT3nRuBkkrRAkMW9TsoxVFwPNBhvDdrMHrZycfxJpz7u7NK8cMWyKV78FnV36",
      |            "assets": [
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 0,
      |                    "amount": 100000000000,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 1,
      |                    "amount": 175291209736,
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
      |            "boxId": "d3259aab2c30425b116f976af4d6b879525a264ea666e42b8fba9b95748168ed",
      |            "transactionId": "003618e751e11776327c069b9707ccdcf48c09492217d514f08286092f3fe636",
      |            "blockId": "804ce9a022ce7a045c50738a4f2e291512ed63f23a7541e51ecd95a529db9512",
      |            "value": 4000000,
      |            "index": 0,
      |            "globalIndex": 6208353,
      |            "creationHeight": 553212,
      |            "settlementHeight": 553214,
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
      |                    "amount": 9223371509643391663,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 2,
      |                    "amount": 998507715799066,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 3,
      |                    "amount": 1750296254341165,
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
      |            "spentTransactionId": "f69c054ea8f871c5b3611fd791a9eb001f23949255a7008a19c8947794cb8362",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "2058bc0c8bb01e926d6d30e67504c28f50be8416641cdcd91b63a92cacefd438",
      |            "transactionId": "003618e751e11776327c069b9707ccdcf48c09492217d514f08286092f3fe636",
      |            "blockId": "804ce9a022ce7a045c50738a4f2e291512ed63f23a7541e51ecd95a529db9512",
      |            "value": 10000000,
      |            "index": 1,
      |            "globalIndex": 6208354,
      |            "creationHeight": 553212,
      |            "settlementHeight": 553214,
      |            "ergoTree": "0008cd0368fbca247b2637b447e09c74a96cf29e04112fbd46a3f309bf22185dd9690443",
      |            "address": "9hG3MZpYoMivY2qJ8ZVGP8aPrdpkjn9q2HjuQeS2c9ydQf96EQg",
      |            "assets": [
      |                {
      |                    "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
      |                    "index": 0,
      |                    "amount": 52799930,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "76db21cba5f616ab066a06847df0c5deb7854f5f7d25fb6989114b5dc65b0396",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "bd8b30defaa104d8276fe0e48f12575653228ea0ea994e2c88a1bff90566ce1b",
      |            "transactionId": "003618e751e11776327c069b9707ccdcf48c09492217d514f08286092f3fe636",
      |            "blockId": "804ce9a022ce7a045c50738a4f2e291512ed63f23a7541e51ecd95a529db9512",
      |            "value": 9000000,
      |            "index": 2,
      |            "globalIndex": 6208355,
      |            "creationHeight": 553212,
      |            "settlementHeight": 553214,
      |            "ergoTree": "0008cd02ddbe95b7f88d47bd8c2db823cc5dd1be69a650556a44d4c15ac65e1d3e34324c",
      |            "address": "9gCigPc9cZNRhKgbgdmTkVxo1ZKgw79G8DvLjCcYWAvEF3XRUKy",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "1a36e45065199265e455fed163e9f49cc8ff2760ae7a2d2d1ee6102cd7f44655",
      |            "transactionId": "003618e751e11776327c069b9707ccdcf48c09492217d514f08286092f3fe636",
      |            "blockId": "804ce9a022ce7a045c50738a4f2e291512ed63f23a7541e51ecd95a529db9512",
      |            "value": 1000000,
      |            "index": 3,
      |            "globalIndex": 6208356,
      |            "creationHeight": 553212,
      |            "settlementHeight": 553214,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "7d2729f64e97270b7474275f9035b2625d7a1eb8162f3201d97f1acb68e49415",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 878
      |}
      |""".stripMargin
  )
    .toOption.get.toTransaction

}
