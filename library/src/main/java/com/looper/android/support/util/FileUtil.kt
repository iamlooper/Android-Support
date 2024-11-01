package com.looper.android.support.util

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

object FileUtil {

    /**
     * Reads the entire content of a given file as a single string.
     *
     * @param file The File object from which to read.
     * @return The content of the file as a string, or an empty string if the file doesn't exist
     * or an error occurs during reading.
     */
    fun read(file: File): String {
        if (!file.exists()) {
            return ""
        }
        return try {
            BufferedReader(FileReader(file)).use { reader ->
                reader.readText()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Writes the provided [content] to a given file.
     *
     * @param file The File object to which to write.
     * @param content The string content to be written to the file.
     * @param append If true, the content will be appended to the end of the file.
     * If false, the file will be overwritten.
     * @return `true` if the write operation is successful, `false` otherwise.
     */
    fun write(file: File, content: String, append: Boolean = false): Boolean {
        return try {
            BufferedWriter(FileWriter(file, append)).use { writer ->
                writer.write(content)
                true
            }
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}