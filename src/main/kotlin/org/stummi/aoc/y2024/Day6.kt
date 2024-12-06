package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.CharMatrix
import org.stummi.aoc.helper.XY

object Day6 : AdventOfCode(2024, 6) {

    init {
        resourceSample()
    }

    override val part1: Any
        get() {
            val map = input.matrix
            return walk(map).first.count()
        }

    override val part2: Any
        get() {
            val map = input.matrix
            val path = walk(map).first
            return path.count {
                if (map[it] != '.') {
                    return@count false
                }

                map[it] = '#'
                val (_, r) = walk(map)
                map[it] = '.'
                r
            }
        }


    private fun walk(map: CharMatrix): Pair<Set<XY>, Boolean> {
        var pos = map.find('^')
        var dir = XY.ZERO.up
        val seen = mutableSetOf(
            pos to dir
        )

        var foundLoop = false

        while ((pos + dir) in map.bounds) {
            if (map[pos + dir] == '#')
                dir = dir.rotateRight
            else
                pos += dir

            if (pos to dir in seen) {
                foundLoop = true
                break
            }

            seen.add(pos to dir)
        }

        return seen.map { it.first }.distinct().toSet() to foundLoop
    }
}

fun main() {
    Day6.fancyRun()
}
