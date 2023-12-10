import Day23.AmphPodType.A
import Day23.AmphPodType.B
import Day23.AmphPodType.C
import Day23.AmphPodType.D
import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.astar
import kotlin.math.absoluteValue

/**

#############
#01234567890#
###B#C#B#D###
..#A#D#C#A#
..#########
 */

object Day23 : AdventOfCode(2021, 23) {

    enum class AmphPodType(val cost: Int, val halwayPos: Int) {
        A(1, 2),
        B(10, 4),
        C(100, 6),
        D(1000, 8);

        override fun toString() = name
    }

    val validHalwayPositions = (0..10).filter {
        AmphPodType.values().none { p -> it == p.halwayPos }
    }.toSortedSet()

    fun <T> List<T>.replace(index: Int, value: T) = this.mapIndexed { i, t -> if (i == index) value else t }

    data class GameState(
        val halway: List<AmphPodType?> = arrayOfNulls<AmphPodType>(11).toList(),
        val rooms: List<List<AmphPodType?>> = generateSequence { arrayOfNulls<AmphPodType>(2).toList() }.take(4)
            .toList(),
    ) {

        fun nextGameStates() = sequence<Pair<GameState, Int>> {

            val cleanRooms = rooms.mapIndexed { index, list ->
                list.all { it == null || it == AmphPodType.values()[index] }
            }

            rooms.forEachIndexed { index, room ->
                val amphPodType = AmphPodType.values()[index]
                if (cleanRooms[index]) {
                    return@forEachIndexed
                }

                val firstIdx = room.indexOfFirst { it != null }
                if (firstIdx == -1) {
                    return@forEachIndexed
                }

                val cost1 = firstIdx + 1
                val newRooms = rooms.replace(index, room.replace(firstIdx, null))
                val podType = rooms[index][firstIdx]!!
                val downwards = (amphPodType.halwayPos downTo 0).filter { it in validHalwayPositions }
                val upwards = (amphPodType.halwayPos..11).filter { it in validHalwayPositions }

                listOf(downwards, upwards).forEach {
                    for (pos in it) {
                        if (halway[pos] != null) {
                            break
                        }

                        val newHalway = halway.replace(pos, podType)
                        val cost = (cost1 + (pos - amphPodType.halwayPos).absoluteValue) * podType.cost

                        yield(
                            copy(
                                halway = newHalway,
                                rooms = newRooms
                            ) to cost
                        )
                    }
                }
            }

            halway.forEachIndexed { index, amphPodType ->
                if (amphPodType == null) {
                    return@forEachIndexed
                }
                val room = amphPodType.ordinal

                if (!cleanRooms[room]) {
                    return@forEachIndexed
                }

                val dir = if (amphPodType.halwayPos > index) {
                    (index + 1)..amphPodType.halwayPos
                } else {
                    index - 1 downTo amphPodType.halwayPos
                }

                if (dir.any { halway[it] != null }) {
                    return@forEachIndexed
                }

                val roomPos = rooms[room].indexOfLast { it == null }
                val newRooms = rooms.replace(room, rooms[room].replace(roomPos, amphPodType))
                val newHalway = halway.replace(index, null)

                val cost = ((index - amphPodType.halwayPos).absoluteValue + roomPos + 1) * amphPodType.cost

                yield(
                    copy(
                        halway = newHalway,
                        rooms = newRooms,
                    ) to cost
                )
            }

        }


        override fun toString() = StringBuilder().apply {
            append("#".repeat(halway.size + 2))
            append("\n#")
            halway.forEach { append(it?.name ?: '.') }
            append("#\n")

            (0..1).forEach { rIdx ->
                val l = rooms.map { it[rIdx] }.map { it?.name ?: '.' }.joinToString("#")
                if (rIdx == 0) {
                    append("###")
                    append(l)
                    append("###")
                } else {
                    append("  #")
                    append(l)
                    append("#  ")
                }
                append("\n")
            }
            append("  ")
            append("#".repeat(9))
            append("  \n")

        }.toString()

        fun hasWon(): Boolean {
            rooms.forEachIndexed { index, list ->
                if (!list.all { it != null && it == AmphPodType.values()[index] }) {
                    return false
                }
            }
            return true
        }
    }

    override val part1: Any
        get() = solve(initialState)

    override val part2: Any
        get() = solve(insertMissingRows(initialState))

    private fun insertMissingRows(initialState: Day23.GameState): Day23.GameState {
        val insert = listOf(
            listOf(D, D),
            listOf(C, B),
            listOf(B, A),
            listOf(A, C)
        )
        return initialState.copy(
            rooms = initialState.rooms.mapIndexed { it, list ->
                listOf(list[0]) + insert[it] + list[1]
            }
        )
    }

    fun solve(state: GameState) =
        astar(state, { it.nextGameStates() }, { it.hasWon() }) { 0 }.sumOf { it.second }


    val initialState: GameState
        get() {
            val data = inputLines()
            val state = GameState()

            val mutableRooms = GameState().rooms.map { it.toMutableList() }

            AmphPodType.values().forEachIndexed { typeIdx, p ->
                (0..1).forEach { roomIdx ->
                    val c = data[2 + roomIdx][p.halwayPos + 1].toString()
                    mutableRooms[typeIdx][roomIdx] = AmphPodType.valueOf(c)
                }
            }
            return state.copy(rooms = mutableRooms.map { it.toList() })
        }


}

fun main() {
    println(Day23.part2)
}
