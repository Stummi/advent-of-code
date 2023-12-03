package org.stummi.aoc.y2015

import kotlin.math.max

data class LightCmd(val cmd: String, val start: Pair<Int, Int>, val end: Pair<Int, Int>)

fun main() {
    val matrix = IntArray(1000 * 1000)

    Unit.javaClass.getResourceAsStream("/2015/6.txt").use {
        it!!.bufferedReader().readLines()
    }.map {
        it.split(" ").let {
            if (it.size == 4) {
                it
            } else {
                it.drop(1)
            }
        }
    }.map {
        LightCmd(it[0], parsePoint(it[1]), parsePoint(it[3]))
    }.forEach {
        val sx = it.start.first
        val ex = it.end.first
        val sy = it.start.second
        val ey = it.end.second
        (sx..ex).forEach { x ->
            (sy..ey).forEach { y ->
                val pos = x * 1000 + y
                // println(pos)
                when (it.cmd) {
                    "on" -> matrix[pos]++
                    "off" -> matrix[pos] = max(matrix[pos] - 1, 0)
                    "toggle" -> matrix[pos] += 2
                    else -> throw IllegalArgumentException()
                }
            }
        }
        //  println(it)

    }

    println(matrix.sum())
}

fun parsePoint(s: String) = s.split(",").let {
    it[0].toInt() to it[1].toInt()
}
