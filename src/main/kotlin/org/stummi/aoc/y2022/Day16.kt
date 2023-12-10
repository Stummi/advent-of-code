package org.stummi.aoc.y2022

import Day23.replace
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

    data class Actor(
        val name: String,
        val lastLocation: String,
        val nextLocation: String? = null,
        val pathLen: Int = 0,
        val progressInPath: Int = 0,
    ) {
        override fun toString(): String {
            return if (nextLocation == null) {
                "{$name@$lastLocation}"
            } else {
                "{$name@$lastLocation ==> $nextLocation: $progressInPath/$pathLen}"
            }
        }
    }

    data class State(
        val actors: List<Actor>,
        val minute: Int = 0,
        val closedValves: Set<String> = emptySet(),
        val currentRelease: Int = 0,
        val accumulatedRelease: Int = 0,
    ) {
        private fun handleIdleActors(valveMap: ValveMap): Sequence<State>? {
            val idleActors = actors.filter { it.nextLocation == null }
            if (idleActors.isEmpty()) {
                return null;
            }

            val remainingTime = 30 - minute
            val currentGoals = actors.mapNotNull { it.nextLocation }.toSet()
            val openValves = valveMap.allValueValves - closedValves - currentGoals
            val possibleMovesPerActor = idleActors.map { a ->
                openValves.filter {
                    valveMap.distance(a.lastLocation, it) < (remainingTime)
                }
            }.toList()

            val actorsToRemove = sequence {
                possibleMovesPerActor.forEachIndexed { idx, moves ->
                    if (moves.isEmpty()) {
                        yield(idleActors[idx])
                    }
                }
            }.toSet()

            if (actorsToRemove.isNotEmpty()) {
                return sequenceOf(
                    copy(actors = actors - actorsToRemove)
                )
            } else {
                val firstActor = idleActors[0]
                val possibleMoves = possibleMovesPerActor[0]
                return possibleMoves.asSequence().map {
                    copy(
                        actors =
                        actors.replace(
                            actors.indexOf(firstActor), firstActor.copy(
                                nextLocation = it,
                                pathLen = valveMap.distance(firstActor.lastLocation, it)
                            )
                        )
                    )
                }
            }
        }

        fun handleActorsAtGoal(valveMap: ValveMap): Sequence<State>? {
            val actorsInGoal = actors.filter { it.nextLocation != null && it.pathLen == it.progressInPath }.toSet()
            if (actorsInGoal.isEmpty()) {
                return null;
            }

            val newlyClosedValves = actorsInGoal.map { it.nextLocation!! }
            val updatedActors = actorsInGoal.map {
                Actor(
                    name = it.name,
                    lastLocation = it.nextLocation!!,
                    nextLocation = null,
                    pathLen = 0,
                    progressInPath = 0,
                )
            }
            val sumOfFlowRate = newlyClosedValves.sumOf { valveMap[it].flowRate }

            return sequenceOf(
                State(
                    actors = (actors - actorsInGoal).map {
                        it.copy(progressInPath = it.progressInPath + 1)
                    } + updatedActors,
                    minute = this.minute + 1,
                    closedValves = closedValves + newlyClosedValves,
                    currentRelease = currentRelease + sumOfFlowRate,
                    accumulatedRelease = accumulatedRelease + currentRelease
                )
            )
        }

        fun solve(valveMap: ValveMap): List<State> {
            if (actors.isEmpty()) {
                return listOf(waitTillEnd())
            } else {
                val solved =
                    transitions(valveMap).map { it.solve(valveMap) }.maxByOrNull { it.last().accumulatedRelease }!!
                return listOf(this) + solved
            }
        }

        fun transitions(valveMap: ValveMap): Sequence<State> {
            if (actors.isEmpty()) {
                return sequenceOf(waitTillEnd())
            }

            // Check if we have idle actors first
            handleIdleActors(valveMap)?.let { return it }
            handleActorsAtGoal(valveMap)?.let { return it }

            val ticks = actors.minOf { (it.pathLen - it.progressInPath) }

            return sequenceOf(
                State(
                    actors.map {
                        it.copy(
                            progressInPath = it.progressInPath + ticks
                        )
                    },
                    minute + ticks,
                    closedValves,
                    currentRelease,
                    accumulatedRelease + currentRelease * ticks
                )
            )
        }

        fun waitTillEnd(end: Int = 30): State {
            if (end == minute) {
                return this;
            }
            if (end < minute) {
                throw IllegalArgumentException("cannot travel back in time: ${this}")
            }

            val timeElapse = (end - minute)
            return State(
                actors,
                minute + timeElapse,
                closedValves,
                currentRelease,
                accumulatedRelease + (currentRelease * timeElapse)
            )
        }
    }

    class ValveMap(val valves: Map<String, Valve>) {
        val distanceCache = mutableMapOf<Pair<String, String>, Int>()
        val allValueValves = valves.values.filter { it.flowRate > 0 }.map { it.name }.toList()

        fun distance(from: String, to: String): Int =
            distanceCache.getOrPut(from to to) {
                astar(
                    from,
                    { valves[it]!!.connections.map { it to 1 }.asSequence() },
                    { it == to },
                    { 0 }
                ).size
            }

        operator fun get(to: String): Valve {
            return valves[to]!!
        }

    }


    override val part1
        get(): Int {
            val valveMap = ValveMap(readValves().associateBy { it.name })
            val state = State(
                listOf(Actor("Me", "AA", null, 0, 0))
            )
            val solved = state.solve(valveMap)
            println(solved.last())
            return solved.last().accumulatedRelease
        }

    override val part2: Int
        get() {
            val valveMap = ValveMap(readValves().associateBy { it.name })
            var state = State(
                listOf(
                    Actor("Me", "AA", null, 0, 0),
                    Actor("Elephant", "AA", null, 0, 0)
                ),
                minute = 4,
            )
            val solved = state.solve(valveMap);
            return solved.last().accumulatedRelease
        }

    private fun applyFixedSteps(
        state: Day16.State,
        fixedSteps: Map<String, List<String>>,
        valveMap: ValveMap
    ): Day16.State {
        return state.copy(
            actors = state.actors.map {
                if (it.nextLocation != null) {
                    it
                } else {
                    fixedSteps[it.name]!!.let { l ->
                        val i = l.indexOf(it.lastLocation) + 1
                        val nextLocation = if (i >= l.size) null else l[i]
                        it.copy(
                            nextLocation = nextLocation,
                            pathLen = nextLocation?.let { n -> valveMap.distance(it.lastLocation, n) } ?: 0

                        )
                    }
                    //it
                }
            }
        )
    }

    private fun readValves(): List<Valve> {
        return inputLines().map { it.split(" ") }.map {
            val name = it[1]
            val flowRate = it[4].removePrefix("rate=").removeSuffix(";").toInt()
            val connections = it.drop(9).map { it.removeSuffix(",") }
            Valve(name, flowRate, connections)
        }
    }
}

fun main() {
    Day16.fancyRun()
}

// p1 1635 low
// p1 1735 high
