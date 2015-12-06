
import org.scalatest.FlatSpec
import java.io.File
import net.ruippeixotog.scalascraper.browser.Browser
import org.github.chessman.Tournament

class TournamentSpec extends FlatSpec {

  val file = new File(getClass.getResource("tournament.html").toURI)
  val doc = new Browser().parseFile(file)

  val tournament = Tournament(doc)

  val matches = tournament.matches

  behavior of "Matches"

  //This match has centuries and the name of referee
  val m = matches(13)

  it should "give round of the tournament" in {
    assert(m.round == "Last 16")
  }

  it should "give names of players" in {
    assert(m.player1 == "Kyren Wilson")
    assert(m.player2 == "Judd Trump")
  }

  it should "give names of winner and loser" in {
    assert(m.winner == m.player1)
    assert(m.loser == m.player2)
  }

  it should "give name of referee when it set" in {
    assert(m.referee == Some("Maike Kesseler"))
  }

  behavior of "Scores"

  val scores = m.scores

  it should "give detailed scores" in {
    assert(scores.detailed ==
      List((72, 9), (27, 67), (75, 18), (0, 104), (82, 1), (73, 1)))
  }

  it should "give total scores" in {
    assert(scores.total == (4, 2))
  }
}
