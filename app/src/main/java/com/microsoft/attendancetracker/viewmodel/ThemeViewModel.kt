package com.microsoft.attendancetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.microsoft.attendancetracker.model.ThemePreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    val isDarkTheme: StateFlow<Boolean> =
        ThemePreferences.isDarkTheme(context)
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun toggleTheme() {
        viewModelScope.launch {
            ThemePreferences.setDarkTheme(context, !isDarkTheme.value)
        }
    }

    fun setDarkTheme(value: Boolean) {
        viewModelScope.launch {
            ThemePreferences.setDarkTheme(context, value)
        }
    }
}
