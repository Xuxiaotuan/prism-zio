package service

import api.Model.{Order, Orders}
import zio._

import java.io.IOException
import scala.collection.mutable

trait OrderService {
  def getOrders(): Task[Orders]
  def submitOrder(order: Order): Task[String]
}

//For a better functional design, it's better to write accessor methods for all of our service methods using ZIO.serviceWithZIO constructor inside the companion object
object OrderService {
  def getOrders(): ZIO[OrderService, Throwable, Orders] = ZIO.serviceWithZIO[OrderService](_.getOrders())

  def submitOrder(order: Order): ZIO[OrderService, Throwable, String] = ZIO.serviceWithZIO[OrderService](_.submitOrder(order))
}


////////////////////////////// OrderService in Memory or Live Implementation

object OrderServiceImp {

  val live: ZLayer[Any, Nothing, OrderService] = ZLayer.fromZIO(
    Ref.make(mutable.Map.empty[String, Order]).map(new OrderServiceImp(_))
  )
}
case class OrderServiceImp(orderMap: Ref[mutable.Map[String, Order]]) extends OrderService {
  override def getOrders(): UIO[Orders] = orderMap.get.map(_.values.toList).map(l => Orders(l))

  override def submitOrder(order: Order): UIO[String] =
    for {
        id <- Random.nextUUID.map(_.toString)
        _ <- orderMap.updateAndGet(_ addOne(id, order))
        _ <- ZIO.log(s"OrderID ${id} processed.")
      } yield id

}
