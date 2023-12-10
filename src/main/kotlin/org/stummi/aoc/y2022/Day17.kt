package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.bounds

object Day17 : AdventOfCode(2022, 17) {
    init {
        rawSample(">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>", 3068, 1514285714288)
    }

    val tiles = listOf(
        Tile(0 to 0, 1 to 0, 2 to 0, 3 to 0),
        Tile(1 to 0, 0 to 1, 1 to 1, 2 to 1, 1 to 2),
        Tile(2 to 0, 2 to 1, 0 to 2, 1 to 2, 2 to 2),
        Tile(0 to 0, 0 to 1, 0 to 2, 0 to 3),
        Tile(0 to 0, 0 to 1, 1 to 0, 1 to 1),
    )

    class RingProvider<T>(val list: List<T>) {
        var index = 0
        fun next() = list[index++].also {
            index %= list.size
        }
    }

    class Game(val jetProvider: RingProvider<Char>) {
        val setTiles = mutableSetOf<XY>()
        var currentTop = 0
        val baseLine = generateSequence { 0 }.take(7).toMutableList()

        fun addTile(tile: Tile) {
            val startX = 2
            val startY = currentTop - 3 - tile.bounds.bottom

            var pos = XY(startX, startY)
            //println("S $pos")
            while (true) {
                val move = jetProvider.next()
                val nextPos = when (move) {
                    '>' -> pos.right
                    '<' -> pos.left
                    else -> throw IllegalArgumentException()
                }
                if (isLegalPosition(tile, nextPos)) {
                    pos = nextPos
                }
                //println("$move $pos")
                val down = pos.down
                if (isLegalPosition(tile, down)) {
                    pos = down
                } else {
                    break
                }
            }

            val newTiles = tile.points.map { it.translate(pos) }

            currentTop = (newTiles.minOf { it.y } - 1).coerceAtMost(currentTop)

            (0..6).forEach { x ->
                val min = newTiles.filter { it.x == x }.minByOrNull { it.y } ?: return@forEach
                baseLine[x] = (min.y - 1).coerceAtMost(baseLine[x])
            }

            setTiles.addAll(newTiles)
        }

        fun print() {
            (XY(0, currentTop)..XY(6, currentTop + 10)).printAsMap {
                if (it in setTiles) '#' else '.'
            }
            println()
        }

        private fun isLegalPosition(tile: Day17.Tile, pos: XY): Boolean {
            if (pos.x < 0) {
                return false
            }

            if (pos.x > (6 - tile.bounds.right)) {
                return false
            }

            if (pos.y > -tile.bounds.bottom) {
                return false
            }

            return tile.points.asSequence().map { it.translate(pos) }.none { it in setTiles }
        }


    }


    data class Tile(val points: Set<XY>) {
        val bounds = points.bounds()

        constructor(vararg coords: Pair<Int, Int>) : this(coords.map { XY(it.first, it.second) }.toSet())
    }

    override val part1: Int
        get() {
            val tileProvider = RingProvider(tiles)
            val jetProvider = RingProvider(inputLines().first().toList())
            val game = Game(jetProvider)
            repeat(2022) {
                game.addTile(tileProvider.next())
                //game.print()
                //println(game.currentTop)
            }

            return -game.currentTop;
        }

    override val part2: Any
        get() {
            val tileProvider = RingProvider(tiles)
            val jetProvider = RingProvider(inputLines().first().toList())
            val game = Game(jetProvider)

            val totalTiles = 1000000000000L
            val fullRound = tiles.size * jetProvider.list.size

            val knownBaseLines = mutableListOf<List<Int>>()
            val heightsAfterRounds = mutableListOf<Int>()
            var repeatFrom = 0
            var repeatTo = 0
            var tilesPlaced = 0
            heightsAfterRounds.add(game.currentTop)
            knownBaseLines.add(game.baseLine)
            for (it in 1..1000) {
                repeat(fullRound) {
                    ++tilesPlaced
                    game.addTile(tileProvider.next())
                }

                heightsAfterRounds.add(game.currentTop)
                val max = game.baseLine.maxOrNull()!!
                val baseLine = game.baseLine.map { it - max }
                if (knownBaseLines.contains(baseLine)) {
                    repeatFrom = knownBaseLines.indexOf(baseLine)
                    repeatTo = it
                    break
                } else {
                    knownBaseLines.add(baseLine)
                }
            }
            println("Found repetition: $repeatFrom $repeatTo")
            val fullRoundsTotal = totalTiles / fullRound
            println("tiles per round: $fullRound")
            println("full rounds: $fullRoundsTotal")
            val repetitionsLength = repeatTo - repeatFrom
            val repetitions = (((fullRoundsTotal) - repeatFrom - 1) / repetitionsLength) - 1
            var repetitionGrowth = heightsAfterRounds[repeatTo].toLong() - heightsAfterRounds[repeatFrom]
            println("growth/repitition: $repetitionGrowth")
            println("repititions: $repetitions")
            println("growth: ${repetitions * repetitionGrowth}")


            val heightAfterRepetitions = heightsAfterRounds[repeatTo] + (repetitions * repetitionGrowth)
            val roundsAfterRepetitions = repeatTo + repetitions * repetitionsLength
            val tilesAfterRepetition = roundsAfterRepetitions * fullRound
            val tilesLeft = totalTiles - tilesAfterRepetition
            println("tiles placed1: $tilesPlaced")
            println("tiles placed2: ${tilesPlaced + (repetitions * repetitionsLength * fullRound)}")
            println("tiles Left: $tilesLeft")
            val curHight = game.currentTop
            repeat(tilesLeft.toInt()) { game.addTile(tileProvider.next()) }
            return -(heightAfterRepetitions + (game.currentTop - curHight))
        }
    // 1597719479942 too high
    // 1597719479942
}

fun main() {
    Day17.fancyRun()
}
