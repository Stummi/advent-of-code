package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode

object Day10 : AdventOfCode(2016, 10) {
    override val part1: Any
        get() = runSimulation().compareLog.find { it.second == 17 to 61 }!!.first
    override val part2: Any
        get() = runSimulation().let {
            it.outputs[0]!! * it.outputs[1]!! * it.outputs[2]!!
        }


    class Target(val type: Type, val id: Int) {
        constructor(type: String, id: Int) : this(Type.valueOf(type.uppercase()), id)

        override fun toString() = "$type $id"

        enum class Type {
            BOT, OUTPUT
        }
    }

    class Context(val bots: Map<Int, Bot>) {
        val outputs: MutableMap<Int, Int> = mutableMapOf()
        fun getBot(id: Int) = bots[id]
        fun getOutput(id: Int) = outputs[id]
        val compareLog = mutableListOf<Pair<Int, Pair<Int, Int>>>()

        fun addOutput(id: Int, value: Int) {
            outputs[id] = value
        }
    }

    data class Bot(val number: Int, val lowTarget: Target, val highTarget: Target) {
        var tmpValue: Int? = null

        fun give(i: Int, context: Context) {
            if (tmpValue == null) {
                tmpValue = i
            } else {
                val low = minOf(tmpValue!!, i)
                val high = maxOf(tmpValue!!, i)
                context.compareLog.add(number to (low to high))
                tmpValue = null
                when (lowTarget.type) {
                    Target.Type.BOT -> context.bots[lowTarget.id]!!.give(low, context)
                    Target.Type.OUTPUT -> context.addOutput(lowTarget.id, low)
                }
                when (highTarget.type) {
                    Target.Type.BOT -> context.bots[highTarget.id]!!.give(high, context)
                    Target.Type.OUTPUT -> context.addOutput(highTarget.id, high)
                }
            }
        }
    }

    fun runSimulation(): Context {
        val map = inputLines().map { it.split(" ") }.groupBy { it[0] }
        val initValues = map["value"]!!.map {
            it[1].toInt() to it[5].toInt()
        }
        val initBots = map["bot"]!!.map {
            Bot(it[1].toInt(), Target(it[5], it[6].toInt()), Target(it[10], it[11].toInt()))
        }

        val bots = initBots.associateBy { it.number }
        val context = Context(bots)
        initValues.shuffled().forEach { (value, bot) ->
            bots[bot]!!.give(value, context)
        }
        return context
    }

}

fun main() {
    Day10.fancyRun()
}

