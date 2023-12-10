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

fun <X, Y : Comparable<Y>> Pair<X, X>.sortedBy(func: (X) -> Y) =
    if (func(this.first) > func(this.second)) this.swap() else this

fun <X, Y> Pair<X, X>.map(func: (X) -> Y) = func(first) to func(second)
