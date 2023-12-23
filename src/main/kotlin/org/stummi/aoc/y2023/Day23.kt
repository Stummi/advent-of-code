package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY

object Day23 : AdventOfCode(2023, 23) {
    val map by lazy { input.matrix }

    val directions = listOf(
        XY.ZERO.up to '^',
        XY.ZERO.down to 'v',
        XY.ZERO.left to '<',
        XY.ZERO.right to '>',
    )

    override val part1 get() = solve(true)
    override val part2 get() = solve(false)


    private fun solve(accountSlopes: Boolean): Int {
        val bounds = map.bounds

        val startNode = XY(map.row(0).indexOf('.'), 0)
        val endNode = XY(map.row(bounds.bottom).indexOf('.'), bounds.bottom)

        val openNodes = mutableListOf(startNode)
        val knownEdges = mutableMapOf<XY, Map<XY, Int>>(endNode to emptyMap())

        while (openNodes.isNotEmpty()) {
            val node = openNodes.removeFirst()
            val edges = findEdges(node, accountSlopes)
            knownEdges[node] = edges.groupBy(keySelector = { it.first }, valueTransform = { it.second })
                .mapValues { it.value.max() }
            edges.forEach { (nextNode, _) ->
                if (nextNode !in openNodes && nextNode !in knownEdges) {
                    openNodes.add(nextNode)
                }
            }
        }

        check(endNode in knownEdges)

        val possiblePaths = findPossiblePaths(startNode, endNode, knownEdges)
        return possiblePaths.maxOf {
            it.windowed(2).sumOf { (from, to) ->
                knownEdges[from]!![to]!!
            }
        }
    }

    private fun findPossiblePaths(
        currentNode: XY,
        endNode: XY,
        edges: Map<XY, Map<XY, Int>>,
        vistedNodes: Set<XY> = emptySet(),
    ): Sequence<List<XY>> {
        if (currentNode == endNode) {
            return sequenceOf(listOf(currentNode))
        }

        return edges[currentNode]!!.keys.asSequence().filter { it !in vistedNodes }.map { xy ->
            findPossiblePaths(xy, endNode, edges, vistedNodes + xy).map {
                listOf(currentNode) + it
            }
        }.flatten()
    }

    private fun findEdges(node: XY, accountSlopes: Boolean): List<Pair<XY, Int>> {
        return outgoingNeighbours(node, accountSlopes).map {
            var last = node
            var cur = it
            var steps = 1
            while (true) {
                val next = (outgoingNeighbours(cur, accountSlopes) - last).toSet()
                if (next.count() > 1) {
                    return@map cur to steps
                }

                last = cur
                cur = next.single()
                ++steps

                if (cur.y == 0 || cur.y == map.bounds.bottom) {
                    return@map cur to steps
                }
            }
            TODO()
        }.toList()
    }

    private fun outgoingNeighbours(xy: XY, accountSlopes: Boolean) =
        if (accountSlopes) {
            sequence {
                directions.forEach { (dir, char) ->
                    val n = xy + dir
                    if (n in map.bounds && (map[n] == '.' || map[n] == char)) {
                        yield(n)
                    }
                }
            }
        } else {
            xy.orthogonalNeighbours().filter {
                it in map.bounds && map[it] != '#'
            }
        }
}

fun main() {
    Day23.fancyRun(includeReal = true)
}
