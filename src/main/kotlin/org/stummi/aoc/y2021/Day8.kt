import java.io.File

//   111
//  2   3
//  2   3
//   444
//  5   6
//  5   6
//   777

val digits = mapOf(
    "36" to 1,
    "13457" to 2,
    "13467" to 3,
    "2346" to 4,
    "12467" to 5,
    "124567" to 6,
    "136" to 7,
    "1234567" to 8,
    "123467" to 9,
    "123567" to 0,
)

fun generateMappings(input: List<Char> = ('a'..'g').toList(), base: String = ""): List<String> {
    if (input.isEmpty()) {
        return listOf(base)
    } else {
        return input.map { generateMappings(input.minus(it), "$base$it") }.flatten()
    }
}

val mappings = generateMappings()

fun translate(signal: String, mapping: String) =
    signal.map {
        mapping.indexOf(it) + 1
    }.sorted().joinToString("")


fun main() {

    //println(translate("badf", "abcdefg"))

    val input = File("/tmp/input.txt").readLines().map { it ->
        val sp = it.split(" | ")
        sp[0].split(" ") to sp[1].split(" ")
    }

    // Solution for 1
    /*println(
        input.map { it.second }.map {
            it.map { s -> s.count() }.count { it == 2 || it == 4 || it == 3 || it == 7 }
        }.sum()
    )
    */

    /*val line  = listOf("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | " +
            "cdfeb fcadb cdfeb cdbaf").map{ it ->
        val sp = it.split(" | ")
        sp[0].split(" ") to sp[1].split(" ")
    }*/

    println(input.map { solve(it) }.sum())

}

fun solve(pair: Pair<List<String>, List<String>>): Int {
    val allInput = pair.first + pair.second
    var tryMappings = mappings
    allInput.forEach { signal ->
        tryMappings = tryMappings.filter { mapping -> translate(signal, mapping) in digits }
    }

    if (tryMappings.size != 1) {
        throw IllegalStateException()
    }

    return pair.second.map { translate(it, tryMappings[0]) }.map { digits[it]!! }.reduce { acc, i -> acc * 10 + i }
}

