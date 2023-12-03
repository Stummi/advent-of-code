package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.merged
import kotlin.math.absoluteValue

object Day15 : AdventOfCode(2022, 15) {

    init {
        resourceSample("demo", 26, 56000011L, additionalData = 10)
    }

    override val part1: Int
        get() {
            val sensors = readSensors()
            val beacons = sensors.map { it.beaconLocation }.toSet()
            val y = 2000000.orSample()
            val ranges = sensors.map {
                it.coverYSlice(y)
            }.filterNot { it.isEmpty() }.toList().merged()

            val from = ranges.minOf { it.first }
            val to = ranges.maxOf { it.last }

            return (from..to).count { x ->
                ranges.any { x in it }
            } - beacons.count { it.y == y }
        }

    override val part2: Long
        get() {
            val sensors = readSensors()
            val beacons = sensors.map { it.beaconLocation }.toSet()
            val maxY = (2000000.orSample() * 2)

            (0..maxY).forEach { y ->
                val ranges = sensors.map {
                    it.coverYSlice(y)
                }.filterNot { it.isEmpty() }.toList().merged()

                if (ranges.size != 1) {
                    val x = ranges[0].last + 1
                    return x * 4000000L + y
                }

            }
            return 0;
        }

    data class Sensor(val sensorLocation: XY, val beaconLocation: XY) {
        val area = sensorLocation.orthogonalDistanceTo(beaconLocation)

        fun covers(xy: XY): Boolean {
            return sensorLocation.orthogonalDistanceTo(xy) <= area
        }

        fun coverYSlice(y: Int): IntRange {
            val yDiff = (sensorLocation.y - y).absoluteValue
            if (yDiff > area) {
                return IntRange.EMPTY
            }
            val xRange = area - yDiff
            val x = sensorLocation.x
            return (x - xRange)..(x + xRange)
        }
    }

    private fun readSensors() =
        input().map {
            val spl = it.split(" ")
            val x1 = spl[2].removePrefix("x=").removeSuffix(",").toInt()
            val y1 = spl[3].removePrefix("y=").removeSuffix(":").toInt()
            val x2 = spl[8].removePrefix("x=").removeSuffix(",").toInt()
            val y2 = spl[9].removePrefix("y=").toInt()

            val xy1 = XY(x1, y1)
            val xy2 = XY(x2, y2)
            Sensor(xy1, xy2)
        }

}

fun main() {
    Day15.fancyRun()
}
