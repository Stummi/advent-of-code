fun main() {
    val connections = Unit.javaClass.getResourceAsStream("/2021/12.txt").use {
        it!!.bufferedReader().readLines()
    }.map { it.split("-") }.flatMap { listOf(it[0] to it[1], it[1] to it[0]) }.toList()

    findPaths("start", connections).onEach {
        println(it)
    }.count().let { println(it) }

}


fun findPaths(s: String, connections: List<Pair<String, String>>, pathSoFar: List<String> = emptyList()):
        List<List<String>> {

    if (s == "end") {
        return listOf(listOf(s))
    }

    var curPath = pathSoFar + s

    var visitedCaves = curPath.groupingBy { it }.eachCount()
    var hasTwoSmallCaves = visitedCaves.any { it.key[0].isLowerCase() && it.value >= 2 }

    var nextConnections = if (s == "start") {
        connections.filterNot { it.first == s || it.second == s }
    } else if (hasTwoSmallCaves) {
        connections.filterNot { listOf(it.first, it.second).any { it[0].isLowerCase() && it in visitedCaves } }
    } else {
        connections
    }
    /*
    val newConnections = if(s[0].isLowerCase()) {
        connections.filterNot { it.first == s || it.second == s  }
    } else {
        connections
    }*/

    val possibleDestinations = connections.filter { it.first == s }.map { it.second }
    return possibleDestinations.flatMap {
        findPaths(it, nextConnections, curPath).map { listOf(s) + it }
    }
}
