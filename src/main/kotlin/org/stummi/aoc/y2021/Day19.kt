package org.stummi.aoc.y2021

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.tuplePermutations
import kotlin.math.absoluteValue

object Day19 : AdventOfCode(2021, 19) {
    val rotations = listOf<XYZ.() -> XYZ>(
        { this }, // == XYZ(x, y, z)
        { XYZ(x, z, -y) },
        { XYZ(x, -y, -z) },
        { XYZ(x, -z, y) },

        { XYZ(-y, x, z) },
        { XYZ(-z, x, -y) },
        { XYZ(y, x, -z) },
        { XYZ(z, x, y) },

        { XYZ(-z, y, x) },
        { XYZ(-y, -z, x) },
        { XYZ(z, -y, x) },
        { XYZ(y, z, x) },

        { XYZ(-x, y, -z) },
        { XYZ(-x, -z, -y) },
        { XYZ(-x, -y, z) },
        { XYZ(-x, z, y) },

        { XYZ(-y, -x, -z) },
        { XYZ(z, -x, -y) },
        { XYZ(y, -x, z) },
        { XYZ(-z, -x, y) },

        { XYZ(z, y, -x) },
        { XYZ(-y, z, -x) },
        { XYZ(-z, -y, -x) },
        { XYZ(y, -z, -x) },
    )

    data class XYZ(
        val x: Int,
        val y: Int,
        val z: Int,
    ) : Comparable<XYZ> {
        companion object {
            val comparator = compareBy<XYZ> { it.x }.thenBy { it.y }.thenBy { it.z }
        }

        override fun compareTo(other: XYZ): Int =
            comparator.compare(this, other)

        operator fun minus(sig1: XYZ) =
            XYZ(
                this.x - sig1.x,
                this.y - sig1.y,
                this.z - sig1.z
            )

        operator fun plus(sig1: XYZ) =
            XYZ(
                this.x + sig1.x,
                this.y + sig1.y,
                this.z + sig1.z
            )


        override fun toString(): String = "{$x,$y,$z}"
        fun manhattenDistanceTo(pos: XYZ) =
            (this - pos).let { listOf(it.x, it.y, it.z) }.map { it.absoluteValue }.sum()
    }

    data class Beacon(
        val index: Int,
        val pos: XYZ = XYZ(0, 0, 0)
    ) {
        fun translate(diff: XYZ) = Beacon(index, pos + diff)

        override fun toString() = "$index$pos"
    }

    data class SignalMap private constructor(
        val beacons: List<Beacon>,
        val signals: List<XYZ>,
        val internalOriginal: SignalMap?,
    ) {
        constructor(
            beacons: List<Beacon>, signals: List<XYZ>
        ) : this(beacons, signals, null)

        val original get() = internalOriginal ?: this

        companion object {
            private fun calculateSignalDistances(signals: List<XYZ>): List<List<XYZ>> =
                signals.map { a -> signals.map { b -> a - b } }
        }

        val signalDistances by lazy { calculateSignalDistances(signals) }
        val allRotations: List<SignalMap> by lazy {
            (internalOriginal?.allRotations) ?: rotations.map { rotate(it) }
        }

        fun rotate(function: XYZ.() -> XYZ) = SignalMap(
            beacons.map { Beacon(it.index, function(it.pos)) },
            signals.map(function),
            original
        )


        // lets do eq and hashCode only by beacons

        override fun equals(other: Any?): Boolean = (other as? SignalMap)?.beacons?.equals(this.beacons) ?: false

        override fun hashCode(): Int =
            beacons.hashCode()

        fun translate(diff: XYZ) = SignalMap(
            beacons.map { it.translate(diff) },
            signals.map { it + diff }.sorted(),
            original
        )

        fun merge(m: SignalMap) = SignalMap(
            this.beacons + m.beacons,
            (this.signals + m.signals).distinct().sorted()
        )

        fun signalDistancesAsSequence() = signalDistances.asSequence().flatMapIndexed { i1, l ->
            l.asSequence().mapIndexed { i2, p -> (i1 to i2) to p }
        }

    }


    override val part1: Int
        get() = solve.signals.size


    override val part2: Int
        get() =
            solve.beacons.tuplePermutations(false).maxOf { (a, b) ->
                a.pos.manhattenDistanceTo(b.pos)
            }

    val solve: SignalMap by lazy {
        var starMaps = inputData.toSet()

        var it = 0
        while (starMaps.size > 1) {
            val nextMap = starMaps.toMutableSet()
            val rem = starMaps.toMutableSet()
            starMaps.forEach {
                if (it !in rem) {
                    return@forEach
                }
                rem.remove(it)
                val m = findMatch(it, rem)
                if (m != null) {
                    val merged = it.merge(m)
                    check(nextMap.remove(it))
                    check(nextMap.remove(m.original))
                    check(rem.remove(m.original))
                    nextMap.add(merged)
                }
            }
            starMaps = nextMap
        }
        starMaps.single()
    }

    internal fun findMatch(signalMap: SignalMap, candidates: Set<SignalMap>): SignalMap? {
        candidates.flatMap { it.allRotations }.forEach { candidate ->
            if (candidate.signalDistances.any { candSignalDistance ->
                    signalMap.signalDistances.any { thisSignalDistance ->
                        candSignalDistance.count { it in thisSignalDistance } >= 12
                    }
                }) {
                // println("here")
                signalMap.signalDistances.forEachIndexed { sdi, sd ->
                    val bestMatch = candidate.signalDistances.mapIndexed { idx, l ->
                        (l to idx) to l.count { it in sd }
                    }.filter { it.second >= 12 }.maxByOrNull { it.second }
                    if (bestMatch != null) {
                        val indexInCandidate = bestMatch.first.second
                        val sig1 = signalMap.signals[sdi]
                        val sig2 = candidate.signals[indexInCandidate]
                        val diff = sig1 - sig2
                        val transl = candidate.translate(diff)
                        val matchCount = transl.signals.count { it in signalMap.signals }
                        //  println("here2 $matchCount")
                        if (matchCount >= 12) {
                            return transl
                        }
                    }
                }
            }
        }
        return null
    }

    val inputData = inputLines().let { i ->
        (i.mapIndexedNotNull { index, s -> if (s.isBlank()) index else null }
                + listOf(-1, i.size)).sorted().windowed(2).map {
            i.subList(it[0] + 1, it[1])
        }
    }.map {
        it[0].split(" ")[2].toInt() to
                it.drop(1).map {
                    it.split(",").map { it.toInt() }.let {
                        XYZ(it[0], it[1], it[2])
                    }
                }.sorted()
    }.map {
        SignalMap(beacons = listOf(Beacon(it.first)), signals = it.second)
    }
}

fun main() {
    println(Day19.part1)
    println(Day19.part2)
}
