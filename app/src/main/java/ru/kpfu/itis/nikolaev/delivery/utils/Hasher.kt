package ru.kpfu.itis.nikolaev.delivery.utils

import java.security.MessageDigest

class Hasher {
    companion object{
        fun hash(inputString: String): String {
            val bytes = inputString.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) })
        }
    }
}