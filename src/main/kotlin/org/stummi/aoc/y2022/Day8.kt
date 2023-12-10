package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode
import kotlin.math.max

object Day8 : AdventOfCode(2022, 8) {
    init {
        resourceSample("demo", result1 = 21, result2 = 8)
    }

    override val part1
        get() = inputLines().map {
            it.map(Char::digitToInt)
        }.let { trees ->
            val rows = trees.size
            val cols = trees[0].size
            var treesVisible = 0;
            (0 until rows).forEach { row ->
                (0 until cols).forEach { col ->
                    var tree = trees[row][col]
                    val treeVisible = (0 until row).all { trees[it][col] < tree }
                            || (row + 1 until rows).all { trees[it][col] < tree }
                            || (0 until col).all { trees[row][it] < tree }
                            || (col + 1 until cols).all { trees[row][it] < tree }
                    if (treeVisible) {
                        ++treesVisible
                    }
                }
            }
            treesVisible
        }

    override val part2
        get() = inputLines().map {
            it.map(Char::digitToInt)
        }.let { trees ->
            val rows = trees.size
            val cols = trees[0].size
            var maxScore = 0;
            (0 until rows).forEach { row ->
                (0 until cols).forEach { col ->

                    var tree = trees[row][col]
                    val reduceFunc: (Pair<Int, Int>, Int) -> Pair<Int, Int> = { (score, max), cur ->
                        if (cur >= max) {
                            score + 1 to cur
                        } else {
                            score to max
                        }
                    }

                    fun viewDist(l: List<Int>, t: Int): Int {
                        if (l.isEmpty()) {
                            return 0;
                        }

                        return l.indexOfFirst { it >= t }.let {
                            if (it == -1) {
                                l.size
                            } else {
                                it + 1
                            }
                        }
                    }

                    val u = (row - 1 downTo 0).map { trees[it][col] }.toList().let { viewDist(it, tree) }
                    val l = (col - 1 downTo 0).map { trees[row][it] }.toList().let { viewDist(it, tree) }
                    val d = (row + 1 until rows).map { trees[it][col] }.toList().let { viewDist(it, tree) }
                    val r = (col + 1 until cols).map { trees[row][it] }.toList().let { viewDist(it, tree) }
                    val score = l * r * u * d
                    maxScore = max(maxScore, score)
                }
            }
            maxScore
        }
}

fun main() {
    Day8.fancyRun()
}
