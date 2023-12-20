package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode

object Day20 : AdventOfCode(2023, 20) {
    init {
        resourceSample("demo1", null, Unit)
        resourceSample("demo2", null, Unit)
    }

    private interface Module {
        val name: String
        val sources: List<String>
        val destinations: List<String>

        fun configureInputs(map: List<Module>) {}
        fun sendSignal(source: String, signal: Boolean): List<Pair<String, Boolean>> = emptyList()
        fun reset() {}
        fun state(): Any = Unit
    }

    private class Conjunction(
        override val name: String,
        override val destinations: List<String>,
    ) : Module {
        val inputs = mutableMapOf<String, Boolean>()
        override lateinit var sources: List<String>

        override fun configureInputs(map: List<Module>) {
            map.forEach {
                inputs[it.name] = false
            }

            sources = map.map { it.name }
        }

        override fun sendSignal(source: String, signal: Boolean): List<Pair<String, Boolean>> {
            inputs[source] = signal
            return if (inputs.values.all { it }) {
                destinations.map { it to false }
            } else {
                destinations.map { it to true }
            }
        }

        override fun reset() {
            inputs.keys.forEach {
                inputs[it] = false
            }
        }

        override fun state() = inputs.toMap()

        override fun toString(): String =
            "&$name"

    }

    private class FlipFlop(
        override val name: String,
        override val destinations: List<String>,
    ) : Module {
        override lateinit var sources: List<String>

        var state = false

        override fun sendSignal(source: String, signal: Boolean): List<Pair<String, Boolean>> {
            if (signal) {
                return emptyList()
            }

            state = !state
            return if (state) {
                destinations.map { it to true }
            } else {
                destinations.map { it to false }
            }
        }

        override fun reset() {
            state = false
        }

        override fun state(): Any = state

        override fun toString() = "%$name"

        override fun configureInputs(map: List<Module>) {
            sources = map.map { it.name }
        }
    }

    private class Broadcast(
        override val name: String,
        override val destinations: List<String>,
    ) : Module {
        override var sources: List<String> = emptyList()
        override fun sendSignal(source: String, signal: Boolean): List<Pair<String, Boolean>> {
            return destinations.map { it to signal }
        }

        override fun configureInputs(map: List<Module>) {
            sources = map.map { it.name }
        }

        override fun toString() = "~$name(${destinations.joinToString(",")})"
    }

    private class Sink(
        override val name: String,
    ) : Module {
        val valuesReceived = mutableListOf<Boolean>()
        override lateinit var sources: List<String>
        override val destinations: List<String>
            get() = emptyList()

        override fun reset() {
            valuesReceived.clear()
        }

        override fun sendSignal(source: String, signal: Boolean): List<Pair<String, Boolean>> {
            valuesReceived.add(signal)
            return emptyList()
        }

        override fun configureInputs(map: List<Module>) {
            sources = map.map { it.name }
        }

        override fun toString(): String = ">$name"

    }


    private val parsedInput by lazy {
        input.lines.map {
            val (id, destList) = it.split(" -> ")
            val dest = destList.split(", ")
            when {
                id[0] == '%' -> FlipFlop(id.drop(1), dest)
                id[0] == '&' -> Conjunction(id.drop(1), dest)
                id == "broadcaster" -> Broadcast(id, dest)
                else -> throw IllegalStateException(id)
            }
        }.associateBy { it.name }.also { modules: Map<String, Module> ->
            modules.values.flatMap { mod ->
                mod.destinations.map { mod.name to it }
            }.groupBy { it.second }.mapValues { it.value.map { it.first } }.forEach { mod, inputs ->
                modules[mod]?.configureInputs(
                    inputs.map { modules[it]!! }
                )
            }

        }
    }

    override val part1: Any
        get() = parsedInput.let { modules ->

            var loSigCount = 0
            var hiSigCount = 0

            repeat(1000) {
                val (hi, lo) = pressButton(modules)
                hiSigCount += hi
                loSigCount += lo
            }

            return loSigCount * hiSigCount
        }

    override val part2: Any
        get() = parsedInput.let { modules ->
            modules.values.forEach { it.reset() }
            val rxSender = modules.values.find { it.destinations == listOf("rx") }!!
            check(rxSender is Conjunction)

            rxSender.inputs.keys.map {
                evaluateSubtree(getSubTree(it)).toLong()
            }.reduce(Long::times)
        }

    private fun evaluateSubtree(tree: List<Module>): Int {
        tree.forEach { it.reset() }
        val map = tree.associateBy { it.name }
        val sink = tree.single { it is Sink } as Sink
        repeat(100000) {
            pressButton(map)

            if (sink.valuesReceived.contains(true)) {
                return it + 1
            }

            sink.reset()
        }
        return 0
    }

    private fun pressButton(modules: Map<String, Module>): Pair<Int, Int> {
        data class Signal(
            val from: String,
            val to: String,
            val signal: Boolean
        )

        val openSignals = mutableListOf(
            Signal("button", "broadcaster", false)
        )

        var hiSigCount = 0
        var loSigCount = 0

        while (openSignals.isNotEmpty()) {
            val (source, current, signal) = openSignals.removeFirst()

            if (signal) {
                hiSigCount++
            } else {
                loSigCount++
            }

            modules[current]?.sendSignal(source, signal)?.map { (target, signal) ->
                Signal(current, target, signal)
            }?.let {
                openSignals.addAll(it)
            }
        }

        return hiSigCount to loSigCount

    }

    private fun getSubTree(module: String): List<Module> {
        val openList = mutableListOf(module)
        val closedList = mutableListOf<String>()

        while (openList.isNotEmpty()) {
            val modName = openList.removeFirst()
            if (modName in closedList) {
                continue
            }
            closedList.add(modName)
            parsedInput[modName]?.sources?.let { openList.addAll(it) }
        }

        val modules = closedList.map { parsedInput[it]!! }
        val broadcast = modules.find { it is Broadcast }!!
        val replaceBroadcast = Broadcast(
            broadcast.name,
            broadcast.destinations.filter { it in closedList }
        )

        val sink = Sink(
            parsedInput[module]!!.destinations.single(),
        )


        return modules - broadcast + replaceBroadcast + sink
    }

    private fun printTgf(modules: Map<String, Module>) {
        modules.values.forEach { from ->
            from.destinations.forEach {
                println("${from} ${modules[it] ?: it}")
            }
        }
    }


}

fun main() {
    Day20.fancyRun()
}
