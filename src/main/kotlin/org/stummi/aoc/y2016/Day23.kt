package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day23 : AdventOfCode(2016, 23) {

    init {
        resourceSample("demo", 3)
    }

    override val part1: Any
        get() = evaluate { put("a", 7) }
    override val part2: Any
        get() = evaluate { put("a", 12) }

    fun evaluate(init: MutableMap<String, Int>.() -> Unit = {}): Any {
        val code = inputLines().map { it.split(" ") }.toMutableList()
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
                        cptr += getValue(inst[2], registers) - 1
                    }
                }

                "tgl" -> {
                    val idx = getValue(inst[1], registers) + cptr
                    if (idx < code.size) {
                        val element = tgl(code[idx])
                        code.set(idx, element)
                    }
                }

                else -> {
                    throw IllegalArgumentException(inst.toString())
                }
            }
            ++cptr
        } while (cptr < code.size)
        return registers
    }

    private fun tgl(inst: List<String>): List<String> {
        val cmd = inst[0]
        return listOf(
            if (inst.size == 2) {
                if (cmd == "inc") "dec" else "inc"
            } else {
                if (cmd == "jnz") "cpy" else "jnz"
            }
        ) + inst.drop(1)
    }

    private fun getValue(src: String, registers: MutableMap<String, Int>) =
        if (src[0] == '-' || src[0].isDigit()) src.toInt() else registers[src] ?: throw IllegalArgumentException(src)
}

fun main() {
    Day23.fancyRun()
}
