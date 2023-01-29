package fi.spectrum.streaming.kafka.config

import derevo.derive
import derevo.pureconfig.pureconfigReader
import fi.spectrum.streaming.kafka.streaming._
import tofu.WithContext
import tofu.logging.derivation.loggable

@derive(pureconfigReader, loggable)
final case class ConsumerConfig(
  groupId: GroupId,
  clientId: ClientId,
  topicId: TopicId
)

object ConsumerConfig extends WithContext.Companion[ConsumerConfig]
