package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode

object Day18 : AdventOfCode(2022, 18) {
    data class XYZ(val x: Int, val y: Int, val z: Int) {

        fun adjecents() = sequenceOf(
            copy(x = x + 1),
            copy(x = x - 1),
            copy(y = y + 1),
            copy(y = y - 1),
            copy(z = z + 1),
            copy(z = z - 1),
        )

    }

    override val part1
        get() = readCubes().let { cubes ->
            cubes.map { it.adjecents().count { it !in cubes } }.sum()
        }

    override val part2
        get() = readCubes().let { cubes ->
            val holeChecker = HoleChecker(cubes)
            cubes.map { it.adjecents().filter { it !in cubes }.count { holeChecker.isOutside(it) } }.sum()
        }

    class HoleChecker(val positions: List<XYZ>) {
        val xRange = positions.minOf { it.x }..positions.maxOf { it.x }
        val yRange = positions.minOf { it.y }..positions.maxOf { it.z }
        val zRange = positions.minOf { it.y }..positions.maxOf { it.z }

        val knownOutside = mutableSetOf<XYZ>()
        val knownInside = mutableSetOf<XYZ>()

        fun isOutside(xyz: XYZ): Boolean {
            if (xyz in knownOutside) {
                return true
            }

            if (xyz in knownInside) {
                return false;
            }

            val filled = mutableSetOf<XYZ>()
            val isOutside = fill(xyz, filled)
            if (isOutside) {
                knownOutside
            } else {
                knownInside
            }.addAll(filled)
            return isOutside
        }

        // tierklinik  leipzig
        // tierklinik hofheim

        fun fill(xyz: XYZ, filled: MutableSet<XYZ> = mutableSetOf<XYZ>()): Boolean {
            val toFill = mutableListOf(xyz)

            while (toFill.isNotEmpty()) {
                val pos = toFill.removeLast()
                if (pos in filled) {
                    continue
                }

                if (pos.x !in xRange || pos.y !in yRange || pos.z !in zRange) {
                    return true
                }
                filled.add(pos)

                pos.adjecents().filter { it !in positions }.forEach { toFill.add(it) }
            }

            return false
        }

    }

    private fun readCubes() = inputLines().map {
        it.split(",").map { it.toInt() }.let { XYZ(it[0], it[1], it[2]) }
    }

}

fun main() {
    Day18.fancyRun()
}
