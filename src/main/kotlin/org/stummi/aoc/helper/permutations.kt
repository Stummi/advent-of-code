package org.stummi.aoc.helper


fun <T> Collection<T>.allPermutations(): Sequence<List<T>> {
    return when (this.size) {
        0 -> emptySequence()
        1 -> sequenceOf(toList())
        else -> this.asSequence().flatMap { e -> (this - e).allPermutations().map { listOf(e) + it } }
    }
}

fun <T> Collection<T>.tuplePermutations(includeReversed: Boolean = true): Sequence<Pair<T, T>> {
    if (includeReversed) {
        return this.asSequence().flatMap { a -> (this - a).map { a to it } }
    } else {
        val l = this.toList()
        return (0 until this.size).asSequence().flatMap { ia ->
            (ia + 1 until this.size).asSequence().map { ib -> l[ia] to l[ib] }
        }
    }
}
