package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode

object Day6 : AdventOfCode(2022, 6) {
    override val part1 get() = input().first().windowed(4).indexOfFirst { it.toSet().size == 4 } + 4
    override val part2 get() = input().first().windowed(14).indexOfFirst { it.toSet().size == 14 } + 14
}

fun main() {
    Day6.fancyRun()
}
