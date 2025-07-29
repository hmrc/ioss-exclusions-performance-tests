import sbt.*

object Dependencies {

  val test = Seq(
    "com.typesafe"          % "config"                    % "1.4.4"         % Test,
    "uk.gov.hmrc"          %% "performance-test-runner"   % "6.2.0"         % Test
  )

}
