
import org.scalatest.FlatSpec
import java.io.File
import net.ruippeixotog.scalascraper.browser.Browser
import org.github.chessman.HeadToHead

class HeadToHeadSpec extends FlatSpec {

  val file = new File(getClass.getResource("head-to-head.html").toURI)
  val doc = new Browser().parseFile(file)

  val headToHead = HeadToHead("p1", "p2", doc)

  behavior of "Head to Head"

  it should "give number of matches that first player won" in {
    assert(headToHead.matchesWon("p1") == 13)
  }

  it should "give number of matches that second player won" in {
    assert(headToHead.matchesWon("p2") == 8)
  }

  it should "give number of frames that first player won" in {
    assert(headToHead.framesWon("p1") == 132)
  }

  it should "give number of frames that second player won" in {
    assert(headToHead.framesWon("p2") == 106)
  }

  for (p <- List("p1", "p2")) {

    def v[T](p1Val: T, p2Val: T): T = if (p == "p1") p1Val else p2Val

    behavior of v("Player 1 career", "Player 2 career")

    val career = headToHead.career(v("p1", "p2"))

    it should "return number of seasons as professional" in {
      assert(career.seasonsAsProfessional == v(24, 17))
    }

    it should "return matches played" in {
      assert(career.matchesPlayed == v(1069, 840))
    }

    it should "return matches won" in {
      assert(career.matchesWon == v(790, 564))
    }

    it should "return matches lost" in {
      assert(career.matchesLost == v(246, 266))
    }

    it should "return matches drawn" in {
      assert(career.matchesDrawn == v(33, 10))
    }

    it should "return frames played" in {
      assert(career.framesPlayed == v(9442, 6100))
    }

    it should "return frames won" in {
      assert(career.framesWon == v(5746, 3528))
    }

    it should "return tournaments played" in {
      assert(career.tournamentsPlayed == v(272, 226))
    }

    it should "return tournaments won" in {
      assert(career.tournamentsWon == v(57, 16))
    }

    it should "return centuries made" in {
      assert(career.centuriesMade == v(789, 386))
    }

    it should "return centuries rate" in {
      assert(career.centuriesRate == v(12f, 15.93f))
    }

    it should "return maximums made" in {
      assert(career.maximumsMade == v(13, 2))
    }

    it should "return deciders played" in {
      assert(career.decidersPlayed == v(220, 224))
    }

    it should "return deciders won" in {
      assert(career.decidersWon == v(142, 139))
    }

    it should "return whitewashes played" in {
      assert(career.whitewashesPlayed == v(165, 169))
    }

    it should "return whitewashes won" in {
      assert(career.whitewashesWon == v(147, 130))
    }

    it should "return prize money" in {
      assert(career.prizeMoney == v(8166784, 3075666))
    }
  }
}
