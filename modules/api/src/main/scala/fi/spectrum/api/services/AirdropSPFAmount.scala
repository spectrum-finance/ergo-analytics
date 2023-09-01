package fi.spectrum.api.services

import cats.Monad
 import cats.data.OptionT
 import cats.effect.Ref
 import cats.effect.kernel.Sync
 import fi.spectrum.api.configs.PriceTrackingConfig
 import fi.spectrum.api.models.FiatUnits
 import fi.spectrum.core.domain.TxId
 import tofu.higherKind.{Mid, RepresentableK}
 import tofu.lift.Lift
 import tofu.logging.{Logging, Logs}
 import tofu.syntax.foption.FOptionOps
 import tofu.syntax.lift._
 import tofu.syntax.logging._
 import tofu.syntax.monadic._

 trait AirdropSPFAmount[F[_]] {

   def get: F[Option[Long]]

   def update: F[Unit]
 }

 object AirdropSPFAmount {

   implicit def representableK: RepresentableK[AirdropSPFAmount] =
     tofu.higherKind.derived.genRepresentableK

   def make[I[_]: Sync, F[_]: Sync: PriceTrackingConfig.Has](implicit
     explorer: Network[F],
     logs: Logs[I, F],
     lift: Lift[F, I]
   ): I[AirdropSPFAmount[F]] =
     for {
       implicit0(logging: Logging[F]) <- logs.forService[AirdropSPFAmount[F]]
       cache                          <- Ref.in[I, F, Option[Long]](None)
       config                         <- PriceTrackingConfig.access.lift
     } yield new Tracing[F] attach new Live[F](cache, config)

   final private[services] class Live[F[_]: Monad: Logging](
     cache: Ref[F, Option[Long]],
     config: PriceTrackingConfig
   )(implicit explorer: Network[F])
     extends AirdropSPFAmount[F] {

     def get: F[Option[Long]] = cache.get

     def update: F[Unit] = for {
       amount <- trackSPFAmount(TxId(config.initAirdropTx))
       _      <- cache.set(amount)
     } yield ()

     private def trackSPFAmount(txId: TxId): F[Option[Long]] =
       (for {
         tx     <- OptionT(explorer.getTransactionById(txId))
         box    <- OptionT.fromOption[F](tx.outputs.headOption)
         amount <- OptionT(box.spentTransactionId.fold(box.assets.map(_.amount).sum.someF)(trackSPFAmount))
       } yield amount).value
   }

   final class Tracing[F[_]: Logging: Monad] extends AirdropSPFAmount[Mid[F, *]] {

     def get: Mid[F, Option[Long]] = trace"Get current not distributed SPF" >> _

     def update: Mid[F, Unit] = info"It's time to update not distributed SPF amount!" >> _

   }
 }