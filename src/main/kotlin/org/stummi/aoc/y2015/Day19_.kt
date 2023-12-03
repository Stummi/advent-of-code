package org.stummi.aoc.y2015

fun main() {

    val lines = Unit.javaClass.getResourceAsStream("/2015/19_demo.txt").use {
        it!!.bufferedReader().readLines()
    }

    val replacements = lines
        .filter { it.contains("=>") }
        .map { it.split(" => ") }
        .map { it[0] to it[1] }
        .toList()

    var formula = lines.last()

    println(findPath("e", formula, replacements))

    /*
        resolve(formula, replacements).let {
            println(it)
        }*/
}

var replacementsPerIteration = 5;
var counter = 0
fun findPath(s: String, goal: String, replacements: List<Pair<String, String>>): Int {
    println(s)
    if (++counter > 10) {
        System.exit(0)
    }
    if (s == goal) {
        return 0;
    }

    var candidates = sequenceOf(s)
    repeat(replacementsPerIteration) {
        candidates = candidates.flatMap { it.allReplacements(replacements) }
    }



    return candidates.sortedByDescending { countCommonChars(it, goal) }.map {
        findPath(it, goal, replacements)
    }.firstOrNull() { it != -1 } ?: -1
}

fun countCommonChars(
    s1: String,
    s2: String
): Int {
    val maxLen = s1.length.coerceAtMost(s2.length)
    return (0 until maxLen).firstOrNull {
        s1[it] != s2[it]
    } ?: maxLen
}
