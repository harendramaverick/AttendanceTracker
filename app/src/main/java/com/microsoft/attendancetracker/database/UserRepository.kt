package com.microsoft.attendancetracker.database

open class UserRepository(private val userDao: UserDao?) {

    open suspend fun createAccount(fullName: String, email: String, password: String) {
        val user = UserEntity(
            fullName = fullName,
            email = email,
            password = password
        )
        userDao?.insertUser(user)
    }

    suspend fun getUser(email: String): UserEntity? {
        return userDao?.getUserByEmail(email)
    }
}
