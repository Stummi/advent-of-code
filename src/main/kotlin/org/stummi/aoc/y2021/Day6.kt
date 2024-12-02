package org.stummi.aoc.y2021

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.splitToInts
import java.math.BigInteger

object Day6 : AdventOfCode(2021, 6) {
    val parsedInput by lazy { input.line.splitToInts() }

    override val part1: Any get() = solve.first
    override val part2: Any get() = solve.second


    val solve by lazy {
        var group = parsedInput.groupingBy { it }.eachCount()
            .map { (k, v) -> k to BigInteger.valueOf(v.toLong()) }.toMap().toMutableMap()

        lateinit var part1Solution: BigInteger

        repeat(256) {
            group = group.map { (k, v) -> k - 1 to v }.toMap().toMutableMap()
            val newFish = group.remove(-1) ?: BigInteger.ZERO
            group[6] = (group[6] ?: BigInteger.ZERO).plus(newFish)
            group[8] = newFish

            if (it == 79) {
                part1Solution = group.values.reduce(BigInteger::plus)
            }
        }

        part1Solution to group.values.reduce(BigInteger::plus)
    }
}

fun main() {
    Day6.fancyRun()
}
