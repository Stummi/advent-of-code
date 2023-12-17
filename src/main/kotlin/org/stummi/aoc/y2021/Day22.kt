import org.stummi.aoc.AdventOfCode
import java.lang.Integer.min
import kotlin.math.max
import kotlin.time.ExperimentalTime

object Day22 : AdventOfCode(2021, 22) {

    data class XYZ(
        val x: Int,
        val y: Int,
        val z: Int
    )

    data class Cube(
        val x: IntRange,
        val y: IntRange,
        val z: IntRange
    ) {
        fun isEmpty() =
            x.isEmpty() || y.isEmpty() || z.isEmpty()

        fun volume() = if (isEmpty()) {
            0L
        } else {
            listOf(x, y, z).map { it.last - it.first + 1 }.fold(1L) { a, b -> a * b }
        }

        fun intersect(other: Cube) = Cube(
            intersect(x, other.x),
            intersect(y, other.y),
            intersect(z, other.z),
        )

        fun splitBy(other: Cube): List<Cube> {
            val xIntersect = rangeIntersections(this.x, other.x)
            val yIntersect = rangeIntersections(this.y, other.y)
            val zIntersect = rangeIntersections(this.z, other.z)


            return xIntersect.flatMap { x ->
                yIntersect.flatMap { y ->
                    zIntersect.map { z -> Cube(x, y, z) }
                }
            }
        }


        fun rangeIntersections(toSplit: IntRange, other: IntRange): List<IntRange> {
            if (toSplit.first > other.last || toSplit.last < other.first) {
                return listOf(toSplit)
            }

            if (toSplit.first >= other.first && toSplit.last <= other.last) return listOf(toSplit)

            if (toSplit.first < other.first && toSplit.last > other.last) {
                return listOf(toSplit.first until other.first, other, (other.last + 1)..toSplit.last)
            }

            if (toSplit.first >= other.first) {
                return listOf((toSplit.first..other.last), (other.last + 1)..toSplit.last)
            }

            if (toSplit.last <= other.last) {
                return listOf((toSplit.first until other.first), (other.first)..toSplit.last)
            }


            throw IllegalArgumentException()
        }

    }

    class CubeMap {
        val cubes = mutableSetOf<Cube>()

        fun insert(c: Cube) {
            if (c.isEmpty() || cubes.contains(c)) {
                return
            }

            var intersect = cubes.find { !it.intersect(c).isEmpty() }

            if (intersect == null) {
                cubes.add(c)
                return
            }
            //println("$c => $intersect")
            //println("  ${intersect.splitBy(c)}")

            //cubes.remove(intersect)
            //cubes.addAll(intersect.splitBy(c))
            val splitBy = c.splitBy(intersect)

            //println("  s: ${splitBy}")
            splitBy.filter { it.intersect(intersect).isEmpty() }
                .forEach {
                    insert(it)
                }
        }


        fun remove(c: Cube) {
            if (c.isEmpty() || cubes.remove(c)) {
                return
            }

            cubes.filterNot { it.intersect(c).isEmpty() }.forEach { hit ->
                cubes.remove(hit)
                cubes.addAll(hit.splitBy(c).filter { it.intersect(c).isEmpty() })
            }

        }
    }

    override val part1: Any
        get() {
            val c = Cube(-50..50, -50..50, -50..50)
            return solve(inputData.filterNot {
                it.cube.intersect(c).isEmpty()
            })
        }

    private fun solve(input: List<RebootSteap>): Long {
        val m = CubeMap()
        input.forEach {
            if (it.toggle) {
                m.insert(it.cube)
            } else {
                m.remove(it.cube)
            }
        }
        return m.cubes.sumOf { it.volume() }
    }

    override val part2: Any
        get() {
            return solve(inputData)
        }

    val inputData = inputLines().map {
        val spl0 = it.split(" ")
        val state = spl0[0] == "on"

        val (xr, yr, zr) =
            spl0[1].split(",").map { it.split("=")[1] }.map { it.split("..").map { it.toInt() } }.map { (it[0]..it[1]) }
        RebootSteap(state, Cube(xr, yr, zr))
    }

    fun intersect(i: IntRange, j: IntRange) =
        (max(i.first, j.first)..min(i.last, j.last))


    data class RebootSteap(
        val toggle: Boolean,
        val cube: Cube,
    )

}

@OptIn(ExperimentalTime::class)
fun main() {
    println(Day22.part1)
    println(Day22.part2)
}
