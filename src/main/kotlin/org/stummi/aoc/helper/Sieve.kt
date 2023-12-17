package org.stummi.aoc.helper

import java.util.BitSet

/**
 * Sieve of Eratosthenes implementation
 */
object Sieve {
    private var size: Long = 128
    private val v: BitSet = BitSet()

    init {
        fillSieve(0)
    }

    private fun resize(newSize: Long) {
        require(((newSize - 3) / 2) <= Integer.MAX_VALUE) { "Out of Range" }

        val oldSize = size
        //v.resize((newSize + 1) / 2)
        size = newSize
        if (newSize < oldSize) {
            return
        }

        fillSieve(oldSize)
    }

    private fun fillSieve(oldSize: Long) {
        for (j in 3..size step 2) {
            if (!get(j)) {
                continue
            }

            var b = if (oldSize < 3 * j) 3 * j else {
                var l = oldSize / j
                if (l % 2L == 0L) {
                    ++l
                }
                l * j
            }

            for (k in (b..size step (j * 2))) {
                v.set(vectorPos(k))
            }
        }
    }

    operator fun get(i: Long) = when {
        i <= 1L -> false
        i == 2L -> true
        i % 2 == 0L -> false
        else -> {
            assertSize(i)
            !v.get(vectorPos(i))
        }
    }

    private fun assertSize(i: Long) = synchronized(v) {
        if (i <= size) {
            return
        }

        var newSize = size
        while (newSize < i) {
            newSize *= 4
        }

        resize(newSize)
    }

    private fun vectorPos(i: Long) = ((i - 3) / 2).toInt()

    fun primesInRange(r: LongRange) = object : Iterable<Long> {
        override operator fun iterator() = object : Iterator<Long> {
            var cur = r.first


            var next: Long? = null

            init {
                findNext()
            }

            fun findNext() {
                while (cur <= r.last) {
                    if (get(cur)) {
                        next = cur++
                        return
                    }
                    ++cur
                }
                next = null
            }

            override fun hasNext(): Boolean = next != null

            override fun next(): Long {
                val ret = next
                findNext()
                return ret!!
            }

        }
    }

    operator fun get(i: Int): Boolean {
        return get(i.toLong())
    }
}
