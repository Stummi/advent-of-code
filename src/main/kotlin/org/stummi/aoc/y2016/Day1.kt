package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode
import kotlin.math.absoluteValue

object Day1 : AdventOfCode(2016, 1) {
    val dirs = listOf('N', 'E', 'S', 'W')
    val rot = mapOf('L' to 3, 'R' to 1)


    private fun move(pos: Pair<Int, Int>, instr: Pair<Char, Int>): Pair<Int, Int> {
        val (x, y) = pos;
        val (dir, l) = instr
        return when (dir) {
            'N' -> x to y - l
            'E' -> x + l to y
            'S' -> x to y + l
            'W' -> x - l to y
            else -> throw IllegalArgumentException()
        }
    }

    private fun instructions(): List<Pair<Char, Int>> {
        var dir = 'N'
        return inputLines().first().split(", ").map {
            it[0] to it.substring(1).toInt()
        }.map {
            dir = dirs[(dirs.indexOf(dir) + rot[it.first]!!) % 4]
            dir to it.second
        }
    }

    override val part1: Int
        get() = instructions().fold(0 to 0) { pos, inst ->
            move(pos, inst)
        }.let { (x, y) ->
            x.absoluteValue + y.absoluteValue
        }

    override val part2: Int
        get() {
            var pos = 0 to 0
            val seen = mutableSetOf<Pair<Int, Int>>()
            return instructions().flatMap { i ->
                generateSequence { i.first to 1 }.take(i.second)
            }.map {
                pos = move(pos, it)
                pos
            }.first {
                !seen.add(it)
            }.let {
                it.first + it.second
            }
        }

}

fun main() {
    println(Day1.part1)
    println(Day1.part2)
}
