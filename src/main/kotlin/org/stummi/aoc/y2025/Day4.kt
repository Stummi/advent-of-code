package org.stummi.aoc.y2025

import org.stummi.aoc.AdventOfCode

object Day4 : AdventOfCode(2025, 4) {
    override val part1: Any
        get() {
            val map = input.matrix
            return map.bounds.asSequence().count {
                map[it] == '@' && it.mooreNeighbours().count { pos ->
                    pos in map.bounds && map[pos] == '@'
                } < 4
            }
        }

    override val part2: Any
        get() {
            val map = input.matrix

            var removedCount = 0

            do {
                var removedRoll = false
                map.bounds.asSequence().forEach {
                    if (map[it] == '@' && it.mooreNeighbours().count { pos ->
                            pos in map.bounds && map[pos] == '@'
                        } < 4) {
                        map[it] = '.'
                        removedRoll = true
                        ++removedCount
                    }
                }
            } while (removedRoll)

            map.print()
            return removedCount
        }

}

fun main() {
    Day4.fancyRun()
}