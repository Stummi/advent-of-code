import java.io.File
import java.math.BigInteger

fun main() {
    val input =
        File("/tmp/input.txt").readLines().first().split(",").map { it.toInt() }.toMutableList()
    //val input = mutableListOf(3,4,3,1,2)

    var group =
        input.groupingBy { it }.eachCount().map { (k, v) -> k to BigInteger.valueOf(v.toLong()) }.toMap().toMutableMap()


    repeat(256) {
        group = group.map { (k, v) -> k - 1 to v }.toMap().toMutableMap()
        val newFish = group.remove(-1) ?: BigInteger.ZERO
        group[6] = (group[6] ?: BigInteger.ZERO).plus(newFish)
        group[8] = newFish

        println("day: $it: ${group.values.reduce(BigInteger::plus)}")
    }

    println(input.size)
}
