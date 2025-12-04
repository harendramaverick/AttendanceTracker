package com.microsoft.attendancetracker.component

import android.app.Activity
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.microsoft.attendancetracker.AttendanceActivity
import com.microsoft.attendancetracker.AttendanceReportActivity
import com.microsoft.attendancetracker.DashboardActivity
import com.microsoft.attendancetracker.R
import com.microsoft.attendancetracker.SettingsActivity

@Composable
fun BottomNavBar(index : Int) {
    val context = LocalContext.current
    val selected = MutableList(4) { false }
    selected[index - 1] = true
    val activity = LocalContext.current as? Activity
    NavigationBar {

        NavigationBarItem(
            selected = selected.get(0),
            onClick = {
                activity?.finish()
                val intent = Intent(context, DashboardActivity::class.java)
                context.startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            },
            icon = { Icon(Icons.Default.Home, "") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selected.get(1),
            onClick = {
                activity?.finish()
                val intent = Intent(context, AttendanceReportActivity::class.java)
                context.startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

            },
            icon = { Icon(Icons.Default.BarChart, "") },
            label = { Text("Reports") }
        )
        NavigationBarItem(
            selected = selected.get(2),
            onClick = {
                activity?.finish()
                val intent = Intent(context, AttendanceActivity::class.java)
                context.startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            },
            icon = { Icon(Icons.Default.Person, "") },
            label = { Text("Students") }
        )
        NavigationBarItem(
            selected = selected.get(3),
            onClick = {
                activity?.finish()
                val intent = Intent(context, SettingsActivity::class.java)
                context.startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            },
            icon = { Icon(Icons.Default.Settings, "") },
            label = { Text("Settings") }
        )
    }
}