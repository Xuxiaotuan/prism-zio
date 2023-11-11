package api


object Model {
  case class Order(item: String, price: Double)
  case class Orders(orders: List[Order])

  case class DecodeError(str: String) extends Error

  final case class InternalServerError(msg: String) extends Error

  final case class NotFoundError(msg: String) extends Error

}
