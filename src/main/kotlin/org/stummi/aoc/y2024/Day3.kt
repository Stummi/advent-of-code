package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode

object Day3 : AdventOfCode(2024, 3) {
    init {
        rawSample("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))", 161)
        rawSample("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))", result2 = 48)
    }

    override val part1: Any
        get() {
            val regex = Regex("mul\\((\\d+),(\\d+)\\)")
            return regex.findAll(input.lines.joinToString("")).sumOf {
                val (_, a, b) = it.groupValues
                a.toInt() * b.toInt()
            }
        }

    override val part2: Any
        get() {
            val regex = Regex("do(n't)?\\(\\)|mul\\((\\d+),(\\d+)\\)")
            var d = 1
            return regex.findAll(input.lines.joinToString("")).sumOf {
                val g = it.groupValues
                when {
                    g[0].startsWith("mul") -> g[2].toInt() * g[3].toInt() * d
                    g[0] == "don't()" -> {d = 0; 0}
                    g[0] == "do()" -> {d = 1; 0}
                    else -> throw IllegalStateException()
                }
            }
        }
}

fun main() {
    Day3.fancyRun()
}
