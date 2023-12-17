package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode
import kotlin.math.min

object Day13 : AdventOfCode(2022, 13) {
    override val part1
        get() = inputLines().chunked(3)
            .map { parseLine(it[0]) to parseLine(it[1]) }
            .mapIndexed { idx, pair -> (idx + 1) to pair.first.compareTo(pair.second) }
            .filter { it.second == -1 }
            .sumOf { it.first }

    override val part2: Int
        get() {
            val div1 = ListPacket(mutableListOf(ListPacket(mutableListOf(IntPacket(2)))))
            val div2 = ListPacket(mutableListOf(ListPacket(mutableListOf(IntPacket(6)))))
            val sortedList =
                (inputLines().filter { it.isNotEmpty() }.map { parseLine(it) } + listOf(div1, div2)).sorted()
            return (sortedList.indexOf(div1) + 1) * (sortedList.indexOf(div2) + 1)
        }

    sealed interface PacketData : Comparable<PacketData>

    class ListPacket(val data: List<PacketData>) : PacketData {
        override fun toString(): String {
            return data.toString()
        }

        override fun compareTo(other: PacketData): Int {
            return when (other) {
                is IntPacket -> compareToList(ListPacket(listOf(other)))
                is ListPacket -> compareToList(other)
            }
        }

        fun compareToList(other: ListPacket): Int {
            (0 until min(data.size, other.data.size)).forEach {
                val cmp = this.data[it].compareTo(other.data[it])
                if (cmp != 0) {
                    return cmp
                }
            }

            return data.size.compareTo(other.data.size)
        }
    }

    class IntPacket(val data: Int) : PacketData {
        override fun toString(): String {
            return data.toString()
        }

        override fun compareTo(other: PacketData): Int {
            return when (other) {
                is IntPacket -> data.compareTo(other.data)
                is ListPacket -> ListPacket(listOf(this)).compareTo(other)
            }
        }
    }

    private fun parseLine(s: String): ListPacket {
        val stackOfList = mutableListOf<MutableList<Any>>()
        var lastClosed: MutableList<Any>? = null
        Regex("\\[|\\]|\\d+").findAll(s).forEach {
            when (it.value) {
                "[" -> {
                    val l = mutableListOf<Any>()
                    stackOfList.lastOrNull()?.add(l)
                    stackOfList.add(l)
                }

                "]" -> {
                    lastClosed = stackOfList.removeLast()
                }

                else -> {
                    stackOfList.last().add(IntPacket(it.value.toInt()))
                }
            }
        }
        assert(stackOfList.isEmpty())

        fun convert(i: Any): PacketData = when (i) {
            is IntPacket -> i
            is Int -> IntPacket(i)
            is List<*> -> ListPacket(i.map { convert(it!!) })
            else -> throw IllegalArgumentException(i.javaClass.toString())
        }

        return convert(lastClosed!!) as ListPacket
    }
}

fun main() {
    Day13.fancyRun()
}
