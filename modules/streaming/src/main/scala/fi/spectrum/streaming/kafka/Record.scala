package fi.spectrum.streaming.kafka

final case class Record[K, V](key: K, value: V)
