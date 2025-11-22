package com.kolayandr.passwordmanager.ui.snackbar

import com.kolayandr.passwordmanager.ui.snackbar.models.SnackbarAction
import com.kolayandr.passwordmanager.ui.snackbar.models.SnackbarEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/** Controller that exposes a Flow of [SnackbarEvent] and allows posting events. */
class SnackbarController(private val scope: CoroutineScope) {
    private val _events: Channel<SnackbarEvent> = Channel(Channel.UNLIMITED)
    val events: Flow<SnackbarEvent> = _events.receiveAsFlow()

    /** Post an event (non-blocking) from any thread. */
    fun postEvent(event: SnackbarEvent) {
        if (!_events.isClosedForSend) {
            scope.launch { _events.send(event) }
        }
    }

    /** Suspendable send (awaits if channel is full). */
    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }

    /** Convenience helper to post a simple text message. */
    fun showMessage(message: String) {
        postEvent(SnackbarEvent(message = message))
    }

    /** Close the internal channel and stop accepting new messages. */
    fun close() {
        _events.close()
    }

    @Deprecated("Use events Flow and postEvent/sendEvent instead")
    fun start(show: suspend (String) -> Unit) {
        // Backwards compatibility: consume events as plain messages
        scope.launch {
            for (evt in events) {
                try {
                    show(evt.message)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                }
            }
        }
    }
}
