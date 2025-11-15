package com.kolayandr.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.kolayandr.passwordmanager.ui.snackbar.SnackbarController
import com.kolayandr.passwordmanager.ui.theme.PasswordManagerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val snackbarController = SnackbarController(lifecycleScope)

        setContent {
            PasswordManagerTheme {
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(snackbarHostState) {
                    // start delivering queued messages to the Scaffold's SnackbarHostState
                    snackbarController.start { message ->
                        snackbarHostState.showSnackbar(message)
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Greeting(
                            name = "Android",
                            modifier = Modifier
                        )
                        DemoSnackbarButtons(snackbarController)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun DemoSnackbarButtons(controller: SnackbarController) {
    Column {
        Button(onClick = { controller.showMessage("Password saved successfully") }) {
            Text("Show success")
        }
        Button(onClick = {
            // enqueue several messages quickly to demonstrate queuing
            controller.showMessage("Error adding item")
            controller.showMessage("Failed authorization")
            controller.showMessage("Another notification")
        }) {
            Text("Enqueue 3 messages")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PasswordManagerTheme {
        Greeting("Android")
    }
}