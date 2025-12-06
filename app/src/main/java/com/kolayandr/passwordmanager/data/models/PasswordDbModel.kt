package com.kolayandr.passwordmanager.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passwords")
data class PasswordDbModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val username: String,
    val encryptedPassword: String,
    var serviceName: String?
)