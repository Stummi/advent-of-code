import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.allPermutations

object Day8 : AdventOfCode(2021, 8) {

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

    val mappings by lazy {
        "abcdefg".toList().allPermutations().map { it.joinToString("") }.toList()
    }

    fun translate(signal: String, mapping: String) =
        signal.map {
            mapping.indexOf(it) + 1
        }.sorted().joinToString("")

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

    val parsedInput by lazy {
        input.lines.map { it ->
            val sp = it.split(" | ")
            sp[0].split(" ") to sp[1].split(" ")
        }
    }

    override val part1
        get() = parsedInput.map { it.second }.sumOf {
            it.map { s -> s.count() }.count { it in listOf(2, 4, 3, 7) }
        }

    override val part2: Any
        get() = parsedInput.sumOf { solve(it) }
}


fun main() {
    Day8.fancyRun()
}


