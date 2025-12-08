package com.microsoft.attendancetracker.component

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.microsoft.attendancetracker.LoginActivity


fun Logout(context: Context) {
    val activity = context as? Activity
    val intent = Intent(context, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
    activity?.finish()
}