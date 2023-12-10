package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode

object Day4 : AdventOfCode(2017, 4) {
    val parsedInput = input().map { it.split(" ") }

    override val part1: Any
        get() = parsedInput.count { it.size == it.toSet().size }

    override val part2: Any
        get() = parsedInput.count {
            val sorted = it.map { it.toList().sorted().joinToString("") }
            sorted.size == sorted.toSet().size
        }
}

fun main() {
    Day4.fancyRun()
}
