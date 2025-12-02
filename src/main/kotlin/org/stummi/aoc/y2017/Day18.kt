package org.stummi.aoc.y2017

import org.stummi.aoc.AdventOfCode

object Day18 : AdventOfCode(2017, 18) {
    init {
        resourceSample(result1 = 4, result2 = Unit)
        resourceSample(input = "demo2", result1 = Unit, result2 = 3)
    }

    private class Program(
        val code: List<List<String>>
    ) {
        var inst = 0
        val registers = mutableMapOf<String, Long>()

        private fun getValue(s: String) =
            if (s[0].isDigit() || s[0] == '-') {
                s.toLong()
            } else {
                registers[s] ?: 0
            }

        private fun updateRegister(registerName: String, argument: String, op: (Long, Long) -> Long) {
            registers.compute(registerName) { _, i ->
                op(i ?: 0, getValue(argument))
            }
        }

        fun advance(input: List<Long>, part2: Boolean): List<Long> {
            var inputs = input.toMutableList()
            val out = mutableListOf<Long>()
            while (true) {
                val line = code[inst]

                when (line[0]) {
                    "snd" -> out.add(getValue(line[1]))
                    "set" -> registers[line[1]] = getValue(line[2])
                    "add" -> updateRegister(line[1], line[2]) { a, b -> a + b }
                    "mul" -> updateRegister(line[1], line[2]) { a, b -> a * b }
                    "mod" -> updateRegister(line[1], line[2]) { a, b -> a % b }
                    "rcv" -> {
                        if (part2) {
                            if (inputs.isEmpty()) {
                                return out
                            } else {
                                registers[line[1]] = inputs.removeFirst()
                            }
                        } else {
                            if (getValue(line[1]) != 0L) {
                                return out
                            }
                        }

                    }

                    "jgz" -> {
                        if (getValue(line[1]) > 0) {
                            inst += getValue(line[2]).toInt()
                            continue
                        }
                    }

                    else -> TODO(line[0])
                }
                ++inst
            }
        }
    }


    override val part1: Any
        get() {
            val code = input.lines.map { it.split(" ") }
            return Program(code).advance(emptyList(), false).last()
        }

    override val part2: Any
        get() {
            val code = input.lines.map { it.split(" ") }
            val prog1 = Program(code)
            val prog2 = Program(code)
            prog2.registers["p"] = 1

            var p1Out = emptyList<Long>()
            var p2Out = emptyList<Long>()
            var p2OutCount = 0

            do {
                p1Out = prog1.advance(p2Out, true)
                p2Out = prog2.advance(p1Out, true)
                p2OutCount += p2Out.size
            } while (p2Out.isNotEmpty())

            return p2OutCount
        }
}

// 3675 - too high

fun main() {
    Day18.fancyRun()
}
