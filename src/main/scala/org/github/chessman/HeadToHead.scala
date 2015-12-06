package org.github.chessman
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import org.jsoup.nodes.Element

object CareerHeadToHead {

  def apply(playerIndex: Int, doc: Element) = {

    def parseData(doc: Element): List[List[String]] =
      (doc >> elements(".col-md-9 tbody") >>
       //workaround for https://github.com/ruippeixotog/scala-scraper/issues/16
       //should be:
       //elementList("tr")).map(_ >> elementList("td") >> text("td"))
       elementList("tr")).map(el => (el select "td").toList.map(_ >> text("td")))

    val data = parseData(doc)

    def parseFirstNumber(value: String): String =
      ("""[\d,.]+""".r findFirstIn value).getOrElse("").replaceAll(",", "")

    def parseTable(row: Int, col: Int, colsTotal: Int): String = {
      val colOffset = (if (colsTotal == 3) 2 else 3) * playerIndex
      parseFirstNumber(data(row)(col + colOffset))
    }

    new CareerHeadToHead(
      seasonsAsProfessional = parseTable(0, 1, 3).toInt,
      matchesPlayed = parseTable(1, 0, 5).toInt,
      matchesWon =  parseTable(1, 1, 5).toInt,
      matchesLost = parseTable(2, 0, 5).toInt,
      matchesDrawn = parseTable(2, 1, 5).toInt,
      framesPlayed = parseTable(3, 0, 5).toInt,
      framesWon = parseTable(3, 1, 5).toInt,
      tournamentsPlayed = parseTable(4, 0, 5).toInt,
      tournamentsWon = parseTable(4, 1, 5).toInt,
      centuriesMade = parseTable(5, 0, 5).toInt,
      centuriesRate = parseTable(5, 1, 5).toFloat,
      maximumsMade = parseTable(6, 1, 3).toInt,
      decidersPlayed = parseTable(7, 0, 5).toInt,
      decidersWon = parseTable(7, 1, 5).toInt,
      whitewashesPlayed = parseTable(8, 0, 5).toInt,
      whitewashesWon = parseTable(8, 1, 5).toInt,
      prizeMoney = parseTable(9, 1, 3).toInt
    )
  }
}

class CareerHeadToHead(val seasonsAsProfessional: Int,
                       val matchesPlayed: Int,
                       val matchesWon: Int,
                       val matchesLost: Int,
                       val matchesDrawn: Int,
                       val framesPlayed: Int,
                       val framesWon: Int,
                       val tournamentsPlayed: Int,
                       val tournamentsWon: Int,
                       val centuriesMade: Int,
                       val centuriesRate: Float,
                       val maximumsMade: Int,
                       val decidersPlayed: Int,
                       val decidersWon: Int,
                       val whitewashesPlayed: Int,
                       val whitewashesWon: Int,
                       val prizeMoney: Int)

object PlayerHeadToHead {

  def apply(player: String, playerIndex: Int, doc: Element) = {

    def parseSummary(section: Int): Int = {
      val str = (doc >> elementList(".col-md-4"))(section) >> text("small")
      (""" (\d+)""".r findAllIn str).toList(playerIndex).replaceAll(" ", "").toInt
    }

    new PlayerHeadToHead(
      player = player,
      matchesWon = parseSummary(0),
      framesWon = parseSummary(1),
      career = CareerHeadToHead(playerIndex, doc)
    )
  }
}

class PlayerHeadToHead(val player: String,
                       val matchesWon: Int,
                       val framesWon: Int,
                       val career: CareerHeadToHead)

object HeadToHead {

  def apply(player1: String, player2: String, doc: Element) = {

    new HeadToHead(Vector(
      PlayerHeadToHead(player1, 0, doc),
      PlayerHeadToHead(player2, 1, doc)
    ))

  }
}

class HeadToHead(playersHeadToHead: Seq[PlayerHeadToHead]) {

  def playerHeadToHead(player: String): PlayerHeadToHead = {
    val playerIndex = if (playersHeadToHead(0).player == player) 0 else 1
    playersHeadToHead(playerIndex)
  }

  def matchesWon(player: String): Int = playerHeadToHead(player).matchesWon
  def framesWon(player: String): Int = playerHeadToHead(player).framesWon
  def career(player: String): CareerHeadToHead = playerHeadToHead(player).career
}
