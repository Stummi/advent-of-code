fun main() {

    val yRange = -109..-63
    val xRange = 179..201

    /*
        val yRange = -10..-5
        val xRange = 20..30
    */

    val validYVelos = (-110..110).asSequence().filter { v ->
        var vel = v
        var y = 0

        // println(vel)

        while (y >= yRange.first) {
            y += vel
            vel--
            //  println("  $vel\t$y")
            if (y in yRange) {
                return@filter true
            }
        }
        return@filter false
    }

    print("Part 1: ")
    validYVelos.maxOrNull()!!.let { (1..it).sum() }.let { println(it) }


    val validXVelos = (1..201).filter { xv ->
        var xVel = xv
        var x = 0
        while (xVel > 0) {
            x += xVel
            if (x in xRange) {
                return@filter true
            }
            --xVel
        }
        return@filter false;
    }


    validXVelos.flatMap { x -> validYVelos.map { y -> x to y } }.filter {
        var xVel = it.first
        var yVel = it.second
        var x = 0
        var y = 0
        while (x <= xRange.last && y >= yRange.first) {
            x += xVel
            y += yVel

            if (x in xRange && y in yRange) {
                return@filter true
            }

            xVel = xVel.dec().coerceAtLeast(0)
            yVel = yVel.dec()
        }
        return@filter false
    }.count().let { println("Part 2: $it") }
    /*
    }
*/
}
