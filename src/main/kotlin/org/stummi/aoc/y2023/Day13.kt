package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.CharMatrix
import org.stummi.aoc.helper.partitionBy

object Day13 : AdventOfCode(2023, 13) {
    private val parsedInput by lazy {
        input.lines.partitionBy { it.isEmpty() }.map {
            CharMatrix.fromLines(it)
        }
    }

    private fun findReflection(
        l: List<List<Char>>,
        checker: (List<Pair<List<Char>, List<Char>>>) -> Boolean = { it.all { (a, b) -> a == b } }
    ): Int? {

        (1..<l.size).forEach { splitCandidate ->
            val l1 = l.subList(0, splitCandidate).reversed()
            val l2 = l.subList(splitCandidate, l.size)

            if (checker(l1.zip(l2))) {
                return splitCandidate
            }
        }
        return null
    }

    private fun part2Check(pairs: List<Pair<List<Char>, List<Char>>>): Boolean {
        return pairs.sumOf { (a, b) ->
            (0..<a.size).count {
                a[it] != b[it]
            }
        } == 1

    }

    override val part1: Any
        get() {
            return parsedInput.sumOf {
                val hRef = findReflection(it.rows().toList())
                val vRef = findReflection(it.cols().toList())
                (vRef ?: 0) + (hRef ?: 0) * 100
            }
        }

    override val part2: Any
        get() {
            return parsedInput.sumOf {
                val hRef = findReflection(it.rows().toList(), ::part2Check)
                val vRef = findReflection(it.cols().toList(), ::part2Check)
                (vRef ?: 0) + (hRef ?: 0) * 100
            }
        }
}

fun main() {
    Day13.fancyRun()
}
