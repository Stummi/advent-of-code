package org.stummi.aoc.y2021

import org.stummi.aoc.AdventOfCode

object Day1 : AdventOfCode(2021, 1) {
    private val parsedInput by lazy { input.lines.map(String::toInt) }
    override val part1: Any
        get() = parsedInput.windowed(2).count { (a, b) -> b > a }

    override val part2: Any
        get() = parsedInput.windowed(3).map { it.sum() }.windowed(2).count { (a, b) -> b > a }
}

fun main() {
    Day1.fancyRun()
}
