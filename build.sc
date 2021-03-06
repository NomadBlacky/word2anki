import mill._
import mill.eval.Evaluator
import mill.scalalib._
import mill.scalalib.scalafmt.ScalafmtModule

object core extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.1"
  override def ivyDeps = Agg(
    ivy"net.ruippeixotog::scala-scraper:2.2.0",
    ivy"com.softwaremill.sttp.client::core:2.1.1",
    ivy"com.softwaremill.sttp.client::circe:2.1.1"
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

  object test extends Tests {
    override def ivyDeps = Agg(ivy"org.scalameta::munit:0.7.7")
    def testFrameworks = Seq("munit.Framework")
  }
}

def idea(ev: Evaluator) = scalalib.GenIdea.idea(ev)
