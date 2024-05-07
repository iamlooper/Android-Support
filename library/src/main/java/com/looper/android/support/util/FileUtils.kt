package com.looper.android.support.util

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

object FileUtils {

    /**
     * Reads the content of a given file.
     *
     * @param file, the File object on which read operation to perform.
     * @return A list of strings representing lines read from the file.
     */
    fun read(file: File): List<String> {
        val lines = mutableListOf<String>()
        if (!file.exists()) {
            return lines
        }
        try {
            BufferedReader(FileReader(file)).useLines { lines.addAll(it) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return lines
    }

    /**
     * Writes the provided [lines] to a given file.
     *
     * @param file, the File object on which write operation to perform.
     * @param lines, the list of strings to be written to the file.
     * @param overWrite Flag indicating whether to overwrite the file if it already exists.
     * @return `true` if the write operation is successful, `false` otherwise.
     */
    fun write(file: File, lines: List<String>, overWrite: Boolean): Boolean {
        return try {
            BufferedWriter(FileWriter(file, !overWrite)).use { writer ->
                lines.forEach {
                    writer.write(it)
                    writer.newLine()
                }
                true
            }
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Deletes a given file.
     *
     * @param file, the File object to be deleted.
     * @return `true` if the file is successfully deleted, `false` otherwise.
     */
    fun delete(file: File): Boolean {
        return file.exists() && file.delete()
    }
   
    /**
     * Checks if the given file exists or is empty.
     *
     * @param file, the File object to be checked.
     * @return `true` if the file exists or its length is 0 (empty), `false` otherwise.
     */
    fun isExistOrEmpty(file: File): Boolean {
        return file.exists() || file.length() == 0L
    }
}