package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode

object Day12 : AdventOfCode(2017, 12) {
    val parsedInput by lazy {
        input.lines.associate {
            val spl = it.split(" <-> ")
            spl[0].toInt() to spl[1].split(", ").map { it.toInt() }
        }
    }

    val groups by lazy {
        val openKeys = parsedInput.keys.toMutableList()
        val groups = mutableListOf<Set<Int>>()
        while (openKeys.isNotEmpty()) {
            val first = openKeys.first()
            val group = collectGroup(first)
            openKeys.removeAll(group)
            groups.add(group)
        }
        groups.toList()
    }

    private fun collectGroup(key: Int, currentSet: MutableSet<Int> = mutableSetOf()): Set<Int> {
        currentSet.add(key)
        parsedInput[key]!!.forEach {
            if (it in currentSet) {
                return@forEach
            }
            collectGroup(it, currentSet)
        }
        return currentSet.toSet()
    }

    override val part1: Any
        get() = groups.find { 0 in it }!!.size

    override val part2: Any
        get() = groups.size


}

fun main() {
    Day12.fancyRun()
}
