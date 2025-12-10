package com.microsoft.attendancetracker.database

class AttendanceRepository(private val dao: AttendanceDao) {

    suspend fun getRecord(date: String) = dao.getRecordForDate(date)

    suspend fun saveCheckIn(date: String, time: Long) {
        val existing = dao.getRecordForDate(date)

        if (existing == null) {
            dao.insertRecord(
                AttendanceEntity(
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
        val existing = dao.getRecordForDate(date)

        if (existing != null) {
            dao.updateRecord(existing.copy(checkOutTime = time))
        }
    }
}
