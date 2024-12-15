package org.stummi.aoc.y2024

import org.stummi.aoc.AdventOfCode
import org.stummi.aoc.helper.XY
import org.stummi.aoc.helper.splitToInts
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Day14 : AdventOfCode(2024, 14) {
    init {
        resourceSample(additionalData = XY(11, 7))
    }

    data class Robot(val p: XY, val v: XY) {
        fun update(steps: Int, bounds: XY): Robot {
            val x = (p.x + (v.x + bounds.x) * steps) % bounds.x
            val y = (p.y + (v.y + bounds.y) * steps) % bounds.y
            return Robot(XY(x, y), v)
        }
    }

    private val parsedInput by lazy {
        input.lines.map {
            val (p, v) = it.splitToInts(true).chunked(2).map { (x, y) -> XY(x, y) }
            Robot(p, v)
        }
    }

    private val bounds get() = XY(101, 103).orSample()

    override val part1: Any
        get() {
            val bounds = bounds
            val steps = 100
            val r = parsedInput.map {
                it.update(steps, bounds)
            }.toList()

            val (sx, sy) = bounds
            val mx = bounds.x / 2
            val my = bounds.y / 2

            return listOf(
                (XY.ZERO until XY(mx, my)),
                (XY(mx + 1, 0) until XY(sx, my)),
                (XY(0, my + 1) until XY(mx, sy)),
                (XY(mx + 1, my + 1) until XY(sx, sy)),
            ).map {
                it.asSequence().sumOf {
                    r.count { r -> r.p == it }
                }
            }.reduce { a, b -> a * b }
        }

    override val part2: Any
        get() {
            val robots = parsedInput
            repeat(100_000) { steps ->
                robots.map { it.update(steps, bounds) }.let { r ->
                    val xySet = r.map { it.p }.toSet()
                    if(isCandidate(xySet)) {
                        writeImage(xySet, steps)
                        return steps
                    }
                }
            }

            return 0
        }

    private fun isCandidate(xySet: Set<XY>): Boolean {
        val mSet = xySet.toMutableSet()
        while(mSet.isNotEmpty()) {
            val todo = mutableListOf<XY>()
            val result = mutableSetOf<XY>()
            val xy = mSet.first()
            todo.add(xy)

            while(todo.isNotEmpty()) {
                val p = todo.removeFirst()
                mSet.remove(p)
                result.add(p)
                p.orthogonalNeighbours().filter { it in mSet && it !in todo }.forEach {
                    todo.add(it)
                }
            }
            if(result.size > 100) {
                return true
            }
        }
        return false
    }

    private fun writeImage(xySet: Set<XY>, i: Int) {
        val img = BufferedImage(101, 103, BufferedImage.TYPE_INT_RGB)
        (XY.ZERO until XY(101, 103)).asSequence().forEach { p ->
            val color = if (p in xySet) {
                Color.BLACK
            } else {
                Color.WHITE
            }
            img.setRGB(p.x, p.y, color.rgb)
        }
        ImageIO.write(img, "PNG", File(String.format("output/%06d.png", i)))
    }
}
// 18193 too high
// 18192 too high

fun main() {
    Day14.fancyRun(includeDemo = false)
}
