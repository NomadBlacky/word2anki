package word2anki

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}
import sttp.client._
import sttp.client.circe._

import scala.util.{Failure, Success, Try}

class AnkiClient(ankiConnectUrl: String = "http://localhost:8765", ankiConnectVersion: Int = 6)(
    implicit backend: SttpBackend[Try, Nothing, NothingT]
) {

  def sync(): Try[Unit] = {
    val payload = SyncPayload(ankiConnectVersion)
    val responseT = basicRequest
      .post(uri"$ankiConnectUrl")
      .body(payload)
      .response(asJsonAlways[NoResultResponse])
      .send()
    for {
      sttpResponse <- responseT
      response     <- sttpResponse.body.left.map(e => AnkiConnectException(e.body)).toTry
      unit         <- response.toTry
    } yield unit
  }
}

case class SyncPayload(version: Int)
object SyncPayload {
  implicit val encoder: Encoder[SyncPayload] = (payload: SyncPayload) =>
    Json.obj(
      "action"  -> Json.fromString("sync"),
      "version" -> Json.fromInt(payload.version)
    )
}

trait AnkiConnectResponse[T] {
  val result: T
  val error: Option[String]

  def toTry: Try[T] = {
    error match {
      case Some(error) => Failure(AnkiConnectException(error))
      case None        => Success(result)
    }
  }
}

case class AnkiConnectException(message: String) extends Exception(message)

case class NoResultResponse(private val _error: Option[String]) extends AnkiConnectResponse[Unit] {
  override val result: Unit          = ()
  override val error: Option[String] = _error
}
object NoResultResponse {
  implicit val decoder: Decoder[NoResultResponse] = new Decoder[NoResultResponse] {
    override def apply(c: HCursor): Result[NoResultResponse] =
      c.downField("error").as[Option[String]].map(NoResultResponse.apply)
  }
}
