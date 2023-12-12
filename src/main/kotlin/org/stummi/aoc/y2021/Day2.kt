package org.stummi.aoc.y2021

import org.stummi.aoc.AdventOfCode

object Day2 : AdventOfCode(2021, 2) {
    val parsedInput = input.lines.map {
        val (cmd, numStr) = it.split(" ")
        val number = numStr.toInt()
        when (cmd) {
            "forward" -> (number to 0)
            "down" -> (0 to number)
            "up" -> (0 to -number)
            else -> TODO()
        }
    }
    override val part1: Any
        get() = parsedInput.reduce { (a1, b1), (a2, b2) ->
            a1 + a2 to b1 + b2
        }.let { (a, b) -> a * b }

    override val part2: Any
        get() {
            var aim = 0
            var hPos = 0
            var depth = 0
            parsedInput.forEach { (forw, aimCh) ->
                aim += aimCh
                hPos += forw
                depth += aim * forw
            }
            return hPos * depth
        }
}

fun main() {
    Day2.fancyRun()
}
