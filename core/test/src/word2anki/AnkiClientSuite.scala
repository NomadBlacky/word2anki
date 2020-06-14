package word2anki

import sttp.client.monad.TryMonad
import sttp.client.testing.SttpBackendStub

import scala.util.{Failure, Success}

class AnkiClientSuite extends munit.FunSuite {
  test("sync() should return Success when Anki Connect returns no error") {
    implicit val backend = SttpBackendStub(TryMonad).whenAnyRequest.thenRespond("""{"result":null,"error":null}""")

    val client = new AnkiClient()

    assertEquals(client.sync(), Success(()))
  }

  test("sync() should return Failure when Anki Connect returns an error") {
    implicit val backend =
      SttpBackendStub(TryMonad).whenAnyRequest.thenRespond("""{"result":null,"error":"errormsg"}""")

    val client = new AnkiClient()

    intercept[AnkiConnectException](client.sync().get)
  }
}
