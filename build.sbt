name := "prism-zio"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "prism-zio"
  )

val akkaHttpVersion= "10.1.8"
val zHttpVersion = "2.0.0-RC10"
val zioVersion = "2.0.0"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.0" % Test,

  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.slf4j" % "slf4j-api" % "1.7.30",
  "ch.qos.logback" % "logback-classic" % "1.3.0-alpha5",

  "dev.zio" %% "zio" % "2.0.13",
  "dev.zio" %% "zio-json" % "0.5.0",
  "io.d11" %% "zhttp" % "2.0.0-RC10",
)