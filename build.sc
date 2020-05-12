import mill._
import mill.scalalib.scalafmt.ScalafmtModule
import scalalib._

object word2anki extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.1"
}
