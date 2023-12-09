package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode

object Day9 : AdventOfCode(2023, 9) {
    val parsedInput by lazy { input().map { it.split(" ").map(String::toInt) } }

    override val part1: Any
        get() = parsedInput.sumOf { resolveSequence(it) }

    override val part2: Any
        get() = parsedInput.sumOf { resolveSequenceReversed(it) }

    private fun resolveSequence(line: List<Int>): Int {
        val differences = line.windowed(2).map { (a, b) -> b - a }
        return if (differences.all { it == 0 })
            line.last()
        else
            line.last() + resolveSequence(differences)
    }

    private fun resolveSequenceReversed(line: List<Int>): Int {
        val differences = line.windowed(2).map { (a, b) -> b - a }
        return if (differences.all { it == 0 })
            line.first()
        else
            line.first() - resolveSequenceReversed(differences)
    }
}

fun main() {
    Day9.fancyRun()
}

