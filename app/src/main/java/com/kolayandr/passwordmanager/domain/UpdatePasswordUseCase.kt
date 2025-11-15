package com.kolayandr.passwordmanager.domain

class UpdatePasswordUseCase(private val passwordsRepository: PasswordsRepository) {

    suspend fun invoke(password: Password) {
        passwordsRepository.updatePassword(password)
    }
}