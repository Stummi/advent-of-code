import org.stummi.aoc.helper.astar

typealias Position = Pair<Int, Int>

fun main() {
    val input = Unit.javaClass.getResourceAsStream("/2021/15.txt").use {
        it!!.bufferedReader().readLines()
    }.map { it.map(Char::digitToInt) }
        .let { expandField(it) } /* this is part 2 */

    val field = Field(input)

    val start = (0 to 0)
    val target = field.w - 1 to field.h - 1

    val nextStates: (Position) -> Sequence<Pair<Position, Int>> = { currentNode ->
        sequence {
            if (currentNode.first > 0)
                yield(currentNode.first - 1 to currentNode.second)
            if (currentNode.second > 0)
                yield(currentNode.first to currentNode.second - 1)
            if (currentNode.first < target.first)
                yield(currentNode.first + 1 to currentNode.second)
            if (currentNode.second < target.second)
                yield(currentNode.first to currentNode.second + 1)
        }.map {
            it to field[it]
        }
    }

    val goal: (Position) -> Boolean = { it == target }
    val heuristicCost: (Position) -> Int = { p ->
        target.first - p.first + target.second - p.second
    }

    astar(start, nextStates, goal, heuristicCost).sumOf { it.second }.let {
        println(it)
    }
}

class Field(val intArray: IntArray, val w: Int, val h: Int) {

    constructor(l: List<List<Int>>) : this(
        l.flatten().toIntArray(),
        l.groupingBy { it.size }.eachCount().let {
            if (it.size != 1) throw IllegalArgumentException()
            it.keys.first()
        },
        l.size,
    )

    fun posToIndex(x: Int, y: Int) = w * y + x

    operator fun get(x: Int, y: Int) = intArray[posToIndex(x, y)]
    operator fun set(x: Int, y: Int, v: Int) {
        intArray[posToIndex(x, y)] = v
    }

    operator fun get(p: Position) = intArray[posToIndex(p.first, p.second)]
}

fun expandField(input: List<List<Int>>): List<List<Int>> {
    val expanded =
        input.map { line ->
            (listOf(line) +
                    (1..4).map { o ->
                        line.map {
                            (((it + o) - 1) % 9) + 1
                        }
                    }).flatten()
        }

    return sequence {
        yield(expanded)
        var cur = expanded
        repeat(4) {
            cur = cur.map { line ->
                line.map { (it % 9) + 1 }
            }
            yield(cur)
        }
    }.flatten().toList()


}
