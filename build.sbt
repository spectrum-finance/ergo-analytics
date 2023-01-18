import utils._
import Dependencies._

ThisBuild / version := "0.1.0-SNAPSHOT"

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
  .aggregate(core, streaming, indexer)

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
      redis
    ) ++ tofu ++ derevo ++ enums ++ circe ++ tests ++ enums ++ doobie ++ sttp
  )

lazy val streaming = mkModule("streaming", "events-streaming")
  .settings(scalacOptions ++= commonScalacOption)
  .settings(
    libraryDependencies ++= List(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.kindProjector
    )
  )
  .dependsOn(core)

lazy val parsers = mkModule("parsers", "order-parsers")
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
  .settings(
    libraryDependencies ++= List(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.kindProjector
    ) ++ tests ++ tofu
  )
  .dependsOn(core, parsers, streaming)
