package org.stummi.aoc.y2015

class Cast(
    val name: String,
    val cost: Int,
    val immediateEffect: (GameState) -> GameState = { it },
    val roundEffect: (GameState) -> GameState = { it },
    val duration: Int = 0
) {
    override fun toString() = name
}

val casts = listOf(
    Cast(
        name = "Magic Missile",
        cost = 53,
        immediateEffect = {
            it.copy(bossHp = it.bossHp - 4)
        }
    ),
    Cast(
        name = "Drain",
        cost = 73,
        immediateEffect = {
            it.copy(
                bossHp = it.bossHp - 2,
                playerHp = it.playerHp + 2
            )
        }
    ),
    Cast(
        name = "Shield",
        cost = 113,
        duration = 6,
    ),
    Cast(
        name = "Poison",
        cost = 173,
        roundEffect = {
            it.copy(
                bossHp = it.bossHp - 3
            )
        },
        duration = 6
    ),
    Cast(
        name = "Recharge",
        cost = 229,
        roundEffect = {
            it.copy(
                playerMana = it.playerMana + 101
            )
        },
        duration = 5
    )
)

data class GameState(
    val bossHp: Int,
    val playerHp: Int,
    val playerMana: Int,
    val bossDamage: Int,
    val activeEffects: Map<Cast, Int> = emptyMap(),
)

fun main() {

    val initialState = GameState(
        bossHp = 71,
        playerHp = 50,
        playerMana = 500,
        bossDamage = 10
    )
    /*

   val initialState = GameState(
       bossHp = 14,
       playerHp = 10,
       bossDamage = 8,
       playerMana = 250,
   )*/

    var minCostSoFar = Int.MAX_VALUE
    getGameStrategies(initialState).maxByOrNull { it.sumOf { it.cost } }?.let {
        println(it)
    }
    /*forEach {
        val sumOf = it.sumOf { it.cost }
        if(minCostSoFar > sumOf) {
            println("new min: $it $sumOf")
            minCostSoFar = sumOf
        }

    }*/
}

val shield = casts.first { it.name == "Shield" }
fun getGameStrategies(state: GameState): Sequence<List<Cast>> {

    if (state.playerHp <= 0) {
        return emptySequence()
    }

    // playerRound
    val curState = state.activeEffects.keys
        .fold(state) { s, c -> c.roundEffect(s) }
        .copy(
            activeEffects = state.activeEffects
                .mapValues { it.value - 1 }
                .filterValues { it > 0 },
            // playerHp = state.playerHp - 1
        )

    if (state.bossHp == 0) {
        return sequenceOf(emptyList())
    }

    return casts.filterNot {
        it in curState.activeEffects
    }.filterNot {
        it.cost > curState.playerMana
    }.asSequence().flatMap { cast ->
        var newState = cast.immediateEffect(curState)

        if (newState.bossHp <= 0) {
            return@flatMap sequenceOf(listOf(cast))
        }

        var newActiveEffects = if (cast.duration > 0) {
            newState.activeEffects + (cast to cast.duration)
        } else {
            newState.activeEffects
        }

        // boss round
        newState = newActiveEffects.keys
            .fold(newState) { s, c -> c.roundEffect(s) }

        if (newState.bossHp <= 0) {
            return@flatMap sequenceOf(listOf(cast))
        }

        newActiveEffects = newActiveEffects
            .mapValues { it.value - 1 }
            .filterValues { it > 0 }

        val damage = if (shield in newActiveEffects) {
            newState.bossDamage - 7
        } else {
            newState.bossDamage
        }

        newState = newState.copy(
            activeEffects = newActiveEffects,
            playerMana = newState.playerMana - cast.cost,
            playerHp = newState.playerHp - damage
        )

        getGameStrategies(newState).map {
            listOf(cast) + it
        }
    }

}

