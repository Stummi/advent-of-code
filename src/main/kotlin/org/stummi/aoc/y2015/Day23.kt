package org.stummi.aoc.y2015


fun main() {
    val lines = Unit.javaClass.getResourceAsStream("/2015/23.txt").use {
        it!!.bufferedReader().readLines()
    }.map {
        it.split(" ", limit = 2)
    }.map {
        it[0] to it[1].split(", ")
    }

    println(lines)
    var registers = mutableMapOf("a" to 1, "b" to 0)
    var inst = 0;
    while (inst < lines.size) {
        val code = lines[inst]
        val opCode = code.first
        val args = code.second
        when (code.first) {
            "jio" ->
                if (registers[args[0]] == 1) {
                    inst += args[1].toInt()
                    continue
                }

            "inc" ->
                registers[args[0]] = registers[args[0]]!! + 1

            "tpl" ->
                registers[args[0]] = registers[args[0]]!! * 3

            "hlf" ->
                registers[args[0]] = registers[args[0]]!! / 2

            "jmp" -> {
                inst += args[0].toInt()
                continue
            }

            "jie" -> if (registers[args[0]]!! % 2 == 0) {
                inst += args[1].toInt()
                continue
            }

            else -> throw IllegalArgumentException(code.toString())
        }

        ++inst
        println("$code => $registers $inst")
    }
}
