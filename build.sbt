val scalaVersionUsed = "2.13.16"
val zioVersion       = "2.1.19"
val zioHttpVersion   = "3.3.3"

/**
 * ***********************************
 * 仓库配置
 * **********************************
 */
ThisBuild / resolvers ++= Seq(
  Resolver.mavenCentral,
  "Aliyun Maven" at "https://maven.aliyun.com/nexus/content/groups/public/"
)

/**
 * ***********************************
 * 公共设置
 * **********************************
 */
lazy val commonSettings = Seq(
  organization := "xuyinyin",
  name         := "prism-zio",
  version      := "0.1.0-SNAPSHOT",
  scalaVersion := scalaVersionUsed,
  libraryDependencies ++= Seq(
    // 日志
    "ch.qos.logback"              % "logback-classic" % "1.5.18",
    "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.5",
    "org.slf4j"                   % "slf4j-api"       % "2.0.17",

    // 测试
    "org.scalatest" %% "scalatest" % "3.2.19" % Test
  )
)

/**
 * ***********************************
 * 项目定义
 * **********************************
 */
lazy val root = (project in file("."))
  .enablePlugins(sbtassembly.AssemblyPlugin)
  .settings(commonSettings)

/**
 * ***********************************
 * Assembly配置
 * **********************************
 */
ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", _ @_*) => MergeStrategy.discard
  case _                           => MergeStrategy.first
}

assembly / mainClass := Some("cn.xuyinyin.prism.kubectl.KubectlPluginMain")
assembly / assemblyJarName := "kubectl-prism.jar"

/**
 * ***********************************
 * 依赖配置
 * **********************************
 */
libraryDependencies ++= Seq(
  // ZIO 核心
  "dev.zio"                       %% "zio"                           % zioVersion,
  "dev.zio"                       %% "zio-streams"                   % zioVersion,
  "dev.zio"                       %% "zio-json"                      % "0.7.44",
  "dev.zio"                       %% "zio-http"                      % zioHttpVersion,
  "com.coralogix"                 %% "zio-k8s-client"                % "3.1.0",
  "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % "3.11.0",
  "com.softwaremill.sttp.client3" %% "slf4j-backend"                 % "3.11.0",
  // CLI 参数解析
  "com.github.scopt"              %% "scopt"                         % "4.1.0",
  // ZIO 测试
  "dev.zio" %% "zio-test"     % zioVersion % Test,
  "dev.zio" %% "zio-test-sbt" % zioVersion % Test,
  // Quill
  "io.getquill" %% "quill-zio"      % "4.8.5",
  "io.getquill" %% "quill-jdbc-zio" % "4.8.5",

  // H2 数据库
  "com.h2database" % "h2" % "2.3.232"
)
