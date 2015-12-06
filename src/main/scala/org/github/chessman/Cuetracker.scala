package org.github.chessman
import net.ruippeixotog.scalascraper.browser.Browser
import org.jsoup.nodes.Document

class Cuetracker {

  def getDocument(url: String): Document = {
      val browser = new Browser
      browser.get(url)
  }

  private def playerUrl(player: String): String =
    player.replaceAll("""[^\w ]""", "").replaceAll(" ", "-")

  def headToHead(player1: String, player2: String): HeadToHead = {

      val url = Url.headToHead(player1, player2)
      val doc = getDocument(url)

      HeadToHead(player1, player2, doc)
  }

  def tournament(name: String, year: Int): Tournament = {

      val url = Url.tournament(name, year)
      val doc = getDocument(url)

      Tournament(doc)
  }
}
