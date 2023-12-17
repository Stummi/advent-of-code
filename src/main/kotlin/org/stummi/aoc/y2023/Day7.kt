package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode

object Day7 : AdventOfCode(2023, 7) {

    enum class Type(val matcher: (Hand) -> Boolean) {
        FIVE_OF_A_KIND({ hand ->
            hand.countPerCard.size == 1
        }),
        FOUR_OF_A_KIND({ hand ->
            hand.countPerCard.any { it.value == 4 }
        }),
        FULL_HOUSE({ hand ->
            hand.countPerCard.any { it.value == 3 } && hand.countPerCard.any { it.value == 2 }
        }),
        THREE_OF_A_KIND({ hand ->
            hand.countPerCard.any { it.value == 3 }
        }),
        TWO_PAIR({ hand ->
            hand.countPerCard.count { it.value == 2 } == 2
        }),
        ONE_PAIR({ hand ->
            hand.countPerCard.count { it.value == 2 } == 1
        }),
        HIGH_CARD({ _ -> true });
    }

    data class Hand(
        val points: Int,
        val cardString: String,
    ) {
        val countPerCard: Map<Char, Int> by lazy {
            cardString.groupingBy { it }.eachCount()
        }
    }

    object HandComparator : Comparator<Hand> {
        override fun compare(o1: Hand, o2: Hand): Int {
            val type1 = type(o1)
            val type2 = type(o2)
            if (type1 < type2) return 1
            if (type1 > type2) return -1
            return compareSameType(o1, o2)
        }

        private fun type(h: Hand): Type {
            return if (h.cardString.contains('?')) {
                typeWithJoker(h)
            } else {
                Type.entries.first { it.matcher(h) }
            }
        }

        private fun typeWithJoker(h: Hand): Type {
            return allJokerCombinations(h.cardString).map {
                Hand(h.points, it)
            }.map { h ->
                Type.entries.first { it.matcher(h) }
            }.min()
        }

        private fun allJokerCombinations(cards: String): Sequence<String> {
            if (!cards.contains('?')) return sequenceOf(cards)

            val idx = cards.indexOf('?')
            val replacementCards = "23456789TQKA"
            return replacementCards.asSequence().map { repl ->
                allJokerCombinations(cards.replaceRange(idx, idx + 1, repl.toString()))
            }.flatten()
        }

        private fun compareSameType(o1: Hand, o2: Hand): Int {
            (0..<5).forEach {
                val c1 = o1.cardString[it]
                val c2 = o2.cardString[it]
                val v1 = cardValue(c1)
                val v2 = cardValue(c2)
                if (v1 > v2) return 1
                if (v1 < v2) return -1
            }

            return 0
        }
    }

    private fun cardValue(c: Char) = when {
        c.isDigit() -> c.digitToInt()
        c == 'T' -> 10
        c == 'J' -> 11
        c == 'Q' -> 12
        c == 'K' -> 13
        c == 'A' -> 14
        c == '?' -> 1
        else -> throw IllegalArgumentException("Invalid card $c")
    }


    private fun parsedInput(replaceJokers: Boolean) =
        inputLines().map {
            var (cardStr, pointStr) = it.split(" ")

            if (replaceJokers) {
                cardStr = cardStr.replace("J", "?")
            }

            Hand(
                points = pointStr.toInt(),
                cardString = cardStr,
            )
        }

    override val part1: Any
        get() = parsedInput(false).sortedWith(HandComparator).mapIndexed { idx, it ->
            (idx + 1) * it.points
        }.sum()

    override val part2: Any
        get() = parsedInput(true).sortedWith(HandComparator).mapIndexed { idx, it ->
            (idx + 1) * it.points
        }.sum()
}

fun main() {
    Day7.fancyRun()
}
