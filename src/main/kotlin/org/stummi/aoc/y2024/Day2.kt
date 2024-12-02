package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.map
import kotlin.math.absoluteValue
import kotlin.math.sign

object Day2 : AdventOfCode(2024, 2) {
    override val part1
        get() = parsedInput().count { isSafe(it) }

    override val part2: Any
        get() = parsedInput().count { isSafe(it)
                || it.indices.any { i ->
                    isSafe(it.toMutableList().apply {
                        removeAt(i)
                    })
                }
        }

    private fun isSafe(it: List<Int>): Boolean {
        val steps = it.windowed(2).map { (a, b) ->
            a - b
        }

        if (steps.map { it.sign }.distinct().size != 1) {
            return false
        }

        return steps.all { it.absoluteValue in 1..3 }
    }

    private fun parsedInput() = input.lines.map {
        it.split(" ").map(String::toInt)
    }
}

fun main() {
    Day2.fancyRun()
}
