fun main() {
    val lines = Unit.javaClass.getResourceAsStream("/2021/13.txt").use {
        it!!.bufferedReader().readLines()
    }

    val sep = lines.indexOf("")

    var dots = lines.subList(0, sep).map { it.split(",") }
        .map { it[0].toInt() to it[1].toInt() }

    val folds = lines.subList(sep + 1, lines.size)
        .map { it.split(" ")[2] }
        .map { it.split("=") }
        .map { it[0] to it[1].toInt() }


    //printDots(dots)

    println("----")

    folds.forEach {
        dots = foldDots(dots, it)
        println("----")
        println(it)
        //printDots(dots)
    }
    //dots = foldDots(dots, folds[0])


    println("----")
    printDots(dots)

    println(dots.distinct().size)
    //println(folds.fold(dots) { dots, fold -> foldDots(dots, fold) }.count())*/
}

fun printDots(dots: List<Pair<Int, Int>>) {
    var maxX = dots.map { it.first }.maxOrNull()!!
    var maxY = dots.map { it.second }.maxOrNull()!!

    (0..maxY).forEach { y ->
        (0..maxX).forEach { x ->
            print(if ((x to y) in dots) "#" else ".")
        }
        println()
    }
}

fun foldDots(dots: List<Pair<Int, Int>>, fold: Pair<String, Int>): List<Pair<Int, Int>> =
    when (fold.first) {
        "x" -> foldDotsX(dots, fold.second)
        "y" -> foldDotsY(dots, fold.second)
        else -> throw IllegalArgumentException()
    }


fun foldDotsX(dots: List<Pair<Int, Int>>, x: Int): List<Pair<Int, Int>> {
    if (dots.any { it.first == x }) {
        throw IllegalStateException()
    }
    val halves = dots.partition { it.first < x }

    val folded = halves.second.map { (px, py) ->
        2 * x - px to py
    }

    return (halves.first + folded).distinct()
}

fun foldDotsY(dots: List<Pair<Int, Int>>, y: Int): List<Pair<Int, Int>> {
    if (dots.any { it.second == y }) {
        throw IllegalStateException()
    }

    val halves = dots.partition { it.second < y }

    val folded = halves.second.map { (px, py) ->
        px to 2 * y - py
    }

    return (halves.first + folded).distinct()
}
