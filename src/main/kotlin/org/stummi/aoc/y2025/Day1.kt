package org.stummi.aoc.y2025

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.map
import kotlin.math.absoluteValue

object Day1 : AdventOfCode(2025, 1) {
    init {
        rawSample("R1000", result2 = 10)
        rawSample("L1000", result2 = 10)
        rawSample("R150", result2 = 2)
        rawSample("L150", result2 = 2)
        rawSample("L68\nL30\nR48\nL5\nR60\nL55\nL1\nL99\nR14\nL82", 3, 6)
    }
    override val part1: Any
        get() {
            var dial = 50
            var pw = 0
            parsedInput().forEach {
                dial += it
                if(dial % 100 == 0) {
                    ++pw
                }
            }
            return pw
        }

    override val part2: Any
        get() {
            var dial = 50
            var pw = 0
            parsedInput().forEach {
                val oldDial = dial
                dial += it

                if(dial < 0) {
                    if(oldDial != 0) {
                        ++pw
                    }
                    dial += 100
                    while(dial < 0) {
                        dial += 100
                        ++pw
                    }
                }


                if(dial == 0 && oldDial != 0) {
                    ++pw
                }

                if(dial >= 100) {
                    pw += dial/100
                    dial %= 100
                }
            }
            return pw
        }

    private fun parsedInput() =
        input.lines.map {
            val n = it.drop(1).toInt()
            when (it.first()) {
                'L' -> -n
                'R' -> n
                else -> throw IllegalStateException()
            }

        }

}

fun main() {
    //7656 = too high
    Day1.fancyRun()
}