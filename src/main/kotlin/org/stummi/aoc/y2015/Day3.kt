package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY

object Day3 : AdventOfCode(2015, 3) {
    override val part1: Int
        get() = solve(1)

    override val part2: Int
        get() = solve(2)

    fun solve(santas: Int): Int {
        val houses = mutableMapOf(XY.ZERO to santas)
        val santaPositions = generateSequence { XY.ZERO }.take(santas).toMutableList()
        inputLines().first().forEachIndexed { santaId, it ->
            val pos = santaPositions[santaId % santas]
            val newPos = when (it) {
                '>' -> pos.right
                '<' -> pos.left
                '^' -> pos.up
                'v' -> pos.down
                else -> throw IllegalStateException()
            }
            santaPositions[santaId % santas] = newPos
            houses[newPos] = (houses[newPos] ?: 0) + 1
        }
        return houses.count()
    }
}

fun main() {
    Day3.fancyRun()
}
