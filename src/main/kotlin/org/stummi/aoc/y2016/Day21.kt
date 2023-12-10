package org.stummi.aoc.y2016

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.allPermutations

object Day21 : AdventOfCode(2016, 21) {
    init {
        resourceSample("demo", "decab", additionalData = "abcde")
    }

    override val part1: String
        get() {
            var password = "abcdefgh".orSample().toList()

            val instructions = inputLines().map { it.split(" ") };
            password = applyInstructions(instructions, password)

            return password.joinToString("")
        }

    private fun applyInstructions(
        instructions: List<List<String>>,
        password: List<Char>
    ): List<Char> {
        var password1 = password.toMutableList()
        instructions.forEach { inst ->
            when (inst.take(2).joinToString(" ")) {
                "swap position" -> swap(password1, inst[2].toInt(), inst[5].toInt())
                "swap letter" -> swap(password1, password1.indexOf(inst[2][0]), password1.indexOf(inst[5][0]))
                "rotate left" -> {
                    val p = inst[2].toInt();
                    password1 = rotateLeft(password1, p)
                }

                "rotate right" -> {
                    val p = inst[2].toInt();
                    password1 = rotateRight(password1, p)
                }

                "rotate based" -> {
                    var p = password1.indexOf(inst[6][0])
                    if (p >= 4) {
                        p += 1
                    }
                    p += 1

                    password1 = rotateRight(password1, (p) % password1.size)
                }

                "reverse positions" -> {
                    val f = inst[2].toInt()
                    val t = inst[4].toInt()
                    password1 = (password1.take(f) + password1.subList(f, t + 1)
                        .reversed() + password1.drop(t + 1)).toMutableList()
                }

                "move position" -> {
                    val f = inst[2].toInt();
                    val t = inst[5].toInt();
                    val c = password1.removeAt(f);
                    password1.add(t, c)
                }

                else -> throw IllegalArgumentException(inst.joinToString(" "))
            }
        }
        return password1.toList()
    }

    private fun rotateLeft(
        password: MutableList<Char>,
        p: Int
    ): MutableList<Char> {
        var password1 = password
        password1 = (password1.drop(p) + password1.take(p)).toMutableList()
        return password1
    }

    private fun rotateRight(
        password: MutableList<Char>,
        p: Int
    ) = (password.takeLast(p) + password.dropLast(p)).toMutableList()

    private fun swap(password: MutableList<Char>, p1: Int, p2: Int) {
        val tmp = password[p1]
        password[p1] = password[p2]
        password[p2] = tmp
    }

    override val part2: String
        get() {
            val instructions = inputLines().map { it.split(" ") };
            val pw = "fbgdceah".toList();
            pw.toList().allPermutations().first() {
                applyInstructions(instructions, it) == pw
            }.let {
                return it.joinToString("")
            }
        }

}

fun main() {
    Day21.fancyRun()
}
