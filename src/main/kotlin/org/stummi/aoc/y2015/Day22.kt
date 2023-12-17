package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode

object Day22 : AdventOfCode(2015, 22) {
    data class GameState(
        val bossHp: Int,
        val playerHp: Int,
        val playerMana: Int,
        val bossDamage: Int,
        val activeEffects: Map<Cast, Int> = emptyMap(),
    )

    enum class Cast(
        val cost: Int,
        val immediateEffect: (GameState) -> GameState = { it },
        val roundEffect: (GameState) -> GameState = { it },
        val duration: Int = 0
    ) {
        Magic_Missile(
            cost = 53,
            immediateEffect = { it.copy(bossHp = it.bossHp - 4) }
        ),

        Drain(
            cost = 73,
            immediateEffect = { it.copy(bossHp = it.bossHp - 2, playerHp = it.playerHp + 2) }
        ),

        Shield(
            cost = 113,
            duration = 6,
        ),

        Poison(
            cost = 173,
            roundEffect = { it.copy(bossHp = it.bossHp - 3) },
            duration = 6
        ),

        Recharge(
            cost = 229,
            roundEffect = { it.copy(playerMana = it.playerMana + 101) },
            duration = 5
        )
        ;

        override fun toString() = name.replace("_", " ")
    }

    fun getGameStrategies(state: GameState, hardMode: Boolean): Sequence<List<Cast>> {

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
                playerHp = if (hardMode) state.playerHp - 1 else state.playerHp
            )

        if (state.bossHp == 0) {
            return sequenceOf(emptyList())
        }

        return Cast.entries.filterNot {
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

            val damage = if (Cast.Shield in newActiveEffects) {
                newState.bossDamage - 7
            } else {
                newState.bossDamage
            }

            newState = newState.copy(
                activeEffects = newActiveEffects,
                playerMana = newState.playerMana - cast.cost,
                playerHp = newState.playerHp - damage
            )

            getGameStrategies(newState, hardMode).map {
                listOf(cast) + it
            }
        }

    }

    val initialState = GameState(
        bossHp = 71,
        playerHp = 50,
        playerMana = 500,
        bossDamage = 10
    )

    private fun parseInput(): GameState {
        val (hp, dmg) = inputLines().map { line ->
            line.split(": ")[1].toInt()
        }
        return GameState(
            bossHp = hp,
            playerHp = 50,
            playerMana = 500,
            bossDamage = dmg
        )
    }

    override val part1: Int
        get() = getGameStrategies(parseInput(), false).minOf { it.sumOf { it.cost } }
    override val part2: Int
        get() = getGameStrategies(parseInput(), true).minOf { it.sumOf { it.cost } }
}

fun main() {
    Day22.fancyRun()
}

