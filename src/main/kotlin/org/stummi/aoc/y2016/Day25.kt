package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day25 : AdventOfCode(2016, 25) {

    class Halt(val looksRight: Boolean) : Exception()

    class OutputChecker {
        var expect = 0
        var cnt = 0

        fun accept(i: Int) {
            //print(i)
            if (i != expect) {
                throw Halt(false)
            }
            if (++cnt == 10_000) {
                throw Halt(true)
            }
            expect = 1 - expect
        }
    }

    override val part1: Any
        get() {
            val code = inputLines()

            (0..1_000_000).forEach {
                //print("$it: ")
                try {
                    val checker = OutputChecker()
                    evaluate(
                        init = { put("a", it) },
                        output = checker::accept,
                    )
                } catch (h: Halt) {
                    //println("H")
                    if (h.looksRight) {
                        return it
                    }
                }
            }
            return -1
        }


    fun evaluate(init: MutableMap<String, Int>.() -> Unit = {}, output: (Int) -> Unit): Any {
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

                "out" -> {
                    output(getValue(inst[1], registers))
                }

                else -> throw IllegalArgumentException()
            }
            ++cptr
        } while (cptr < code.size)
        return registers
    }

    private fun getValue(src: String, registers: MutableMap<String, Int>) =
        if (src[0].isDigit()) src.toInt() else registers[src]!!
}

fun main() {
    Day25.fancyRun()
}
