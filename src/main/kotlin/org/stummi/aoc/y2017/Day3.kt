package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode

object Day3 : AdventOfCode(2017, 3) {
    override val part1: Any
        get() {

            var diagValue = 1
            (0..1000).asSequence().map {
                it to diagValue.also { diagValue += 8 * it }
            }.take(20).forEach {
                println(it)
            }


            return 0
        }
}

fun main() {
    Day3.fancyRun()
}
