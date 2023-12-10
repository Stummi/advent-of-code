package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.astar

object Day16 : AdventOfCode(2022, 16) {

    init {
        resourceSample("demo", 1651, 1707)
    }

    data class Valve(
        val name: String,
        val flowRate: Int,
        val connections: List<String>,
    )

    private val parsedInput by lazy {
        Solver(
            input.lines.map { it.split(" ") }.map {
                val name = it[1]
                val flowRate = it[4].removePrefix("rate=").removeSuffix(";").toInt()
                val connections = it.drop(9).map { it.removeSuffix(",") }
                name to Valve(name, flowRate, connections)
            }.toMap()
        )
    }

    private class Solver(val valves: Map<String, Valve>) {
        val pathCache = mutableMapOf<Pair<String, String>, List<String>>()
        val allValueValves = valves.values.filter { it.flowRate > 0 }.map { it.name }.toList()

        data class Path(
            val visitedValves: Set<String> = emptySet(),
            val timeLeft: Int = 30,
            val flowRate: Int = 0,
            val totalFlow: Int = 0,
        ) {
            val value = totalFlow + (flowRate * timeLeft)
        }

        fun plausiblePaths(
            pos: String = "AA",
            pathSoFar: Path = Path()
        ): Sequence<Path> {
            return sequenceOf(pathSoFar) + allValueValves.asSequence().filter {
                it !in pathSoFar.visitedValves
            }.map {
                it to path(pos, it)
            }.filter { (_, path) ->
                path.size < pathSoFar.timeLeft
            }.map { (goal, path) ->
                val timeStep = path.size + 1
                plausiblePaths(
                    goal,
                    Path(
                        visitedValves = pathSoFar.visitedValves + goal,
                        timeLeft = pathSoFar.timeLeft - timeStep,
                        flowRate = pathSoFar.flowRate + valves[goal]!!.flowRate,
                        totalFlow = pathSoFar.totalFlow + pathSoFar.flowRate * timeStep
                    )
                )
            }.flatten()
        }

        operator fun get(to: String): Valve {
            return valves[to]!!
        }

        fun path(from: String, to: String): List<String> {
            pathCache[to to from]?.let { return (it.dropLast(1).reversed() + to) }
            return pathCache.getOrPut(from to to) {
                astar(
                    from,
                    { valves[it]!!.connections.map { it to 1 }.asSequence() },
                    { it == to },
                    { 0 }
                ).map { it.first }.reversed()
            }
        }

    }


    override val part1
        get(): Int {
            return parsedInput.plausiblePaths().maxOf {
                it.value
            }
        }

    override val part2: Int
        get(): Int {
            val bestMap = parsedInput.plausiblePaths(pathSoFar = Solver.Path(timeLeft = 26)).groupingBy {
                it.visitedValves
            }.fold(0) { cur, cand -> maxOf(cur, cand.value) }

            return bestMap.maxOf { (meVisisted, value) ->
                value + bestMap.filterKeys {
                    it.none { it in meVisisted }
                }.values.max()
            }
        }
}

fun main() {
    Day16.fancyRun()
}
