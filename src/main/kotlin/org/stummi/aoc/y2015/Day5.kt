package org.stummi.aoc.y2015

fun main() {
    val count = Unit.javaClass.getResourceAsStream("/2015/5.txt").use {
        it!!.bufferedReader().readLines()
    }.count { it.isNice2() }

    println(count)
}

val vowels = "aeiou".toList()

val forbiddenStrings = listOf("ab", "cd", "pq", "xy")


fun String.isNice(): Boolean =
    this.count { it in vowels } >= 3 &&
            ((1 until this.length).any { idx ->
                this[idx - 1] == this[idx]
            }) &&
            forbiddenStrings.none { it in this }

fun String.isNice2(): Boolean =
    ((2 until this.length).any { idx ->
        this.indexOf(this.substring(idx - 2, idx), idx) >= 0
    }) &&
            ((2 until this.length).any { idx ->
                this[idx - 2] == this[idx]
            })

