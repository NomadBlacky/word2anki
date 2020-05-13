import mill._
import mill.eval.Evaluator
import mill.scalalib._
import mill.scalalib.scalafmt.ScalafmtModule

object word2anki extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.1"
  override def ivyDeps = Agg(
    ivy"net.ruippeixotog::scala-scraper:2.2.0"
  )
}

def idea(ev: Evaluator) = scalalib.GenIdea.idea(ev)
