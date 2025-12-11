package com.microsoft.attendancetracker.database.repository

import com.microsoft.attendancetracker.data.SessionManager
import com.microsoft.attendancetracker.database.dao.AttendanceDao
import com.microsoft.attendancetracker.database.entity.AttendanceEntity

class AttendanceRepository(private val dao: AttendanceDao, val sessionManager: SessionManager) {

    suspend fun getRecord(date: String) = dao.getRecordForDate(date, sessionManager.getLoginEmail())

    suspend fun saveCheckIn(date: String, time: Long) {
        val existing = dao.getRecordForDate(date, sessionManager.getLoginEmail())

        if (existing == null) {
            dao.insertRecord(
                AttendanceEntity(
                    email = sessionManager.getLoginEmail(),
                    date = date,
                    checkInTime = time,
                    checkOutTime = null
                )
            )
        } else {
            dao.updateRecord(existing.copy(checkInTime = time))
        }
    }

    suspend fun saveCheckOut(date: String, time: Long) {
        val existing = dao.getRecordForDate(date, sessionManager.getLoginEmail())

        if (existing != null) {
            dao.updateRecord(existing.copy(checkOutTime = time))
        }
    }
}