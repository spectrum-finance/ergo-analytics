package fi.spectrum.indexer

import pureconfig.ConfigReader
import sttp.model.Uri
import cats.syntax.either._
import pureconfig.error.CannotConvert
import tofu.logging.Loggable

package object config {

  implicit val uriConfigReader: ConfigReader[Uri] =
    ConfigReader.fromString(s => Uri.parse(s).leftMap(r => CannotConvert(s, "Uri", r)))

  implicit val uriLoggable: Loggable[Uri] = Loggable.stringValue.contramap(_.toString())
}
