package com.microsoft.attendancetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microsoft.attendancetracker.database.entity.AttendanceEntity
import com.microsoft.attendancetracker.database.repository.AttendanceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

open class AttendanceViewModel(private val repository: AttendanceRepository?) : ViewModel() {
    private val dateFormat  =   SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat  =   SimpleDateFormat("hh:mm a", Locale.getDefault())
    private val today       =   dateFormat.format(Date())
    val _record             =   MutableStateFlow<AttendanceEntity?>(null)
    val record              =   _record.asStateFlow()

    init {
        loadTodayRecord()
    }

    open fun loadTodayRecord() {
        viewModelScope.launch {
            _record.value = repository?.getRecord(today)
        }
    }

    open fun checkIn() {
        viewModelScope.launch {
            repository?.saveCheckIn(today, System.currentTimeMillis())
            loadTodayRecord()
        }
    }

    open fun checkOut() {
        viewModelScope.launch {
            repository?.saveCheckOut(today, System.currentTimeMillis())
            loadTodayRecord()
        }
    }

    open fun format(time: Long?): String =
        if (time == null) "--" else timeFormat.format(Date(time))
}
