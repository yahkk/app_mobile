package com.example.scanner.utils

import java.io.IOException

object NetworkUtils {
    @Throws(IOException::class)
    fun makeHTTPRequest(url: URL): String? {
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        val inputStream: InputStream = connection.getInputStream()
        return try {
            val scanner = Scanner(inputStream)
            scanner.useDelimiter("\\A")
            val hasInput: Boolean = scanner.hasNext()
            if (hasInput) scanner.next() else null
        } finally {
            connection.disconnect()
        }
    }
}