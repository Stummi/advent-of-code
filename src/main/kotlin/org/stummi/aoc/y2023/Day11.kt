package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.CharMatrix
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.tuplePermutations

object Day11 : AdventOfCode(2023, 11) {

    init {
        resourceSample("demo", 374L, 1030L, 10L)
    }

    private class Space(
        val map: CharMatrix
    ) {
        val emptyRows: List<Int> = map.bounds.yRange.filter { r ->
            map.row(r).all { it == '.' }
        }

        val emptyCols: List<Int> = map.bounds.xRange.filter { c ->
            map.col(c).all { it == '.' }
        }

        val galaxies = map.findAll('#')

        fun distance(from: XY, to: XY, expansion: Long): Long {
            val range = from..to
            val xLength = range.width - 1L
            val yLength = range.height - 1L
            val emptyRowCount = range.yRange.count { it in emptyRows }
            val emptyColCount = range.xRange.count { it in emptyCols }
            return xLength + yLength + emptyRowCount * (expansion - 1) + emptyColCount * (expansion - 1)
        }

        fun print() {
            println(galaxies.toList())
            map.bounds.printAsMap { xy ->
                val emptyRow = xy.y in emptyRows
                val emptyCol = xy.x in emptyCols
                when {
                    emptyRow && emptyCol -> '+'
                    emptyRow -> '-'
                    emptyCol -> '|'
                    else -> map[xy]
                }
            }
        }

    }

    private val parsedInput by lazy { Space(input.matrix) }
    override val part1: Any
        get() {
            val inp = parsedInput
            val galaxies = inp.galaxies.toList()
            return galaxies.tuplePermutations(false).sumOf { (from, to) ->
                inp.distance(from, to, 2)
            }
        }

    override val part2: Any
        get() {
            val inp = parsedInput
            val galaxies = inp.galaxies.toList()
            return galaxies.tuplePermutations(false).sumOf { (from, to) ->
                inp.distance(from, to, 1000000L.orSample())
            }
        }
}

fun main() {
    Day11.fancyRun()
}
