package com.microsoft.attendancetracker.database.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.microsoft.attendancetracker.database.repository.UserRepository
import com.microsoft.attendancetracker.viewmodel.LoginViewModel

class LoginVMFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}