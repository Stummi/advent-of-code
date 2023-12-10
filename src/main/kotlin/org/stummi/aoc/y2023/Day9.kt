package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode

object Day9 : AdventOfCode(2023, 9) {
    val parsedInput by lazy { inputLines().map { it.split(" ").map(String::toInt) } }

    override val part1: Any
        get() = parsedInput.sumOf { resolveSequence(it) }

    override val part2: Any
        get() = parsedInput.sumOf { resolveSequence(it.reversed()) }

    private fun resolveSequence(line: List<Int>): Int {
        val differences = line.windowed(2).map { (a, b) -> b - a }
        return if (differences.all { it == 0 })
            line.last()
        else
            line.last() + resolveSequence(differences)
    }
}

fun main() {
    Day9.fancyRun()
}

