package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.astar
import org.stummi.aoc.helper.md5String

object Day17 : AdventOfCode(2016, 17) {

    init {
        rawSample("ihgpwlah", "DDRRRD", 370)
        rawSample("kglvqrro", "DDUDRLRRUDRD", 492)
        rawSample("ulqzkmiv", "DRURDRUDDLLDLUURRDULRLDUUDDDRR", 830)
    }

    data class State(
        val input: String,
        val movements: String = "",
        val pos: XY = XY(0, 0)
    ) {
        fun moves(): Sequence<State> = sequence {
            val openCodes = "bcdef"
            val hash = md5String("$input$movements")

            if (hash[0] in openCodes) yield(copy(movements = "${movements}U", pos = pos.up))
            if (hash[1] in openCodes) yield(copy(movements = "${movements}D", pos = pos.down))
            if (hash[2] in openCodes) yield(copy(movements = "${movements}L", pos = pos.left))
            if (hash[3] in openCodes) yield(copy(movements = "${movements}R", pos = pos.right))
        }.filter {
            it.pos in (XY.ZERO..XY(3, 3))
        }
    }

    override val part1
        get() = astar(
            initialState = State(inputLines().first()),
            nextStates = { it.moves().map { it to 1 } },
            goal = { it.pos == XY(3, 3) },
            heuristicCost = { XY(3, 3).orthogonalDistanceTo(it.pos) }
        ).first().first.movements

    override val part2: Int
        get() {
            val states = mutableListOf(State(inputLines().first()))
            var longestPath = 0;
            while (states.isNotEmpty()) {
                val s = states.removeFirst()
                if (s.pos == XY(3, 3)) {
                    val len = s.movements.length
                    if (len > longestPath) longestPath = len
                    continue
                }
                states.addAll(s.moves())
            }
            return longestPath
        }
}

fun main() {
    Day17.fancyRun()
}
