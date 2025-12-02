package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.CharMatrix
import org.stummi.aoc.helper.OpenList
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.astar

object Day16 : AdventOfCode(2024, 16) {
    private val map by lazy { input.matrix }

    init {
        resourceSample("demo_2")
    }

    private data class State(
        val pos: XY,
        val direction: XY,
    ) {
        fun left() = copy(
            direction = direction.rotateLeft
        )

        fun right() = copy(
            direction = direction.rotateRight
        )

        fun forward() = copy(
            pos = pos + direction
        )
    }

    override val part1: Int
        get() {
            val map = map
            val start = map.find('S')
            val end = map.find('E')

            return astar(
                initialState = State(start, XY.ZERO.right),
                nextStates = { state ->
                    nextStates(state, map).asSequence()
                },
                goal = { it.pos == end },
                heuristicCost = { it.pos.orthogonalDistanceTo(end) }
            ).sumOf {
                it.second
            }
        }

    private fun nextStates(
        state: State,
        map: CharMatrix
    ) = listOfNotNull(
        state.left() to 1000,
        state.right() to 1000,
        state.forward().takeIf { map[it.pos] != '#' }?.let { it to 1 }
    )


    override val part2: Int
        get() {
            val bestCost = part1
            val map = map
            val start = map.find('S')
            val end = map.find('E')
            val initialState = start to XY.ZERO.right
            val openList = OpenList(initialState)

            while (true) {
                val (state, cost) = openList.popNext() ?: break
                if (cost > bestCost) {
                    break
                }


            }



            return 0
        }

    private fun getOnePath(
        map: CharMatrix,
        pos: XY,
        direction: XY,
        end: XY,
        remainingCost: Int,
        visited: Set<XY> = emptySet()
    ): Set<XY> {
        if (
            end.orthogonalDistanceTo(pos) > remainingCost ||
            map[pos] == '#' ||
            pos in visited
        ) {
            return emptySet()
        }

        if (pos == end) {
            return setOf(pos)
        }

        val retSet = sequence {
            val nextVisited = visited + pos
            yield(
                getOnePath(
                    map,
                    pos + direction,
                    direction,
                    end,
                    remainingCost - 1,
                    nextVisited
                )
            )

            yield(
                getOnePath(
                    map,
                    pos + direction.rotateLeft,
                    direction.rotateLeft,
                    end,
                    remainingCost - 1001,
                    nextVisited
                )
            )

            yield(
                getOnePath(
                    map,
                    pos + direction.rotateRight,
                    direction.rotateRight,
                    end,
                    remainingCost - 1001,
                    nextVisited
                )
            )
        }.reduce { a, b -> a + b }

        return if (retSet.isEmpty()) {
            retSet
        } else {
            retSet + pos
        }
    }

}

fun main() {
    Day16.fancyRun()
}
