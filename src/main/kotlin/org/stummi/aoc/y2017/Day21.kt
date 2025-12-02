package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode
import java.util.BitSet

object Day21 : AdventOfCode(2017, 21) {
    private data class Tile(
        val size: Int,
        val id: Int,
    ) {

        constructor(size: Int, bits: BitSet) : this(size, bits.toLongArray().let {
            if (it.isEmpty()) 0 else it[0].toInt()
        })


        val bits: BitSet
            get() = BitSet.valueOf(longArrayOf(id.toLong()))

        fun rotateRight(): Tile {
            return when (size) {
                2 -> transform(2, 0, 3, 1)
                3 -> transform(6, 3, 0, 7, 4, 1, 8, 5, 2)
                else -> error("wrong size")
            }
        }

        fun flipV(): Tile {
            return when (size) {
                2 -> transform(1, 0, 3, 2)
                3 -> transform(2, 1, 0, 5, 4, 3, 8, 7, 6)
                else -> error("wrong size")
            }
        }

        fun flipH(): Tile {
            return when (size) {
                2 -> transform(2, 3, 0, 1)
                3 -> transform(6, 7, 8, 3, 4, 5, 0, 1, 2)
                else -> error("wrong size")
            }
        }

        private fun transform(vararg pos: Int, newSize: Int = size): Tile {
            require(pos.size == newSize * newSize)
            val dest = BitSet(newSize * newSize)
            val source = bits
            pos.forEachIndexed { d, s ->
                dest[d] = source[s]
            }
            return Tile(newSize, dest)
        }

        fun print() {
            println("- $size/$id -")
            repeat(size) { y ->
               println(row(y))
            }
        }

        private fun row(y: Int): String {
            val sb = StringBuilder()
            repeat(size) { x ->
                sb.append(if (bits[y * size + x]) '#' else '.')
            }
            return sb.toString()
        }

        fun splitIfBig(): List<Tile> {
            return when (size) {
                2, 3 -> listOf(this)
                4 -> listOf(
                    transform(0, 1, 4, 5, newSize = 2),
                    transform(2, 3, 6, 7, newSize = 2),
                    transform(8, 9, 12, 13, newSize = 2),
                    transform(10, 11, 14, 15, newSize = 2)
                )

                else -> error("wrong size")
            }

        }

        companion object {
            fun parse(t: String): Tile {
                val l = t.split("/")
                val s = l.size
                val bits = BitSet.valueOf(LongArray(1))
                l.joinToString("").forEachIndexed { i, c ->
                    if (c == '#') {
                        bits[i] = true
                    }
                }
                val id = if (bits.size() == 0) {
                    0
                } else {
                    bits.toLongArray()[0].toInt()
                }
                return Tile(s, id)
            }
        }
    }

    override val part1: Any
        get() {
            val rules = input.lines.associate {
                it.split(" => ").map(Tile::parse).let { (a, b) -> a to b.splitIfBig() }
            }

            val allRules = rules.toMutableMap()


            rules.forEach { (k, v) ->
                (generateSequence(k.rotateRight()) { it.rotateRight() }.take(4)
                    .flatMap { listOf(it, it.flipH(), it.flipV()) }
                    .toList()
                ).forEach {
                    if (it !in allRules) {
                        allRules[it] = v
                    }
                }
            }

            var state = listOf(Tile.parse(".#./..#/###"))

            repeat(5) {
                state = state.flatMap {
                    val ar = allRules[it]
                    if(ar == null)
                    {
                        it.print()
                        error("asf")
                    }
                    ar
                }
                println(state)

            }

            state.forEach { it.print() }

            return state.sumOf { it.bits.cardinality() }
        }

}

fun main() {
    Day21.fancyRun()
}
