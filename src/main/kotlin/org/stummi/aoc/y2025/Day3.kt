package org.stummi.aoc.y2025

import org.stummi.aoc.AdventOfCode

object Day3 : AdventOfCode(2025, 3) {
    init {
        rawSample("987654321111111", 98)
        rawSample("811111111111119", 89)
        rawSample("234234234234278", 78)
        rawSample("234234234234278", 92)
    }

    override val part1: Any
        get() {
            return input.lines.sumOf { line ->
                val digits = line.map { it.digitToInt() }
                findLargestJoltage(digits, 2).toLong()
            }
        }

    override val part2: Any
        get() {
            return input.lines.sumOf { line ->
                val digits = line.map { it.digitToInt() }
                findLargestJoltage(digits, 12).toLong()
            }
        }

    fun findLargestJoltage(allDigits: List<Int>, digitsToPick: Int): String {
        val max = allDigits.dropLast(digitsToPick-1).max()
        if(digitsToPick == 1) {
            return "$max"
        }

        return "$max" + findLargestJoltage(allDigits.dropWhile { it != max }.drop(1), digitsToPick - 1)
    }

}

fun main() {
    Day3.fancyRun()
}