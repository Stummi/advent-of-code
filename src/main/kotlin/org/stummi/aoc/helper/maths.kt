package org.stummi.aoc.helper

fun Iterable<Long>.lcm(): Long {
    val lcmPrimes = mutableMapOf<Long, Long>()

    this.forEach {
        it.primes().forEach { (p, m) ->
            lcmPrimes.merge(p, m) { a, b -> maxOf(a, b) }
        }
    }

    return lcmPrimes.map { (p, m) -> p pow m }.reduce { a, b -> a * b }
}

fun Long.primes(): Map<Long, Long> {
    return Sieve.primesInRange(0L..this).filter {
        this % it == 0L
    }.associateWith {
        var cnt = 0L
        var tmp = this
        while (tmp % it == 0L) {
            tmp /= it
            ++cnt
        }
        cnt
    }
}

infix fun Long.pow(exp: Long): Long {
    return when (exp) {
        0L -> 1L
        1L -> this
        else -> {
            generateSequence { this }.take(exp.toInt()).reduce { a, b -> a * b }
        }
    }
}
