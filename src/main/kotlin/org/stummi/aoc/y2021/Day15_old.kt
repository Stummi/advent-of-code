fun main() {
    val input = Unit.javaClass.getResourceAsStream("/2021/15.txt").use {
        it!!.bufferedReader().readLines()
    }.map { it.map(Char::digitToInt) }
        .let { expandField(it) } /* this is part 2 */

    val field = Field(input)

    var start = (0 to 0)
    val target = field.w - 1 to field.h - 1

    val openList = mutableMapOf(start to 0)
    val closedList = mutableSetOf<Position>()
    val g = mutableMapOf(start to 0)
    val pred = mutableMapOf<Position, Position>()

    var lastPrint = 0L
    while (true) {
        val now = System.currentTimeMillis()
        if (lastPrint + 1000 < now) {
            println("open ${openList.size} closed: ${closedList.size}")
            lastPrint = now;
        }
        val currentEntry = openList.minByOrNull { it.value }!!
        openList.remove(currentEntry.key)
        val currentNode = currentEntry.key
        if (currentNode == target) {
            println("found target")
            break
        }
        closedList.add(currentNode)


        sequence {
            if (currentNode.first > 0)
                yield(currentNode.first - 1 to currentNode.second)
            if (currentNode.second > 0)
                yield(currentNode.first to currentNode.second - 1)
            if (currentNode.first < target.first)
                yield(currentNode.first + 1 to currentNode.second)
            if (currentNode.second < target.second)
                yield(currentNode.first to currentNode.second + 1)
        }.forEach { p ->
            if (closedList.contains(p)) {
                return@forEach
            }
            val ten_g = g[currentNode]!! + field[p]
            openList[p]?.let {
                if (ten_g >= g[p]!!)
                    return@forEach
            }
            pred[p] = currentNode
            g[p] = ten_g

            val h = target.first - p.first + target.second - p.second
            val f = ten_g + h
            openList[p] = f
        }
    }

    var cur = target

    sequence {
        while (cur != start) {
            yield(cur)
            cur = pred[cur]!!
        }
    }.map { field[it] }.sum().let { println(it) }
}
