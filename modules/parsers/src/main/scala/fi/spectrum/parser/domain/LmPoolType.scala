package fi.spectrum.parser.domain

sealed trait LmPoolType

object LmPoolType {
  sealed trait SelfHosted extends LmPoolType
  sealed trait Default extends LmPoolType
}