package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.md5String

object Day4 : AdventOfCode(2015, 4) {

    override val part1: Any
        get() = solve(5)

    override val part2: Any
        get() = solve(6)

    fun solve(zeroes: Int): Int {
        val searchPrefix = "0".repeat(zeroes)
        val prefix = input().first()
        var idx = 0
        while(true) {
            val check = md5String("$prefix$idx")
            val searchPredix = check.startsWith(searchPrefix)
            if(searchPredix) {
                return idx
            }
            ++idx
        }
    }


}

fun main() {
    Day4.fancyRun()
}
