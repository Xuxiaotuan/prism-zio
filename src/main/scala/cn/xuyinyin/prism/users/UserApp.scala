package cn.xuyinyin.prism.users

import zio._
import zio.http._
import zio.json._

/**
 * An http app that:
 *   - Accepts a `Request` and returns a `Response`
 *   - May fail with type of `Throwable`
 *   - Uses a `UserRepo` as the environment
 */
object UserApp {

  def apply(): Http[UserRepo, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      // POST /users -d '{"name": "John", "age": 35}'
      case req @ (Method.POST -> Root / "users") =>
        (for {
          u <- req.body.asString.map(_.fromJson[User])
          r <- u match {
            case Left(e) =>
              ZIO
                .debug(s"Failed to parse the input: $e")
                .as(
                  Response.text(e).withStatus(Status.BadRequest)
                )
            case Right(u) =>
              UserRepo
                .register(u)
                .map(id => Response.text(id))
          }
        } yield r).orDie

      // GET /users/:id
      case Method.GET -> Root / "users" / id =>
        UserRepo
          .lookup(id)
          .map {
            case Some(user) =>
              Response.json(user.toJson)
            case None =>
              Response.status(Status.NotFound)
          }
          .orDie
      // GET /users
      case Method.GET -> Root / "users" =>
        UserRepo.users.map(response => Response.json(response.toJson)).orDie
    }
}
