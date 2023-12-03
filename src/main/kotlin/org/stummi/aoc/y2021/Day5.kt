import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    override fun toString(): String = "$x,$y"
}

data class Line(val start: Point, val end: Point) {
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

data class Matrix(
    val w: Int,
    val h: Int,
) {
    val data = IntArray(w * h)

    fun translatePos(x: Int, y: Int) = x * w + y

    operator fun get(x: Int, y: Int) =
        data[translatePos(x, y)]


    operator fun set(x: Int, y: Int, v: Int) {
        data[translatePos(x, y)] = v
    }

}

fun main() {
    var input = File("/tmp/input.txt").readLines().map {
        it.split(" -> ").map {
            it.split(",").let {
                Point(it[0].toInt(), it[1].toInt());
            }
        }.let {
            Line(it[0], it[1])
        }
    }

    println(input.all { it.isStraightLine() || it.isDiagonal() })

    val m = Matrix(1000, 1000)

    input.forEach { l ->
        if (l.isHorizontal()) {
            rangeHelper(l.start.x, l.end.x).forEach {
                m[it, l.start.y]++; // = m[it, l.start.y] + 1
            }
        } else if (l.isVertical()) {
            rangeHelper(l.start.y, l.end.y).forEach {
                m[l.start.x, it]++; // = m[it, l.start.y] + 1
            }
        } else {
            if (l.start.x - l.end.x == l.start.y - l.end.y) {
                val sx = min(l.start.x, l.end.x)
                val sy = min(l.start.y, l.end.y)
                var steps = abs(l.start.x - l.end.x)
                (0..steps).forEach {
                    m[sx + it, sy + it]++
                }
            } else {
                val sx = max(l.start.x, l.end.x)
                val sy = min(l.start.y, l.end.y)
                var steps = abs(l.start.x - l.end.x)
                (0..steps).forEach {
                    m[sx - it, sy + it]++
                }
            }
        }
    }

    println(m.data.count { it > 1 })
}

fun rangeHelper(a: Int, b: Int) = if (a < b)
    (a..b)
else
    (b..a)
