package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode

object Day17 : AdventOfCode(2017, 17) {
    private val steps by lazy { input.line.toInt() }

    override val part1: Any
        get() {
            var pos = 0
            val l = mutableListOf(0)

            repeat(2017) { i ->
                pos += 349
                pos %= l.size
                l.add(pos+1, i+1)
                ++pos
            }

            return l[l.indexOf(2017) + 1]
        }

    override val part2: Any
        get() {
            val steps = steps
            //Day17.fancyRun()
            var pos = 0
            //val l = mutableListOf(0)
            var lastItem = 0
            var listLen = 1
            repeat(50000000) { i ->
                pos += steps
                pos %= listLen
                ++listLen
                if(pos == 0) {
                    lastItem = (i+1)
                }
                ++pos
            }

            return lastItem
        }
}

fun main() {
    Day17.fancyRun()
}
