package org.github.chessman

object Url {

  val topUrl = "http://cuetracker.net"

  private def text2Url(text: String): String =
    text.replaceAll("""[^\w ]""", "").replaceAll(" ", "-")

  def headToHead(player1: String, player2: String): String =
    topUrl + "/Head-to-Head/" + text2Url(player1) + "/" + text2Url(player2)

  def tournament(name: String, year: Int): String =
    topUrl + "/Tournaments/" + text2Url(name) + "/" + year
}
