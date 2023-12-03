package org.stummi.aoc.helper

fun <A, B> Pair<A, B>.swap() = this.second to this.first

fun <X : Comparable<X>> Pair<X, X>.sorted() = if (this.first > this.second) this.swap() else this
fun <X : Comparable<X>> Pair<X, X>.high() = if (this.first > this.second) this.first else this.second
fun <X : Comparable<X>> Pair<X, X>.low() = if (this.first < this.second) this.first else this.second

fun <X> Pair<X, X>.sorted(comparator: Comparator<X>) =
    if (comparator.compare(this.first, this.second) < 0)
        this
    else
        this.swap()

