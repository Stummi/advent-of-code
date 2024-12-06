package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode
import kotlin.streams.toList

object Day10 : AdventOfCode(2017, 10) {
    init {
        //rawSample("3,4,1,5", 12, Unit, 4)
        rawSample("", Unit, "a2582a3a0e66e6e86e3812dcb672a272")
        rawSample("AoC 2017", Unit, "33efeb34ea91902bb2f59c9920caa6cd")
        rawSample("1,2,3", Unit, "3efbe78a8d82f29979031a4aa0b16a9d")
        rawSample("1,2,4", Unit, "63960835bcdc130f0b66d7ff4f6a5a8e")
    }

    fun doSwaps(knot: List<Int>, swaps: List<Int>, repeat: Int = 1): List<Int> {
        val tmpList = knot.toMutableList()
        var currentPos = 0
        var skipSize = 0
        val size = knot.size
        repeat(repeat) { _ ->
            swaps.forEach { il ->
                repeat(il / 2) { sp ->
                    val idx1 = (currentPos + sp) % size
                    val idx2 = (currentPos + il - sp - 1) % size
                    val tmp = tmpList[idx1]
                    tmpList[idx1] = tmpList[idx2]
                    tmpList[idx2] = tmp
                }
                currentPos += il + skipSize
                skipSize++
            }
        }
        return tmpList.toList()
    }

    override val part1: Any
        get() {
            val knot = (0..255).toMutableList()
            val swaps = input.lines.first().split(",").map { it.toInt() }
            return doSwaps(knot, swaps).let { (a, b) -> a * b }
        }

    override val part2: Any
        get() {
            return knotHash(input.line)
        }

    internal fun knotHash(input: String): String {
        val knot = (0..255).toMutableList()
        val swaps = input.chars().toList() + listOf(17, 31, 73, 47, 23)
        val swapped = doSwaps(knot, swaps, 64)
        return swapped.chunked(16).map { it.reduce(Int::xor) }.joinToString("") {
            String.format("%02x", it)
        }
    }
}

fun main() {
    Day10.fancyRun()
}
