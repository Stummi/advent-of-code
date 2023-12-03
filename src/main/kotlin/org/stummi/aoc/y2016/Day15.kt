package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day15 : AdventOfCode(2016, 15) {

    private fun disks() = input().mapIndexed { idx, line ->
        val spl = line.split(" ")
        val positions = spl[3].toInt()
        var curPos = spl.last().removeSuffix(".").toInt()
        positions to curPos
    }

    override val part1: Int
        get() = calculateTime(disks())

    override val part2: Int
        get() = calculateTime(disks() + (11 to 0))


    private fun calculateTime(disks: List<Pair<Int, Int>>): Int {
        var time = 0
        var steps = 1
        var line = 0
        disks.forEachIndexed { idx, (positions, cp) ->
            var curPos = cp
            curPos = (curPos + time + (idx + 1)) % positions
            while (curPos != 0) {
                time += steps
                curPos = (curPos + steps) % positions
            }
            steps *= positions
        }
        return time;
    }
}

fun main() {
    //Day15.inputType = "demo"
    println(Day15.part1)
    println(Day15.part2)
//
}
