package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode

object Day1 : AdventOfCode(2022, 1) {
    override val part1: Any
        get() = snacks().maxOrNull()!!

    override val part2: Any
        get() = snacks().sortedDescending().take(3).sum()

    private fun snacks() = sequence {
        var cnt = 0
        input().forEach {
            if (it.isBlank()) {
                yield(cnt)
                cnt = 0
            } else {
                cnt += it.toInt()
            }
        }
        yield(cnt)
    }
}

fun main() {
    Day1.fancyRun()
}
