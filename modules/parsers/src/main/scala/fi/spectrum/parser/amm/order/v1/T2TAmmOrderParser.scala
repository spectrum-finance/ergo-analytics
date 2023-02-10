package fi.spectrum.parser.amm.order.v1

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.ERG
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit._
import fi.spectrum.core.domain.order.Order.Redeem.AmmRedeem.RedeemV1
import fi.spectrum.core.domain.order.Order.Swap.SwapV1
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, ErgoTreeTemplate, PubKey, TokenId}
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.T2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.T2T._
import sigmastate.Values

final class T2TAmmOrderParser extends AmmOrderParser[V1, T2T] {

  def swap(box: Output, tree: Values.ErgoTree): Option[Swap] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == swapV1,
        for {
          poolId       <- tree.constants.parseBytea(14).map(PoolId.fromBytes)
          maxMinerFee  <- tree.constants.parseLong(21)
          inAmount     <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          outId        <- tree.constants.parseBytea(2).map(TokenId.fromBytes)
          minOutAmount <- tree.constants.parseLong(15)
          outAmount = AssetAmount(outId, minOutAmount)
          dexFeePerTokenNum   <- tree.constants.parseLong(16)
          dexFeePerTokenDenom <- tree.constants.parseLong(17)
          redeemer            <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = SwapParams(inAmount, outAmount, dexFeePerTokenNum, dexFeePerTokenDenom)
        } yield SwapV1(
          box,
          poolId,
          PublicKeyRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.V1
        ),
        none
      )
      .merge

  def deposit(box: Output, tree: Values.ErgoTree): Option[AmmDeposit] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == depositV1,
        for {
          poolId      <- tree.constants.parseBytea(13).map(PoolId.fromBytes)
          maxMinerFee <- tree.constants.parseLong(25)
          inX         <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          inY         <- box.assets.lift(1).map(a => AssetAmount(a.tokenId, a.amount))
          dexFee      <- tree.constants.parseLong(15)
          redeemer    <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = AmmDepositParams(inX, inY)
        } yield AmmDepositV1(
          box,
          ERG(dexFee),
          poolId,
          PublicKeyRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.V1
        ),
        none
      )
      .merge

  def redeem(box: Output, tree: Values.ErgoTree): Option[Redeem] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == redeemV1,
        for {
          poolId      <- tree.constants.parseBytea(13).map(PoolId.fromBytes)
          maxMinerFee <- tree.constants.parseLong(19)
          inLP        <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          dexFee      <- tree.constants.parseLong(15)
          redeemer    <- tree.constants.parsePk(0).map(pk => PubKey.fromBytes(pk.pkBytes))
          params = RedeemParams(inLP)
        } yield RedeemV1(
          box,
          ERG(dexFee),
          poolId,
          PublicKeyRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.V1
        ),
        none
      )
      .merge
}

object T2TAmmOrderParser {
  implicit def t2tV1: AmmOrderParser[V1, T2T] = new T2TAmmOrderParser
}
