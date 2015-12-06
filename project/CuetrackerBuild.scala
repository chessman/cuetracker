import sbt._
import sbt.Keys._

object CuetrackerBuild extends Build {

  lazy val cuetracker = Project(
    id = "cuetracker",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "cuetracker",
      organization := "org.github.chessman",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.11.6",
      initialCommands in console += """
      import org.github.chessman._
      import net.ruippeixotog.scalascraper.browser.Browser
      import org.jsoup.nodes.Element
      import java.io.File
      val cue = new Cuetracker
      """,
      libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "0.1.2",
      libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
    )
  )
}
