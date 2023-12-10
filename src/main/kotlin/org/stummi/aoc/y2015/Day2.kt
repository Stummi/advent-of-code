package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode

object Day2 : AdventOfCode(2015, 2) {
    val boxes get() = inputLines().map { it.split("x").map { it.toInt() } }.map { GiftBox(it[0], it[1], it[2]) }

    override val part1 get() = boxes.sumOf { wrappingPaper(it) }
    override val part2 get() = boxes.sumOf { ribbon(it) }

    data class GiftBox(
        val w: Int,
        val h: Int,
        val l: Int,
    )

    fun ribbon(it: GiftBox) =
        listOf(it.w, it.l, it.h).sorted().take(2).map { it * 2 }.sum() + it.w * it.l * it.h


    private fun wrappingPaper(it: GiftBox): Int {
        val s1 = it.w * it.h
        val s2 = it.w * it.l
        val s3 = it.l * it.h

        return (s1 + s2 + s3) * 2 + listOf(s1, s2, s3).minOrNull()!!
    }
}


fun main() {
    Day2.fancyRun()
}

