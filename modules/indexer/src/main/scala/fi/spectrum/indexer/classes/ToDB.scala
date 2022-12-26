package fi.spectrum.indexer.classes

trait ToDB[A, B] {
  def toDB(a: A): B
}
