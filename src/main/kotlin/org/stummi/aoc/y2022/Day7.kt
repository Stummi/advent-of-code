package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode

object Day7 : AdventOfCode(2022, 7) {
    override val part1: Int
        get() {
            val totalSizeByDir = readDirSizes()
            return totalSizeByDir.filterValues { it <= 100000 }.values.sum()
        }

    override val part2: Int
        get() {
            val files = readDirSizes()
            val totalSpace = 70_000_000
            val requiredSpace = 30_000_000
            val usedSpace = files[emptyList()]!!
            val availableSpace = totalSpace - usedSpace
            val needToFree = requiredSpace - availableSpace
            val min = files.filterValues { it >= needToFree }.values.minOf { it }
            return min
        }

    private fun readDirSizes(): MutableMap<List<String>, Int> {
        val lines = inputLines().map { it.split(" ") }
        var l = 0
        val path = mutableListOf<String>()
        val totalSizeByDir = mutableMapOf<List<String>, Int>()
        while (l < lines.size) {
            val line = lines[l]
            if (line[0] != "$") throw IllegalStateException(line.toString())
            when (line[1]) {
                "cd" -> when (line[2]) {
                    "/" -> path.clear()
                    ".." -> path.removeLast()
                    else -> path.add(line[2])
                }

                "ls" -> {
                    while (l < lines.size - 1 && lines[l + 1][0] != "$") {
                        ++l
                        if (lines[l][0] == "dir") {
                            continue
                        }

                        totalSizeByDir.compute(path.toList()) { _, i ->
                            (i ?: 0) + lines[l][0].toInt()
                        }
                        val p = path.toMutableList()
                        while (!p.isEmpty()) {
                            p.removeLast()
                            totalSizeByDir.compute(p.toList()) { _, i ->
                                (i ?: 0) + lines[l][0].toInt()
                            }
                        }
                    }
                }
            }
            ++l
        }
        return totalSizeByDir
    }
}

fun main() {
    Day7.fancyRun()
}
