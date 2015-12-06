package org.github.chessman

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import org.jsoup.nodes.Element

object Scores {
  def apply(scoresDoc: Element) = {
    def parseDetailed(scoresDoc: Element): List[Tuple2[Int, Int]] = {
      val scoresText = (scoresDoc >> elementList("td"))(1).text
                               .replaceAll("""\(\d+\)""", "")

      ("""(\d+)-(\d+)""".r findAllMatchIn scoresText)
                        .map(m => (m.group(1).toInt, m.group(2).toInt)).toList

    }

    new Scores(parseDetailed(scoresDoc))
  }
}

class Scores(val detailed: List[Tuple2[Int, Int]]) {

  val total = (detailed.count(s => s._1 > s._2),
               detailed.count(s => s._1 < s._2))
}

object Match {

  def apply(mainDoc: Element, scoresDoc: => Element, referee: Option[String]) = {

    val Walkover = """(.+) \(Walkover\)""".r

    val (player1, walkover) = mainDoc >> text(".match_player1") match {
      case Walkover(p) => (p, true)
      case p => (p, false)
    }

    new Match(
      round = mainDoc >> text(".match_round"),
      player1 = player1,
      player2 = mainDoc >> text(".match_player2"),
      walkover = walkover,
      referee = referee,
      scores = if (walkover) new Scores(List((0, 0)))
               else          Scores(scoresDoc)
    )
  }
}

class Match(
  val round: String,
  val player1: String,
  val player2: String,
  val walkover: Boolean,
  val referee: Option[String],
  val scores: Scores) {

  val winner = player1
  val loser = player2
}
