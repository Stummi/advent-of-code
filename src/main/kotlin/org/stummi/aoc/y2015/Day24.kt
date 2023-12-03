package org.stummi.aoc.y2015

import java.math.BigInteger


fun main() {
    val weights = Unit.javaClass.getResourceAsStream("/2015/24.txt").use {
        it!!.bufferedReader().readLines()
    }.map {
        it.toInt()
    }

    val weightSum = weights.sum()
    println(weightSum)
    val search = weightSum / 4


    val allPossibleCombinations = findPossibleCombinations(search, weights.sortedDescending())

    val mapByAmount = allPossibleCombinations.groupBy { it.count() }

    val mapByProduct = mapByAmount[mapByAmount.keys.minOrNull()!!]!!.groupBy {
        it.fold(BigInteger.ONE) { a, b -> a.times(b.toBigInteger()) }
    }

    val smallestProduct = mapByProduct.keys.minOrNull()!!

    //val p1 = mapByProduct[smallestProduct]!![0]
    //println(findPossibleCombinations(search, weights - p1).count())
    println(smallestProduct)

    /*
    val minKey = productMap.keys.minOrNull()!!
    val p1 = productMap[minKey]!!.first()
    val remainingWeights = weights - p1

    findPossibleCombinations(search, remainingWeights).forEach {
        println(it)
    }

    allPossibleCombinations.map {  p1 ->
        allPossibleCombinations.filter { p2 -> p2.none { it in p1 } }.map { p2 -> p1 to p2 }
    }.flatten().map { (p1, p2)  ->
        allPossibleCombinations.filter { p3 -> p3.none { it in p1 || it in p2 } }
            .map { p3 -> Triple(p1, p2, p3) }
    }.flatten().count().let {
        println(it)
    }
*/
}

fun findPossibleCombinations(search: Int, weights: List<Int>): Sequence<Set<Int>> {
    if (search == 0) {
        return sequenceOf(emptySet())
    }

    if (search < 0) {
        return emptySequence()
    }

    return weights.asSequence().flatMapIndexed { widx, w ->
        findPossibleCombinations(search - w, weights.drop(widx + 1)).map {
            it + w
        }
    }
}

