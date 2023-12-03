package org.stummi.aoc.y2015

interface Item {
    val v: Int
    val price: Int
}

data class Armor(
    override val v: Int,
    override val price: Int
) : Item

data class Weapon(
    override val v: Int,
    override val price: Int
) : Item

enum class RingType {
    dmg,
    def
}

interface Ring : Item

data class DmgRing(
    override val v: Int,
    override val price: Int,
    //val type: RingType
) : Ring

data class DefRing(
    override val v: Int,
    override val price: Int,
    //val type: RingType
) : Ring

val weapons = listOf(
    Weapon(4, 8),
    Weapon(5, 10),
    Weapon(6, 25),
    Weapon(7, 40),
    Weapon(8, 74),
)

val armors = listOf(
    Armor(0, 0), // "no armor" to make the algo easier
    Armor(1, 13),
    Armor(2, 31),
    Armor(3, 53),
    Armor(4, 75),
    Armor(5, 102)
)

val damageRings = listOf(
    DmgRing(1, 25),
    DmgRing(2, 50),
    DmgRing(3, 100),
)

val defenseRings = listOf(
    DefRing(1, 20),
    DefRing(2, 40),
    DefRing(3, 80),
)

fun _main() {
    var playerHitpoints = 100
    val playerDamage = 9
    val playerArmor = 1
    var enemyHitpoints = 109
    val enemyArmor = 2
    val enemyDamage = 8

    val playerDmgDeal = playerDamage - enemyArmor
    val enemyDmgDeal = enemyDamage - playerArmor


    val damage = enemyDamage - playerArmor
    val rounds = ((playerHitpoints - 1) / damage) + 1

    val minPlayerDamageToWin = (enemyHitpoints - 1) / rounds + 1 + enemyArmor
    playerArmor to minPlayerDamageToWin

    println("Rounds: $rounds")
    println("Min Player damage: $minPlayerDamageToWin")

    var round = 0
    while (true) {
        println("Round ${round++}")
        enemyHitpoints -= playerDmgDeal
        println("Enemy HP: $enemyHitpoints")
        if (enemyHitpoints <= 0) {
            println("Enemy dead")
            break;
        }

        println("Player HP: $playerHitpoints")
        playerHitpoints -= enemyDmgDeal
        if (playerHitpoints <= 0) {
            println("player dead")
            break
        }
    }
}

fun main() {


    val possibleDamages = (4..13)

    val enemyHitpoints = 109
    val enemyDamage = 8
    val enemyArmor = 2

    val playerHitpoints = 100

    // everything above (enemyDamage-1) results in the same
    val usefulCombinations = (0..10).map { playerArmor ->
        val damage = (enemyDamage - playerArmor).coerceAtLeast(1)
        val rounds = ((playerHitpoints - 1) / damage) + 1

        val minPlayerDamageToWin = (enemyHitpoints - 1) / rounds + 1 + enemyArmor
        val maxPlayerDamageToLoose = minPlayerDamageToWin - 1
        playerArmor to maxPlayerDamageToLoose
    }.flatMap { (armor, dmg) ->
        println("$armor $dmg")
        getItemCombinations(armor, dmg)
    }.maxByOrNull { it.sumOf { it.price } }.let {
        println(it)
        println(it?.sumOf { it.price })
    }
}

fun getItemCombinations(armor: Int, dmg: Int): List<List<Item>> =
    getSingleStatCombinations(armors, defenseRings, 2, armor).flatMap { armorItems ->
        val ringsLeft = 2 - armorItems.count { it is Ring }
        getSingleStatCombinations(weapons, damageRings, ringsLeft, dmg).map {
            it + armorItems
        }
    }


fun getSingleStatCombinations(items: List<Item>, rings: List<Ring>, ringsLeft: Int, targetValue: Int) = listOf(
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

