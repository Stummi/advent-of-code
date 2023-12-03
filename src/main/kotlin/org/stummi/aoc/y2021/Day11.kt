fun main() {
    val input = Unit.javaClass.getResourceAsStream("/2021/11.txt").use {
        it!!.bufferedReader().readLines()
    }.map { it.map(Char::digitToInt).toMutableList() }

    var steps = 0
    var flashSum = 0

    do {
        ++steps
        flashSum += step(input)
    } while (!(input.all { it.all { it == 0 } }))
    printBoard(input)
    println(flashSum)
    println(steps)
}

fun printBoard(input: List<MutableList<Int>>) {
    input.forEach { row ->
        row.forEach {
            print(it)
        }
        println()
    }
}

fun step(input: List<MutableList<Int>>): Int {
    val h = input.size
    val w = input[0].size

    (0 until w).forEach { x ->
        (0 until h).forEach { y ->
            input[y][x]++
        }
    }

    val flashed = mutableListOf<Pair<Int, Int>>()
    var totalFlashes = 0
    do {
        var newFlashes = 0
        (0 until w).forEach { x ->
            (0 until h).forEach { y ->
                if (input[y][x] > 9) {
                    val sx = (x - 1).coerceAtLeast(0)
                    val ex = (x + 1).coerceAtMost(w - 1)
                    val sy = (y - 1).coerceAtLeast(0)
                    val ey = (y + 1).coerceAtMost(h - 1)


                    (sx..ex).forEach { fx ->
                        (sy..ey).forEach { fy ->
                            if ((fx to fy) !in flashed) {
                                input[fy][fx]++
                            }
                        }
                    }

                    flashed.add(x to y)
                    input[y][x] = 0
                    ++newFlashes
                }
            }
        }
        totalFlashes += newFlashes
    } while (newFlashes > 0)

    return totalFlashes

}
