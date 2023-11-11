package api

import api.Model.{ Order, Orders}
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

trait JsonDecoders {
  implicit val orderDecoder: JsonDecoder[Order] = DeriveJsonDecoder.gen[Order]
  implicit val ordersDecoder: JsonDecoder[Orders] = DeriveJsonDecoder.gen[Orders]

  implicit val orderEncoder: JsonEncoder[Order] = DeriveJsonEncoder.gen[Order]
  implicit val ordersEncoder: JsonEncoder[Orders] = DeriveJsonEncoder.gen[Orders]
}
