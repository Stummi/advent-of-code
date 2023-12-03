package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode


object Day11 : AdventOfCode(2015, 11) {

    override val part1: String
        get() {
            val input = input().first()
            val pw = input.toCharArray()

            return generateSequence {
                increasePassword(pw)
                pw
            }.filter { isValidPassword(it) }.first().let {
                String(it)
            }
        }

    override val part2: String
        get() = part1.let {
            val pw = it.toCharArray()
            increasePassword(pw)
            generateSequence {
                increasePassword(pw)
                pw
            }.filter { isValidPassword(it) }.first().let {
                String(it)
            }
        }

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
