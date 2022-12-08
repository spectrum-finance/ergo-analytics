package fi.spectrum.parser.domain

sealed trait AmmType

object AmmType {
  sealed trait N2T extends AmmType
  sealed trait T2T extends AmmType
}