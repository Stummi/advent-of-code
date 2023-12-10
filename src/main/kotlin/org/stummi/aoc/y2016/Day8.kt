package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.IntMatrix
import org.stummi.aoc.helper.XY

object Day8 : AdventOfCode(2016, 8) {
    override val part1: Int
        get() =
            resolve().values.count { it == 1 }

    private fun resolve(): IntMatrix {
        val matrix = IntMatrix(50, 6)
        inputLines().map {
            it.split(" ")
        }.forEach {
            when (it.first()) {
                "rect" -> {
                    var (w, h) = it[1].split("x").let { it[0].toInt() to it[1].toInt() }
                    (0 until w).flatMap { x ->
                        (0 until h).map { y -> x to y }
                    }.forEach { (x, y) ->
                        matrix[XY(x, y)] = 1
                    }
                }

                "rotate" -> {
                    val rc = it[1]
                    val idx = it[2].split("=")[1].toInt()
                    val amount = it[4].toInt()
                    val (getter, setter) = when (rc) {
                        "row" ->
                            (matrix::row to matrix::setRow)

                        "column" ->
                            (matrix::col to matrix::setCol)

                        else ->
                            throw IllegalArgumentException(rc)
                    }

                    val data = getter(idx).toList()
                    val newData = data.subList(data.size - amount, data.size) + data.subList(0, data.size - amount)
                    setter(idx, newData)
                }

                else -> throw IllegalArgumentException(it.first())
            }
        }
        return matrix
    }

    override val part2: String
        get() =
            resolve().let { matrixToString(it) }

    private fun matrixToString(matrix: IntMatrix) =
        matrix.rows().map {
            it.chunked(5).map { it.joinToString("") { if (it == 0) "  " else "██" } }
                .joinToString(" ")

        }.joinToString("\n")

}

fun main() {
    println(Day8.part1)
    println(Day8.part2)
}
