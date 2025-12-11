package com.microsoft.attendancetracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.microsoft.attendancetracker.database.dao.AttendanceDao
import com.microsoft.attendancetracker.database.dao.UserDao
import com.microsoft.attendancetracker.database.entity.AttendanceEntity
import com.microsoft.attendancetracker.database.entity.UserEntity

@Database(entities = [UserEntity::class, AttendanceEntity::class], version = 3, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun AttendanceDao(): AttendanceDao
}
