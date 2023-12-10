package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode

object Day25 : AdventOfCode(2015, 25) {
    override val part1: Long
        get() {
            val (row, col) = inputLines().first().split(" ").let {
                it[16].dropLast(1).toInt() to it[18].dropLast(1).toInt()
            }
            val idx = cellLocation(row, col)

            var number = 20151125L
            repeat(idx - 1) {
                number *= 252533
                number %= 33554393
            }
            return number
        }

    override val part2: String
        get() = "⭐"
}

fun main() {
    Day25.fancyRun()
    return
    print("    ||")
    (1..10).forEach { col ->
        print(String.format("%4s|", col))
    }
    println();
    println("----++-------------------------------------------------------")
    (1..40).forEach { row ->
        print(String.format("%3s ||", row))
        (1..40).forEach { col ->
            print(String.format("%4s|", cellLocation(row, col)))
        }

        println()
    }

    val idx = cellLocation(2978, 3083)

    var number = 20151125L
    repeat(idx - 1) {
        number *= 252533
        number %= 33554393
    }
    println(number)

}

fun cellLocation(row: Int, col: Int): Int {
    val start = (1 until row).sum() + 1

    return start + (1 until col).map { it + row }.sum()
}
