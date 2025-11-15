package com.kolayandr.passwordmanager

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SnackbarControllerTest {
    @Test
    fun enqueuesAndDeliversMessages() = runBlocking {
        val delivered = mutableListOf<String>()
        val controller = com.kolayandr.passwordmanager.ui.snackbar.SnackbarController(this)

        controller.start { msg ->
            delivered.add(msg)
        }

        controller.showMessage("one")
        controller.showMessage("two")
        controller.showMessage("three")

        // give the consumer some time to process queued messages
        delay(100)

        assertEquals(listOf("one", "two", "three"), delivered)
        controller.close()
    }
}
