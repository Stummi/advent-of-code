package org.stummi.aoc.y2025

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.map
import kotlin.math.absoluteValue

object Day2 : AdventOfCode(2025, 2) {
    init {
        resourceSample(result1 = 13)
    }
    override val part1: Any
        get() {
            return parsedInput().flatten().filterNot {
                val str = "$it"
                if (str.length % 2 != 0) {
                    return@filterNot true
                }
                val (l, r) = str.chunked(str.length / 2)
                l != r
            }.sum()
        }

    override val part2: Any
        get() {
            return parsedInput().flatten().filterNot {
                val str = "$it"
                (1..str.length / 2).filter { str.length % it == 0 }.none {
                    str.chunked(it).distinct().size == 1
                }
            }.onEach { println(it) }.sum()
        }

    private fun parsedInput() =
        input.line.split(",").map {
            val (f, t) = it.split("-").map { it.toLong() }
            f..t
        }

}

fun main() {
    Day2.fancyRun()
}