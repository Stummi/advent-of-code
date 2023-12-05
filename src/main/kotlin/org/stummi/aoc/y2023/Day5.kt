package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.partitionBy

object Day5 : AdventOfCode(2023, 5) {

    private data class RangeMap(
        val destRangeStart: Long,
        val sourceRangeStart: Long,
        val rangeLength: Long,
    ) {
        val sourceRange get() = sourceRangeStart until (sourceRangeStart + rangeLength)
        fun isInSourceRange(pos: Long) = pos in sourceRange

        fun translate(pos: Long) = destRangeStart + (pos - sourceRangeStart)
        fun translate(range: LongRange) = translate(range.first)..translate(range.last)
    }

    private data class Input(
        val seeds: List<Long>,
        val maps: Map<Pair<String, String>, List<RangeMap>>
    )

    private val parsedInput by lazy {
        val inp = input()
        val seeds = inp.first().split(" ").drop(1).map(String::toLong)
        val maps = inp.drop(2).partitionBy { it.isBlank() }.map {
            val (from, _, to) = it.first().split(" ")[0].split("-")
            val ranges = it.drop(1).map {
                val (dStart, sStart, rLen) = it.split(" ").map(String::toLong)
                RangeMap(dStart, sStart, rLen)
            }
            (from to to) to fillEmpties(ranges)
        }.toMap()
        Input(seeds, maps)
    }

    private fun fillEmpties(ranges: List<RangeMap>): List<RangeMap> {
        val sorted = ranges.sortedBy { it.sourceRangeStart }
        var lastKnown = 0L
        return sequence {
            sorted.forEach {
                if (it.sourceRangeStart > lastKnown) {
                    yield(RangeMap(lastKnown, lastKnown, it.sourceRangeStart - lastKnown))
                }
                lastKnown = it.sourceRange.last + 1
                yield(it)
            }
            if (lastKnown < Long.MAX_VALUE) {
                yield(RangeMap(lastKnown, lastKnown, Long.MAX_VALUE - lastKnown))
            }
        }.toList()
    }

    private fun resolveIndex(type: String, index: Long): Pair<String, Long> {
        val (pair, rangeMaps) = parsedInput.maps.entries.find { (k, v) -> k.first == type }!!
        val (_, to) = pair
        return rangeMaps.find { it.isInSourceRange(index) }?.let { rangeMap ->
            val pos = index - rangeMap.sourceRangeStart
            val destPos = rangeMap.destRangeStart + pos
            to to destPos
        } ?: (to to index)
    }

    private fun fullResolve(type: String, index: Long, goal: String): Long {
        val (newType, newIndex) = resolveIndex(type, index)
        return if (newType == goal) {
            newIndex
        } else {
            fullResolve(newType, newIndex, goal)
        }
    }

    fun resolveRange(type: String, range: LongRange): Pair<String, List<LongRange>> {
        val (pair, rangeMaps) = parsedInput.maps.entries.find { (k, v) -> k.first == type }!!
        val (_, to) = pair
        return to to rangeMaps.map {
            it to intersectRange(it.sourceRange, range)
        }.filterNot {
            it.second.isEmpty()
        }.map { (rangeMap, range) ->
            rangeMap.translate(range)
        }
    }

    fun fullResolveRange(type: String, ranges: List<LongRange>, goal: String): List<LongRange> {
        if (type == goal) {
            return ranges
        }

        return ranges.map {
            val (newType, newRanges) = resolveRange(type, it)
            fullResolveRange(newType, newRanges, goal)
        }.flatten()
    }

    fun intersectRange(first: LongRange, second: LongRange): LongRange {
        return maxOf(first.first, second.first)..minOf(first.last, second.last)
    }

    override val part1: Any
        get() =
            parsedInput.seeds.map {
                fullResolve("seed", it, "location")
            }.min()

    override val part2: Any
        get() {
            return parsedInput.seeds.chunked(2).map { (a, b) -> a until (a + b) }.map {
                fullResolveRange("seed", listOf(it), "location")
            }.flatten().map {
                it.first
            }.min()
        }
}

fun main() {
    Day5.fancyRun()
}
