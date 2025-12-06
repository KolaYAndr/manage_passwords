package com.kolayandr.passwordmanager.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kolayandr.passwordmanager.data.dao.PasswordsDao
import com.kolayandr.passwordmanager.data.models.PasswordDbModel

@Database(entities = [PasswordDbModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun passwordsDao(): PasswordsDao

    companion object {

        private const val NAME_DB = "passwords.db"
        private var INSTANCE: AppDatabase? = null

        fun getInstance(application: Application): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    NAME_DB
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}