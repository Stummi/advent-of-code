package org.stummi.aoc.helper

/**
 * generic A* implementation
 *
 * @param initialState: The starting point
 * @param nextStates: A function that provides all (valid) transitions to other states from a given input state,
 *      paired with the cost of this transition
 * @param goal: A function that checks whether a given state is the target
 * @param heuristicCost: The heuristic cost to guess the remaining steps from a given state to the goal. The Cost
 * function must not be exact, but it must never exceed the actual remaining cost. `{ 0 }` is a valid (though pretty
 * inefficient) cost function.
 */
fun <T> astar(
    initialState: T,
    nextStates: (T) -> Sequence<Pair<T, Int>>,
    goal: (T) -> Boolean,
    heuristicCost: (T) -> Int
): List<Pair<T, Int>> {

    // Minimalistic debug interface :D
    fun println(s: String) {
    }

    class OpenList(initialState: T) {
        val map = mutableMapOf(initialState to 0)
        val sorted = sortedMapOf(0 to mutableSetOf(initialState))

        fun popNext(): Pair<T, Int>? {
            val (v, list) = sorted.asSequence().firstOrNull() ?: return null
            val ret = list.first().also { list.remove(it) }
            if (list.isEmpty()) {
                sorted.remove(v)
            }
            map.remove(ret)
            return ret to v
        }

        operator fun get(p: T): Int? = map[p]

        operator fun contains(p: T) = p in map

        operator fun set(p: T, value: Int) {
            val existing = map[p]
            map[p] = value
            if (existing != null) {
                sorted[value]!!.let {
                    it.remove(p)
                    if (it.isEmpty()) {
                        sorted.remove(value)
                    }
                }
            }
            sorted.computeIfAbsent(value) { mutableSetOf() }.add(p)
        }
    }

    val openList = OpenList(initialState)
    val closedList = mutableSetOf<T>()
    val g = mutableMapOf(initialState to 0)
    val pred = mutableMapOf<T, Pair<T, Int>>()

    val target: T
    while (true) {
        val currentEntry =
            openList.popNext() ?: throw IllegalStateException("no more entries in openList. Maybe not solvable?")

        println("current candidate: ${currentEntry.first} g=${currentEntry.second}")

        val currentNode = currentEntry.first
        if (goal(currentNode)) {
            println("found target")
            target = currentNode
            break
        }
        closedList.add(currentNode)
        nextStates(currentNode).forEach { (p, cost) ->
            println("  transition: ${p} for $cost")
            if (closedList.contains(p)) {
                println("    (skip, in closeList)")
                return@forEach
            }
            val ten_g = g[currentNode]!! + cost
            println("    ten_g: $ten_g")
            if (p in openList) {
                if (ten_g >= g[p]!!) {
                    println("    (skip, has cheaper entry ${g[p]})")
                    return@forEach
                } else {
                    println("    (prev cost: ${g[p]})")
                }
            }

            println("    set pred $p -> ${currentNode to cost} (override: ${pred[p]})")

            pred[p] = currentNode to cost
            g[p] = ten_g

            val h = heuristicCost(p)
            val f = ten_g + h
            println("    add with cost: $f")
            openList[p] = f
            check(p in openList)
        }
    }

    var cur = target

    return sequence {
        while (cur != initialState) {
            val last = pred[cur]!!
            yield((cur to last.second))
            cur = last.first
        }
    }.toList()
}
