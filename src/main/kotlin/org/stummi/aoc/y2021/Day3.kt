package org.stummi.aoc.y2021

import org.stummi.aoc.AdventOfCode

object Day3 : AdventOfCode(2021, 3) {
    override val part1: Any
        get() {
            val lines = input.lines
            var gamma = 0
            var epsilon = 0
            repeat(lines[0].length) { pos ->
                gamma *= 2
                epsilon *= 2
                val ones = lines.count { it[pos] == '1' }
                val zeroes = lines.size - ones
                if (ones > zeroes) {
                    gamma += 1
                } else {
                    epsilon += 1
                }
            }
            return gamma * epsilon
        }

    override val part2: Any
        get() {
            val lines = input.lines
            var oxyLines = lines
            var scrubberLines = lines
            repeat(lines[0].length) { pos ->
                if (oxyLines.size > 1) {
                    val ones = oxyLines.count { it[pos] == '1' }
                    val zeroes = oxyLines.size - ones

                    val filter = if (ones >= zeroes) {
                        '1'
                    } else {
                        '0'
                    }

                    oxyLines = oxyLines.filter { it[pos] == filter }
                }

                if (scrubberLines.size > 1) {
                    val ones = scrubberLines.count { it[pos] == '1' }
                    val zeroes = scrubberLines.size - ones

                    val filter = if (ones >= zeroes) {
                        '0'
                    } else {
                        '1'
                    }

                    scrubberLines = scrubberLines.filter { it[pos] == filter }
                }
            }

            return scrubberLines.first().toInt(2) * oxyLines.first().toInt(2)
        }
}

fun main() {
    Day3.fancyRun()
}
