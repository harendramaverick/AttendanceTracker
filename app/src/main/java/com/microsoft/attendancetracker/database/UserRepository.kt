package com.microsoft.attendancetracker.database

import com.microsoft.attendancetracker.model.SessionManager

open class UserRepository(private val userDao: UserDao?, val sessionManager: SessionManager) {

    open suspend fun createAccount(fullName: String, email: String, password: String) {
        val user = UserEntity(
            fullName = fullName,
            email = email,
            password = password
        )
        userDao?.insertUser(user)
    }

    suspend fun getUser(email: String?): UserEntity? {
        return userDao?.getUserByEmail(email)
    }

    suspend fun verifyCurrentPassword(email: String, currentPw: String): Boolean {
        val user = userDao?.getUserByEmail(email)
        return user?.password == currentPw
    }

    suspend fun updatePassword(email: String, newPassword: String): Boolean {
        return userDao?.updatePassword(email, newPassword)!! > 0
    }
}
