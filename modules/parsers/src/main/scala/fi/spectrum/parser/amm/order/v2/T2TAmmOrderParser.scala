package fi.spectrum.parser.amm.order.v2

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Order.Swap.SwapV2
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.OrderType.AMM
import fi.spectrum.core.domain.order.Redeemer.ErgoTreeRedeemer
import fi.spectrum.core.domain.analytics.Version.V2
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.T2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.T2T._
import sigmastate.Values

final class T2TAmmOrderParser extends AmmOrderParser[V2, T2T] {

  def swap(box: Output, tree: Values.ErgoTree): Option[Swap[V2, AMM]] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == swapV2,
        for {
          poolId       <- tree.constants.parseBytea(14).map(PoolId.fromBytes)
          maxMinerFee  <- tree.constants.parseLong(22)
          inAmount     <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          outId        <- tree.constants.parseBytea(1).map(TokenId.fromBytes)
          minOutAmount <- tree.constants.parseLong(16)
          outAmount = AssetAmount(outId, minOutAmount)
          dexFeePerTokenNum   <- tree.constants.parseLong(17)
          dexFeePerTokenDenom <- tree.constants.parseLong(18)
          redeemer            <- tree.constants.parseBytea(15).map(SErgoTree.fromBytes)
          params = SwapParams(
                     inAmount,
                     outAmount,
                     dexFeePerTokenNum,
                     dexFeePerTokenDenom
                   )
        } yield SwapV2(
          box,
          poolId,
          ErgoTreeRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.V2,
          OrderType.AMM,
          Operation.Swap
        ),
        none
      )
      .merge

  def deposit(box: Output, tree: Values.ErgoTree): Option[Deposit[V2, AMM]] = none

  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem[V2, AMM]] = none
}

object T2TAmmOrderParser {
  implicit def t2tV2: AmmOrderParser[V2, T2T] = new T2TAmmOrderParser
}