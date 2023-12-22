package org.stummi.aoc.y2023

import org.stummi.aoc.AdventOfCode

object Day22 : AdventOfCode(2023, 22) {
    private data class XYZ(
        val x: Int,
        val y: Int,
        val z: Int,
    ) {
        val down: XYZ
            get() {
                check(z > 1)
                return XYZ(x, y, z - 1)
            }

        override fun toString(): String = "($x,$y,$z)"
    }

    private data class Brick(
        val start: XYZ,
        val end: XYZ,
        val id: Int,
    ) {
        val cubes: List<XYZ>
            get() {
                return when {
                    start.x != end.x -> (start.x..end.x).map { XYZ(it, start.y, start.z) }
                    start.y != end.y -> (start.y..end.y).map { XYZ(start.x, it, start.z) }
                    start.z != end.z -> (start.z..end.z).map { XYZ(start.x, start.y, it) }
                    else -> listOf(start)
                }
            }

        val down: Brick get() = Brick(start.down, end.down, id)

        override fun toString(): String = "$start~$end"
    }

    private class Field(
        ctorBricks: List<Brick>,
    ) {
        val bricks = ctorBricks.toMutableList()
        val occupiedPoints = ctorBricks.flatMap { it.cubes }.toMutableSet()

        fun partsMovableDown(): List<Brick> {
            return bricks.filter { brick ->
                brick.start.z > 1 &&

                        brick.down.cubes.none {
                            it !in brick.cubes &&
                                    it in occupiedPoints
                        }
            }
        }

        fun moveDown(b: Brick) {
            check(bricks.remove(b))
            b.cubes.forEach {
                check(occupiedPoints.remove(it))
            }
            val down = b.down
            bricks.add(down)
            down.cubes.forEach {
                check(occupiedPoints.add(it))
            }

        }
    }

    private val parsedInput by lazy {
        input.lines.mapIndexed { idx, line ->
            line.split("~").map {
                it.split(",").map(String::toInt).let { (x, y, z) -> XYZ(x, y, z) }
            }.let { (start, end) ->
                Brick(start, end, idx)
            }
        }
    }

    override val part1: Any
        get() {
            val f = Field(parsedInput)
            settleField(f)

            return f.bricks.count {
                Field(f.bricks - it).partsMovableDown().isEmpty()
            }
        }

    override val part2: Any
        get() {
            val f = Field(parsedInput)
            settleField(f)

            val bricksById = f.bricks.associateBy { it.id }

            return f.bricks.sumOf {
                val newField = Field(f.bricks - it)
                if (!settleField(newField)) {
                    return@sumOf 0
                }

                newField.bricks.count {
                    it != bricksById[it.id]
                }
            }
        }

    private fun settleField(f: Field): Boolean {
        var changed = false
        while (true) {
            val down = f.partsMovableDown()
            if (down.isEmpty()) {
                break
            }
            changed = true

            down.forEach {
                f.moveDown(it)
            }
        }
        return changed
    }


}

fun main() {
    Day22.fancyRun()
}
