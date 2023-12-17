import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.IntMatrix
import org.stummi.aoc.helper.XY
import kotlin.math.abs

object Day5 : AdventOfCode(2021, 5) {
    data class Line(val start: XY, val end: XY) {
        fun isStraightLine() =
            start.x == end.x || start.y == end.y

        fun isHorizontal() =
            start.y == end.y

        fun isVertical() =
            start.x == end.x

        fun isDiagonal(): Boolean {
            return abs(start.y - end.y) == abs(start.x - end.x)
        }

        override fun toString() = "$start -> $end"
    }

    private val parsedInput by lazy {
        input.lines.map {
            it.split(" -> ").map {
                it.split(",").let {
                    XY(it[0].toInt(), it[1].toInt())
                }
            }.let {
                Line(it[0], it[1])
            }
        }
    }

    fun solve(considerDiagonals: Boolean): Int {
        val m = IntMatrix(XY.ZERO..XY(1000,1000))

        parsedInput.forEach { l ->
            if (l.isHorizontal()) {
                rangeHelper(l.start.x, l.end.x).forEach {
                    m[XY(it, l.start.y)]++; // = m[it, l.start.y] + 1
                }
            } else if (l.isVertical()) {
                rangeHelper(l.start.y, l.end.y).forEach {
                    m[XY(l.start.x, it)]++; // = m[it, l.start.y] + 1
                }
            } else if (considerDiagonals) {
                if (l.start.x - l.end.x == l.start.y - l.end.y) {
                    val sx = minOf(l.start.x, l.end.x)
                    val sy = minOf(l.start.y, l.end.y)
                    val steps = abs(l.start.x - l.end.x)
                    (0..steps).forEach {
                        m[XY(sx + it, sy + it)]++
                    }
                } else {
                    val sx = maxOf(l.start.x, l.end.x)
                    val sy = minOf(l.start.y, l.end.y)
                    val steps = abs(l.start.x - l.end.x)
                    (0..steps).forEach {
                        m[XY(sx - it, sy + it)]++
                    }
                }
            }
        }
        return m.allValues.count { it > 1 }
    }

    fun rangeHelper(a: Int, b: Int) = if (a < b)
        (a..b)
    else
        (b..a)

    override val part1: Any
        get() = solve(false)

    override val part2: Any
        get() = solve(true)

}

fun main() {
    Day5.fancyRun()
}
