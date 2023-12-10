package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode

object Day23 : AdventOfCode(2015, 23) {
    fun parsedInput() = inputLines().map {
        it.split(" ", limit = 2)
    }.map {
        it[0] to it[1].split(", ")
    }

    override val part1: Int
        get() = RegisterMap().apply(::runSimulation)["b"]

    override val part2: Int
        get() = RegisterMap("a" to 1).apply(::runSimulation)["b"]

    private class RegisterMap(
        vararg initial: Pair<String, Int>
    ) {
        private val registers = mutableMapOf<String, Int>().apply {
            initial.forEach { (k, v) -> this[k] = v }
        }

        operator fun get(key: String) = registers.getOrDefault(key, 0)
        operator fun set(key: String, value: Int) {
            registers[key] = value
        }
    }

    private fun runSimulation(registers: RegisterMap) {
        val lines = parsedInput()
        var inst = 0;
        while (inst < lines.size) {
            val code = lines[inst]
            val opCode = code.first
            val args = code.second
            when (code.first) {
                "jio" ->
                    if (registers[args[0]] == 1) {
                        inst += args[1].toInt()
                        continue
                    }

                "inc" ->
                    registers[args[0]]++

                "tpl" ->
                    registers[args[0]] *= 3

                "hlf" ->
                    registers[args[0]] /= 2

                "jmp" -> {
                    inst += args[0].toInt()
                    continue
                }

                "jie" -> if (registers[args[0]] % 2 == 0) {
                    inst += args[1].toInt()
                    continue
                }

                else -> throw IllegalArgumentException(code.toString())
            }

            ++inst
        }
    }
}

fun main() {
    Day23.fancyRun()
}
