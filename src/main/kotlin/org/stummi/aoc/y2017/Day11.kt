package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import kotlin.math.absoluteValue

object Day11 : AdventOfCode(2017, 11) {
    init {
        rawSample("ne,ne,ne", 3)
        rawSample("ne,ne,sw,sw", 0)
        rawSample("ne,ne,s,s", 2)
        rawSample("se,sw,se,sw,sw", 3)
    }

    private val hexMovements = mapOf<String, (XY) -> XY>(
        "n" to { it.translate(XY(0, -1)) },
        "s" to { it.translate(XY(0, 1)) },
        "nw" to { (x, y) -> XY(x - 1, y - (x % 2).absoluteValue) },
        "ne" to { (x, y) -> XY(x + 1, y - (x % 2).absoluteValue) },
        "sw" to { (x, y) -> XY(x - 1, y + 1 - (x % 2).absoluteValue) },
        "se" to { (x, y) -> XY(x + 1, y + 1 - (x % 2).absoluteValue) },
    )

    private val moves by lazy {
        input.lines.first().split(",").runningFold(XY.ZERO) { xy, cmd -> hexMovements[cmd]!!(xy) }
    }

    override val part1: Any
        get() = calcDistance(moves.last())

    override val part2: Any
        get() = moves.maxOf { calcDistance(it) }

    /**
     * This still works even though we are moving through hex-coords and not squares, because our mapping works so
     * that the speed into both directions (east/west and north/south) is still 1/step
     */
    private fun calcDistance(hexTile: XY): Int = hexTile.mooreDistanceTo(XY.ZERO)
}

fun main() {
    Day11.fancyRun()
}
