package com.commityourself.gitmobile.utils

import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder
import java.io.*
import java.util.*

val STORAGE: File by lazy { Environment.getExternalStorageDirectory() }

fun openFile(path: String): File {
    return File(STORAGE, path)
}

fun dateToString(date: Long): String {
    val timeDifference = Period(date, Calendar.getInstance().timeInMillis)
    return when {
        timeDifference.seconds < 60 -> "Less than a minute "
        timeDifference.minutes < 60 -> PeriodFormatterBuilder()
            .appendMinutes()
            .appendSuffix(" minute ", " minutes ")
            .printZeroNever()
            .toFormatter().print(timeDifference)
        timeDifference.hours < 24 -> PeriodFormatterBuilder()
            .appendHours()
            .appendSuffix(" hour ", " hours ")
            .printZeroNever()
            .toFormatter().print(timeDifference)
        timeDifference.days < 31 -> PeriodFormatterBuilder()
            .appendDays()
            .appendSuffix(" day ", " days ")
            .printZeroNever()
            .toFormatter().print(timeDifference)
        timeDifference.months < 12 -> PeriodFormatterBuilder()
            .appendMonths()
            .appendSuffix(" month ", " months ")
            .printZeroNever()
            .toFormatter().print(timeDifference)
        else -> PeriodFormatterBuilder()
            .appendYears()
            .appendSuffix(" year ", " years ")
            .printZeroNever()
            .toFormatter().print(timeDifference)
    } + "ago"
}

fun String.simplify(): String = toLowerCase().replace(' ', '-').filter { it in 'a'..'z' || it == '-' || it in '0'..'9' }

fun Long.sizeToString(): String {
    if (this == 1L)
        return "$this byte"
    if (this < 1024L)
        return "$this bytes"
    val kBytes = this / 1024
    if (kBytes < 1024L)
        return "$kBytes Kb"
    val mBytes = kBytes / 1024
    if (mBytes < 1024L)
        return "$mBytes Mb"
    val gBytes = mBytes / 1024
    if (gBytes < 1024L)
        return "$gBytes Gb"
    val tBytes = gBytes / 1024
    if (tBytes < 1024L)
        return "$tBytes Tb"
    return "${tBytes / 1024L} Tb"
}

suspend fun readFile(file: File): String = withContext(Dispatchers.IO) {
    val output = StringBuilder()
    val reader = BufferedReader(FileReader(file))
    var line = reader.readLine()

    while (line != null) {
        output.append(line)
        output.append("\n")
        line = reader.readLine()
    }

    output.toString()
}