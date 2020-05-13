import mill._
import mill.eval.Evaluator
import mill.scalalib._
import mill.scalalib.scalafmt.ScalafmtModule

object word2anki extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.1"
}

def idea(ev: Evaluator) = scalalib.GenIdea.idea(ev)
