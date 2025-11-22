package com.kolayandr.passwordmanager

import java.util.concurrent.ConcurrentHashMap

class InMemoryPasswordRepository(
    private val encryptor: PasswordEncryptor
) : PasswordRepository {
    private val store = ConcurrentHashMap<String, String>()

    override suspend fun savePassword(id: String, plainPassword: String) {
        store[id] = encryptor.encrypt(plainPassword)
    }

    override suspend fun getPassword(id: String): String? {
        return store[id]?.let { encryptor.decrypt(it) }
    }
}
