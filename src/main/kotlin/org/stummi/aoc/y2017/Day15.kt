package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode

object Day15 : AdventOfCode(2017, 15) {

    init {
        rawSample("... 65\n... 8921")
    }

    val parsedInput by lazy {
        input.lines.map {
            it.split(" ").last().toInt()
        }.let { it[0] to it[1] }
    }

    val GEN_A_FACTOR = 16807
    val GEN_B_FACTOR = 48271

    private fun generator(seed: Int, factor: Int) =
        generateSequence(seed) {
            (it.toLong() * factor % Integer.MAX_VALUE).toInt()
        }.drop(1)


    override val part1: Any
        get() {
            val (seedA, seedB) = parsedInput
            val genA = generator(seedA, GEN_A_FACTOR)
            val genB = generator(seedB, GEN_B_FACTOR)
            val seq = genA.zip(genB)
            return seq.take(40_000_000).count { (a, b) ->
                a and 0xFFFF == b and 0xFFFF
            }
        }

    override val part2: Any
        get() {
            val (seedA, seedB) = parsedInput
            val genA = generator(seedA, GEN_A_FACTOR).filter { it % 4 == 0 }
            val genB = generator(seedB, GEN_B_FACTOR).filter { it % 8 == 0 }
            val seq = genA.zip(genB)
            return seq.take(5_000_000).count { (a, b) ->
                a and 0xFFFF == b and 0xFFFF
            }
        }
}

fun main() {
    Day15.fancyRun()
}
