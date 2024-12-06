package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY

object Day4 : AdventOfCode(2024, 4) {
    init {
        resourceSample(result1 = 18, result2 = 9)
    }

    override val part1: Any
        get() {
            val m = input.matrix
            val dirs = XY.ZERO.mooreNeighbours()
            return m.findAll('X').sumOf { xPos ->
                dirs.count { dir ->
                    val seq = generateSequence(xPos) { it + dir }.take(4).toList()
                    seq.all { it in m.bounds } && seq.map { m[it] }.joinToString("") == "XMAS"
                }
            }
        }

    override val part2: Any
        get() {
            val m = input.matrix
            val bounds = input.matrix.bounds
            val innerbounds = bounds.topLeft.downRight..bounds.bottomRight.upLeft
            val msSet = setOf('M', 'S')
            return m.findAll('A').count { aPos ->
                if(aPos !in innerbounds) {
                    return@count false
                }

                setOf(m[aPos.upLeft], m[aPos.downRight]) == msSet
                        && setOf(m[aPos.upRight], m[aPos.downLeft]) == msSet
            }
        }
}

fun main() {
    Day4.fancyRun()
}