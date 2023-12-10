package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.sorted
import org.stummi.aoc.helper.tuplePermutations

object Day2 : AdventOfCode(2017, 2) {
    init {
        resourceSample("demo", 18, Unit)
        resourceSample("demo2", Unit, 9)
    }

    fun parseInput() = inputLines().map { it.split(" ", "\t").map(String::toInt) }
    override val part1: Any
        get() = parseInput().map { numbers ->
            numbers.max() - numbers.min()
        }.sum()

    override val part2: Any
        get() = parseInput().map {
            it.tuplePermutations(false).map(Pair<Int, Int>::sorted).filter {
                it.second % it.first == 0
            }.map {
                it.second / it.first
            }.first()
        }.sum()
}


fun main() {
    Day2.fancyRun()
}
