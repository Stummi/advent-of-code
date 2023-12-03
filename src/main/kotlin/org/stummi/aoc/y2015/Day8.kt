package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode

object Day8 : AdventOfCode(2015, 8) {
    override val part1: Any
        get() = input().map {
            it.length - it
                .substring(1, it.length - 1)
                .replace(Regex("\\\\[^x]"), "_")
                .replace(Regex("\\\\x.."), "_")
                .length
        }.sum()

    override val part2: Any
        get() = input().map {
            it.count { c -> c in listOf('"', '\\') } + 2
        }.sum()
}

fun main() {
    Day8.fancyRun()
}

