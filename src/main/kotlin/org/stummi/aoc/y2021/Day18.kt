interface SnailfishNumber {
    fun explode(d: Int = 1): ExplodeResult
    fun addLeft(v: Int): SnailfishNumber
    fun addRight(v: Int): SnailfishNumber
    fun split(): SnailfishNumber

    operator fun plus(other: SnailfishNumber) = SnailfishPair(this, other)

    fun reduce(): SnailfishNumber {
        var cur = this
        while (true) {
            val next = cur.reduceStep()
            if (next == cur) {
                return cur
            }
            cur = next
        }
    }

    fun reduceStep(): SnailfishNumber {
        val explodeResult = this.explode()
        if (explodeResult.didExplode) {
            return explodeResult.newValue
        } else {
            return split()
        }
    }

    fun mag(): Long
}


data class SnailfishPair(
    val left: SnailfishNumber,
    val right: SnailfishNumber
) : SnailfishNumber {
    override fun explode(d: Int): ExplodeResult {
        if (d == 5) {
            val lValue = (left as SnailfishLiteral).value
            val rValue = (right as SnailfishLiteral).value
            return ExplodeResult(true, SnailfishLiteral(0), lValue, rValue)
        } else {
            val lexp = left.explode(d + 1)
            if (lexp.didExplode) {
                val newRight = if (lexp.rightRemaining != 0) {
                    right.addLeft(lexp.rightRemaining)
                } else {
                    right
                }
                return ExplodeResult(
                    true,
                    lexp.newValue + newRight,
                    lexp.leftRemaining, 0
                )
            }

            val rexp = right.explode(d + 1)
            if (rexp.didExplode) {
                val newLeft = if (rexp.leftRemaining != 0) {
                    left.addRight(rexp.leftRemaining)
                } else {
                    left
                }
                return ExplodeResult(
                    true,
                    newLeft + rexp.newValue,
                    0, rexp.rightRemaining
                )
            }
        }
        return ExplodeResult(false, this, 0, 0)
    }

    override fun addLeft(v: Int): SnailfishNumber {
        return left.addLeft(v) + right
    }

    override fun addRight(v: Int): SnailfishNumber {
        return left + right.addRight(v)
    }

    override fun split(): SnailfishNumber {
        val lval = left.split()

        if (lval != left) {
            return lval + right
        }

        val rval = right.split()

        if (rval != right) {
            return left + rval
        }

        return this
    }

    override fun mag(): Long {
        return left.mag() * 3 + right.mag() * 2
    }

    override fun toString() = "[$left,$right]"
}

data class SnailfishLiteral(
    val value: Int
) : SnailfishNumber {
    override fun explode(d: Int) =
        ExplodeResult(false, this, 0, 0)

    override fun toString() = "$value"

    override fun addLeft(v: Int): SnailfishNumber {
        return SnailfishLiteral(value + v)
    }

    override fun addRight(v: Int) = addLeft(v)

    override fun split(): SnailfishNumber = if (value < 10) {
        this
    } else {
        val lval = value / 2
        val rval = value - lval
        SnailfishLiteral(lval) + SnailfishLiteral(rval)
    }

    override fun mag(): Long {
        return value.toLong()
    }
}

data class ExplodeResult(
    val didExplode: Boolean,
    val newValue: SnailfishNumber,
    val leftRemaining: Int,
    val rightRemaining: Int,
)

fun main() {

    val lines = Unit.javaClass.getResourceAsStream("/2021/18.txt").use {
        it!!.bufferedReader().readLines()
    }.map { parseSnailfishNumber(it) }

    val result = lines.reduce { a, b ->
        (a + b).reduce()
    }.mag()

    println(result)



    lines.flatMap { l ->
        (lines - l).map { r -> l to r }
    }.maxOf { (a, b) -> (a + b).reduce().mag() }.let {
        println(it)
    }

}

fun parseSnailfishNumber(input: String) = parseSnailfishNumber(ArrayDeque(input.toList()))

fun parseSnailfishNumber(input: ArrayDeque<Char>): SnailfishNumber {
    var char = input.removeFirst()
    if (char.isDigit()) {
        return SnailfishLiteral(char.digitToInt())
    }

    check(char == '[')
    val left = parseSnailfishNumber(input)
    val comma = input.removeFirst()
    check(comma == ',')
    val right = parseSnailfishNumber(input)
    val closing = input.removeFirst()
    check(closing == ']')
    return left + right
}
