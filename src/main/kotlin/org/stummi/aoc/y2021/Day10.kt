fun main() {
    val input = Unit.javaClass.getResourceAsStream("/2021/10.txt").use {
        it!!.bufferedReader().readLines()
    }

    val ret = input.map {
        parseLine(it)
    }.filterNot { it == 0L }.sorted()

    println(ret)
    println(ret[(ret.size - 1) / 2])
}


fun parseLine(it: String): Long {
    val l = mutableListOf<Char>()

    val pairs = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
    )

    val charPoints = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4,
    )

    it.forEach { c ->
        when {
            c in pairs -> l.add(pairs[c]!!)
            else -> {
                if (l.removeLast() != c) {
                    return 0
                }
            }
        }
    }

    return l.reversed().fold(0L) { acc, c ->
        val l1ret = acc * 5 + charPoints[c]!!
        l1ret
    }
}


fun parseLine_pt1(it: String): Int {
    val l = mutableListOf<Char>()

    val pairs = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
    )

    val charPoints = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )

    it.forEach { c ->
        when {
            c in pairs -> l.add(pairs[c]!!)
            else -> {
                if (l.removeLast() != c) {
                    return charPoints[c]!!
                }
            }
        }
    }
    return 0
}
