package fi.spectrum.indexer.db

import cats.effect.IO
import doobie.ConnectionIO
import doobie.util.log.LogHandler
import org.ergoplatform.ErgoAddressEncoder
import tofu.doobie.log.EmbeddableLogHandler
import tofu.logging.Logging.Make
import tofu.logging.{Logging, Logs}

trait Indexer {
  implicit def lh: LogHandler = LogHandler.jdkLogHandler

  implicit def logs: Logs[IO, IO]                 = Logs.sync[IO, IO]
  implicit def logging: Make[IO]                  = Logging.Make.plain[IO]
  implicit def addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)

  implicit def elh: EmbeddableLogHandler[ConnectionIO] = EmbeddableLogHandler.nop
}
