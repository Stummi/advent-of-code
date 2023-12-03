package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode

object Day5 : AdventOfCode(2022, 5) {
    override val part1: String
        get() = readState().let { (stacks, moves) ->
            return applyMoves(moves, stacks)
        }

    override val part2: String
        get() = readState().let { (stacks, moves) ->
            return applyMoves(moves, stacks, true)
        }

    private fun applyMoves(
        moves: List<String>,
        stacks: List<MutableList<Char>>,
        reversed: Boolean = false
    ): String {
        moves.map { it.split(" ") }.forEach { line ->
            val cnt = line[1].toInt()
            val from = line[3].toInt() - 1
            val to = line[5].toInt() - 1
            val tmp = mutableListOf<Char>()
            repeat(cnt) {
                tmp.add(stacks[from].removeLast())
            }
            if (reversed)
                stacks[to].addAll(tmp.reversed())
            else
                stacks[to].addAll(tmp)
        }
        return stacks.map { it.last() }.joinToString("")
    }

    private fun readState(): Pair<List<MutableList<Char>>, List<String>> {
        val inp = input()
        val sep = inp.indexOfFirst { it.isBlank() }
        val field = inp.take(sep)
        val stacks = parseField(field)
        val moves = inp.drop(sep + 1)
        return stacks to moves
    }


    private fun parseField(field: List<String>): List<MutableList<Char>> {
        val stackCount = (field.last().length / 4) + 1
        val stacks = generateSequence { mutableListOf<Char>() }.take(stackCount).toList()
        field.reversed().drop(1).forEach line@{ line ->
            (0 until stackCount).forEach { s ->
                val pos = s * 4 + 1
                if (pos >= line.length) {
                    return@line
                }
                val c = line[pos]
                if (c != ' ') {
                    stacks[s].add(c)
                }
            }
        }
        return stacks
    }
}

fun main() {
    Day5.fancyRun()
}
