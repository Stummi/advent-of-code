package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day4 : AdventOfCode(2016, 4) {

    data class Room(
        val name: String,
        val id: Int,
        val checksum: String,
    ) {
        fun realChecksum(): String = name.filter {
            it != '-'
        }.groupingBy { it }.eachCount().entries.sortedByDescending { it.value * 1000 - it.key.code }.take(5)
            .map { it.key }.joinToString("")

        fun decryptName(): String {
            return name.map {
                if (it == '-') ' ' else
                    (((it.code - 'a'.code) + id) % (('z'.code - 'a'.code) + 1) + 'a'.code).toChar()
            }.joinToString("")
        }
    }

    override val part1: Int
        get() {
            return realRooms().sumOf { it.id }
        }

    private fun realRooms() = inputLines().map {
        val lastDash = it.lastIndexOf("-")
        val roomName = it.substring(0, lastDash)
        val (id: Int, checksum: String) = it.substring(lastDash + 1).split("[")
            .let { it[0].toInt() to it[1].trimEnd(']') }
        Room(roomName, id, checksum)
    }.filter {
        it.checksum == it.realChecksum()
    }

    override val part2: Int
        get() =
            realRooms().filter { it.decryptName().contains("northpole") }.first().id

}

fun main() {
    println(Day4.part1)
    println(Day4.part2)
}
