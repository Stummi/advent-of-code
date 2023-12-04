package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode

object Day4 : AdventOfCode(2023, 4) {
    override val part1: Any
        get() = parseInput().sumOf { (winning, actual) ->
            val c = actual.count { it in winning }
            if (c == 0) {
                0
            } else {
                1 shl (c - 1)
            }
        }

    override val part2: Any
        get() = parseInput().let { input ->
            val amountOfCards = input.indices.associateWith { 1 }.toMutableMap()
            input.indices.forEach { idx ->
                val (winning, actual) = input[idx]
                val amount = amountOfCards[idx]!!
                val points = actual.count { it in winning }
                (1..points).forEach { off ->
                    amountOfCards.computeIfPresent(idx + off) { k,v -> v + amount }
                }
            }
            return amountOfCards.values.sum()
        }

    fun parseInput() =
        input().map {
            it.split(":")[1]
        }.map {
            it.split(" | ")
        }.map { row ->
            val (winning, actual) = row.map { numbers ->
                numbers.split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
            }
            winning to actual
        }
}

fun main() {
    Day4.fancyRun()
}
