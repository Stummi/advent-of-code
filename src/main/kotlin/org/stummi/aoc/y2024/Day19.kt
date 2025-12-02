package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode

object Day19 : AdventOfCode(2024, 19) {
    override val part1: Any
        get() {
            val inp = input.lines
            val towels = inp.first().split(", ")

            return inp.drop(2).count {
                hasPossibleCombination(it, towels)
            }
        }

    override val part2: Long
        get() {
            val inp = input.lines
            val towels = inp.first().split(", ")

            return inp.drop(2).sumOf {
                countPossibleCombination(it, towels)
            }
        }

    private fun hasPossibleCombination(pattern: String, towels: List<String>): Boolean {
        if (pattern.isEmpty()) {
            return true
        }

        return towels.asSequence().filter {
            pattern.endsWith(it)
        }.any { towel ->
            hasPossibleCombination(pattern.dropLast(towel.length), towels)
        }
    }

    private fun countPossibleCombination(pattern: String, towels: List<String>): Long {
        class Node (
            val index: Int,
            val goal: Int,
            val next: MutableSet<Node> = mutableSetOf(),
        ) {
            override fun toString() =
                "{idx: $index, goal: ${goal}: next: ${next.map { it.index }}}"

            val pathsToGoal: Long by lazy {
                if(index == goal) {
                    1
                } else {
                    next.sumOf { it.pathsToGoal }
                }
            }
        }

        val nodes = (0..pattern.length).map {
            Node(it, pattern.length)
        }

        towels.forEach { t ->
            pattern.allIndicesOf(t).forEach { idx ->
                val node = nodes[idx]
                val next = node.next
                val element = nodes[idx + t.length]
                next.add(element)
            }
        }
        val ret = nodes[0].pathsToGoal

        println("====")
        nodes.forEach {
            println("${it.index} -> ${it.pathsToGoal}")
        }

        return ret
    }

    private fun String.allIndicesOf(pattern: String): Sequence<Int> {
        val t = this
        return sequence {
            var i = t.indexOf(pattern)
            while(i >= 0) {
                yield(i)
                i = t.indexOf(pattern, i + 1)
            }
        }
    }


}

fun main() {
    Day19.fancyRun(includeDemo = false)
}
