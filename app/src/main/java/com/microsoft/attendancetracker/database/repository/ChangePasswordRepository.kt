package com.microsoft.attendancetracker.database.repository

import com.microsoft.attendancetracker.data.SessionManager
import com.microsoft.attendancetracker.database.dao.UserDao

class ChangePasswordRepository(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) {

    fun getLoggedInEmail(): String? {
        return sessionManager.getLoginEmail()
    }

    fun saveLoggedInEmail(email: String) {
        sessionManager.saveLoginEmail(email)
    }

    fun logout() {
        sessionManager.clearSession()
    }

    suspend fun verifyCurrentPassword(email: String, currentPw: String): Boolean {
        val user = userDao.verifyCurrentPassword(email, currentPw)
        return user != null
    }

    suspend fun updatePassword(email: String, newPassword: String): Boolean {
        return userDao.updatePassword(email, newPassword) > 0
    }

}