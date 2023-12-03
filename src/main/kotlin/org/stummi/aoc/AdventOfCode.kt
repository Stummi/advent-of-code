package org.stummi.aoc

import org.stummi.aoc.api.AocApi
import java.io.IOException
import java.io.InputStream
import java.util.Random
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class InputMissingException : IOException() {}

abstract class AdventOfCode(val year: Int, val day: Int) {
    open val part1: Any
        get() {
            TODO("part1 not yet implemented")
        }
    open val part2: Any
        get() {
            TODO("part2 not yet implemented")
        }

    private val samples = mutableListOf<Sample>()
    protected var currentSample: Sample? = null
    private val api = Api()

    protected inline fun <reified T> T.orSample() = (currentSample?.additionalData as? T) ?: this

    init {
        if (resourceAsStream("demo") != null) {
            resourceSample("demo")
        }
    }

    protected val sample get() = currentSample != null

    fun rawSample(input: String, result1: Any? = null, result2: Any? = null, additionalData: Any? = null) {
        samples.add(Sample(Raw(input), result1, result2, "\"${shorten(input)}\"", additionalData))
    }

    private fun shorten(input: String) = input.takeIf { it.length < 15 } ?: (input.substring(0, 12) + "...")

    fun resourceSample(input: String, result1: Any? = null, result2: Any? = null, additionalData: Any? = null) {
        samples.removeIf { it.input is Resource && it.input.name == input }
        samples.add(Sample(Resource(input), result1, result2, "<${shorten(input)}>", additionalData))
    }

    open fun input() = ((currentSample?.input) ?: api).read()

    fun interface Input {
        fun read(): List<String>
    }

    data class Raw(val s: String) : Input {
        override fun read() = listOf(s)
        override fun toString() = s
    }

    inner class Resource(val name: String) : Input {
        override fun read(): List<String> =
            (resourceAsStream(name) ?: throw InputMissingException())
                .use {
                    it.bufferedReader().readLines()
                }

        override fun toString(): String = "<$name>"
    }

    private fun resourceAsStream(name: String): InputStream? =
        Unit.javaClass.getResourceAsStream("/$year/${day}_$name.txt")

    inner class Api : Input {
        private val cachedInput by lazy { AocApi.input(year, day) }

        override fun read() = cachedInput
        override fun toString(): String = "(API)"
    }

    class Sample(val input: Input, val result1: Any?, val result2: Any?, val name: String, val additionalData: Any?)


    @OptIn(ExperimentalTime::class)
    final fun fancyRun(includeReal: Boolean = true) {

        fun printPart(p: Int, expected: Any?, result: () -> Any?): Boolean {
            val result = runCatching { measureTimedValue { result() } }
            print(" Part $p: ")
            var wrong = false;
            if (result.isSuccess) {
                val (res, duration) = result.getOrThrow()
                when (expected) {
                    null -> print("    ")
                    res -> print("  ✔️ ")
                    else -> {
                        print("  ❌ ")
                        wrong = true
                        //print("     expected: $expected")
                        //return false;
                    }
                }
                if (res.toString().contains("\n")) {
                    println("($duration)")
                    println("$res")
                } else {
                    println("$res ($duration)")
                    if (wrong) {
                        println("     expected: $expected")
                    }
                }
                return !wrong
            } else {
                when (result.exceptionOrNull()) {
                    is NotImplementedError -> println("TODO()")
                    else -> {
                        println("ERROR!")
                        result.exceptionOrNull()!!.printStackTrace(System.out)
                    }
                }
                return false
            }
        }

        val stars = "⋆★☆✢✥✦✧❂❉✯"
        val r = Random(year * 1000L + day)
        fun star() {
            if (r.nextInt(4) == 1)
                print(stars[r.nextInt(stars.length)])
            else
                print(" ")
        }

        fun stars(i: Int) {
            repeat(i) { star() }
        }
        stars(32)
        println()
        stars(4)
        print(" Advent Of Code $year $day ")
        stars(4)
        println()
        stars(32)
        println()
        println()
        var samplesPass1 = true
        var samplesPass2 = true
        if (samples.isEmpty()) {
            println("No Samples")
            println("----")
        } else {
            samples.forEach {
                println("Sample: ${it.name}")
                currentSample = it
                input()
                samplesPass1 = samplesPass1 and printPart(1, it.result1) { part1 }
                samplesPass2 = samplesPass2 and printPart(2, it.result2) { part2 }
                println("----")
            }
        }

        if (!includeReal) {
            return
        }
        currentSample = null
        println("Real Run:");
        input() // read input into cache so ApiAccess is nt part of timing data
        printPart(1, null) { part1 }
        printPart(2, null) { part2 }
    }
}
