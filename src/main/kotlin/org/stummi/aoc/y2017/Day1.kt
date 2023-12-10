package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode

object Day1 : AdventOfCode(2017, 1) {
    init {
        rawSample("1122", 3)
        rawSample("1111", 4)
        rawSample("1234", 0)
        rawSample("91212129", 9)
    }

    override val part1: Any
        get() = inputLines()[0].toList().let { it + it[0] }.windowed(2)
            .filter { it[0] == it[1] }.sumOf { it[0].digitToInt() }

    override val part2: Any
        get() = inputLines()[0].toList().let { l -> l.mapIndexed { i, c -> c to l[(i + l.size / 2) % l.size] } }
            .filter { it.first == it.second }.sumOf { it.first.digitToInt() }
}

fun main() {
    Day1.fancyRun()
}
