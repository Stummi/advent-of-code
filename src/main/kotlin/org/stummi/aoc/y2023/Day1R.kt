package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode

object Day1R : AdventOfCode(2023, 1) {
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

    private fun firstDigitInString(s: String): Int {
        (0 until s.length).forEach {
            if (s[it].isDigit()) {
                return s[it].digitToInt()
            }
            numbers.entries.firstOrNull { (k, _) -> s.substring(it).startsWith(k) }?.let { (_, v) ->
                return v.digitToInt()
            }
        }
        throw Exception("No digit in $s")
    }

    private fun lastDigitInString(s: String): Int {
        ((s.length - 1) downTo 0).forEach {
            if (s[it].isDigit()) {
                return s[it].digitToInt()
            }
            numbers.entries.firstOrNull { (k, _) -> s.substring(it).startsWith(k) }?.let { (_, v) ->
                return v.digitToInt()
            }
        }
        throw Exception("No digit in $s")
    }

}

fun main() {

    Day1R.fancyRun()
}
