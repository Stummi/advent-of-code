package org.stummi.aoc

import org.stummi.aoc.api.AocApi
import org.stummi.aoc.helper.CharMatrix
import java.io.IOException
import java.io.InputStream
import java.time.Instant
import java.util.Random
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue
import kotlin.time.toKotlinDuration

class InputMissingException : IOException()

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
        set(value) {
            field = value
            localLazies.forEach { it.reset() }
        }

    private val api = Api()

    var disablePrint: Boolean = false

    fun println(message: Any? = "") {
        if (!disablePrint) {
            kotlin.io.println(message)
        }
    }

    fun print(message: Any? = "") {
        if (!disablePrint) {
            kotlin.io.print(message)
        }
    }

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

    val input get() = ((currentSample?.input) ?: api)
    fun inputLines() = input.lines


    interface Input {
        val lines: List<String>
        val matrix: CharMatrix get() = CharMatrix.fromLines(lines)
    }

    data class Raw(val s: String) : Input {
        override val lines get() = listOf(s)
        override fun toString() = s
    }

    inner class Resource(val name: String) : Input {
        override val lines
            get() =
                (resourceAsStream(name) ?: throw InputMissingException())
                    .use {
                        it.bufferedReader().readLines()
                    }

        override fun toString(): String = "<$name>"
    }

    private fun resourceAsStream(name: String): InputStream? =
        Unit.javaClass.getResourceAsStream("/$year/${day}_$name.txt")

    inner class Api : Input {
        override val lines by kotlin.lazy { AocApi.input(year, day) }
        override fun toString(): String = "(API)"
    }

    val localLazies = mutableListOf<LocalLazyValue<*>>()

    inner class LocalLazyValue<T>(
        private val input: () -> T,
        private var delegate: Lazy<T> = kotlin.lazy(input)
    ) : Lazy<T> {
        init {
            localLazies.add(this)
        }

        override val value: T
            get() = delegate.value

        override fun isInitialized(): Boolean = delegate.isInitialized()

        fun reset() {
            delegate = kotlin.lazy(input)
        }
    }

    /**
     * This "overrides" kotlin.lazy with a mechanism that resets for every new input
     */
    fun <T> lazy(input: () -> T): Lazy<T> = LocalLazyValue(input)

    class Sample(val input: Input, val result1: Any?, val result2: Any?, val name: String, val additionalData: Any?)


    @OptIn(ExperimentalTime::class)
    fun fancyRun(includeReal: Boolean = true) {

        fun printPart(p: Int, expected: Any?, result: () -> Any?): Boolean {
            val result = runCatching { measureTimedValue { result() } }
            print(" Part $p: ")
            var wrong = false
            if (result.isSuccess) {
                val (res, duration) = result.getOrThrow()
                when (expected?.toString()) {
                    null -> print("    ")
                    res.toString() -> print("  ✔️ ")
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
                inputLines()
                if (it.result1 != Unit)
                    samplesPass1 = samplesPass1 and printPart(1, it.result1) { part1 }
                if (it.result2 != Unit)
                    samplesPass2 = samplesPass2 and printPart(2, it.result2) { part2 }
                println("----")
            }
        }

        if (!includeReal) {
            return
        }
        currentSample = null
        println("Real Run:")
        inputLines() // read input into cache so ApiAccess is nt part of timing data
        printPart(1, null) { part1 }
        printPart(2, null) { part2 }
    }
}

fun List<AdventOfCode>.runAll() {
    println("       Day | Part 1                         | Part 2 ")

    val availableCpus = Runtime.getRuntime().availableProcessors()
    val pool = Executors.newFixedThreadPool(availableCpus)

    val futures = mutableMapOf<Pair<AdventOfCode, Int>, Future<TimedValue<Result<Any>>>>()

    forEach {
        it.disablePrint = true
        val future1 = pool.submit<TimedValue<Result<Any>>> { measureTimedValue { runCatching { it.part1 } } }
        val future2 = pool.submit<TimedValue<Result<Any>>> { measureTimedValue { runCatching { it.part2 } } }
        futures[it to 1] = future1
        futures[it to 2] = future2
    }

    var totalTime = Duration.ZERO
    var totalClockTime = Duration.ZERO
    var maxTime = Duration.ZERO
    lateinit var maxTimeTask: Pair<AdventOfCode, Int>

    var allStart = Instant.now()
    forEach {
//        it.input() // read input into cache so ApiAccess is nt part of timing data
        print(String.format("%10s", "${it.year}/${it.day}"))

        fun printResult(p: Result<Any?>, t: Duration) = if (p.isSuccess) {
            val args = p.getOrNull().toString()
            val prettyResult = if (args.contains("\n") || args.length > 16)
                "(...<${args.length}>)"
            else
                args

            var time = t.inWholeMicroseconds.toDouble()
            val timeUnit: String
            if (time > 1e6) {
                time /= 1e3
                timeUnit = "s"
            } else {
                timeUnit = "ms"
            }

            print(String.format(" | %16s (%8.3f %2s)", prettyResult, time / 1e3, timeUnit))
        } else {
            print(
                String.format(
                    " | %30s",
                    if (p.exceptionOrNull() is NotImplementedError) "TODO()" else "ERROR: ${p.exceptionOrNull()?.message}".let {
                        if (it.length > 30) it.substring(0, 27) + "..." else it
                    })
            )
        }

        val (p1, t1) = futures[it to 1]!!.get()
        printResult(p1, t1)
        val (p2, t2) = futures[it to 2]!!.get()
        printResult(p2, t2)

        totalTime += t1
        totalTime += t2

        if (t1 > maxTime) {
            maxTime = t1
            maxTimeTask = it to 1
        }

        if (t2 > maxTime) {
            maxTime = t2
            maxTimeTask = it to 2
        }

        println()
    }
    var allEnd = Instant.now()!!
    println("Clock Time: ${java.time.Duration.between(allStart, allEnd).toKotlinDuration()}")
    println("Total Time: $totalTime")
    println("Max Time: $maxTime - ${maxTimeTask.first.year}/${maxTimeTask.first.day} pt ${maxTimeTask.second}")
}
