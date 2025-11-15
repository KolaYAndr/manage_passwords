package com.kolayandr.passwordmanager.domain

import kotlinx.coroutines.flow.Flow

interface PasswordsRepository {

    suspend fun addPassword(password: Password)
    fun getListPasswords(): Flow<List<Password>>
    suspend fun removePassword(id: Long)
    suspend fun updatePassword(password: Password)
}