fun pt1() {
    val matrix = Unit.javaClass.getResourceAsStream("/2021/9.txt").use {
        it!!.bufferedReader().readLines()
    }.map {
        it.map { it.digitToInt() }
    }

    println(matrix)

    val rows = matrix.size
    val cols = matrix[0].size

    var result = 0
    (0 until rows).forEach { row ->
        (0 until cols).forEach { col ->
            val adj = mutableListOf<Int>()
            if (row > 0) {
                adj.add(matrix[row - 1][col])
            }
            if (row < rows - 1) {
                adj.add(matrix[row + 1][col])
            }
            if (col > 0) {
                adj.add(matrix[row][col - 1])
            }
            if (col < cols - 1) {
                adj.add(matrix[row][col + 1])
            }

            if (adj.all { it > matrix[row][col] }) {
                result += matrix[row][col] + 1
            }
        }
    }
    println(result)

}

fun main() {
    val matrix = Unit.javaClass.getResourceAsStream("/2021/9.txt").use {
        it!!.bufferedReader().readLines()
    }.map {
        it.map { it.digitToInt() }.toMutableList()
    }

    val rows = matrix.size
    val cols = matrix[0].size

    val result =
        (0 until rows).flatMap { row ->
            (0 until cols).map { col ->
                fillAndGetBasinSize(matrix, row, col)
            }
        }.sortedDescending().take(3).reduce { a, b -> a * b }
    println(result)

}

fun fillAndGetBasinSize(matrix: List<MutableList<Int>>, row: Int, col: Int): Int {
    if (matrix[row][col] == 9) {
        return 0;
    }

    matrix[row][col] = 9

    var ret = 1

    if (row > 0) {
        ret += fillAndGetBasinSize(matrix, row - 1, col)
    }
    if (row < matrix.size - 1) {
        ret += fillAndGetBasinSize(matrix, row + 1, col)
    }
    if (col > 0) {
        ret += fillAndGetBasinSize(matrix, row, col - 1)
    }
    if (col < matrix[row].size - 1) {
        ret += fillAndGetBasinSize(matrix, row, col + 1)
    }

    return ret;

}
