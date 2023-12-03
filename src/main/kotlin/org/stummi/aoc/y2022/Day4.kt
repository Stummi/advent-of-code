package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.sorted

object Day4 : AdventOfCode(2022, 4) {
    override val part1: Any
        get() = data().count {
            fullyContains(it.first, it.second) || fullyContains(it.second, it.first)
        }

    override val part2
        get() = data().count {
            overlaps(it.first, it.second) || overlaps(it.second, it.first)
        }

    private fun overlaps(first: IntRange, second: IntRange): Boolean =
        first.contains(second.first)


    private fun data() = input().map {
        it.split(Regex("[\\-,]")).map { it.toInt() }
    }.map {
        (it[0]..it[1]) to (it[2]..it[3])
    }.map { it.sorted(Comparator.comparingInt { it.first }) }

    private fun fullyContains(first: IntRange, second: IntRange): Boolean {
        return first.contains(second.first) && first.contains(second.last)
    }

}

fun main() {
    Day4.fancyRun()
}
