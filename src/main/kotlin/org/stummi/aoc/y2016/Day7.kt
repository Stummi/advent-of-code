package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day7 : AdventOfCode(2016, 7) {

    override val part1: Any
        get() = inputIps().filter {
            it.first.any { isAbba(it) } && it.second.none { isAbba(it) }
        }.count()

    override val part2: Any
        get() = inputIps().filter {
            it.first.flatMap { getAbas(it) }.map {
                listOf(it[1], it[0], it[1]).joinToString("")
            }.any { aba ->
                it.second.any { it.contains(aba) }
            }
        }.count()

    private fun inputIps() = inputLines().map {
        it.split('[', ']').mapIndexed { i, s -> s to i }.partition {
            it.second % 2 == 0
        }.let {
            it.first.map { it.first } to it.second.map { it.first }
        }
    }

    private fun getAbas(it: String) = it.windowed(3).filter {
        it[0] != it[1] && it[0] == it[2]
    }

    private fun isAbba(it: String) =
        it.windowed(4).any {
            it[0] != it[1] && it[0] == it[3] && it[1] == it[2]
        }


}

fun main() {
    println(Day7.part1)
    println(Day7.part2)
}
