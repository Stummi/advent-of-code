package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode

object Day6 : AdventOfCode(2017, 6) {
    init {
        rawSample("0\t2\t7\t0", 5, 4)
    }

    val parsedInput by lazy { input.lines.first().split("\t").map { it.toInt() } }

    override val part1: Any
        get() {
            var v = parsedInput
            val seenConfigurations = mutableSetOf(parsedInput)

            while (true) {
                v = redistribute(v)
                if (v in seenConfigurations) {
                    break
                }
                seenConfigurations.add(v)
            }

            return seenConfigurations.size
        }

    override val part2: Any
        get() {
            var v = parsedInput
            val seenConfigurations = mutableMapOf(parsedInput to 0)
            var cnt = 0

            while (true) {
                v = redistribute(v)
                if (v in seenConfigurations) {
                    break
                }
                seenConfigurations[v] = ++cnt
            }

            return seenConfigurations.size - seenConfigurations[v]!!
        }

    private fun redistribute(parsedInput: List<Int>): List<Int> {
        val maxValue = parsedInput.max()
        val maxPos = parsedInput.indexOf(maxValue)
        val len = parsedInput.size
        val split = maxValue / len
        val remain = maxValue % len

        return parsedInput.mapIndexed { idx, v ->
            var idxAfterMaxPos = idx - maxPos - 1
            if (idxAfterMaxPos < 0) {
                idxAfterMaxPos += len
            }

            val c = if (idx == maxPos) {
                0
            } else {
                v
            }

            if (idxAfterMaxPos < remain) {
                split + 1
            } else {
                split
            } + c
        }


    }
}

fun main() {
    Day6.fancyRun()
}
