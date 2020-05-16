import mill._
import mill.eval.Evaluator
import mill.scalalib._
import mill.scalalib.scalafmt.ScalafmtModule

object core extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.1"
  override def ivyDeps = Agg(
    ivy"net.ruippeixotog::scala-scraper:2.2.0"
  )
  def nativeImage = T {
    assembly()
    os.proc(
      s"${sys.env("GRAALVM_HOME")}/bin/native-image",
      "-jar",
      os.pwd / "out" / "core" / "assembly" / "dest" / "out.jar",
      s"-H:Name=${T.dest / "word2anki"}",
      "--initialize-at-build-time",
      "--no-server",
      "--enable-http",
      "--enable-https",
      "-H:EnableURLProtocols=http,https",
      "--no-fallback",
      "-H:+ReportExceptionStackTraces",
      "--allow-incomplete-classpath",
      "--initialize-at-build-time=scala.runtime.Statics$VM"
    ).call(stdout = os.Inherit, stderr = os.Inherit)
  }
}

def idea(ev: Evaluator) = scalalib.GenIdea.idea(ev)
