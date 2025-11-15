package com.kolayandr.passwordmanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kolayandr.passwordmanager.domain.Password
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordsDao {

    @Insert
    suspend fun addPassword(password: PasswordDbModel)
    @Query("SELECT * FROM passwords ORDER BY website COLLATE NOCASE, username COLLATE NOCASE")
    fun getListPasswords(): Flow<List<PasswordDbModel>>
    @Query("DELETE FROM passwords WHERE id = :id")
    suspend fun removePassword(id: Long)
    @Update
    suspend fun updatePassword(password: PasswordDbModel)
}