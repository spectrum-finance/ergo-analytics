import sbt._

object Dependencies {

  object Version {
    val ce3          = "3.4.2"
    val tofu         = "0.11.0"
    val derevo       = "0.13.0"

    val sigma = "7.7.7"
  }

  val ce3 = "org.typelevel" %% "cats-effect-kernel" % Version.ce3

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
}
