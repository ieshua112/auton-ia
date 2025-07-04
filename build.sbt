name := "auton-ia"

version := "0.1"

scalaVersion := "2.13.16"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.1.19",
  "dev.zio" %% "zio-interop-cats" % "23.1.0.5",
  "org.http4s" %% "http4s-ember-server" % "0.23.30",
  "org.http4s" %% "http4s-dsl" % "0.23.30"
)