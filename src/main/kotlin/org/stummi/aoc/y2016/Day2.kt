package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.IntMatrix
import org.stummi.aoc.helper.XY

object Day2 : AdventOfCode(2016, 2) {
    val keyPad1 = IntMatrix(
        listOf(
            listOf(0, 0, 0, 0, 0),
            listOf(0, 1, 2, 3, 0),
            listOf(0, 4, 5, 6, 0),
            listOf(0, 7, 8, 9, 0),
            listOf(0, 0, 0, 0, 0),
        )
    )

    val startPos1 = XY(2, 2)

    val keyPad2 = IntMatrix(
        listOf(
            listOf(0, 0, 0, 0, 0, 0, 0),
            listOf(0, 0, 0, 1, 0, 0, 0),
            listOf(0, 0, 2, 3, 4, 0, 0),
            listOf(0, 5, 6, 7, 8, 9, 0),
            listOf(0, 0, 10, 11, 12, 0, 0),
            listOf(0, 0, 0, 13, 0, 0, 0),
            listOf(0, 0, 0, 0, 0, 0, 0),
        )
    )

    val startPos2 = XY(3, 3)

    private fun solve(keyPad: IntMatrix, startPos: XY): String {
        var pos = startPos

        return inputLines().map {
            it.fold(pos) { pos, c ->
                val pos2 = when (c) {
                    'U' -> pos.move(y = -1)
                    'D' -> pos.move(y = 1)
                    'L' -> pos.move(x = -1)
                    'R' -> pos.move(x = 1)
                    else -> throw IllegalArgumentException()
                }
                if (keyPad[pos2] == 0) pos else pos2
            }
        }.map {
            keyPad[it].toString(16)
        }.joinToString("")
    }

    override val part1: String
        get() {
            return solve(keyPad1, startPos1)
        }

    override val part2: String
        get() {
            return solve(keyPad2, startPos2)
        }

}

fun main() {
    println(Day2.part1)
    println(Day2.part2)
}
