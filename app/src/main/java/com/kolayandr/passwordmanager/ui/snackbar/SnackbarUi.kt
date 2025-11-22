package com.kolayandr.passwordmanager.ui.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.material3.SnackbarHostState
import com.kolayandr.passwordmanager.ui.snackbar.models.SnackbarEvent

/**
 * Public reusable composable that collects events from [SnackbarController]
 * and shows snackbars on the provided [SnackbarHostState].
 */
@Composable
fun CollectSnackbarEventOnLifecycle(
    controller: SnackbarController,
    snackbarHostState: SnackbarHostState
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner.lifecycle, controller) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            controller.events.collect { event: SnackbarEvent ->
                // If a snackbar is currently shown, dismiss it before showing next
                snackbarHostState.currentSnackbarData?.dismiss()

                val result = snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.action?.name,
                    withDismissAction = true,
                    duration = event.duration
                )

                if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                    event.action?.effect?.invoke()
                }
            }
        }
    }
}
