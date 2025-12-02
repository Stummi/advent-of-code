package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.astar
import org.stummi.aoc.helper.splitToInts

object Day18 : AdventOfCode(2024, 18) {
    override val part1: Any
        get() {
            val walls = input.lines.map(String::splitToInts).map { (x, y) -> XY(x, y) }.take(1024)
            val goal = XY(70, 70)
            val bounds = XY.ZERO..goal

            return astar(
                initialState = XY(0, 0),
                nextStates = { xy ->
                    xy.orthogonalNeighbours().filter {
                        it in bounds && it !in walls
                    }.map { it to 1 }
                },
                goal = { it == goal },
                heuristicCost = { it.orthogonalDistanceTo(goal) }
            ).size
        }

    override val part2: Any
        get() {
            val walls = input.lines.map(String::splitToInts).map { (x, y) -> XY(x, y) }

            val goal = XY(70, 70)
            val bounds = XY.ZERO..goal

            var lowerBound = 0
            var upperBound = walls.size

            while(upperBound - lowerBound > 1) {
                val mid = (lowerBound) + (upperBound - lowerBound) / 2

                val slice = walls.take(mid)
                val result = kotlin.runCatching {
                    astar(
                        initialState = XY(0, 0),
                        nextStates = { xy ->
                            xy.orthogonalNeighbours().filter {
                                it in bounds && it !in slice
                            }.map { it to 1 }
                        },
                        goal = { it == goal },
                        heuristicCost = { it.orthogonalDistanceTo(goal) }
                    )
                }

                if(result.isSuccess) {
                    lowerBound = mid
                } else {
                    upperBound = mid
                }
            }
            walls[lowerBound].let { (x,y) ->
                return "$x,$y"
            }
        }
}

fun main() {
    Day18.fancyRun()
}
