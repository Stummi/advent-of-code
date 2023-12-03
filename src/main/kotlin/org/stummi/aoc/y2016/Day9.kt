package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day9 : AdventOfCode(2016, 9) {
    override val part1: Any
        get() = decompressedLength(input().first())
    override val part2: Any
        get() = decompressedLength(input().first(), true)

    fun decompressedLength(s: String, recursive: Boolean = false): Long {
        var curPos = 0;
        var ret = 0L
        while (true) {
            var markerStart = s.indexOf('(', curPos)
            if (markerStart == -1) {
                break;
            }
            var markerStop = s.indexOf(')', markerStart)

            val (len, rep) = s.substring(markerStart + 1, markerStop).split('x', limit = 2).let {
                it[0].toInt() to it[1].toInt()
            }

            ret += (markerStart - curPos)
            if (recursive) {
                val ss = s.substring(markerStop + 1, markerStop + 1 + len)
                ret += decompressedLength(ss, true) * rep
            } else {
                ret += (len * rep)
            }
            curPos = markerStop + 1 + len
        }
        return ret + s.length - curPos
    }
}

fun main() {
    println(Day9.part1)
    println(Day9.part2)
}

