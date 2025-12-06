package com.kolayandr.passwordmanager.data.mappers

import com.kolayandr.passwordmanager.data.models.PasswordDbModel
import com.kolayandr.passwordmanager.domain.entities.Password
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PasswordsMapper {

    fun mapEntityToDbModel(password: Password) = PasswordDbModel(
        id = password.id,
        username = password.username,
        encryptedPassword = password.encryptedPassword,
        serviceName = password.serviceName
    )

    fun mapDbModelToEntity(passwordDbModel: PasswordDbModel) = Password(
        id = passwordDbModel.id,
        username = passwordDbModel.username,
        encryptedPassword = passwordDbModel.encryptedPassword,
        serviceName = passwordDbModel.serviceName
    )

    fun mapListDbModelToListEntity(list: Flow<List<PasswordDbModel>>): Flow<List<Password>> {
        return list.map { dbList ->
            dbList.map { mapDbModelToEntity(it) }
        }
    }
}