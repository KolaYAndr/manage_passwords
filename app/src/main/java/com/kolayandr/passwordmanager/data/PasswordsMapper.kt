package com.kolayandr.passwordmanager.data

import kotlinx.coroutines.flow.Flow
import com.kolayandr.passwordmanager.domain.Password
import kotlinx.coroutines.flow.map

class PasswordsMapper {

    fun mapEntityToDbModel(password: Password) = PasswordDbModel(
        id = password.id,
        username = password.username,
        encryptedPassword = password.encryptedPassword,
        website = password.website
    )

    fun mapDbModelToEntity(passwordDbModel: PasswordDbModel) = Password(
        id = passwordDbModel.id,
        username = passwordDbModel.username,
        encryptedPassword = passwordDbModel.encryptedPassword,
        website = passwordDbModel.website
    )

    fun mapListDbModelToListEntity(list: Flow<List<PasswordDbModel>>): Flow<List<Password>> {
        return list.map { dbList ->
            dbList.map { mapDbModelToEntity(it) }
        }
    }
}