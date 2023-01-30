import sbt._

object Dependencies {

  object Version {
    val ce3          = "3.4.2"
    val tofu         = "0.11.0"
    val glass        = "0.1.0"
    val derevo       = "0.13.0"
    val newtype      = "0.4.4"
    val refined      = "0.10.1"
    val enumeratum   = "1.7.0"
    val circe        = "0.14.1"
    val mouse        = "1.2.1"
    val doobie       = "1.0.0-RC2"
    val fs2Kafka     = "2.5.0"
    val doobieTest   = "0.13.4"
    val pureConfig   = "0.17.2"
    val redis4Cats   = "1.3.0"
    val sttp         = "3.8.5"
    val rocksDB      = "0.2.1"
    val retry        = "3.1.0"
    val fs2IO        = "3.5.0"
    val tapir        = "1.0.0"
    val tapirOpenapi = "0.20.2"
    val tapirRedoc   = "0.19.0-M4"
    val scodecCore   = "1.11.7"
    val scodecBits   = "1.1.21"
    val jawnFs2      = "2.0.0"
    val scodecCats   = "1.1.0"
    val http4s       = "0.23.12"
    val zioInterop   = "3.1.1.0"

    val scalaCheckVersion             = "1.14.1"
    val scalaCheckShapelessVersion    = "1.2.5"
    val testContainersPostgresVersion = "1.7.3"
    val testContainersScalaVersion    = "0.40.11"
    val scalaTestVersion              = "3.2.14"
    val scalaTestPlusVersion          = "3.2.2.0"
    val flywayVersion                 = "9.8.3"

    val sigma = "7.7.7"

    val betterMonadicFor = "0.3.1"
    val kindProjector    = "0.13.2"
  }

  val ce3        = "org.typelevel"    %% "cats-effect-kernel" % Version.ce3
  val fs2IO      = "co.fs2"           %% "fs2-io"             % Version.fs2IO
  val kafka      = "com.github.fd4s"  %% "fs2-kafka"          % Version.fs2Kafka
  val retry      = "com.github.cb372" %% "cats-retry"         % Version.retry
  val zioInterop = "dev.zio"          %% "zio-interop-cats"   % Version.zioInterop

  val rocksDB = "io.github.oskin1" %% "rocks4cats-scodec" % Version.rocksDB

  val newtype     = "io.estatico" %% "newtype"      % Version.newtype
  val refined     = "eu.timepit"  %% "refined"      % Version.refined
  val refinedCats = "eu.timepit"  %% "refined-cats" % Version.refined

  val mouse        = "org.typelevel"         %% "mouse"                  % Version.mouse
  val redis        = "dev.profunktor"        %% "redis4cats-effects"     % Version.redis4Cats
  val pureConfigCE = "com.github.pureconfig" %% "pureconfig-cats-effect" % Version.pureConfig

  val sttp =
    List(
      "com.softwaremill.sttp.client3" %% "async-http-client-backend-fs2" % Version.sttp,
      "com.softwaremill.sttp.client3" %% "core"                          % Version.sttp,
      "com.softwaremill.sttp.client3" %% "circe"                         % Version.sttp,
      "com.softwaremill.sttp.client3" %% "okhttp-backend"                % Version.sttp % Test
    )

  val tapir =
    List(
      "com.softwaremill.sttp.tapir" %% "tapir-core"               % Version.tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe"         % Version.tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"      % Version.tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % Version.tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % Version.tapirOpenapi,
      "com.softwaremill.sttp.tapir" %% "tapir-redoc-http4s"       % Version.tapirRedoc
    )

  val http4s = List(
    "org.http4s" %% "http4s-blaze-server" % Version.http4s
  )

  val jawnFs2 = List("org.typelevel" %% "jawn-fs2" % Version.jawnFs2)

  val scodec = List(
    "org.scodec" %% "scodec-core" % Version.scodecCore,
    "org.scodec" %% "scodec-bits" % Version.scodecBits,
    "org.scodec" %% "scodec-cats" % Version.scodecCats
  )

  val doobie = List(
    "org.tpolecat" %% "doobie-postgres" % Version.doobie,
    "org.tpolecat" %% "doobie-hikari"   % Version.doobie,
    "org.tpolecat" %% "doobie-refined"  % Version.doobie
  )

  val enums: List[ModuleID] = List(
    "com.beachape" %% "enumeratum"       % Version.enumeratum,
    "com.beachape" %% "enumeratum-circe" % Version.enumeratum
  )

  val circe: List[ModuleID] =
    List(
      "io.circe" %% "circe-core"    % Version.circe,
      "io.circe" %% "circe-generic" % Version.circe,
      "io.circe" %% "circe-parser"  % Version.circe,
      "io.circe" %% "circe-refined" % Version.circe
    )

  val tofu = List(
    "tf.tofu" %% "tofu-core-ce3"           % Version.tofu,
    "tf.tofu" %% "tofu-doobie-ce3"         % Version.tofu,
    "tf.tofu" %% "tofu-fs2-ce3-interop"    % Version.tofu,
    "tf.tofu" %% "tofu-derivation"         % Version.tofu,
    "tf.tofu" %% "tofu-streams"            % Version.tofu,
    "tf.tofu" %% "tofu-logging"            % Version.tofu,
    "tf.tofu" %% "tofu-doobie-logging-ce3" % Version.tofu,
    "tf.tofu" %% "glass"                   % Version.glass
  )

  val derevo = List(
    "tf.tofu" %% "derevo-core"         % Version.derevo,
    "tf.tofu" %% "derevo-cats"         % Version.derevo,
    "tf.tofu" %% "derevo-cats-tagless" % Version.derevo,
    "tf.tofu" %% "derevo-circe"        % Version.derevo,
    "tf.tofu" %% "derevo-pureconfig"   % Version.derevo
  )

  val tests = List(
    "org.flywaydb"                % "flyway-core"                     % Version.flywayVersion              % Test,
    "org.tpolecat"               %% "doobie-scalatest"                % Version.doobieTest                 % Test,
    "org.scalatest"              %% "scalatest"                       % Version.scalaTestVersion           % Test,
    "org.scalatestplus"          %% "scalacheck-1-14"                 % Version.scalaTestPlusVersion       % Test,
    "com.dimafeng"               %% "testcontainers-scala-postgresql" % Version.testContainersScalaVersion % Test,
    "com.dimafeng"               %% "testcontainers-scala"            % Version.testContainersScalaVersion % Test,
    "com.github.alexarchambault" %% "scalacheck-shapeless_1.14"       % Version.scalaCheckShapelessVersion % Test
  )

  val sigma = "org.scorexfoundation" %% "sigma-state" % Version.sigma

  object CompilerPlugins {

    val betterMonadicFor = compilerPlugin(
      "com.olegpy" %% "better-monadic-for" % Version.betterMonadicFor
    )

    val kindProjector = compilerPlugin(
      "org.typelevel" % "kind-projector" % Version.kindProjector cross CrossVersion.full
    )
  }
}
