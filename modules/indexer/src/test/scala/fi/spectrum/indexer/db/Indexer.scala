package fi.spectrum.indexer.db

import cats.effect.IO
import doobie.ConnectionIO
import doobie.util.log.LogHandler
import fi.spectrum.core.domain.TokenId
import fi.spectrum.parser.evaluation.ProcessedOrderParser
import org.ergoplatform.ErgoAddressEncoder
import tofu.doobie.log.EmbeddableLogHandler
import tofu.logging.Logs

trait Indexer {
  implicit val lh: LogHandler = LogHandler.jdkLogHandler

  implicit val logs: Logs[IO, IO]                 = Logs.sync[IO, IO]
  implicit def addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)

  implicit val elh: EmbeddableLogHandler[ConnectionIO] = EmbeddableLogHandler.nop
}
