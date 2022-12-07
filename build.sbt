import utils._
import Dependencies._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(name := "ergo-analytics")
  .aggregate(core, streaming)

lazy val core = mkModule("core", "core")

lazy val streaming = mkModule("streaming", "events-streaming")
  .settings(libraryDependencies ++= List(ce3, sigma) ++ tofu ++ derevo)
  .dependsOn(core)
