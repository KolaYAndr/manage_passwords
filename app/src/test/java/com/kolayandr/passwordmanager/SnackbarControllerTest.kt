package com.kolayandr.passwordmanager

import com.kolayandr.passwordmanager.ui.snackbar.SnackbarController
import com.kolayandr.passwordmanager.ui.snackbar.models.SnackbarEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SnackbarControllerTest {
    @Test
    fun enqueuesAndDeliversEvents() = runBlocking {
        val delivered = mutableListOf<String>()
        val controller = SnackbarController(this)

        // start a collector to record delivered messages
        val job = launch {
            controller.events.collect { evt ->
                delivered.add(evt.message)
            }
        }

        controller.postEvent(SnackbarEvent("one"))
        controller.postEvent(SnackbarEvent("two"))
        controller.postEvent(SnackbarEvent("three"))

        // give the collector some time to process queued events
        delay(200)

        assertEquals(listOf("one", "two", "three"), delivered)
        job.cancel()
        controller.close()
    }
}
