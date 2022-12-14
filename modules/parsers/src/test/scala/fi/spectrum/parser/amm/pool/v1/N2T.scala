package fi.spectrum.parser.amm.pool.v1

import fi.spectrum.core.domain.{AssetAmount, TokenId}
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.core.domain.transaction.Output
import io.circe.parser.decode

object N2T {

  val output = decode[Output](
    """
      |{
      |    "boxId": "0000ee3bb27d72ede6fa042e280c4f6bdb0b256b135e888378ff2cc178cdc5d7",
      |    "transactionId": "904b1a4e8bf0af621f2140fd4d6cae7ea658a253ebea2d0f0a7c8ca9bf7555f5",
      |    "blockId": "b9af17ea4bc9a4adcf20effbd0bb7c96ef8c4ff16657024d735cb10d4b196924",
      |    "value": 37604599329678,
      |    "index": 0,
      |    "globalIndex": 19603057,
      |    "creationHeight": 0,
      |    "settlementHeight": 803965,
      |    "ergoTree": "1999030f0400040204020404040405feffffffffffffffff0105feffffffffffffffff01050004d00f040004000406050005000580dac409d819d601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d6099973058c720602d60a999973068c7205027209d60bc17201d60cc1a7d60d99720b720cd60e91720d7307d60f8c720802d6107e720f06d6117e720d06d612998c720702720fd6137e720c06d6147308d6157e721206d6167e720a06d6177e720906d6189c72117217d6199c72157217d1ededededededed93c27201c2a793e4c672010404720293b27203730900b27204730a00938c7205018c720601938c7207018c72080193b17203730b9593720a730c95720e929c9c721072117e7202069c7ef07212069a9c72137e7214067e9c720d7e72020506929c9c721372157e7202069c7ef0720d069a9c72107e7214067e9c72127e7202050695ed720e917212730d907216a19d721872139d72197210ed9272189c721672139272199c7216721091720b730e",
      |    "address": "5vSUZRZbdVbnk4sJWjg2uhL94VZWRg4iatK9VgMChufzUgdihgvhR8yWSUEJKszzV7Vmi6K8hCyKTNhUaiP8p5ko6YEU9yfHpjVuXdQ4i5p4cRCzch6ZiqWrNukYjv7Vs5jvBwqg5hcEJ8u1eerr537YLWUoxxi1M4vQxuaCihzPKMt8NDXP4WcbN6mfNxxLZeGBvsHVvVmina5THaECosCWozKJFBnscjhpr3AJsdaL8evXAvPfEjGhVMoTKXAb2ZGGRmR8g1eZshaHmgTg2imSiaoXU5eiF3HvBnDuawaCtt674ikZ3oZdekqswcVPGMwqqUKVsGY4QuFeQoGwRkMqEYTdV2UDMMsfrjrBYQYKUBFMwsQGMNBL1VoY78aotXzdeqJCBVKbQdD3ZZWvukhSe4xrz8tcF3PoxpysDLt89boMqZJtGEHTV9UBTBEac6sDyQP693qT3nKaErN8TCXrJBUmHPqKozAg9bwxTqMYkpmb9iVKLSoJxG7MjAj72SRbcqQfNCVTztSwN3cRxSrVtz4p87jNFbVtFzhPg7UqDwNFTaasySCqM",
      |    "assets": [
      |        {
      |            "tokenId": "7d2e28431063cbb1e9e14468facc47b984d962532c19b0b14f74d0ce9ed459be",
      |            "index": 0,
      |            "amount": 1,
      |            "name": null,
      |            "decimals": null,
      |            "type": null
      |        },
      |        {
      |            "tokenId": "e249780a22e14279357103749102d0a7033e0459d10b7f277356522ae9df779c",
      |            "index": 1,
      |            "amount": 9223349261757475531,
      |            "name": "ERG_NETA_LP",
      |            "decimals": 0,
      |            "type": "EIP-004"
      |        },
      |        {
      |            "tokenId": "472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8",
      |            "index": 2,
      |            "amount": 15782370143546,
      |            "name": "NETA",
      |            "decimals": 6,
      |            "type": "EIP-004"
      |        }
      |    ],
      |    "additionalRegisters": {
      |        "R4": {
      |            "serializedValue": "04c80f",
      |            "sigmaType": "SInt",
      |            "renderedValue": "996"
      |        }
      |    },
      |    "spentTransactionId": "922713f0c01ebfbc7805ea9a3643bc1f0fb21f49f251583c121adcf0e17094b3",
      |    "mainChain": true
      |}
      |""".stripMargin
  ).toOption.get

  val pool = AmmPool(
    PoolId.unsafeFromString("7d2e28431063cbb1e9e14468facc47b984d962532c19b0b14f74d0ce9ed459be"),
    AssetAmount(
      TokenId.unsafeFromString("e249780a22e14279357103749102d0a7033e0459d10b7f277356522ae9df779c"),
      9223349261757475531L
    ),
    AssetAmount(
      TokenId.unsafeFromString("0000000000000000000000000000000000000000000000000000000000000000"),
      37604599329678L
    ),
    AssetAmount(
      TokenId.unsafeFromString("472c3d4ecaa08fb7392ff041ee2e6af75f4a558810a74b28600549d5392810e8"),
      15782370143546L
    ),
    996,
    output
  )

}
