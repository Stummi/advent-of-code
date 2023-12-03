package org.stummi.aoc.y2015

import java.security.MessageDigest

fun main() {
    val md = MessageDigest.getInstance("MD5")

    val input = "ckczppom"
    var idx = 0;
    var zero: Byte = 0
    var bF0: Byte = 0xF0.toByte()
    while (true) {
        val d = md.digest("$input$idx".toByteArray())
        if (d[0] == zero && d[1] == zero && d[2] == zero) {
            println("$idx")
            break
        }
        ++idx
    }
}
