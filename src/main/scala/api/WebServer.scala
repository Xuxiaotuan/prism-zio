package api


import service.OrderServiceImp
import zhttp.service.Server
import zio._

object WebServer extends ZIOAppDefault with OrderRoutes {

  println(s"Server online at http://localhost:3300/")
  def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    Server
      .start(
        port = 3300,
        http = httpApp
      )
      .provide(
        OrderServiceImp.live
      )

}