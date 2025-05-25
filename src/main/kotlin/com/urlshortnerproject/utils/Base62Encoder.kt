package com.urlshortnerproject.utils

object Base62Encoder {
    private val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

    fun encode(num: Long): String {
        var n = num
        val sb = StringBuilder()
        while (n > 0) {
            sb.append(chars[(n % 62).toInt()])
            n /= 62
        }
        return sb.reverse().toString()
    }
}