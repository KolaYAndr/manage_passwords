package com.kolayandr.passwordmanager.ui.snackbar

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/**
 * SnackbarController queues messages and delivers them one-by-one to a provided suspend show function.
 *
 * Usage:
 *  - Create an instance in the Activity with a lifecycle scope: `SnackbarController(lifecycleScope)`
 *  - Call `start { msg -> snackbarHostState.showSnackbar(msg) }` from Compose's `LaunchedEffect`.
 *  - Call `showMessage("...")` to enqueue a message. Messages are delivered in FIFO order and won't be lost.
 */
class SnackbarController(private val scope: CoroutineScope) {
    private val messages = Channel<String>(Channel.UNLIMITED)

    /** Enqueue a message to be shown. Returns immediately; message delivery happens asynchronously. */
    fun showMessage(message: String) {
        if (!messages.isClosedForSend) {
            scope.launch {
                messages.send(message)
            }
        }
    }

    /**
     * Start consuming queued messages and deliver them via [show].
     * Should be called once — typically from Compose `LaunchedEffect` tied to the SnackbarHostState.
     */
    fun start(show: suspend (String) -> Unit) {
        scope.launch {
            for (msg in messages) {
                try {
                    show(msg)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    // swallow and continue to next message to avoid blocking the queue
                }
            }
        }
    }

    /** Close the internal queue and stop accepting new messages. */
    fun close() {
        messages.close()
    }
}
