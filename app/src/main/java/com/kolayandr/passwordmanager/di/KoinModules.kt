package com.kolayandr.passwordmanager.di

import com.kolayandr.passwordmanager.ui.snackbar.SnackbarController
import com.kolayandr.passwordmanager.ui.snackbar.models.SnackbarEvent
import com.kolayandr.passwordmanager.ui.snackbar.models.SnackbarAction
import org.koin.dsl.module
import com.kolayandr.passwordmanager.PasswordRepository
import com.kolayandr.passwordmanager.PasswordEncryptor
import com.kolayandr.passwordmanager.SimplePasswordEncryptor
import com.kolayandr.passwordmanager.InMemoryPasswordRepository

val coreModule = module {
    single<PasswordEncryptor> { SimplePasswordEncryptor() }
    single<PasswordRepository> { InMemoryPasswordRepository(get()) }
}
