package org.stummi.aoc.y2015

fun main() {

    var board = Unit.javaClass.getResourceAsStream("/2015/18.txt").use {
        it!!.bufferedReader().readLines()
    }.map {
        it.map { c ->
            when (c) {
                '#' -> true
                '.' -> false
                else -> throw IllegalArgumentException()
            }
        }
    }

    val w = board[0].size
    val h = board.size

    board = board.mapIndexed { y, row ->
        row.mapIndexed { x, b ->
            ((x == 0 || x == w - 1) && (y == 0 || y == h - 1)) || b
        }
    }

    println("=========".repeat(10))
    printGolBoard(board)
    println("=========".repeat(10))



    repeat(100) {
        board = golStep(board)
    }
    println("------")
    printGolBoard(board)
    println(board.map { it.count { it } }.sum())
}


fun golStep(board: List<List<Boolean>>): List<List<Boolean>> {
    val onRules = listOf(2, 3)
    val offRules = listOf(3)
    val h = board.size
    val w = board[0].size
    return (0 until h).map { y ->
        (0 until w).map { x ->
            if ((x == 0 || x == w - 1) && (y == 0 || y == h - 1)) {
                true
            } else {
                val cnt = countNeighbors(board, x, y)
                (board[y][x] && cnt in onRules) || (!board[y][x] && cnt in offRules)
            }
        }
    }
}

fun countNeighbors(board: List<List<Boolean>>, x: Int, y: Int): Int {
    val fy = (y - 1).coerceAtLeast(0)
    val ty = (y + 1).coerceAtMost(board.size - 1)
    val fx = (x - 1).coerceAtLeast(0)
    val tx = (x + 1).coerceAtMost(board[y].size - 1)


    return (fy..ty).flatMap { yp -> (fx..tx).map { it to yp } }.filterNot { it == x to y }
        .count { (x, y) -> board[y][x] }
}

fun printGolBoard2(board: List<List<Int>>) {
    board.forEach {
        it.forEach {
            print(it)
        }
        println()
    }
}

fun printGolBoard(board: List<List<Boolean>>) {
    board.forEach {
        it.forEach {
            print(if (it) '#' else '.')
        }
        println()
    }
}


