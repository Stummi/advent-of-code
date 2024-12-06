package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode
import java.security.MessageDigest
import kotlin.experimental.and

object Day4 : AdventOfCode(2015, 4) {

    override val part1: Any
        get() = solve {
            val ZERO = 0.toByte()
            val XF0 = 0xF0.toByte()
            it[0] == ZERO && it[1] == ZERO && (it[2] and XF0)  == ZERO
        }

    override val part2: Any
        get() = solve {
            val ZERO = 0.toByte()
            it[0] == ZERO && it[1] == ZERO && it[2] == ZERO
        }

    fun solve(func: (ByteArray) -> Boolean): Int {
        val md5 = MessageDigest.getInstance("MD5")
        val prefix = inputLines().first()
        var idx = 0
        while (true) {
            val check = md5.digest("$prefix$idx".toByteArray())
            if (func(check)) {
                return idx
            }
            ++idx
        }
    }


}

fun main() {
    Day4.fancyRun()
}
