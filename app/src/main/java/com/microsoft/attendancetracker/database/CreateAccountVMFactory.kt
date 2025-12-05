package com.microsoft.attendancetracker.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.microsoft.attendancetracker.viewmodel.CreateAccountViewModel

class CreateAccountVMFactory(private val repo: UserRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateAccountViewModel(repo) as T
    }
}
