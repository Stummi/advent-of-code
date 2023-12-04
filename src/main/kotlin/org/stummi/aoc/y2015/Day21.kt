package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode


object Day21 : AdventOfCode(2015, 21) {
    private interface Item {
        val v: Int
        val price: Int
    }

    private data class Armor(
        override val v: Int,
        override val price: Int
    ) : Item

    private data class Weapon(
        override val v: Int,
        override val price: Int
    ) : Item

    private interface Ring : Item

    private data class DmgRing(
        override val v: Int,
        override val price: Int,
    ) : Ring

    private data class DefRing(
        override val v: Int,
        override val price: Int,
    ) : Ring

    private val weapons = listOf(
        Weapon(4, 8),
        Weapon(5, 10),
        Weapon(6, 25),
        Weapon(7, 40),
        Weapon(8, 74),
    )

    private val armors = listOf(
        Armor(0, 0), // "no armor" to make the algo easier
        Armor(1, 13),
        Armor(2, 31),
        Armor(3, 53),
        Armor(4, 75),
        Armor(5, 102)
    )

    private val damageRings = listOf(
        DmgRing(1, 25),
        DmgRing(2, 50),
        DmgRing(3, 100),
    )

    private val defenseRings = listOf(
        DefRing(1, 20),
        DefRing(2, 40),
        DefRing(3, 80),
    )

    private data class Enemy(
        val hitpoints: Int,
        val damage: Int,
        val armor: Int
    )


    private fun getItemCombinations(armor: Int, dmg: Int): List<List<Item>> =
        getSingleStatCombinations(armors, defenseRings, 2, armor).flatMap { armorItems ->
            val ringsLeft = 2 - armorItems.count { it is Ring }
            getSingleStatCombinations(weapons, damageRings, ringsLeft, dmg).map {
                it + armorItems
            }
        }


    private fun parseInput(): Enemy {
        val (hp, dmg, armor) = input().map { line ->
            line.split(": ")[1].toInt()
        }
        return Enemy(hp, dmg, armor)
    }

    private fun getSingleStatCombinations(items: List<Item>, rings: List<Ring>, ringsLeft: Int, targetValue: Int) =
        listOf(
            emptyList(),
            listOf(rings[0]),
            listOf(rings[1]),
            listOf(rings[2]),
            listOf(rings[0], rings[1]),
            listOf(rings[0], rings[2]),
            listOf(rings[1], rings[2]),
        ).filter { it.size <= ringsLeft }
            .mapNotNull { rings ->
                val rem = targetValue - rings.sumOf { r -> r.v }
                items.find { it.v == rem }?.let {
                    rings + it
                }
            }

    override val part1: Int
        get() {
            val playerHitpoints = 100
            val enemy = parseInput()

            // everything above (enemyDamage-1) results in the same
            return (0..10).map { playerArmor ->
                val damage = (enemy.damage - playerArmor).coerceAtLeast(1)
                val rounds = ((playerHitpoints - 1) / damage) + 1

                val minPlayerDamageToWin = (enemy.hitpoints - 1) / rounds + 1 + enemy.armor
                playerArmor to minPlayerDamageToWin
            }.flatMap { (armor, dmg) ->
                getItemCombinations(armor, dmg)
            }.minOf { it.sumOf { it.price } }
        }

    override val part2: Int
        get() {
            val playerHitpoints = 100
            val enemy = parseInput()

            // everything above (enemyDamage-1) results in the same
            return (0..10).map { playerArmor ->
                val damage = (enemy.damage - playerArmor).coerceAtLeast(1)
                val rounds = ((playerHitpoints - 1) / damage) + 1

                val minPlayerDamageToWin = (enemy.hitpoints - 1) / rounds + 1 + enemy.armor
                val maxPlayerDamageToLoose = minPlayerDamageToWin - 1
                playerArmor to maxPlayerDamageToLoose
            }.flatMap { (armor, dmg) ->
                getItemCombinations(armor, dmg)
            }.maxOf { it.sumOf { it.price } }
        }
}

fun main() {
    Day21.fancyRun()
}
