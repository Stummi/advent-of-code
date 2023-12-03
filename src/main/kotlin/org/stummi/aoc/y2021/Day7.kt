import java.io.File
import kotlin.math.abs

fun main() {
    val input = File("/tmp/input.txt").readLines().first().split(",").map { it.toInt() }

    //val input = listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14)
    val crabs = input.groupingBy { it }.eachCount()
    val min = crabs.keys.minOrNull()!!
    val max = crabs.keys.maxOrNull()!!
    val toMap = (min..max).associateWith { d ->
        crabs.map { (cd, count) -> count * costFunc2(abs(cd - d)) }.sum()
    }

    println(toMap.minByOrNull { (_, v) -> v })
}

private fun costFunc1(d: Int) = d
private fun costFunc2(d: Int) = d * (d + 1) / 2 // (1..d).sum()
