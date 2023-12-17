package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.XYRange
import org.stummi.aoc.helper.astar

object Day17 : AdventOfCode(2023, 17) {
    val directions = XY.ZERO.orthogonalNeighbours().toList()


    init {
        resourceSample("demo2")
    }

    data class State(
        val position: XY,
        val lastDirection: XY,
        val lastDirectionCount: Int
    ) {
        fun possibleTransitions(bounds: XYRange) = directions.asSequence().mapNotNull { dir ->
            val newPos = position.translate(dir)

            if (dir.translate(lastDirection) == XY.ZERO) {
                return@mapNotNull null
            }

            if (newPos !in bounds) {
                return@mapNotNull null
            }

            if (dir == lastDirection && lastDirectionCount >= 3) {
                return@mapNotNull null
            }

            State(
                newPos,
                dir,
                if (dir == lastDirection) {
                    lastDirectionCount + 1
                } else {
                    1
                }
            )
        }

        fun possibleTransitions2(bounds: XYRange) = directions.asSequence().mapNotNull { dir ->
            val newPos = position.translate(dir)

            if (dir.translate(lastDirection) == XY.ZERO) {
                return@mapNotNull null
            }

            if (newPos !in bounds) {
                return@mapNotNull null
            }


            if (lastDirection != XY.ZERO && (dir != lastDirection && lastDirectionCount < 4)) {
                return@mapNotNull null
            }


            if (dir == lastDirection && lastDirectionCount >= 10) {
                return@mapNotNull null
            }

            State(
                newPos,
                dir,
                if (dir == lastDirection) {
                    lastDirectionCount + 1
                } else {
                    1
                }
            )
        }
    }

    override val part1: Any
        get() {
            val map = input.matrix.toIntMatrix()
            val initialState = State(
                XY.ZERO,
                XY.ZERO,
                0
            )

            val path = astar(
                initialState,
                { it.possibleTransitions(map.bounds).map { it to map[it.position] } },
                { it.position == map.bounds.bottomRight },
                { it.position.orthogonalDistanceTo(map.bounds.bottomRight) }
            )

            return path.sumOf { it.second }
        }

    override val part2: Any
        get() {
            val map = input.matrix.toIntMatrix()
            val initialState = State(
                XY.ZERO,
                XY.ZERO,
                0
            )

            val path = astar(
                initialState,
                { it.possibleTransitions2(map.bounds).map { it to map[it.position] } },
                { it.position == map.bounds.bottomRight },
                { 0 }
            )

            return path.sumOf { it.second }
        }
}

fun main() {
    Day17.fancyRun(includeReal = false)
}
