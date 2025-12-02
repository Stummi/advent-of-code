package org.stummi.aoc.helper

import java.math.BigInteger
import java.util.BitSet
import java.util.Objects

interface Matrix<T> {
    val bounds: XYRange
    operator fun get(xy: XY): T
    operator fun set(xy: XY, value: T)

    fun find(value: T): XY
    fun findAll(value: T): Sequence<XY>

    val allValues: Sequence<T>
    val width: Int get() = bounds.width
    val height: Int get() = bounds.height

    fun print(f: (T) -> Any) {
        bounds.printAsMap { f(get(it)) }
    }
}

abstract class AbstractArrayMatrix<T>(
    override val bounds: XYRange
) : Matrix<T> {
    protected abstract fun getByIndex(index: Int): T
    protected abstract fun setByIndex(index: Int, value: T)

    override val allValues: Sequence<T>
        get() = (0..bounds.area).asSequence().map {
            getByIndex(it)
        }

    private fun posToIdx(xy: XY): Int {
        check(xy in bounds) { "$xy is out of bounds" }
        return xy.translate(bounds.topLeft.negativeValue).let {
            it.y * width + it.x
        }
    }

    private fun idxToPos(idx: Int) = XY(idx % width, idx / width).translate(bounds.topLeft)

    fun row(r: Int) =
        bounds.xRange.asSequence().map { this[XY(it, r)] }

    fun col(c: Int) =
        bounds.yRange.asSequence().map { this[XY(c, it)] }


    fun setRow(r: Int, data: Iterable<T>) {
        data.forEachIndexed { i, v -> this[XY(i + bounds.left, r)] = v }
    }

    fun setCol(c: Int, data: Iterable<T>) {
        data.forEachIndexed { i, v -> this[XY(c, i + bounds.top)] = v }
    }

    fun rows() =
        bounds.yRange.asSequence().map { row(it).toList() }

    fun cols() =
        bounds.xRange.asSequence().map { col(it).toList() }

    override operator fun get(xy: XY) = getByIndex(posToIdx(xy))
    override operator fun set(xy: XY, value: T) = setByIndex(posToIdx(xy), value)

    override fun find(value: T): XY =
        idxToPos(allValues.indexOfFirst { it == value })

    override fun findAll(value: T): Sequence<XY> =
        allValues.withIndex().filter { (_, v) -> v == value }.map { (idx, _) -> idxToPos(idx) }

    override fun toString(): String = "Matrix[$width,$height]"

    /**
     * used for equals and hashcode
     */
    protected abstract fun rawDataObject(): Any

    abstract fun clone(): AbstractArrayMatrix<T>

    override fun hashCode(): Int = Objects.hash(bounds, rawDataHash())
    override fun equals(other: Any?): Boolean =
        (other is AbstractArrayMatrix<*>) &&
                other.bounds == bounds &&
                rawDataEquals(other.rawDataObject())

    protected open fun rawDataEquals(other: Any) = other == rawDataHash()
    protected open fun rawDataHash(): Any = rawDataObject()
}

class IntMatrix(
    override val bounds: XYRange,
    val values: IntArray = IntArray(bounds.area),
) : AbstractArrayMatrix<Int>(bounds) {
    constructor(rows: List<List<Int>>) : this(
        XY.ZERO..<XY(rows[0].size, rows.size),
        rows.flatten().toIntArray()
    )

    constructor(width: Int, height: Int) : this(XY.ZERO..<XY(width, height))

    override fun getByIndex(index: Int): Int =
        values[index]

    override fun setByIndex(index: Int, value: Int) {
        values[index] = value
    }

    override val allValues: Sequence<Int>
        get() = values.asSequence()

    override fun rawDataObject(): Any = values

    override fun clone() = IntMatrix(bounds, values.clone())

    override fun rawDataEquals(other: Any): Boolean =
        (other as? IntArray)?.contentEquals(values) ?: false

    override fun rawDataHash(): Any =
        values.contentHashCode()


}

class CharMatrix(
    override val bounds: XYRange,
    val values: CharArray = CharArray(bounds.area),
) : AbstractArrayMatrix<Char>(bounds) {

    companion object {
        fun fromLines(lines: List<String>): CharMatrix {
            if (lines.isEmpty()) {
                return CharMatrix(XY.ZERO..<XY.ZERO)
            }

            val firstLength = lines.first().length
            check(lines.all { it.length == firstLength }) { "All lines must have same length" }
            val bounds = XY(0, 0)..<XY(firstLength, lines.size)
            return CharMatrix(
                bounds,
                lines.flatMap { it.toCharArray().asSequence() }.toCharArray()
            )
        }
    }


    override val allValues: Sequence<Char>
        get() = values.asSequence()

    override fun getByIndex(index: Int) = values[index]
    override fun setByIndex(index: Int, value: Char) {
        values[index] = value
    }

    fun print() {
        bounds.printAsMap { get(it) }
    }

    override fun rawDataObject(): Any = values
    override fun clone() = CharMatrix(bounds, values.clone())

    override fun rawDataEquals(other: Any): Boolean =
        (other as? CharArray)?.contentEquals(values) ?: false

    override fun rawDataHash() =
        values.contentHashCode()

    fun toIntMatrix(mapFunc: (Char) -> Int = { it.digitToInt() }): IntMatrix {
        val ints = IntArray(values.size) {
            mapFunc(values[it])
        }

        return IntMatrix(bounds, ints)
    }
}

class BoolMatrix(
    override val bounds: XYRange,
    val values: BitSet = BitSet(bounds.area)
) : AbstractArrayMatrix<Boolean>(bounds) {
    override fun getByIndex(index: Int): Boolean =
        values[index]

    override fun setByIndex(index: Int, value: Boolean) {
        values[index] = value
    }

    override fun rawDataObject(): Any = values
    override fun clone() = BoolMatrix(bounds, values.clone() as BitSet)

    fun print() {
        super.print { if(it) '#' else '.' }
    }
}


class OutOfRangeHandlingMatrix<T>(
    private val delegate: Matrix<T>,
    private val outOfRangeFunc: Matrix<T>.(xy: XY) -> T
) : Matrix<T> by delegate {
    override fun get(xy: XY): T {
        return if (xy in bounds) {
            delegate[xy]
        } else {
            outOfRangeFunc(xy)
        }
    }
}

fun <T> Matrix<T>.withOutOfRangeFunc(func: Matrix<T>.(xy: XY) -> T): Matrix<T> = OutOfRangeHandlingMatrix(this, func)
