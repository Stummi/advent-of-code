import org.stummi.aoc.AdventOfCode

object Day24 : AdventOfCode(2021, 24) {
    override val part1: Any
        get() = TODO("Not yet implemented")

    override val part2: Any
        get() = TODO("Not yet implemented")


}

data class DigitChecker(
    val divZ: Boolean,
    val aVal: Int,
    val bVal: Int
) {
    fun check(inp: Int, curZ: Int): Int {
        var z = curZ
        if (divZ) {
            z /= 26
        }
        val x = curZ % 26 + aVal
        if (x != inp) {
            z *= 26
            z += (inp + bVal)
        }
        return z
    }
}


fun main() {
    val checkers = Day24.inputLines().filterNot { it.isBlank() }.chunked(18).map {
        //println(it)
        val divZ = it[4].split(" ")[2] == "26"
        val aVal = it[5].split(" ")[2].toInt()
        val bVal = it[15].split(" ")[2].toInt()
        println("$divZ\t$aVal\t$bVal")
        DigitChecker(divZ, aVal, bVal)
    }


    val c = checkers[1]
    println(c)
    (1..9).forEach {
        println("$it ${c.check(it, 0)}")
    }
}

/*
fun main() {
    val code = Day24.input().filterNot { it.isBlank() }.chunked(18).filterNot { it.isEmpty() }

    val lines = code[0].size

    (0 until lines).forEach {l ->
        print("$l")
        code.forEach {
            print("\t${it[l]}")
        }
        println()
    }

    /*
    code.forEach {
        it.forEachIndexed { id, s ->
            codePoints.getOrPut(id) { mutableSetOf() }.add(s)
        }
    }

    codePoints.forEach { println(it) }*/

}
*/
/*


inp w   //
mul x 0 // x = 0
add x z // x = z
mod x 26 // x %= 26
div z 1 // z /= 1    *** (sometimes 26)
add x 13 // x += 13  ***
eql x w // x = (x == w)
eql x 0 // x = !x
mul y 0 // y = 0
add y 25 // y = 25
mul y x // y *= x
add y 1 // y += 1
mul z y // z *= y
mul y 0 // y *= 0
add y w // y += w
add y 0 // y += 0 ***
mul y x // y *= x
add z y // z += y

z /= 26        #sometimes
x = z % 26 + a
y = (x == w) ? 1 : 26
z *= y
y = (inp + b) * x

z += y



*/
