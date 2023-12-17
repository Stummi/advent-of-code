package org.stummi.aoc.y2021

import org.stummi.aoc.AdventOfCode

object Day4 : AdventOfCode(2021, 4) {
    data class Board(
        var matrix: List<List<Int?>>
    ) {
        var winningNumber: Int = -1

        override fun toString(): String =
            matrix.joinToString("\n") {
                it.joinToString("") {
                    String.format("%2s ", it ?: "x")
                }
            }

        fun remove(n: Int) {
            matrix = matrix.map {
                it.map {
                    if (it == n) null else it
                }
            }
        }

        fun hasWon(): Boolean =
            (0..4).any { p1 ->
                (0..4).all { p2 ->
                    matrix[p1][p2] == null
                } || (0..4).all { p2 ->
                    matrix[p2][p1] == null
                }
            }

        fun sumOfUnmarkedFields(): Int =
            matrix.map {
                it.filterNotNull().sum()
            }.sum()

        fun points(): Int = sumOfUnmarkedFields() * winningNumber
    }

    private fun findLastWinningBoard(ints: List<Int>, boards: List<Board>): Board {
        val mutableBards = boards.toMutableList()
        ints.forEach { n ->
            mutableBards.forEach {
                it.remove(n)
            }
            mutableBards.removeIf { it.hasWon() }
            if (mutableBards.size == 1) {
                return mutableBards[0]
            }
        }
        throw IllegalStateException()
    }

    private fun findFirstWonBoard(ints: List<Int>, boards: List<Board>): Pair<Board, Int> {
        ints.forEach { n ->
            boards.forEach {
                it.remove(n)

                if (it.hasWon()) {
                    return it to n
                }
            }
        }
        throw IllegalStateException()
    }

    private data class ParsedInput(
        val ints: List<Int>,
        val boards: List<Board>,
    )

    private val parsedInput by lazy {
        val lines = input.lines
        ParsedInput(
            ints = lines[0].split(",").map { it.toInt() },
            boards = lines.asSequence().drop(1).chunked(6).map {
                if (it[0].isNotBlank()) {
                    throw IllegalArgumentException()
                }

                it.drop(1).map {
                    val split = it.trim().split(Regex(" +"))
                    split.map { it.toInt() }
                }
            }.map { Board(it) }.toList()
        )
    }

    private val solve by lazy {
        var firstWonBoard: Board? = null
        var lastWonBoard: Board? = null
        val boards = parsedInput.boards.toMutableList()

        parsedInput.ints.forEach { n ->
            boards.forEach { it.remove(n) }
            val winBoards = boards.filter { it.hasWon() }.onEach {
                it.winningNumber = n
            }


            if (winBoards.isNotEmpty()) {
                if (firstWonBoard == null) {
                    firstWonBoard = winBoards.first()
                }

                lastWonBoard = winBoards.last()
            }

            boards.removeAll(winBoards)
        }

        firstWonBoard!!.points() to lastWonBoard!!.points()
    }

    override val part1 get() = solve.first
    override val part2 get() = solve.second
}


fun main() {
    Day4.fancyRun()
}
