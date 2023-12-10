package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day6 : AdventOfCode(2016, 6) {

    private fun solve(f: (Int) -> Int) = inputLines().let { input ->
        (0 until input[0].length).map { pos ->
            input.map { it[pos] }
        }
    }.map {
        it.groupingBy { it }.eachCount().maxByOrNull { f(it.value) }!!.key
    }.joinToString("")

    override val part1: Any
        get() = solve { it }

    override val part2: Any
        get() = solve { -it }


}

fun main() {
    println(Day6.part1)
    println(Day6.part2)
}
