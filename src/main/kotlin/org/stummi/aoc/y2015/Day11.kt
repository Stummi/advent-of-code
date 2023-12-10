package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode


object Day11 : AdventOfCode(2015, 11) {

    val solve by lazy {
        val input = inputLines().first()
        val pw = input.toCharArray()
        val (p1, p2) = generateSequence {
            increasePassword(pw)
            pw
        }.filter { isValidPassword(it) }.take(2).map {
            String(it)
        }.toList()
        p1 to p2
    }

    override val part1: String
        get() = solve.first

    override val part2: String
        get() = solve.second

    private fun isValidPassword(pw: CharArray): Boolean {
        if (!((0..pw.size - 3).any {
                pw[it + 1] == pw[it] + 1 && pw[it + 2] == pw[it] + 2
            } && listOf('i', 'o', 'l').none { it in pw })) {
            return false
        }

        val firstDouble = (0 until pw.size - 1).find {
            pw[it] == pw[it + 1]
        } ?: return false

        val secondDouble = (firstDouble + 2 until pw.size - 1).find {
            pw[it] == pw[it + 1]
        }

        return secondDouble != null
    }

    private fun increasePassword(pw: CharArray) {
        var incPos = pw.size - 1

        while (true) {
            if (pw[incPos] == 'z') {
                pw[incPos] = 'a'
                --incPos
            } else {
                pw[incPos]++
                return
            }
        }
    }

}

fun main() {
    Day11.fancyRun()
}
