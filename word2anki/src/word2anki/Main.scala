package word2anki

object Main {
  def main(args: Array[String]): Unit = {
    val weblio = new WeblioClient()
    println(weblio.en2ja(args(0)))
  }
}
