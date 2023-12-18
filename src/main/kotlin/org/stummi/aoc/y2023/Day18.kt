package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.BoolMatrix
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.XYRange

object Day18 : AdventOfCode(2023, 18) {
    data class Point(var dir: Char, var length: Int, var color: String) {
        fun parseColor(): Point {
            val length = color.drop(2).take(5).toInt(16)
            val dir = when (color.dropLast(1).last()) {
                '0' -> 'R'
                '1' -> 'D'
                '2' -> 'L'
                '3' -> 'U'
                else -> TODO()
            }
            return Point(dir, length, color)
        }
    }

    data class PointMapping(
        val xMappings: List<IntRange>,
        val yMappings: List<IntRange>,
    ) {
        val mappedBounds = XY.ZERO..<XY(xMappings.size, yMappings.size)

        fun mappedToOrigin(xy: XY): XYRange {
            val xr = xMappings[xy.x]
            val yr = yMappings[xy.y]
            return XY(xr.first, yr.first)..XY(xr.last, yr.last)
        }

        fun exactOriginPointToMapping(xy: XY): XY {
            val xr = xMappings.binarySearch(comparison = binSearch(xy.x))
            val yr = yMappings.binarySearch(comparison = binSearch(xy.y))
            check(xMappings[xr] == xy.x..xy.x)
            check(yMappings[yr] == xy.y..xy.y)
            return XY(xr, yr)
        }

        private fun binSearch(v: Int): (IntRange) -> Int = {
            if (it.first > v) {
                1
            } else if (it.last < v) {
                -1
            } else {
                0
            }
        }
    }

    val parsedInput by lazy {
        input.lines.map {
            val (d, l, c) = it.split(" ")
            Point(d.first(), l.toInt(), c)
        }
    }

    private fun floodFill(map: BoolMatrix, start: XY): Pair<MutableSet<XY>, Boolean> {
        val openList = mutableListOf(start)
        val filled = mutableSetOf<XY>()
        var oob = false
        while (openList.isNotEmpty()) {
            val xy = openList.removeAt(0)
            if (xy !in map.bounds) {
                oob = true
                continue
            }

            if (xy in filled) {
                continue
            }

            if (map[xy]) {
                continue
            }

            filled.add(xy)

            openList.addAll(xy.orthogonalNeighbours())
        }

        return filled to oob
    }

    fun createPointMapping(points: List<XY>): PointMapping {
        val allX = points.map { it.x }.distinct().sorted()
        val xRanges = mapToRanges(allX)

        val allY = points.map { it.y }.distinct().sorted()
        val yRanges = mapToRanges(allY)

        return PointMapping(xRanges, yRanges)
    }

    private fun mapToRanges(
        allX: List<Int>,
    ): List<IntRange> {
        var last = allX.first()
        val mappings = mutableListOf<IntRange>()
        mappings.add(last..last)
        allX.drop(1).forEach { x ->
            if (x - last > 1) {
                mappings.add((last + 1)..<x)
            }
            mappings.add(x..x)
            last = x
        }
        return mappings.toList()
    }

    private fun solveForInput(points: List<XY>): Long {
        val mappings = createPointMapping(points)
        val map = BoolMatrix(mappings.mappedBounds)

        points.windowed(2).forEach { (f, t) ->
            val mf = mappings.exactOriginPointToMapping(f)
            val mt = mappings.exactOriginPointToMapping(t)
            (mf..mt).asSequence().forEach {
                map[it] = true
            }
        }

        val allFilled = mutableSetOf<XY>()
        val allInside: Set<XY>

        while (true) {
            val pos = map.bounds.asSequence().first {
                it !in allFilled && !map[it]
            }

            val (fill, oob) = floodFill(map, pos)
            if (oob) {
                allFilled.addAll(fill)
            } else {
                allInside = fill.toSet()
                break
            }
        }

        return (allInside + map.findAll(true)).sumOf {
            mappings.mappedToOrigin(it).areaAsLong
        }
    }

    private fun inputPoints(part2: Boolean = false) = sequence {
        var current = XY.ZERO
        yield(current)
        parsedInput.let {
            if (part2) {
                it.map { p -> p.parseColor() }
            } else {
                it
            }
        }.forEach {
            current = current.translate(
                when (it.dir) {
                    'R' -> XY(it.length, 0)
                    'D' -> XY(0, it.length)
                    'L' -> XY(-it.length, 0)
                    'U' -> XY(0, -it.length)
                    else -> TODO()
                }
            )
            yield(current)
        }
    }.toList()

    override val part1: Any
        get() {
            val points = inputPoints()
            return solveForInput(points)
        }

    override val part2: Any
        get() {
            val points = inputPoints(true)
            return solveForInput(points)
        }
}

fun main() {
    Day18.fancyRun()
}
