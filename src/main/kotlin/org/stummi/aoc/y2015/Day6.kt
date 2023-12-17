package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.XYRange

object Day6 : AdventOfCode(2015, 6) {
    class LightMatrix(
        private val matrix: IntArray = IntArray(1000 * 1000)
    ) {
        operator fun get(xy: XY) = matrix[posToIdx(xy)]
        operator fun set(xy: XY, value: Int) {
            matrix[posToIdx(xy)] = value
        }

        private fun posToIdx(xy: XY) = xy.x * 1000 + xy.y

        fun sum() = matrix.sum()
    }

    data class LightCmd(val cmd: String, val start: XY, val end: XY) {
        fun apply(func: (XY) -> Unit) {
            XYRange(start, end).asSequence().forEach(func)
        }
    }

    override val part1: Int
        get() {
            val matrix = LightMatrix()
            parseInput().forEach { cmd ->
                when (cmd.cmd) {
                    "on" -> cmd.apply { matrix[it] = 1 }
                    "off" -> cmd.apply { matrix[it] = 0 }
                    "toggle" -> cmd.apply { matrix[it] = 1 - matrix[it] }
                    else -> throw IllegalArgumentException()
                }
            }
            return matrix.sum()
        }

    override val part2: Int
        get() {
            val matrix = LightMatrix()
            parseInput().forEach { cmd ->
                when (cmd.cmd) {
                    "on" -> cmd.apply { matrix[it]++ }
                    "off" -> cmd.apply { matrix[it] = (matrix[it] - 1).coerceAtLeast(0) }
                    "toggle" -> cmd.apply { matrix[it] += 2 }
                    else -> throw IllegalArgumentException()
                }
            }
            return matrix.sum()
        }

    private fun parseInput() =
        inputLines().map {
            it.split(" ").let { spl ->
                if (spl.size == 4) {
                    spl
                } else {
                    spl.drop(1)
                }
            }
        }.map {
            LightCmd(it[0], parsePoint(it[1]), parsePoint(it[3]))
        }

    fun parsePoint(s: String) = s.split(",").let {
        XY(it[0].toInt(), it[1].toInt())
    }
}

fun main() {
    Day6.fancyRun()
}
