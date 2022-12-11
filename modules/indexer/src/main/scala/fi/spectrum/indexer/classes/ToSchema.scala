package fi.spectrum.indexer.classes

/** Transforms entity A into B which can be somehow inserted into db
  * where A is one of the analytics models.
  */
trait ToSchema[A, B] {
  def transform(in: A): B
}
