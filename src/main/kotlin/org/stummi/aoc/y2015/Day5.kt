package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode

object Day5 : AdventOfCode(2015, 5) {
    override val part1: Any
        get() {
            val vowels = "aeiou".toList()
            val forbiddenStrings = listOf("ab", "cd", "pq", "xy")

            val niceRules = listOf<(String) -> Boolean>(
                { it.count { it in vowels } >= 3 },
                { (1 until it.length).any { idx -> it[idx - 1] == it[idx] } },
                { forbiddenStrings.none { fs -> fs in it } }
            )

            return input().count { s -> niceRules.all { it(s) } }
        }

    override val part2: Any
        get() {
            val niceRules = listOf<(String) -> Boolean>(
                { (2 until it.length).any { idx -> it.indexOf(it.substring(idx - 2, idx), idx) >= 0 } },
                { (2 until it.length).any { idx -> it[idx - 2] == it[idx] } }
            )

            return input().count { s -> niceRules.all { it(s) } }
        }

}

fun main() {
    Day5.fancyRun()
}
