package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode

object Day10 : AdventOfCode(2022, 10) {
    init {
        resourceSample("demo", 13140)
    }

    override val part1: Any
        get() {
            val cycleValues = execute()
            return (20..220 step 40).map { cycle ->
                cycle * cycleValues[cycle]
            }.sum()
        }

    override val part2: Any
        get() {
            val values = execute()
            return (0..5).joinToString("\n") { r ->
                (0 until 40).joinToString(" ") { c ->
                    val cycle = (r * 40 + c) + 1
                    val xvalue = values[cycle]
                    //print("pos: $r,$c - Cycle: $cycle - value: ${xvalue}: ")

                    if (xvalue in (c - 1..c + 1)) {
                        "#"
                    } else {
                        "."
                    }
                    //println()
                }
            }
        }

    private fun execute(): MutableList<Int> {
        val cycleValues = mutableListOf<Int>(1)
        var register = 1
        inputLines().map { it.split(" ") }.forEach {
            when (it.first()) {
                "noop" -> {
                    cycleValues.add(register)
                }

                "addx" -> {
                    cycleValues.add(register)
                    cycleValues.add(register)
                    register += it[1].toInt()
                }

                else ->
                    throw IllegalArgumentException(it.toString())
            }
        }
        return cycleValues
    }
}

fun main() {
    Day10.fancyRun()
}
