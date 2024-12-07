package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode

object Day7 : AdventOfCode(2024, 7) {

    init {
        resourceSample(result1 = 3749.toBigInteger(), result2 = 11387.toBigInteger())
    }

    private data class Equation(
        val result: Long,
        val numbers: List<Long>
    ) {

        fun canSolve(part2: Boolean): Boolean {
            return checkSolve(numbers[0], numbers.drop(1), part2)
        }

        private fun checkSolve(current: Long, numbers: List<Long>, part2: Boolean): Boolean {
            if(numbers.isEmpty()) {
                return current == result
            }

            if(current > result) {
                return false
            }

            val subList = numbers.drop(1)
            if (checkSolve(current + numbers[0], subList, part2)
                ||  checkSolve(current * numbers[0], subList, part2)) {
                return true
            }

            if(part2 &&
                checkSolve("$current${numbers[0]}".toLong(), subList, part2)
            ) {
                return true
            }

            return false
        }
    }

    override val part1: Any
        get() = parsedInput.filter { it.canSolve(false) } .sumOf { it.result }

    override val part2: Any
        get() = parsedInput.filter { it.canSolve(true) } .sumOf { it.result }

    private val parsedInput by lazy {
        val r = "\\d+".toRegex()
        input.lines.map {
            r.findAll(it).map { it.value.toLong() }.toList().let { l ->
                Equation(l[0], l.drop(1))
            }
        }
    }
}

fun main() {
    Day7.fancyRun()
}
