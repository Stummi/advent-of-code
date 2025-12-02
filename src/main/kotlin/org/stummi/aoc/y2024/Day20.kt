package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.astar

object Day20 : AdventOfCode(2024, 20) {
    override val part1: Any
        get() {
            return solve(2)
        }

    override val part2: Any
        get() {
            return solve(20)
        }

    private fun solve(maxCheat: Int): Int {
        val d = XY(maxCheat, maxCheat)
        val radius = ((XY.ZERO - d)..(XY.ZERO + d)).asSequence().filter {
            it != XY.ZERO && it.orthogonalDistanceTo(XY.ZERO) <= maxCheat
        }.toList()

        val map = input.matrix
        val start = map.find('S')
        val end = map.find('E')

        val path = astar(
            initialState = start,
            nextStates = {
                it.orthogonalNeighbours().filter {
                    map[it] != '#'
                }.map { it to 1 }
            },
            goal = { it == end },
            heuristicCost = { it.orthogonalDistanceTo(end) }
        ).map { it.first }.let { it + start }.reversed()

        val pathSet = path.mapIndexed { index, xy -> xy to index }.toMap()

        return path.sumOf { p ->
            radius.count { r ->
                val pr = p + r
                pr in pathSet && (pathSet[pr]!! - pathSet[p]!!
                        - pr.orthogonalDistanceTo(p)) >= 100

            }
        }
    }
}

fun main() {
    Day20.fancyRun()
}
