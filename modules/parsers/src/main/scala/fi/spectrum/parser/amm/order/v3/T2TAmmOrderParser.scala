package fi.spectrum.parser.amm.order.v3

import cats.syntax.option._
import cats.syntax.either._
import fi.spectrum.core.domain._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Fee.SPF
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit._
import fi.spectrum.core.domain.order.Order.Redeem.AmmRedeem.RedeemV3
import fi.spectrum.core.domain.order.Order.Swap.SwapV3
import fi.spectrum.core.domain.order.Order._
import fi.spectrum.core.domain.order.Redeemer.{ErgoTreeRedeemer, PublicKeyRedeemer}
import fi.spectrum.core.domain.analytics.Version.V3
import fi.spectrum.core.domain.order.Order.Deposit.AmmDeposit
import fi.spectrum.core.domain.order._
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.protocol.ErgoTreeSerializer
import fi.spectrum.parser.amm.order.AmmOrderParser
import fi.spectrum.parser.domain.AmmType.T2T
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.T2T._
import org.ergoplatform.{ErgoAddressEncoder, P2PKAddress}
import sigmastate.Values

class T2TAmmOrderParser(implicit spf: TokenId, e: ErgoAddressEncoder) extends AmmOrderParser[V3, T2T] {

  def swap(box: Output, tree: Values.ErgoTree): Option[Swap] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == swapV3,
        for {
          poolId        <- tree.constants.parseBytea(18).map(PoolId.fromBytes)
          maxMinerFee   <- tree.constants.parseLong(33)
          reservedExFee <- tree.constants.parseLong(12)
          baseAmount    <- tree.constants.parseLong(3)
          inAmount      <- box.assets.headOption.map(a => AssetAmount(a.tokenId, baseAmount))
          outId         <- tree.constants.parseBytea(1).map(TokenId.fromBytes)
          minOutAmount  <- tree.constants.parseLong(20)
          outAmount = AssetAmount(outId, minOutAmount)
          dexFeePerTokenDenom   <- tree.constants.parseLong(2)
          dexFeePerTokenNumDiff <- tree.constants.parseLong(13)
          dexFeePerTokenNum = dexFeePerTokenDenom - dexFeePerTokenNumDiff
          redeemer <- tree.constants.parseBytea(19).map(SErgoTree.fromBytes).map(mapRedeemer)
          params = SwapParams(
                     inAmount,
                     outAmount,
                     dexFeePerTokenNum,
                     dexFeePerTokenDenom
                   )
        } yield SwapV3(
          box,
          poolId,
          redeemer,
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
          poolId      <- tree.constants.parseBytea(13).map(PoolId.fromBytes)
          maxMinerFee <- tree.constants.parseLong(24)
          selfX       <- tree.constants.parseLong(8)
          selfY       <- tree.constants.parseLong(10)
          inX         <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          inY         <- box.assets.lift(1).map(a => AssetAmount(a.tokenId, a.amount))
          dexFee <- if (inX.tokenId == spf) (inX.amount - selfX).some
                    else if (inY.tokenId == spf) (inY.amount - selfY).some
                    else box.assets.find(_.tokenId == spf).map(_.amount)
          redeemer <- tree.constants.parseBytea(14).map(SErgoTree.fromBytes).map(mapRedeemer)
          params = AmmDepositParams(inX.withAmount(selfX), inY.withAmount(selfY))
        } yield AmmDepositV3(
          box,
          SPF(dexFee),
          poolId,
          redeemer,
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
          redeemer    <- tree.constants.parseBytea(14).map(SErgoTree.fromBytes).map(mapRedeemer)
          params = RedeemParams(inLP)
        } yield RedeemV3(
          box,
          SPF(dexFee.amount),
          poolId,
          redeemer,
          params,
          maxMinerFee,
          Version.V3
        ),
        none
      )
      .merge

  private def mapRedeemer(redeemer: SErgoTree): Redeemer =
    Either
      .catchNonFatal {
        e.fromProposition(ErgoTreeSerializer.default.deserialize(redeemer)).toOption.map {
          case address: P2PKAddress => PublicKeyRedeemer(PubKey.fromBytes(address.pubkeyBytes))
          case _                    => ErgoTreeRedeemer(redeemer)
        }
      }
      .toOption
      .flatten
      .getOrElse(ErgoTreeRedeemer(redeemer))
}

object T2TAmmOrderParser {
  implicit def t2tV3(implicit spf: TokenId, e: ErgoAddressEncoder): AmmOrderParser[V3, T2T] = new T2TAmmOrderParser
}
