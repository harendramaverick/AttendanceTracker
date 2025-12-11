package com.microsoft.attendancetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microsoft.attendancetracker.database.entity.UserEntity
import com.microsoft.attendancetracker.database.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



open class LoginViewModel(private val repository: UserRepository?) : ViewModel() {

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

    private val _loginError = MutableStateFlow("")
    val loginError = _loginError.asStateFlow()

    val _record             =   MutableStateFlow<UserEntity?>(null)
    val record              =   _record.asStateFlow()

    fun getUserRecord(email: String?)
    {
      viewModelScope.launch {
          _record.value = repository?.getUser(email)
     }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = repository?.getUser(email)
            if (user != null && user.password == password) {
                _loginSuccess.value = true
                repository.sessionManager.saveLoginEmail(email)
            } else {
                _loginError.value = "Invalid email or password"
            }
        }
    }
}
