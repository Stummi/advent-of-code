package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day19 : AdventOfCode(2016, 19) {
    init {
        rawSample("5", 3, 2)
    }

    override val part1
        get() = input().first().let { inp ->
            var l = (1..inp.toInt()).toList()
            while (l.size > 1) {
                val isOdd = l.size % 2 == 1
                var s = l.asSequence().chunked(2) {
                    it.first()
                }
                if (isOdd) {
                    s = s.drop(1)
                }
                l = s.toList()
            }
            l[0]
        }

    override val part2: Int
        get() = input().first().let { inp ->
            val l = (1..inp.toInt()).toList()
            return elimination2(l)[0]
        }

    fun elimination2(l: List<Int>): List<Int> {
        var ret = l;
        while (ret.size > 1) {
//            println(ret)
            ret = eliminationRound2(ret)
        }
        return ret;
    }

    fun eliminationRound2(l: List<Int>) = sequence {
        ((l.size - 1) % 3 until ((l.size - 1) / 2) step 3).forEach {
            yield(l[it])
        }

        if (l.size > 1 && l.size % 6 == 1) {
            yield(l[l.size / 2 - 1])
        }
        if (l.size % 2 == 0) {
            ((l.size / 2) - 1 until l.size step 3).forEach {
                yield(l[it])
            }
        } else {
            ((l.size / 2) + 1 until l.size step 3).forEach {
                yield(l[it])
            }
        }
    }.toList()

}

fun main() {
    Day19.fancyRun()
}
