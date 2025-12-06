package com.kolayandr.passwordmanager.domain.repository

import com.kolayandr.passwordmanager.domain.entities.Password
import kotlinx.coroutines.flow.Flow

interface PasswordsRepository {

    suspend fun upsertPassword(password: Password)
    fun getListPasswords(): Flow<List<Password>>
    suspend fun removePassword(id: Long)
}