package org.stummi.aoc.y2015

fun main() {

    val ingredients = Unit.javaClass.getResourceAsStream("/2015/15.txt").use {
        it!!.bufferedReader().readLines()
    }.map {
        it.split(" ")
    }.associate {
        it[0].trimEnd(':') to
                it.drop(1).chunked(2).associate {
                    it[0] to it[1].trimEnd(',').toInt()
                }
    }

    val ings = ingredients.keys.toList()

    val traits = listOf("capacity", "durability", "flavor", "texture")
    ingredientCombinations(ings)
        .filter {
            it.map { (ing, amount) -> ingredients[ing]!!["calories"]!! * amount }.sum() == 500
        }
        .map { comb ->
            traits.map { trait ->
                comb.map { (ingName, ingamount) ->
                    ingamount * ingredients[ingName]!![trait]!!
                }.sum().coerceAtLeast(0)
            }.reduce { a, b -> a * b }
        }.maxOrNull()!!.let {
            println(it)
        }
}

fun ingredientCombinations(input: List<String>, existing: Map<String, Int> = emptyMap()): List<Map<String, Int>> {
    val remaining = 100 - existing.values.sum()
    return if (input.size == 1) {
        listOf(existing + mapOf(input[0] to remaining))
    } else {
        val to = remaining + 1 - input.size
        val firstInput = input[0]
        val remInput = input.drop(1)
        (1..to).flatMap {
            ingredientCombinations(remInput, existing + mapOf(firstInput to it))
        }
    }
}
