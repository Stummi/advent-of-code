package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY

object Day19 : AdventOfCode(2017, 19) {
    override val part1: Any
        get() = solve.first

    override val part2: Any
        get() = solve.second

    private val solve by lazy {
        val map = input.matrix

        var pos = map.find('|')
        var dir = XY.ZERO.down

        var steps = 0
        var str = ""
        while (true) {
            val char = map[pos]
            when {
                char.isLetter() -> str += map[pos]
                char == '+' -> {
                    val next = (pos.orthogonalNeighbours() - (pos - dir)).find { it in map.bounds && map[it] != ' ' }!!
                    dir = next - pos
                    pos = next
                    ++steps
                    continue
                }

                char == ' ' -> break
            }
            pos += dir
            ++steps
        }

        str to steps
    }
}

fun main() {
    Day19.fancyRun()
}
