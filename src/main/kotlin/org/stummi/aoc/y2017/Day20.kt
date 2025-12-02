package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.splitToInts
import kotlin.math.absoluteValue

object Day20 : AdventOfCode(2017, 20) {
    private data class XYZ(
        val x: Long,
        val y: Long,
        val z: Long
    ) {
        val vec get() = x.absoluteValue + y.absoluteValue + z.absoluteValue
        operator fun plus(xyz: XYZ) =
            XYZ(x + xyz.x, y + xyz.y, z + xyz.z)

    }

    private data class Particle(
        val id: Int,
        val p: XYZ,
        val v: XYZ,
        val a: XYZ
    ) {
        val nextState: Particle get() {
            val nextVel = v + a
            val nextPos = p + nextVel
            return Particle(id, nextPos, nextVel, a)
        }
    }

    private fun List<Particle>.filterByMinVec(func: (Particle) -> XYZ): List<Particle> {
        val min = minOf { func(it).vec }
        return filter { func(it).vec == min }
    }

    override val part1: Any
        get() = particles.filterByMinVec { it.a }
            .filterByMinVec { it.v }
            .filterByMinVec { it.p }
            .first().id

    override val part2: Any
        get() {
            var p = particles

            repeat(50) {
                val cols = p.groupingBy { it.p }.eachCount().filterValues { it > 1 }.keys
                if(cols.isNotEmpty()) {
                    p = p.filterNot { it.p in cols }
                }
                p = p.map { it.nextState }
            }

            return p.size
        }


    private val particles by lazy {
        input.lines.mapIndexed { i, it ->
            it.splitToInts(true).map { it.toLong() }
                .chunked(3).map { (x, y, z) -> XYZ(x, y, z) }
                .let { (p, v, a) -> Particle(i, p, v, a) }
        }
    }

}

fun main() {
    Day20.fancyRun()
}
