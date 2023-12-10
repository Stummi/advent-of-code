package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode

object Day6 : AdventOfCode(2023, 6) {
    fun parseInputPt1(): List<Pair<Long, Long>> = inputLines().let {
        val (timeLine, distLine) = it.map { it.split(":").drop(1).first().trim() }

        return timeLine.split(" +".toRegex()).map { it.toLong() }
            .zip(distLine.split(" +".toRegex()).map { it.toLong() })
    }

    fun parseInputPt2(): Pair<Long, Long> = inputLines().let {
        val (time, dist) = it
            .map {
                it.split(":")
                    .drop(1)
                    .first()
                    .replace(" ", "")
                    .toLong()
            }

        time to dist
    }


    override val part1: Any
        get() {
            return parseInputPt1().map { (time, dist) ->
                (1..<time).count { charge ->
                    calculatePosition(charge, time) > dist
                }
            }.reduce(Int::times)
        }

    override val part2: Any
        get() = parseInputPt2().let {
            val (time, dist) = it
            (1L..<time).count { charge ->
                calculatePosition(charge, time) > dist
            }
        }

    fun calculatePosition(chargeTime: Long, totalTime: Long) =
        (chargeTime * (totalTime - chargeTime)).also {
            if (it < 0) throw IllegalStateException("Negative position")
        }
}

fun main() {
    Day6.fancyRun()
}
