package org.stummi.aoc.y2015

fun main_() {

    val lines = Unit.javaClass.getResourceAsStream("/2015/19.txt").use {
        it!!.bufferedReader().readLines()
    }

    val replacements = lines
        .filter { it.contains("=>") }
        .map { it.split(" => ") }
        .map { it[0] to it[1] }
        .toList()

    var formula = "e"

    repeat(20) {
        val rep = replacements.filter { it.first in formula }.shuffled().first()
        val index = findAll(formula, rep.first).shuffled().first()
        formula = formula.replaceRange(index, index + rep.first.length, rep.second)
    }
    println(formula)
    println(formula.length)
}

fun main() {

    val lines = Unit.javaClass.getResourceAsStream("/2015/19.txt").use {
        it!!.bufferedReader().readLines()
    }

    val replacements = lines
        .filter { it.contains("=>") }
        .map { it.split(" => ") }
        .map { it[0] to it[1] }
        .toList()

    val parts = replacements.asSequence()
        .flatMap { listOf(it.first, it.second) }
        .filterNot { it == "e" }
        .flatMap { splitFormula(it) }
        .distinct()
        .sorted()
        .toList()
    println(parts)

    println(parts.filter { p -> replacements.none { r -> r.first == p } })
    parts.forEach { p ->
        println("$p -> ${replacements.count { it.second.contains(p) }}")
    }

    var formula = lines.last()
    println("--")
    println(splitFormula(formula).groupingBy { it }.eachCount())
    println(splitFormula(formula).count())
    println("--")
    /*
    resolve(formula, replacements).let {
        println(it)
    }*/
}

var nextPrint = 0L

fun resolve(
    formula: String,
    replacements: List<Pair<String, String>>,
    cached: MutableMap<String, Int> = mutableMapOf(),
    stepsSoFar: Int = 0,
): Int {

    val now = System.currentTimeMillis()
    if (now > nextPrint) {
        nextPrint = now + 120000
        println("Cache: ${cached.size}")
    }

    cached[formula]?.let { return it }

    if (formula == "e") {
        println("found something: $stepsSoFar")
        return 0
    }

    val r = replacements.asSequence().shuffled().map { repl ->
        formula.replaceEach(repl.second, repl.first).shuffled().map { newFormula ->
            resolve(newFormula, replacements, cached, stepsSoFar + 1)
        }
    }.flatten().filter { it != -1 }.minOrNull()?.let { it + 1 } ?: -1

    cached[formula] = r
    return r
}


fun splitFormula(formula: String) = sequence {
    var lastFind = 0
    (1 until formula.length).forEach {
        if (formula[it].isUpperCase()) {
            yield(formula.substring(lastFind, it))
            lastFind = it
        }
    }
    yield(formula.substring(lastFind))
}

fun String.allReplacements(replacement: List<Pair<String, String>>): Sequence<String> =
    replacement.asSequence().flatMap {
        this.replaceEach(it.first, it.second)
    }


fun String.replaceEach(search: String, replace: String) = findAll(this, search).map {
    this.replaceRange(it, it + search.length, replace)
}

fun findAll(search: String, substring: String): Sequence<Int> {
    return sequence {
        var pos = 0

        while (true) {
            var newPos = search.indexOf(substring, pos)
            if (newPos < 0) {
                break;
            }
            yield(newPos)
            pos = newPos + 1
        }
    }
}



