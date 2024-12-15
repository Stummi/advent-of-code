package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import java.math.BigInteger
import java.util.SortedSet

object Day9 : AdventOfCode(2024, 9) {
    init {
        rawSample("2333133121414131402", 1928)
    }

    override val part1: Any
        get() {
            val numbers = input.line.map { it.digitToInt() }.toMutableList()
            var result = BigInteger.ZERO
            var idx = 0
            var dIdx = 0
            var rightDIdx = numbers.size / 2
            while (numbers.isNotEmpty()) {
                val data = numbers.removeFirst()

                repeat(data) {
                    result += ((idx++) * (dIdx)).toBigInteger()
                }
                ++dIdx

                var space = numbers.removeFirstOrNull() ?: break

                while (space > 0) {
                    val rightData = numbers.removeLastOrNull() ?: break
                    if (rightData > space) {
                        numbers.add(rightData - space)
                        repeat(minOf(rightData, space)) {
                            result += ((idx++) * (rightDIdx)).toBigInteger()
                        }
                        space = 0
                    } else {
                        numbers.removeLastOrNull()
                        repeat(minOf(rightData, space)) {
                            result += ((idx++) * (rightDIdx)).toBigInteger()
                        }
                        --rightDIdx
                        space -= rightData
                    }
                }
            }

            return result
        }

    override val part2: Any
        get() {
            data class DataEntry(
                val idx: Int,
                val pos: Int,
                val size: Int,
            )

            val numbers = input.line.map { it.digitToInt() }

            var dIdx = 0
            var pos = 0
            val blocks: MutableList<DataEntry> = sequence {
                numbers.chunked(2).forEach { it ->
                    yield(
                        DataEntry(
                            dIdx++,
                            pos,
                            it[0]
                        )
                    )
                    pos += it[0]
                    it.getOrNull(1)?.let { pos += it }
                }
            }.toMutableList()

            var spaces = blocks.windowed(2).map { (a, b) ->
                (a.pos + a.size) to (b.pos - (a.pos + a.size))
            }

            (blocks.last().idx downTo 0).forEach { i ->
                val b = blocks.find { it.idx == i }!!
                val space = spaces.firstOrNull { s -> s.second >= b.size && s.first < b.pos }
                    ?: return@forEach

                blocks.remove(b)
                blocks.add(b.copy(
                    pos = space.first
                ))
                blocks.sortBy { it.pos }
                spaces = blocks.windowed(2).map { (a, b) ->
                    (a.pos + a.size) to (b.pos - (a.pos + a.size))
                }
            }

            return blocks.flatMap { b ->
                ((b.pos) until (b.pos + b.size)).map { p ->
                    BigInteger.valueOf(p.toLong() * b.idx)
                }
            }.reduce { a, b -> a + b }
        }
}

fun main() {
    Day9.fancyRun()
}
