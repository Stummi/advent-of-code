package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode

object Day9 : AdventOfCode(2017, 9) {
    init {
        rawSample("{}", 1)
        rawSample("{{{}}}", 6)
        rawSample("{{},{}}", 5)
        rawSample("{{{},{},{{}}}}", 16)
        rawSample("{<a>,<a>,<a>,<a>}", 1, 4)
        rawSample("{{<ab>},{<ab>},{<ab>},{<ab>}}", 9, 8)
        rawSample("{{<!!>},{<!!>},{<!!>},{<!!>}}", 9, 0)
        rawSample("{{<a!>},{<a!>},{<a!>},{<ab>}}", 3, 17)
    }

    private interface Package {
        val garbageLength: Int
        fun score(level: Int): Int
    }

    private data class Garbage(val content: String) : Package {
        override fun score(level: Int): Int = 0

        override val garbageLength: Int
            get() = content.length

    }

    private data class Group(val content: List<Package>) : Package {
        override fun score(level: Int): Int = level + content.sumOf { it.score(level + 1) }

        override val garbageLength: Int
            get() = content.sumOf { it.garbageLength }
    }

    private val parseInput by lazy {
        val it = input.lines.first().iterator()
        check(it.nextChar() == '{')
        consumeGroup(it).also { _ ->
            check(!it.hasNext())
        }
    }

    private fun consumeGroup(it: CharIterator): Group {
        val content = mutableListOf<Package>()
        while (true) {
            when (val c = it.nextChar()) {
                ',' -> {}
                '<' -> content.add(consumeGarbage(it))
                '{' -> content.add(consumeGroup(it))
                '}' -> return Group(content)
                else -> throw IllegalStateException("char: $c")
            }

        }
    }

    private fun consumeGarbage(it: CharIterator): Garbage {
        val content = StringBuilder()
        while (true) {
            when (val next = it.next()) {
                '!' -> it.nextChar()
                '>' -> return Garbage(content.toString())
                else -> content.append(next)
            }
        }
    }

    override val part1: Any
        get() = parseInput.score(1)

    override val part2: Any
        get() = parseInput.garbageLength
}

fun main() {
    Day9.fancyRun()
}
