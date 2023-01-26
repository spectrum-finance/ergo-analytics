package fi.spectrum.api.processes

trait SnapshotsProcess[S[_]] {
  def run: S[Unit]
}
