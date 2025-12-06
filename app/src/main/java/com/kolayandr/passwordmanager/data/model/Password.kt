package com.kolayandr.passwordmanager.data.model

data class Password(
    val id: String? = null,
    val itemName: String = "",        // Название (Google, Facebook)
    val website: String = "",         // URL (google.com)
    val username: String = "",        // Email/логин
    val encryptedPassword: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)