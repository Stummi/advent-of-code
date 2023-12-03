package org.stummi.aoc.y2015

fun main() {

    val sues = Unit.javaClass.getResourceAsStream("/2015/16.txt").use {
        it!!.bufferedReader().readLines()
    }.map {
        it.split(" ")
    }.associate {
        val c = it.chunked(2)
        val number = c[0][1].trimEnd(':').toInt()
        val m = c.drop(1).associate {
            it[0].trimEnd(':') to it[1].trimEnd(',').toInt()
        }
        number to m
    }

    fun eq(i: Int): (Int) -> Boolean = { it == i }
    fun gt(i: Int): (Int) -> Boolean = { it > i }
    fun lt(i: Int): (Int) -> Boolean = { it < i }

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

    sues.filterValues {
        it.all { (k, v) -> hints[k]!!(v) }
    }.forEach {
        println(it)
    }


}
