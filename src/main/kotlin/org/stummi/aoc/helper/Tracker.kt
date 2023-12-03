package org.stummi.aoc.helper

import kotlin.time.Duration.Companion.milliseconds

object Tracker {

    var data = mutableMapOf<String, Long>()

    fun <T> track(key: String, func: () -> T): T {
        val ms = System.currentTimeMillis()
        val ret = func()
        val ms2 = System.currentTimeMillis()
        data.compute(key) { _, it -> (it ?: 0) + (ms2 - ms) }
        return ret
    }

    fun prettyPrint() {
        data.entries.sortedBy { it.value }.forEach {
            println("${it.value.milliseconds} ${it.key}")
        }
    }
}
