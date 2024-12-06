package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode

object Day13 : AdventOfCode(2017, 13) {
    override val part1: Any
        get() {
            val firewall = parsedInput

            return firewall.filter { (depth, range) ->
                val cycle = (range - 1) * 2
                depth % cycle == 0
            }.sumOf { (depth, range) ->
                depth * range
            }
        }

    val parsedInput by lazy {
        input.lines.map {
            it.split(": ").map { it.toInt() }.let { (a, b) -> a to b }
        }
    }

    override val part2: Any
        get() {
            val firewall = parsedInput
            // kinda cheating, there is probably an easier way
            return (0..Int.MAX_VALUE).first { delay ->
                firewall.none { (depth, range) ->
                    val cycle = (range - 1) * 2
                    (depth + delay) % cycle == 0
                }
            }
        }
}

fun main() {
    Day13.fancyRun()
}
