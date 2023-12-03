package org.stummi.aoc.y2015

fun main() {
    val input = "hepxcrrq"

    val pw = input.toCharArray()

    generateSequence {
        increasePassword(pw)
        pw
    }.filter { isValidPassword(it) }.take(2).forEach {
        println(it)
    }


}

fun isValidPassword(pw: CharArray): Boolean {
    if (!((0..pw.size - 3).any {
            pw[it + 1] == pw[it] + 1 && pw[it + 2] == pw[it] + 2
        } && listOf('i', 'o', 'l').none { it in pw })) {
        return false
    }

    val firstDouble = (0 until pw.size - 1).find {
        pw[it] == pw[it + 1]
    } ?: return false

    val secondDouble = (firstDouble + 2 until pw.size - 1).find {
        pw[it] == pw[it + 1]
    }

    return secondDouble != null
}

fun increasePassword(pw: CharArray) {
    var incPos = pw.size - 1

    while (true) {
        if (pw[incPos] == 'z') {
            pw[incPos] = 'a'
            --incPos
        } else {
            pw[incPos]++
            return
        }
    }
}
