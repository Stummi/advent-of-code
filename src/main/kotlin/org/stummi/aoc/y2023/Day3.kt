package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.XYRange

object Day3 : AdventOfCode(2023, 3) {
    override val part1: Any
        get() {
            val (numbersWithPos, symbolsWithPos) = parseInput()

            return numbersWithPos.filter { (pos, number) ->
                val numberLength = number.toString().length
                val topLeft = pos.move(-1, -1)
                val bottomRight = pos.move(numberLength, 1)

                XYRange(topLeft, bottomRight).outLineAsSequence().any {
                    symbolsWithPos.any { (symbolPos, symbol) -> symbolPos == it }
                }
            }.sumOf { (_, number) -> number }
        }

    override val part2: Any
        get() {
            val (numbersWithPos, symbolsWithPos) = parseInput()

            val gearsToNumbers =
                symbolsWithPos.filter { (_, symbol) -> symbol == '*' }.map { (pos, _) -> pos }.associateWith { _ ->
                    mutableListOf<Int>()
                }

            numbersWithPos.forEach { (pos, number) ->
                val numberLength = number.toString().length
                val topLeft = pos.move(-1, -1)
                val bottomRight = pos.move(numberLength, 1)

                XYRange(topLeft, bottomRight).outLineAsSequence()
                    .filter { it in gearsToNumbers.keys }
                    .forEach { gearsToNumbers[it]!!.add(number) }

            }

            return gearsToNumbers.entries.filter { it.value.size == 2 }.map {
                it.value.reduce(Int::times)
            }.sum()
        }


    private fun parseInput(): Pair<List<Pair<XY, Int>>, List<Pair<XY, Char>>> {
        val numberRegex = Regex("\\d+")

        val numbersWithPos = mutableListOf<Pair<XY, Int>>()
        val symbolsWithPos = mutableListOf<Pair<XY, Char>>()

        input().forEachIndexed { y, line ->
            numberRegex.findAll(line).forEach { match ->
                val x = match.range.first
                val number = match.value.toInt()
                numbersWithPos.add(XY(x, y) to number)
            }

            line.forEachIndexed { x, c ->
                val isSymbol = !c.isDigit() && c != '.'
                if (isSymbol) {
                    symbolsWithPos.add(XY(x, y) to c)
                }
            }
        }
        return Pair(numbersWithPos, symbolsWithPos)
    }

}

fun main() {
    Day3.fancyRun()
}
