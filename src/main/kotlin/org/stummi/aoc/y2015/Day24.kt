package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode
import java.math.BigInteger

object Day24 : AdventOfCode(2015, 24) {
    private fun solveForGroups(groups: Int): BigInteger {
        val weights = Day24.inputLines().map {
            it.toInt()
        }

        val weightSum = weights.sum()
        val search = weightSum / groups

        return findPossibleCombinations(search, weights.sortedDescending())
            .groupBy { it.count() }
            .minBy { it.key }.value
            .minOf {
                it.fold(BigInteger.ONE) { a, b -> a.times(b.toBigInteger()) }
            }
    }

    private fun findPossibleCombinations(search: Int, weights: List<Int>): Sequence<Set<Int>> {
        if (search == 0) {
            return sequenceOf(emptySet())
        }

        if (search < 0) {
            return emptySequence()
        }

        return weights.asSequence().flatMapIndexed { widx, w ->
            findPossibleCombinations(search - w, weights.drop(widx + 1)).map {
                it + w
            }
        }
    }

    override val part1: BigInteger
        get() = solveForGroups(3)
    override val part2: BigInteger
        get() = solveForGroups(4)

}

fun main() {
    Day24.fancyRun()
}


