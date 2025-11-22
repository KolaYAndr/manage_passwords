package com.kolayandr.passwordmanager

interface PasswordRepository {
    suspend fun savePassword(id: String, plainPassword: String)
    suspend fun getPassword(id: String): String?
}
