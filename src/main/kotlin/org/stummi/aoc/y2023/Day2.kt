package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode

object Day2 : AdventOfCode(2023, 2) {
    override val part1: Any
        get() = parsedInput().filter { (_, drafts) ->
            drafts.all { isPossibleDraft(it) }
        }.sumOf { (id, _) -> id }

    override val part2: Any
        get() = parsedInput().map { (_, drafts) ->
            val minPossibleValues = mutableMapOf<String, Int>()
            drafts.forEach { draft ->
                draft.forEach { (amount, color) ->
                    minPossibleValues.compute(color) { _, it -> (it ?: 0).coerceAtLeast(amount) }
                }
            }

            minPossibleValues.values.reduce(Int::times)
        }.sum()

    private fun parsedInput() = inputLines().map {
        val (name, descr) = it.split(": ")
        val id = name.split(" ")[1].toInt()
        val drafts = parseDrafts(descr)

        id to drafts
    }

    private fun isPossibleDraft(pairs: List<Pair<Int, String>>): Boolean {
        val maxNumbers = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14,
        )

        return pairs.all { (amount, color) ->
            val max = maxNumbers[color] ?: error("Unknown color $color")
            amount <= max
        }.also {
            //println("isPossibleDraft($pairs) = $it")
        }
    }

    private fun parseDrafts(descr: String): List<List<Pair<Int, String>>> {
        return descr.split("; ").map {
            it.split(", ").map {
                val (amount, color) = it.split(" ")
                amount.toInt() to color
            }
        }

    }
}

fun main() {
    Day2.fancyRun()
}
