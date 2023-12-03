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
