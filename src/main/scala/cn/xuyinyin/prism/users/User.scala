package cn.xuyinyin.prism.users

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

case class User(name: String, age: Int)

object User {
  implicit val jsonEncoder: JsonEncoder[User] = DeriveJsonEncoder.gen[User]
  implicit val jsonDecoder: JsonDecoder[User] = DeriveJsonDecoder.gen[User]
}
