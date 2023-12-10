package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.allPermutations

object Day9 : AdventOfCode(2015, 9) {
    override val part1: Any
        get() = allDistances().minOrNull()!!

    override val part2: Any
        get() = allDistances().maxOrNull()!!


    private fun allDistances(): Sequence<Int> {
        val distances = inputLines().map {
            it.split(" ")
        }.associate {
            val f = it[0]
            val t = it[2]
            val d = it[4].toInt()
            (f to t) to d
        }

        val locations = distances.keys.flatMap { listOf(it.first, it.second) }.distinct()

        return locations.allPermutations().map {
            calculateDistance(it, distances)
        }
    }

    fun calculateDistance(path: List<String>, distances: Map<Pair<String, String>, Int>) =
        (1 until path.size).sumOf {
            val l1 = path[it - 1]
            val l2 = path[it]
            (distances[l1 to l2] ?: distances[l2 to l1])!!
        }
}

fun main() {
    Day9.fancyRun()
}
