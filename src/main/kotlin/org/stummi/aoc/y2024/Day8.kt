package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.tuplePermutations

object Day8 : AdventOfCode(2024, 8) {

    private val map by lazy { input.matrix }
    private val freqs by lazy { map.allValues.filterNot { it == '.' }.distinct().toList() }

    private fun countFreqs(generator: (Pair<XY, XY>) -> Sequence<XY>): Int {
        return freqs.flatMap { f ->
            map.findAll(f).toList().tuplePermutations().flatMap { xy ->
                generator(xy).takeWhile { it in map.bounds }
            }
        }.distinct()
            .count()
    }

    override val part1: Any
        get() = countFreqs { (a, b) -> sequenceOf(b + (b - a)) }

    override val part2: Any
        get() = countFreqs { (a, b) ->
            val d = (b - a)
            generateSequence(b) { it + d }
        }

}

fun main() {
    Day8.fancyRun()
}