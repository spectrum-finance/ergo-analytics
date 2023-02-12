package fi.spectrum.parser.lm.compound.v1

import cats.syntax.option._
import fi.spectrum.core.domain.analytics.Version.V1
import fi.spectrum.core.domain.analytics.Version
import fi.spectrum.core.domain.order.Order.Compound
import fi.spectrum.core.domain.order.Order.Compound.CompoundV1
import fi.spectrum.core.domain.order.{LmCompoundParams, PoolId}
import fi.spectrum.core.domain.order.Redeemer.PublicKeyRedeemer
import fi.spectrum.core.domain.transaction.SConstant.{ByteaConstant, SigmaPropConstant}
import fi.spectrum.core.domain.transaction.{Output, RegisterId}
import fi.spectrum.core.domain.{AssetAmount, ErgoTreeTemplate, TokenId}
import fi.spectrum.parser.lm.compound.CompoundParser
import fi.spectrum.parser.templates.LM.bundleV1
import sigmastate.Values

final class CompoundParserV1 extends CompoundParser[V1] {

  def compound(box: Output, tree: Values.ErgoTree): Option[Compound] =
    Either
      .cond(
        ErgoTreeTemplate.fromBytes(tree.template) == bundleV1,
        for {
          redeemer <- box.additionalRegisters.get(RegisterId.R4).collect { case SigmaPropConstant(x) => x }
          poolId <- box.additionalRegisters
                      .get(RegisterId.R5)
                      .collect { case ByteaConstant(x) => x }
                      .map(x => PoolId(TokenId(x)))
          vLq <- box.assets.headOption.map(a => AssetAmount(a.tokenId, a.amount))
          maybeTmp   = box.assets.lift(1).map(a => AssetAmount(a.tokenId, a.amount))
          maybeKeyId = box.assets.lift(2).map(a => AssetAmount(a.tokenId, a.amount)).map(_.tokenId)
          bundleKeyId <- maybeKeyId.orElse(maybeTmp.map(_.tokenId))
          tmp = if (maybeKeyId.isEmpty) none else maybeTmp
        } yield CompoundV1(
          box,
          LmCompoundParams(vLq, tmp, bundleKeyId),
          poolId,
          PublicKeyRedeemer(redeemer),
          Version.V1
        ),
        none
      )
      .merge
}

object CompoundParserV1 {
  implicit def v1Compound: CompoundParser[V1] = new CompoundParserV1
}
