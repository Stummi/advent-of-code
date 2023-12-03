package org.stummi.aoc.y2015

sealed interface Gatter {
    fun getVariableInputs(): List<String> = getInputs().filter {
        isVariable(it)
    }

    fun isVariable(it: String) = it[0].isLetter()

    fun getInputs(): List<String>

    fun getValue(resolver: (String) -> UShort): UShort

    fun getValueFromInputs(resolver: (String) -> UShort, inputName: String): UShort {
        return if (inputName[0].isDigit()) {
            inputName.toUShort()
        } else {
            resolver(inputName)
        }
    }

    val output: String;
}

data class NotOp(val input: String, override val output: String) : Gatter {
    override fun getInputs(): List<String> {
        return listOf(input)
    }

    override fun getValue(resolver: (String) -> UShort): UShort {
        return getValueFromInputs(resolver, this.input).inv()
    }


}

data class StaticOp(val input: String, override val output: String) : Gatter {
    override fun getInputs(): List<String> {
        return listOf(input)
    }

    override fun getValue(resolver: (String) -> UShort): UShort {
        return getValueFromInputs(resolver, this.input)
    }
}

data class BinOp(val leftInput: String, val op: String, val rightInput: String, override val output: String) : Gatter {
    override fun getInputs(): List<String> {
        return listOf(leftInput, rightInput)
    }

    override fun getValue(resolver: (String) -> UShort): UShort {
        val left = getValueFromInputs(resolver, leftInput)
        val right = getValueFromInputs(resolver, rightInput)

        return when (op) {
            "LSHIFT" -> (left.toInt() shl right.toInt()).toUShort()
            "RSHIFT" -> (left.toInt() shr right.toInt()).toUShort()
            "OR" -> (left.toInt() or right.toInt()).toUShort()
            "AND" -> (left.toInt() and right.toInt()).toUShort()
            else -> throw NotImplementedError("$left $op $right")
        }
    }
}

fun main() {
    val input = Unit.javaClass.getResourceAsStream("/2015/7.txt").use {
        it!!.bufferedReader().readLines()
    }.map {
        it.split(" -> ").let { s ->
            s[0].split(" ") to s[1]
        }
    }.map {
        when (it.first.size) {
            1 -> StaticOp(it.first[0], it.second)
            2 -> NotOp(it.first[1], it.second)
            3 -> BinOp(it.first[0], it.first[1], it.first[2], it.second)
            else -> throw IllegalArgumentException()
        }
    }.toList()

    //val aOut = input.filter { it.output == "a" }.first()
    //println(aOut.getValue(input))


    var remaining = input;
    var currentList = emptyList<Gatter>()

    while (remaining.isNotEmpty()) {
        val knownInputs = currentList.map { it.output }.toList()
        val p = remaining.partition {
            it.getVariableInputs().all { it in knownInputs }
        }

        if (p.first.isEmpty()) {
            throw IllegalArgumentException("something is wrong")

        }
        currentList = currentList + p.first
        remaining = p.second


    }


    val map = mutableMapOf<String, UShort>()
    val resolver: (String) -> UShort = { s -> map[s]!! }
    currentList.forEach {
        val value = it.getValue(resolver)
        println("$value // $it")
        map[it.output] = value
    }

    map.toSortedMap().forEach { k, v ->
        println("$k $v")
    }


}

