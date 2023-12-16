package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.BoolMatrix
import org.stummi.aoc.helper.CharMatrix
import org.stummi.aoc.helper.XY

object Day16 : AdventOfCode(2023, 16) {

    enum class Direction(val func: (XY) -> XY) {
        UP(XY::up),
        LEFT(XY::left),
        DOWN(XY::down),
        RIGHT(XY::right),
    }

    val transformations = mapOf<Char, (Direction) -> List<Direction>>(
        '|' to {
            if (it in listOf(Direction.UP, Direction.DOWN)) {
                listOf(it)
            } else {
                listOf(Direction.UP, Direction.DOWN)
            }
        },
        '-' to {
            if (it in listOf(Direction.LEFT, Direction.RIGHT)) {
                listOf(it)
            } else {
                listOf(Direction.LEFT, Direction.RIGHT)
            }
        },
        '/' to {
            listOf(
                when (it) {
                    Direction.LEFT -> Direction.DOWN
                    Direction.DOWN -> Direction.LEFT
                    Direction.RIGHT -> Direction.UP
                    Direction.UP -> Direction.RIGHT
                }
            )
        },
        '\\' to {
            listOf(
                when (it) {
                    Direction.LEFT -> Direction.UP
                    Direction.DOWN -> Direction.RIGHT
                    Direction.RIGHT -> Direction.DOWN
                    Direction.UP -> Direction.LEFT
                }
            )
        }
    )

    private fun simulate(map: CharMatrix, startPos: XY = XY.ZERO, startDirection: Direction = Direction.RIGHT): Int {
        val rayMap = BoolMatrix(map.bounds)

        val openRays = mutableListOf(
            startPos to startDirection
        )

        val closedRays = mutableSetOf<Pair<XY, Direction>>()
        while (openRays.isNotEmpty()) {
            val ray = openRays.removeFirst()
            var (pos, direction) = ray
            closedRays.add(ray)

            while (true) {
                rayMap[pos] = true
                if (map[pos] in transformations) {
                    transformations[map[pos]]!!(direction).forEach {
                        val newRay = it.func(pos) to it
                        if (newRay !in closedRays && newRay.first in map.bounds) {
                            openRays.add(newRay)
                        }
                    }
                    break
                }

                pos = direction.func(pos)

                if (pos !in map.bounds) {
                    break
                }

            }
        }

        /*
            map.bounds.printAsMap {
                if (rayMap[it]) {
                    '#'
                } else {
                    map[it]
                }
            }

             */
        return rayMap.values.cardinality()
    }


    override val part1: Any
        get() {
            val map = input.matrix
            return simulate(map, XY.ZERO, Direction.RIGHT)
        }


    override val part2: Any
        get() {
            val map = input.matrix
            return sequence {
                yield(map.bounds.topLeft to Direction.DOWN)
                yield(map.bounds.topLeft to Direction.RIGHT)

                yield(map.bounds.bottomLeft to Direction.UP)
                yield(map.bounds.bottomLeft to Direction.RIGHT)

                yield(map.bounds.topRight to Direction.DOWN)
                yield(map.bounds.topRight to Direction.LEFT)

                yield(map.bounds.bottomLeft to Direction.UP)
                yield(map.bounds.bottomRight to Direction.LEFT)

                (1..<map.bounds.right).forEach {
                    yield(XY(it, 0) to Direction.DOWN)
                    yield(XY(it, map.bounds.bottom) to Direction.UP)
                }

                (1..<map.bounds.bottom).forEach {
                    yield(XY(0, it) to Direction.RIGHT)
                    yield(XY(map.bounds.right, it) to Direction.LEFT)
                }
            }.maxOf { (pos, dir) ->
                simulate(map, pos, dir)
            }
        }

}

fun main() {
    Day16.fancyRun()
}
