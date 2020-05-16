package word2anki

import java.net.URLEncoder

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

class WeblioClient {
  private[this] val browser = JsoupBrowser()

  def en2ja(word: String): JapaneseContent = {
    val encoded = URLEncoder.encode(word, "utf-8")
    val doc = browser.get(s"https://ejje.weblio.jp/content/$encoded")
    val explanation = doc >> text(
      "#summary > div.summaryM.descriptionWrp > table > tbody > tr > td.content-explanation.ej"
    )
    JapaneseContent(explanation)
  }
}

case class JapaneseContent(explanation: String)
