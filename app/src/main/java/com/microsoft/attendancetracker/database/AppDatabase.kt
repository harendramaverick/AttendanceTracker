package com.microsoft.attendancetracker.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, AttendanceEntity::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun AttendanceDao(): AttendanceDao
}
