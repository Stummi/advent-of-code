package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode

object Day20 : AdventOfCode(2015, 20) {
    val parsedInput get() = input.lines.first().toInt()

    override val part1: Int
        get() {
            val maxHouses = parsedInput / 10
            val houses = IntArray(maxHouses + 1)
            (1..maxHouses).forEach { e ->
                (e..maxHouses step e).forEach {
                    houses[it] += e * 10
                }
                if (houses[e] >= parsedInput) {
                    return e
                }
            }
            return 0
        }

    override val part2: Int
        get() {
            val maxHouses = parsedInput / 10
            val houses = IntArray(maxHouses + 1)
            (1..maxHouses).forEach { e ->
                repeat(50) {
                    val i = e * (it + 1)
                    if (i < maxHouses) {
                        houses[i] += e * 11
                    }
                }

                if (houses[e] >= parsedInput) {
                    return e
                }
            }
            return -1
        }
}

fun main() {
    Day20.fancyRun()
}

