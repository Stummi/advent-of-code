package org.stummi.aoc.y2015

fun <T> generatePermutations(values: List<T>): List<List<T>> {
    if (values.isEmpty()) {
        return listOf(emptyList())
    }

    return values.flatMap { v ->
        generatePermutations(values - v).map {
            listOf(v) + it
        }
    }
}

fun main() {
    val distances = Unit.javaClass.getResourceAsStream("/2015/9.txt").use {
        it!!.bufferedReader().readLines()
    }.map {
        it.split(" ")
    }.associate {
        val f = it[0]
        val t = it[2]
        val d = it[4].toInt()
        (f to t) to d
    }

    val locations = distances.keys.flatMap { listOf(it.first, it.second) }.distinct()
    println(generatePermutations(locations).map {
        calculateDistance(it, distances)
    }.maxOrNull())


}

fun calculateDistance(path: List<String>, distances: Map<Pair<String, String>, Int>) =
    (1 until path.size).sumOf {
        val l1 = path[it - 1]
        val l2 = path[it]
        (distances[l1 to l2] ?: distances[l2 to l1])!!
    }


