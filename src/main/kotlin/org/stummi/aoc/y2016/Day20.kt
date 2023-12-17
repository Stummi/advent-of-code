package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day20 : AdventOfCode(2016, 20) {
    init {
        resourceSample("demo", 3L, 2L, additionalData = 9L)
    }

    override val part1: Long
        get() {
            val ranges = readRanges()
            var candidate = 0L
            while (true) {
                val r = ranges.find { candidate in it } ?: return candidate
                candidate = r.last + 1
            }
        }

    override val part2: Long
        get() {
            val l1 = readRanges().sortedBy { it.first }

            var currentRange: LongRange = l1.first()


            val l2 = sequence {
                l1.drop(1).forEach {
                    if (it.first <= currentRange.last)
                        currentRange = currentRange.first..kotlin.math.max(it.last, currentRange.last)
                    else {
                        yield(currentRange)
                        currentRange = it
                    }
                }
                yield(currentRange)
            }.toList()

            return (l2.first().first + 0xFFFFFFFFL.orSample() - l2.last().last + l2.windowed(2)
                .map { it[1].first - it[0].last - 1 }.sum())
            /*     val ranges = readRanges()
                 var lastNumber = 0xFFFFFFFFL.orSample()
                 var freeNumbers = 0L
                 var candidate=0L

                 while(true) {
                     val nextRange = ranges.filter { it.last >= candidate }.minByOrNull { it.first }
                         ?: return (freeNumbers + lastNumber - candidate)
                     freeNumbers += nextRange.first - candidate
                     var endRange = nextRange
                     while (true) {
                         endRange = ranges.filter { endRange.last+1 in it }.maxByOrNull { it.last } ?: break
                     }
                     candidate = endRange.last + 1
                 }

         */
        }

    private fun readRanges() = inputLines().map {
        it.split("-").let { it[0].toLong()..it[1].toLong() }
    }

}

fun main() {
    Day20.fancyRun()
}
