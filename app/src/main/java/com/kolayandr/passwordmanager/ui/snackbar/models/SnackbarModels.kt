package com.kolayandr.passwordmanager.ui.snackbar.models

import androidx.compose.material3.SnackbarDuration

/**
 * Data models for snackbar events and actions.
 */
data class SnackbarEvent(
    val message: String,
    val duration: SnackbarDuration = SnackbarDuration.Long,
    val action: SnackbarAction? = null
)

data class SnackbarAction(
    val name: String,
    val effect: suspend () -> Unit
)
