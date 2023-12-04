package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode

object Day1 : AdventOfCode(2015, 1) {
    init {
        rawSample("))(((((", 3, 1)
    }

    override val part1 get() = solve.first
    override val part2 get() = solve.second

    val solve: Pair<Int, Int> by lazy {
        var level = 0
        var firstBasement = 0
        val input = input().first().forEachIndexed { pos, it ->
            when (it) {
                '(' -> ++level
                ')' -> --level
                else -> throw IllegalArgumentException(it.toString())
            }


            if (level == -1 && firstBasement == 0) {
                firstBasement = pos + 1
            }
        }

        level to firstBasement
    }
}

fun main() {
    Day1.fancyRun()
}

