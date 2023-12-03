package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.astar

object Day13 : AdventOfCode(2016, 13) {
    override val part1: Any
        get() {
            val fav = input().first().toInt()
            val goal = if (sample) XY(7, 4) else XY(31, 39)
            val path = astar(
                XY(1, 1),
                {
                    it.orthogonalNeighbours().filter {
                        !isWall(it.x, it.y, fav)
                    }.map { it to 1 }
                },
                { it == goal },
                { it.orthogonalDistanceTo(goal) }
            )

//            val maxX = path.maxOf { it.first.x }
//            val maxY = path.maxOf { it.first.y }
//            printLabyrinth(maxX, maxY, path.map { it.first }, fav)

            return path.size
        }

    private fun printLabyrinth(
        maxY: Int,
        maxX: Int,
        path: Collection<XY>,
        fav: Int
    ) {
        println()
        (0..maxY + 1).forEach { y ->
            (0..maxX + 1).forEach { x ->
                if (path.any { it == XY(x, y) }) {
                    print("O")
                } else if (isWall(x, y, fav)) {
                    print("#")
                } else {
                    print(".")
                }
                print(" ")
            }
            println()
        }
    }

    override val part2: Any
        get() {
            val fav = input().first().toInt()
            val currentLocations = mutableSetOf(XY(1, 1))
            val allLocations = mutableSetOf(XY(1, 1))
            repeat(50) {
                val nextLocations = currentLocations.flatMap {
                    it.orthogonalNeighbours()
                }.filter {
                    !isWall(it.x, it.y, fav)
                            && it !in allLocations
                }
                currentLocations.clear()
                currentLocations.addAll(nextLocations)
                allLocations.addAll(nextLocations)
            }
            //printLabyrinth(30, 30, allLocations, fav)
            return allLocations.size
        }

    fun isWall(x: Int, y: Int, favNumber: Int) =
        x < 0 || y < 0 || ((x * x + 3 * x + 2 * x * y + y + y * y) + favNumber).countOneBits() % 2 == 1

}


fun main() {
    Day13.fancyRun()
}
