package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode
import java.security.MessageDigest

object Day5 : AdventOfCode(2016, 5) {
    private fun generateHashes(
        input: String,
    ): Sequence<ByteArray> {
        var idx = 0
        val md = MessageDigest.getInstance("MD5")
        return generateSequence { idx++ }.map {
            val code = "$input$idx"
            md.digest(code.toByteArray())
        }.filter { d ->
            d[0] == 0.toByte() &&
                    d[1] == 0.toByte() &&
                    (d[2].toUByte() / 16.toUByte()) == 0.toUInt()
        }
    }

    override val part1: Any
        get() {
            val input = "cxdnnyjw"
            return generateHashes(input).take(8).map {
                it[2].toString(16)
            }.joinToString("")
        }

    override val part2: Any
        get() {
            var password = CharArray(8)
            generateHashes("cxdnnyjw").map {
                val pos = it[2].toInt()
                val char = (it[3].toUByte().toInt() / 16).toString(16)[0]
                pos to char
            }.filter {
                it.first < 8
            }.filter {
                password[it.first] == 0.toChar()
            }.onEach {
                password[it.first] = it.second
            }.takeWhile {
                password.any { it == 0.toChar() }
            }.count()

            return String(password)
        }
}

fun main() {
    println(Day5.part1)
    println(Day5.part2)
}
