package org.stummi.aoc.y2015

data class Reindeer(
    val name: String,
    val speed: Int,
    val time: Int,
    val resting: Int,
) {
    fun progress(i: Int): Int {
        val fullCycles = i / cycle
        val cur = i % cycle
        val flyTime = fullCycles * time + cur.coerceAtMost(time)
        return flyTime * speed
    }

    val cycle: Int
        get() = time + resting
}

fun main() {
    val reindeers = Unit.javaClass.getResourceAsStream("/2015/14.txt").use {
        it!!.bufferedReader().readLines()
    }.map {
        it.split(" ")
    }.map {
        val name = it[0]
        val speed = it[3].toInt()
        val time = it[6].toInt()
        val resting = it[13].toInt()
        Reindeer(name, speed, time, resting)
    }

    var points = reindeers.associate { it.name to 0 }.toMutableMap()

    (1..2503).forEach { s ->
        val progMap = reindeers.associate { it.name to it.progress(s) }
        val max = progMap.values.maxOrNull()!!
        progMap.filterValues { it == max }.keys.forEach {
            points[it] = points[it]!! + 1
        }
    }

    println(points.maxByOrNull { it.value })


}
