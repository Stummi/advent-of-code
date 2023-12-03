package org.stummi.aoc.y2015

fun main() {
    var max_houses = 3_400_000
    var houses = IntArray(max_houses + 1)

    (1..max_houses).forEach { e ->
        deliver(e, houses)
    }

    houses.indexOfFirst { it > 34_000_000 }.let { println(it) }
}

fun deliver(e: Int, houses: IntArray) {
    repeat(50) {
        val idx = e * (it + 1)
        if (idx >= houses.size) {
            return
        }
        houses[idx] += e * 11
    }
}

