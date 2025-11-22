package com.kolayandr.passwordmanager

interface PasswordEncryptor {
    fun encrypt(plain: String): String
    fun decrypt(encrypted: String): String
}
