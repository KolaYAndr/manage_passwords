package com.kolayandr.passwordmanager.data.repository_impl

import android.app.Application
import com.kolayandr.passwordmanager.data.database.AppDatabase
import com.kolayandr.passwordmanager.data.mappers.PasswordsMapper
import com.kolayandr.passwordmanager.domain.entities.Password
import com.kolayandr.passwordmanager.domain.repository.PasswordsRepository
import kotlinx.coroutines.flow.Flow

class PasswordsRepositoryImpl(application: Application): PasswordsRepository {

    private val passwordsDao = AppDatabase.Companion.getInstance(application).passwordsDao()
    private val mapper = PasswordsMapper()

    override suspend fun upsertPassword(password: Password) {
        passwordsDao.upsertPassword(mapper.mapEntityToDbModel(password))
    }

    override fun getListPasswords(): Flow<List<Password>> {
        return mapper.mapListDbModelToListEntity(passwordsDao.getListPasswords())
    }

    override suspend fun removePassword(id: Long) {
        passwordsDao.removePassword(id)
    }
}