package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode

object Day8 : AdventOfCode(2017, 8) {
    private data class Instruction(
        val register: String,
        val add: Int,
        val conditionRegister: String,
        val condition: String,
        val conditionValue: Int,
    ) {
        fun eval(registers: MutableMap<String, Int>) {
            val conditionRegValue = registers[conditionRegister] ?: 0
            val check = when (condition) {
                ">" -> conditionRegValue > conditionValue
                "<" -> conditionRegValue < conditionValue
                ">=" -> conditionRegValue >= conditionValue
                "<=" -> conditionRegValue <= conditionValue
                "==" -> conditionRegValue == conditionValue
                "!=" -> conditionRegValue != conditionValue
                else -> throw IllegalStateException(condition)
            }

            if (!check) {
                return
            }

            registers[register] = (registers[register] ?: 0) + add
        }
    }

    private val parsedInput by lazy {
        input.lines.map {
            val spl = it.split(" ")
            val reg = spl[0]
            val add = spl[2].toInt().let { v ->
                when (spl[1]) {
                    "dec" -> -v
                    "inc" -> v
                    else -> TODO()
                }
            }
            val condRegister = spl[4]
            val cond = spl[5]
            val condValue = spl[6].toInt()
            check(spl[3] == "if")
            Instruction(
                reg,
                add,
                condRegister,
                cond,
                condValue
            )
        }
    }

    override val part1: Any
        get() {
            val registers = mutableMapOf<String, Int>()
            val instructions = parsedInput
            instructions.forEach {
                it.eval(registers)
            }
            return registers.maxOf { it.value }
        }

    override val part2: Any
        get() {
            val registers = mutableMapOf<String, Int>()
            val instructions = parsedInput
            return instructions.maxOf {
                it.eval(registers)
                registers.maxOfOrNull { it.value } ?: 0
            }
        }
}

fun main() {
    Day8.fancyRun()
}
