package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY

object Day3 : AdventOfCode(2017, 3) {
    override val part1: Any
        get() {
            var target = inputLines().first().toInt()
            spiralCord.drop(target - 1).first().let {
                return it.orthogonalDistanceTo(XY.ZERO)
            }
        }

    override val part2: Any
        get() {
            val target = inputLines().first().toInt()
            val values = mutableMapOf(XY.ZERO to 1)
            spiralCord.drop(1).forEach { pos ->
                val value = pos.mooreNeighbours().sumOf { values[it] ?: 0 }
                values[pos] = value
                if (value > target) {
                    return value
                }
            }
            throw IllegalStateException()
        }

    val spiralCord = sequence {
        var cur = XY.ZERO
        yield(cur)
        var diag = 0
        while (true) {
            ++diag
            cur = cur.right
            yield(cur)
            val moves = diag * 2
            repeat(moves - 1) {
                cur = cur.up
                yield(cur)
            }
            repeat(moves) {
                cur = cur.left
                yield(cur)
            }
            repeat(moves) {
                cur = cur.down
                yield(cur)
            }
            repeat(moves) {
                cur = cur.right
                yield(cur)
            }
        }
    }
}

fun main() {
    Day3.fancyRun()
}
