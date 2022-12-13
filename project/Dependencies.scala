import sbt._

object Dependencies {

  object Version {
    val ce3            = "3.4.2"
    val tofu           = "0.11.0"
    val glass          = "0.1.0"
    val derevo         = "0.13.0"
    val newtype        = "0.4.4"
    val refined        = "0.10.1"
    val enumeratum     = "1.7.0"
    val circe          = "0.14.1"
    val mouse          = "1.2.1"
    val doobiePostgres = "1.0.0-RC2"
    val fs2Kafka       = "2.5.0"
    val doobieVersion  = "0.13.4"

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

  val ce3            = "org.typelevel"   %% "cats-effect-kernel" % Version.ce3
  val doobiePostgres = "org.tpolecat"    %% "doobie-postgres"    % Version.doobiePostgres
  val kafka          = "com.github.fd4s" %% "fs2-kafka"          % Version.fs2Kafka

  val newtype     = "io.estatico" %% "newtype"      % Version.newtype
  val refined     = "eu.timepit"  %% "refined"      % Version.refined
  val refinedCats = "eu.timepit"  %% "refined-cats" % Version.refined

  val mouse = "org.typelevel" %% "mouse" % Version.mouse

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
    "tf.tofu" %% "tofu-core-ce3"        % Version.tofu,
    "tf.tofu" %% "tofu-doobie-ce3"      % Version.tofu,
    "tf.tofu" %% "tofu-fs2-ce3-interop" % Version.tofu,
    "tf.tofu" %% "tofu-derivation"      % Version.tofu,
    "tf.tofu" %% "tofu-streams"         % Version.tofu,
    "tf.tofu" %% "tofu-logging"         % Version.tofu,
    "tf.tofu" %% "glass"                % Version.glass
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
    "org.tpolecat"               %% "doobie-scalatest"                % Version.doobieVersion              % Test,
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
