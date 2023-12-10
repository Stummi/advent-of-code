package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.XYRange
import org.stummi.aoc.helper.bounds

object Day14 : AdventOfCode(2022, 14) {

    class Game(val walls: Set<XY>) {
        enum class Tile {
            WALL, SAND
        }

        val tiles = walls.associateWith { Tile.WALL }.toMutableMap()
        val bounds: XYRange = (walls + XY(500, 0)).bounds()

        fun print() {
            println(bounds)
            val b = XYRange(bounds.topLeft.left.left, bounds.bottomRight.downRight.downRight.downRight)
            b.yRange.forEach { y ->
                b.xRange.forEach { x ->
                    print(
                        when (tiles[XY(x, y)]) {
                            Tile.WALL -> "#"
                            Tile.SAND -> "o"
                            null -> "."
                        }
                    )

                }
                println()
            }
        }

        fun addSand(pos: XY, part2: Boolean): Boolean {
            var finalPos = pos
            val bounds = this.bounds
            if (pos in tiles) {
                return false;
            }
            while (true) {
                if (!part2) {
                    if (finalPos !in bounds) {
                        return false;
                    }
                } else {
                    if (finalPos.y == bounds.bottomRight.y + 1) {
                        break;
                    }
                }
                if (finalPos.down !in tiles) {
                    finalPos = finalPos.down
                } else if (finalPos.downLeft !in tiles) {
                    finalPos = finalPos.downLeft
                } else if (finalPos.downRight !in tiles) {
                    finalPos = finalPos.downRight
                } else {
                    break
                }
            }
            tiles[finalPos] = Tile.SAND
            return true
        }

    }

    override val part1: Int
        get() = readMap().let {
            var cnt = 0
            while (it.addSand(XY(500, 0), false)) {
                cnt++
            }
            cnt;
        }

    override val part2: Int
        get() = readMap().let {
            var cnt = 0
            while (it.addSand(XY(500, 0), true)) {
                cnt++
            }
            cnt;
        }

    private fun readMap(): Game {
        val l = inputLines().map {
            it.split(" -> ").windowed(2).map {
                val xy1 = parseXY(it[0])
                val xy2 = parseXY(it[1])
                listOf(xy1, xy2).bounds().asSequence().toList()
            }.flatten()
        }.flatten().toSet()
        return Game(l)
    }

    private fun parseXY(s: String): XY {
        return s.split(",").let {
            XY(it[0].toInt(), it[1].toInt())
        }
    }
}

fun main() {
    Day14.fancyRun()
}
