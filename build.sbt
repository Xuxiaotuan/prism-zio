val scala = "2.13.10"

val zioHttpVersion = "3.0.0-RC2"
val zioVersion     = "2.0.19"

lazy val commonSettings = Seq(
  organization := "xuyinyin",
  name         := "prism-zio",
  version      := "0.1.0-SNAPSHOT",
  scalaVersion := scala,
  libraryDependencies ++= Seq(
    "ch.qos.logback"              % "logback-classic" % "1.4.11",
    "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.5",
    "org.scalatest"              %% "scalatest"       % "3.2.17" % Test,
    "org.slf4j"                   % "slf4j-api"       % "2.0.9",
  )
)

lazy val root = (project in file("."))
  .settings(commonSettings)

libraryDependencies ++= Seq(
  "dev.zio"       %% "zio"            % zioVersion,
  "dev.zio"       %% "zio-http"       % zioHttpVersion,
  "dev.zio"       %% "zio-json"       % "0.6.2",
  "io.getquill"   %% "quill-zio"      % "4.7.0",
  "io.getquill"   %% "quill-jdbc-zio" % "4.7.0",
  "com.h2database" % "h2"             % "2.2.224"
)
