import sbt._

object Dependencies {

  object Version {
    val ce3        = "3.4.2"
    val tofu       = "0.11.0"
    val derevo     = "0.13.0"
    val newtype    = "0.4.4"
    val refined    = "0.10.1"
    val enumeratum = "1.7.0"
    val circe      = "0.14.1"
    val mouse       = "1.2.1"

    val sigma = "7.7.7"

    val betterMonadicFor = "0.3.1"
    val kindProjector    = "0.13.2"
  }

  val ce3 = "org.typelevel" %% "cats-effect-kernel" % Version.ce3

  val newtype     = "io.estatico" %% "newtype"      % Version.newtype
  val refined     = "eu.timepit"  %% "refined"      % Version.refined
  val refinedCats = "eu.timepit"  %% "refined-cats" % Version.refined

  val mouse = "org.typelevel" %% "mouse"               % Version.mouse

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
//    "tf.tofu" %% "tofu-zio-interop"     % Version.tofu,
    "tf.tofu" %% "tofu-derivation" % Version.tofu,
    "tf.tofu" %% "tofu-streams"    % Version.tofu,
    "tf.tofu" %% "tofu-logging"    % Version.tofu
  )

  val derevo = List(
    "tf.tofu" %% "derevo-core"         % Version.derevo,
    "tf.tofu" %% "derevo-cats"         % Version.derevo,
    "tf.tofu" %% "derevo-cats-tagless" % Version.derevo,
    "tf.tofu" %% "derevo-circe"        % Version.derevo,
    "tf.tofu" %% "derevo-pureconfig"   % Version.derevo
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
