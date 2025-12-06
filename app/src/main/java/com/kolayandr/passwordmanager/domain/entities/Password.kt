package com.kolayandr.passwordmanager.domain.entities

data class Password(
    val id: Long,
    val username: String,
    val encryptedPassword: String,
    var serviceName: String?
)