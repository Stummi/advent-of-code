import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.IntMatrix
import java.util.BitSet

object Day20 : AdventOfCode(2021, 20) {

    override val part1 =
        enhance(2)

    private fun enhance(iterations: Int) = generateSequence(inputData()) { (code, data) -> code to decode(data, code) }
        .take(iterations + 1)
        .last().second.data.sum()

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
                IntMatrix(it).withOutOfRangeFunc { _, _ -> 0 }
            }
        }

    fun decode(input: IntMatrix, code: BitSet): IntMatrix {
        var ret = IntMatrix(input.w + 2, input.h + 2, outOfRangeFunc = { _, _ -> 1 - input[-2, -2] })
        (-1..input.h).forEach { x ->
            (-1..input.w).forEach { y ->
                var f = 0
                (-1..1).forEach { dy ->
                    (-1..1).forEach { dx ->
                        f = f shl 1 or input[x + dx, y + dy]
                    }
                }
                ret[x + 1, y + 1] = if (code[f]) 1 else 0
            }
        }
        return ret
    }


}

// 5687 -> Too high

fun main() {
    println(Day20.part1)
    println(Day20.part2)
}
