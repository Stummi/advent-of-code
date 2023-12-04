package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.XYRange
import java.util.BitSet

object Day18 : AdventOfCode(2015, 18) {
    class GameOfLife(
        val w: Int,
        val h: Int,
    ) {
        private val fields: BitSet = BitSet(w * h)

        operator fun get(xy: XY): Boolean {
            return fields[xyToIdx(xy)]
        }

        operator fun set(xy: XY, value: Boolean) {
            fields[xyToIdx(xy)] = value
        }

        fun xyToIdx(xy: XY): Int {
            return xy.y * w + xy.x
        }

        fun step(): GameOfLife {
            val onRules = listOf(2, 3)
            val offRules = listOf(3)

            val ret = GameOfLife(w, h)

            val xyRange = XYRange(XY.ZERO, XY(w - 1, h - 1))
            xyRange.asSequence().forEach { xy ->
                val neighbors = xy.mooreNeighbours().filter { it in xyRange }.count { this[it] }
                val on = this[xy]
                val rules = if (on) onRules else offRules
                ret[xy] = rules.contains(neighbors)
            }

            return ret
        }

        fun countLit(): Int {
            return fields.cardinality()
        }
    }

    fun parsedInput(): GameOfLife {
        val lines = input()
        val w = lines[0].length
        val h = lines.size

        val board = GameOfLife(w, h)

        lines.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                when (c) {
                    '#' -> board[XY(x, y)] = true
                    '.' -> {}
                    else -> throw IllegalArgumentException()
                }
            }
        }

        return board
    }

    override val part1: Any
        get() = parsedInput().let { it ->
            var board = it
            repeat(100) {
                board = board.step()
            }
            board.countLit()
        }

    override val part2: Any
        get() = parsedInput().let { it ->
            var board = it
            board[XY(0, 0)] = true
            board[XY(0, it.h - 1)] = true
            board[XY(it.w - 1, 0)] = true
            board[XY(it.w - 1, it.h - 1)] = true
            repeat(100) { _ ->
                board = board.step()
                board[XY(0, 0)] = true
                board[XY(0, it.h - 1)] = true
                board[XY(it.w - 1, 0)] = true
                board[XY(it.w - 1, it.h - 1)] = true
            }
            board.countLit()
        }

}

fun main() {
    Day18.fancyRun()
}
