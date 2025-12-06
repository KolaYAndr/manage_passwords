package com.kolayandr.passwordmanager.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kolayandr.passwordmanager.data.repository.SharedPasswordRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordDetailScreen(
    passwordId: String?,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    // Подключаем ViewModel
    val viewModel: PasswordDetailViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val savedStateHandle = SavedStateHandle().apply {
                    set("passwordId", passwordId)
                }
                return PasswordDetailViewModel(
                    repository = SharedPasswordRepository,
                    savedStateHandle = savedStateHandle
                ) as T
            }
        }
    )

    val screenMode by viewModel.screenMode.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Получаем контекст для копирования
    val context = LocalContext.current

    // Определяем заголовок в зависимости от режима
    val title = when {
        passwordId == null -> "Add New Password"
        screenMode == ScreenMode.EDIT -> "Edit Password"
        else -> "Password Details"
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = title,
                onBack = {
                    // Всегда просто возвращаемся назад
                    onBack()
                },
                actions = {
                    when (screenMode) {
                        ScreenMode.VIEW -> {
                            TextButton(onClick = { viewModel.switchToEditMode() }) {
                                Text("Edit")
                            }
                        }

                        ScreenMode.EDIT -> {
                            TextButton(
                                onClick = { viewModel.savePassword(onSuccess = onSave) },
                                enabled = uiState.website.isNotBlank() &&
                                        uiState.username.isNotBlank() &&
                                        uiState.password.isNotBlank()
                            ) {
                                Text("Save")
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (screenMode) {
                ScreenMode.VIEW -> ViewModeContent(
                    uiState = uiState,
                    context = context,
                    viewModel = viewModel
                )

                ScreenMode.EDIT -> EditModeContent(
                    uiState = uiState,
                    viewModel = viewModel,
                    onSave = { viewModel.savePassword(onSuccess = onSave) },
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
private fun CustomTopBar(
    title: String,
    onBack: () -> Unit,
    actions: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Кнопка назад слева
        TextButton(onClick = onBack) {
            Text("← Back")
        }

        // Заголовок по центру
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        // Действия справа
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions()
        }
    }
}

@Composable
private fun ViewModeContent(
    uiState: PasswordDetailState,
    context: android.content.Context,
    viewModel: PasswordDetailViewModel
) {
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Отступ от TopBar
        Spacer(modifier = Modifier.height(20.dp))

        // Первый контейнер: Иконка + Почта + Подпись Login
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(108.dp)
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Иконка сайта слева
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.website.take(2).uppercase(),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                // Почта и подпись Login справа
                Column {
                    // Почта (Email)
                    Text(
                        text = uiState.username,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Подпись "Login" под почтой
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        // Контейнер для Username (с кнопкой Copy)
        Spacer(modifier = Modifier.height(24.dp))
        UsernameContainer(username = uiState.username) {
            viewModel.copyToClipboard(uiState.username, context)
        }

        // Контейнер для Password (с кнопками Show/Hide и Copy)
        Spacer(modifier = Modifier.height(24.dp))
        PasswordContainer(
            password = uiState.password,
            showPassword = showPassword,
            onToggleVisibility = { showPassword = !showPassword }
        ) {
            viewModel.copyToClipboard(uiState.password, context)
        }

        // Контейнер для Website (с кнопкой Copy)
        Spacer(modifier = Modifier.height(24.dp))
        WebsiteContainer(website = uiState.website) {
            viewModel.copyToClipboard(uiState.website, context)
        }
    }
}

@Composable
private fun UsernameContainer(
    username: String,
    onCopyClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Username",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = username,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            TextButton(
                onClick = onCopyClicked,
                enabled = username.isNotBlank()
            ) {
                Text("Copy")
            }

        }
    }
}

@Composable
private fun PasswordContainer(
    password: String,
    showPassword: Boolean,
    onToggleVisibility: () -> Unit,
    onCopyClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Password",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (showPassword) password else "•".repeat(password.length),
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onToggleVisibility) {
                        Text(if (showPassword) "Hide" else "Show")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = onCopyClicked,
                        enabled = password.isNotBlank()
                    ) {
                        Text("Copy")
                    }
                }

            }
        }
    }
}

/*
@Composable
private fun NotesContainer(notes: String) {
    Box(
        modifier = Modifier  // ← ИСПРАВЛЕНО: Modider -> Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column {
            Text(
                text = "Notes",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notes,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
*/

@Composable
private fun WebsiteContainer(
    website: String,
    onCopyClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Website",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = website,
                    style = MaterialTheme.typography.bodyLarge
                )
            }


            TextButton(
                onClick = onCopyClicked,
                enabled = website.isNotBlank()
            ) {
                Text("Copy")
            }

        }
    }
}

@Composable
private fun EditModeContent(
    uiState: PasswordDetailState,
    viewModel: PasswordDetailViewModel,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // 1. Item Name (название сервиса)

        OutlinedTextField(
            value = uiState.website,
            onValueChange = viewModel::updateWebsite,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Google, Facebook, etc.") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            isError = uiState.website.isBlank(),
            label = {
                Text(
                    text = "Item Name",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Email

        OutlinedTextField(
            value = uiState.username,
            onValueChange = viewModel::updateUsername,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("user@example.com") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.username.isBlank(),
            label = {
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Password

        OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::updatePassword,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter password") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { showPassword = !showPassword }) {
                    Text(if (showPassword) "Hide" else "Show")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = uiState.password.isBlank(),
            label = {
                Text(
                    text = "Password",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Website (URL)

        OutlinedTextField(
            value = uiState.website,
            onValueChange = viewModel::updateWebsite,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("https://example.com") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            isError = uiState.website.isBlank(),
            label = {
                Text(
                    text = "Website",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Кнопка сохранения
        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = uiState.website.isNotBlank() &&
                    uiState.username.isNotBlank() &&
                    uiState.password.isNotBlank(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Save")
        }

        // Кнопка отмены
        TextButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Cancel")
        }

        // Сообщение об ошибке
        uiState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}