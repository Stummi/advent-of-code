package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.splitToInts

object Day17 : AdventOfCode(2024, 17) {

    data class Machine(
        var regA: Int,
        var regB: Int,
        var regC: Int,
        val program: List<Int>,
        var inst: Int = 0,
        var out: MutableList<Int> = mutableListOf(),
    ) {
        val opcode get() = program[inst]
        val litOperand get() = program[inst + 1]
        val comboOperand get() = combo(litOperand)

        enum class Op(val func: Machine.() -> Unit, val defJmp: Int = 2) {
            Adv({ regA /= 1 shl comboOperand }),
            Bxl({ regB = regB xor litOperand }),
            Bst({ regB = comboOperand % 8 }),
            Jnz({
                if (regA != 0) {
                    inst = litOperand
                } else {
                    inst += 2
                }
            }, defJmp = 0),
            Bxc({ regB = regB xor regC }),
            Out({ out.add(comboOperand % 8) }),
            Bdv({ regB = regA / (1 shl comboOperand) }),
            Cdv({ regC = regA / (1 shl comboOperand) }),
        }

        fun run() {
            while (inst < program.size) {
                val opCode = program[inst]
                val op = Op.entries[opCode]
                op.func(this)
                inst += op.defJmp
            }
        }

        fun combo(operand: Int) = when (operand) {
            0, 1, 2, 3 -> operand
            4 -> regA
            5 -> regB
            6 -> regC
            else -> error("invalid operand: $operand")
        }

    }

    override val part1: Any
        get() {
            val i = input.lines
            val prog = i.last().splitToInts()
            val (regA, regB, regC) = i.take(3).map { it.splitToInts().first() }

            val m = Machine(regA, regB, regC, prog)
            m.run()
            return m.out.joinToString(",")
        }

    override val part2: Any
        get() {
            val i = input.lines
            val prog = i.last().splitToInts()
            val (regA, regB, regC) = i.take(3).map { it.splitToInts().first() }

            val s = 100_000
            (s..(s + 100)).forEach {
                val m = Machine(it, regB, regC, prog)
                m.run()
                println("$it -> ${m.out}")
            }
            return 0
        }

}

fun run(regA: Long): List<Int> {
    var a = regA
    val ret = mutableListOf<Int>()
    do {
        var b = a % 8L
        // b = b xor 3
        val c = a / (1L shl (b.toInt() xor 3))
        // b = b xor c
        b = b xor 3
        a /= 8
        ret.add((b % 8).toInt())
    } while (a != 0L)
    return ret
}

fun main() {
    val code = listOf(2, 4, 1, 3, 7, 5, 4, 0, 1, 3, 0, 3, 5, 5, 3)

    bruteforce(code, listOf(3))?.let {
        var a = 0L
        (it).forEach { i ->
            a = a shl 3
            a = a or i.toLong()
        }

        println(a)
        println(run(a))

        return
    }
}

fun bruteforce(code: List<Int>, existingInput: List<Int> = emptyList()): List<Int>? {
    if (code.isEmpty()) {
        return existingInput
    }

    println("$existingInput - Bruteforce: $code")
    (0..7).forEach {
        var a = 0L
        (existingInput + it).forEach { i ->
            a = a shl 3
            a = a or i.toLong()
        }
        val ret = run(a)
        if (ret[0] == code.last()) {
            println("$existingInput - Found: $it: $ret")
            bruteforce(code.dropLast(1), existingInput + it)?.let {
                println("$existingInput - Return: $it")
                return it
            }
        }

    }
    println("$existingInput - No :(")
    return null
}
