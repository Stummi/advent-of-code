package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.swap

object Day19 : AdventOfCode(2015, 19) {
    private data class Input(
        val replacements: List<Pair<String, String>>,
        val formula: String
    )

    private fun parsedInput(): Input {
        val lines = input()
        val replacements = lines.dropLast(2)
            .map { it.split(" => ") }
            .map { it[0] to it[1] }
            .toList()

        val formula = lines.last()

        return Input(replacements, formula)
    }

    override val part1: Any
        get() {
            val input = parsedInput()
            return getAllTransitions(input.formula, input.replacements).size
        }

    override val part2: Any
        get() {
            val input = parsedInput()
            val formula = input.formula
            val reductions = input.replacements.map { it.swap() }.sortedBy { it.first.length }

            return generateSequence { shuffleReduction(formula, reductions) }.dropWhile { it == -1 }.first()
        }

    // I am not proud of this, but it does work surprisingly well
    private fun shuffleReduction(
        formula: String,
        reductions: List<Pair<String, String>>,
    ): Int {
        var current = formula
        var steps = 0
        while (current != "e") {
            val old = current
            for (reduction in reductions.shuffled()) {
                if (current.contains(reduction.first)) {
                    current = current.replaceFirst(reduction.first, reduction.second)
                    ++steps
                    break
                }
            }
            if (old == current) {
                return -1
            }
        }
        return steps
    }

    private fun getAllTransitions(input: String, replacements: List<Pair<String, String>>): Set<String> {
        val ret = mutableSetOf<String>()
        replacements.forEach { (from, to) ->
            from.toRegex().findAll(input).forEach {
                ret.add(input.replaceRange(it.range, to))
            }
        }
        return ret
    }
}

fun main() {
    Day19.fancyRun()
}
