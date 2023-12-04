package org.stummi.aoc

import org.stummi.aoc.y2015.aoc2015
import org.stummi.aoc.y2016.aoc2016
import org.stummi.aoc.y2017.aoc2017
import org.stummi.aoc.y2022.aoc2022

val allYears = listOf(
    aoc2015,
    aoc2016,
    aoc2017,
    aoc2022,
)

fun main() {
    allYears.flatten().runAll()
}
