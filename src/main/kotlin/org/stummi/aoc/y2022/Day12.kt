package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.astar

object Day12 : AdventOfCode(2022, 12) {
    override val part1
        get(): Int {
            val input = input();
            val height = input.size
            val width = input[0].length
            val bounds = XY.ZERO until XY(width, height)
            val goal = bounds.asSequence().find { (x, y) -> input[y][x] == 'E' }!!
            val start = bounds.asSequence().find { (x, y) -> input[y][x] == 'S' }!!
            val map = input().map { it.replace("S", "a").replace("E", "z") }

            val path = astar(
                start,
                { xy ->
                    xy.orthogonalNeighbours()
                        .filter { it in bounds }
                        .filter { canPass(map[xy.y][xy.x], map[it.y][it.x]) }
                        .map { it to 1 }
                },
                { it == goal },
                { it.orthogonalDistanceTo(goal) }
            )
            return path.size
        }

    override val part2
        get(): Int {
            val input = input();
            val height = input.size
            val width = input[0].length
            val bounds = XY.ZERO until XY(width, height)
            val goal = bounds.asSequence().find { (x, y) -> input[y][x] == 'E' }!!
            val map = input().map { it.replace("S", "a").replace("E", "z") }
            val possibleStarts = bounds.asSequence().filter { map[it.y][it.x] == 'a' }.toList()

            return possibleStarts.map {
                kotlin.runCatching {
                    astar(
                        it,
                        { xy ->
                            xy.orthogonalNeighbours()
                                .filter { it in bounds }
                                .filter { canPass(map[xy.y][xy.x], map[it.y][it.x]) }
                                .map { it to 1 }
                        },
                        { it == goal },
                        { it.orthogonalDistanceTo(goal) }
                    ).size
                }.getOrNull()
            }.filterNotNull().minOrNull()!!
        }

    private fun canPass(from: Char, to: Char): Boolean {
        return to <= from + 1
    }
}

fun main() {
    Day12.fancyRun()
}
