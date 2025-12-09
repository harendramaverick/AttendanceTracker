package com.microsoft.attendancetracker.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microsoft.attendancetracker.database.AuthRepository
import kotlinx.coroutines.launch

open class AuthViewModel(private val repository: AuthRepository?) : ViewModel() {

    // UI state
    var isSuccess by mutableStateOf<Boolean?>(null)
    var message by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    open fun getLoggedInEmail(): String? {
        return repository?.getLoggedInEmail()
    }

    open fun saveLoggedInEmail(email: String) {
        repository?.saveLoggedInEmail(email)
    }

    open fun logout() {
        repository?.logout()
    }

    open fun changePassword(currentPw: String, newPw: String) {
        val email = repository?.getLoggedInEmail()

        if (email == null) {
            isSuccess = false
            message = "Session expired. Please log in again."
            return
        }

        viewModelScope.launch {
            isLoading = true

            // Step 1 – verify current password
            val isValid = repository.verifyCurrentPassword(email, currentPw)

            isValid?.let {
                if (!it) {
                    isLoading = false
                    isSuccess = false
                    message = "Current password is incorrect"
                    return@launch
                }
            }

            // Step 2 – update password
            val updated = repository.updatePassword(email, newPw)

            isLoading = false

            if (updated == true) {
                isSuccess = true
                message = "Password updated successfully"
            } else {
                isSuccess = false
                message = "Failed to update password"
            }
        }
    }
}
