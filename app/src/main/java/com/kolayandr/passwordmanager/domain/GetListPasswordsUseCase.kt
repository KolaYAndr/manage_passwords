package com.kolayandr.passwordmanager.domain

import kotlinx.coroutines.flow.Flow

class GetListPasswordsUseCase(private val passwordsRepository: PasswordsRepository) {

    fun invoke(): Flow<List<Password>>{
        return passwordsRepository.getListPasswords()
    }
}