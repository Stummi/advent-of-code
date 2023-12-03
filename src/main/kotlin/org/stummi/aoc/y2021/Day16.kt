import java.math.BigInteger
import java.util.BitSet

sealed interface Packet {
    val version: Int
    val type: Int

    val versionSum: Int
    val result: BigInteger

    val expr: String
}

data class LiteralPacket(
    override val version: Int,
    override val type: Int,
    val value: Long
) : Packet {
    override val versionSum: Int
        get() = version

    override val result: BigInteger
        get() = value.toBigInteger()

    override val expr: String
        get() = value.toString()
}

data class OperatorPacket(
    override val version: Int,
    override val type: Int,
    val subPackets: List<Packet>
) : Packet {

    companion object {
        val chars = mapOf<Int, (List<String>) -> String>(
            0 to { it.joinToString(" + ", prefix = "(", postfix = ")") },
            1 to { it.joinToString(" * ", prefix = "(", postfix = ")") },
            2 to { it.joinToString(prefix = "min(", postfix = ")") },
            3 to { it.joinToString(prefix = "max(", postfix = ")") },
            5 to { "(${it[0]} > ${it[1]})" },
            6 to { "(${it[0]} < ${it[1]})" },
            7 to { "(${it[0]} == ${it[1]})" }
        )
    }

    override val versionSum: Int
        get() = version + subPackets.sumOf { it.versionSum }

    override val result: BigInteger
        get() = when (type) {
            0 -> subPackets.sumOf { it.result }
            1 -> subPackets.map { it.result }.reduce { a, b -> a * b }
            2 -> subPackets.minOf { it.result }
            3 -> subPackets.maxOf { it.result }
            5 -> cmp { a, b -> a > b }
            6 -> cmp { a, b -> a < b }
            7 -> cmp { a, b -> a == b }
            else -> throw IllegalArgumentException("type $type not defined")
        }

    override val expr: String
        get() = chars[type]!!(subPackets.map { it.expr })

    private fun cmp(check: (Int, Int) -> Boolean): BigInteger {
        val res = subPackets[0].result.compareTo(subPackets[1].result)
        return if (check(res, 0)) BigInteger.ONE else BigInteger.ZERO
    }
}

class PacketReader(val bs: BitSet) {
    var pos: Int = 0;

    private fun readHeader(): Pair<Int, Int> = readNumber(3) to readNumber(3)

    fun readNumber(bitlen: Int) =
        generateSequence { nextBit() }.take(bitlen).reduce { ret, b ->
            (ret shl 1) or b
        }

    fun readLiteral(): Long {
        var ret = 0L
        var c = 0
        do {
            ++c
            //110 100 10111 11110 00101
            val hasMore = nextBit()
            val readNumber = readNumber(4)
            ret = (ret shl 4) + readNumber
        } while (hasMore == 1)

        return ret
    }

    fun readPacket(): Packet {
        val (version, type) = readHeader()
        if (type == 4) {
            return LiteralPacket(version, type, readLiteral())
        }
        val lengthTypeId = nextBit()
        val subPackets = if (lengthTypeId == 0) {
            readSubPacketsByLength(readNumber(15))
        } else {
            readSubPacketsByCount(readNumber(11))
        }
        return OperatorPacket(version, type, subPackets)
    }

    private fun readSubPacketsByCount(readNumber: Int): List<Packet> {
        return generateSequence { readPacket() }.take(readNumber).toList()
    }

    private fun readSubPacketsByLength(length: Int): List<Packet> {
        val originalPos = pos

        return sequence {
            while (pos < originalPos + length) {
                yield(readPacket())
            }
        }.toList().also {
            if (pos != originalPos + length) {
                throw IllegalStateException("pos should be ${originalPos + length} but is $pos")
            }
        }
    }

    fun nextBit(): Int {
        return if (bs.get(pos++)) 1 else 0
    }
}

fun main() {
    val value = Unit.javaClass.getResourceAsStream("/2021/16.txt").use {
        it!!.bufferedReader().readLines()
    }.first()

    val demoValues = listOf(
        "8A004A801A8002F478",
        "620080001611562C8802118E34",
        "C0015000016115A2E0802F182340",
        "A0016C880162017C3686B18A3D4780",

        "C200B40A82",
        "04005AC33890",
        "880086C3E88112",
        "CE00C43D881120",
        "D8005AC2A8F0",
        "F600BC2D8F",
        "9C005AC2F8F0",
        "9C0141080250320F1802104A08",

        value
    )



    demoValues.forEach {
        println(it)
        val pkg = it.toBitSet().let { PacketReader(it).readPacket() }
        println("  versionSum = ${pkg.versionSum} ")
        println("      result = ${pkg.result} ")
        println("        expr = ${pkg.expr}")
    }
}

fun String.toBitSet(): BitSet {
    check(length % 2 == 0) { "Must have an even length" }

    return chunked(2)
        .map { it.toInt(16).toByte().mirror() }
        .toByteArray().let { BitSet.valueOf(it) }
}

private fun Byte.mirror(): Byte {
    var ret = 0
    var cur = this.toUByte().toInt()
    repeat(8) {
        ret = ret shl 1
        ret = ret or (cur and 1)
        cur = cur shr 1
    }
    return ret.toByte()
}
