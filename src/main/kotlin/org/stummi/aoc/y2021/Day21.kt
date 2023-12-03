import org.stummi.aoc.AdventOfCode

object Day21 : AdventOfCode(2021, 21) {

    private class DeterministDice {
        var nRolls = 0

        fun roll() = ((nRolls++) % 100) + 1

        fun move(pos: Int): Int {
            val roll = generateSequence { roll() }.take(3).toList()
            val newPos = pos + roll.sum()
            return ((newPos - 1) % 10) + 1
        }
    }


    override val part1: Int
        get() {
            val dice = DeterministDice()
            val pos = inputData()
            var player1Pos = pos.first
            var player2Pos = pos.second
            var player1Points = 0
            var player2Points = 0
            while (true) {
                player1Pos = dice.move(player1Pos)
                player1Points += player1Pos
                if (player1Points >= 1000) {
                    return dice.nRolls * player2Points
                }

                player2Pos = dice.move(player2Pos)
                player2Points += player2Pos
                if (player2Points >= 1000) {
                    return dice.nRolls * player1Points
                }
            }
        }

    private fun inputData() = 8 to 9

    data class StateOfWorld(
        val playerPos: IntArray = IntArray(2),
        val playerPoints: IntArray = IntArray(2),
    ) {


        fun move(playerId: Int) =
            quantumResults.mapKeys { (r, _) ->
                StateOfWorld(
                    playerPos.clone(),
                    playerPoints.clone()
                ).apply {
                    playerPos[playerId] = (playerPos[playerId] + r) % 10
                    playerPoints[playerId] += (playerPos[playerId] + 1)
                }
            }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as StateOfWorld

            if (!playerPos.contentEquals(other.playerPos)) return false
            if (!playerPoints.contentEquals(other.playerPoints)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = playerPos.contentHashCode()
            result = 31 * result + playerPoints.contentHashCode()
            return result
        }

        companion object {
            val quantumResults = combine(3).groupingBy { it }.eachCount()

            private fun combine(d: Int): Sequence<Int> {
                return if (d == 0) {
                    sequenceOf(0)
                } else {
                    (1..3).asSequence().flatMap { v ->
                        combine(d - 1).map { it + v }
                    }
                }
            }
        }

    }

    override val part2: Any
        get() {
            val input = inputData()
            val initialState = StateOfWorld(listOf(input.first - 1, input.second - 1).toIntArray())

            var universes = mapOf(initialState to 1L)
            val winners = LongArray(2)
            var player = 0
            while (universes.isNotEmpty()) {

                var nextUniverses = mutableMapOf<StateOfWorld, Long>()
                universes.map { (u, c) ->
                    u.move(player).mapValues { it.value * c }
                }.forEach {
                    it.forEach { (k, v) ->
                        nextUniverses[k] = (nextUniverses[k] ?: 0L) + v
                    }
                }
                universes = nextUniverses

                val newWinners = universes.filterKeys { it.playerPoints[player] >= 21 }.map { it.value }.sum()
                universes = universes.filterKeys { it.playerPoints[player] < 21 }

                winners[player] += newWinners

                player = 1 - player
            }

            return winners.maxOrNull()!!
        }
}

fun main() {
    println(Day21.part1)
    println(Day21.part2)
}
