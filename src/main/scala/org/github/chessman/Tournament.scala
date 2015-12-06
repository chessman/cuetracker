package org.github.chessman
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import org.jsoup.nodes.Element

object Tournament {

  def apply(doc: Element) = {

    //group elements by predicate:
    //
    //List(first-element-which-satisfied-pred),  List(rest-elements),
    //List(second-element-which-satisfied-pred), List(rest-elements)
    //...
    //
    //example:
    //
    //alternationSplit(List(1, 2, 3, 1, 4, 5, 1, 1, 6, 7), (p: Int) => p == 1)
    //List(List(1), List(2, 3), List(1), List(4, 5), List(1), List(), List(1), List(6, 7))
    //
    def alternationSplit[T](col: Traversable[T], pred: T => Boolean): List[Traversable[T]] = {
      if (col.isEmpty) Nil
      else {
        if (!pred(col.head)) Nil
        else {
          val (a, b) = col.tail.span(x => !pred(x))
          List(col.head) :: a :: alternationSplit(b, pred)
        }
      }
    }

    //take table with results and drop header
    val matchesTable = (doc >> elementList(".table-small"))
                       .find(el => (el >?> text(".match_round")) != None)
                       .get >> elementList("tr") drop(1)

    //match layout has variable number of rows. we need to group data with
    //the following structure: even elements are first rows
    //of the table (have information about round and players), odd elements
    //contain the rest data of the match and start with the score information.
    val matchesDoc = alternationSplit(matchesTable,
                    (row: Element) => (row >?> text(".match_round")) != None)

    val matches = matchesDoc.sliding(2, 2)
              .map(
                matchDoc => {
                  val Referee = "Referee: (.*)".r
                  val referee = matchDoc(1).map(_ >?> text("div.col-md-6")).collectFirst {
                    case Some(Referee(c)) => c
                  }
                  Match(matchDoc(0).head, matchDoc(1).head, referee)
                }
                ).toList

    new Tournament(matches)
  }
}

class Tournament(val matches: List[Match])
