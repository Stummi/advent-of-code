package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.splitToInts

object Day13 : AdventOfCode(2024, 13) {
    private data class XYLong(
        val x: Long,
        val y: Long,
    ) {
        constructor(xy: XY) : this(xy.x.toLong(), xy.y.toLong())
        operator fun minus(xy: XY) : XYLong {
            return XYLong(x - xy.x, y - xy.y)
        }
    }
    private data class Machine(
        val btnA: XY,
        val btnB: XY,
        val prize: XYLong,
    )

    private val parsedInput by lazy {
        input.lines.chunked(4).map {
            it.take(3).map {
                it.splitToInts().let { (x, y) -> XY(x, y) }
            }.let { (btnA, btnB, prize) ->
                Machine(btnA, btnB, XYLong(prize))
            }
        }
    }

    override val part1: Any
        get() = solve(parsedInput)


    override val part2: Any
        get() = solve(parsedInput.map {
            it.copy(
                prize = XYLong(
                    it.prize.x + 10000000000000L,
                    it.prize.y + 10000000000000L
                )
            )
        })

    private fun solve(machines: List<Machine>) {
        val m = machines.first()
        val xDiff = m.prize.x % m.btnA.x
        println(m)
        println(xDiff)
    }

    private fun solve2(machines: List<Machine>) = machines.sumOf { m ->
        generateSequence(XY.ZERO) { it + m.btnA }
            .takeWhile { ap -> ap.x <= m.prize.x && ap.y <= m.prize.y }.mapNotNull {
                val diff = m.prize - it
                if (
                    diff.x % m.btnB.x == 0L
                    && diff.y % m.btnB.y == 0L
                    && diff.x / m.btnB.x == diff.y / m.btnB.y
                ) {
                    it.x / m.btnA.x to (m.prize.x - it.x) / m.btnB.x
                } else {
                    null
                }
            }.minOfOrNull {
                it.first * 3 + it.second * 1
            } ?: 0
    }

}

fun main() {
    Day13.fancyRun()
}
