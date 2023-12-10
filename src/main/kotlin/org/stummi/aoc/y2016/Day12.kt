package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day12 : AdventOfCode(2016, 12) {
    override val part1 get() = evaluate()
    override val part2 get() = evaluate { this["c"] = 1 }

    fun evaluate(init: MutableMap<String, Int>.() -> Unit = {}): Any {
        val code = inputLines().map { it.split(" ") }
        var cptr = 0
        val registers = mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)
        init(registers)
        do {
            val inst = code[cptr]
            when (inst[0]) {
                "cpy" -> {
                    val src = inst[1]
                    val v = getValue(src, registers)
                    registers[inst[2]] = v
                }

                "inc" -> {
                    registers[inst[1]] = registers[inst[1]]!! + 1
                }

                "dec" -> {
                    registers[inst[1]] = registers[inst[1]]!! - 1
                }

                "jnz" -> {
                    if (getValue(inst[1], registers) != 0) {
                        cptr += inst[2].toInt() - 1
                    }
                }
            }
            ++cptr
        } while (cptr < code.size)
        return registers
    }

    private fun getValue(src: String, registers: MutableMap<String, Int>) =
        if (src[0].isDigit()) src.toInt() else registers[src]!!
}

fun main() {
    Day12.fancyRun()
}
