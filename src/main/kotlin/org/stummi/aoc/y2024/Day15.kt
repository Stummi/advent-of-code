package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.CharMatrix
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.partitionBy

object Day15 : AdventOfCode(2024, 15) {

    init {
        resourceSample("demo_2")
        resourceSample("demo_3")
    }

    private data class State(
        var robot: XY,
        val map: CharMatrix,
    ) {
        fun moveRobot(direction: XY) {
            if (moveTile(robot, direction)) {
                map[robot] = '.'
                robot += direction
            }
        }

        fun moveRobotPt2(direction: XY) {
            if (canMoveTilePt2(robot, direction)) {
                moveTilePt2(robot, direction)
                map[robot] = '.'
                robot += direction
            }
        }

        private fun canMoveTilePt2(
            pos: XY,
            dir: XY,
        ): Boolean {
            return when (map[pos + dir]) {
                '#' -> false
                '.' -> true
                '[' -> {
                    canMoveTilePt2(pos + dir, dir) &&
                            (dir == XY.ZERO.left || canMoveTilePt2((pos + dir).right, dir))
                }

                ']' -> {
                    canMoveTilePt2(pos + dir, dir) &&
                            (dir == XY.ZERO.right || canMoveTilePt2((pos + dir).left, dir))
                }

                else -> error(map[pos + dir])
            }
        }

        private fun moveTilePt2(pos: XY, dir: XY, neighbours: Boolean = true) {
            if (neighbours) {
                when (map[pos]) {
                    '[' -> moveTilePt2(pos.right, dir, false)
                    ']' -> moveTilePt2(pos.left, dir, false)
                }
            }

            when (map[pos + dir]) {
                '#' -> error("wall")
                '[', ']' -> moveTilePt2(pos + dir, dir)
            }

            map[pos + dir] = map[pos]
            map[pos] = '.'
        }

        private fun moveTile(pos: XY, dir: XY): Boolean {
            when (map[pos + dir]) {
                '#' -> return false
                'O' -> {
                    if (moveTile(pos + dir, dir)) {
                        map[pos + dir] = map[pos]
                        return true
                    } else {
                        return false
                    }
                }

                '.' -> {
                    map[pos + dir] = map[pos]
                    return true
                }

                else -> error(map[pos + dir])
            }
        }
    }

    override val part1: Any
        get() {
            val (m, moves) = input.lines.partitionBy { it.isEmpty() }
            val map = CharMatrix.fromLines(m)
            val robot = map.find('@')
            val state = State(robot, map)

            moves.joinToString("").forEach {
                val dir = when (it) {
                    '^' -> XY.ZERO.up
                    '>' -> XY.ZERO.right
                    '<' -> XY.ZERO.left
                    'v' -> XY.ZERO.down
                    else -> error(it)
                }
                state.moveRobot(dir)
            }

            return state.map.findAll('O').sumOf {
                it.x + 100 * it.y
            }
        }

    override val part2: Any
        get() {
            val (m, moves) = input.lines.partitionBy { it.isEmpty() }
            val map = m.map { line ->
                line.map { c ->
                    when (c) {
                        '#' -> "##"
                        'O' -> "[]"
                        '.' -> ".."
                        '@' -> "@."
                        else -> error(c)
                    }
                }.joinToString("")
            }.let {
                CharMatrix.fromLines(it)
            }

            val robot = map.find('@')
            val state = State(robot, map)

            moves.joinToString("").forEach {
                val dir = when (it) {
                    '^' -> XY.ZERO.up
                    '>' -> XY.ZERO.right
                    '<' -> XY.ZERO.left
                    'v' -> XY.ZERO.down
                    else -> error(it)
                }
                state.moveRobotPt2(dir)
            }

            return state.map.findAll('[').sumOf {
                it.x + 100 * it.y
            }
        }
}

fun main() {
    Day15.fancyRun()
}
