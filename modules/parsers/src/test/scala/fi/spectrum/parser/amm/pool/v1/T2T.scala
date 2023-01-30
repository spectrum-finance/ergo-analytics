package fi.spectrum.parser.amm.pool.v1

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.{AssetAmount, TokenId}
import fi.spectrum.core.domain.order.PoolId
import fi.spectrum.core.domain.pool.Pool.AmmPool
import fi.spectrum.core.domain.transaction.Output
import io.circe.parser.decode

object T2T {

  val output = decode[Output](
    """
      |{
      |    "boxId": "00010c024ce20a31ca80e2090e0aa405c4832ca0a2d21458fd0fb56199a85b56",
      |    "transactionId": "c79530d8f88d9a05bf6cd754f0bc1d2ce83c1651e38a1b8b7da0730aaded23f6",
      |    "blockId": "b03c6a42407c1e644b0691b830b8de4187ad0529137c181a79d67113dc68de63",
      |    "value": 4000000,
      |    "index": 0,
      |    "globalIndex": 6499812,
      |    "creationHeight": 560711,
      |    "settlementHeight": 560714,
      |    "ergoTree": "19a9030f040004020402040404040406040605feffffffffffffffff0105feffffffffffffffff01050004d00f0400040005000500d81ad601b2a5730000d602e4c6a70404d603db63087201d604db6308a7d605b27203730100d606b27204730200d607b27203730300d608b27204730400d609b27203730500d60ab27204730600d60b9973078c720602d60c999973088c720502720bd60d8c720802d60e998c720702720dd60f91720e7309d6108c720a02d6117e721006d6127e720e06d613998c7209027210d6147e720d06d615730ad6167e721306d6177e720c06d6187e720b06d6199c72127218d61a9c72167218d1edededededed93c27201c2a793e4c672010404720292c17201c1a793b27203730b00b27204730c00938c7205018c720601ed938c7207018c720801938c7209018c720a019593720c730d95720f929c9c721172127e7202069c7ef07213069a9c72147e7215067e9c720e7e72020506929c9c721472167e7202069c7ef0720e069a9c72117e7215067e9c72137e7202050695ed720f917213730e907217a19d721972149d721a7211ed9272199c7217721492721a9c72177211",
      |    "address": "3gb1RZucekcRdda82TSNS4FZSREhGLoi1FxGDmMZdVeLtYYixPRviEdYireoM9RqC6Jf4kx85Y1jmUg5XzGgqdjpkhHm7kJZdgUR3VBwuLZuyHVqdSNv3eanqpknYsXtUwvUA16HFwNa3HgVRAnGC8zj8U7kksrfjycAM1yb19BB4TYR2BKWN7mpvoeoTuAKcAFH26cM46CEYsDRDn832wVNTLAmzz4Q6FqE29H9euwYzKiebgxQbWUxtupvfSbKaHpQcZAo5Dhyc6PFPyGVFZVRGZZ4Kftgi1NMRnGwKG7NTtXsFMsJP6A7yvLy8UZaMPe69BUAkpbSJdcWem3WpPUE7UpXv4itDkS5KVVaFtVyfx8PQxzi2eotP2uXtfairHuKinbpSFTSFKW3GxmXaw7vQs1JuVd8NhNShX6hxSqCP6sxojrqBxA48T2KcxNrmE3uFk7Pt4vPPdMAS4PW6UU82UD9rfhe3SMytK6DkjCocuRwuNqFoy4k25TXbGauTNgKuPKY3CxgkTpw9WfWsmtei178tLefhUEGJueueXSZo7negPYtmcYpoMhCuv4G1JZc283Q7f3mNXS",
      |    "assets": [
      |        {
      |            "tokenId": "f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781",
      |            "index": 0,
      |            "amount": 1,
      |            "name": null,
      |            "decimals": null,
      |            "type": null
      |        },
      |        {
      |            "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
      |            "index": 1,
      |            "amount": 9223371479844923167,
      |            "name": null,
      |            "decimals": null,
      |            "type": null
      |        },
      |        {
      |            "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
      |            "index": 2,
      |            "amount": 1089316810972585,
      |            "name": "WT_ERG",
      |            "decimals": 9,
      |            "type": "EIP-004"
      |        },
      |        {
      |            "tokenId": "30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e",
      |            "index": 3,
      |            "amount": 1793636456106346,
      |            "name": "WT_ADA",
      |            "decimals": 8,
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
      |    "spentTransactionId": "3d1fcde2c839333a651a60f5662c775898687542d885cb5fe0d0d65dae42e17a",
      |    "mainChain": true
      |}
      |""".stripMargin
  ).toOption.get

  val pool = AmmPool(
    PoolId.unsafeFromString("f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781"),
    AssetAmount(
      TokenId.unsafeFromString("1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db"),
      9223371479844923167L
    ),
    AssetAmount(
      TokenId.unsafeFromString("ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b"),
      1089316810972585L
    ),
    AssetAmount(
      TokenId.unsafeFromString("30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e"),
      1793636456106346L
    ),
    996,
    199,
    output,
    Version.V1, 10
  )
}
