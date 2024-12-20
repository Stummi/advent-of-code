package org.stummi.aoc.helper

import kotlin.math.absoluteValue
import kotlin.math.max

data class XY(
    val x: Int,
    val y: Int,
) {
    val up get() = copy(y = y - 1)
    val down get() = copy(y = y + 1)
    val left get() = copy(x = x - 1)
    val right get() = copy(x = x + 1)

    val upLeft get() = XY(x - 1, y - 1)
    val upRight get() = XY(x + 1, y - 1)
    val downLeft get() = XY(x - 1, y + 1)
    val downRight get() = XY(x + 1, y + 1)

    val negativeValue get() = -this

    val rotateRight get() = XY(-y, x)
    val rotateLeft get() = XY(y, -x)

    fun move(x: Int = 0, y: Int = 0): XY {
        return XY(this.x + x, this.y + y)
    }

    fun orthogonalNeighbours() = sequenceOf(up, left, down, right)
    fun diagonalNeighbours() = sequenceOf(upLeft, upRight, downRight, downLeft)

    fun mooreNeighbours() = orthogonalNeighbours() + diagonalNeighbours()

    fun orthogonalDistanceTo(goal: XY) = (x - goal.x).absoluteValue + (y - goal.y).absoluteValue
    fun mooreDistanceTo(goal: XY) = max((x - goal.x).absoluteValue, (y - goal.y).absoluteValue)

    operator fun rangeTo(other: XY) = listOf(this, other).bounds()
    operator fun rangeUntil(xy: XY) = rangeTo(xy.upLeft)

    infix fun until(xy: XY) = this..xy.upLeft

    override fun toString() = "($x,$y)"

    operator fun unaryMinus() = XY(-x, -y)
    operator fun plus(move: XY) = XY(x + move.x, y + move.y)
    operator fun minus(move: XY) = XY(x - move.x, y - move.y)

    fun translate(move: XY) = this + move

    companion object {
        val ZERO = XY(0, 0)
    }
}

data class XYRange(val topLeft: XY, val bottomRight: XY) {
    val topRight get() = XY(topLeft.y, bottomRight.x)
    val bottomLeft get() = XY(topLeft.x, bottomRight.y)

    val top get() = topLeft.y
    val left: Int get() = topLeft.x
    val right: Int get() = bottomRight.x
    val bottom: Int get() = bottomRight.y

    val xRange get() = left..right
    val yRange get() = top..bottom

    val width get() = right - left + 1
    val height get() = bottom - top + 1
    val area: Int get() = width * height

    val areaAsLong: Long get() = width.toLong() * height


    enum class IterationOrder(val func: XYRange.() -> Sequence<XY>) {
        TB_LR(yThenX(false, false)),
        TB_RL(yThenX(true, false)),
        BT_LR(yThenX(false, true)),
        BT_RL(yThenX(true, true)),
        LR_TB(xThenY(false, false)),
        RL_TB(xThenY(true, false)),
        LR_BT(xThenY(false, true)),
        RL_BT(xThenY(true, true));

        operator fun invoke(r: XYRange) =
            func(r)

    }

    fun asSequence(order: IterationOrder = IterationOrder.TB_LR) = order(this)

    fun outLineAsSequence(): Sequence<XY> {
        val topLine = xRange.asSequence().map { XY(it, top) }
        val bottomLine = xRange.asSequence().map { XY(it, bottom) }
        val innerY = (top + 1) until bottom
        val leftLine = innerY.asSequence().map { XY(left, it) }
        val rightLine = innerY.asSequence().map { XY(right, it) }

        return (topLine + bottomLine + leftLine + rightLine)
    }

    operator fun contains(point: XY) = point.x in xRange && point.y in yRange
    operator fun plus(point: XY) = listOf(topLeft, bottomRight, point).bounds()
    operator fun plus(bounds: XYRange) = listOf(topLeft, bottomRight, bounds.topLeft, bounds.bottomRight).bounds()

    override fun toString() = "[$topLeft..${bottomRight}]"

    fun printAsMap(mapFunc: (XY) -> Any) {
        (top..bottom).forEach { y ->
            (left..right).forEach { x ->
                print(mapFunc(XY(x, y)))
            }
            println()
        }
    }

    companion object {
        private fun IntRange.maybeReverse(b: Boolean) = if (b) this.reversed() else this
        private fun xThenY(reverseX: Boolean, reverseY: Boolean): XYRange.() -> Sequence<XY> = {
            xRange
                .maybeReverse(reverseX)
                .asSequence().map { x ->
                    yRange
                        .maybeReverse(reverseY)
                        .asSequence().map { y -> XY(x, y) }
                }.flatten()
        }

        private fun yThenX(reverseX: Boolean, reverseY: Boolean): XYRange.() -> Sequence<XY> = {
            yRange
                .maybeReverse(reverseY)
                .asSequence().map { y ->
                    xRange
                        .maybeReverse(reverseX)
                        .asSequence().map { x -> XY(x, y) }
                }.flatten()
        }
    }
}

fun Iterable<XY>.bounds(): XYRange {
    val minX = this.minOf { it.x }
    val maxX = this.maxOf { it.x }
    val minY = this.minOf { it.y }
    val maxY = this.maxOf { it.y }
    return XYRange(XY(minX, minY), XY(maxX, maxY))
}
