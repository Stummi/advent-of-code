package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.partitionBy

object Day19 : AdventOfCode(2023, 19) {
    private data class Workflow(
        val name: String,
        val conditions: List<Condition>,
        val fallback: String,
    ) {
        fun evaluate(part: Map<Char, Int>): String {
            return conditions.firstOrNull { it.matches(part) }?.target ?: fallback
        }
    }

    private data class Condition(
        val property: Char,
        val number: Int,
        val compare: Char,
        val target: String,
    ) {
        fun matches(part: Map<Char, Int>): Boolean {
            return when (compare) {
                '>' -> part[property]!! > number
                '<' -> part[property]!! < number
                else -> TODO()
            }
        }

        fun evaluateRange(part: Map<Char, IntRange>): Pair<Map<Char, IntRange>, Map<Char, IntRange>> {
            val existingRange = part[property]!!
            val (yesRange, noRange) = when (compare) {
                '>' -> (number + 1)..(existingRange.last) to
                        existingRange.first..number

                '<' -> existingRange.first..<number to
                        number..existingRange.last

                else -> TODO()
            }

            return (part + mapOf(property to yesRange)) to
                    (part + mapOf(property to noRange))
        }
    }

    private fun parseWorkflow(s: String): Workflow {
        val bracket = s.indexOf('{')
        val name = s.substring(0..<bracket)
        val conditions =
            s.substring((bracket + 1)..<(s.length - 1)).split(",")

        val c = conditions.dropLast(1).map {
            val (cmpString, target) = it.split(":")
            val cmp: Char = if ('>' in it) {
                '>'
            } else {
                check(it.indexOf('<') > 0)
                '<'
            }

            val (property, number) = cmpString.split(cmp)
            Condition(property.single(), number.toInt(), cmp, target)
        }

        return Workflow(name, c, conditions.last())
    }

    private fun parsePart(s: String): Map<Char, Int> {
        return s.substring(1..<(s.length - 1)).split(",").map {
            val sp = it.split("=")
            sp[0].single() to sp[1].toInt()
        }.toMap()
    }

    private val parsedInput by lazy {
        val (wfLines, partLines) = input.lines.partitionBy { it.isBlank() }
        val workflows = wfLines.map { parseWorkflow(it) }.associateBy { it.name }
        val parts = partLines.map { parsePart(it) }
        workflows to parts
    }

    private val workflows by lazy { parsedInput.first }
    private val parts by lazy { parsedInput.second }


    override val part1: Any
        get() {
            return parts.filter {
                var workflow = "in"
                while (workflow in workflows) {
                    workflow = workflows[workflow]!!.evaluate(it)
                }

                workflow == "A"
            }.sumOf {
                it.values.sum()
            }
        }

    private fun valid(part: Map<Char, IntRange>): Boolean {
        return part.values.all { it.last - it.first >= 0 }
    }

    override val part2: Any
        get() {
            val openList = mutableListOf(
                "xmas".associateWith { (1..4000) } to "in"
            )

            val closedList = mutableListOf<Pair<Map<Char, IntRange>, String>>()

            while (openList.isNotEmpty()) {
                val (part, wfName) = openList.removeFirst()
                if (wfName !in workflows) {
                    closedList.add(part to wfName)
                    continue
                }

                var currentPart = part
                val workflow = workflows[wfName]!!
                for (condition in workflow.conditions) {
                    val (yesPart, noPart) = condition.evaluateRange(currentPart)
                    if (valid(yesPart)) {
                        openList.add(yesPart to condition.target)
                    }
                    currentPart = noPart
                    if (!valid(noPart)) {
                        break
                    }
                }

                if (valid(currentPart)) {
                    openList.add(currentPart to workflow.fallback)
                }
            }

            return closedList.filter { it.second == "A" }.sumOf { (part, _) ->
                part.values.map { it.last - it.first + 1L }.reduce(Long::times)
            }
        }

}


fun main() {
    Day19.fancyRun()
}

