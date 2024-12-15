package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.CharMatrix
import org.stummi.aoc.helper.XY

object Day12 : AdventOfCode(2024, 12) {
    override val part1: Any
        get() {
            val map = input.matrix
            val allVisited = mutableSetOf<XY>()
            val areas = mutableListOf<Set<XY>>()

            while(allVisited.size < map.bounds.area) {
                val unvisited = map.bounds.asSequence().first { it !in allVisited }
                val area = followArea(map, unvisited)
                allVisited.addAll(area)
                areas.add(area)
            }

            return areas.sumOf {
                it.size * getPerimeter(it)
            }
        }

    private fun getPerimeter(area: Set<XY>): Int {
        return area.sumOf {
            it.orthogonalNeighbours().count { n ->
                n !in area
            }
        }
    }

    private fun followArea(map: CharMatrix, start: XY): Set<XY> {
        val todo = mutableListOf(start)
        val area = mutableSetOf<XY>()
        val char = map[start]

        while(todo.isNotEmpty()) {
            val xy = todo.removeFirst()
            area.add(xy)
            todo.addAll(xy.orthogonalNeighbours().filter {
                it in map.bounds
                        && it !in todo
                        && it !in area
                        && map[it] == char
            })
        }
        return area.toSet()
    }
}

fun main() {
    Day12.fancyRun()
}
