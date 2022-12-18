package fi.spectrum.indexer.repository

import fi.spectrum.parser.models.TransactionTest
import io.circe.parser.decode

object Redeems {

  val executed = decode[TransactionTest](
    """
      |{
      |    "id": "12208d5a4a71211d12b47615791bcde8d7a8e3693f2a1230af093aaf22f68285",
      |    "blockId": "2caeb85f4004b520e270d880fa8dca36756429f272bad824fb40530ef48c340f",
      |    "inclusionHeight": 617206,
      |    "timestamp": 1636588725863,
      |    "index": 3,
      |    "globalIndex": 2097925,
      |    "numConfirmations": 281684,
      |    "inputs": [
      |        {
      |            "boxId": "1d82ca568e000aac25e06204a0e408226b8f4d84f8e51a35cb95a01e794d5de7",
      |            "value": 29955574334663,
      |            "index": 0,
      |            "spendingProof": null,
      |            "outputBlockId": "9b9cbf4baeec1b0e3903b8dbdcaa0990826389bdfa49e204e554ec4b23a11e87",
      |            "outputTransactionId": "37f19e80ed75d1d552aae50460f7035c1958bf3b0d33313d2243ab8af62e69fc",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 9512007,
      |            "outputCreatedAt": 617200,
      |            "outputSettledAt": 617202,
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
      |                    "amount": 9223372022395459626,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 2,
      |                    "amount": 29978599,
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
      |            "boxId": "00edbd2a55b3a9de78221691eda751c8d092b9801c731646808cc877c60bdd0f",
      |            "value": 7050000,
      |            "index": 1,
      |            "spendingProof": null,
      |            "outputBlockId": "2d964d08e490e12fd81b6afbe72375c2151e7f44f8d36d47fffeed461e34a1fb",
      |            "outputTransactionId": "00c05d2e715abf9d2aa2b0db7fd3c0bdce0c89ffd6eafb6ebc030c328ac47fd4",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 9512179,
      |            "outputCreatedAt": 506880,
      |            "outputSettledAt": 617205,
      |            "ergoTree": "1997020e08cd0225baf63034b4f8a7e073d608194515a32f51fb3d726c97fd311211e9a4c58ce8040004040406040204000404040005feffffffffffffffff01040204000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f0580bfd6060100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d806d603db63087202d604b2a5730400d605b2db63087204730500d606b27203730600d6077e8cb2db6308a77307000206d6087e9973088cb272037309000206edededed938cb27203730a0001730b93c27204d07201938c7205018c720601927e9a99c17204c1a7730c069d9c72077ec17202067208927e8c720502069d9c72077e8c720602067208730d",
      |            "address": "ZSU7trPRbhyhQP9pQbmcFHjQjwzMFQ93L7iNfkY1x41t4QbU5uMN5HX84dKnwudWXkNhL7wvkZhsbBhmg2SWjQHFUg5NsbYAwnez5HHxaH4md2nFVFDTWzXKqYW43z9wXbPQYJbjYRZVgRXgtwREeFSRJQpDNQzphesDSr6hLM7jK1aUc49XyFex7ZD6czanU48deH7qdwVF5UhLTncPCtSzGG8QXdbuCacq4rWGi9LE9ouZq8y6bZWv6BC4JkT9AvfanRg3EZPmTz1PJjvddgyxfFiMxpVGgyqdjVN86PwLCLcdoh1tRR4kKhLbcTSz5KjydGyV43q1EnUEiW8vGHzFvhwCeRLdwCcY6hWvsmaSuVPxDcmRqVkzKSeEwwPn3EJWPt7",
      |            "assets": [
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 0,
      |                    "amount": 862796,
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
      |            "boxId": "35013997ae22651b0c0f460157598a17bfcad232657250a20aaf3207b63f7fb0",
      |            "transactionId": "12208d5a4a71211d12b47615791bcde8d7a8e3693f2a1230af093aaf22f68285",
      |            "blockId": "2caeb85f4004b520e270d880fa8dca36756429f272bad824fb40530ef48c340f",
      |            "value": 29953786867719,
      |            "index": 0,
      |            "globalIndex": 9512207,
      |            "creationHeight": 617204,
      |            "settlementHeight": 617206,
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
      |                    "amount": 9223372022396322422,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                },
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 2,
      |                    "amount": 29976811,
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
      |            "spentTransactionId": "4b98256a23d6e31d66d720cdf7d5cfdaa815baa420f9239d24a582704082f8f3",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "78a1ff97dfede342290715b60227ffde8fdf2b3a0475bfab1420bb2dfa6cc578",
      |            "transactionId": "12208d5a4a71211d12b47615791bcde8d7a8e3693f2a1230af093aaf22f68285",
      |            "blockId": "2caeb85f4004b520e270d880fa8dca36756429f272bad824fb40530ef48c340f",
      |            "value": 1787516944,
      |            "index": 1,
      |            "globalIndex": 9512208,
      |            "creationHeight": 617204,
      |            "settlementHeight": 617206,
      |            "ergoTree": "0008cd0225baf63034b4f8a7e073d608194515a32f51fb3d726c97fd311211e9a4c58ce8",
      |            "address": "9eogHWHnHb2QxzcgGTHwv3HpSiGFJevqVFxqpznyuVXQVgTjpbT",
      |            "assets": [
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 0,
      |                    "amount": 1788,
      |                    "name": "SigRSV",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "f50bb00a94320aaf3f2ac92422bc3c071a40b4e3a8f31cda0ccfe331983a706f",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "07cc738aa59c6692d62928983df1a6a2628995709b39e65686573c273b3f0213",
      |            "transactionId": "12208d5a4a71211d12b47615791bcde8d7a8e3693f2a1230af093aaf22f68285",
      |            "blockId": "2caeb85f4004b520e270d880fa8dca36756429f272bad824fb40530ef48c340f",
      |            "value": 6000000,
      |            "index": 2,
      |            "globalIndex": 9512209,
      |            "creationHeight": 617204,
      |            "settlementHeight": 617206,
      |            "ergoTree": "0008cd02870028db3cfa1cbaef31bd920842d118de32a796db8889cff125562bfd915215",
      |            "address": "9fYWvb9W6VNbHrw6jYzLtW2mNr9a7JBnGHHxvJTR2aDS4Pv6Yif",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "e493b44e69248226888a34fb78cff0766311b742e43e232f14b74b12e381d78f",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "d3e413edcd70e2119e662d7c689e47a575daba19554805a16d7278e083f0aff7",
      |            "transactionId": "12208d5a4a71211d12b47615791bcde8d7a8e3693f2a1230af093aaf22f68285",
      |            "blockId": "2caeb85f4004b520e270d880fa8dca36756429f272bad824fb40530ef48c340f",
      |            "value": 1000000,
      |            "index": 3,
      |            "globalIndex": 9512210,
      |            "creationHeight": 617204,
      |            "settlementHeight": 617206,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "a4c74e0619ae5b7dc88fc3049b4328bc39de0ad7a98b75b8e0a6db074b61c0fb",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 819
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  val redeem = decode[TransactionTest](
    """
      |{
      |    "id": "00c05d2e715abf9d2aa2b0db7fd3c0bdce0c89ffd6eafb6ebc030c328ac47fd4",
      |    "blockId": "2d964d08e490e12fd81b6afbe72375c2151e7f44f8d36d47fffeed461e34a1fb",
      |    "inclusionHeight": 617205,
      |    "timestamp": 1636588668107,
      |    "index": 2,
      |    "globalIndex": 2097915,
      |    "numConfirmations": 281685,
      |    "inputs": [
      |        {
      |            "boxId": "f145eecf51e41871abcc8bed41d1c9280bd05cb7705d0ae9c78c35bc27da45d5",
      |            "value": 50000,
      |            "index": 0,
      |            "spendingProof": "9cdf228bbe283a13d8ee3bf7df6cdd6f5b289265e9167d047de6493842b57f0b6b027b44e1c3cae5538cb6775090a008be5cf05db2e502e9",
      |            "outputBlockId": "dbab0ff1d533d4f1af38ab9b7511295dd77febe2588ba123bcdbd48d90bfed8c",
      |            "outputTransactionId": "8fc65b55b68be8408125fe8c11720430080eb1b38e93e99548233fe39d2496ca",
      |            "outputIndex": 1,
      |            "outputGlobalIndex": 9499334,
      |            "outputCreatedAt": 616948,
      |            "outputSettledAt": 616950,
      |            "ergoTree": "0008cd0225baf63034b4f8a7e073d608194515a32f51fb3d726c97fd311211e9a4c58ce8",
      |            "address": "9eogHWHnHb2QxzcgGTHwv3HpSiGFJevqVFxqpznyuVXQVgTjpbT",
      |            "assets": [
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 0,
      |                    "amount": 862796,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {}
      |        },
      |        {
      |            "boxId": "5ad23a170790ab3060ed928f26bac2d32b8bbdceefd9740f567685b86e31a7a3",
      |            "value": 162250000,
      |            "index": 1,
      |            "spendingProof": "b208d0c3325d32e3c9b5f42a28bb73529f3f4bbe188b8e63a5799dbe416ffcb1cf0d5e6a5447029006fd8e529f869911465ec50ba8358cac",
      |            "outputBlockId": "dbab0ff1d533d4f1af38ab9b7511295dd77febe2588ba123bcdbd48d90bfed8c",
      |            "outputTransactionId": "451b1b00fa08f770bf015a69c056b5dcb1c6a3702decd589a85bbc53769abb69",
      |            "outputIndex": 2,
      |            "outputGlobalIndex": 9499323,
      |            "outputCreatedAt": 506880,
      |            "outputSettledAt": 616950,
      |            "ergoTree": "0008cd0225baf63034b4f8a7e073d608194515a32f51fb3d726c97fd311211e9a4c58ce8",
      |            "address": "9eogHWHnHb2QxzcgGTHwv3HpSiGFJevqVFxqpznyuVXQVgTjpbT",
      |            "assets": [
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 0,
      |                    "amount": 2156,
      |                    "name": "SigRSV",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "e91cbc48016eb390f8f872aa2962772863e2e840708517d1ab85e57451f91bed",
      |                    "index": 1,
      |                    "amount": 1200,
      |                    "name": "Ergold",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "36aba4b4a97b65be491cf9f5ca57b5408b0da8d0194f30ec8330d1e8946161c1",
      |                    "index": 2,
      |                    "amount": 1,
      |                    "name": "Erdoge",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 3,
      |                    "amount": 200000000000,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 4,
      |                    "amount": 500000000000,
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
      |            "boxId": "00edbd2a55b3a9de78221691eda751c8d092b9801c731646808cc877c60bdd0f",
      |            "transactionId": "00c05d2e715abf9d2aa2b0db7fd3c0bdce0c89ffd6eafb6ebc030c328ac47fd4",
      |            "blockId": "2d964d08e490e12fd81b6afbe72375c2151e7f44f8d36d47fffeed461e34a1fb",
      |            "value": 7050000,
      |            "index": 0,
      |            "globalIndex": 9512179,
      |            "creationHeight": 506880,
      |            "settlementHeight": 617205,
      |            "ergoTree": "1997020e08cd0225baf63034b4f8a7e073d608194515a32f51fb3d726c97fd311211e9a4c58ce8040004040406040204000404040005feffffffffffffffff01040204000e201d5afc59838920bb5ef2a8f9d63825a55b1d48e269d7cecee335d637c3ff5f3f0580bfd6060100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d806d603db63087202d604b2a5730400d605b2db63087204730500d606b27203730600d6077e8cb2db6308a77307000206d6087e9973088cb272037309000206edededed938cb27203730a0001730b93c27204d07201938c7205018c720601927e9a99c17204c1a7730c069d9c72077ec17202067208927e8c720502069d9c72077e8c720602067208730d",
      |            "address": "ZSU7trPRbhyhQP9pQbmcFHjQjwzMFQ93L7iNfkY1x41t4QbU5uMN5HX84dKnwudWXkNhL7wvkZhsbBhmg2SWjQHFUg5NsbYAwnez5HHxaH4md2nFVFDTWzXKqYW43z9wXbPQYJbjYRZVgRXgtwREeFSRJQpDNQzphesDSr6hLM7jK1aUc49XyFex7ZD6czanU48deH7qdwVF5UhLTncPCtSzGG8QXdbuCacq4rWGi9LE9ouZq8y6bZWv6BC4JkT9AvfanRg3EZPmTz1PJjvddgyxfFiMxpVGgyqdjVN86PwLCLcdoh1tRR4kKhLbcTSz5KjydGyV43q1EnUEiW8vGHzFvhwCeRLdwCcY6hWvsmaSuVPxDcmRqVkzKSeEwwPn3EJWPt7",
      |            "assets": [
      |                {
      |                    "tokenId": "fa6326a26334f5e933b96470b53b45083374f71912b0d7597f00c2c7ebeb5da6",
      |                    "index": 0,
      |                    "amount": 862796,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "12208d5a4a71211d12b47615791bcde8d7a8e3693f2a1230af093aaf22f68285",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "6cb116c1ae9196b374d357781337c5f2f4501082adf9c0343e9dcbb9236171b6",
      |            "transactionId": "00c05d2e715abf9d2aa2b0db7fd3c0bdce0c89ffd6eafb6ebc030c328ac47fd4",
      |            "blockId": "2d964d08e490e12fd81b6afbe72375c2151e7f44f8d36d47fffeed461e34a1fb",
      |            "value": 10000000,
      |            "index": 1,
      |            "globalIndex": 9512180,
      |            "creationHeight": 506880,
      |            "settlementHeight": 617205,
      |            "ergoTree": "0008cd02ddbe95b7f88d47bd8c2db823cc5dd1be69a650556a44d4c15ac65e1d3e34324c",
      |            "address": "9gCigPc9cZNRhKgbgdmTkVxo1ZKgw79G8DvLjCcYWAvEF3XRUKy",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": null,
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "764e233764918737ace9d15d89cacd002ee03ad66a2160d962df7e7082e25449",
      |            "transactionId": "00c05d2e715abf9d2aa2b0db7fd3c0bdce0c89ffd6eafb6ebc030c328ac47fd4",
      |            "blockId": "2d964d08e490e12fd81b6afbe72375c2151e7f44f8d36d47fffeed461e34a1fb",
      |            "value": 143250000,
      |            "index": 2,
      |            "globalIndex": 9512181,
      |            "creationHeight": 506880,
      |            "settlementHeight": 617205,
      |            "ergoTree": "0008cd0225baf63034b4f8a7e073d608194515a32f51fb3d726c97fd311211e9a4c58ce8",
      |            "address": "9eogHWHnHb2QxzcgGTHwv3HpSiGFJevqVFxqpznyuVXQVgTjpbT",
      |            "assets": [
      |                {
      |                    "tokenId": "003bd19d0187117f130b62e1bcab0939929ff5c7709f843c5c4dd158949285d0",
      |                    "index": 0,
      |                    "amount": 2156,
      |                    "name": "SigRSV",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "e91cbc48016eb390f8f872aa2962772863e2e840708517d1ab85e57451f91bed",
      |                    "index": 1,
      |                    "amount": 1200,
      |                    "name": "Ergold",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "36aba4b4a97b65be491cf9f5ca57b5408b0da8d0194f30ec8330d1e8946161c1",
      |                    "index": 2,
      |                    "amount": 1,
      |                    "name": "Erdoge",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |                    "index": 3,
      |                    "amount": 200000000000,
      |                    "name": "WT_ADA",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 4,
      |                    "amount": 500000000000,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "4eadb7cece5ffd89714533e2ef2ac8132ce69574c0271ab8800339edc2472f12",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "fb6ddc9206c789c573c3368271fa9e356419e50331bae1341ac45c6ef719119b",
      |            "transactionId": "00c05d2e715abf9d2aa2b0db7fd3c0bdce0c89ffd6eafb6ebc030c328ac47fd4",
      |            "blockId": "2d964d08e490e12fd81b6afbe72375c2151e7f44f8d36d47fffeed461e34a1fb",
      |            "value": 2000000,
      |            "index": 3,
      |            "globalIndex": 9512182,
      |            "creationHeight": 506880,
      |            "settlementHeight": 617205,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "ed2ee2016ab9feb07f40c207e16a9ee67b782a5c5d1eea2ec6b1cda060026d99",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 896
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

  val redeemToRefund = decode[TransactionTest]("""
      |{
      |    "id": "2b792d75553fedc049e155e61622ef3265ed48f6df2770e55895fc6ba77a0670",
      |    "blockId": "b81291fc10f3041f9adbe4c1a200cc58bd72b585e1969469a36b410787b4f739",
      |    "inclusionHeight": 733833,
      |    "timestamp": 1650646621012,
      |    "index": 6,
      |    "globalIndex": 3041039,
      |    "numConfirmations": 165057,
      |    "inputs": [
      |        {
      |            "boxId": "4036b4c63fb28612041291a51f3332a35508fc69f5e1e8386042f140deac8115",
      |            "value": 522696913,
      |            "index": 0,
      |            "spendingProof": "6922c11ce749d9ae4b27b42ceaea363e3cf2c1e6ffaba3f2238f2269681df2d39e4d74095f744f52b5f32aa6c75133420251e1966bc6f43c",
      |            "outputBlockId": "8e0717df8ff84b7fa1dd5168465ff0ecee7e7c9350c2b3cd054c8481a915f9ea",
      |            "outputTransactionId": "52e444913714ccdf931a7f30943682cea3d0197d88608cffe54f4b8572bfafb1",
      |            "outputIndex": 1,
      |            "outputGlobalIndex": 15705990,
      |            "outputCreatedAt": 731136,
      |            "outputSettledAt": 731140,
      |            "ergoTree": "0008cd03de11ea43220f91b8bb236665d8c0d2fa0783f3bcef83412e5b08801908524a88",
      |            "address": "9i9cAsezp3vzaqLhCdvWKtHE7EFvqVSJHxXSDUHjs9EqQsXVfXK",
      |            "assets": [
      |                {
      |                    "tokenId": "944f72c571f7e894fe75fe5b351cdc67ea2fa6daa538321d72f759d551b1d147",
      |                    "index": 0,
      |                    "amount": 63269327835,
      |                    "name": "ADA",
      |                    "decimals": 6,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "88d61bf95b1ca83ed0b008ab889cb531e550f4a89c09385b6bb18e583ab88055",
      |                    "index": 1,
      |                    "amount": 1048714892,
      |                    "name": "ERG_ADA_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "e249780a22e14279357103749102d0a7033e0459d10b7f277356522ae9df779c",
      |                    "index": 2,
      |                    "amount": 4101681621,
      |                    "name": "ERG_NETA_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "566acbe1657136edc3bd7c27f1619490ed9a896f9dc7dbea07ace755c4ff857a",
      |                    "index": 3,
      |                    "amount": 10488225,
      |                    "name": "ERG_QualiCoin_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "0779ec04f2fae64e87418a1ad917639d4668f78484f45df962b0dec14a2591d2",
      |                    "index": 4,
      |                    "amount": 2000,
      |                    "name": "Mi Goreng ",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |                    "index": 5,
      |                    "amount": 41884770290,
      |                    "name": "NETA",
      |                    "decimals": 6,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "5a34d53ca483924b9a6aa0c771f11888881b516a8d1a9cdc535d063fe26d065e",
      |                    "index": 6,
      |                    "amount": 1674085342871,
      |                    "name": "LunaDog",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 7,
      |                    "amount": 5983874896252,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "46bd8c30598aa8fe6237722d5f32f56ad7aab43a3668dcc3cbde97d539e41e95",
      |                    "index": 8,
      |                    "amount": 2002857237577,
      |                    "name": "WT_ERG_WT_ADA_LP",
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
      |            "boxId": "00dea5b12270f5b5bfa56401d451de8e492f8da87c1f9d3c219e74e8bfc7428e",
      |            "transactionId": "2b792d75553fedc049e155e61622ef3265ed48f6df2770e55895fc6ba77a0670",
      |            "blockId": "b81291fc10f3041f9adbe4c1a200cc58bd72b585e1969469a36b410787b4f739",
      |            "value": 6060000,
      |            "index": 0,
      |            "globalIndex": 15849242,
      |            "creationHeight": 733831,
      |            "settlementHeight": 733833,
      |            "ergoTree": "19b1031208cd03de11ea43220f91b8bb236665d8c0d2fa0783f3bcef83412e5b08801908524a88040004040406040204000404040005feffffffffffffffff01040204000e20c0070feda814a6d74f97ac24444976a4d07aae9d2645874732df99e94617bfdb0580b6dc050e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d806d603db63087202d604b2a5730400d605b2db63087204730500d606b27203730600d6077e8cb2db6308a77307000206d6087e9973088cb272037309000206ededededed938cb27203730a0001730b93c27204d07201938c7205018c720601927e9a99c17204c1a7730c069d9c72077ec17202067208927e8c720502069d9c72077e8c72060206720890b0ada5d90109639593c27209730dc17209730e730fd90109599a8c7209018c72090273107311",
      |            "address": "2ysN6BW5GJAzU4UAwnVvBnuwZB1AXB7hM3EpweFEotqvd56vjf4qmMv98PXdHfDyrBJay4adX2m9iEv9cQeQqoA2R74g1ANefPSJK2c8NVJ2JVMekovmph3TTPsCmZJx93M8yeXMJm9GfGQG2XPppbT1Mmb6cZYjwhefVrR1oRCUx9AxHB1QGkdiY3PVGexJq8MPtP8Gr1KpkbrS8HcADk5c5ssCn9R3PGpJpnsynhyfEzVsPnTuNvMtAR8C1cCwoYYjwgUbotwzc7b1Pr8QXpGBgywYd3Uv31zrZ1SXh9aoedBPhqyBbgDa6mJNkbCC4UAsX4xx74Zv8kyjwMz86quteXtibkjK1KaZHJsEFCE6KV47Mgn6LRPHXUzC5U6xEDjYQsMz27ubCruvtFzsD9gQr4kKksCzFxfrTHyqu92dKiroUcN7ooKSfSpeQxh7WwGFnYfxLtgwDgKqyLrw7nBf6LuYJ6xcR8hbYoPm3u53DSmCrtBzY7NKgNh3dojsaVcBS3HT8Mv2u9Wg4FPCFVZdGwPqR5CJM3PXMP5PzRcmDKHPmUEhtZcCAzRcX7jk3y5JHyrqwyGV1Ga9aCJEUibXyz",
      |            "assets": [
      |                {
      |                    "tokenId": "88d61bf95b1ca83ed0b008ab889cb531e550f4a89c09385b6bb18e583ab88055",
      |                    "index": 0,
      |                    "amount": 1048714892,
      |                    "name": "ERG_ADA_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "17d27268f98b6c9523ffe230e22617cd38bea8cf8116fa0109d617a375ff13f1",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "1b9f33efca210e2d9a5c1f6abc406f96555a154780d6b6cf83cd7b411562741a",
      |            "transactionId": "2b792d75553fedc049e155e61622ef3265ed48f6df2770e55895fc6ba77a0670",
      |            "blockId": "b81291fc10f3041f9adbe4c1a200cc58bd72b585e1969469a36b410787b4f739",
      |            "value": 514636913,
      |            "index": 1,
      |            "globalIndex": 15849243,
      |            "creationHeight": 733831,
      |            "settlementHeight": 733833,
      |            "ergoTree": "0008cd03de11ea43220f91b8bb236665d8c0d2fa0783f3bcef83412e5b08801908524a88",
      |            "address": "9i9cAsezp3vzaqLhCdvWKtHE7EFvqVSJHxXSDUHjs9EqQsXVfXK",
      |            "assets": [
      |                {
      |                    "tokenId": "944f72c571f7e894fe75fe5b351cdc67ea2fa6daa538321d72f759d551b1d147",
      |                    "index": 0,
      |                    "amount": 63269327835,
      |                    "name": "ADA",
      |                    "decimals": 6,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "e249780a22e14279357103749102d0a7033e0459d10b7f277356522ae9df779c",
      |                    "index": 1,
      |                    "amount": 4101681621,
      |                    "name": "ERG_NETA_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "566acbe1657136edc3bd7c27f1619490ed9a896f9dc7dbea07ace755c4ff857a",
      |                    "index": 2,
      |                    "amount": 10488225,
      |                    "name": "ERG_QualiCoin_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "0779ec04f2fae64e87418a1ad917639d4668f78484f45df962b0dec14a2591d2",
      |                    "index": 3,
      |                    "amount": 2000,
      |                    "name": "Mi Goreng ",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |                    "index": 4,
      |                    "amount": 41884770290,
      |                    "name": "NETA",
      |                    "decimals": 6,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "5a34d53ca483924b9a6aa0c771f11888881b516a8d1a9cdc535d063fe26d065e",
      |                    "index": 5,
      |                    "amount": 1674085342871,
      |                    "name": "LunaDog",
      |                    "decimals": 8,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |                    "index": 6,
      |                    "amount": 5983874896252,
      |                    "name": "WT_ERG",
      |                    "decimals": 9,
      |                    "type": "EIP-004"
      |                },
      |                {
      |                    "tokenId": "46bd8c30598aa8fe6237722d5f32f56ad7aab43a3668dcc3cbde97d539e41e95",
      |                    "index": 7,
      |                    "amount": 2002857237577,
      |                    "name": "WT_ERG_WT_ADA_LP",
      |                    "decimals": 0,
      |                    "type": "EIP-004"
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "8d5f4ddb94086b547b1e040312695e661b29130d5eed0c985f15a8f50fab436b",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "a3222856e81203f7a642078d175ea556e2b4a344ab830802537d8ca54556be37",
      |            "transactionId": "2b792d75553fedc049e155e61622ef3265ed48f6df2770e55895fc6ba77a0670",
      |            "blockId": "b81291fc10f3041f9adbe4c1a200cc58bd72b585e1969469a36b410787b4f739",
      |            "value": 2000000,
      |            "index": 2,
      |            "globalIndex": 15849244,
      |            "creationHeight": 733831,
      |            "settlementHeight": 733833,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "9890de765a8f6bde533fa0881a288013e2ad6401539ee2335e218bf807767748",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 1042
      |}
      |""".stripMargin).toOption.get.toTransaction

  val refund = decode[TransactionTest](
    """
      |{
      |    "id": "17d27268f98b6c9523ffe230e22617cd38bea8cf8116fa0109d617a375ff13f1",
      |    "blockId": "00a4a8ff943aafeff3d82ff71e40df19849ab5b27d3c3378d85bd6f2ecb9bd42",
      |    "inclusionHeight": 766085,
      |    "timestamp": 1654513960096,
      |    "index": 1,
      |    "globalIndex": 3308532,
      |    "numConfirmations": 132805,
      |    "inputs": [
      |        {
      |            "boxId": "00dea5b12270f5b5bfa56401d451de8e492f8da87c1f9d3c219e74e8bfc7428e",
      |            "value": 6060000,
      |            "index": 0,
      |            "spendingProof": "aea97f1246831aa61188fb0ddbcc1b5fe5031980a0cccf9da22cf920569876a2d591d7c8598e860df37f5faf832ab78bbb2083216e431495",
      |            "outputBlockId": "b81291fc10f3041f9adbe4c1a200cc58bd72b585e1969469a36b410787b4f739",
      |            "outputTransactionId": "2b792d75553fedc049e155e61622ef3265ed48f6df2770e55895fc6ba77a0670",
      |            "outputIndex": 0,
      |            "outputGlobalIndex": 15849242,
      |            "outputCreatedAt": 733831,
      |            "outputSettledAt": 733833,
      |            "ergoTree": "19b1031208cd03de11ea43220f91b8bb236665d8c0d2fa0783f3bcef83412e5b08801908524a88040004040406040204000404040005feffffffffffffffff01040204000e20c0070feda814a6d74f97ac24444976a4d07aae9d2645874732df99e94617bfdb0580b6dc050e691005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a5730405000500058092f4010100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d806d603db63087202d604b2a5730400d605b2db63087204730500d606b27203730600d6077e8cb2db6308a77307000206d6087e9973088cb272037309000206ededededed938cb27203730a0001730b93c27204d07201938c7205018c720601927e9a99c17204c1a7730c069d9c72077ec17202067208927e8c720502069d9c72077e8c72060206720890b0ada5d90109639593c27209730dc17209730e730fd90109599a8c7209018c72090273107311",
      |            "address": "2ysN6BW5GJAzU4UAwnVvBnuwZB1AXB7hM3EpweFEotqvd56vjf4qmMv98PXdHfDyrBJay4adX2m9iEv9cQeQqoA2R74g1ANefPSJK2c8NVJ2JVMekovmph3TTPsCmZJx93M8yeXMJm9GfGQG2XPppbT1Mmb6cZYjwhefVrR1oRCUx9AxHB1QGkdiY3PVGexJq8MPtP8Gr1KpkbrS8HcADk5c5ssCn9R3PGpJpnsynhyfEzVsPnTuNvMtAR8C1cCwoYYjwgUbotwzc7b1Pr8QXpGBgywYd3Uv31zrZ1SXh9aoedBPhqyBbgDa6mJNkbCC4UAsX4xx74Zv8kyjwMz86quteXtibkjK1KaZHJsEFCE6KV47Mgn6LRPHXUzC5U6xEDjYQsMz27ubCruvtFzsD9gQr4kKksCzFxfrTHyqu92dKiroUcN7ooKSfSpeQxh7WwGFnYfxLtgwDgKqyLrw7nBf6LuYJ6xcR8hbYoPm3u53DSmCrtBzY7NKgNh3dojsaVcBS3HT8Mv2u9Wg4FPCFVZdGwPqR5CJM3PXMP5PzRcmDKHPmUEhtZcCAzRcX7jk3y5JHyrqwyGV1Ga9aCJEUibXyz",
      |            "assets": [
      |                {
      |                    "tokenId": "88d61bf95b1ca83ed0b008ab889cb531e550f4a89c09385b6bb18e583ab88055",
      |                    "index": 0,
      |                    "amount": 1048714892,
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
      |            "boxId": "79d9e5ec894e57b3d905687c590e8eb40c30748fad36363b75c9066fb50d492f",
      |            "transactionId": "17d27268f98b6c9523ffe230e22617cd38bea8cf8116fa0109d617a375ff13f1",
      |            "blockId": "00a4a8ff943aafeff3d82ff71e40df19849ab5b27d3c3378d85bd6f2ecb9bd42",
      |            "value": 4060000,
      |            "index": 0,
      |            "globalIndex": 17551625,
      |            "creationHeight": 766081,
      |            "settlementHeight": 766085,
      |            "ergoTree": "0008cd03de11ea43220f91b8bb236665d8c0d2fa0783f3bcef83412e5b08801908524a88",
      |            "address": "9i9cAsezp3vzaqLhCdvWKtHE7EFvqVSJHxXSDUHjs9EqQsXVfXK",
      |            "assets": [
      |                {
      |                    "tokenId": "88d61bf95b1ca83ed0b008ab889cb531e550f4a89c09385b6bb18e583ab88055",
      |                    "index": 0,
      |                    "amount": 1048714892,
      |                    "name": null,
      |                    "decimals": null,
      |                    "type": null
      |                }
      |            ],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "475f1c21e96de251d575278ebe3d951033b4574feacbc102ed454723fee6fe0a",
      |            "mainChain": true
      |        },
      |        {
      |            "boxId": "9f6cc0a3e37c868781484e9ec9ff022b406f8acbc91454cda6ab3a83b3f242a6",
      |            "transactionId": "17d27268f98b6c9523ffe230e22617cd38bea8cf8116fa0109d617a375ff13f1",
      |            "blockId": "00a4a8ff943aafeff3d82ff71e40df19849ab5b27d3c3378d85bd6f2ecb9bd42",
      |            "value": 2000000,
      |            "index": 1,
      |            "globalIndex": 17551626,
      |            "creationHeight": 766081,
      |            "settlementHeight": 766085,
      |            "ergoTree": "1005040004000e36100204a00b08cd0279be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798ea02d192a39a8cc7a701730073011001020402d19683030193a38cc7b2a57300000193c2b2a57301007473027303830108cdeeac93b1a57304",
      |            "address": "2iHkR7CWvD1R4j1yZg5bkeDRQavjAaVPeTDFGGLZduHyfWMuYpmhHocX8GJoaieTx78FntzJbCBVL6rf96ocJoZdmWBL2fci7NqWgAirppPQmZ7fN9V6z13Ay6brPriBKYqLp1bT2Fk4FkFLCfdPpe",
      |            "assets": [],
      |            "additionalRegisters": {},
      |            "spentTransactionId": "61b1d41554512943afcd4ec6165cc3078ddbeaeba9aae1bcfc33d485bc7e628a",
      |            "mainChain": true
      |        }
      |    ],
      |    "size": 290
      |}
      |""".stripMargin
  ).toOption.get.toTransaction

}
