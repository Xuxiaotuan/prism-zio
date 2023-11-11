package api

import api.Model._
import service.OrderService
import zhttp.http._
import zio._
import zio.json._

trait OrderRoutes extends JsonDecoders {
  /** An http app which
   *   - Accepts a `Request`` and produces `Response`` effectually
   *   - May fail with type of `Throwable`
   *   - Uses a `OrderService` as the environment
   */
  val httpApp: Http[OrderService, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> !! / "orders" =>
      OrderService.getOrders.map(response => Response.json(response.toJson))


    case req @ Method.POST -> !! / "order" => for {
        order <- req.bodyAsString.map(_.fromJson[Order])
        response <- order match {
                    case Left(error) =>
                      ZIO
                        .debug(s"Failed to parse the input: $error")
                        .as(Response.text(error).setStatus(Status.BadRequest))
                    case Right(order) =>
                      OrderService.submitOrder(order)
                        .map(id => Response.text(s"order ${id} is created" ).setStatus(Status.Created))
                  }

      } yield response

  }

}