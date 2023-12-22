package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.CharMatrix
import org.stummi.aoc.helper.IntMatrix
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.XYRange

object Day21 : AdventOfCode(2023, 21) {

    init {
        resourceSample("demo", additionalData = 6)
    }

    private val map by lazy { input.matrix }
    override val part1: Any
        get() {
            val startPos = map.find('S')
            return getPossiblePlots(map, startPos, 64.orSample()).count()
        }

    override val part2: Any
        get() {
            TODO()
        }

    private fun floodFillMap(map: CharMatrix, startPosition: XY): IntMatrix {
        val returnValue = IntMatrix(map.bounds, IntArray(map.bounds.area) { -1 })
        returnValue[startPosition] = 0
        val openList = mutableListOf(startPosition)
        while (openList.isNotEmpty()) {
            val pos = openList.removeFirst()
            val cost = returnValue[pos]
            pos.orthogonalNeighbours().forEach {
                if (it in map.bounds && map[it] != '#' && returnValue[it] == -1) {
                    returnValue[it] = cost + 1
                    openList.add(it)
                }
            }
        }
        return returnValue
    }

    private fun getPossiblePlots(
        map: CharMatrix,
        position: XY,
        steps: Int,
        cache: MutableMap<Pair<XY, Int>, Set<XY>> = mutableMapOf()
    ): Set<XY> {
        if (steps == 0) {
            return setOf(position)
        }

        cache[position to steps]?.let { return it }


        val t = translatePoint(position, map.bounds)
        if (t != position) {
            val diff = t - position
            return getPossiblePlots(map, t, steps, cache).asSequence().map {
                it - diff
            }.toSet().also { ret ->
                cache[position to steps] = ret
            }
        }


        val tmp = mutableSetOf<XY>()
        position.orthogonalNeighbours().filterNot { map[translatePoint(it, map.bounds)] == '#' }.map {
            getPossiblePlots(map, it, steps - 1, cache)
        }.forEach { tmp.addAll(it) }

        val ret = tmp.toSet()
        cache[position to steps] = ret
        return ret
    }

    private fun translatePoint(position: XY, bounds: XYRange): XY {
        if (position in bounds) {
            return position
        } else {
            return XY(
                translateIntoRange(position.x, bounds.xRange),
                translateIntoRange(position.y, bounds.yRange)
            )
        }
    }


    fun translateIntoRange(v: Int, r: IntRange): Int {
        check(!r.isEmpty())

        if (v in r) {
            return v
        }

        val rangeLen = r.last - r.first + 1

        if (v > r.last) {
            return r.first + (v - r.first) % rangeLen
        }

        if (v < r.first) {
            return r.last - (r.first - v - 1) % rangeLen
        }

        TODO()
    }
}

fun main() {
    Day21.fancyRun()
}
