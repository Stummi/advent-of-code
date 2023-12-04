package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode

object Day16 : AdventOfCode(2015, 16) {
    fun parsedInput() = input().map {
        it.split(" ")
    }.associate {
        val c = it.chunked(2)
        val number = c[0][1].trimEnd(':').toInt()
        val m = c.drop(1).associate {
            it[0].trimEnd(':') to it[1].trimEnd(',').toInt()
        }
        number to m
    }

    val eqOp: (Int, Int) -> Boolean = { i1, i2 -> i1 == i2 }
    val gtOp: (Int, Int) -> Boolean = { i1, i2 -> i1 > i2 }
    val ltOp: (Int, Int) -> Boolean = { i1, i2 -> i1 < i2 }

    val hints = mapOf(
        "children" to eq(3),
        "cats" to gt(7),
        "samoyeds" to eq(2),
        "pomeranians" to lt(3),
        "akitas" to eq(0),
        "vizslas" to eq(0),
        "goldfish" to lt(5),
        "trees" to gt(3),
        "cars" to eq(2),
        "perfumes" to eq(1),
    )

    fun eq(i: Int) = eqOp to i
    fun gt(i: Int) = gtOp to i
    fun lt(i: Int) = ltOp to i

    override val part1: Any
        get() = parsedInput().filter { (idx, it) ->
            it.all { (k, v) ->
                val (_, i) = hints[k]!!
                v == i
            }
        }.keys.first()
    override val part2: Any
        get() = parsedInput().filter { (idx, it) ->
            it.all { (k, v) ->
                val (op, i) = hints[k]!!
                op(v, i)
            }
        }.keys.first()

}


fun main() {
    Day16.fancyRun()
}
