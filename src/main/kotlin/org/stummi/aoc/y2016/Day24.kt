package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.allPermutations
import org.stummi.aoc.helper.astar
import org.stummi.aoc.helper.sorted
import org.stummi.aoc.helper.tuplePermutations

object Day24 : AdventOfCode(2016, 24) {
    class Game(val walls: Set<XY>, val digits: Map<Int, XY>) {
        fun getWayLength(from: Int, to: Int): Int {
            val start = digits[from]!!
            val end = digits[to]!!
            return astar(
                start,
                { it.orthogonalNeighbours().filterNot { it in walls }.map { it to 1 } },
                { it == end },
                { it.orthogonalDistanceTo(end) }
            ).size
        }
    }

    override val part1: Int
        get() = solve()

    override val part2: Any
        get() = solve(true)


    private fun solve(part2: Boolean = false): Int {
        val map = readMap()
        val paths = map.digits.keys - 0
        val lengths = paths.tuplePermutations(false).map {
            it.sorted() to map.getWayLength(it.first, it.second)
        }.toMap() + paths.map { (0 to it) to map.getWayLength(0, it) }

        val routes = paths.allPermutations().map {
            if (part2)
                listOf(0) + it + listOf(0)
            else
                listOf(0) + it
        }

        return routes.minOf { route ->
            route.windowed(2).map { it[0] to it[1] }.map { lengths[it.sorted()]!! }.sum()
        }
    }

    private fun readMap(): Game {
        val walls = mutableListOf<XY>()
        val digits = mutableMapOf<Int, XY>()
        input().forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                when (char) {
                    '#' -> walls.add(XY(x, y))
                    '.' -> { /* ignore */
                    }

                    else -> digits[char.digitToInt()] = XY(x, y)
                }
            }
        }
        return Game(walls.toSet(), digits.toMap())
    }
}

fun main() {
    Day24.fancyRun()
}
