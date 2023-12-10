package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day3 : AdventOfCode(2016, 3) {
    private fun lines(): List<List<Int>> = inputLines().map {
        it.split(" ").filterNot { it.isBlank() }.map { it.toInt() }
    }

    override val part1: Int
        get() =
            lines().map { it.sorted() }.filterNot {
                it[2] >= it[0] + it[1]
            }.count()

    override val part2: Int
        get() =
            lines().chunked(3).flatMap {
                (0 until it[0].size).map { r ->
                    listOf(it[0][r], it[1][r], it[2][r])
                }
            }.map { it.sorted() }.filterNot {
                it[2] >= it[0] + it[1]
            }.count()
}

fun main() {
    println(Day3.part1)
    println(Day3.part2)
}
