package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode

object Day5 : AdventOfCode(2017, 5) {
    private val parsedInput: List<Int> get() = input().map { it.toInt() }

    override val part1: Any
        get() {
            val instructions = parsedInput.toMutableList()
            var current = 0
            var cnt = 0
            while (current in instructions.indices) {
                current += instructions[current]++
                ++cnt
            }
            return cnt
        }

    override val part2: Any
        get() {
            val instructions = parsedInput.toMutableList()
            var current = 0
            var cnt = 0
            while (current in instructions.indices) {
                val offset = instructions[current]
                if (offset >= 3) {
                    instructions[current]--
                } else {
                    instructions[current]++
                }
                current += offset
                ++cnt
            }
            return cnt
        }
}

fun main() {
    Day5.fancyRun()
}
