package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode

object Day7 : AdventOfCode(2017, 7) {
    private data class Program(
        val name: String,
        val weight: Int,
        val children: List<String>,
    )

    private data class ProgramHierarchy(
        val name: String,
        val weight: Int,
        val children: List<ProgramHierarchy>
    ) {
        val totalWeight: Int = weight + children.sumOf { it.totalWeight }
    }

    private val parsedInput by lazy {
        input.lines.map {
            val initialSplit = it.split(" -> ")
            val (name, wStr) = initialSplit[0].split(" ")
            val w = wStr.drop(1).dropLast(1).toInt()
            val children = if (initialSplit.size > 1) {
                initialSplit[1].split(", ")
            } else {
                emptyList()
            }
            Program(name, w, children)
        }
    }

    private val rootProgram by lazy {
        val progs = parsedInput
        val map = progs.associateBy { it.name }
        val root = (progs.map { it.name }.toSet() - progs.flatMap { it.children }.toSet()).first()

        fun convert(p: Program): ProgramHierarchy = ProgramHierarchy(
            p.name,
            p.weight,
            p.children.map { convert(map[it]!!) }
        )

        convert(map[root]!!)
    }

    override val part1: Any
        get() = rootProgram.name

    override val part2: Any
        get() = rootProgram.let {
            val (prog, expect) = findImbalance(it)
            val diff = prog.totalWeight - expect
            prog.weight - diff
        }

    private fun findImbalance(prog: ProgramHierarchy, expectedWeight: Int = -1): Pair<ProgramHierarchy, Int> {
        val childCode = prog.children.size
        return when (childCode) {
            0 -> prog to expectedWeight
            1 -> TODO() // findImbalance(prog.children.first())
            2 -> {
                val weight1 = prog.children[0].totalWeight
                val weight2 = prog.children[1].totalWeight
                check(weight1 == weight2)
                prog to expectedWeight
            }

            else -> {
                val weights = prog.children.groupingBy { it.totalWeight }.eachCount()
                if (weights.size == 1) {
                    prog to expectedWeight
                } else {
                    check(weights.size == 2) { "$weights" }
                    check(weights.count { it.value == 1 } == 1)
                    val wrongWeight = weights.filterValues { it == 1 }.keys.first()
                    val expectedWeight = weights.filterValues { it > 1 }.keys.first()
                    val outlier = prog.children.find { it.totalWeight == wrongWeight }!!
                    return findImbalance(outlier, expectedWeight)
                }
            }
        }
    }
}

fun main() {
    Day7.fancyRun()
}
