package org.stummi.aoc.y2015

fun main() {

    val containers = Unit.javaClass.getResourceAsStream("/2015/17.txt").use {
        it!!.bufferedReader().readLines()
    }.map {
        it.toInt()
    }.toList()

    //println(containers)

    fillContainers(150, containers).groupingBy { it.size }.eachCount().minByOrNull { it.key }.let { println(it) }

}

fun fillContainers(i: Int, containers: List<Int>, currentContainers: List<Int> = emptyList()): List<List<Int>> {
    if (i < 0) {
        return emptyList()
    }

    if (i == 0) {
        return listOf(currentContainers)
    }

    return containers.flatMapIndexed { idx, v ->
        fillContainers(i - v, containers.drop(idx + 1), currentContainers + v)
    }
}


