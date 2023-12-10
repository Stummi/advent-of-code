package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY

object Day9 : AdventOfCode(2022, 9) {
    init {
        resourceSample("demo", result1 = 13)
        resourceSample("demo2", result2 = 36)
    }

    override val part1 get() = simulation(1)
    override val part2 get() = simulation(9)

    private fun simulation(amountOfTails: Int): Int {
        var head = XY(0, 0)
        val tail = generateSequence { XY(0, 0) }.take(amountOfTails).toMutableList()
        val tails = mutableSetOf<XY>()
        inputLines().map { it.split(" ") }.forEach { (d, a) ->
            repeat(a.toInt()) {
                head = when (d) {
                    "R" -> head.right
                    "U" -> head.up
                    "L" -> head.left
                    "D" -> head.down
                    else -> throw IllegalArgumentException(d)
                }
                tail[0] = fixTail(head, tail[0])
                (1 until amountOfTails).forEach { tail[it] = fixTail(tail[it - 1], tail[it]) }
                tails.add(tail.last())
            }
        }
        return tails.size
    }

    private fun fixTail(head: XY, tail: XY): XY {
        if (tail == head || tail in head.mooreNeighbours()) {
            // nothing to do
            return tail
        }

        if (tail.x == head.x) {
            return if (tail.y < head.y) {
                head.up
            } else {
                head.down
            }
        } else if (tail.y == head.y) {
            return if (tail.x < head.x) {
                head.left
            } else {
                head.right
            }
        }

        if (tail.x < head.x && tail.y < head.y) {
            return fixTail(head, tail.down.right)
        } else if (tail.x > head.x && tail.y < head.y) {
            return fixTail(head, tail.down.left)
        } else if (tail.x > head.x && tail.y > head.y) {
            return fixTail(head, tail.up.left)
        } else { // if(tail.x < head.x && tail.y > head.y) {
            return fixTail(head, tail.up.right)
        }
    }
}

fun main() {
    Day9.fancyRun()
}
