package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.tuplePermutations

object Day22 : AdventOfCode(2016, 22) {
    data class Node(val name: String, val size: Int, val used: Int, val avail: Int) {
        fun toXY() = name.split("-").let { spl -> XY(spl[1].drop(1).toInt(), spl[2].drop(1).toInt()) }
    }

    override val part1: Int
        get() = nodes().tuplePermutations().filter { (a, b) ->
            a.used != 0 && a.used <= b.avail
        }.count()

    override val part2: Any
        get() {
            val nodeMap = nodes().associateBy { it.toXY() }
            val maxX = nodeMap.keys.maxOf { it.x }
            val maxY = nodeMap.keys.maxOf { it.y }

            (0..maxY).forEach { y ->
                (0..maxX).forEach { x ->
                    val node = nodeMap[XY(x, y)]!!
                    val type = when {
                        node.used > 100 -> "#"
                        node.used == 0 -> "_"
                        else -> "."
                    }
                    print(String.format(" %s ", type))
                }
                println()
            }

            TODO()
        }

    // 27 + 5 * 13
    // 26 + 33 * 5 + 1

    // 1755 -> too high?
    private fun nodes() = inputLines().drop(2).map { it.split(Regex(" +")) }.map { line ->
        val name = line[0]
        val (size, used, avail) = (1..3).map { line[it].trimEnd('T').toInt() }
        Node(name, size, used, avail)
    }


}

fun main() {
    Day22.fancyRun()
}
