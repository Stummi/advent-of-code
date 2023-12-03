package org.stummi.aoc.y2015

fun main() {
    val input = Unit.javaClass.getResourceAsStream("/2015/8.txt").use {
        it!!.bufferedReader().readLines()
    }

    var pt1 = input.map {
        it to
                it
                    .substring(1, it.length - 1)
                    .replace(Regex("\\\\[^x]"), "_")
                    .replace(Regex("\\\\x.."), "_")


    }.map { p ->
        p.first.length - p.second.length
    }.sum()

    var pt2 = input.map {
        it.count { c -> c in listOf('"', '\\') } + 2
    }.sum()

    println(pt1)
    println(pt2)
}

