package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.astar
import kotlin.time.ExperimentalTime

object Day11 : AdventOfCode(2016, 11) {
    override val part1
        get() = astar(
            readState(),
            { it.possibleMoves().map { it to 1 } },
            { it.isDone() },
            { it.heuristicCost() }
        ).size

    override val part2
        get() = astar(
            readState().applyPart2(),
            { it.possibleMoves().map { it to 1 } },
            { it.isDone() },
            { it.heuristicCost() }
        ).size

    enum class MachineType {
        Generator,
        Microchip,
    }

    data class Item(val type: MachineType, val element: String) {
        override fun toString() = "$element $type"
    }

    data class State(
        val elevatorFloor: Int,
        val floors: List<Set<Item>>
    ) {

        fun possibleMoves() =
            when (elevatorFloor) {
                0 -> possibleMoves(1)
                floors.size - 1 -> possibleMoves(floors.size - 2)
                else -> possibleMoves(elevatorFloor - 1) + possibleMoves(elevatorFloor + 1)
            }.filter { it.isValid() }

        private fun possibleMoves(target: Int) =
            floors[elevatorFloor].asSequence().map { item ->
                transform(target, setOf(item))
            } + permutations(floors[elevatorFloor]).asSequence().map {
                transform(target, it)
            }


        private fun permutations(items: Set<Day11.Item>): List<Set<Day11.Item>> {
            val list = items.toList()
            return list.flatMapIndexed { idx, left ->
                list
                    .asSequence().drop(idx + 1).map { right -> setOf(left, right) }
            }
        }

        fun isValid(): Boolean {
            return floors.all {
                isFloorValid(it)
            }
        }

        private fun isFloorValid(it: Set<Day11.Item>): Boolean {
            val generators = mutableSetOf<String>()
            val chips = mutableSetOf<String>()
            it.forEach {
                when (it.type) {
                    MachineType.Generator -> generators
                    MachineType.Microchip -> chips
                }.add(it.element)
            }

            return generators.isEmpty() || chips.all { it in generators }
        }

        private fun transform(target: Int, items: Set<Day11.Item>) = State(
            target,
            floors.mapIndexed { idx, f ->
                when (idx) {
                    target -> f + items
                    elevatorFloor -> f - items
                    else -> f
                }
            }
        )

        fun prettyPrint() {
            floors.forEachIndexed { index, items ->
                print("#$index ")
                if (elevatorFloor == index) {
                    print("|E| ")
                } else {
                    print("| | ")
                }
                items.forEach {
                    print("$it, ")
                }
                println()
            }
        }

        fun isDone() = floors.take(floors.size - 1).all { it.isEmpty() }
        fun applyPart2(): State {
            return State(
                elevatorFloor,
                floors.mapIndexed { idx, i ->
                    if (idx == 0) i + setOf(
                        Item(MachineType.Generator, "elerium"),
                        Item(MachineType.Microchip, "elerium"),
                        Item(MachineType.Generator, "dilithium"),
                        Item(MachineType.Microchip, "dilithium"),
                    ) else i
                }
            )
        }

        fun heuristicCost(): Int {
            return 0
            /*return floors.dropLast(1).mapIndexed { f, items ->
                (3 - f * items.size) / 2
            }.sum()*/
        }
    }

    fun readState() = inputLines().map {
        Regex("(\\w+)(?:-compatible)? (microchip|generator)").findAll(it).map {
            val element = it.groupValues[1]
            val type = MachineType.valueOf(it.groupValues[2].replaceFirstChar { it.uppercase() })
            Item(type, element)
        }.toSet()
    }.let {
        State(0, it)
    }
}

@OptIn(ExperimentalTime::class)
fun main() {
    Day11.fancyRun()
}

