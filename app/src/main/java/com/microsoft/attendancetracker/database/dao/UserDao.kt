package com.microsoft.attendancetracker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.microsoft.attendancetracker.database.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String?): UserEntity?

    @Query("UPDATE users SET password = :newPassword WHERE email = :email")
    suspend fun updatePassword(email: String, newPassword: String): Int

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun verifyCurrentPassword(email: String, password: String): UserEntity?
}