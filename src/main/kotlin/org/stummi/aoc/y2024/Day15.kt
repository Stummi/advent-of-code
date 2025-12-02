package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.CharMatrix
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.partitionBy

object Day15 : AdventOfCode(2024, 15) {

    init {
        resourceSample("demo", result1 = 2028)
        resourceSample("demo_2")
        resourceSample("demo_3", result1 = 10092, result2 = 9021 )
    }

    private data class State(
        var robot: XY,
        val map: CharMatrix,
    ) {
        fun moveRobot(direction: XY) {
            if (canMoveTile(robot, direction)) {
                moveTile(robot, direction)
                map[robot] = '.'
                robot += direction
            }
        }

        private fun canMoveTile(
            pos: XY,
            dir: XY,
        ): Boolean {
            return when (map[pos + dir]) {
                '#' -> false
                '.' -> true
                '[' -> {
                    canMoveTile(pos + dir, dir) &&
                            (dir == XY.ZERO.left || canMoveTile((pos + dir).right, dir))
                }

                ']' -> {
                    canMoveTile(pos + dir, dir) &&
                            (dir == XY.ZERO.right || canMoveTile((pos + dir).left, dir))
                }

                'O' -> {
                    canMoveTile(pos + dir, dir)
                }

                else -> error(map[pos + dir])
            }
        }

        private fun moveTile(pos: XY, dir: XY, neighbours: Boolean = true) {
            if (neighbours) {
                when (map[pos]) {
                    '[' -> moveTile(pos.right, dir, false)
                    ']' -> moveTile(pos.left, dir, false)
                }
            }

            when (map[pos + dir]) {
                '#' -> error("wall")
                '[', ']', 'O' -> moveTile(pos + dir, dir)
            }

            map[pos + dir] = map[pos]
            map[pos] = '.'
        }
    }

    override val part1: Any
        get() {
            val (m, moves) = input.lines.partitionBy { it.isEmpty() }
            val map = CharMatrix.fromLines(m)
            applyMoves(map, moves)
            return calculateResult(map, 'O')
        }

    private fun calculateResult(map: CharMatrix, c: Char): Int {
        return map.findAll(c).sumOf {
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

            applyMoves(map, moves)
            return calculateResult(map, '[')
        }

    private fun applyMoves(
        map: CharMatrix,
        moves: List<String>
    ) {
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
    }
}

fun main() {
    Day15.fancyRun()
}
