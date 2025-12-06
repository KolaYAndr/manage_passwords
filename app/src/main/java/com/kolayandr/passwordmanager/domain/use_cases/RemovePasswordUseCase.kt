package com.kolayandr.passwordmanager.domain.use_cases

import com.kolayandr.passwordmanager.domain.repository.PasswordsRepository

class RemovePasswordUseCase(private val passwordsRepository: PasswordsRepository) {

    suspend fun invoke(id: Long) {
        passwordsRepository.removePassword(id)
    }
}