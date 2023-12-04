package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode

object Day7 : AdventOfCode(2015, 7) {

    data class GateContext(
        val gatters: Map<String, Gate>,
        val cache: MutableMap<String, UShort> = mutableMapOf()
    )

    sealed interface Gate {
        fun getInputs(): List<String>

        fun getValue(ctx: GateContext): UShort

        fun getValueFromInputs(ctx: GateContext, inputName: String): UShort {
            return if (inputName[0].isDigit()) {
                inputName.toUShort()
            } else if (inputName in ctx.cache) {
                ctx.cache[inputName]!!
            } else {
                val value = ctx.gatters[inputName]?.getValue(ctx)
                    ?: throw IllegalArgumentException("Unknown input $inputName")
                ctx.cache[inputName] = value
                value
            }
        }

        val output: String;
    }

    data class NotOp(val input: String, override val output: String) : Gate {
        override fun getInputs(): List<String> {
            return listOf(input)
        }

        override fun getValue(ctx: GateContext): UShort {
            return getValueFromInputs(ctx, this.input).inv()
        }


    }

    data class StaticOp(val input: String, override val output: String) : Gate {
        override fun getInputs(): List<String> {
            return listOf(input)
        }

        override fun getValue(ctx: GateContext): UShort {
            return getValueFromInputs(ctx, this.input)
        }
    }

    data class BinOp(val leftInput: String, val op: String, val rightInput: String, override val output: String) :
        Gate {
        override fun getInputs(): List<String> {
            return listOf(leftInput, rightInput)
        }

        override fun getValue(ctx: GateContext): UShort {
            val left = getValueFromInputs(ctx, leftInput)
            val right = getValueFromInputs(ctx, rightInput)

            return when (op) {
                "LSHIFT" -> (left.toInt() shl right.toInt()).toUShort()
                "RSHIFT" -> (left.toInt() shr right.toInt()).toUShort()
                "OR" -> (left.toInt() or right.toInt()).toUShort()
                "AND" -> (left.toInt() and right.toInt()).toUShort()
                else -> throw NotImplementedError("$left $op $right")
            }
        }
    }

    private fun parseInput() = input().map { line ->
        line.split(" -> ").let { s ->
            s[0].split(" ") to s[1]
        }
    }.map { (cmd, output) ->
        when (cmd.size) {
            1 -> StaticOp(cmd[0], output)
            2 -> NotOp(cmd[1], output)
            3 -> BinOp(cmd[0], cmd[1], cmd[2], output)
            else -> throw IllegalArgumentException()
        }
    }.associateBy {
        it.output
    }

    override val part1: Any
        get() {
            val gatters = parseInput()
            return gatters["a"]!!.getValue(GateContext(gatters))
        }

    override val part2: Any
        get() {
            val gatters = parseInput()
            val ctx = GateContext(gatters)
            val a = gatters["a"]!!.getValue(ctx)
            ctx.cache.clear()
            ctx.cache["b"] = a
            return gatters["a"]!!.getValue(ctx)
        }
}


fun main() {
    Day7.fancyRun()
}

