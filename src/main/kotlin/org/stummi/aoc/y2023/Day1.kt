package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode

object Day1 : AdventOfCode(2023, 1) {
    override val part1: Any
        get() = input().map {
            val l = it.filter { it.isDigit() }.toList()
            assert(l.size >= 2)
            l.first().digitToInt() * 10 + l.last().digitToInt()
        }.sum()


    val numbers = mapOf(
        "one" to '1',
        "two" to '2',
        "three" to '3',
        "four" to '4',
        "five" to '5',
        "six" to '6',
        "seven" to '7',
        "eight" to '8',
        "nine" to '9',
    )

    val anyNumberRegex = Regex("(?=(\\d|${numbers.keys.joinToString("|")}))")

    override val part2: Any
        get() = input().map {
            val finds = anyNumberRegex.findAll(it)

            val firstFind = finds.first().groupValues.first { it.isNotBlank() }
            val lastFind = finds.last().groupValues.first { it.isNotBlank() }

            val first = (numbers[firstFind] ?: firstFind.first()).digitToInt()
            val last = (numbers[lastFind] ?: lastFind.first()).digitToInt()
            first * 10 + last
        }.sum()
}

fun main() {

    Day1.fancyRun()
}
