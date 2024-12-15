package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode

object Day11 : AdventOfCode(2024, 11) {
    init {
        rawSample("125 17", result1 = 55312)
    }

    override val part1 get() = parsedInput.sumOf { countStones(it, 25) }
    override val part2 get() = parsedInput.sumOf { countStones(it, 75) }

    private val parsedInput by lazy {
        input.line.split(" ").map {
            it.toLong()
        }
    }

    private val cache = mutableMapOf<Pair<Long, Int>, Long>()

    private fun countStones(value: Long, blinks: Int): Long {
        if (blinks == 0) {
            return 1L
        }

        val key = value to blinks
        return cache.getOrPut(key) {
            if (value == 0L) {
                return@getOrPut countStones(1L, blinks - 1)
            }

            val str = value.toString()
            if (str.length % 2 == 0) {
                val (val1, val2) = str.chunked(str.length / 2).map { it.toLong() }
                return@getOrPut countStones(val1, blinks - 1) + countStones(val2, blinks - 1)
            }

            return@getOrPut countStones(value * 2024L, blinks - 1)
        }
    }
}

fun main() {
    Day11.fancyRun()
}
