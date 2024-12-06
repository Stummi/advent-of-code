package org.stummi.aoc.api

import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.time.*

object AocApi {
    val session by lazy { File("cookie").readText().trim() }
    val cache by lazy {
        Paths.get("input-cache/$session/").apply { Files.createDirectories(this) }
    }

    fun input(year: Int, day: Int): List<String> {
        val cachedFile = cache.resolve("${year}_$day.txt")
        if (!Files.exists(cachedFile)) {
            val data = readFromApi(year, day)
            Files.write(cachedFile, data)
        }
        return Files.readAllLines(cachedFile)
    }

    private fun readFromApi(year: Int, day: Int): List<String> {
        val availableFrom = LocalDate.of(year, 12, day).atTime(5, 0).atZone(ZoneOffset.UTC)
        if (ZonedDateTime.now().isBefore(availableFrom)) {
            throw Exception("Input for $year/$day is not available yet")
        }

        val url = URL("https://adventofcode.com/$year/day/$day/input")
        val connection = url.openConnection() as HttpURLConnection
        connection.addRequestProperty("Cookie", "session=$session")
        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        if (responseCode != 200) {
            throw Exception("Request to $url failed with response code: $responseCode")
        }

        return connection.inputStream.reader().use {
            it.readLines()
        }
    }
}
