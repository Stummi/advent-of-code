package org.stummi.aoc.y2015

fun main() {
    var santas = 2

    var houses = mutableMapOf(
        Pair(0, 0) to santas
    )

    var santaPositions = generateSequence { 0 to 0 }.take(santas).toMutableList()

    val input = Unit.javaClass.getResourceAsStream("/2015/3.txt")!!.use {
        it.readAllBytes().map { it.toInt().toChar() }
    }.forEachIndexed { santaId, it ->
        var (xPos, yPos) = santaPositions[santaId % santas]

        when (it) {
            '>' -> ++xPos
            '<' -> --xPos
            '^' -> --yPos
            'v' -> ++yPos
            else -> throw IllegalStateException()
        }

        val newPos = xPos to yPos
        santaPositions[santaId % santas] = newPos

        houses[newPos] = (houses[newPos] ?: 0) + 1
    }

    println(houses.count())
}
