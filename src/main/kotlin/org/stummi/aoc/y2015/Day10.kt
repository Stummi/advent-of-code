package org.stummi.aoc.y2015

fun main() {
    val input = "1113222113"

    var c = StringBuilder(input);
    repeat(50) {
        c = lookAndSay(c)
        println("$it -> ${c.length}")
    }

    println(c.length)
}

fun lookAndSay(input: StringBuilder): StringBuilder {
    var currentChar = input[0]
    var currentCount = 1

    var ret = StringBuilder()

    input.drop(1).forEach {
        if (it == currentChar) {
            ++currentCount
        } else {
            ret.append(currentCount).append(currentChar.digitToInt())
            currentChar = it
            currentCount = 1
        }
    }
    ret.append(currentCount).append(currentChar.digitToInt())
    return ret;
}
