
# About

Scala interface to CueTracker.net - Snooker Results and Statistics Database.

# What have been done

* Head-to-Head statistics
** Mathes and frames head-to-head
** Career comparision
** TODO: detailed information for each match
* Tournament statistics
** Overall results (winner/loser/score)
** Detailed match progress
** Referee name
** TODO: advanced match statistics (series/points/etc)
* TODO: Player season statistics

# Examples

    val cue = new Cuetracker

    val t = cue.tournament("Masters", 2015)
    for (m <- t.matches) println(m.round + " " + m.winner)

    val h2h = cue.headToHead("Neil Robertson", "Judd Trump")
    println(h2h.matchesWon("Neil Robertson"))
    println(h2h.career("Judd Trump").centuriesMade)
