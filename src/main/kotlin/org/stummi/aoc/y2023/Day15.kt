package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode

object Day15 : AdventOfCode(2023, 15) {

    init {
        rawSample("HASH", 52L, Unit)
        rawSample("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7", 1320L)
    }

    private fun hash(s: String): Int {
        var hash = 0
        s.chars().forEach {
            hash += it
            hash *= 17
            hash %= 256
        }
        return hash
    }

    override val part1: Any
        get() =
            input.lines.first().split(",").sumOf {
                hash(it).toLong()
            }

    override val part2: Any
        get() {

            val boxes = generateSequence { mutableListOf<Pair<String, Int>>() }.take(256).toList()

            input.lines.first().split(",").forEach {
                if (it.endsWith("-")) {
                    val label = it.dropLast(1)
                    val hash = hash(label)
                    boxes[hash].removeAll { (l, _) -> l == label }
                } else if ("=" in it) {
                    val spl = it.split("=")
                    val label = spl[0]
                    val v = spl[1].toInt()
                    val hash = hash(label)
                    val index = boxes[hash].indexOfFirst { (l, _) -> l == label }
                    if (index >= 0) {
                        boxes[hash][index] = label to v
                    } else {
                        boxes[hash].add(label to v)
                    }
                } else {
                    TODO()
                }
            }


            return boxes.withIndex().sumOf { (boxIdx, box) ->
                box.withIndex().sumOf { (slot, p) ->
                    (boxIdx + 1) * (slot + 1) * p.second
                }
            }
        }
}

fun main() {
    Day15.fancyRun()
}
