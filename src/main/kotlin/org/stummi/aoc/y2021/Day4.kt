package org.stummi.aoc.y2021

import java.io.File

class Board(var matrix: List<List<Int?>>) {
    override fun toString(): String =
        matrix.map {
            it.map {
                String.format("%2s ", it ?: "x")
            }.joinToString("")
        }.joinToString("\n")

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


}


fun main() {

    println("a")
    var input = File("/tmp/input.txt").readLines()

    var ints = input[0].split(",").map { it.toInt() }


    println("lol")

    val boards = input.asSequence().drop(1).chunked(6).map {
        if (it[0].isNotBlank()) {
            throw IllegalArgumentException()
        }

        it.drop(1).map {
            val split = it.trim().split(Regex(" +"))
            split.map { it.toInt() }
        }
    }.map { Board(it) }.toList()


    var lwb = findLastWinningBoard(ints, boards)
    println(lwb);

    val (b, n) = findFirstWonBoard(ints, listOf(lwb))
    println(b.sumOfUnmarkedFields() * n)

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
