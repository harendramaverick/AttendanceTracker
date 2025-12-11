package com.microsoft.attendancetracker.database.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.microsoft.attendancetracker.database.repository.ChangePasswordRepository
import com.microsoft.attendancetracker.viewmodel.AuthViewModel

class ChangePasswordVMlFactory(
    private val repository: ChangePasswordRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}