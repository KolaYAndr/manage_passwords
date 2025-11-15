package com.kolayandr.passwordmanager.domain

import androidx.room.PrimaryKey

data class Password(
    val id: Long,
    val username: String,
    val encryptedPassword: String,
    val website: String,
)