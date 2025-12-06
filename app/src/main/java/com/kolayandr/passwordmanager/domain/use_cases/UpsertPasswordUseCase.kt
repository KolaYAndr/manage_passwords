package com.kolayandr.passwordmanager.domain.use_cases

import com.kolayandr.passwordmanager.domain.entities.Password
import com.kolayandr.passwordmanager.domain.repository.PasswordsRepository

class UpsertPasswordUseCase(private val passwordsRepository: PasswordsRepository) {

    suspend fun invoke(password: Password) {
        passwordsRepository.upsertPassword(password)
    }
}