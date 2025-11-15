package com.kolayandr.passwordmanager.data

import android.app.Application
import androidx.compose.material.icons.materialIcon
import kotlinx.coroutines.flow.Flow
import com.kolayandr.passwordmanager.domain.Password
import com.kolayandr.passwordmanager.domain.PasswordsRepository

class PasswordsRepositoryImpl(application: Application): PasswordsRepository {

    private val passwordsDao = AppDatabase.getInstance(application).passwordsDao()
    private val mapper = PasswordsMapper()

    override suspend fun addPassword(password: Password) {
        passwordsDao.addPassword(mapper.mapEntityToDbModel(password))
    }

    override fun getListPasswords(): Flow<List<Password>> {
        return mapper.mapListDbModelToListEntity(passwordsDao.getListPasswords())
    }

    override suspend fun removePassword(id: Long) {
        passwordsDao.removePassword(id)
    }

    override suspend fun updatePassword(password: Password) {
        passwordsDao.updatePassword(mapper.mapEntityToDbModel(password))
    }
}