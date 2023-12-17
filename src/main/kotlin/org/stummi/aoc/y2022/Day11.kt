package org.stummi.aoc.y2022

import org.stummi.aoc.AdventOfCode

object Day11 : AdventOfCode(2022, 11) {
    init {
        resourceSample("demo", 10605L, 2713310158L)
    }

    override val part1: Any
        get() = readMonkeys().apply {
            val bound = this.map { it.divTest }.reduce { a, b -> a * b }
            repeat(20) {
                forEach { it.moveAllItems(this, true, bound) }
            }
        }.let {
            it.map { it.inspectedItems }.sortedDescending().take(2).let { it[0] * it[1] }
        }

    override val part2: Any
        get() = readMonkeys().apply {
            val bound = this.map { it.divTest }.reduce { a, b -> a * b }
            repeat(10000) {
                forEach { it.moveAllItems(this, false, bound) }
            }
        }.let {
            it.map { it.inspectedItems }.sortedDescending().take(2).let { it[0] * it[1] }
        }

    class Monkey(
        val nr: Int,
        items: List<Long>,
        val op: List<String>,
        val divTest: Int,
        val trueGoal: Int,
        val falseGoal: Int,
    ) {
        var inspectedItems = 0L
        val items = items.toMutableList()

        fun addItem(item: Long) {
            if (item < 0) {
                throw IllegalStateException("this does not seem right")
            }
            items.add(item)
        }

        private fun moveItem(input: Long, monkeys: List<Monkey>, decreaseWorry: Boolean, bound: Int) {
            ++inspectedItems
            var newLevel = applyOperation(input)
            if (decreaseWorry) {
                newLevel /= 3
            }
            newLevel %= bound
            val isDivisible = newLevel % divTest.toLong() == 0L
            if (isDivisible) {
                monkeys[trueGoal].addItem(newLevel)
            } else {
                monkeys[falseGoal].addItem(newLevel)
            }
        }

        private fun applyOperation(input: Long): Long {
            val left = if (op[0] == "old") input else op[0].toLong()
            val right = if (op[2] == "old") input else op[2].toLong()
            return when (op[1]) {
                "*" -> left * right
                "+" -> left + right
                else -> throw IllegalArgumentException(op.toString())
            }
        }

        override fun toString(): String =
            "[$nr: $items - inspected: $inspectedItems]"

        fun moveAllItems(monkeys: List<Monkey>, decreaseWorry: Boolean, bound: Int) {
            val i = items.toList()
            items.clear()
            i.forEach {
                moveItem(it, monkeys, decreaseWorry, bound)
            }
        }


    }

    private fun readMonkeys(): List<Monkey> {
        return inputLines().chunked(7).map {
            it.map { l -> l.trim().split(" ") }.let {
                val nr = it[0][1].trimEnd(':').toInt()
                val items = it[1].drop(2).map { w -> w.trimEnd(',').toLong() }
                val op = it[2].drop(3)
                val divTest = it[3][3].toInt()
                val trueGoal = it[4][5].toInt()
                val falseGoal = it[5][5].toInt()
                Monkey(nr, items.toMutableList(), op, divTest, trueGoal, falseGoal)
            }
        }
    }


}

fun main() {
    Day11.fancyRun()
}
