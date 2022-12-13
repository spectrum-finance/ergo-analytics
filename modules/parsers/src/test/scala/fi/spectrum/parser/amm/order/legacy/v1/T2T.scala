package fi.spectrum.parser.amm.order.legacy.v1

import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Order.Redeem.RedeemLegacyV1
import fi.spectrum.core.domain.order.Order.Swap.SwapLegacyV1
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, PubKey, TokenId}
import io.circe.parser.decode

object T2T {

  object swap {

    val output = decode[Output](
      """
        |{
        |    "boxId": "b7510c7cd1ff26bd61aec41a6ecb19fbbbcc38900f4451af3fcc127d424a24eb",
        |    "transactionId": "527046977fc5955c7f8c5fd27b08e593730b99b113c967b0dc8a32409509e4ec",
        |    "blockId": "b1b0e18db8afe5e7c2bcd5e4a80dea7d2e4c54098c75826f5989e8ad1c0b84b6",
        |    "value": 16000000,
        |    "index": 0,
        |    "globalIndex": 6215413,
        |    "creationHeight": 508928,
        |    "settlementHeight": 553342,
        |    "ergoTree": "19a2031308cd02a97f8976bd6363bdba123d6325d71d17b1e53d22e233fdbfcadd65f4268e790904000e2030974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e04c80f04d00f040404080402040004040400040606010104000e20f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781059cdaed8d7f05d2d5b282c1a38501058080a0f6f4acdbe01b0100d805d6017300d602b2a4730100d6037302d6047303d6057304eb027201d195ed93b1a4730593b1db630872027306d80ad606db63087202d607b2a5730700d608b2db63087207730800d6098c720802d60a7e720906d60bb27206730900d60c7e8c720b0206d60d7e8cb2db6308a7730a000206d60e7e8cb27206730b000206d60f9a720a730cededededed938cb27206730d0001730e93c27207d07201938c7208017203927209730f927ec1720706997ec1a7069d9c720a7e7310067e73110695938c720b017203909c9c720c720d7e7204069c720f9a9c720e7e7205069c720d7e720406909c9c720e720d7e7204069c720f9a9c720c7e7205069c720d7e7204067312",
        |    "address": "H3AA3N1iexfbxFB3DUX1TxwqZssB87zYbcBqovvutcq6jFDNvi7ug42gkMnjfo7K14ZnCbNfxKRhn5aDd2NVSTkro4QJ5AttcEwdDa7QggGDYnGbcw3ZHM8VDAYQz3PoTSVyKByjAqiaFSBWU8jkKA5ruWJqwMRW84n2LFg9sek2KzrCcQ82zjjkpeLjMDwhpEqmdn4iscbw61xtgV1p5LeWEws6k5ARgJns5ZsmRr9cQFhbJ4cX5HzW6AJoKNEgvxVb26iMKmhKgjxvGpqLD2nXJgijk6jjGvuy39h3d2Hi1QwqGM4SB6He9aMZisWXjE8aHYuCHy1AzN2jxUUcEL3b3eXwDdgp3H2QvsqZrrqVVueEkL8qpKxzwmre55f7ccY4T6WZoDMqQjSPRdMRkF3BxZdpQNLwPgwJDV7oYtY3LWTSzPQxoRvt1BoWKzj2BQGc4i5VWqEpD3qMczSBHxt57yJXTb2Mxuth4urqornWJ6r2T3CMGTgWjjjqf7an1iheA8LUJjuFSv2f5kVMUbU97GvxvNDQkC9Vyn5BitnQrWXwtBMCbT8hvwhzRg6ABiJma",
        |    "assets": [
        |        {
        |            "tokenId": "ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b",
        |            "index": 0,
        |            "amount": 10000000000,
        |            "name": "WT_ERG",
        |            "decimals": 9,
        |            "type": "EIP-004"
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "52f5c53103e2fe4108e25b2af16efa721ff8a7636d45e32d6e5128785c8e6c27",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val order: SwapLegacyV1 = SwapLegacyV1(
      output,
      PoolId.unsafeFromString("f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781"),
      PublicKeyRedeemer(PubKey.unsafeFromString("02a97f8976bd6363bdba123d6325d71d17b1e53d22e233fdbfcadd65f4268e7909")),
      SwapParams(
        AssetAmount(
          TokenId.unsafeFromString("ef802b475c06189fdbf844153cdc1d449a5ba87cce13d11bb47b5a539f27f12b"),
          10000000000L
        ),
        AssetAmount(
          TokenId.unsafeFromString("30974274078845f263b4f21787e33cc99e9ec19a17ad85a5bc6da2cca91c5a2e"),
          17060181646L
        ),
        293080115074409L,
        1000000000000000000L
      ),
      Version.make.legacyV1,
      OrderType.make.amm,
      Operation.make.swap
    )
  }

  object redeem {

    val output: Output = decode[Output](
      """
        |{
        |    "boxId": "4b7566339451a7feb4316b73607e1095b40dae86de72cf7c21016dd6ddbc3502",
        |    "transactionId": "cf9a47326f68d1f3f51c839e002d7f37d65bc10c2724bf772cd795c7340c70e9",
        |    "blockId": "a6923960f72f724dd668d590d306fee080b2cac1f742b215667db3cb0a8e9a39",
        |    "value": 20000000,
        |    "index": 0,
        |    "globalIndex": 6154418,
        |    "creationHeight": 506880,
        |    "settlementHeight": 551912,
        |    "ergoTree": "19c3021108cd02c3f56e66191a903758f53a4b90d07cef80f93e7a4f17d106098ad0caf189722a04000404040804020400040404020406040005feffffffffffffffff01040204000e20f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc78105020580dac4090100d802d6017300d602b2a4730100eb027201d195ed93b1a4730293b1db630872027303d809d603db63087202d604b2a5730400d605db63087204d606b27205730500d607b27203730600d608b27205730700d609b27203730800d60a7e8cb2db6308a77309000206d60b7e99730a8cb27203730b000206edededededed93b27203730c008602730d730e93c27204d0720192c1720499c1a7730f938c7206018c720701938c7208018c720901927e8c720602069d9c720a7e8c72070206720b927e8c720802069d9c720a7e8c72090206720b7310",
        |    "address": "oZfBf79wWTJbR3fqfvksQvzVy7A1rSPTbFdEPCCp7oNoXE6GUwRMVqCBipWdUkMswEHJZZVbpZPXzuJozSBfgCHUT82YVJoHb8dmpm4ae2mafBRazsjxv7em5VaLd9BVCPRy12b1JF3WbBdcj8usPJyu6SMe1bpmUTdRWk9RTerFfYtBKMY3C4a8xg94mwrepFcCuCHi3GxdvH64NaR2UZucKgBYfVjg4wPdDPqH2bgJrLxEzDFNBEyPfqgEaeg3suoDTV765ogfAhVMxJzD6rbsUzRRkNqzsvNs2vJnMp5PR34R25gaACa3EkoiRzDDjxfq8cewNo9cZgYHQtuEPm5G7FX6vqo9S96jPihv2ThK7c1mYrKWT4GjVqwWKEyCRK8vFqTjz2e5yh8GAU5hmHF4QzerY3sRYRiFwrxP2uApijpbKneumd3ToijiAtWuD68",
        |    "assets": [
        |        {
        |            "tokenId": "1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db",
        |            "index": 0,
        |            "amount": 18555857,
        |            "name": null,
        |            "decimals": null,
        |            "type": null
        |        }
        |    ],
        |    "additionalRegisters": {},
        |    "spentTransactionId": "883d7a8e5abd00779a098d3e191306e486f854c7de6b6a3bdbb071efb2bddbeb",
        |    "mainChain": true
        |}
        |""".stripMargin
    ).toOption.get

    val redeem: RedeemLegacyV1 =
      RedeemLegacyV1(
        output,
        ERG(10000000),
        PoolId.unsafeFromString("f1fb942ebd039dc782fd9109acdb60aabea4dc7e75e9c813b6528c62692fc781"),
        PublicKeyRedeemer(
          PubKey.unsafeFromString("02c3f56e66191a903758f53a4b90d07cef80f93e7a4f17d106098ad0caf189722a")
        ),
        RedeemParams(
          AssetAmount(
            TokenId.unsafeFromString("1c51c3a53abfe87e6db9a03c649e8360f255ffc4bd34303d30fc7db23ae551db"),
            18555857
          )
        ),
        Version.make.legacyV1,
        OrderType.make.amm,
        Operation.make.redeem
      )

  }
}
