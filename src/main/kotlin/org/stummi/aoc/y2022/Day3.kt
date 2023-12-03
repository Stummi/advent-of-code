package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode

object Day3 : AdventOfCode(2022, 3) {
    override val part1: Any
        get() = input().sumOf {
            val spl = it.chunked(it.length / 2)
            val common = spl[0].filter { it in spl[1] }[0]
            prio(common)
        }

    private fun prio(common: Char) = common.lowercase()[0] - 'a' + if (common.isUpperCase()) 27 else 1

    override val part2
        get() = input().chunked(3).sumOf {
            prio(it[0].filter { c -> c in it[1] && c in it[2] }[0])
        }

}

fun main() {
    Day3.fancyRun()
}
