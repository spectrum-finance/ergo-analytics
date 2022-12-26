package fi.spectrum.streaming

import derevo.derive
import io.estatico.newtype.macros.newtype
import pureconfig.ConfigReader
import tofu.logging.derivation.loggable

package object kafka {

  @derive(loggable)
  @newtype case class TopicId(value: String)

  object TopicId {
    implicit val configReader: ConfigReader[TopicId] = ConfigReader.stringConfigReader.map(TopicId(_))
  }

  @derive(loggable)
  @newtype case class GroupId(value: String)

  object GroupId {
    implicit val configReader: ConfigReader[GroupId] = ConfigReader.stringConfigReader.map(GroupId(_))
  }

  @derive(loggable)
  @newtype case class ClientId(value: String)

  object ClientId {
    implicit val configReader: ConfigReader[ClientId] = ConfigReader.stringConfigReader.map(ClientId(_))
  }

}
