package cn.xuyinyin.prism.kubectl

import com.coralogix.zio.k8s.client.model.K8sNamespace
import scopt.OParser
import zio._

import java.io.IOException

case class Config(command: String = "", namespace: Option[String] = None, verbose: Boolean = false)

object KubectlPluginMain extends ZIOAppDefault {
  private val builder = OParser.builder[Config]
  private val parser = {
    import builder._
    OParser.sequence(
      programName("kubectl-prism"),
      head("kubectl-prism", "0.1.0"),
      opt[String]('n', "namespace")
        .optional()
        .action((x, c) => c.copy(namespace = Some(x)))
        .text("Kubernetes namespace"),
      opt[Unit]('v', "verbose")
        .action((_, c) => c.copy(verbose = true))
        .text("Enable verbose logging"),
      cmd("pods")
        .action((_, c) => c.copy(command = "pods"))
        .text("List pods")
        .children(
          note("List all pods in the specified namespace")
        ),
      cmd("version")
        .action((_, c) => c.copy(command = "version"))
        .text("Show version information"),
      help("help").text("Show this help message")
    )
  }
  private def listPods(namespace:
                       Option[String]): ZIO[Any, IOException, Unit] = {
    val ns = namespace.map(K8sNamespace(_)).getOrElse(K8sNamespace.default)
    
    for {
      _         <- Console.printLine(s"Pods in namespace '${ns.value}':")
      _         <- Console.printLine("NAME\t\tSTATUS\t\tAGE")
      _         <- Console.printLine("mock-pod-1\t\tRunning\t\t1d")
      _         <- Console.printLine("mock-pod-2\t\tPending\t\t5m")
    } yield ()
  }
  private def showVersion: ZIO[Any, IOException, Unit] = Console.printLine("kubectl-prism version 0.1.0")

  def run: ZIO[ZIOAppArgs, Any, Unit] = {
    for {
      args   <- getArgs
      config <- ZIO.fromOption(OParser.parse(parser, args.toList, Config()))
                  .orElseFail("Invalid arguments")
      _      <- if (config.verbose) Console.printLine("Verbose mode enabled") else ZIO.unit
      _      <- config.command match {
                  case "pods" =>
                    listPods(config.namespace)
                  case "version" =>
                    showVersion
                  case _ =>
                    Console.printLine("Unknown command") *>
                    Console.printLine(OParser.usage(parser))
                }
    } yield ()
  }.catchAll { error =>
    Console.printLine(s"Error: $error") *>
    Console.printLine(OParser.usage(parser))
  }
}