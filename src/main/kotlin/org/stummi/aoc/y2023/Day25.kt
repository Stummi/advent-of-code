package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode
import java.io.PrintStream
import kotlin.math.abs

object Day25 : AdventOfCode(2023, 25) {

    val parsedInput by lazy {
        input.lines.associate {
            it.split(" ").let {
                it.first().dropLast(1) to it.drop(1)
            }
        }
    }
    fun graphViz(out: PrintStream) {
        out.println("diagraph G {")
        out.println("edge [ arrowhead=\"none\" ];")
        parsedInput.forEach { (k, v) ->
            v.forEach {
                out.println("$k -> $it")
            }
        }
        out.println("}")
    }

    override val part1: Any
        get() {
            var graph = parsedInput.toMutableMap()
            listOf(
                "zdj" to "nvt",
                "mzg" to "bbm",
                "cth" to "xxk"
            ).forEach { (f, t) ->
                val l1 = graph[f]?.toMutableList() ?: mutableListOf()
                val l2 = graph[t]?.toMutableList() ?: mutableListOf()

                check(l1.remove(t) xor l2.remove(f))

                graph[f] = l1.toList()
                graph[t] = l2.toList()
            }

            var openList = mutableListOf( graph.keys.first() )
            var closedList = mutableListOf<String>()

            val nodeCount = (graph.keys.toSet() + graph.values.flatten().toSet()).count()

            while(!openList.isEmpty()) {
                val i = openList.removeFirst()
                closedList.add(i)

                val next = ((graph[i] ?: emptyList()) + graph.filterValues { i in it }.keys.toList())
                    .filterNot { it in closedList || it in openList }

                println("$i -> $next")

                next.forEach {
                    openList.add(it)
                }
            }


            println(closedList.size)
            println(closedList.toSet().size)



            return closedList.size * (nodeCount - closedList.size)
        }
}

fun main() {
    Day25.graphViz(PrintStream("/tmp/day25.graph"))
    Day25.fancyRun()
}
