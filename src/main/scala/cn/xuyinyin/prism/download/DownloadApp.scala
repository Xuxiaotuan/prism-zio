package cn.xuyinyin.prism.download

import zio._
import zio.http._
import zio.stream.ZStream

/** An http app that:
 *   - Accepts a `Request` and returns a `Response`
 *   - May fail with type of `Throwable`
 *   - Does not require any environment
 */
object DownloadApp{
  def apply(): Http[Any, Throwable, Request, Response] =
    Http.collect[Request] {
      // GET /download
      case Method.GET -> Root / "download" =>
        val fileName = "file.txt"
        http.Response(
          status = Status.Ok,
          headers = Headers(
            Header.ContentType(MediaType.application.`octet-stream`),
            Header.ContentDisposition.attachment(fileName)
          ),
          body = Body.fromStream(ZStream.fromResource(fileName))
        )

      // Download a large file using streams
      // GET /download/stream
      case Method.GET -> Root / "download" / "stream" =>
        val file = "bigfile.txt"
        http.Response(
          status = Status.Ok,
          headers = Headers(
            Header.ContentType(MediaType.application.`octet-stream`),
            Header.ContentDisposition.attachment(file)
          ),
          body = Body.fromStream(
            ZStream
              .fromResource(file)
              .schedule(Schedule.spaced(50.millis))
          )
        )
    }

}
