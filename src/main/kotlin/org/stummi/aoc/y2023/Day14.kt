package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.CharMatrix
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.XYRange
import org.stummi.aoc.helper.XYRange.IterationOrder.BT_RL
import org.stummi.aoc.helper.XYRange.IterationOrder.TB_LR

object Day14 : AdventOfCode(2023, 14) {
    private enum class Moves(val iter: XYRange.IterationOrder, val move: (XY) -> XY) {
        NORTH(TB_LR, XY::up),
        WEST(TB_LR, XY::left),
        SOUTH(BT_RL, XY::down),
        EAST(BT_RL, XY::right),
    }

    private fun moveMap(map: CharMatrix, dir: Moves) {
        map.bounds.asSequence(dir.iter)
            .filter { map[it] == 'O' }
            .forEach { xy ->
                var target = xy
                while (true) {
                    val nextTarget = dir.move(target)
                    if (nextTarget in map.bounds && map[nextTarget] == '.') {
                        target = nextTarget
                    } else {
                        break
                    }
                }

                if (xy != target) {
                    map[target] = 'O'
                    map[xy] = '.'
                }
            }
    }

    override val part1: Any
        get() {
            val map = input.matrix

            moveMap(map, Moves.NORTH)
            return mapPoints(map)
        }

    private fun mapPoints(map: CharMatrix): Int {
        val h = map.bounds.height
        return (0..<h).sumOf { r ->
            val rowPoints = h - r
            rowPoints * map.row(r).count { it == 'O' }
        }
    }

    override val part2: Any
        get() {
            val map = input.matrix
            val resultToCycle = mutableMapOf(
                map.clone() to 0
            )
            var iteration = 0
            while (true) {
                Moves.entries.forEach {
                    moveMap(map, it)
                }

                ++iteration
                if (map in resultToCycle) {
                    break
                }
                resultToCycle[map.clone()] = iteration
            }

            val cycleStart = resultToCycle[map]!!
            val cycleLen = iteration - cycleStart

            val x = cycleStart + ((1000000000 - cycleStart) % cycleLen)
            return mapPoints(resultToCycle.filterValues { it == x }.keys.first())
        }
}

fun main() {
    Day14.fancyRun()
}
