package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode

object Day12 : AdventOfCode(2023, 12) {
    data class InputLine(
        val template: String,
        val parts: List<Int>
    ) {
        fun unfold() = InputLine(
            generateSequence(template) { it }.take(5).joinToString("?"),
            generateSequence(parts) { it }.take(5).flatten().toList()
        )
    }

    init {
        rawSample("???.### 1,1,3", 1, 1)
        rawSample(".??..??...?##. 1,1,3", 4, 16384)
        rawSample("?#?#?#?#?#?#?#? 1,3,1,6", 1, 1)
        rawSample("????.#...#... 4,1,1", 1, 16)
        rawSample("????.######..#####. 1,6,5", 4, 2500)
        rawSample("?###???????? 3,2,1", 10, 506250)

    }

    val parsedInput by lazy {
        input.lines.map { line ->
            val (template, numbers) = line.split(" ")
            InputLine(template, numbers.split(",").map { it.toInt() })
        }
    }

    private data class CacheKey(
        val template: String,
        val offset: Int,
        val groups: List<Int>
    )

    private var resultCache = mutableMapOf<CacheKey, Long>()

    private fun countPossibleSetups(
        line: InputLine,
        offset: Int = 0,
        groups: List<Int> = line.parts
    ): Long {
        val cacheKey = CacheKey(line.template, offset, groups)

        resultCache[cacheKey]?.let { return it }

        if (groups.isEmpty()) {
            return if ((offset..<line.template.length).any {
                    line.template[it] == '#'
                })
                0
            else
                1
        }

        val next = groups.first()
        val remain = groups.drop(1)
        val remainMinSpace = remain.sum() + remain.size
        val useSpace = line.template.length - offset - remainMinSpace
        val maxPos = useSpace - next + offset
        return (offset..maxPos).asSequence().map { pos ->
            val newOffset = pos + next + 1
            val end = pos + next

            if ((offset..<pos).any {
                    line.template[it] == '#'
                }) {
                return@map 0
            }

            if ((pos..<end).any {
                    line.template[it] == '.'
                }) {
                return@map 0
            }

            if (end < line.template.length && line.template[end] == '#') {
                return@map 0
            }

            countPossibleSetups(line, newOffset, remain)
        }.sum().also {
            check(it >= 0)
            resultCache[cacheKey] = it
        }
    }

    override val part1: Any
        get() = parsedInput.sumOf { inputLine ->
            countPossibleSetups(inputLine)
        }

    override val part2: Any
        get() = parsedInput.sumOf { inputLine ->
            countPossibleSetups(inputLine.unfold())
        }
}

fun main() {
    Day12.fancyRun()
}
