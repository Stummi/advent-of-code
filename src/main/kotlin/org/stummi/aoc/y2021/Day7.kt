package org.stummi.aoc.y2021

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.splitToInts
import kotlin.math.abs

object Day7 : AdventOfCode(2021, 7) {
    private val parsedInput by lazy {
        input.line.splitToInts()
    }

    override val part1 get() = solve(::costFunc1)
    override val part2 get() = solve(::costFunc2)

    private fun solve(costFunc: (Int) -> Int): Int {
        val crabs = parsedInput.groupingBy { it }.eachCount()
        val min = crabs.keys.min()
        val max = crabs.keys.max()
        val toMap = (min..max).associateWith { d ->
            crabs.map { (cd, count) -> count * costFunc(abs(cd - d)) }.sum()
        }

        return toMap.minBy { (_, v) -> v }.value
    }

    private fun costFunc1(d: Int) = d
    private fun costFunc2(d: Int) = d * (d + 1) / 2
}

fun main() {
    Day7.fancyRun()
}


