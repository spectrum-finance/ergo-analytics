package fi.spectrum.parser.amm.order.v3

import cats.syntax.option._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.SPF
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit._
import fi.spectrum.core.domain.order.Order.Redeem.AmmRedeem.RedeemV3
import fi.spectrum.core.domain.order.Order.Swap.SwapV3
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.Redeemer.ErgoTreeRedeemer
import fi.spectrum.core.domain.analytics.Version.V3
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.T2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.T2T._
import sigmastate.Values

class T2TAmmOrderParser extends AmmOrderParser[V3, T2T] {

  def swap(box: Output, tree: Values.ErgoTree): Option[Swap] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == swapV3,
        for {
          poolId        <- tree.constants.parseBytea(19).map(PoolId.fromBytes)
          maxMinerFee   <- tree.constants.parseLong(32)
          spectrumId    <- tree.constants.parseBytea(4).map(TokenId.fromBytes)
          reservedExFee <- tree.constants.parseLong(2)
          inAmount      <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          outId         <- tree.constants.parseBytea(1).map(TokenId.fromBytes)
          minOutAmount  <- tree.constants.parseLong(21)
          outAmount = AssetAmount(outId, minOutAmount)
          dexFeePerTokenNum   <- tree.constants.parseLong(24)
          dexFeePerTokenDenom <- tree.constants.parseLong(25)
          redeemer            <- tree.constants.parseBytea(20).map(SErgoTree.fromBytes)
          params = SwapParams(
                     if (spectrumId == inAmount.tokenId) inAmount - reservedExFee else inAmount,
                     outAmount,
                     dexFeePerTokenNum,
                     dexFeePerTokenDenom
                   )
        } yield SwapV3(
          box,
          poolId,
          ErgoTreeRedeemer(redeemer),
          params,
          maxMinerFee,
          reservedExFee,
          Version.V3
        ),
        none
      )
      .merge

  def deposit(box: Output, tree: Values.ErgoTree): Option[AmmDeposit] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == depositV3,
        for {
          poolId      <- tree.constants.parseBytea(17).map(PoolId.fromBytes)
          maxMinerFee <- tree.constants.parseLong(27)
          dexFeeFromX <- tree.constants.parseBoolean(9)
          dexFeeFromY <- tree.constants.parseBoolean(13)
          inX         <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          inY         <- box.assets.lift(1).map(a => AssetAmount(a.tokenId, a.amount))
          dexFee      <- tree.constants.parseLong(10)
          redeemer    <- tree.constants.parseBytea(17).map(SErgoTree.fromBytes)
          params = AmmDepositParams(
                     if (dexFeeFromX) inX - dexFee else inX,
                     if (dexFeeFromY) inY - dexFee else inY
                   )
        } yield AmmDepositV3(
          box,
          SPF(dexFee),
          poolId,
          ErgoTreeRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.V3
        ),
        none
      )
      .merge

  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == redeemV3,
        for {
          poolId      <- tree.constants.parseBytea(13).map(PoolId.fromBytes)
          maxMinerFee <- tree.constants.parseLong(18)
          inLP        <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          dexFee      <- box.assets.lift(1).map(a => AssetAmount(a.tokenId, a.amount))
          redeemer    <- tree.constants.parseBytea(14).map(SErgoTree.fromBytes)
          params = RedeemParams(inLP)
        } yield RedeemV3(
          box,
          SPF(dexFee.amount),
          poolId,
          ErgoTreeRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.V3
        ),
        none
      )
      .merge
}

object T2TAmmOrderParser {
  implicit def t2tV3: AmmOrderParser[V3, T2T] = new T2TAmmOrderParser
}
