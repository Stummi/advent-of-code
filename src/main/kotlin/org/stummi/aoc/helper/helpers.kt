package org.stummi.aoc.helper


fun Iterable<IntRange>.merged(): List<IntRange> {
    // Sort the ranges by their start value in ascending order
    val sortedRanges = this.sortedBy { it.first }

    // Initialize the result list with the first range in the sorted list
    val result = mutableListOf(sortedRanges[0])

    for (i in 1 until sortedRanges.size) {
        val currentRange = sortedRanges[i]
        val previousRange = result.last()

        // If the current range overlaps or is consecutive with the previous range,
        // merge the current range with the previous range
        if (currentRange.first <= previousRange.last + 1) {
            result[result.lastIndex] = previousRange.first..maxOf(previousRange.last, currentRange.last)
        } else {
            // Otherwise, add the current range to the result list
            result.add(currentRange)
        }
    }

    return result
}

fun <T> Iterable<T>.partitionBy(predicate: (T) -> Boolean): List<List<T>> {
    val result = mutableListOf<MutableList<T>>()
    var currentList = mutableListOf<T>()
    result.add(currentList)
    this.forEach {
        if (predicate(it)) {
            currentList = mutableListOf()
            result.add(currentList)
        } else {
            currentList.add(it)
        }
    }
    return result
}

fun String.splitToInts(): List<Int> {
    val regex = "\\d+".toRegex()
    return regex.findAll(this).map { it.value.toInt() }.toList()
}
