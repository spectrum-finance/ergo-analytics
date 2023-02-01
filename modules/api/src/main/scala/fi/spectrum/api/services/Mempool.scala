package fi.spectrum.api.services

import fi.spectrum.api.models.MempoolKey
import fi.spectrum.core.domain.analytics.Processed

trait Mempool[F[_]] {
  def get: F[Map[MempoolKey, Processed.Any]]
  def update(o: Processed.Any): F[Unit]
  def restore: F[Unit]
}
