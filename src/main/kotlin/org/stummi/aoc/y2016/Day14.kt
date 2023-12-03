package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.md5String

object Day14 : AdventOfCode(2016, 14) {
    override val part1
        get() = keys().take(64).last().first

    override val part2
        get() = keys(2017).take(64).last().first

    private fun keys(rounds: Int = 1) = sequence {

        val salt = input()[0]
        val hashes = (0..1000).map { hash(salt, it, rounds) }.toMutableList()
        var i = 0
        while (true) {
            i += 1
            hashes.add(hash(salt, (i + 1000), rounds))
            val triple = getTriple(hashes[i])
            if (triple != null) {
                val quintuple = generateSequence { triple }.take(5).joinToString("")
                if ((i + 1..i + 1000).any { hashes[it].contains(quintuple) }) {
                    yield(i to hashes[i])
                }
            }
        }
    }

    fun hash(salt: String, it: Int, rounds: Int) =
        generateSequence("$salt$it") { md5String(it) }.take(rounds + 1).last()

    private fun getTriple(md5Sum: String): Char? {
        (0 until md5Sum.length - 2).forEach {
            if (md5Sum[it] == md5Sum[it + 1] && md5Sum[it] == md5Sum[it + 2]) {
                return md5Sum[it]
            }
        }
        return null;
    }
}

fun main() {
    println(Day14.hash("abc", 0, 1))
    println(Day14.hash("abc", 0, 2017))

    Day14.fancyRun()
}
