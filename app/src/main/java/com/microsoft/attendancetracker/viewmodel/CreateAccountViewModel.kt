package com.microsoft.attendancetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microsoft.attendancetracker.database.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class CreateAccountViewModel(private val repo: UserRepository?) : ViewModel() {

    private val _success = MutableStateFlow(false)
    val success = _success.asStateFlow()

    open fun createAccount(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            repo?.createAccount(fullName, email, password)
            _success.value = true
        }
    }
}