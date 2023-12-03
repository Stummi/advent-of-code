package org.stummi.aoc.helper

typealias OutOfRangeFunc = (Int, Int) -> Int

class IntMatrix(
    val w: Int,
    val h: Int,
    val data: IntArray = IntArray(w * h),
    val outOfRangeFunc: OutOfRangeFunc = { _, _ -> throw IllegalArgumentException("out of range") }
) {

    constructor(l: List<List<Int>>) : this(l[0].size, l.size, l.flatten().toIntArray())

    fun withOutOfRangeFunc(func: OutOfRangeFunc) = IntMatrix(w, h, data, func)

    private fun posToIdx(x: Int, y: Int): Int {
        check(isInRange(x, y))
        return y * w + x
    }

    private fun isInRange(x: Int, y: Int) =
        (x in (0 until w)) && (y in (0 until h))


    fun row(r: Int) =
        (0 until w).asSequence().map { this[it, r] }

    fun setRow(r: Int, data: Iterable<Int>) {
        data.forEachIndexed { i, v -> this[i, r] = v }
    }

    fun setCol(c: Int, data: Iterable<Int>) {
        data.forEachIndexed { i, v -> this[c, i] = v }
    }


    fun col(c: Int) =
        (0 until h).asSequence().map { this[c, it] }

    fun rows() =
        (0 until h).asSequence().map { row(it).toList() }

    fun cols() =
        (0 until w).asSequence().map { col(it).toList() }


    operator fun get(x: Int, y: Int) = if (isInRange(x, y)) {
        data[posToIdx(x, y)]
    } else {
        outOfRangeFunc(x, y)
    }

    operator fun get(pos: XY) = get(pos.x, pos.y)

    operator fun set(x: Int, y: Int, value: Int) {
        data[posToIdx(x, y)] = value
    }

    operator fun set(pos: XY, value: Int) {
        set(pos.x, pos.y, value)
    }

    fun toString(printer: (Int) -> String): String =
        rows().joinToString("\n") {
            it.joinToString("", transform = printer)
        }

    companion object {
        fun binaryPrinter(trueVal: String, falseVal: String): (Int) -> String = {
            if (it == 0) falseVal else trueVal
        }
    }

}

fun main() {
    val im = IntMatrix(5, 5)

    im.rows().forEach {
        println(it)
    }
}

/*
class Matrix<K>(val w: Int, val h: Int, val data: Array<K>) {

    companion object {
        inline fun <reified K>  newMatrix(w: Int, h: Int): Matrix<K> {
            val f = Array<K>(w * h)

        }
    }
}*/
