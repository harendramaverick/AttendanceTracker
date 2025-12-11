package com.microsoft.attendancetracker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.microsoft.attendancetracker.database.entity.AttendanceEntity

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertRecord(record: AttendanceEntity)

    @Update
    suspend fun updateRecord(record: AttendanceEntity)

    @Query("SELECT * FROM attendance_records WHERE date = :date and email = :email LIMIT 1")
    suspend fun getRecordForDate(date: String, email: String?): AttendanceEntity?
}