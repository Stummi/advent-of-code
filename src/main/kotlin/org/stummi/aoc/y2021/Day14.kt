import java.math.BigInteger

fun main() {
    val lines = Unit.javaClass.getResourceAsStream("/2021/14.txt").use {
        it!!.bufferedReader().readLines()
    }

    val tpl = lines[0]

    val insertions = lines.drop(2).map { it.split(" -> ") }.associate {
        (it[0][0] to it[0][1]) to it[1][0]
    }

    val cachedResults = mutableMapOf<Pair<Pair<Char, Char>, Int>, Map<Char, BigInteger>>()
    val rounds = 40

    val map = tpl.windowed(2).map {
        countByCharacter(it[0], it[1], rounds, insertions, cachedResults)
//          .also {
//                println("Step: $it - ${cachedResults.size}")
//            }
    }.let {
        mergeMaps(
            *it.toTypedArray(),
            tpl.groupingBy { it }.eachCount().mapValues { BigInteger.valueOf(it.value.toLong()) })
    }

    println(cachedResults.size)

    val min = map.minOf { it.value }
    val max = map.maxOf { it.value }

    println(max - min)

    println(map.values.reduce(BigInteger::plus))
}

fun countByCharacter(
    left: Char,
    right: Char,
    steps: Int,
    insertions: Map<Pair<Char, Char>, Char>,
    cachedResults: MutableMap<Pair<Pair<Char, Char>, Int>, Map<Char, BigInteger>>
): Map<Char, BigInteger> {
    return if (steps == 0) {
        emptyMap()
    } else {
        (insertions[left to right] ?: return emptyMap()).let {
            val cacheKey = (left to right) to steps
            return cachedResults[cacheKey]
                ?: mergeMaps(
                    mapOf(it to BigInteger.ONE),
                    countByCharacter(left, it, steps - 1, insertions, cachedResults),
                    countByCharacter(it, right, steps - 1, insertions, cachedResults)
                ).also {
                    cachedResults[cacheKey] = it
                }
        }
    }
}

fun mergeMaps(vararg m: Map<Char, BigInteger>): Map<Char, BigInteger> =
    m.flatMap { it.keys }.associateWith {
        m.mapNotNull { m -> m[it] }.reduce { bi1, bi2 -> bi1.plus(bi2) }
    }

