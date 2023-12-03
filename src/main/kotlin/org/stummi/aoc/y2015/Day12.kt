package org.stummi.aoc.y2015

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

fun main() {
    val map = Unit.javaClass.getResourceAsStream("/2015/12.txt").use {
        jacksonObjectMapper().readValue<Map<String, Any>>(it)
    }

    println(sumOfValues(map))
}

fun sumOfValues(inp: Any): Int {
    return when {
        inp is Int -> inp
        inp is List<*> -> inp.map { sumOfValues(it!!) }.sum()
        inp is Map<*, *> -> {
            if ("red" in inp.values) {
                0
            } else {
                inp.values.map { sumOfValues(it!!) }.sum()
            }

        }

        inp is String -> 0
        else -> throw IllegalArgumentException(inp.javaClass.toString())
    }

}
