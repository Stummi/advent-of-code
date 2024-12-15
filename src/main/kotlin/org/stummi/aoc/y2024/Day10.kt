package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.IntMatrix
import org.stummi.aoc.helper.XY

object Day10 : AdventOfCode(2024, 10) {
    init {
        resourceSample(result1 = 36, result2 = 81)
    }

    override val part1
        get(): Any {
            val map = input.matrix.toIntMatrix()
            return map.findAll(0).sumOf {
                findEnds(
                    map,
                    it).size
            }
        }

    override val part2
        get(): Any {
            val map = input.matrix.toIntMatrix()
            return map.findAll(0).sumOf {
                countTrails(
                    map,
                    it)
            }
        }

    private fun countTrails(
        map: IntMatrix,
        pos: XY,
        currentHeight: Int = 0
    ) : Int {
        if(currentHeight == 9) {
            return 1
        }
        return pos.orthogonalNeighbours().filter { it in map.bounds && map[it] == currentHeight + 1 }.sumOf {
            countTrails(
                map,
                it,
                currentHeight + 1
            )
        }
    }

    private fun findEnds(
        map: IntMatrix,
        pos: XY,
        currentHeight: Int = 0
    ) : Set<XY> {
        if(currentHeight == 9) {
            return setOf(pos)
        }
        return pos.orthogonalNeighbours().filter { it in map.bounds && map[it] == currentHeight + 1 }.flatMap {
            findEnds(
                map,
                it,
                currentHeight + 1
            )
        }.toSet()
    }
}

fun main() {
    Day10.fancyRun()
}
