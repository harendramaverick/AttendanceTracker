package com.microsoft.attendancetracker.data


import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveLoginEmail(email: String) {
        prefs.edit().putString("email", email).apply()
    }

    fun getLoginEmail(): String? {
        return prefs.getString("email", null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
