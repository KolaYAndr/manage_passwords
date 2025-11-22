package com.kolayandr.passwordmanager.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object Auth : Screen()

    @Serializable
    data object PasswordList : Screen()

    @Serializable
    data class PasswordDetail(val passwordId: String?) : Screen()
}