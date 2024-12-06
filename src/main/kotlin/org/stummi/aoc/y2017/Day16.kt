package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode
import java.util.*

object Day16 : AdventOfCode(2017, 16) {

    init {
        rawSample("s1", "eabcd", additionalData = "abcde")
        rawSample("s1,x3/4", "eabdc", additionalData = "abcde")
        rawSample("s1,x3/4,pe/b", "baedc", additionalData = "abcde")
    }

    private fun interface DanceMove {
        fun accept(arr: CharArray)
    }

    private fun spin(c: Int) = DanceMove { state ->
        val s = state.size - c
        val tmp = CharArray(c)
        state.copyInto(tmp, startIndex = s)
        state.copyInto(state, c, 0, s)
        tmp.copyInto(state, 0)
    }

    private fun exchange(c1: Int, c2: Int) = DanceMove { state ->
        val tmp = state[c1]
        state[c1] = state[c2]
        state[c2] = tmp
    }

    private fun partner(c1: Char, c2: Char) = DanceMove { state ->
        val p1 = state.indexOf(c1)
        val p2 = state.indexOf(c2)
        state[p1] = c2
        state[p2] = c1
    }

    private val parsedInput: List<DanceMove> by lazy {
        val ret = input.line.split(",").map {
            when (it[0]) {
                's' -> spin(it.drop(1).toInt())
                'x' -> {
                    val (c1, c2) = it.drop(1).split("/").map { it.toInt() }
                    exchange(c1, c2)
                }

                'p' -> {
                    val (c1, c2) = it.drop(1).split("/").map { it[0] }
                    partner(c1, c2)
                }

                else -> throw IllegalArgumentException(it)
            }
        }
        ret
    }

    private val setup by lazy {
        ('a'..'p').toList().toCharArray().concatToString().orSample()
    }

    fun dance(s: String): String {
        val state = s.toCharArray()
        parsedInput.forEach { it.accept(state) }
        return state.concatToString()
    }

    override val part1: Any
        get() {
            return dance(setup)
        }

    override val part2: Any
        get() {
            val str = setup
            val seq =
                listOf(str) + (generateSequence(str) { dance(it) }.drop(1).takeWhile { it != str }.toList())

            val cycle = seq.size

            return seq[1_000_000_000 % cycle]
        }
}

fun main() {
    Day16.fancyRun()
}
