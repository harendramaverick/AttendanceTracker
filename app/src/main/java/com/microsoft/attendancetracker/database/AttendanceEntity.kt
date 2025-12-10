package com.microsoft.attendancetracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_records")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: String,         // e.g., "2024-07-24"
    val checkInTime: Long?,   // epoch millis
    val checkOutTime: Long?   // epoch millis
)
