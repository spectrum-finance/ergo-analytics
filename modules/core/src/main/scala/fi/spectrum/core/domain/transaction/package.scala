package fi.spectrum.core.domain

import tofu.logging.Loggable

package object transaction {

  implicit val regsLoggable: Loggable[Map[RegisterId, SConstant]] =
    Loggable.stringValue.contramap { x =>
      x.toString()
    }
}
