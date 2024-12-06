package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.BoolMatrix
import org.stummi.aoc.helper.XY
import java.math.BigInteger
import java.util.*
import kotlin.experimental.and

object Day14 : AdventOfCode(2017, 14) {
    init {
        rawSample("flqrgnkx", 8108)
    }

    override val part1: Any
        get() {
            return bigIntInput.bitCount()
        }

    override val part2: Any
        get() {
            val arr = bigIntInput.toByteArray().copyOfRange(1, 2049)

            (0..2047).forEach {
                arr[it] = reversed(arr[it])
            }

            val m = BoolMatrix(
                XY(0, 0)..XY(127, 127),
                BitSet.valueOf(arr)
            ).findAll(true).toMutableSet()

            var groups = 0

            while(m.isNotEmpty()) {
                var remList = mutableListOf(m.first())
                while(remList.isNotEmpty()) {
                    val xy = remList.removeFirst()
                    if(m.remove(xy)) {
                        remList.addAll(xy.orthogonalNeighbours())
                    }
                }
                ++groups
            }

            return groups
        }

    private fun reversed(uByte: Byte): Byte {
        var b = uByte.toUByte().toInt()
        b = (b and 0xF0) shr 4 or ((b and 0x0F) shl 4)
        b = (b and 0xCC) shr 2 or ((b and 0x33) shl 2)
        b = (b and 0xAA) shr 1 or ((b and 0x55) shl 1)
        return b.toByte()
    }

    private val hashes by lazy {
        val inp = input.line
        (0..127).map {
            Day10.knotHash("$inp-$it")
        }
    }

    private val bigIntInput: BigInteger by lazy {
        hashes.joinToString("").let {
            BigInteger(it, 16)
        }
    }
}


fun main() {
    Day14.fancyRun()
}
