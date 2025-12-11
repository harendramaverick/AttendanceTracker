package com.microsoft.attendancetracker.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeManager(context: Context) {
    private val prefs = context.getSharedPreferences("theme_session", Context.MODE_PRIVATE)

    private val _themeFlow = MutableStateFlow(getThemeType())
    val themeFlow = _themeFlow.asStateFlow()

    fun setThemeType(theme: Boolean) {
        prefs.edit().putBoolean("theme", theme).apply()
        _themeFlow.value = theme
    }

    fun getThemeType(): Boolean {
        return prefs.getBoolean("theme", false)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}