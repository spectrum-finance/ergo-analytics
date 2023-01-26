package fi.spectrum.api.repositories

trait Blocks[F[_]] {
  def getCurrentHeight: F[Int]
}
