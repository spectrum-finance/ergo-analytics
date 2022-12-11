package fi.spectrum.indexer

import cats.{FlatMap, Monad}
import fi.spectrum.core.domain.analytics.ProcessedOrder
import fi.spectrum.indexer.classes.Handle
import fi.spectrum.indexer.db.persistence.PersistBundle
import fi.spectrum.indexer.models.{RedeemDB, SwapDB}
import tofu.doobie.LiftConnectionIO
import tofu.doobie.log.EmbeddableLogHandler
import tofu.doobie.transactor.Txr
import tofu.syntax.monadic._
import fi.spectrum.indexer.models.RedeemDB.{toSchema, _}
import fi.spectrum.indexer.models.SwapDB.{toSchema, _}
import fi.spectrum.indexer.processes.{FromNetwork, OrdersProcessor}
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import fs2.Chunk
import tofu.streams.{Chunks, Evals}

object Main {

  def run[S[_]: Evals[*[_], F]: Chunks[*[_], Chunk]: Monad, F[_]: Monad, D[_]: FlatMap: LiftConnectionIO](implicit
    elh: EmbeddableLogHandler[D],
    txr: Txr[F, D]
  ) = {
    val bundle   = PersistBundle.make[D, F]
    val handle1  = Handle.makeOptional[ProcessedOrder, RedeemDB, F](bundle.redeems)
    val handle2  = Handle.makeOptional[ProcessedOrder, SwapDB, F](bundle.swaps)
    val process1 = OrdersProcessor.make[Chunk, F, S](???, List(handle1, handle2))
    val parser   = ProcessedOrderParser.make
    val process2 = FromNetwork.make[Chunk, F, S](???, parser)
  }

}
