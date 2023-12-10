package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.CharMatrix
import org.stummi.aoc.helper.Matrix
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.map

object Day10 : AdventOfCode(2023, 10) {
    init {
        resourceSample("demo_square", 4, Unit)
        resourceSample("demo_complex", 8, Unit)
        resourceSample("demo2_simple", Unit, 4)
        resourceSample("demo2_squeeze", Unit, 4)
        resourceSample("demo2_large", Unit, 8)
        resourceSample("demo2_complex", Unit, 10)
    }

    private class InputMap(
        val delegate: Matrix<Char>,
        val assumeStartPos: Char? = null
    ) : Matrix<Char> by delegate {
        fun withAssumeStartPos(char: Char) = InputMap(delegate, char)

        override operator fun get(xy: XY) = delegate[xy].let {
            if (it == 'S') assumeStartPos ?: it else it
        }

        fun allConnectionsFrom(from: XY): Set<XY> {
            return (connections[get(from)] ?: return emptySet())
                .map {
                    from.translate(it)
                }.toList().toSet()
        }

        fun hasConnection(from: XY, to: XY): Boolean {
            return (from in allConnectionsFrom(to) && to in allConnectionsFrom(from))
        }
    }

    private val connections = mapOf(
        '|' to (XY(0, 1) to XY(0, -1)),
        '-' to (XY(1, 0) to XY(-1, 0)),
        'L' to (XY(0, -1) to XY(1, 0)),
        'J' to (XY(0, -1) to XY(-1, 0)),
        '7' to (XY(0, 1) to XY(-1, 0)),
        'F' to (XY(0, 1) to XY(1, 0)),
    )

    private val parsedInput by lazy { InputMap(input.matrix) }

    private data class SolvedCircle(
        val startTile: Char,
        val startPos: XY,
        val circleTiles: Set<XY>,
    )

    private val solvedCircle by lazy {
        connections.keys.firstNotNullOf {
            getSolvedCircle(parsedInput, it)
        }
    }

    private fun getSolvedCircle(map: InputMap, assumeStartTile: Char): SolvedCircle? {
        val startPos = parsedInput.find('S')
        var lastPos = startPos
        val (first, last) = connections[assumeStartTile]!!.map { lastPos.translate(it) }
        var currentPos = first
        val circleTiles = mutableSetOf(currentPos)
        while (currentPos != startPos) {
            if ((currentPos.x !in (0..<map.width)) || (currentPos.y !in (0..<map.height))) {
                return null
            }

            val tile = map[currentPos]
            if (tile == '.') {
                return null
            }
            val connection =
                (connections[tile] ?: throw IllegalArgumentException("tile $tile at $currentPos (S= $startPos)"))
                    .map { currentPos.translate(it) }.toList().filter { lastPos != it }
            if (connection.size > 1) {
                return null
            }
            lastPos = currentPos
            currentPos = connection.first()
            circleTiles += currentPos
        }
        return if (lastPos == last) SolvedCircle(
            assumeStartTile,
            startPos,
            circleTiles,
        ) else null
    }

    private fun resize(iMap: InputMap, solved: SolvedCircle): InputMap {
        val map = iMap.withAssumeStartPos(solved.startTile)
        val w = map.width * 2
        val h = map.height * 2
        val ret = CharMatrix(XY.ZERO..<XY(w, h))
        ret.bounds.asSequence().forEach { xy ->
            val (x, y) = xy
            ret[xy] = if (x % 2 == 0 && y % 2 == 0) {
                val mapXy = XY(x / 2, y / 2)
                if (mapXy in solved.circleTiles) {
                    map[mapXy]
                } else {
                    ' '
                }
            } else if (x % 2 == 0 && y % 2 == 1) {
                val above = XY(x / 2, y / 2)
                val below = above.down
                if (
                    above in solved.circleTiles && below in solved.circleTiles &&
                    map.hasConnection(above, below)
                ) {
                    '|'
                } else {
                    ' '
                }
            } else if (x % 2 == 1 && y % 2 == 0) {
                val left = XY(x / 2, y / 2)
                val right = left.right
                if (
                    left in solved.circleTiles && right in solved.circleTiles &&
                    map.hasConnection(left, right)
                ) {
                    '-'
                } else {
                    ' '
                }
            } else {
                ' '
            }
        }
        return InputMap(ret)
    }

    override val part1: Any
        get() = solvedCircle.circleTiles.size / 2

    override val part2: Any
        get() {
            val map = parsedInput
            val resized = resize(map, solvedCircle)
            val inside = mutableListOf<XY>()
            val outside = mutableListOf<XY>()
            map.bounds.asSequence().forEach {
                if (it !in inside && it !in outside && resized[it] == ' ') {
                    val (fill, oob) = floodFill(resized, it)
                    (if (oob) {
                        outside
                    } else {
                        inside
                    }).addAll(fill)
                }
            }

            /*
            resized.bounds.printAsMap { xy ->
                when {
                    (xy in inside) -> 'I'
                    (xy in outside) -> 'O'
                    else -> resized[xy]
                }
             }
            */
            return inside.count { it.x % 2 == 0 && it.y % 2 == 0 }
        }

    private fun floodFill(inp: InputMap, start: XY): Pair<MutableSet<XY>, Boolean> {
        val openList = mutableListOf(start)
        val filled = mutableSetOf<XY>()
        var oob = false
        while (openList.isNotEmpty()) {
            val xy = openList.removeAt(0)
            if (xy !in inp.bounds) {
                oob = true
                continue
            }

            if (xy in filled) {
                continue
            }

            if (inp[xy] != ' ') {
                continue
            }

            filled.add(xy)

            openList.addAll(xy.orthogonalNeighbours())
        }

        return filled to oob
    }
}

fun main() {
    Day10.fancyRun()
}
