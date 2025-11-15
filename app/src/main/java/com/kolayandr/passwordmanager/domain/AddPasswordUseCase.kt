package com.kolayandr.passwordmanager.domain

class AddPasswordUseCase(private val passwordsRepository: PasswordsRepository) {

    suspend fun invoke(password: Password) {
        passwordsRepository.addPassword(password)
    }
}