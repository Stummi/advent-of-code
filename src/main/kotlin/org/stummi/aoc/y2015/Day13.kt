package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.allPermutations

object Day13 : AdventOfCode(2015, 13) {
    override val part1: Any
        get() = parseInput().let { map ->
            map.keys.map { it.first }.distinct().allPermutations().map {
                calulcateHappiness(it, map)
            }.max()
        }

    override val part2: Any
        get() = parseInput().let { map ->
            map.keys.map { it.first }.distinct().allPermutations().map {
                calulcateHappiness(it + "myself", map)
            }.max()
        }

    private fun parseInput() = inputLines().map {
        it.split(" ")
    }.associate {
        val p1 = it[0]
        val p2 = it[10].trimEnd('.')
        val points = when (it[2]) {
            "gain" -> it[3].toInt()
            "lose" -> -it[3].toInt()
            else -> throw IllegalArgumentException()
        }

        (p1 to p2) to points
    }

    private fun calulcateHappiness(seating: List<String>, map: Map<Pair<String, String>, Int>): Int {
        return (0 until seating.size).map {
            val p = seating[it]
            listOf(
                seating[(it + 1) % seating.size],
                seating[(it + seating.size - 1) % seating.size]
            ).sumOf {
                map[p to it] ?: 0
            }
        }.sum()

    }
}

fun main() {
    Day13.fancyRun()
}
