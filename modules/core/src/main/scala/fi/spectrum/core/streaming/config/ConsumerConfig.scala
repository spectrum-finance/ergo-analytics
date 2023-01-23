package fi.spectrum.core.streaming.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.core.streaming.{ClientId, GroupId, TopicId}
import tofu.WithContext
import tofu.logging.derivation.loggable

@derive(pureconfigReader, loggable)
final case class ConsumerConfig(
  groupId: GroupId,
  clientId: ClientId,
  topicId: TopicId
)

object ConsumerConfig extends WithContext.Companion[ConsumerConfig]
