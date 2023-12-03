package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day16 : AdventOfCode(2016, 16) {
    override val part1: String
        get() {
            val length = if (sample) 20 else 272
            val s = input().first()
            return calculateSum(s, length)
        }

    override val part2: Any
        get() {
            val length = if (sample) 20 else 35651584
            val s = input().first()
            return calculateSum(s, length)
        }

    private fun calculateSum(s: String, length: Int): String {
        var s1 = s
        while (s1.length < length) {
            s1 = extend(s1)
        }
        return checksum(s1.substring(0, length))
    }

    private fun checksum(substring: String): String {
        var ret = substring;
        while (ret.length % 2 == 0) {
            ret = ret.chunked(2).map {
                if (it[0] == it[1]) {
                    "1"
                } else {
                    "0"
                }
            }.joinToString("")
        }
        return ret;
    }


    fun extend(s: String): String {
        var s1 = s
        val a = s1
        val b = s1.reversed().map {
            when (it) {
                '1' -> '0'
                '0' -> '1'
                else -> throw IllegalStateException("$it")
            }
        }.joinToString("")
        s1 = "${a}0$b"
        return s1
    }

}

fun main() {
    Day16.fancyRun()
}
