package fi.spectrum.api.db

import cats.effect.IO
import doobie.ConnectionIO
import doobie.util.log.LogHandler
import fi.spectrum.core.domain.TokenId
import org.ergoplatform.ErgoAddressEncoder
import tofu.doobie.log.EmbeddableLogHandler
import tofu.logging.Logging.Make
import tofu.logging.{Logging, Logs}

trait Api {

  implicit val spf: TokenId = TokenId.unsafeFromString("")

  implicit def lh: LogHandler = LogHandler.jdkLogHandler

  implicit def logs: Logs[IO, IO] = Logs.sync[IO, IO]

  implicit def logs2: Logs[IO, ConnectionIO] = Logs.sync[IO, ConnectionIO]

  implicit def logging: Make[IO] = Logging.Make.plain[IO]

  implicit def addressEncoder: ErgoAddressEncoder = ErgoAddressEncoder(ErgoAddressEncoder.MainnetNetworkPrefix)

  implicit def elh: EmbeddableLogHandler[ConnectionIO] = EmbeddableLogHandler.nop
}
