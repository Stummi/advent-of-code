package org.stummi.aoc.y2015

fun main() {
    val map = Unit.javaClass.getResourceAsStream("/2015/13.txt").use {
        it!!.bufferedReader().readLines()
    }.map {
        it.split(" ")
    }.associate {
        val p1 = it[0]
        val p2 = it[10].trimEnd('.')
        val points = when (it[2]) {
            "gain" -> it[3].toInt()
            "lose" -> -it[3].toInt()
            else -> throw IllegalArgumentException()
        }

        (p1 to p2) to points
    }

    val names = map.keys.map { it.first }.distinct()

    generatePermutations(names + listOf("myself")).map {
        calulcateHappiness(it, map)
    }.maxOrNull().let {
        println(it)
    }
}

fun calulcateHappiness(seating: List<String>, map: Map<Pair<String, String>, Int>): Int {
    return (0 until seating.size).map {
        var p = seating[it]
        listOf(
            seating[(it + 1) % seating.size],
            seating[(it + seating.size - 1) % seating.size]
        ).sumOf {
            map[p to it] ?: 0
        }
    }.sum()

}
