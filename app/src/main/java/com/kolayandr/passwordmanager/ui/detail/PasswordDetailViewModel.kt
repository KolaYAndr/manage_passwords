package com.kolayandr.passwordmanager.ui.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolayandr.passwordmanager.data.model.Password
import com.kolayandr.passwordmanager.data.repository.PasswordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PasswordDetailViewModel(
    private val repository: PasswordRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val passwordId: String? = savedStateHandle["passwordId"]

    private val _screenMode = MutableStateFlow(
        if (passwordId == null) ScreenMode.EDIT else ScreenMode.VIEW
    )
    val screenMode: StateFlow<ScreenMode> = _screenMode

    private val _uiState = MutableStateFlow(PasswordDetailState())
    val uiState: StateFlow<PasswordDetailState> = _uiState

    init {
        loadPassword()
    }

    private fun loadPassword() {
        viewModelScope.launch {
            passwordId?.let { id ->
                repository.getPasswordById(id)?.let { password ->
                    _uiState.value = _uiState.value.copy(
                        itemName = password.itemName,
                        website = password.website,
                        username = password.username,
                        password = password.encryptedPassword,
                        notes = password.notes
                    )
                }
            }
        }
    }


    fun copyToClipboard(text: String, context: Context) {
        if (text.isNotBlank()) {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("password", text)
            clipboardManager.setPrimaryClip(clip)
        }
    }


    fun switchToEditMode() {
        _screenMode.value = ScreenMode.EDIT
    }

    fun updateItemName(itemName: String) {
        _uiState.value = _uiState.value.copy(itemName = itemName)
    }

    fun updateWebsite(website: String) {
        _uiState.value = _uiState.value.copy(website = website)
    }

    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun updateNotes(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }

    fun savePassword(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val password = Password(
                id = passwordId,
                itemName = currentState.itemName,
                website = currentState.website,
                username = currentState.username,
                encryptedPassword = currentState.password,
                notes = currentState.notes
            )

            try {
                if (passwordId == null) {
                    repository.savePassword(password)
                } else {
                    repository.updatePassword(password)
                }
                // После сохранения переходим в режим просмотра
                _screenMode.value = ScreenMode.VIEW
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Ошибка сохранения"
                )
            }
        }
    }

    // Проверяем, можно ли выйти без сохранения
    fun canExitWithoutSaving(): Boolean {
        return passwordId != null || // Существующий пароль - можно выйти в режиме VIEW
                uiState.value.itemName.isBlank() &&
                uiState.value.website.isBlank() &&
                uiState.value.username.isBlank() &&
                uiState.value.password.isBlank()
    }

    // Метод для отмены редактирования
    fun cancelEdit() {
        if (passwordId == null) {
            // Для нового пароля - ничего не делаем
        } else {
            // Для существующего - возвращаемся в режим просмотра
            _screenMode.value = ScreenMode.VIEW
            loadPassword() // Загружаем оригинальные данные
        }
    }
}

data class PasswordDetailState(
    val itemName: String = "",
    val website: String = "",
    val username: String = "",
    val password: String = "",
    val notes: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class ScreenMode {
    VIEW,  // Режим просмотра
    EDIT   // Режим редактирования/создания
}