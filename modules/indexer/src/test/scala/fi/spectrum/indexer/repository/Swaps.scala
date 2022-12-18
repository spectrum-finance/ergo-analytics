package fi.spectrum.indexer.repository

import fi.spectrum.parser.models.TransactionTest
import io.circe.parser.decode

object Swaps {

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
}
