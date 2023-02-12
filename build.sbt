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
  "-Ymacro-annotations",
  "-Ywarn-value-discard"
)

def compilerDependencies = List(CompilerPlugins.betterMonadicFor, CompilerPlugins.kindProjector)

lazy val root = (project in file("."))
  .settings(name := "ergo-analytics")
  .aggregate(core, streaming, indexer, graphite, cache, api, mempool, common)

lazy val core = mkModule("core", "core")
  .settings(scalacOptions ++= commonScalacOption)
  .settings(
    libraryDependencies ++= compilerDependencies ++ List(
      ce3, sigma, newtype, refined, refinedCats, mouse, kafka, pureConfigCE, redis, rocksDB, retry, redis
    ) ++ tofu ++ derevo ++ enums ++ circe ++ tests ++ enums ++ doobie ++ sttp ++ scodec ++ tapir
  )

lazy val streaming = mkModule("streaming", "streaming")
  .settings(scalacOptions ++= commonScalacOption)
  .settings(libraryDependencies ++= compilerDependencies)
  .dependsOn(core)

lazy val parsers = mkModule("parsers", "parsers")
  .settings(libraryDependencies ++= tests)
  .settings(scalacOptions ++= commonScalacOption)
  .settings(libraryDependencies ++= compilerDependencies)
  .dependsOn(core)

lazy val api = mkModule("api", "api")
  .settings(scalacOptions ++= commonScalacOption)
  .settings(dockerBaseImage := "openjdk:11-jre-slim")
  .settings(libraryDependencies ++= compilerDependencies ++ scodec ++ http4s ++ tapir ++ tests ++ List(zioInterop))
  .dependsOn(common, graphite, cache)
  .settings(assembly / mainClass := Some("fi.spectrum.api.Main"))
  .settings(nativePackagerSettings("api"))
  .enablePlugins(JavaAppPackaging, UniversalPlugin, DockerPlugin)

lazy val indexer = mkModule("indexer", "indexer")
  .settings(scalacOptions ++= commonScalacOption)
  .settings(dockerBaseImage := "openjdk:11-jre-slim")
  .settings(libraryDependencies ++= compilerDependencies ++ tests ++ tofu ++ List(rocksDB))
  .dependsOn(common, streaming, graphite, cache)
  .settings(assembly / mainClass := Some("fi.spectrum.indexer.Main"))
  .settings(nativePackagerSettings("indexer"))
  .enablePlugins(JavaAppPackaging, UniversalPlugin, DockerPlugin)

lazy val graphite = mkModule("graphite", "graphite")
  .settings(scalacOptions ++= commonScalacOption)
  .settings(libraryDependencies ++= compilerDependencies ++ derevo ++ List(fs2IO) ++ http4s ++ tofu)

lazy val cache = mkModule("cache", "cache")
  .settings(scalacOptions ++= commonScalacOption)
  .settings(libraryDependencies ++= compilerDependencies ++ derevo ++ List(fs2IO) ++ tofu)
  .dependsOn(core, streaming)

lazy val mempool = mkModule("mempool", "mempool")
  .settings(scalacOptions ++= commonScalacOption)
  .settings(dockerBaseImage := "openjdk:11-jre-slim")
  .settings(libraryDependencies ++= compilerDependencies ++ tests ++ tofu)
  .dependsOn(core, common, streaming, graphite, cache)
  .settings(assembly / mainClass := Some("fi.spectrum.mempool.Main"))
  .settings(nativePackagerSettings("mempool"))
  .enablePlugins(JavaAppPackaging, UniversalPlugin, DockerPlugin)

lazy val common = mkModule("common", "common")
  .settings(scalacOptions ++= commonScalacOption)
  .settings(libraryDependencies ++= compilerDependencies)
  .dependsOn(parsers)

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
