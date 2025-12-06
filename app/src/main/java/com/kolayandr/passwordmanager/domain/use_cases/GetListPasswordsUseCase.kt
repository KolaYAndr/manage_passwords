package com.kolayandr.passwordmanager.domain.use_cases

import com.kolayandr.passwordmanager.domain.entities.Password
import com.kolayandr.passwordmanager.domain.repository.PasswordsRepository
import kotlinx.coroutines.flow.Flow

class GetListPasswordsUseCase(private val passwordsRepository: PasswordsRepository) {

    fun invoke(): Flow<List<Password>> {
        return passwordsRepository.getListPasswords()
    }
}