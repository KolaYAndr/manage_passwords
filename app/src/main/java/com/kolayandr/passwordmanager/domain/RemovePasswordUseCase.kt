package com.kolayandr.passwordmanager.domain

class RemovePasswordUseCase(private val passwordsRepository: PasswordsRepository) {

    suspend fun invoke(id: Long) {
        passwordsRepository.removePassword(id)
    }
}