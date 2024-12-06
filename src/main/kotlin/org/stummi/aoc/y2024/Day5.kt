package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.allPermutations
import org.stummi.aoc.helper.splitToInts
import org.stummi.aoc.helper.toPair

object Day5 : AdventOfCode(2024, 5) {

    init {
        resourceSample(result1 = 143, result2 = 123)
    }

    val parsedInput by lazy {
        input.lines.let {
            val i = it.indexOf("")
            it.take(i).map {
                it.splitToInts().toPair()
            } to it.drop(i + 1).map { it.splitToInts() }
        }
    }

    override val part1: Any
        get() {
            val (orderRules, pageNumbers) = parsedInput

            return pageNumbers.filter { pgs ->
                isOrdered(orderRules, pgs)
            }.sumOf {
                it[it.size / 2]
            }
        }

    override val part2: Any
        get() {
            val (orderRules, pageNumbers) = parsedInput

            var sum = 0
            pageNumbers.forEach { pg ->
                val rules = orderRules.filter {
                    it.first in pg && it.second in pg
                }

                val ordered = createOrderFromRules(rules)
                if(pg != ordered) {
                    sum += ordered[ordered.size / 2]
                }
            }

            return sum
        }

    private fun createOrderFromRules(orderingRules: List<Pair<Int, Int>>): List<Int> {
        val remainNumbers = orderingRules.flatMap { it.toList() }.distinct().toMutableList()
        val sortedNumbers = mutableListOf<Int>()

        while(remainNumbers.isNotEmpty()) {
            val nextNumber = remainNumbers.find { number ->
                orderingRules.filter { it.second == number }.all {
                    it.first in sortedNumbers
                }
            }!!

            sortedNumbers.add(nextNumber)
            remainNumbers.remove(nextNumber)
        }

        return sortedNumbers.toList()
    }

    private fun isOrdered(
        orderRules: List<Pair<Int, Int>>,
        pgs: List<Int>
    ) = orderRules.all { (a, b) ->
        (a !in pgs || b !in pgs || pgs.indexOf(a) < pgs.indexOf(b))
    }
}

fun main() {
    Day5.fancyRun()
}
