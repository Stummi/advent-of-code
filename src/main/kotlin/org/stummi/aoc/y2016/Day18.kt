package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day18 : AdventOfCode(2016, 18) {
    init {
        rawSample(".^^.^.^^^^", result1 = 38, additionalData = 10)
    }

    override val part1: Int
        get() {
            return generateSequence(input().first()) { nextLine(it) }.take(40.orSample()).map {
                it.count { it == '.' }
            }.sum()
        }
    override val part2: Int
        get() {
            return generateSequence(input().first()) { nextLine(it) }.take(400000).map {
                it.count { it == '.' }
            }.sum()
        }

    fun nextLine(input: String): String {
        return ".$input.".let { line ->
            (1..input.length).map {
                when (line.substring(it - 1..it + 1)) {
                    "^^.", ".^^", "^..", "..^" -> "^"
                    else -> "."
                }
            }.joinToString("")
        }
    }
}

fun main() {
    Day18.fancyRun()
}
