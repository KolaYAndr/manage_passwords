package com.kolayandr.passwordmanager.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kolayandr.passwordmanager.data.models.PasswordDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordsDao {

    @Upsert
    suspend fun upsertPassword(password: PasswordDbModel)
    @Query("SELECT * FROM passwords ORDER BY serviceName COLLATE NOCASE, username COLLATE NOCASE")
    fun getListPasswords(): Flow<List<PasswordDbModel>>
    @Query("DELETE FROM passwords WHERE id = :id")
    suspend fun removePassword(id: Long)
}