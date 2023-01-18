import utils._
import Dependencies._

ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := "2.13.10"

lazy val commonScalacOption = List(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Ymacro-annotations"
)

lazy val root = (project in file("."))
  .settings(name := "ergo-analytics")
  .aggregate(core, streaming, indexer, graphite)

lazy val core = mkModule("core", "core")
  .settings(scalacOptions ++= commonScalacOption)
  .settings(
    libraryDependencies ++= List(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.kindProjector
    )
  )
  .settings(
    libraryDependencies ++= List(
      ce3,
      sigma,
      newtype,
      refined,
      refinedCats,
      mouse,
      kafka,
      pureConfigCE,
      redis,
      rocksDB,
      retry
    ) ++ tofu ++ derevo ++ enums ++ circe ++ tests ++ enums ++ doobie ++ sttp
  )

lazy val streaming = mkModule("streaming", "streaming")
  .settings(scalacOptions ++= commonScalacOption)
  .settings(
    libraryDependencies ++= List(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.kindProjector
    )
  )
  .dependsOn(core)

lazy val parsers = mkModule("parsers", "parsers")
  .settings(libraryDependencies ++= tests)
  .settings(scalacOptions ++= commonScalacOption)
  .settings(
    libraryDependencies ++= List(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.kindProjector
    )
  )
  .dependsOn(core)

lazy val indexer = mkModule("indexer", "indexer")
  .settings(scalacOptions ++= commonScalacOption)
  .settings(dockerBaseImage := "openjdk:11-jre-slim")
  .settings(
    libraryDependencies ++= List(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.kindProjector
    ) ++ tests ++ tofu ++ List(rocksDB)
  )
  .dependsOn(core, parsers, streaming, graphite)
  .settings(
    assembly / mainClass := Some("fi.spectrum.indexer.Main")
  )
  .settings(nativePackagerSettings("indexer"))
  .enablePlugins(JavaAppPackaging, UniversalPlugin, DockerPlugin)

lazy val graphite = mkModule("graphite", "graphite")
  .settings(scalacOptions ++= commonScalacOption)
  .settings(
    libraryDependencies ++= List(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.kindProjector
    ) ++ derevo ++ List(fs2IO)
  )
  .settings(libraryDependencies ++= tofu)

def nativePackagerSettings(moduleSig: String) = List(
  Universal / name := name.value,
  UniversalDocs / name := (Universal / name).value,
  UniversalSrc / name := (Universal / name).value,
  Universal / packageName := packageName.value,
  Universal / mappings += ((Compile / packageBin) map { p =>
    p -> s"lib/$moduleSig.jar"
  }).value,
  dockerExposedVolumes := Seq(s"/var/lib/$moduleSig", "/opt/docker/logs/")
)
