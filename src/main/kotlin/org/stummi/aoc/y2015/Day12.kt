package org.stummi.aoc.y2015

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.stummi.aoc.AdventOfCode

object Day12 : AdventOfCode(2015, 12) {
    fun parsedInput() = jacksonObjectMapper().readValue<Map<String, Any>>(inputLines().joinToString(""))

    override val part1: Any
        get() = sumOfValues(parsedInput(), mapFilter = { false })

    override val part2: Any
        get() = sumOfValues(parsedInput(), mapFilter = { "red" in it.values })

    fun sumOfValues(inp: Any, mapFilter: (Map<*, *>) -> Boolean): Int {
        return when {
            inp is Int -> inp
            inp is List<*> -> inp.sumOf { sumOfValues(it!!, mapFilter) }
            inp is Map<*, *> -> if (mapFilter(inp)) 0 else inp.values.sumOf { sumOfValues(it!!, mapFilter) }

            inp is String -> 0
            else -> throw IllegalArgumentException(inp.javaClass.toString())
        }
    }

}

fun main() {
    Day12.fancyRun()
}
