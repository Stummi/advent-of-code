package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode

object Day17 : AdventOfCode(2015, 17) {

    init {
        resourceSample("demo1", 4, 3, 25)
    }

    override val part1: Any
        get() = solve.size
    override val part2: Any
        get() = solve.minOf { it.size }.let { min ->
            solve.count { it.size == min }
        }

    val solve by lazy {
        val containers = inputLines().map { it.toInt() }
        fillContainers(150.orSample(), containers)
    }
}

fun main() {
    Day17.fancyRun()
}

fun fillContainers(i: Int, containers: List<Int>, currentContainers: List<Int> = emptyList()): List<List<Int>> {
    if (i < 0) {
        return emptyList()
    }

    if (i == 0) {
        return listOf(currentContainers)
    }

    return containers.flatMapIndexed { idx, v ->
        fillContainers(i - v, containers.drop(idx + 1), currentContainers + v)
    }
}


