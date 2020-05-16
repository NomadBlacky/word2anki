package word2anki

import java.net.URLEncoder

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Document

import scala.util.Try

class WeblioClient {
  type ExplanationDoc = Document

  private[this] val browser = JsoupBrowser()

  def en2ja(word: String): Either[WeblioError, JapaneseContent] = {
    val encoded = URLEncoder.encode(word, "utf-8")
    for {
      doc <- getDocFromUrl(s"https://ejje.weblio.jp/content/$encoded")
      exp <- extractExplanation(doc)
    } yield JapaneseContent(exp)
  }

  private def getDocFromUrl(url: String): Either[UnexpectedError, ExplanationDoc] =
    Try(browser.get(url)).toEither.left.map(UnexpectedError.apply)

  private def extractExplanation(doc: ExplanationDoc): Either[ExplanationNotFoundError.type, String] = {
    val expOpt = doc >?> text(
        "#summary > div.summaryM.descriptionWrp > table > tbody > tr > td.content-explanation.ej"
      )
    expOpt match {
      case Some(exp) => Right(exp)
      case None      => Left(ExplanationNotFoundError)
    }
  }

  sealed trait WeblioError
  case object ExplanationNotFoundError             extends WeblioError
  case class UnexpectedError(throwable: Throwable) extends WeblioError
}

case class JapaneseContent(explanation: String)
