package org.stummi.aoc.y2015

import org.stummi.aoc.AdventOfCode

object Day15 : AdventOfCode(2015, 15) {
    fun parsedInput() = input().map {
        it.split(" ")
    }.associate {
        it[0].trimEnd(':') to
                it.drop(1).chunked(2).associate {
                    it[0] to it[1].trimEnd(',').toInt()
                }
    }

    private fun ingredientCombinations(
        input: List<String>,
        existing: Map<String, Int> = emptyMap()
    ): List<Map<String, Int>> {
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

    fun allIngredientCombinations() = parsedInput().let { ingredients ->
        ingredientCombinations(ingredients.keys.toList()).map {
            val propertySum = mutableMapOf<String, Int>()
            it.forEach { (ing, amount) ->
                ingredients[ing]!!.forEach { (prop, value) ->
                    propertySum[prop] = propertySum.getOrDefault(prop, 0) + value * amount
                }
            }
            it to propertySum
        }.filter { (_, propertySum) ->
            propertySum.values.all { it > 0 }
        }
    }

    override val part1: Int
        get() = allIngredientCombinations().maxOf { (_, propertySum) ->
            (propertySum - "calories").values.reduce { a, b -> a * b }
        }

    override val part2: Int
        get() = allIngredientCombinations().filter { (_, propertySum) ->
            propertySum["calories"] == 500
        }.maxOf { (_, propertySum) ->
            (propertySum - "calories").values.reduce { a, b -> a * b }
        }
}

fun main() {
    Day15.fancyRun()
}


