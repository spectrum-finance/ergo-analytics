package fi.spectrum.parser.lm.order.v1

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.order.Order.Deposit
import fi.spectrum.core.domain.order.Order.Deposit.LmDeposit.LmDepositV1
import fi.spectrum.core.domain.order.Redeemer.ErgoTreeRedeemer
import fi.spectrum.core.domain.order.{LmDepositParams, Order, PoolId}
import fi.spectrum.core.domain.transaction.Output
import fi.spectrum.core.domain.{AssetAmount, ErgoTreeTemplate, SErgoTree}
import fi.spectrum.parser.lm.compound.CompoundParser
import fi.spectrum.parser.lm.order.LmOrderParser
import fi.spectrum.parser.syntax._
import fi.spectrum.parser.templates.LM.depositV1
import sigmastate.Values

final class LmOrderParserV1(parser: CompoundParser[V1]) extends LmOrderParser[V1] {

  def deposit(box: Output, tree: Values.ErgoTree): Option[Deposit.LmDeposit] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == depositV1,
        for {
          poolId            <- tree.constants.parseBytea(1).map(PoolId.fromBytes)
          redeemer          <- tree.constants.parseBytea(3).map(SErgoTree.fromBytes)
          maxMinerFee       <- tree.constants.parseLong(21)
          expectedNumEpochs <- tree.constants.parseInt(14)
          tokens            <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          params = LmDepositParams(expectedNumEpochs, tokens)
        } yield LmDepositV1(
          box,
          poolId,
          ErgoTreeRedeemer(redeemer),
          params,
          maxMinerFee,
          Version.V1
        ),
        none
      )
      .merge

  def compound(box: Output, tree: Values.ErgoTree): Option[Order.Compound] =
    parser.compound(box, tree)
}

object LmOrderParserV1 {
  implicit def v1(implicit parser: CompoundParser[V1]): LmOrderParser[V1] = new LmOrderParserV1(parser)
}
