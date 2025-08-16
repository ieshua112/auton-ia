name := "auton-ia"

version := "0.0.1"

scalaVersion := "2.13.16"

scalacOptions += "-Wnonunit-statement"

Compile / run / fork := true

resolvers += "Evolution" at "https://evolution.jfrog.io/artifactory/public/"

libraryDependencies ++= Seq(
  "org.typelevel"         %% "log4cats-slf4j"          % "2.7.1",
  "ch.qos.logback"         % "logback-classic"         % "1.5.18",
  "com.github.pureconfig" %% "pureconfig"              % "0.17.9",
  "com.h2database"         % "h2"                      % "2.3.232",
  "io.github.liquibase4s" %% "liquibase4s-cats-effect" % "1.0.0",
  "org.typelevel"         %% "cats-effect"             % "3.6.3",
  "org.http4s"            %% "http4s-ember-server"     % "0.23.30",
  "org.http4s"            %% "http4s-dsl"              % "0.23.30",
  "org.http4s"            %% "http4s-circe"            % "0.23.30",
  "io.circe"              %% "circe-core"              % "0.14.6",
  "io.circe"              %% "circe-generic"           % "0.14.6",
  "io.circe"              %% "circe-parser"            % "0.14.6",
  "io.circe"              %% "circe-generic-extras"    % "0.14.4",
  "org.tpolecat"          %% "doobie-core"             % "1.0.0-RC10",
  "org.tpolecat"          %% "doobie-hikari"           % "1.0.0-RC10",
  "org.tpolecat"          %% "doobie-postgres"         % "1.0.0-RC10", // понадобится позже
  "com.evolutiongaming"   %% "cats-helper"             % "3.12.0"
)

Compile / scalacOptions ++= List("-Ymacro-annotations", "-Ywarn-macros:after")

addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.3" cross CrossVersion.full)
