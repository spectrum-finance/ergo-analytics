package fi.spectrum.core.streaming

final case class Record[K, V](key: K, value: V)
