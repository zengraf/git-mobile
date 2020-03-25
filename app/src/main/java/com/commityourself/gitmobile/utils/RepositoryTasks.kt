package com.commityourself.gitmobile.utils

import com.commityourself.gitmobile.database.models.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.eclipse.jgit.api.Git
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

suspend fun Repository.clone(recursive: Boolean): Repository? {
    Timber.i("Attempting to clone \"$name\" from $remoteURL")
    var directory = openFile(localPath)
    directory.mkdirs()
    val cloneCommand = Git.cloneRepository()
        .setURI(remoteURL)
        .setCloneAllBranches(true)
        .setDirectory(directory)
        .setCloneSubmodules(recursive)

    //runBlocking { setCredentials(activity, cloneCommand, repo) }

    try {
        withContext(Dispatchers.IO) { cloneCommand.call() }
    } catch (t: OutOfMemoryError) {
        Timber.e(t, "Not enough memory on internal STORAGE to clone \"$name\" from $remoteURL")
        return null
    } catch (t: Throwable) {
        Timber.e(t, "Exception caught while cloning \"$name\" from $remoteURL to $localPath")
        return null
    }
    Timber.i("Cloned \"$name\" from $remoteURL to $localPath successfully")
    return this
}

suspend fun Repository.init(): Repository? {
    Timber.i("Attempting to init \"$name\" at $localPath")
    try {
        val directory = openFile(localPath)
        directory.mkdirs()
        withContext(Dispatchers.IO) { Git.init().setDirectory(directory).call() }
    } catch (t: Throwable) {
        Timber.e(t, "Exception caught while initialising \"$name\" at $localPath")
        return null
    }
    Timber.i("Initialised \"$name\" at $localPath successfully")
    return this
}

suspend fun Repository.update(): Repository = withContext(Dispatchers.IO) {
    val directory = openFile(localPath)
    if (directory.exists() && (readme == null || !File(directory, readme).exists()))
        readme = directory.list().run { find { it.toUpperCase() == "README.MD" } ?: firstOrNull { it.toUpperCase().contains(".MD") } }

    if (readme != null) {
            val reader = BufferedReader(FileReader(File(directory, readme)))
            var line: String? = reader.readLine()
            while (line != null) {
                if (line.isNotEmpty() && (line[0] in 'a'..'z' || line[0] in 'A'..'Z' || line[0] in '0'..'9')) {
                    val output = StringBuilder(line)
                    line = reader.readLine()
                    while (line != null && line.trim().isNotEmpty() && line[0] != '<') {
                        output.append(' ').append(line.trim())
                        line = reader.readLine()
                    }
                    description = output.toString()
                    break
                }
                line = reader.readLine()
            }
        }
    this@update
}

suspend fun Repository.delete() {
    Timber.i("Deleting \"$name\" from \"$localPath\"")
    try {
        openFile(localPath).delete()
        com.commityourself.gitmobile.component.repository().delete(this)
    } catch (t: Throwable) {
        Timber.e(t, "Exception caught while deleting \"$name\" from $localPath")
    }
    Timber.i("Deleted \"$name\" from $localPath successfully")
}
