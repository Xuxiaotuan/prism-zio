package cn.xuyinyin.prism

import cn.xuyinyin.prism.counter.CounterApp
import cn.xuyinyin.prism.download.DownloadApp
import cn.xuyinyin.prism.greet.GreetingApp
import cn.xuyinyin.prism.users.{InmemoryUserRepo, UserApp}
import zio._
import zio.http._

object MainApp extends ZIOAppDefault {
  def run: ZIO[Environment with ZIOAppArgs with Scope, Throwable, Any] = {
    val httpApps = GreetingApp() ++ DownloadApp() ++ CounterApp() ++ UserApp()
    Server
      .serve(
        httpApps.withDefaultErrorResponse
      )
      .provide(
        Server.defaultWithPort(3300),

        // An layer responsible for storing the state of the `counterApp`
        ZLayer.fromZIO(Ref.make(0)),

        // To use the persistence layer, provide the `PersistentUserRepo.layer` layer instead
        InmemoryUserRepo.layer
      )
  }

}
