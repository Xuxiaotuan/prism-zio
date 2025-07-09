package cn.xuyinyin.prism

import zio.Console.{printLine, readLine}
import zio._

/**
 * 项目启动入口
 */
object MainApp extends ZIOAppDefault {

  import zio.http._
  val routes: Routes[Any, Nothing] = Routes(
    Method.GET / "hello" / string("name") -> handler { (name: String, req: Request) =>
      Response.text(s"Hello $name")
    }
  )

  def run: ZIO[Any with ZIOAppArgs with Scope, Any, Unit] = {
    for {
      _    <- printLine("Hello! What is your name?")
      name <- readLine
      _    <- printLine(s"Hello, $name, welcome to ZIO!")
    } yield ()
  }

}
