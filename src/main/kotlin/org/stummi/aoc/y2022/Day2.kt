package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode

object Day2 : AdventOfCode(2022, 2) {

    enum class Move(val value: Int) {
        ROCK(1),
        PAPER(2),
        SCISSOR(3),
    }

    val winningMove = mapOf(
        Move.ROCK to Move.PAPER,
        Move.SCISSOR to Move.ROCK,
        Move.PAPER to Move.SCISSOR
    )

    val losingMove = winningMove.map { (k, v) -> v to k }.toMap()

    val moveMappings = mapOf(
        "A" to Move.ROCK,
        "B" to Move.PAPER,
        "C" to Move.SCISSOR,
        "X" to Move.ROCK,
        "Y" to Move.PAPER,
        "Z" to Move.SCISSOR
    )


    override val part1: Any
        get() = moves().sumOf { pointsForMove(it) }

    private fun pointsForMove(rnd: Pair<Move, Move>): Int =
        (when (rnd.first) {
            winningMove[rnd.second] -> 0
            rnd.second -> 3
            else -> 6
        } + rnd.second.value)

    private fun moves(): List<Pair<Move, Move>> = inputLines().map {
        val (a, b) = it.split(" ")
        moveMappings[a]!! to moveMappings[b]!!
    }

    override val part2: Any
        get() = moves2().sumOf { pointsForMove(it) }

    private fun moves2(): List<Pair<Move, Move>> = inputLines().map {
        val (a, b) = it.split(" ")
        val move = moveMappings[a]!!
        move to when (b) {
            "X" -> losingMove[move]!!
            "Y" -> move
            "Z" -> winningMove[move]!!
            else -> throw IllegalArgumentException(b)
        }
    }

}

fun main() {
    Day2.fancyRun()
}
