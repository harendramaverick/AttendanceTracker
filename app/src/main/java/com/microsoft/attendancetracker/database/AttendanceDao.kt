package com.microsoft.attendancetracker.database

import androidx.room.*

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: AttendanceEntity)

    @Update
    suspend fun updateRecord(record: AttendanceEntity)

    @Query("SELECT * FROM attendance_records WHERE date = :date LIMIT 1")
    suspend fun getRecordForDate(date: String): AttendanceEntity?
}
