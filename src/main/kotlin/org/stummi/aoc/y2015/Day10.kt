package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode

object Day10 : AdventOfCode(2015, 10) {
    override val part1: Any
        get() {
            var inp = StringBuilder(input().first())
            repeat(40) {
                inp = lookAndSay(inp)
            }

            return inp.length
        }

    override val part2: Any
        get() {
            var inp = StringBuilder(input().first())
            repeat(50) {
                inp = lookAndSay(inp)
            }

            return inp.length
        }

    private fun lookAndSay(input: StringBuilder): StringBuilder {
        var currentChar = input[0]
        var currentCount = 1

        val ret = StringBuilder()

        input.drop(1).forEach {
            if (it == currentChar) {
                ++currentCount
            } else {
                ret.append(currentCount).append(currentChar.digitToInt())
                currentChar = it
                currentCount = 1
            }
        }
        ret.append(currentCount).append(currentChar.digitToInt())
        return ret;
    }

}

fun main() {
    Day10.fancyRun()
}



