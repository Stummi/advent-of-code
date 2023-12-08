package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.lcm

object Day8 : AdventOfCode(2023, 8) {
    init {
        resourceSample("demo1", 2, Unit)
        resourceSample("demo2", 6, Unit)
        resourceSample("demo3", Unit, 6)
    }

    private data class Input(
        val lrString: String,
        val mappings: Map<String, Pair<String, String>>
    )

    private fun parseInput(): Input {
        val input = input()
        val lrString = input.first()
        val mappings = input.drop(2).map {
            val (a, b) = it.split(" = ")
            val (l, r) = b.drop(1).dropLast(1).split(", ")
            a to (l to r)
        }.toMap()
        return Input(lrString, mappings)
    }

    override val part1: Any
        get() {
            val (lrString, mappings) = parseInput()
            var current = "AAA"
            var lrIdx = 0
            while (current != "ZZZ") {
                val lr = lrString[lrIdx % lrString.length]
                current = mappings[current]!!.let { (l, r) ->
                    if (lr == 'L') {
                        l
                    } else {
                        r
                    }
                }
                ++lrIdx
            }

            return lrIdx
        }

    override val part2: Any
        get() {
            val (lrString, mappings) = parseInput()
            var startPositions = mappings.keys.filter { it.endsWith("A") }

            val times = startPositions.map {
                var current = it
                var lrIdx = 0
                while (!current.endsWith("Z")) {
                    var lr = lrString[lrIdx % lrString.length]
                    current = mappings[current]!!.let { (l, r) ->
                        if (lr == 'L') {
                            l
                        } else {
                            r
                        }
                    }
                    ++lrIdx
                }
                lrIdx
            }

            return times.map { it.toLong() }.lcm()
        }
}

fun main() {
    Day8.fancyRun()
}
