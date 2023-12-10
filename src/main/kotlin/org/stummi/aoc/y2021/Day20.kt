import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.IntMatrix
import org.stummi.aoc.helper.Matrix
import org.stummi.aoc.helper.XY

import org.stummi.aoc.helper.withOutOfRangeFunc
import java.util.BitSet

object Day20 : AdventOfCode(2021, 20) {

    override val part1 =
        enhance(2)

    private fun enhance(iterations: Int) = generateSequence(inputData()) { (code, data) -> code to decode(data, code) }
        .take(iterations + 1)
        .last().second.allValues.sum()

    override val part2 = enhance(50)

    fun inputData() =
        input().let {
            BitSet(256).apply {
                it[0].forEachIndexed { idx, v ->
                    set(idx, v == '#')
                }
            } to it.drop(2).map {
                it.map { c ->
                    if (c == '#') 1 else 0
                }
            }.let {
                IntMatrix(it).withOutOfRangeFunc { _ -> 0 }
            }
        }

    fun decode(input: Matrix<Int>, code: BitSet): Matrix<Int> {
        val ret = IntMatrix(input.bounds.width + 2, input.bounds.height + 2)
            .withOutOfRangeFunc { _ -> 1 - input[XY(-2, -2)] }
        (-1..input.bounds.height).forEach { x ->
            (-1..input.bounds.width).forEach { y ->
                var f = 0
                (-1..1).forEach { dy ->
                    (-1..1).forEach { dx ->
                        f = f shl 1 or input[XY(x + dx, y + dy)]
                    }
                }
                ret[XY(x + 1, y + 1)] = if (code[f]) 1 else 0
            }
        }
        return ret
    }
}

fun main() {
    println(Day20.part1)
    println(Day20.part2)
}
