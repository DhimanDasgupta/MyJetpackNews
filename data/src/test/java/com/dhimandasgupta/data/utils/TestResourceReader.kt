package com.dhimandasgupta.data.utils

object TestResourceReader {
    fun readTestResourceFile(fileName: String): String {
        val fileInputStream = javaClass.classLoader?.getResourceAsStream(fileName)
        return fileInputStream?.bufferedReader()?.readText()?.replace("\\\\", "") ?: ""
    }
}
