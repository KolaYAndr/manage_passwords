package com.kolayandr.passwordmanager.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.net.PasswordAuthentication

@Database(entities = [PasswordDbModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun passwordsDao(): PasswordsDao

    companion object {

        private const val NAME_DB = "passwords.db"
        private var INSTANCE: AppDatabase? = null

        fun getInstance(application: Application): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            val instance = Room.databaseBuilder(
                application,
                AppDatabase::class.java, NAME_DB
            ).build()
            INSTANCE = instance
            return instance
        }
    }
}