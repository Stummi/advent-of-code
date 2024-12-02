package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.map
import kotlin.math.absoluteValue

object Day1 : AdventOfCode(2024, 1) {
    override val part1: Any
        get() {
            val (llist, rlist) = parsedInput().map { it.sorted() }

            return llist.mapIndexed(
                { i, l -> (rlist[i] - l).absoluteValue }
            ).sum()
        }

    override val part2: Any
        get() {
            val (llist, rlist) = parsedInput()

            return llist.sumOf { lv -> lv * rlist.count { it == lv } }
        }

    private fun parsedInput(): Pair<List<Int>, List<Int>> {
        val llist = mutableListOf<Int>()
        val rlist = mutableListOf<Int>()
        input.lines.map { it.split(Regex(" +")) }.map { (a, b) ->
            a.toInt() to b.toInt()
        }.forEach { (l, r) ->
            llist.add(l)
            rlist.add(r)
        }
        return Pair(llist, rlist)
    }
}

fun main() {
    Day1.fancyRun()
}
